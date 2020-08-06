package com.poweredbypace.pace.push.cors;

import java.util.concurrent.atomic.AtomicReference;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResponse;

public class CorsInterceptor implements AtmosphereInterceptor {
	
	//private static final Logger logger = LoggerFactory.getLogger(CorsInterceptor.class);
	private final AtomicReference<String> emptyMessage = new AtomicReference<String>("");
	private final String[] allowedDomains = {"http://studio.irisbook.com", "http://www.studio.irisbook.com"};
	
	public void configure(AtmosphereConfig arg0) {
	}
	
	
	public Action inspect(AtmosphereResource resource) {
		
		AtmosphereRequest req = resource.getRequest();                                   
        AtmosphereResponse res = resource.getResponse();
        
        String ipAddress = req.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null) {  
     	   ipAddress = req.getRemoteAddr();
        }
        //logger.debug("IP="+ipAddress);
        
        String origin = req.getHeader("Origin");
        if( origin!= null){
        	//logger.debug("Origin = " + req.getHeader("Origin"));
            //res.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        	
        	boolean allow = false;
        	for(String domain:allowedDomains) {
        		if (domain.equals(origin))
        		{
        			allow = true;
        			break;
        		}
        	}
        	
        	if (allow) {
        		res.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
            	res.addHeader("Access-Control-Expose-Headers", "X-Cache-Date, X-Atmosphere-tracking-id, X-Pace-Auth");
            	res.setHeader("Access-Control-Allow-Credentials", "true");
        	}
        }

        if("OPTIONS".equals(req.getMethod())){
        	res.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");
            res.setHeader("Access-Control-Allow-Headers",
            		"Origin, Content-Type, X-Atmosphere-Framework, X-Cache-Date, X-Atmosphere-tracking-id, X-Atmosphere-Transport, X-Pace-Auth");
            res.setHeader("Access-Control-Max-Age", "-1");
            res.write(emptyMessage.get());
            
            return Action.SKIP_ATMOSPHEREHANDLER;
        } 
    	
		return Action.CONTINUE;
	}

	public void postInspect(AtmosphereResource resource) {
		
	}


	@Override
	public void destroy() {
		
	}
	
}
