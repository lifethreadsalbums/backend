package com.poweredbypace.pace.json;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.layout.ImageStampElement;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.json.View.OrderShortInfo;
import com.poweredbypace.pace.manager.ProductManager;
import com.poweredbypace.pace.util.HibernateUtil;
import com.poweredbypace.pace.util.SpringContextUtil;

public class ProductSerializer extends JsonSerializer<Product> {
	
	private static DateFormat dateFormat = new ISO8601DateFormat();
	
	private <U extends Number> void writeNumberField(JsonGenerator jgen, String fieldName, U value) throws JsonGenerationException, IOException {
		if (value!=null)
			jgen.writeNumberField(fieldName, value.longValue());
		else
			jgen.writeNullField(fieldName);
	}
	
	
	
	@Override
	public void serialize(Product value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		Class<?> activeView = provider.getActiveView();
		boolean includePrice = activeView!=OrderShortInfo.class;
		
		if (includePrice) {
			value.setProductContext(new ProductContext(value));
		}
		jgen.writeStartObject();
		
		writeNumberField(jgen, "id", value.getId());
		writeNumberField(jgen, "version", value.getVersion());
		writeNumberField(jgen, "prototypeId", value.getPrototypeProduct()!=null ? value.getPrototypeProduct().getId() : null);
		jgen.writeStringField("productType", value.getPrototypeProduct()!=null ? value.getPrototypeProduct().getProductType().name() : null);
		writeNumberField(jgen, "parentId", value.getParent()!=null ? value.getParent().getId() : null);
		writeNumberField(jgen, "layoutId", value.getLayout()!=null ? value.getLayout().getId() : null);
		writeNumberField(jgen, "coverLayoutId", value.getCoverLayout()!=null ? value.getCoverLayout().getId() : null);
		writeNumberField(jgen, "originalId", value.getOriginal()!=null ? value.getOriginal().getId() : null);
		
		OrderItem orderItem = value.getParent()!=null ? value.getParent().getOrderItem() : value.getOrderItem();
		if (orderItem!=null) {
			Order o = orderItem.getOrder();
			writeNumberField(jgen, "orderId", o.getId());
			if (o.getDateCreated()!=null) {
				jgen.writeStringField("orderDate", dateFormat.format(o.getDateCreated()));
			}
		}
		
		if (value.getBatch()!=null) {
			JsonSerializer<Batch> batchSerializer = new SimpleBatchSerializer();
			jgen.writeFieldName("batch");
			batchSerializer.serialize(value.getBatch(), jgen, provider);
		}
		
		if (includePrice) {
			jgen.writeArrayFieldStart("productPrices");
			for(ProductPrice productPrice:value.getProductPrices()) {
				jgen.writeObject(productPrice);
			}
			jgen.writeEndArray();
		}
		if (!includePrice && value.getParent()==null && value.getOrderItem()!=null) {
			//check if company logo is new
			ProductOption<?> logoOption = value.getSystemAttribute(SystemAttribute.CustomLogo);
			if (logoOption!=null) {
				Object logoValue = HibernateUtil.unproxy(logoOption.getValue());
				if (logoValue!=null && logoValue.getClass().isAssignableFrom(ImageStampElement.class)) {
					ImageStampElement stampElement = (ImageStampElement)logoValue;
					boolean firstUse = false;
					ImageFile imageFile = stampElement.getImageFile();
					if (imageFile!=null) {
						ProductManager pm = SpringContextUtil.getProductManager();
						List<BigInteger> products = pm.findByImageFileId(imageFile.getId().longValue());
						if (products.size()>0 && products.get(0).longValue()==value.getId().longValue()) {
							firstUse = true;
						}
					} 
					if (firstUse) {
						jgen.writeBooleanField("newCompanyLogo", true);
					}
				}
			}
		}
		
		jgen.writeObjectField("subtotal", value.getSubtotal());
		jgen.writeObjectField("total", value.getTotal());
		jgen.writeObjectField("attachments", value.getAttachments());
		
		JsonSerializer<User> userSerializer = new SimpleUserSerializer();
		jgen.writeFieldName("user");
		userSerializer.serialize(value.getUser(), jgen, provider);
		
		jgen.writeStringField("state", value.getState().name());
		jgen.writeStringField("orderState", value.getOrderState()!=null ? value.getOrderState().name() : null);
		jgen.writeStringField("productNumber", value.getProductNumber());
		
		jgen.writeBooleanField("inCart", BooleanUtils.isTrue(value.getInCart()));
		jgen.writeBooleanField("isCoverBuilderWizardEnabled", BooleanUtils.isTrue(value.getIsCoverBuilderWizardEnabled()));
		jgen.writeBooleanField("isFavourite", BooleanUtils.isTrue(value.getIsFavourite()));
		jgen.writeBooleanField("isReprint", BooleanUtils.isTrue(value.getIsReprint()));
		jgen.writeBooleanField("linkLayout", BooleanUtils.isTrue(value.getLinkLayout()));
		jgen.writeBooleanField("onHold", BooleanUtils.isTrue(value.getOnHold()));
		
		if (value.getChildIndex()!=null) {
			jgen.writeNumberField("childIndex", value.getChildIndex().intValue());
		}
		
		jgen.writeObjectFieldStart("options");
		for(ProductOption<?> option:value.getProductOptions()) {
			PrototypeProductOption prototypeOption = SpringContextUtil.getProductManager()
					.getPrototypeProductOption(option.getPrototypeProductOption().getId());
			
			String optionCode = prototypeOption.getEffectiveCode();
			
			if(option.getClass().isAssignableFrom(ProductOptionValue.class)) {
				ProductOptionValue productOptionValue = (ProductOptionValue) option;
				String valueCode = null; 
				if (productOptionValue.getValue()!=null) {
					PrototypeProductOptionValue prototypeOptionValue = SpringContextUtil.getProductManager()
							.getPrototypeProductOptionValue(productOptionValue.getValue().getId());
					valueCode = prototypeOptionValue.getCode();
				}
				jgen.writeObjectField(optionCode, valueCode);
			} else {
				jgen.writeObjectField(optionCode, option.getValue());
			}
		}
		jgen.writeEndObject();
		
		jgen.writeObjectFieldStart("displayOptions");
		for(ProductOption<?> option:value.getProductOptions()) {
			PrototypeProductOption prototypeOption = SpringContextUtil.getProductManager()
					.getPrototypeProductOption(option.getPrototypeProductOption().getId());
			
			String optionCode = prototypeOption.getEffectiveCode();
			String displayValue = null;
			
			if(option.getClass().isAssignableFrom(ProductOptionValue.class)) {
				ProductOptionValue productOptionValue = (ProductOptionValue) option;
				
				if (productOptionValue.getValue()!=null) {
					PrototypeProductOptionValue prototypeOptionValue = SpringContextUtil.getProductManager()
							.getPrototypeProductOptionValue(productOptionValue.getValue().getId());
					
					displayValue = prototypeOptionValue.getDisplayName();
				}
			} else {
				displayValue = option.getDisplayValue();
			}
			
			jgen.writeObjectField(optionCode, displayValue);
		}
		jgen.writeEndObject();
		
		jgen.writeArrayFieldStart("children");
		for(Product p:value.getChildren()) {
			serialize(p, jgen, provider);
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
		value.setProductContext(null);
        
	}

}
