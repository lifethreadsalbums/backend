package com.poweredbypace.pace.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.poweredbypace.pace.domain.File;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionBoolean;
import com.poweredbypace.pace.domain.ProductOptionDate;
import com.poweredbypace.pace.domain.ProductOptionDouble;
import com.poweredbypace.pace.domain.ProductOptionElement;
import com.poweredbypace.pace.domain.ProductOptionFile;
import com.poweredbypace.pace.domain.ProductOptionInteger;
import com.poweredbypace.pace.domain.ProductOptionString;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.layout.CameoSetElement;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.manager.ProductManager;
import com.poweredbypace.pace.util.SpringContextUtil;

@Component
public class ProductDeserializer extends JsonDeserializer<Product> {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private ProductManager productManager;
	
	@Override
    public Product deserialize(JsonParser jsonParser, 
            DeserializationContext deserializationContext) throws IOException {

    	ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Product product = deserialize(jsonParser, node);
        
        //initialize the children collection
        //weird things are happening when the collection is not fully initialized and we add some unsaved objects to it
        //see discussion here: https://groups.google.com/forum/#!topic/play-framework/ovEIA6qfYo0
        product.getChildren().size();
        
        JsonNode children = node.get("children");
        if (children!=null) {
        	List<Long> childrenIds = new ArrayList<Long>();
        	for(JsonNode child:children) {
        		Product childProduct = deserialize(jsonParser, child);
        		childProduct.setParent(product);
        		childrenIds.add(childProduct.getId());
        		if (childProduct.getId()!=null) {
        			Product prevChild = findChild(product, childProduct.getId());
        			product.getChildren().set(product.getChildren().indexOf(prevChild), childProduct);
        		} else
        			product.getChildren().add( childProduct );
        	}
        	//remove children not included in JSON
        	for (int i=product.getChildren().size()-1;i>=0;i--) {    
        		Product child = product.getChildren().get(i);
        		if (childrenIds.indexOf(child.getId())==-1)
        			product.getChildren().remove(i);
        	} 
        	
        } else 
        	product.getChildren().clear();
        
        return product;
    }
    
    private Product findChild(Product parent, long childId) {
    	for(Product p:parent.getChildren())
    	{
    		if (p.getId()!=null && p.getId().equals(childId))
    			return p;
    	}
    	return null;
    }

    private Product deserialize(JsonParser jsonParser, JsonNode node) throws JsonParseException, JsonMappingException, IOException {
    	long prototypeId = node.get("prototypeId").asLong();
    	Long id = nullableLong(node.get("id"));
    	boolean prototypeChanged = false;
    	
    	ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();  
    	
    	if (productManager==null) {
    		productManager = SpringContextUtil.getApplicationContext().getBean(ProductManager.class);
    	}
    	
        Product product = id!=null ? productManager.getProduct(id) :
        	productManager.createProductFromPrototype(prototypeId);
        
        
        
        if (!product.getPrototypeProduct().getId().equals(prototypeId)) {
        	//prototype has been changed, we need to remove the options and create them again
        	
        	if (product.getProductPrices()!=null) {
        		for(ProductPrice productPrice:product.getProductPrices()) {
        			productPrice.getLineItems().clear();
        		}
        	}
        	
        	Product newProduct = productManager.createProductFromPrototype(prototypeId);
        	product.getProductOptions().clear();
        	
        	//product.setProductPrice(null);
        	for(ProductOption<?> option:newProduct.getProductOptions()) {
        		option.setProduct(product);
        		product.getProductOptions().add(option);
        	}
        	product.setPrototypeProduct(newProduct.getPrototypeProduct());
        	prototypeChanged = true;
        }
        
        Long originalId = nullableLong(node.get("originalId"));
        if (originalId!=null) {
        	product.setOriginal( productManager.getProduct(originalId) );
        }
        
        Long parentId = nullableLong(node.get("parentId"));
        if (parentId!=null) {
        	product.setParent( productManager.getProduct(parentId) );
        }
        
        product.setId( id );
        product.setVersion( nullableInt(node.get("version")) );
        
        String state = nullableString(node.get("state"));
        product.setState(state!=null ? ProductState.valueOf(state) : ProductState.New);
        product.setProductNumber( nullableString(node.get("productNumber")) );
        product.setIsReprint( nullableBoolean(node.get("isReprint")) );
        product.setIsFavourite( nullableBoolean(node.get("isFavourite")) );
        product.setLinkLayout( nullableBoolean(node.get("linkLayout")) );
        product.setOnHold( nullableBoolean(node.get("onHold")) );
        product.setSkipLowResCheck( nullableBoolean(node.get("skipLowResCheck")) );
        product.setIsCoverBuilderWizardEnabled( nullableBoolean(node.get("isCoverBuilderWizardEnabled")) );
        product.setChildIndex( nullableInt(node.get("childIndex")));
        
        Long layoutId = nullableLong(node.get("layoutId"));
        if (layoutId!=null) {
        	product.setLayout(productManager.getLayout(layoutId));
        }
        
        JsonNode options = node.get("options");
        
        for (Iterator<String> iter = options.fieldNames(); iter.hasNext();) {
        	 String optionCode = iter.next();
        	 
        	 //find option
        	 
        	 ProductOption<?> productOption = null;
        	 
        	 for(ProductOption<?> po:product.getProductOptions()) {
        		 if (optionCode.equals(po.getPrototypeProductOption().getEffectiveCode())) {
        			 productOption = po;
        			 break;
        		 }
        	 }
        	 if (productOption==null) {
        		 PrototypeProductOption prototypeProductOption = product.getPrototypeProduct().getOptionByCode(optionCode);
        		 if (prototypeProductOption!=null) {
        			 try {
        				 productOption = prototypeProductOption.getProductOptionType().getProductOptionClass().newInstance();
	       				if (prototypeProductOption.getSystemAttribute()==SystemAttribute.DateCreated &&
	       					productOption.getClass().isAssignableFrom(ProductOptionDate.class)) {
	       					ProductOptionDate poDate = (ProductOptionDate) productOption;
	       					poDate.setValue(new Date());
	       				}
	       				productOption.setProduct(product);
	       				productOption.setPrototypeProductOption(prototypeProductOption);
	       				product.getProductOptions().add(productOption);
        			 } catch (Exception e) {
           				log.error("", e);
           			} 
        		 }
        	 }
        	
        	//for(ProductOption<?> productOption:product.getProductOptions()) {
			//String optionCode = productOption.getPrototypeProductOption().getEffectiveCode();
			//if (!options.has(optionCode))
			//	continue;
        	if (productOption==null) continue;
			
			JsonNode jsonValue = options.get(optionCode);
			
			if (jsonValue==null || jsonValue.isNull()) {
				productOption.setValue(null);
				continue;
			}
			
			if(productOption.getClass().isAssignableFrom(ProductOptionValue.class)) {
				ProductOptionValue productOptionValue = (ProductOptionValue) productOption;
				String value = jsonValue.asText();
				//log.debug(optionCode+"="+value);
				for(PrototypeProductOptionValue val:productOption.getPrototypeProductOption().getPrototypeProductOptionValues()) {
					if (val.getProductOptionValue().getCode().equals(value)) {
						productOptionValue.setValue(val);
						break;
					}
				}
			} else if(productOption.getClass().isAssignableFrom(ProductOptionString.class)) {
				ProductOptionString poString = (ProductOptionString) productOption;
				poString.setValue(jsonValue.asText());
			} else if(productOption.getClass().isAssignableFrom(ProductOptionBoolean.class)) {
				ProductOptionBoolean poBoolean = (ProductOptionBoolean) productOption;
				poBoolean.setValue(jsonValue.asBoolean());
			} else if(productOption.getClass().isAssignableFrom(ProductOptionInteger.class)) {
				ProductOptionInteger poInteger = (ProductOptionInteger) productOption;
				poInteger.setValue(jsonValue.asInt());
			} else if(productOption.getClass().isAssignableFrom(ProductOptionDouble.class)) {
				ProductOptionDouble poDouble = (ProductOptionDouble) productOption;
				poDouble.setValue(jsonValue.asDouble());
			} else if(productOption.getClass().isAssignableFrom(ProductOptionDate.class)) {
				ProductOptionDate poDate = (ProductOptionDate) productOption;
				String date = jsonValue.asText();
				Date value = null;
				if (date!=null) {
					if (date.indexOf("T")<0) {
						date += "T00:00:00Z";
					}
					value = ISO8601Utils.parse(date);
				}
				poDate.setValue(value);
			} else if (productOption.getClass().isAssignableFrom(ProductOptionElement.class)) {
				ProductOptionElement poElement = (ProductOptionElement) productOption;
				Element newEl = mapper.readValue(jsonValue.traverse(), Element.class);
				
				if (poElement.getElement()!=null) {
					
					Element el = poElement.getElement();
					String elementType = nullableString(jsonValue.get("type"));
					
					Hibernate.initialize(el);
					if (el instanceof HibernateProxy) { 
						el = (Element) ((HibernateProxy) el).getHibernateLazyInitializer().getImplementation();
						poElement.setValue(el);
					}
					
					if (el instanceof CameoSetElement && newEl instanceof CameoSetElement) {
						CameoSetElement newCameoSet = (CameoSetElement) newEl;
						CameoSetElement cameoSet = (CameoSetElement) el;
						cameoSet.setPositionCode(newCameoSet.getPositionCode());
						for(int i=0;i<newCameoSet.getShapes().size();i++) {
							if (i<cameoSet.getShapes().size()) {
								newCameoSet.getShapes().get(i).copy(cameoSet.getShapes().get(i));
							} else {
								cameoSet.getShapes().add(newCameoSet.getShapes().get(i));
							}
						}
						if (cameoSet.getShapes().size()>newCameoSet.getShapes().size()) {
							int diff = cameoSet.getShapes().size() - newCameoSet.getShapes().size();
							for(int i=0;i<diff;i++) {
								cameoSet.getShapes().remove(cameoSet.getShapes().size()-1);
							}
						}
					} else {
						String className = el.getClass().getSimpleName();
						if (className.equals(elementType)) {
							newEl.copy(el);
						} else {
							poElement.setValue( newEl );
						}
					}
					
				} else
					poElement.setValue( newEl );
				
				
				if (prototypeChanged && poElement.getValue()!=null && poElement.getValue().getId()!=null) {
					poElement.getValue().setId(null);
				}
				
			} else if (productOption.getClass().isAssignableFrom(ProductOptionFile.class)) {
				ProductOptionFile poFile = (ProductOptionFile) productOption;
				poFile.setValue( mapper.readValue(jsonValue.traverse(), File.class) );
			} 
		
		}
        
        //em.detach(product);
        return product;
    }
    
    private Boolean nullableBoolean(JsonNode node) {
    	return node==null || node.isNull() ? null : node.asBoolean();
    }
    
    private Integer nullableInt(JsonNode node) {
    	return node==null || node.isNull() ? null : node.asInt();
    }
    
    private Long nullableLong(JsonNode node) {
    	return node==null || node.isNull() ? null : node.asLong();
    }
    
    @SuppressWarnings("unused")
	private Date nullableDate(JsonNode node) {
    	return node==null || node.isNull() ? null : ISO8601Utils.parse(node.asText());
    }
    
    private String nullableString(JsonNode node) {
    	return node==null || node.isNull() ? null : node.asText();
    }
}
