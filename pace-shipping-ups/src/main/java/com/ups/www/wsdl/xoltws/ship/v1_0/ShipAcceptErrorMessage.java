
/**
 * ShipAcceptErrorMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.ups.www.wsdl.xoltws.ship.v1_0;

public class ShipAcceptErrorMessage extends java.lang.Exception{

    private static final long serialVersionUID = 1381181320241L;
    
    private com.ups.www.wsdl.xoltws.ship.v1_0.ShipServiceStub.Errors faultMessage;

    
        public ShipAcceptErrorMessage() {
            super("ShipAcceptErrorMessage");
        }

        public ShipAcceptErrorMessage(java.lang.String s) {
           super(s);
        }

        public ShipAcceptErrorMessage(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ShipAcceptErrorMessage(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.ups.www.wsdl.xoltws.ship.v1_0.ShipServiceStub.Errors msg){
       faultMessage = msg;
    }
    
    public com.ups.www.wsdl.xoltws.ship.v1_0.ShipServiceStub.Errors getFaultMessage(){
       return faultMessage;
    }
}
    