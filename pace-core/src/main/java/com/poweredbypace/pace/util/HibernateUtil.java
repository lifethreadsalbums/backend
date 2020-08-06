package com.poweredbypace.pace.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T unproxy(T obj) {
		
		if (obj==null) return null;
		
		Hibernate.initialize(obj);
		if (obj instanceof HibernateProxy) { 
			obj = (T) ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}
		return obj;
		
	}

}
