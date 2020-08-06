
/**
 * RateErrorMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.ups.www.wsdl.xoltws.rate.v1_1;

public class RateErrorMessage extends java.lang.Exception{

    private static final long serialVersionUID = 1379430585876L;
    
    private com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.Errors faultMessage;

    
        public RateErrorMessage() {
            super("RateErrorMessage");
        }

        public RateErrorMessage(java.lang.String s) {
           super(s);
        }

        public RateErrorMessage(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public RateErrorMessage(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.Errors msg){
       faultMessage = msg;
    }
    
    public com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.Errors getFaultMessage(){
       return faultMessage;
    }
}
    