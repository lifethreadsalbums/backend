//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.29 at 11:26:11 AM EDT 
//


package ca.canadapost.cpcdp.tracking.generated.track;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pin-summary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pin-summary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="pin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origin-postal-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destination-postal-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destination-province" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="service-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mailed-on-date" type="{http://www.canadapost.ca/ws/track}emptyDate"/>
 *         &lt;element name="expected-delivery-date" type="{http://www.canadapost.ca/ws/track}emptyDate"/>
 *         &lt;element name="actual-delivery-date" type="{http://www.canadapost.ca/ws/track}emptyDate"/>
 *         &lt;element name="delivery-option-completed-ind" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="event-date-time" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="event-description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attempted-date" type="{http://www.canadapost.ca/ws/track}emptyDate"/>
 *         &lt;element name="customer-ref-1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customer-ref-2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="return-pin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="event-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="event-location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signatory-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pin-summary", propOrder = {

})
public class PinSummary {

    @XmlElement(required = true)
    protected String pin;
    @XmlElement(name = "origin-postal-id", required = true)
    protected String originPostalId;
    @XmlElement(name = "destination-postal-id", required = true)
    protected String destinationPostalId;
    @XmlElement(name = "destination-province", required = true)
    protected String destinationProvince;
    @XmlElement(name = "service-name", required = true)
    protected String serviceName;
    @XmlElement(name = "mailed-on-date", required = true)
    protected String mailedOnDate;
    @XmlElement(name = "expected-delivery-date", required = true)
    protected String expectedDeliveryDate;
    @XmlElement(name = "actual-delivery-date", required = true)
    protected String actualDeliveryDate;
    @XmlElement(name = "delivery-option-completed-ind", required = true)
    protected String deliveryOptionCompletedInd;
    @XmlElement(name = "event-date-time", required = true)
    protected String eventDateTime;
    @XmlElement(name = "event-description", required = true)
    protected String eventDescription;
    @XmlElement(name = "attempted-date", required = true)
    protected String attemptedDate;
    @XmlElement(name = "customer-ref-1", required = true)
    protected String customerRef1;
    @XmlElement(name = "customer-ref-2", required = true)
    protected String customerRef2;
    @XmlElement(name = "return-pin", required = true)
    protected String returnPin;
    @XmlElement(name = "event-type", required = true)
    protected String eventType;
    @XmlElement(name = "event-location", required = true)
    protected String eventLocation;
    @XmlElement(name = "signatory-name", required = true)
    protected String signatoryName;

    /**
     * Gets the value of the pin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPin() {
        return pin;
    }

    /**
     * Sets the value of the pin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPin(String value) {
        this.pin = value;
    }

    /**
     * Gets the value of the originPostalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginPostalId() {
        return originPostalId;
    }

    /**
     * Sets the value of the originPostalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginPostalId(String value) {
        this.originPostalId = value;
    }

    /**
     * Gets the value of the destinationPostalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationPostalId() {
        return destinationPostalId;
    }

    /**
     * Sets the value of the destinationPostalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationPostalId(String value) {
        this.destinationPostalId = value;
    }

    /**
     * Gets the value of the destinationProvince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationProvince() {
        return destinationProvince;
    }

    /**
     * Sets the value of the destinationProvince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationProvince(String value) {
        this.destinationProvince = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the mailedOnDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailedOnDate() {
        return mailedOnDate;
    }

    /**
     * Sets the value of the mailedOnDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailedOnDate(String value) {
        this.mailedOnDate = value;
    }

    /**
     * Gets the value of the expectedDeliveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * Sets the value of the expectedDeliveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpectedDeliveryDate(String value) {
        this.expectedDeliveryDate = value;
    }

    /**
     * Gets the value of the actualDeliveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    /**
     * Sets the value of the actualDeliveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualDeliveryDate(String value) {
        this.actualDeliveryDate = value;
    }

    /**
     * Gets the value of the deliveryOptionCompletedInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryOptionCompletedInd() {
        return deliveryOptionCompletedInd;
    }

    /**
     * Sets the value of the deliveryOptionCompletedInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryOptionCompletedInd(String value) {
        this.deliveryOptionCompletedInd = value;
    }

    /**
     * Gets the value of the eventDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Sets the value of the eventDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDateTime(String value) {
        this.eventDateTime = value;
    }

    /**
     * Gets the value of the eventDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the value of the eventDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDescription(String value) {
        this.eventDescription = value;
    }

    /**
     * Gets the value of the attemptedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttemptedDate() {
        return attemptedDate;
    }

    /**
     * Sets the value of the attemptedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttemptedDate(String value) {
        this.attemptedDate = value;
    }

    /**
     * Gets the value of the customerRef1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerRef1() {
        return customerRef1;
    }

    /**
     * Sets the value of the customerRef1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerRef1(String value) {
        this.customerRef1 = value;
    }

    /**
     * Gets the value of the customerRef2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerRef2() {
        return customerRef2;
    }

    /**
     * Sets the value of the customerRef2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerRef2(String value) {
        this.customerRef2 = value;
    }

    /**
     * Gets the value of the returnPin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnPin() {
        return returnPin;
    }

    /**
     * Sets the value of the returnPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnPin(String value) {
        this.returnPin = value;
    }

    /**
     * Gets the value of the eventType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the value of the eventType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventType(String value) {
        this.eventType = value;
    }

    /**
     * Gets the value of the eventLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * Sets the value of the eventLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventLocation(String value) {
        this.eventLocation = value;
    }

    /**
     * Gets the value of the signatoryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatoryName() {
        return signatoryName;
    }

    /**
     * Sets the value of the signatoryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatoryName(String value) {
        this.signatoryName = value;
    }

}
