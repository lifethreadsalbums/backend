package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.IccProfile;

public class SimpleIccProfileSerializer extends JsonSerializer<IccProfile> {

	@Override
	public void serialize(IccProfile value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("code", value.getCode());
		jgen.writeStringField("label", value.getLabel());
		jgen.writeEndObject();
	}

}
