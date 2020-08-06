
/**
 * ShipConfirmErrorMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.ups.www.wsdl.xoltws.ship.v1_0;

public class ShipConfirmErrorMessage extends java.lang.Exception{

    private static final long serialVersionUID = 1381181320224L;
    
    private com.ups.www.wsdl.xoltws.ship.v1_0.ShipServiceStub.Errors faultMessage;

    
        public ShipConfirmErrorMessage() {
            super("ShipConfirmErrorMessage");
        }

        public ShipConfirmErrorMessage(java.lang.String s) {
           super(s);
        }

        public ShipConfirmErrorMessage(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ShipConfirmErrorMessage(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.ups.www.wsdl.xoltws.ship.v1_0.ShipServiceStub.Errors msg){
       faultMessage = msg;
    }
    
    public com.ups.www.wsdl.xoltws.ship.v1_0.ShipServiceStub.Errors getFaultMessage(){
       return faultMessage;
    }
}
    