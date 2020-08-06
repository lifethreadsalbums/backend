package com.poweredbypace.pace.exception.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseBody;

//@ControllerAdvice 
public class ExceptionControllerAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public String exception(AuthenticationException e) {
        return "{\"status\":\"access denied\"}";
    } 
}