package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;

public class SimplePrototypeProductOptionValueSerializer extends JsonSerializer<PrototypeProductOptionValue> {

	@Override
	public void serialize(PrototypeProductOptionValue value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		//jgen.writeNumberField("prototypeProductOptionId", value.getPrototypeProductOption().getId());
		//jgen.writeStringField("prototypeProductOptionEffectiveCode", value.getPrototypeProductOption().getEffectiveCode());
		//jgen.writeObjectField("tProductOptionValue", value.getProductOptionValue());
		jgen.writeStringField("code", value.getCode());
		jgen.writeEndObject();
	}

}
