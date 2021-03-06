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
 * <p>Java class for RecipientAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecipientAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="name" type="{http://www.canadapost.ca/ws/shipment-v4}ContactNameType" minOccurs="0"/>
 *         &lt;element name="company" type="{http://www.canadapost.ca/ws/shipment-v4}CompanyNameType" minOccurs="0"/>
 *         &lt;element name="additional-address-info" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;maxLength value="44"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="client-voice-number" type="{http://www.canadapost.ca/ws/shipment-v4}PhoneNumberType" minOccurs="0"/>
 *         &lt;element name="address-details" type="{http://www.canadapost.ca/ws/shipment-v4}DestinationAddressDetailsType"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientAddressType", propOrder = {

})
public class RecipientAddressType {

    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String company;
    @XmlElement(name = "additional-address-info")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String additionalAddressInfo;
    @XmlElement(name = "client-voice-number")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String clientVoiceNumber;
    @XmlElement(name = "address-details", required = true)
    protected DestinationAddressDetailsType addressDetails;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the company property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the value of the company property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompany(String value) {
        this.company = value;
    }

    /**
     * Gets the value of the additionalAddressInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalAddressInfo() {
        return additionalAddressInfo;
    }

    /**
     * Sets the value of the additionalAddressInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalAddressInfo(String value) {
        this.additionalAddressInfo = value;
    }

    /**
     * Gets the value of the clientVoiceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientVoiceNumber() {
        return clientVoiceNumber;
    }

    /**
     * Sets the value of the clientVoiceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientVoiceNumber(String value) {
        this.clientVoiceNumber = value;
    }

    /**
     * Gets the value of the addressDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DestinationAddressDetailsType }
     *     
     */
    public DestinationAddressDetailsType getAddressDetails() {
        return addressDetails;
    }

    /**
     * Sets the value of the addressDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinationAddressDetailsType }
     *     
     */
    public void setAddressDetails(DestinationAddressDetailsType value) {
        this.addressDetails = value;
    }

}
