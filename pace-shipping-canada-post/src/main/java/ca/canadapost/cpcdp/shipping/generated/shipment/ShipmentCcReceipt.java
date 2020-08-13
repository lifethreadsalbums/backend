//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:01 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.shipment;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ShipmentReceiptType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipmentReceiptType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="cc-receipt-details">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="merchant-name">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *                         &lt;maxLength value="44"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="merchant-url">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *                         &lt;maxLength value="60"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="name-on-card" type="{http://www.canadapost.ca/ws/shipment-v4}CcNameType" minOccurs="0"/>
 *                   &lt;element name="auth-code" type="{http://www.canadapost.ca/ws/shipment-v4}CcAuthorizationCodeType"/>
 *                   &lt;element name="auth-timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                   &lt;element name="card-type" type="{http://www.canadapost.ca/ws/shipment-v4}CcType"/>
 *                   &lt;element name="charge-amount" type="{http://www.canadapost.ca/ws/shipment-v4}CcAuthorizationAmountType"/>
 *                   &lt;element name="currency" type="{http://www.canadapost.ca/ws/shipment-v4}CurrencyType"/>
 *                   &lt;element name="transaction-type">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *                         &lt;maxLength value="44"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentReceiptType", propOrder = {

})
@XmlRootElement(name = "shipment-cc-receipt")
public class ShipmentCcReceipt {

    @XmlElement(name = "cc-receipt-details", required = true)
    protected ShipmentCcReceipt.CcReceiptDetails ccReceiptDetails;

    /**
     * Gets the value of the ccReceiptDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ShipmentCcReceipt.CcReceiptDetails }
     *     
     */
    public ShipmentCcReceipt.CcReceiptDetails getCcReceiptDetails() {
        return ccReceiptDetails;
    }

    /**
     * Sets the value of the ccReceiptDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipmentCcReceipt.CcReceiptDetails }
     *     
     */
    public void setCcReceiptDetails(ShipmentCcReceipt.CcReceiptDetails value) {
        this.ccReceiptDetails = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="merchant-name">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *               &lt;maxLength value="44"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="merchant-url">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *               &lt;maxLength value="60"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="name-on-card" type="{http://www.canadapost.ca/ws/shipment-v4}CcNameType" minOccurs="0"/>
     *         &lt;element name="auth-code" type="{http://www.canadapost.ca/ws/shipment-v4}CcAuthorizationCodeType"/>
     *         &lt;element name="auth-timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *         &lt;element name="card-type" type="{http://www.canadapost.ca/ws/shipment-v4}CcType"/>
     *         &lt;element name="charge-amount" type="{http://www.canadapost.ca/ws/shipment-v4}CcAuthorizationAmountType"/>
     *         &lt;element name="currency" type="{http://www.canadapost.ca/ws/shipment-v4}CurrencyType"/>
     *         &lt;element name="transaction-type">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
     *               &lt;maxLength value="44"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class CcReceiptDetails {

        @XmlElement(name = "merchant-name", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String merchantName;
        @XmlElement(name = "merchant-url", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String merchantUrl;
        @XmlElement(name = "name-on-card")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String nameOnCard;
        @XmlElement(name = "auth-code", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String authCode;
        @XmlElement(name = "auth-timestamp", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar authTimestamp;
        @XmlElement(name = "card-type", required = true)
        protected CcType cardType;
        @XmlElement(name = "charge-amount", required = true)
        protected BigDecimal chargeAmount;
        @XmlElement(required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String currency;
        @XmlElement(name = "transaction-type", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String transactionType;

        /**
         * Gets the value of the merchantName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMerchantName() {
            return merchantName;
        }

        /**
         * Sets the value of the merchantName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMerchantName(String value) {
            this.merchantName = value;
        }

        /**
         * Gets the value of the merchantUrl property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMerchantUrl() {
            return merchantUrl;
        }

        /**
         * Sets the value of the merchantUrl property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMerchantUrl(String value) {
            this.merchantUrl = value;
        }

        /**
         * Gets the value of the nameOnCard property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNameOnCard() {
            return nameOnCard;
        }

        /**
         * Sets the value of the nameOnCard property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNameOnCard(String value) {
            this.nameOnCard = value;
        }

        /**
         * Gets the value of the authCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuthCode() {
            return authCode;
        }

        /**
         * Sets the value of the authCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuthCode(String value) {
            this.authCode = value;
        }

        /**
         * Gets the value of the authTimestamp property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getAuthTimestamp() {
            return authTimestamp;
        }

        /**
         * Sets the value of the authTimestamp property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setAuthTimestamp(XMLGregorianCalendar value) {
            this.authTimestamp = value;
        }

        /**
         * Gets the value of the cardType property.
         * 
         * @return
         *     possible object is
         *     {@link CcType }
         *     
         */
        public CcType getCardType() {
            return cardType;
        }

        /**
         * Sets the value of the cardType property.
         * 
         * @param value
         *     allowed object is
         *     {@link CcType }
         *     
         */
        public void setCardType(CcType value) {
            this.cardType = value;
        }

        /**
         * Gets the value of the chargeAmount property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getChargeAmount() {
            return chargeAmount;
        }

        /**
         * Sets the value of the chargeAmount property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setChargeAmount(BigDecimal value) {
            this.chargeAmount = value;
        }

        /**
         * Gets the value of the currency property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCurrency() {
            return currency;
        }

        /**
         * Sets the value of the currency property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCurrency(String value) {
            this.currency = value;
        }

        /**
         * Gets the value of the transactionType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransactionType() {
            return transactionType;
        }

        /**
         * Sets the value of the transactionType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransactionType(String value) {
            this.transactionType = value;
        }

    }

}