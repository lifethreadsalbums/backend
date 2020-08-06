package com.poweredbypace.pace.json;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 7511462027804428297L;

	public HibernateAwareObjectMapper() {
		Hibernate4Module hibernateMod = new Hibernate4Module();
		hibernateMod.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING , true);
		
        registerModule(hibernateMod);
        registerModule(new JsonEnumModule());
        registerModule(new DomainEntityModule());
        this.setDateFormat(new ISO8601DateFormat());
        
    }
}