//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:01 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.shipment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for NotificationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotificationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="email" type="{http://www.canadapost.ca/ws/shipment-v4}EmailType"/>
 *         &lt;element name="on-shipment" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="on-exception" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="on-delivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationType", propOrder = {

})
public class NotificationType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String email;
    @XmlElement(name = "on-shipment")
    protected boolean onShipment;
    @XmlElement(name = "on-exception")
    protected boolean onException;
    @XmlElement(name = "on-delivery")
    protected boolean onDelivery;

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the onShipment property.
     * 
     */
    public boolean isOnShipment() {
        return onShipment;
    }

    /**
     * Sets the value of the onShipment property.
     * 
     */
    public void setOnShipment(boolean value) {
        this.onShipment = value;
    }

    /**
     * Gets the value of the onException property.
     * 
     */
    public boolean isOnException() {
        return onException;
    }

    /**
     * Sets the value of the onException property.
     * 
     */
    public void setOnException(boolean value) {
        this.onException = value;
    }

    /**
     * Gets the value of the onDelivery property.
     * 
     */
    public boolean isOnDelivery() {
        return onDelivery;
    }

    /**
     * Sets the value of the onDelivery property.
     * 
     */
    public void setOnDelivery(boolean value) {
        this.onDelivery = value;
    }

}
