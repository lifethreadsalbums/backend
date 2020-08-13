//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:02 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.manifest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ShipmentTransmitSetType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipmentTransmitSetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="group-ids" type="{http://www.canadapost.ca/ws/manifest-v4}GroupIDListType"/>
 *         &lt;element name="requested-shipping-point" type="{http://www.canadapost.ca/ws/manifest-v4}PostalCodeType"/>
 *         &lt;element name="detailed-manifests" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="method-of-payment" type="{http://www.canadapost.ca/ws/manifest-v4}MethodOfPaymentType"/>
 *         &lt;element name="manifest-address" type="{http://www.canadapost.ca/ws/manifest-v4}ManifestAddressType"/>
 *         &lt;element name="customer-reference" type="{http://www.canadapost.ca/ws/manifest-v4}CustomerReferenceType" minOccurs="0"/>
 *         &lt;element name="excluded-shipments" type="{http://www.canadapost.ca/ws/manifest-v4}ExcludedShipmentsType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentTransmitSetType", propOrder = {

})
@XmlRootElement(name = "transmit-set")
public class TransmitSet {

    @XmlElement(name = "group-ids", required = true)
    protected GroupIDListType groupIds;
    @XmlElement(name = "requested-shipping-point", required = true)
    protected String requestedShippingPoint;
    @XmlElement(name = "detailed-manifests")
    protected boolean detailedManifests;
    @XmlElement(name = "method-of-payment", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String methodOfPayment;
    @XmlElement(name = "manifest-address", required = true)
    protected ManifestAddressType manifestAddress;
    @XmlElement(name = "customer-reference")
    protected String customerReference;
    @XmlElement(name = "excluded-shipments")
    protected ExcludedShipmentsType excludedShipments;

    /**
     * Gets the value of the groupIds property.
     * 
     * @return
     *     possible object is
     *     {@link GroupIDListType }
     *     
     */
    public GroupIDListType getGroupIds() {
        return groupIds;
    }

    /**
     * Sets the value of the groupIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupIDListType }
     *     
     */
    public void setGroupIds(GroupIDListType value) {
        this.groupIds = value;
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
     * Gets the value of the detailedManifests property.
     * 
     */
    public boolean isDetailedManifests() {
        return detailedManifests;
    }

    /**
     * Sets the value of the detailedManifests property.
     * 
     */
    public void setDetailedManifests(boolean value) {
        this.detailedManifests = value;
    }

    /**
     * Gets the value of the methodOfPayment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodOfPayment() {
        return methodOfPayment;
    }

    /**
     * Sets the value of the methodOfPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodOfPayment(String value) {
        this.methodOfPayment = value;
    }

    /**
     * Gets the value of the manifestAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ManifestAddressType }
     *     
     */
    public ManifestAddressType getManifestAddress() {
        return manifestAddress;
    }

    /**
     * Sets the value of the manifestAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManifestAddressType }
     *     
     */
    public void setManifestAddress(ManifestAddressType value) {
        this.manifestAddress = value;
    }

    /**
     * Gets the value of the customerReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerReference() {
        return customerReference;
    }

    /**
     * Sets the value of the customerReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerReference(String value) {
        this.customerReference = value;
    }

    /**
     * Gets the value of the excludedShipments property.
     * 
     * @return
     *     possible object is
     *     {@link ExcludedShipmentsType }
     *     
     */
    public ExcludedShipmentsType getExcludedShipments() {
        return excludedShipments;
    }

    /**
     * Sets the value of the excludedShipments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExcludedShipmentsType }
     *     
     */
    public void setExcludedShipments(ExcludedShipmentsType value) {
        this.excludedShipments = value;
    }

}