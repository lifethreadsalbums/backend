package com.poweredbypace.pace.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poweredbypace.pace.domain.user.User;

public class SimpleUserSerializer extends JsonSerializer<User> {

	@Override
	public void serialize(User value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		if (value==null) {
			jgen.writeNull();
			return;
		}
		
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("email", value.getEmail());
		jgen.writeStringField("firstName", value.getFirstName());
		jgen.writeStringField("lastName", value.getLastName());
		jgen.writeStringField("companyName", value.getCompanyName());
		jgen.writeEndObject();
        
	}

}
