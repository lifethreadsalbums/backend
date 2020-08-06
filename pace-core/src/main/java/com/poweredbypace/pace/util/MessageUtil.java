package com.poweredbypace.pace.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageUtil {
 
    public static String getMessage(String key) {
 
        try {
            MessageSource bean = SpringContextUtil.getApplicationContext().getBean(MessageSource.class);
            Locale locale = LocaleContextHolder.getLocale();
            return bean.getMessage(key, null, locale);
        } catch (NoSuchMessageException e) {
            return "No message for key: " + key;
        }
 
    }
    
    public static String getMessage(Throwable ex) {
    	try {
            MessageSource bean = SpringContextUtil.getApplicationContext().getBean(MessageSource.class);
            Locale locale = LocaleContextHolder.getLocale();
            String message = bean.getMessage(ex.getClass().getName(), null, locale);
            if (message==null)
            	return ex.getMessage();
            return message;
        } catch (Exception e) {
            return ex.getMessage();
        }
    }
}