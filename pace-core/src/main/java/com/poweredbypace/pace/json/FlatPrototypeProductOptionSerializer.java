package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.PrototypeProductOption;

public class FlatPrototypeProductOptionSerializer extends JsonSerializer<PrototypeProductOption> {

	@Override
	public void serialize(PrototypeProductOption value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeNumberField("prototypeProductId", value.getPrototypeProduct().getId());
		jgen.writeStringField("effectiveCode", value.getEffectiveCode());
		jgen.writeStringField("systemAttribute", value.getSystemAttribute()!=null ? value.getSystemAttribute().toString() : null);
		jgen.writeEndObject();
	}

}
