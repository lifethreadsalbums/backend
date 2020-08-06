package com.poweredbypace.pace.json;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class JsonEnumModule extends SimpleModule {
	
	private static final long serialVersionUID = -3617521544450958714L;

	public JsonEnumModule() {
		super("json-enum", new Version(1, 0, 0, "", "com.poweredbypace", "json-enum"));
		addSerializer(Enum.class, new LowerEnumSerializer());
	}
	
	@Override
	public void setupModule(SetupContext context) {
		super.setupModule(context);
		Base deser = new Deserializers.Base() {
			@SuppressWarnings("unchecked")
			@Override
			public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
					DeserializationConfig config, BeanDescription beanDesc)
					throws JsonMappingException {
				return new LowerEnumDeserializer((Class<Enum<?>>) type);
			}
		};
		context.addDeserializers(deser);
	};

	@SuppressWarnings("rawtypes")
	private static class LowerEnumSerializer extends StdScalarSerializer<Enum> {

		public LowerEnumSerializer() {
			super(Enum.class, false);
		}

		@Override
		public void serialize(Enum value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonGenerationException {
			String enumValue = value.name();
			jgen.writeString(enumValue);
		}
	}
	
	@SuppressWarnings("serial")
	public class LowerEnumDeserializer extends StdScalarDeserializer<Enum<?>> {

		protected LowerEnumDeserializer(Class<Enum<?>> clazz) {
			super(clazz);
		}

		@Override
		public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			String text = jp.getText();
			try {
				Method valueOfMethod = getValueClass().getDeclaredMethod("valueOf", String.class);
				return (Enum<?>) valueOfMethod.invoke(null, text);
			} catch (Exception e) {
				throw new RuntimeException("Cannot deserialize enum " + getValueClass().getName() + " from " + text, e);
			}
		}

	}
}