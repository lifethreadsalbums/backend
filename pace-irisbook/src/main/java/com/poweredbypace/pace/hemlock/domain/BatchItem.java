package com.poweredbypace.pace.hemlock.domain;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.util.PACEUtils;

public class BatchItem {

	private Product product;
	private ExpressionEvaluator evaluator;
	private GenericRuleService ruleService;
	private ProductContext productContext;
	private DecimalFormat format = new DecimalFormat("$#0.00");
	private DecimalFormat scaleFormat = new DecimalFormat("#.#%");
	private IrisProduct irisProduct;
	private Batch batch;
	private Invoice invoice;
	
	public BatchItem(Product product, ExpressionEvaluator evalutor, GenericRuleService ruleService) {
		this.product = product;
		this.evaluator = evalutor;
		this.ruleService = ruleService;
		this.productContext = new ProductContext(product);
		this.irisProduct = new IrisProduct(product);
	}
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	
	public Product getProduct() {
		return product;
	}

	public Integer getSheets() {
		return eval("IRIS_SHEET_COUNT", Integer.class, 0);
	}
	
	public String getGrain () {
		return eval("IRIS_GRAIN", String.class, "");
	}
	
	public Integer getNumPages() {
		List<Integer> pages = null;
		if (product.isReprint()) {
			pages = PACEUtils.getReprintPages(product);
		}
		return pages!=null ? pages.size() : product.getPageCount();
	}
	
	public Integer getNumSets() {
		return product.getQuantity();
	}
	
	public Integer getTotalPages() {
		return getNumPages() * getNumSets();
	}
		
	public float getRate() {
		return eval("IRIS_RATE", Float.class, 0f);
	}
	
	public String getRateFormatted() {
		float rate = getRate();
		if (rate==0.0f) return "N/C";
		return format.format(rate);
	}
	
	public float getTotal() {
		return getSheets() * getRate();
	}
	
	public String getTotalFormatted() {
		float total = getTotal();
		return format.format(total);
	}
	
	public String getScale() {
		double scale = 1;
		return scaleFormat.format(scale);
	}
	
	public Double getSortOrder() {
		return eval("IRIS_SORT_ORDER", Double.class, 0d);
	}
	
	public String getSingleSided() {
		return product.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased ? "1/S" : "";
	}
	
	public String getProductLine() {
		return irisProduct.getProductLineLabel();
	}
	
	public String getShape() {
		return irisProduct.getShapeCode();
	}
	
	public String getMaterial() {
		return irisProduct.getBookMaterialLabel();
	}
	
	public String getPaper() {
		return irisProduct.getPaperTypeLabel();
	}
	
	public String getBatchNumber() {
		if (this.batch!=null)
			return this.batch.getName();
		
		Batch batch = product.getBatch();
		if (batch==null)
			batch = product.getParent().getBatch();
		return batch.getName();
	}
	
	public Date getDatePrinted() {
		Batch batch = product.getBatch();
		if (batch==null) return null;
		return batch.getDatePrinted();
	}
	
	public String getGroup() {
		return eval("IRIS_BATCH_GROUP", String.class, "");
	}
	
	private <T> T eval(String code, Class<T> clazz, T defaultValue) {
		try {
			GenericRule rule = ruleService.findRule(product, code);
			return evaluator.evaluate(productContext, rule.getJsonData(), clazz);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
