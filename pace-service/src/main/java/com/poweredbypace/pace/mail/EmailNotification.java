package com.poweredbypace.pace.mail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EmailNotification {
	
	public static enum Recipient {
		None,
		LoggedUser,
		SuperAdmin,
		StoreOwner,
		MethodParam
	}
	
	String template() default "";

	Recipient to() default Recipient.None;
}

