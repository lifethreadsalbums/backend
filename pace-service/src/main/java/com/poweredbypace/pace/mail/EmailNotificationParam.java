package com.poweredbypace.pace.mail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotificationParam {
	String value() default "";
	Class<?> wrapper() default EmailNotificationParam.class;
}
