package com.poweredbypace.pace.payment.psi;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.TransactionLogEntry;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.payment.PaymentException;
import com.poweredbypace.pace.payment.PaymentGateway;
import com.poweredbypace.pace.payment.PaymentGatewayConfiguration;
import com.poweredbypace.pace.repository.TransactionLogEntryRepository;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.SpringContextUtil;

public class PsiGate implements PaymentGateway {
	
	private final Log log = LogFactory.getLog(PsiGate.class);
	
	private Map<String,String> storeKeys; //store keys per currency
	private String thanksUrl;
	private String noThanksUrl;
	private String gatewayUrl;
	private String verifyApiUrl;
	private String storeID;
	private String passphrase;
	private boolean useVerifyApi = true; 
	
	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public Map<String, String> getStoreKeys() {
		return storeKeys;
	}

	public void setStoreKeys(Map<String, String> storeKeys) {
		this.storeKeys = storeKeys;
	}

	public String getThanksUrl() {
		return thanksUrl;
	}

	public void setThanksUrl(String thanksUrl) {
		this.thanksUrl = thanksUrl;
	}

	public String getNoThanksUrl() {
		return noThanksUrl;
	}

	public void setNoThanksUrl(String noThanksUrl) {
		this.noThanksUrl = noThanksUrl;
	}
	
	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
	
	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	
	public String getVerifyApiUrl() {
		return verifyApiUrl;
	}

	public void setVerifyApiUrl(String verifyApiUrl) {
		this.verifyApiUrl = verifyApiUrl;
	}

	public boolean isUseVerifyApi() {
		return useVerifyApi;
	}

	public void setUseVerifyApi(boolean useVerifyApi) {
		this.useVerifyApi = useVerifyApi;
	}



	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrencyManager currencyManager;
	
	@Autowired
	private TransactionLogEntryRepository transactionLogEntryRepo;
	
	
	@Override
	public Order handlePaymentCompleteRequest(HttpServletRequest request,
			HttpServletResponse response) {
		
		Order order = findOrder(request);
		logTransactionInfo(order, "PSI_PREAUTH_COMPLETE", getParams(request));
		
		if (order==null) {
			paymentError(null, "Order not found.");
		}
		
		if (useVerifyApi) {
			verifyPayment(order, request);
		}
		
		orderService.setOrderState(order, OrderState.PaymentComplete);
		return order;
	}

	@Override
	public void handlePaymentErrorRequest(HttpServletRequest request,
			HttpServletResponse response) {
		
		log.error("PSI GATE Error. " + getParams(request));
		Order order = findOrder(request);
		
		logTransactionInfo(order, "PSI_PAYMENT_ERROR", getParams(request));
		String errorMessage = request.getParameter("ErrMsg");
		
		String avsResult = request.getParameter("AVSResult");
		if ("A".equals(avsResult) || "N".equals(avsResult))
			errorMessage = "Please check and verify that your billing address is correct in order to check out.";
		
		if (errorMessage==null) {
			errorMessage = request.getParameter("ReturnCode");
		}
		throw new PaymentException(errorMessage);
	}

	@Override
	public PaymentGatewayConfiguration getConfiguration() {
		PaymentGatewayConfiguration config = new PaymentGatewayConfiguration();
		
		config.getFormFields().put("OrderID", "order.orderNumber");
		config.getFormFields().put("StoreKey", "'" + getStoreKey() + "'");
		config.getFormFields().put("ResponseFormat", "'HTML'");
		config.getFormFields().put("ThanksURL", "'" + thanksUrl + "'");
		config.getFormFields().put("NoThanksURL", "'" + noThanksUrl + "'");
		
		config.getFormFields().put("CustomerRefNo", "order.user.id");
		config.getFormFields().put("PaymentType", "'CC'");
		config.getFormFields().put("CardAction", "'0'"); //PreAuth
		config.getFormFields().put("TestResult", "");
		config.getFormFields().put("UserID", "order.user.id");
		config.getFormFields().put("Bname", "order.billingAddress.firstName + ' ' + order.billingAddress.lastName");
		config.getFormFields().put("Bcompany","order.billingAddress.companName");
		config.getFormFields().put("Baddress1", "order.billingAddress.addressLine1");
		config.getFormFields().put("Baddress2", "order.billingAddress.addressLine2");
		config.getFormFields().put("Bcity" ,"order.billingAddress.city");
		config.getFormFields().put("Bprovince", "order.billingAddress.state.name");
		config.getFormFields().put("Bpostalcode", "order.billingAddress.zipCode");
		config.getFormFields().put("Bcountry", "order.billingAddress.country.name");
		config.getFormFields().put("Sname", "order.shippingAddress.firstName + ' ' + order.shippingAddress.lastName");
		config.getFormFields().put("Scompany", "order.shippingAddress.companyName");
		config.getFormFields().put("Saddress1", "order.shippingAddress.addressLine1");
		config.getFormFields().put("Saddress2", "order.shippingAddress.addressLine2");
		config.getFormFields().put("Scity", "order.shippingAddress.city");
		config.getFormFields().put("Sprovince", "order.shippingAddress.state.name");
		config.getFormFields().put("Spostalcode", "order.shippingAddress.zipCode");
		config.getFormFields().put("Scountry", "order.shippingAddress.country.name");
		config.getFormFields().put("Phone", "user.phone");
		config.getFormFields().put("Email", "order.user.email");
		//config.getFormFields().put("Comments", "'No comments today'");
		
		config.getFormFields().put("Tax1", "order.taxes[0].tax.amount");
		config.getFormFields().put("Tax2", "order.taxes[1].tax.amount");
		config.getFormFields().put("Tax3", "order.taxes[2].tax.amount");
		config.getFormFields().put("Tax4", "order.taxes[3].tax.amount");
		config.getFormFields().put("Tax5", "order.taxes[4].tax.amount");
		config.getFormFields().put("ShippingTotal", "order.shippingCost.amount");
		
		config.getFormFields().put("SubTotal", "order.subtotal.amount");
		config.setGatewayUrl(gatewayUrl);
		
		return config;
	}
	
	private void paymentError(Order order, String message) {
		log.error(message);
		logTransactionInfo(order, "PSI_ERROR", message);
		throw new PaymentException(message);
	}
	
	private Order findOrder(HttpServletRequest request) {
		String orderId = request.getParameter("OrderID");
		if (orderId==null)
			return null;
		
		Order order = orderService.getByOrderNumber(orderId);
		return order;
	}
	
	private void verifyPayment(Order order, HttpServletRequest httpRequest) {		
		log.info("PSI GATE Payment Complete request received");
		System.setProperty("https.protocols", "TLSv1.2");
		String orderId = httpRequest.getParameter("OrderID");
		
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters); 
		
		PsiVerifyOrderRequest req = new PsiVerifyOrderRequest();
		req.StoreID = getStoreID();
		req.PassPhrase = getPassphrase();
		req.OrderID = orderId;
		
		PsiVerifyOrderResponse res = restTemplate.postForObject(getVerifyApiUrl(), 
				req, PsiVerifyOrderResponse.class);
		
		if (res==null) {
			paymentError(order, "Error while verifying payment");
		} else if (!StringUtils.equals("CAPTURED", res.OrderStatus)) {
			String error = res.ResponseCode + ". " + res.ResponseMsg;
			paymentError(order, error);
		}
		
		log.info("Verify order response code:" + res.ResponseCode + ", order status=" + res.OrderStatus);
	}
	
	private String getStoreKey() {
		Store store = SpringContextUtil.getEnv().getStore();
		Currency currency = currencyManager.getCurrency(store);
		if (!storeKeys.containsKey(currency.getCurrencyCode()))
			throw new IllegalStateException("No store key specified for currency " + currency.getCurrencyCode());
		
		return storeKeys.get(currency.getCurrencyCode());
	}
	
	@SuppressWarnings("rawtypes")
	private String getParams(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		Map params = request.getParameterMap();
		Iterator i = params.keySet().iterator();
		while ( i.hasNext() ) {
			String key = (String) i.next();		
			String value = ((String[]) params.get( key ))[ 0 ];
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
