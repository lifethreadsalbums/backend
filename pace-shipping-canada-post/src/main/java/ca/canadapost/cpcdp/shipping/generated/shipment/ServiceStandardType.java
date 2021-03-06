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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ServiceStandardType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceStandardType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="am-delivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="guaranteed-delivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="expected-transmit-time" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="expected-delivery-date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceStandardType", propOrder = {

})
public class ServiceStandardType {

    @XmlElement(name = "am-delivery")
    protected boolean amDelivery;
    @XmlElement(name = "guaranteed-delivery")
    protected boolean guaranteedDelivery;
    @XmlElement(name = "expected-transmit-time")
    protected Integer expectedTransmitTime;
    @XmlElement(name = "expected-delivery-date")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar expectedDeliveryDate;

    /**
     * Gets the value of the amDelivery property.
     * 
     */
    public boolean isAmDelivery() {
        return amDelivery;
    }

    /**
     * Sets the value of the amDelivery property.
     * 
     */
    public void setAmDelivery(boolean value) {
        this.amDelivery = value;
    }

    /**
     * Gets the value of the guaranteedDelivery property.
     * 
     */
    public boolean isGuaranteedDelivery() {
        return guaranteedDelivery;
    }

    /**
     * Sets the value of the guaranteedDelivery property.
     * 
     */
    public void setGuaranteedDelivery(boolean value) {
        this.guaranteedDelivery = value;
    }

    /**
     * Gets the value of the expectedTransmitTime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExpectedTransmitTime() {
        return expectedTransmitTime;
    }

    /**
     * Sets the value of the expectedTransmitTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExpectedTransmitTime(Integer value) {
        this.expectedTransmitTime = value;
    }

    /**
     * Gets the value of the expectedDeliveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * Sets the value of the expectedDeliveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpectedDeliveryDate(XMLGregorianCalendar value) {
        this.expectedDeliveryDate = value;
    }

}
