package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.ProductOptionGroup;

public class SimpleProductOptionGroupSerializer extends JsonSerializer<ProductOptionGroup> {

	@Override
	public void serialize(ProductOptionGroup value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("code", value.getCode());
		jgen.writeStringField("label", value.getDisplayLabel());
		jgen.writeStringField("url", value.getUrl());
		jgen.writeStringField("visibilityExpression", value.getVisibilityExpression());
		if (value.getOrder()!=null)
			jgen.writeNumberField("order", value.getOrder());
		else
			jgen.writeNullField("order");
		jgen.writeEndObject();
	}

}
