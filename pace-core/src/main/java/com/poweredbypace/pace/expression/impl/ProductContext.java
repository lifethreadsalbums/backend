package com.poweredbypace.pace.expression.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.ScriptableObject;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.TProductOptionValue;
import com.poweredbypace.pace.domain.layout.CameoSetElement;
import com.poweredbypace.pace.domain.layout.ImageStampElement;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.manager.ProductManager;
import com.poweredbypace.pace.util.HibernateUtil;
import com.poweredbypace.pace.util.PACEUtils;
import com.poweredbypace.pace.util.SpringContextUtil;

public class ProductContext extends HashMap<String, Object> implements ExpressionContext {
	
	private static final long serialVersionUID = 1192552659210668417L;
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ProductContext.class);
	private Product product;
	
	private static String getPropertyName(ProductOption<?> productOption) {
		PrototypeProductOption prototypeOption = SpringContextUtil.getProductManager()
				.getPrototypeProductOption(productOption.getPrototypeProductOption().getId());
		
		String propertyName = prototypeOption.getEffectiveCode();
		return propertyName;
	}
	
	private static ValueLabel getValueLabel(ProductOption<?> productOption) {
		Object value = null;
		String label = null;
		
		if(productOption.getClass().isAssignableFrom(ProductOptionValue.class)) {
			ProductOptionValue productOptionValue = (ProductOptionValue) productOption;
			
			
			if (productOptionValue.getValue()!=null) {
				PrototypeProductOptionValue prototypeOptionValue = SpringContextUtil.getProductManager()
						.getPrototypeProductOptionValue(productOptionValue.getValue().getId());
				
				value = prototypeOptionValue.getCode();
				label = prototypeOptionValue.getDisplayName();
			} 
			//else 
			//	log.debug(propertyName + " has no value");
		} else {
			value = HibernateUtil.unproxy(productOption.getValue());
			if (value!=null) {
				if (value.getClass().isAssignableFrom(TextStampElement.class)) {
					value = new TextStampValue((TextStampElement) value);
				} else if (value.getClass().isAssignableFrom(ImageStampElement.class)) {
					value = new ImageStampValue((ImageStampElement) value, productOption.getProduct());
				} else if (value.getClass().isAssignableFrom(CameoSetElement.class)) {
					value = new CameoSetValue((CameoSetElement) value);
				}
			}
			label = value!=null ? value.toString() : null;
			
		}
		if (label==null) label = "";
		//log.debug(propertyName + " = " + value);
		
		return new ValueLabel(value, label);
	}
	
	@SuppressWarnings("serial")
	public ProductContext(Product product)
	{
		super();
		this.product = product;
		
		PrototypeProduct prototype = SpringContextUtil.getProductManager()
				.getPrototype(product.getPrototypeProduct().getId());
		
		this.put("productPrototypeName", prototype.getCode());
		this.put("productPrototypeId", prototype.getId());
		this.put("linkLayout", BooleanUtils.isTrue(product.getLinkLayout()));
		
		ScriptableObject optionsJs = new ScriptableObject() {
			@Override public String getClassName() { return null; }
		};
		
		ScriptableObject productJs = new ScriptableObject() {
			@Override public String getClassName() { return null; }
		};
		
		productJs.defineProperty("options", optionsJs, ScriptableObject.READONLY);
		
		for(ProductOption<?> productOption:product.getProductOptions()) {
			String prop = getPropertyName(productOption);
			ValueLabel vl = getValueLabel(productOption);
			optionsJs.defineProperty(prop, vl.get("value"), ScriptableObject.READONLY);
			this.put(prop, vl);
		}
		
		this.put("product", productJs);
		this.put("parent", product.getParent()!=null ? new ParentProduct(product.getParent()) : null);
		this.put("user", product.getUser()!=null ? product.getUser().getEmail() : null);
		this.put("userFullName", product.getUser()!=null ? product.getUser().getFullName() : null);
		this.put("id", product.getId());
		this.put("productNumber", product.getProductNumber());
		this.put("state", product.getState().toString());
		
		int reprintPageCount = 0;
		if (product.isReprint()) {
			List<Integer> pages = PACEUtils.getReprintPages(product);
			if (pages!=null) {
				reprintPageCount = pages.size();
			}
		} 
		this.put("reprintPageCount", reprintPageCount);
		
		boolean firstOrderItem = false;
		int orderItemIndex = 0;
		int numOrderItems = 0;
		long orderId = 0;
		if (product.getOrderItem()!=null) {
			Order order = product.getOrderItem().getOrder();
			int idx = order.getOrderItems().indexOf(product.getOrderItem());
			if (idx==0) firstOrderItem = true;
			orderItemIndex = idx;
			numOrderItems = order.getOrderItems().size();
			orderId = order.getId();
		}
		this.put("numOrderItems", numOrderItems);
		this.put("orderItemIndex", orderItemIndex);
		this.put("firstOrderItem", firstOrderItem);
		this.put("orderId", orderId);
		
		
		String batchNumber = null;
		if (product.getBatch()!=null) {
			batchNumber = product.getBatch().getName();
		}
		this.put("batchNumber", batchNumber);
		
	}
	
	public void setParent(Product product) {
		this.put("parent", new ParentProduct(product));
		if (this.product==product) {
			this.put("linkLayout", true);
		}
	}
	
	public void setDuplicateIndex(int index) {
		this.put("duplicateIndex", index);
	}
	
	public void setFirstNotSample(boolean value) {
		this.put("firstNotSample", value);
	}
	
	
	
	
	private static class ParentProduct extends ScriptableObject {

		private static final long serialVersionUID = -7392272277023982478L;

		@Override
		public String getClassName() {
			return "Product";
		}
		
		public ParentProduct(Product product) {
			PrototypeProduct prototype = SpringContextUtil.getProductManager()
					.getPrototype(product.getPrototypeProduct().getId());
			
			this.defineProperty("productPrototypeName", prototype.getCode(), READONLY);
			this.defineProperty("productPrototypeId", prototype.getId(), READONLY);
			int numDuplicates = -1;
			int numStudioSamples = 0;
			for(Product p:product.getProductAndChildren()) {
				Integer qty = p.getQuantity();
				numDuplicates += qty!=null ? qty.intValue() : 0;
				if (BooleanUtils.isTrue(p.getStudioSample())) 
					numStudioSamples++;
			}
			this.defineProperty("numDuplicates", numDuplicates, READONLY);
			this.defineProperty("numStudioSamples", numStudioSamples, READONLY);
			
			for(ProductOption<?> productOption:product.getProductOptions())
			{
				this.defineProperty(getPropertyName(productOption), getValueLabel(productOption), READONLY);
			}
		}
		
	}
	
	private static class ValueLabel extends ScriptableObject {
		
		private static final long serialVersionUID = -5636098982247974689L;
		
		public ValueLabel(Object value, String label) {
			this.defineProperty("value", value, READONLY);
			this.defineProperty("label", label, READONLY);
		}
		
		@Override
		public String getClassName() {
			return "ValueLabel";
		}
		
	}
	
	private static class CameoSetValue extends ScriptableObject {
		
		private static final long serialVersionUID = -2256542237136162220L;
		
		public CameoSetValue(CameoSetElement el) {
			this.defineProperty("type", "CameoSetElement", READONLY);
			this.defineProperty("code", el.getPositionCode(), READONLY);
			this.defineProperty("numCameos", el.getShapes().size(), READONLY);
			
			if (el.getPositionCode()!=null) {
				TProductOptionValue val = SpringContextUtil.getProductManager()
						.getProductOptionValueByCode(el.getPositionCode());
				if (val!=null) {
					this.defineProperty("label", val.getDisplayName(), READONLY);
				}
			}
		}
		@Override
		public String getClassName() {
			return "CameoSetValue";
		}
		
		@Override
		public String toString() {
			return this.get("label").toString();
		}
		
		
	}
	
	private static class TextStampValue extends ScriptableObject {

		private static final long serialVersionUID = -1239527728960468791L;
		
		private String getTextCase(String text) {
			if (text==null) return "";
			String textCase = "Upper & Lower Case";
			if (text.toLowerCase().equals(text))
				textCase = "Lower Case";
			else if (text.toUpperCase().equals(text))
				textCase = "CAPS";
			return textCase;
		}

		@SuppressWarnings("unchecked")
		public TextStampValue(TextStampElement element) {
			this.defineProperty("type", "TextStampElement", READONLY);
			this.defineProperty("text", element.getText(), READONLY);
			
			String[] textLines = {};
			if (element.getText()!=null)
				textLines = element.getText().split("\\n");
			
			this.defineProperty("textLines", textLines, READONLY);
			this.defineProperty("fontFamily", element.getFontFamily(), READONLY);
			this.defineProperty("fontSize", element.getFontSize(), READONLY);
			if (element.getFoilCode()!=null) {
				this.defineProperty("foil", StringUtils.capitalize(element.getFoilCode()), READONLY);
			}
			
			if (textLines.length>0) {
				this.defineProperty("textCase", getTextCase(textLines[0]), READONLY);
			}
			
			//second line
			if (textLines.length>1) {
				String fontFamily2 = element.getFontFamily();
				float fontSize2 = element.getFontSize()!=null ? element.getFontSize() : 0f;
				
				Map<String,Object> styles = element.getStyles();
				if (styles!=null && styles.containsKey("1")) {
					try {
						Map<String,Object> lineStyles = (Map<String, Object>) styles.get("1");
						Map<String,Object> charStyles = (Map<String, Object>) lineStyles.get("0");
						if (charStyles.containsKey("fontFamily"))
							fontFamily2 = (String) charStyles.get("fontFamily");
						if (charStyles.containsKey("fontSize"))
							fontSize2 = (Integer) charStyles.get("fontSize");
					} catch(Exception ex) {}
				}
				
				this.defineProperty("fontFamily2", fontFamily2, READONLY);
				this.defineProperty("fontSize2", fontSize2, READONLY);
				this.defineProperty("textCase2", getTextCase(textLines[1]), READONLY);
			}
			
			if (textLines.length>2) {
				String fontFamily2 = element.getFontFamily();
				float fontSize2 = element.getFontSize()!=null ? element.getFontSize() : 0f;
				
				Map<String,Object> styles = element.getStyles();
				if (styles!=null && styles.containsKey("2")) {
					try {
						Map<String,Object> lineStyles = (Map<String, Object>) styles.get("2");
						Map<String,Object> charStyles = (Map<String, Object>) lineStyles.get("0");
						if (charStyles.containsKey("fontFamily"))
							fontFamily2 = (String) charStyles.get("fontFamily");
						if (charStyles.containsKey("fontSize"))
							fontSize2 = (Integer) charStyles.get("fontSize");
					} catch(Exception ex) {}
				}
				
				this.defineProperty("fontFamily3", fontFamily2, READONLY);
				this.defineProperty("fontSize3", fontSize2, READONLY);
				this.defineProperty("textCase3", getTextCase(textLines[2]), READONLY);
			}
		}
		
		@Override
		public String getClassName() {
			return "TextStampValue";
		}
		
	}
	
	private static class ImageStampValue extends ScriptableObject {

		private static final long serialVersionUID = -2531778819607767337L;

		public ImageStampValue(ImageStampElement element, Product p) {
			this.defineProperty("type", "ImageStampElement", READONLY);
			this.defineProperty("imageFile", element.getImageFile()!=null ? element.getImageFile().getFilename() : null, READONLY);
			
			boolean firstUse = false;
			ImageFile imageFile = element.getImageFile();
			if (imageFile!=null) {
				ProductManager pm = SpringContextUtil.getApplicationContext().getBean(ProductManager.class);
				List<BigInteger> products = pm.findByImageFileId(imageFile.getId().longValue());
				if (products.size()>0 && products.get(0).longValue()==p.getId().longValue()) {
					Date fakeDate = new GregorianCalendar(2000, 0, 1).getTime();
					Date creationDate = imageFile.getCreationDate();
					boolean isImportedFromFlashApp = fakeDate.equals(creationDate);
					if (!isImportedFromFlashApp)
						firstUse = true;
				}
			} 
			this.defineProperty("firstUse", firstUse, READONLY);
			if (element.getFoilCode()!=null) {
				this.defineProperty("foil", StringUtils.capitalize(element.getFoilCode()), READONLY);
			}
			float w = element.getWidth()!=null ? element.getWidth().floatValue() / 72f : 0f;
			float h = element.getHeight()!=null ? element.getHeight().floatValue() / 72f : 0f;
			if (element.getCropWidth()!=null && element.getCropWidth()!=null &&
				element.getCropWidth()>0 && element.getCropHeight()>0) {
				w = element.getCropWidth().floatValue() / 72f;
				h = element.getCropHeight().floatValue() / 72f;
			}
			this.defineProperty("chargeableArea", (w+1) * (h+1), READONLY);
			this.defineProperty("width", w, READONLY);
			this.defineProperty("height", h, READONLY);
		}
		
		@Override
		public String getClassName() {
			return "ImageStampValue";
		}
		
	}

}
