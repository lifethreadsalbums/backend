//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:01 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.shipment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ShipmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://www.canadapost.ca/ws/shipment-v4}groupIdOrTransmitShipment"/>
 *         &lt;element name="requested-shipping-point" type="{http://www.canadapost.ca/ws/shipment-v4}PostalCodeType"/>
 *         &lt;element name="expected-mailing-date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="delivery-spec" type="{http://www.canadapost.ca/ws/shipment-v4}DeliverySpecType"/>
 *         &lt;element name="return-spec" type="{http://www.canadapost.ca/ws/shipment-v4}ReturnSpecType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentType", propOrder = {

})
@XmlRootElement(name = "shipment")
public class Shipment {

    @XmlElementRef(name = "groupIdOrTransmitShipment", namespace = "http://www.canadapost.ca/ws/shipment-v4", type = JAXBElement.class)
    protected JAXBElement<?> groupIdOrTransmitShipment;
    @XmlElement(name = "requested-shipping-point", required = true)
    protected String requestedShippingPoint;
    @XmlElement(name = "expected-mailing-date")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar expectedMailingDate;
    @XmlElement(name = "delivery-spec", required = true)
    protected DeliverySpecType deliverySpec;
    @XmlElement(name = "return-spec")
    protected ReturnSpecType returnSpec;

    /**
     * Gets the value of the groupIdOrTransmitShipment property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<?> getGroupIdOrTransmitShipment() {
        return groupIdOrTransmitShipment;
    }

    /**
     * Sets the value of the groupIdOrTransmitShipment property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGroupIdOrTransmitShipment(JAXBElement<?> value) {
        this.groupIdOrTransmitShipment = value;
    }

    /**
     * Gets the value of the requestedShippingPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestedShippingPoint() {
        return requestedShippingPoint;
    }

    /**
     * Sets the value of the requestedShippingPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestedShippingPoint(String value) {
        this.requestedShippingPoint = value;
    }

    /**
     * Gets the value of the expectedMailingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpectedMailingDate() {
        return expectedMailingDate;
    }

    /**
     * Sets the value of the expectedMailingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpectedMailingDate(XMLGregorianCalendar value) {
        this.expectedMailingDate = value;
    }

    /**
     * Gets the value of the deliverySpec property.
     * 
     * @return
     *     possible object is
     *     {@link DeliverySpecType }
     *     
     */
    public DeliverySpecType getDeliverySpec() {
        return deliverySpec;
    }

    /**
     * Sets the value of the deliverySpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliverySpecType }
     *     
     */
    public void setDeliverySpec(DeliverySpecType value) {
        this.deliverySpec = value;
    }

    /**
     * Gets the value of the returnSpec property.
     * 
     * @return
     *     possible object is
     *     {@link ReturnSpecType }
     *     
     */
    public ReturnSpecType getReturnSpec() {
        return returnSpec;
    }

    /**
     * Sets the value of the returnSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnSpecType }
     *     
     */
    public void setReturnSpec(ReturnSpecType value) {
        this.returnSpec = value;
    }

}