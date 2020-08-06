package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.BaseEntity;

public class BaseEntitySerializer extends JsonSerializer<BaseEntity> {

	@Override
	public void serialize(BaseEntity value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeNumberField("version", value.getVersion());
		jgen.writeEndObject();
        
	}

}
