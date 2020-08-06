package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.store.Store;

public class SimpleStoreSerializer extends JsonSerializer<Store> {

	@Override
	public void serialize(Store value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		if (value==null) {
			jgen.writeNull();
			return;
		}
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("name", value.getName());
		jgen.writeStringField("domainName", value.getDomainName());
		jgen.writeStringField("ownerEmail", value.getOwnerEmail());
		jgen.writeEndObject();
        
	}

}
