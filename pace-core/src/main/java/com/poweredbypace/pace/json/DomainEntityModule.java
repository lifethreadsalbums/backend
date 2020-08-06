package com.poweredbypace.pace.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.poweredbypace.pace.domain.Product;

public class DomainEntityModule extends SimpleModule {

	private static final long serialVersionUID = -8956712065668416029L;

	public DomainEntityModule() {
		super("json-entity", new Version(1, 0, 0, "", "com.poweredbypace", "json-entity"));
		addSerializer(Product.class, new ProductSerializer());
		addDeserializer(Product.class, new ProductDeserializer());
	}
	
	

}
