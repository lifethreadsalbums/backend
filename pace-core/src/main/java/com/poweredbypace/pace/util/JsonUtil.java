package com.poweredbypace.pace.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.json.HibernateAwareObjectMapper;

public class JsonUtil {
	
	private static final Log log = LogFactory.getLog(JsonUtil.class);
	
	private static final ObjectMapper mapper = new HibernateAwareObjectMapper();
	
	@SuppressWarnings({ "rawtypes" })
	public static <T> T deserialize(String json, TypeReference valueTypeRef) {
		if (json==null) return null;
		try {
			return mapper.readValue(json, valueTypeRef);
		} catch (JsonParseException e) {
			log.error(e, e.getCause());
		} catch (JsonMappingException e) {
			log.error(e, e.getCause());
		} catch (IOException e) {
			log.error(e, e.getCause());
		}
		return null;
	}
	
	public static <T> T deserialize(String json, Class<T> valueType) {
		if (json==null) return null;
		try {
			return mapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			log.error(e, e.getCause());
		} catch (JsonMappingException e) {
			log.error(e, e.getCause());
		} catch (IOException e) {
			log.error(e, e.getCause());
		}
		return null;
	}
	
	public static String serialize(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error(e, e.getCause());
		}
		return null;
	}
}
