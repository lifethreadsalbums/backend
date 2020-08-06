package com.poweredbypace.pace.payment.payeezy;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.TransactionLogEntry;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.payment.PaymentException;
import com.poweredbypace.pace.payment.PaymentGateway;
import com.poweredbypace.pace.payment.PaymentGatewayConfiguration;
import com.poweredbypace.pace.repository.TransactionLogEntryRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.UserService;

public class PayeezyPaymentGateway implements PaymentGateway {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private GenericRuleService ruleService;

	@Autowired
	private TransactionLogEntryRepository transactionLogEntryRepo;

	public PayeezyPaymentGateway() {
	}

	private String strVal(String val, int maxLen) {
		if (val==null) return "''";
		val = Normalizer.normalize(val, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		return "'" + StringEscapeUtils.escapeJavaScript(StringUtils.left(val, maxLen)) + "'";
	}
	
	@Override
	public PaymentGatewayConfiguration getConfiguration() {

		PayeezyConfig cfg = ruleService.getRuleValue("PAYEEZY_CONFIG", PayeezyConfig.class);

		PaymentGatewayConfiguration config = new PaymentGatewayConfiguration();

		User user = userService.getCurrentUser();
		Order order = orderService.getCart(user);

		config.setHttpMethod("POST");
		config.setGatewayUrl(cfg.getGatewayUrl());

		String x_amount = order.getTotal().getAmount().toString();
		String x_currency_code = order.getTotal().getCurrency();
		
		// get payment page ID and transaction key by currency
		String x_login = cfg.getPaymentPageIds().get(x_currency_code); 
		String transactionKey = cfg.getTransactionKeys().get(x_currency_code);

		// Generate a random sequence number
		Random generator = new Random();
		int x_fp_sequence = generator.nextInt(1000);

		// Generate the timestamp
		// Make sure this will be in UTC
		long x_fp_timestamp = System.currentTimeMillis() / 1000;

		// Use Java Cryptography functions to generate the x_fp_hash value
		// generate secret key for HMAC-SHA1 using the transaction key
		SecretKey key = new SecretKeySpec(transactionKey.getBytes(), "HmacSHA1");

		// Get instance of Mac object implementing HMAC-SHA1, and
		// Initialize it with the above secret key
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		}

		// process the input string
		String inputstring = x_login + "^" + x_fp_sequence + "^" + x_fp_timestamp + "^" + x_amount + "^"
				+ x_currency_code;
		byte[] result = mac.doFinal(inputstring.getBytes());

		// convert the result from byte[] to hexadecimal format
		StringBuffer strbuf = new StringBuffer(result.length * 2);
		for (int i = 0; i < result.length; i++) {
			if (((int) result[i] & 0xff) < 0x10)
				strbuf.append("0");
			strbuf.append(Long.toString((int) result[i] & 0xff, 16));
		}
		String x_fp_hash = strbuf.toString();

		Map<String, String> form = config.getFormFields();
		form.put("x_login", "'" + x_login + "'");
		form.put("x_fp_sequence", "'" + x_fp_sequence + "'");
		form.put("x_fp_timestamp", "'" + x_fp_timestamp + "'");
		form.put("x_fp_hash", "'" + x_fp_hash + "'");
		form.put("x_currency_code", "'" + x_currency_code + "'");

		form.put("x_cust_id", "order.user.id");
		form.put("x_invoice_num", "order.orderNumber");
		form.put("x_po_num", "order.id");

		if (order.getBillingAddress()!=null) {
			form.put("x_first_name", strVal(order.getBillingAddress().getFirstName(), 30));
			form.put("x_last_name", strVal(order.getBillingAddress().getLastName(), 30));
	
			form.put("x_company", strVal(order.getBillingAddress().getCompanyName(), 30));
			form.put("x_address", strVal(order.getBillingAddress().getAddressLine1(), 30));
			form.put("x_city", strVal(order.getBillingAddress().getCity(), 20));
			form.put("x_state", strVal(order.getBillingAddress().getState().getName(), 20));
			form.put("x_zip", strVal(order.getBillingAddress().getZipCode(), 10));
			form.put("x_country", strVal(order.getBillingAddress().getCountry().getName(), 20));
		}

		if (order.getShippingAddress()!=null) {
			form.put("x_ship_to_first_name", strVal(order.getShippingAddress().getFirstName(), 30));
			form.put("x_ship_to_last_name", strVal(order.getShippingAddress().getLastName(), 30));
			form.put("x_ship_to_company", strVal(order.getShippingAddress().getCompanyName(), 30));
			form.put("x_ship_to_address", strVal(order.getShippingAddress().getAddressLine1(), 30));
			form.put("x_ship_to_city", strVal(order.getShippingAddress().getCity(), 20));
			form.put("x_ship_to_state", strVal(order.getShippingAddress().getState().getName(), 20));
			form.put("x_ship_to_zip", strVal(order.getShippingAddress().getZipCode(), 10));
			form.put("x_ship_to_country", strVal(order.getShippingAddress().getCountry().getName(), 20));
			form.put("x_phone", strVal(order.getUser().getPhone(), 14));
		}

		form.put("x_tax", "order.taxes[0].tax.amount");
		form.put("alternate_tax", "order.taxes[1].tax.amount");
		form.put("x_freight", "order.shippingCost.amount");

		form.put("x_amount", "'" + x_amount + "'");
		form.put("x_show_form", "'PAYMENT_FORM'");

		if (cfg.getSoftDescriptors().containsKey(x_currency_code)) {
			SoftDescriptorInfo sd = cfg.getSoftDescriptors().get(x_currency_code);
			// add soft descriptor
			form.put("x_sd_dba_name", "'" + sd.getDbaName() + "'");
			form.put("x_sd_merchant_contact_info", "'" + sd.getMerchantContactInfo() + "'");
			form.put("x_sd_street", "'" + sd.getStreet()+ "'");
			form.put("x_sd_city", "'" + sd.getCity()+ "'");
			form.put("x_sd_region", "'" + sd.getRegion()+ "'");
			form.put("x_sd_country_code", "'" + sd.getCountryCode()+ "'");
			form.put("x_sd_postal_code", "'" + sd.getPostalCode()+ "'");
		}

		return config;
	}

	@Override
	public Order handlePaymentCompleteRequest(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("x_po_num");
		if (orderId == null) {
			logTransactionInfo(null, "PAYEEZY_ERROR", getParams(request));
			paymentError(null, "Order not found.");
		}

		Order order = orderService.get(Long.parseLong(orderId));

		String reason = request.getParameter("x_response_reason_text");
		String code = request.getParameter("x_response_code");
		// String ctr = request.getParameter("exact_ctr");

		if ("1".equals(code)) {
			orderService.setOrderState(order, OrderState.PaymentComplete);
			logTransactionInfo(order, "PAYEEZY_PAYMENT_COMPLETE", getParams(request));
		} else if ("2".equals(code)) {
			// Payment failed.
			logTransactionInfo(order, "PAYEEZY_PAYMENT_FAILED", getParams(request));
			paymentError(order, reason);
		} else if ("3".equals(code)) {
			// An error occurred while processing payment.
			logTransactionInfo(order, "PAYEEZY_ERROR", getParams(request));
			paymentError(order, reason);
		}

		return order;
	}

	@Override
	public void handlePaymentErrorRequest(HttpServletRequest request, HttpServletResponse response) {

	}

	private void paymentError(Order order, String message) {
		throw new PaymentException("Payment error: " + message);
	}

	@SuppressWarnings("rawtypes")
	private String getParams(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		Map params = request.getParameterMap();
		Iterator i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			String value = ((String[]) params.get(key))[0];
			builder.append(key + "=" + value + "\n");
		}
		return builder.toString();
	}

	private void logTransactionInfo(Order order, String type, String message) {
		try {
			TransactionLogEntry entry = new TransactionLogEntry();
			entry.setUser(userService.getCurrentUser());
			entry.setDate(new Date());
			entry.setOrder(order);
			entry.setType(type);
			entry.setMessage(message);
			transactionLogEntryRepo.save(entry);
		} catch (Exception ex) {
			log.error("Cannot save TransactionLogEntry", ex);
		}
	}
}
