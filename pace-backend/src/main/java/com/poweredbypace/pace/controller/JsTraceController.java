package com.poweredbypace.pace.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.user.User;

@Controller
@RequestMapping(value = "/api/log")
public class JsTraceController {
	
	private Log log = LogFactory.getLog("JsLogger");

	public JsTraceController() { }
	
	@RequestMapping(value = "/error", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void logError(@RequestBody JsError error, @AuthenticationPrincipal User currentUser) {
		
		
		log.error("URL: "+error.url + ", user: " + currentUser.getEmail() + 
			", error: " +error.message + ", stack trace:\n" + error.stackTrace);
		
	}

	public static class JsError {
		public String message;
		public String stackTrace;
		public String url;
	}
}
