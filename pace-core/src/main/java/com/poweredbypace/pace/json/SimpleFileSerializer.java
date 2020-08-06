package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.File;

public class SimpleFileSerializer extends JsonSerializer<File> {

	@Override
	public void serialize(File value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("name", value.getFilename());
		jgen.writeStringField("url", value.getUrl());
		jgen.writeEndObject();
        
	}

}
