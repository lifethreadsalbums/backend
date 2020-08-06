
/**
 * CurrencyConvertorStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
        package com.poweredbypace.pace.currency.webservicex;

        

        /*
        *  CurrencyConvertorStub java implementation
        */

        
        @SuppressWarnings({"rawtypes", "unchecked", "serial", "unused"})
        public class CurrencyConvertorStub extends org.apache.axis2.client.Stub
        {
        protected org.apache.axis2.description.AxisOperation[] _operations;

        //hashmaps to keep the fault mapping
        private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
        private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
        private java.util.HashMap faultMessageMap = new java.util.HashMap();

        private static int counter = 0;

        private static synchronized java.lang.String getUniqueSuffix(){
            // reset the counter if it is greater than 99999
            if (counter > 99999){
                counter = 0;
            }
            counter = counter + 1; 
            return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
        }

    
    private void populateAxisService() throws org.apache.axis2.AxisFault {

     //creating the Service with a unique name
     _service = new org.apache.axis2.description.AxisService("CurrencyConvertor" + getUniqueSuffix());
     addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[1];
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://www.webserviceX.NET/", "conversionRate"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[0]=__operation;
            
        
        }

    //populates the faults
    private void populateFaults(){
         


    }

    /**
      *Constructor that takes in a configContext
      */

    public CurrencyConvertorStub(org.apache.axis2.context.ConfigurationContext configurationContext,
       java.lang.String targetEndpoint)
       throws org.apache.axis2.AxisFault {
         this(configurationContext,targetEndpoint,false);
   }


   /**
     * Constructor that takes in a configContext  and useseperate listner
     */
   public CurrencyConvertorStub(org.apache.axis2.context.ConfigurationContext configurationContext,
        java.lang.String targetEndpoint, boolean useSeparateListener)
        throws org.apache.axis2.AxisFault {
         //To populate AxisService
         populateAxisService();
         populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext,_service);
        
	
        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(
                targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
        
            //Set the soap version
            _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        
    
    }

    /**
     * Default Constructor
     */
    public CurrencyConvertorStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
        
                    this(configurationContext,"http://www.webservicex.net/CurrencyConvertor.asmx" );
                
    }

    /**
     * Default Constructor
     */
    public CurrencyConvertorStub() throws org.apache.axis2.AxisFault {
        
                    this("http://www.webservicex.net/CurrencyConvertor.asmx" );
                
    }

    /**
     * Constructor taking the target endpoint
     */
    public CurrencyConvertorStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }



        
                    /**
                     * Auto generated method signature
                     * &lt;br&gt;&lt;b&gt;Get conversion rate from one currency to another currency &lt;b&gt;&lt;br&gt;&lt;p&gt;&lt;b&gt;&lt;font color='#000080' size='1' face='Verdana'&gt;&lt;u&gt;Differenct currency Code and Names around the world&lt;/u&gt;&lt;/font&gt;&lt;/b&gt;&lt;/p&gt;&lt;blockquote&gt;&lt;p&gt;&lt;font face='Verdana' size='1'&gt;AFA-Afghanistan Afghani&lt;br&gt;ALL-Albanian Lek&lt;br&gt;DZD-Algerian Dinar&lt;br&gt;ARS-Argentine Peso&lt;br&gt;AWG-Aruba Florin&lt;br&gt;AUD-Australian Dollar&lt;br&gt;BSD-Bahamian Dollar&lt;br&gt;BHD-Bahraini Dinar&lt;br&gt;BDT-Bangladesh Taka&lt;br&gt;BBD-Barbados Dollar&lt;br&gt;BZD-Belize Dollar&lt;br&gt;BMD-Bermuda Dollar&lt;br&gt;BTN-Bhutan Ngultrum&lt;br&gt;BOB-Bolivian Boliviano&lt;br&gt;BWP-Botswana Pula&lt;br&gt;BRL-Brazilian Real&lt;br&gt;GBP-British Pound&lt;br&gt;BND-Brunei Dollar&lt;br&gt;BIF-Burundi Franc&lt;br&gt;XOF-CFA Franc (BCEAO)&lt;br&gt;XAF-CFA Franc (BEAC)&lt;br&gt;KHR-Cambodia Riel&lt;br&gt;CAD-Canadian Dollar&lt;br&gt;CVE-Cape Verde Escudo&lt;br&gt;KYD-Cayman Islands Dollar&lt;br&gt;CLP-Chilean Peso&lt;br&gt;CNY-Chinese Yuan&lt;br&gt;COP-Colombian Peso&lt;br&gt;KMF-Comoros Franc&lt;br&gt;CRC-Costa Rica Colon&lt;br&gt;HRK-Croatian Kuna&lt;br&gt;CUP-Cuban Peso&lt;br&gt;CYP-Cyprus Pound&lt;br&gt;CZK-Czech Koruna&lt;br&gt;DKK-Danish Krone&lt;br&gt;DJF-Dijibouti Franc&lt;br&gt;DOP-Dominican Peso&lt;br&gt;XCD-East Caribbean Dollar&lt;br&gt;EGP-Egyptian Pound&lt;br&gt;SVC-El Salvador Colon&lt;br&gt;EEK-Estonian Kroon&lt;br&gt;ETB-Ethiopian Birr&lt;br&gt;EUR-Euro&lt;br&gt;FKP-Falkland Islands Pound&lt;br&gt;GMD-Gambian Dalasi&lt;br&gt;GHC-Ghanian Cedi&lt;br&gt;GIP-Gibraltar Pound&lt;br&gt;XAU-Gold Ounces&lt;br&gt;GTQ-Guatemala Quetzal&lt;br&gt;GNF-Guinea Franc&lt;br&gt;GYD-Guyana Dollar&lt;br&gt;HTG-Haiti Gourde&lt;br&gt;HNL-Honduras Lempira&lt;br&gt;HKD-Hong Kong Dollar&lt;br&gt;HUF-Hungarian Forint&lt;br&gt;ISK-Iceland Krona&lt;br&gt;INR-Indian Rupee&lt;br&gt;IDR-Indonesian Rupiah&lt;br&gt;IQD-Iraqi Dinar&lt;br&gt;ILS-Israeli Shekel&lt;br&gt;JMD-Jamaican Dollar&lt;br&gt;JPY-Japanese Yen&lt;br&gt;JOD-Jordanian Dinar&lt;br&gt;KZT-Kazakhstan Tenge&lt;br&gt;KES-Kenyan Shilling&lt;br&gt;KRW-Korean Won&lt;br&gt;KWD-Kuwaiti Dinar&lt;br&gt;LAK-Lao Kip&lt;br&gt;LVL-Latvian Lat&lt;br&gt;LBP-Lebanese Pound&lt;br&gt;LSL-Lesotho Loti&lt;br&gt;LRD-Liberian Dollar&lt;br&gt;LYD-Libyan Dinar&lt;br&gt;LTL-Lithuanian Lita&lt;br&gt;MOP-Macau Pataca&lt;br&gt;MKD-Macedonian Denar&lt;br&gt;MGF-Malagasy Franc&lt;br&gt;MWK-Malawi Kwacha&lt;br&gt;MYR-Malaysian Ringgit&lt;br&gt;MVR-Maldives Rufiyaa&lt;br&gt;MTL-Maltese Lira&lt;br&gt;MRO-Mauritania Ougulya&lt;br&gt;MUR-Mauritius Rupee&lt;br&gt;MXN-Mexican Peso&lt;br&gt;MDL-Moldovan Leu&lt;br&gt;MNT-Mongolian Tugrik&lt;br&gt;MAD-Moroccan Dirham&lt;br&gt;MZM-Mozambique Metical&lt;br&gt;MMK-Myanmar Kyat&lt;br&gt;NAD-Namibian Dollar&lt;br&gt;NPR-Nepalese Rupee&lt;br&gt;ANG-Neth Antilles Guilder&lt;br&gt;NZD-New Zealand Dollar&lt;br&gt;NIO-Nicaragua Cordoba&lt;br&gt;NGN-Nigerian Naira&lt;br&gt;KPW-North Korean Won&lt;br&gt;NOK-Norwegian Krone&lt;br&gt;OMR-Omani Rial&lt;br&gt;XPF-Pacific Franc&lt;br&gt;PKR-Pakistani Rupee&lt;br&gt;XPD-Palladium Ounces&lt;br&gt;PAB-Panama Balboa&lt;br&gt;PGK-Papua New Guinea Kina&lt;br&gt;PYG-Paraguayan Guarani&lt;br&gt;PEN-Peruvian Nuevo Sol&lt;br&gt;PHP-Philippine Peso&lt;br&gt;XPT-Platinum Ounces&lt;br&gt;PLN-Polish Zloty&lt;br&gt;QAR-Qatar Rial&lt;br&gt;ROL-Romanian Leu&lt;br&gt;RUB-Russian Rouble&lt;br&gt;WST-Samoa Tala&lt;br&gt;STD-Sao Tome Dobra&lt;br&gt;SAR-Saudi Arabian Riyal&lt;br&gt;SCR-Seychelles Rupee&lt;br&gt;SLL-Sierra Leone Leone&lt;br&gt;XAG-Silver Ounces&lt;br&gt;SGD-Singapore Dollar&lt;br&gt;SKK-Slovak Koruna&lt;br&gt;SIT-Slovenian Tolar&lt;br&gt;SBD-Solomon Islands Dollar&lt;br&gt;SOS-Somali Shilling&lt;br&gt;ZAR-South African Rand&lt;br&gt;LKR-Sri Lanka Rupee&lt;br&gt;SHP-St Helena Pound&lt;br&gt;SDD-Sudanese Dinar&lt;br&gt;SRG-Surinam Guilder&lt;br&gt;SZL-Swaziland Lilageni&lt;br&gt;SEK-Swedish Krona&lt;br&gt;TRY-Turkey Lira&lt;br&gt;CHF-Swiss Franc&lt;br&gt;SYP-Syrian Pound&lt;br&gt;TWD-Taiwan Dollar&lt;br&gt;TZS-Tanzanian Shilling&lt;br&gt;THB-Thai Baht&lt;br&gt;TOP-Tonga Pa'anga&lt;br&gt;TTD-Trinidad&amp;amp;amp;Tobago Dollar&lt;br&gt;TND-Tunisian Dinar&lt;br&gt;TRL-Turkish Lira&lt;br&gt;USD-U.S. Dollar&lt;br&gt;AED-UAE Dirham&lt;br&gt;UGX-Ugandan Shilling&lt;br&gt;UAH-Ukraine Hryvnia&lt;br&gt;UYU-Uruguayan New Peso&lt;br&gt;VUV-Vanuatu Vatu&lt;br&gt;VEB-Venezuelan Bolivar&lt;br&gt;VND-Vietnam Dong&lt;br&gt;YER-Yemen Riyal&lt;br&gt;YUM-Yugoslav Dinar&lt;br&gt;ZMK-Zambian Kwacha&lt;br&gt;ZWD-Zimbabwe Dollar&lt;/font&gt;&lt;/p&gt;&lt;/blockquote&gt;
                     * @see com.poweredbypace.pace.currency.webservicex.CurrencyConvertor#conversionRate
                     * @param conversionRate
                    
                     */

                    

                            public  com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse conversionRate(

                            com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate conversionRate)
                        

                    throws java.rmi.RemoteException
                    
                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
              _operationClient.getOptions().setAction("http://www.webserviceX.NET/ConversionRate");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    conversionRate,
                                                    optimizeContent(new javax.xml.namespace.QName("http://www.webserviceX.NET/",
                                                    "conversionRate")), new javax.xml.namespace.QName("http://www.webserviceX.NET/",
                                                    "conversionRate"));
                                                
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"ConversionRate"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"ConversionRate"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"ConversionRate"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
            }
        }
            


       /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
       private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
       return returnMap;
    }

    
    
    private javax.xml.namespace.QName[] opNameArray = null;
    private boolean optimizeContent(javax.xml.namespace.QName opName) {
        

        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;   
            }
        }
        return false;
    }
     //http://www.webservicex.net/CurrencyConvertor.asmx
        public static class ConversionRateResponse
        implements org.apache.axis2.databinding.ADBBean{
        
                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://www.webserviceX.NET/",
                "ConversionRateResponse",
                "ns1");

            

                        /**
                        * field for ConversionRateResult
                        */

                        
                                    protected double localConversionRateResult ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double getConversionRateResult(){
                               return localConversionRateResult;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ConversionRateResult
                               */
                               public void setConversionRateResult(double param){
                            
                                            this.localConversionRateResult=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.webserviceX.NET/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":ConversionRateResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "ConversionRateResponse",
                           xmlWriter);
                   }

               
                   }
               
                                    namespace = "http://www.webserviceX.NET/";
                                    writeStartElement(null, namespace, "ConversionRateResult", xmlWriter);
                             
                                               if (java.lang.Double.isNaN(localConversionRateResult)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("ConversionRateResult cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localConversionRateResult));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                    xmlWriter.writeEndElement();
               

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.webserviceX.NET/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        
        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                
                                      elementList.add(new javax.xml.namespace.QName("http://www.webserviceX.NET/",
                                                                      "ConversionRateResult"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localConversionRateResult));
                            

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ConversionRateResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            ConversionRateResponse object =
                new ConversionRateResponse();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"ConversionRateResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ConversionRateResponse)ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.webserviceX.NET/","ConversionRateResult").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"ConversionRateResult" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setConversionRateResult(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                              
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    
        public static class ConversionRate
        implements org.apache.axis2.databinding.ADBBean{
        
                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://www.webserviceX.NET/",
                "ConversionRate",
                "ns1");

            

                        /**
                        * field for FromCurrency
                        */

                        
                                    protected Currency localFromCurrency ;
                                

                           /**
                           * Auto generated getter method
                           * @return Currency
                           */
                           public  Currency getFromCurrency(){
                               return localFromCurrency;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param FromCurrency
                               */
                               public void setFromCurrency(Currency param){
                            
                                            this.localFromCurrency=param;
                                    

                               }
                            

                        /**
                        * field for ToCurrency
                        */

                        
                                    protected Currency localToCurrency ;
                                

                           /**
                           * Auto generated getter method
                           * @return Currency
                           */
                           public  Currency getToCurrency(){
                               return localToCurrency;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param ToCurrency
                               */
                               public void setToCurrency(Currency param){
                            
                                            this.localToCurrency=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.webserviceX.NET/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":ConversionRate",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "ConversionRate",
                           xmlWriter);
                   }

               
                   }
               
                                            if (localFromCurrency==null){
                                                 throw new org.apache.axis2.databinding.ADBException("FromCurrency cannot be null!!");
                                            }
                                           localFromCurrency.serialize(new javax.xml.namespace.QName("http://www.webserviceX.NET/","FromCurrency"),
                                               xmlWriter);
                                        
                                            if (localToCurrency==null){
                                                 throw new org.apache.axis2.databinding.ADBException("ToCurrency cannot be null!!");
                                            }
                                           localToCurrency.serialize(new javax.xml.namespace.QName("http://www.webserviceX.NET/","ToCurrency"),
                                               xmlWriter);
                                        
                    xmlWriter.writeEndElement();
               

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.webserviceX.NET/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        
        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                
                            elementList.add(new javax.xml.namespace.QName("http://www.webserviceX.NET/",
                                                                      "FromCurrency"));
                            
                            
                                    if (localFromCurrency==null){
                                         throw new org.apache.axis2.databinding.ADBException("FromCurrency cannot be null!!");
                                    }
                                    elementList.add(localFromCurrency);
                                
                            elementList.add(new javax.xml.namespace.QName("http://www.webserviceX.NET/",
                                                                      "ToCurrency"));
                            
                            
                                    if (localToCurrency==null){
                                         throw new org.apache.axis2.databinding.ADBException("ToCurrency cannot be null!!");
                                    }
                                    elementList.add(localToCurrency);
                                

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ConversionRate parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            ConversionRate object =
                new ConversionRate();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"ConversionRate".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ConversionRate)ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.webserviceX.NET/","FromCurrency").equals(reader.getName())){
                                
                                                object.setFromCurrency(Currency.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.webserviceX.NET/","ToCurrency").equals(reader.getName())){
                                
                                                object.setToCurrency(Currency.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                              
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    
        public static class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://www.webserviceX.NET/".equals(namespaceURI) &&
                  "Currency".equals(typeName)){
                   
                            return  Currency.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    
        public static class _double
        implements org.apache.axis2.databinding.ADBBean{
        
                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://www.webserviceX.NET/",
                "double",
                "ns1");

            

                        /**
                        * field for _double
                        */

                        
                                    protected double local_double ;
                                

                           /**
                           * Auto generated getter method
                           * @return double
                           */
                           public  double get_double(){
                               return local_double;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param _double
                               */
                               public void set_double(double param){
                            
                                            this.local_double=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                
                //We can safely assume an element has only one type associated with it
                
                            java.lang.String namespace = "http://www.webserviceX.NET/";
                            java.lang.String _localName = "double";
                        
                            writeStartElement(null, namespace, _localName, xmlWriter);

                            // add the type details if this is used in a simple type
                               if (serializeType){
                                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.webserviceX.NET/");
                                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           namespacePrefix+":double",
                                           xmlWriter);
                                   } else {
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           "double",
                                           xmlWriter);
                                   }
                               }
                            
                                               if (java.lang.Double.isNaN(local_double)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("property value cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_double));
                                               }
                            
                            xmlWriter.writeEndElement();
                    

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.webserviceX.NET/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        
        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                
                //We can safely assume an element has only one type associated with it
                 return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME,
                            new java.lang.Object[]{
                            org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                            org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_double)
                            },
                            null);

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static _double parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            _double object =
                new _double();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                   
                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){
                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.webserviceX.NET/","double").equals(reader.getName())){
                                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"double" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                              object.set_double(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDouble(content));
                                            
                              }  // End of if for expected property start element
                                
                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }
                          
                             } else {
                                reader.next();
                             }  
                           }  // end of while loop
                        



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    
        public static class Currency
        implements org.apache.axis2.databinding.ADBBean{
        
                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://www.webserviceX.NET/",
                "Currency",
                "ns1");

            

                        /**
                        * field for Currency
                        */

                        
                                    protected java.lang.String localCurrency ;
                                
                            private static java.util.HashMap _table_ = new java.util.HashMap();

                            // Constructor
                            
                                protected Currency(java.lang.String value, boolean isRegisterValue) {
                                    localCurrency = value;
                                    if (isRegisterValue){
                                        
                                               _table_.put(localCurrency, this);
                                           
                                    }

                                }
                            
                                    public static final java.lang.String _AFA =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("AFA");
                                
                                    public static final java.lang.String _ALL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ALL");
                                
                                    public static final java.lang.String _DZD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("DZD");
                                
                                    public static final java.lang.String _ARS =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ARS");
                                
                                    public static final java.lang.String _AWG =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("AWG");
                                
                                    public static final java.lang.String _AUD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("AUD");
                                
                                    public static final java.lang.String _BSD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BSD");
                                
                                    public static final java.lang.String _BHD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BHD");
                                
                                    public static final java.lang.String _BDT =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BDT");
                                
                                    public static final java.lang.String _BBD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BBD");
                                
                                    public static final java.lang.String _BZD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BZD");
                                
                                    public static final java.lang.String _BMD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BMD");
                                
                                    public static final java.lang.String _BTN =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BTN");
                                
                                    public static final java.lang.String _BOB =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BOB");
                                
                                    public static final java.lang.String _BWP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BWP");
                                
                                    public static final java.lang.String _BRL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BRL");
                                
                                    public static final java.lang.String _GBP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GBP");
                                
                                    public static final java.lang.String _BND =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BND");
                                
                                    public static final java.lang.String _BIF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("BIF");
                                
                                    public static final java.lang.String _XOF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XOF");
                                
                                    public static final java.lang.String _XAF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XAF");
                                
                                    public static final java.lang.String _KHR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KHR");
                                
                                    public static final java.lang.String _CAD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CAD");
                                
                                    public static final java.lang.String _CVE =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CVE");
                                
                                    public static final java.lang.String _KYD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KYD");
                                
                                    public static final java.lang.String _CLP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CLP");
                                
                                    public static final java.lang.String _CNY =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CNY");
                                
                                    public static final java.lang.String _COP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("COP");
                                
                                    public static final java.lang.String _KMF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KMF");
                                
                                    public static final java.lang.String _CRC =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CRC");
                                
                                    public static final java.lang.String _HRK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("HRK");
                                
                                    public static final java.lang.String _CUP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CUP");
                                
                                    public static final java.lang.String _CYP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CYP");
                                
                                    public static final java.lang.String _CZK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CZK");
                                
                                    public static final java.lang.String _DKK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("DKK");
                                
                                    public static final java.lang.String _DJF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("DJF");
                                
                                    public static final java.lang.String _DOP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("DOP");
                                
                                    public static final java.lang.String _XCD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XCD");
                                
                                    public static final java.lang.String _EGP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("EGP");
                                
                                    public static final java.lang.String _SVC =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SVC");
                                
                                    public static final java.lang.String _EEK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("EEK");
                                
                                    public static final java.lang.String _ETB =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ETB");
                                
                                    public static final java.lang.String _EUR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("EUR");
                                
                                    public static final java.lang.String _FKP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("FKP");
                                
                                    public static final java.lang.String _GMD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GMD");
                                
                                    public static final java.lang.String _GHC =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GHC");
                                
                                    public static final java.lang.String _GIP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GIP");
                                
                                    public static final java.lang.String _XAU =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XAU");
                                
                                    public static final java.lang.String _GTQ =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GTQ");
                                
                                    public static final java.lang.String _GNF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GNF");
                                
                                    public static final java.lang.String _GYD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("GYD");
                                
                                    public static final java.lang.String _HTG =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("HTG");
                                
                                    public static final java.lang.String _HNL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("HNL");
                                
                                    public static final java.lang.String _HKD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("HKD");
                                
                                    public static final java.lang.String _HUF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("HUF");
                                
                                    public static final java.lang.String _ISK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ISK");
                                
                                    public static final java.lang.String _INR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("INR");
                                
                                    public static final java.lang.String _IDR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("IDR");
                                
                                    public static final java.lang.String _IQD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("IQD");
                                
                                    public static final java.lang.String _ILS =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ILS");
                                
                                    public static final java.lang.String _JMD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("JMD");
                                
                                    public static final java.lang.String _JPY =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("JPY");
                                
                                    public static final java.lang.String _JOD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("JOD");
                                
                                    public static final java.lang.String _KZT =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KZT");
                                
                                    public static final java.lang.String _KES =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KES");
                                
                                    public static final java.lang.String _KRW =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KRW");
                                
                                    public static final java.lang.String _KWD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KWD");
                                
                                    public static final java.lang.String _LAK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LAK");
                                
                                    public static final java.lang.String _LVL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LVL");
                                
                                    public static final java.lang.String _LBP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LBP");
                                
                                    public static final java.lang.String _LSL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LSL");
                                
                                    public static final java.lang.String _LRD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LRD");
                                
                                    public static final java.lang.String _LYD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LYD");
                                
                                    public static final java.lang.String _LTL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LTL");
                                
                                    public static final java.lang.String _MOP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MOP");
                                
                                    public static final java.lang.String _MKD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MKD");
                                
                                    public static final java.lang.String _MGF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MGF");
                                
                                    public static final java.lang.String _MWK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MWK");
                                
                                    public static final java.lang.String _MYR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MYR");
                                
                                    public static final java.lang.String _MVR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MVR");
                                
                                    public static final java.lang.String _MTL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MTL");
                                
                                    public static final java.lang.String _MRO =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MRO");
                                
                                    public static final java.lang.String _MUR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MUR");
                                
                                    public static final java.lang.String _MXN =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MXN");
                                
                                    public static final java.lang.String _MDL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MDL");
                                
                                    public static final java.lang.String _MNT =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MNT");
                                
                                    public static final java.lang.String _MAD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MAD");
                                
                                    public static final java.lang.String _MZM =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MZM");
                                
                                    public static final java.lang.String _MMK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("MMK");
                                
                                    public static final java.lang.String _NAD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("NAD");
                                
                                    public static final java.lang.String _NPR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("NPR");
                                
                                    public static final java.lang.String _ANG =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ANG");
                                
                                    public static final java.lang.String _NZD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("NZD");
                                
                                    public static final java.lang.String _NIO =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("NIO");
                                
                                    public static final java.lang.String _NGN =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("NGN");
                                
                                    public static final java.lang.String _KPW =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("KPW");
                                
                                    public static final java.lang.String _NOK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("NOK");
                                
                                    public static final java.lang.String _OMR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("OMR");
                                
                                    public static final java.lang.String _XPF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XPF");
                                
                                    public static final java.lang.String _PKR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PKR");
                                
                                    public static final java.lang.String _XPD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XPD");
                                
                                    public static final java.lang.String _PAB =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PAB");
                                
                                    public static final java.lang.String _PGK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PGK");
                                
                                    public static final java.lang.String _PYG =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PYG");
                                
                                    public static final java.lang.String _PEN =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PEN");
                                
                                    public static final java.lang.String _PHP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PHP");
                                
                                    public static final java.lang.String _XPT =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XPT");
                                
                                    public static final java.lang.String _PLN =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("PLN");
                                
                                    public static final java.lang.String _QAR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("QAR");
                                
                                    public static final java.lang.String _ROL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ROL");
                                
                                    public static final java.lang.String _RUB =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("RUB");
                                
                                    public static final java.lang.String _WST =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("WST");
                                
                                    public static final java.lang.String _STD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("STD");
                                
                                    public static final java.lang.String _SAR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SAR");
                                
                                    public static final java.lang.String _SCR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SCR");
                                
                                    public static final java.lang.String _SLL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SLL");
                                
                                    public static final java.lang.String _XAG =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("XAG");
                                
                                    public static final java.lang.String _SGD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SGD");
                                
                                    public static final java.lang.String _SKK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SKK");
                                
                                    public static final java.lang.String _SIT =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SIT");
                                
                                    public static final java.lang.String _SBD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SBD");
                                
                                    public static final java.lang.String _SOS =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SOS");
                                
                                    public static final java.lang.String _ZAR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ZAR");
                                
                                    public static final java.lang.String _LKR =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("LKR");
                                
                                    public static final java.lang.String _SHP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SHP");
                                
                                    public static final java.lang.String _SDD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SDD");
                                
                                    public static final java.lang.String _SRG =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SRG");
                                
                                    public static final java.lang.String _SZL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SZL");
                                
                                    public static final java.lang.String _SEK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SEK");
                                
                                    public static final java.lang.String _CHF =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CHF");
                                
                                    public static final java.lang.String _SYP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SYP");
                                
                                    public static final java.lang.String _TWD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TWD");
                                
                                    public static final java.lang.String _TZS =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TZS");
                                
                                    public static final java.lang.String _THB =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("THB");
                                
                                    public static final java.lang.String _TOP =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TOP");
                                
                                    public static final java.lang.String _TTD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TTD");
                                
                                    public static final java.lang.String _TND =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TND");
                                
                                    public static final java.lang.String _TRL =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TRL");
                                
                                    public static final java.lang.String _USD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("USD");
                                
                                    public static final java.lang.String _AED =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("AED");
                                
                                    public static final java.lang.String _UGX =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("UGX");
                                
                                    public static final java.lang.String _UAH =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("UAH");
                                
                                    public static final java.lang.String _UYU =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("UYU");
                                
                                    public static final java.lang.String _VUV =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("VUV");
                                
                                    public static final java.lang.String _VEB =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("VEB");
                                
                                    public static final java.lang.String _VND =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("VND");
                                
                                    public static final java.lang.String _YER =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("YER");
                                
                                    public static final java.lang.String _YUM =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("YUM");
                                
                                    public static final java.lang.String _ZMK =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ZMK");
                                
                                    public static final java.lang.String _ZWD =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("ZWD");
                                
                                    public static final java.lang.String _TRY =
                                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString("TRY");
                                
                                public static final Currency AFA =
                                    new Currency(_AFA,true);
                            
                                public static final Currency ALL =
                                    new Currency(_ALL,true);
                            
                                public static final Currency DZD =
                                    new Currency(_DZD,true);
                            
                                public static final Currency ARS =
                                    new Currency(_ARS,true);
                            
                                public static final Currency AWG =
                                    new Currency(_AWG,true);
                            
                                public static final Currency AUD =
                                    new Currency(_AUD,true);
                            
                                public static final Currency BSD =
                                    new Currency(_BSD,true);
                            
                                public static final Currency BHD =
                                    new Currency(_BHD,true);
                            
                                public static final Currency BDT =
                                    new Currency(_BDT,true);
                            
                                public static final Currency BBD =
                                    new Currency(_BBD,true);
                            
                                public static final Currency BZD =
                                    new Currency(_BZD,true);
                            
                                public static final Currency BMD =
                                    new Currency(_BMD,true);
                            
                                public static final Currency BTN =
                                    new Currency(_BTN,true);
                            
                                public static final Currency BOB =
                                    new Currency(_BOB,true);
                            
                                public static final Currency BWP =
                                    new Currency(_BWP,true);
                            
                                public static final Currency BRL =
                                    new Currency(_BRL,true);
                            
                                public static final Currency GBP =
                                    new Currency(_GBP,true);
                            
                                public static final Currency BND =
                                    new Currency(_BND,true);
                            
                                public static final Currency BIF =
                                    new Currency(_BIF,true);
                            
                                public static final Currency XOF =
                                    new Currency(_XOF,true);
                            
                                public static final Currency XAF =
                                    new Currency(_XAF,true);
                            
                                public static final Currency KHR =
                                    new Currency(_KHR,true);
                            
                                public static final Currency CAD =
                                    new Currency(_CAD,true);
                            
                                public static final Currency CVE =
                                    new Currency(_CVE,true);
                            
                                public static final Currency KYD =
                                    new Currency(_KYD,true);
                            
                                public static final Currency CLP =
                                    new Currency(_CLP,true);
                            
                                public static final Currency CNY =
                                    new Currency(_CNY,true);
                            
                                public static final Currency COP =
                                    new Currency(_COP,true);
                            
                                public static final Currency KMF =
                                    new Currency(_KMF,true);
                            
                                public static final Currency CRC =
                                    new Currency(_CRC,true);
                            
                                public static final Currency HRK =
                                    new Currency(_HRK,true);
                            
                                public static final Currency CUP =
                                    new Currency(_CUP,true);
                            
                                public static final Currency CYP =
                                    new Currency(_CYP,true);
                            
                                public static final Currency CZK =
                                    new Currency(_CZK,true);
                            
                                public static final Currency DKK =
                                    new Currency(_DKK,true);
                            
                                public static final Currency DJF =
                                    new Currency(_DJF,true);
                            
                                public static final Currency DOP =
                                    new Currency(_DOP,true);
                            
                                public static final Currency XCD =
                                    new Currency(_XCD,true);
                            
                                public static final Currency EGP =
                                    new Currency(_EGP,true);
                            
                                public static final Currency SVC =
                                    new Currency(_SVC,true);
                            
                                public static final Currency EEK =
                                    new Currency(_EEK,true);
                            
                                public static final Currency ETB =
                                    new Currency(_ETB,true);
                            
                                public static final Currency EUR =
                                    new Currency(_EUR,true);
                            
                                public static final Currency FKP =
                                    new Currency(_FKP,true);
                            
                                public static final Currency GMD =
                                    new Currency(_GMD,true);
                            
                                public static final Currency GHC =
                                    new Currency(_GHC,true);
                            
                                public static final Currency GIP =
                                    new Currency(_GIP,true);
                            
                                public static final Currency XAU =
                                    new Currency(_XAU,true);
                            
                                public static final Currency GTQ =
                                    new Currency(_GTQ,true);
                            
                                public static final Currency GNF =
                                    new Currency(_GNF,true);
                            
                                public static final Currency GYD =
                                    new Currency(_GYD,true);
                            
                                public static final Currency HTG =
                                    new Currency(_HTG,true);
                            
                                public static final Currency HNL =
                                    new Currency(_HNL,true);
                            
                                public static final Currency HKD =
                                    new Currency(_HKD,true);
                            
                                public static final Currency HUF =
                                    new Currency(_HUF,true);
                            
                                public static final Currency ISK =
                                    new Currency(_ISK,true);
                            
                                public static final Currency INR =
                                    new Currency(_INR,true);
                            
                                public static final Currency IDR =
                                    new Currency(_IDR,true);
                            
                                public static final Currency IQD =
                                    new Currency(_IQD,true);
                            
                                public static final Currency ILS =
                                    new Currency(_ILS,true);
                            
                                public static final Currency JMD =
                                    new Currency(_JMD,true);
                            
                                public static final Currency JPY =
                                    new Currency(_JPY,true);
                            
                                public static final Currency JOD =
                                    new Currency(_JOD,true);
                            
                                public static final Currency KZT =
                                    new Currency(_KZT,true);
                            
                                public static final Currency KES =
                                    new Currency(_KES,true);
                            
                                public static final Currency KRW =
                                    new Currency(_KRW,true);
                            
                                public static final Currency KWD =
                                    new Currency(_KWD,true);
                            
                                public static final Currency LAK =
                                    new Currency(_LAK,true);
                            
                                public static final Currency LVL =
                                    new Currency(_LVL,true);
                            
                                public static final Currency LBP =
                                    new Currency(_LBP,true);
                            
                                public static final Currency LSL =
                                    new Currency(_LSL,true);
                            
                                public static final Currency LRD =
                                    new Currency(_LRD,true);
                            
                                public static final Currency LYD =
                                    new Currency(_LYD,true);
                            
                                public static final Currency LTL =
                                    new Currency(_LTL,true);
                            
                                public static final Currency MOP =
                                    new Currency(_MOP,true);
                            
                                public static final Currency MKD =
                                    new Currency(_MKD,true);
                            
                                public static final Currency MGF =
                                    new Currency(_MGF,true);
                            
                                public static final Currency MWK =
                                    new Currency(_MWK,true);
                            
                                public static final Currency MYR =
                                    new Currency(_MYR,true);
                            
                                public static final Currency MVR =
                                    new Currency(_MVR,true);
                            
                                public static final Currency MTL =
                                    new Currency(_MTL,true);
                            
                                public static final Currency MRO =
                                    new Currency(_MRO,true);
                            
                                public static final Currency MUR =
                                    new Currency(_MUR,true);
                            
                                public static final Currency MXN =
                                    new Currency(_MXN,true);
                            
                                public static final Currency MDL =
                                    new Currency(_MDL,true);
                            
                                public static final Currency MNT =
                                    new Currency(_MNT,true);
                            
                                public static final Currency MAD =
                                    new Currency(_MAD,true);
                            
                                public static final Currency MZM =
                                    new Currency(_MZM,true);
                            
                                public static final Currency MMK =
                                    new Currency(_MMK,true);
                            
                                public static final Currency NAD =
                                    new Currency(_NAD,true);
                            
                                public static final Currency NPR =
                                    new Currency(_NPR,true);
                            
                                public static final Currency ANG =
                                    new Currency(_ANG,true);
                            
                                public static final Currency NZD =
                                    new Currency(_NZD,true);
                            
                                public static final Currency NIO =
                                    new Currency(_NIO,true);
                            
                                public static final Currency NGN =
                                    new Currency(_NGN,true);
                            
                                public static final Currency KPW =
                                    new Currency(_KPW,true);
                            
                                public static final Currency NOK =
                                    new Currency(_NOK,true);
                            
                                public static final Currency OMR =
                                    new Currency(_OMR,true);
                            
                                public static final Currency XPF =
                                    new Currency(_XPF,true);
                            
                                public static final Currency PKR =
                                    new Currency(_PKR,true);
                            
                                public static final Currency XPD =
                                    new Currency(_XPD,true);
                            
                                public static final Currency PAB =
                                    new Currency(_PAB,true);
                            
                                public static final Currency PGK =
                                    new Currency(_PGK,true);
                            
                                public static final Currency PYG =
                                    new Currency(_PYG,true);
                            
                                public static final Currency PEN =
                                    new Currency(_PEN,true);
                            
                                public static final Currency PHP =
                                    new Currency(_PHP,true);
                            
                                public static final Currency XPT =
                                    new Currency(_XPT,true);
                            
                                public static final Currency PLN =
                                    new Currency(_PLN,true);
                            
                                public static final Currency QAR =
                                    new Currency(_QAR,true);
                            
                                public static final Currency ROL =
                                    new Currency(_ROL,true);
                            
                                public static final Currency RUB =
                                    new Currency(_RUB,true);
                            
                                public static final Currency WST =
                                    new Currency(_WST,true);
                            
                                public static final Currency STD =
                                    new Currency(_STD,true);
                            
                                public static final Currency SAR =
                                    new Currency(_SAR,true);
                            
                                public static final Currency SCR =
                                    new Currency(_SCR,true);
                            
                                public static final Currency SLL =
                                    new Currency(_SLL,true);
                            
                                public static final Currency XAG =
                                    new Currency(_XAG,true);
                            
                                public static final Currency SGD =
                                    new Currency(_SGD,true);
                            
                                public static final Currency SKK =
                                    new Currency(_SKK,true);
                            
                                public static final Currency SIT =
                                    new Currency(_SIT,true);
                            
                                public static final Currency SBD =
                                    new Currency(_SBD,true);
                            
                                public static final Currency SOS =
                                    new Currency(_SOS,true);
                            
                                public static final Currency ZAR =
                                    new Currency(_ZAR,true);
                            
                                public static final Currency LKR =
                                    new Currency(_LKR,true);
                            
                                public static final Currency SHP =
                                    new Currency(_SHP,true);
                            
                                public static final Currency SDD =
                                    new Currency(_SDD,true);
                            
                                public static final Currency SRG =
                                    new Currency(_SRG,true);
                            
                                public static final Currency SZL =
                                    new Currency(_SZL,true);
                            
                                public static final Currency SEK =
                                    new Currency(_SEK,true);
                            
                                public static final Currency CHF =
                                    new Currency(_CHF,true);
                            
                                public static final Currency SYP =
                                    new Currency(_SYP,true);
                            
                                public static final Currency TWD =
                                    new Currency(_TWD,true);
                            
                                public static final Currency TZS =
                                    new Currency(_TZS,true);
                            
                                public static final Currency THB =
                                    new Currency(_THB,true);
                            
                                public static final Currency TOP =
                                    new Currency(_TOP,true);
                            
                                public static final Currency TTD =
                                    new Currency(_TTD,true);
                            
                                public static final Currency TND =
                                    new Currency(_TND,true);
                            
                                public static final Currency TRL =
                                    new Currency(_TRL,true);
                            
                                public static final Currency USD =
                                    new Currency(_USD,true);
                            
                                public static final Currency AED =
                                    new Currency(_AED,true);
                            
                                public static final Currency UGX =
                                    new Currency(_UGX,true);
                            
                                public static final Currency UAH =
                                    new Currency(_UAH,true);
                            
                                public static final Currency UYU =
                                    new Currency(_UYU,true);
                            
                                public static final Currency VUV =
                                    new Currency(_VUV,true);
                            
                                public static final Currency VEB =
                                    new Currency(_VEB,true);
                            
                                public static final Currency VND =
                                    new Currency(_VND,true);
                            
                                public static final Currency YER =
                                    new Currency(_YER,true);
                            
                                public static final Currency YUM =
                                    new Currency(_YUM,true);
                            
                                public static final Currency ZMK =
                                    new Currency(_ZMK,true);
                            
                                public static final Currency ZWD =
                                    new Currency(_ZWD,true);
                            
                                public static final Currency TRY =
                                    new Currency(_TRY,true);
                            

                                public java.lang.String getValue() { return localCurrency;}

                                public boolean equals(java.lang.Object obj) {return (obj == this);}
                                public int hashCode() { return toString().hashCode();}
                                public java.lang.String toString() {
                                
                                        return localCurrency.toString();
                                    

                                }

                        

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                
                //We can safely assume an element has only one type associated with it
                
                            java.lang.String namespace = parentQName.getNamespaceURI();
                            java.lang.String _localName = parentQName.getLocalPart();
                        
                            writeStartElement(null, namespace, _localName, xmlWriter);

                            // add the type details if this is used in a simple type
                               if (serializeType){
                                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.webserviceX.NET/");
                                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           namespacePrefix+":Currency",
                                           xmlWriter);
                                   } else {
                                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                                           "Currency",
                                           xmlWriter);
                                   }
                               }
                            
                                          if (localCurrency==null){
                                            
                                                     throw new org.apache.axis2.databinding.ADBException("Currency cannot be null !!");
                                                
                                         }else{
                                        
                                                       xmlWriter.writeCharacters(localCurrency);
                                            
                                         }
                                    
                            xmlWriter.writeEndElement();
                    

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.webserviceX.NET/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        
        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                
                //We can safely assume an element has only one type associated with it
                 return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME,
                            new java.lang.Object[]{
                            org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                            org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCurrency)
                            },
                            null);

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        
                public static Currency fromValue(java.lang.String value)
                      throws java.lang.IllegalArgumentException {
                    Currency enumeration = (Currency)
                       
                               _table_.get(value);
                           

                    if ((enumeration == null) && !((value == null) || (value.equals("")))) {
                        throw new java.lang.IllegalArgumentException();
                    }
                    return enumeration;
                }
                public static Currency fromString(java.lang.String value,java.lang.String namespaceURI)
                      throws java.lang.IllegalArgumentException {
                    try {
                       
                                       return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));
                                   

                    } catch (java.lang.Exception e) {
                        throw new java.lang.IllegalArgumentException();
                    }
                }

                public static Currency fromString(javax.xml.stream.XMLStreamReader xmlStreamReader,
                                                                    java.lang.String content) {
                    if (content.indexOf(":") > -1){
                        java.lang.String prefix = content.substring(0,content.indexOf(":"));
                        java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
                        return Currency.Factory.fromString(content,namespaceUri);
                    } else {
                       return Currency.Factory.fromString(content,"");
                    }
                }
            

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static Currency parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            Currency object = null;
                // initialize a hash map to keep values
                java.util.Map attributeMap = new java.util.HashMap();
                java.util.List extraAttributeList = new java.util.ArrayList<org.apache.axiom.om.OMAttribute>();
            

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                   
                while(!reader.isEndElement()) {
                    if (reader.isStartElement()  || reader.hasText()){
                
                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"Currency" +"  cannot be null");
                                    }
                                    

                                    java.lang.String content = reader.getElementText();
                                    
                                        if (content.indexOf(":") > 0) {
                                            // this seems to be a Qname so find the namespace and send
                                            prefix = content.substring(0, content.indexOf(":"));
                                            namespaceuri = reader.getNamespaceURI(prefix);
                                            object = Currency.Factory.fromString(content,namespaceuri);
                                        } else {
                                            // this seems to be not a qname send and empty namespace incase of it is
                                            // check is done in fromString method
                                            object = Currency.Factory.fromString(content,"");
                                        }
                                        
                                        
                             } else {
                                reader.next();
                             }  
                           }  // end of while loop
                        



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    
            private  org.apache.axiom.om.OMElement  toOM(com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate.class.equals(type)){
                
                           return com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse.class.equals(type)){
                
                           return com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    
   }
   