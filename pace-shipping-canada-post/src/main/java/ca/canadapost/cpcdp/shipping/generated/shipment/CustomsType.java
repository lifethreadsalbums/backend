//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:01 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.shipment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CustomsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="currency">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="conversion-from-cad" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;maxInclusive value="999.999999"/>
 *               &lt;fractionDigits value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="reason-for-export">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="other-reason" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;maxLength value="44"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="additional-customs-info" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;maxLength value="44"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sku-list">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{http://www.canadapost.ca/ws/shipment-v4}SkuType" maxOccurs="500"/>
 *                 &lt;/sequence>
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
@XmlType(name = "CustomsType", propOrder = {

})
public class CustomsType {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String currency;
    @XmlElement(name = "conversion-from-cad")
    protected BigDecimal conversionFromCad;
    @XmlElement(name = "reason-for-export", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String reasonForExport;
    @XmlElement(name = "other-reason")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String otherReason;
    @XmlElement(name = "additional-customs-info")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String additionalCustomsInfo;
    @XmlElement(name = "sku-list", required = true)
    protected CustomsType.SkuList skuList;

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
     * Gets the value of the conversionFromCad property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConversionFromCad() {
        return conversionFromCad;
    }

    /**
     * Sets the value of the conversionFromCad property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConversionFromCad(BigDecimal value) {
        this.conversionFromCad = value;
    }

    /**
     * Gets the value of the reasonForExport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonForExport() {
        return reasonForExport;
    }

    /**
     * Sets the value of the reasonForExport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonForExport(String value) {
        this.reasonForExport = value;
    }

    /**
     * Gets the value of the otherReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherReason() {
        return otherReason;
    }

    /**
     * Sets the value of the otherReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherReason(String value) {
        this.otherReason = value;
    }

    /**
     * Gets the value of the additionalCustomsInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalCustomsInfo() {
        return additionalCustomsInfo;
    }

    /**
     * Sets the value of the additionalCustomsInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalCustomsInfo(String value) {
        this.additionalCustomsInfo = value;
    }

    /**
     * Gets the value of the skuList property.
     * 
     * @return
     *     possible object is
     *     {@link CustomsType.SkuList }
     *     
     */
    public CustomsType.SkuList getSkuList() {
        return skuList;
    }

    /**
     * Sets the value of the skuList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomsType.SkuList }
     *     
     */
    public void setSkuList(CustomsType.SkuList value) {
        this.skuList = value;
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
     *       &lt;sequence>
     *         &lt;element name="item" type="{http://www.canadapost.ca/ws/shipment-v4}SkuType" maxOccurs="500"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "items"
    })
    public static class SkuList {

        @XmlElement(name = "item", required = true)
        protected List<SkuType> items;

        /**
         * Gets the value of the items property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the items property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItems().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SkuType }
         * 
         * 
         */
        public List<SkuType> getItems() {
            if (items == null) {
                items = new ArrayList<SkuType>();
            }
            return this.items;
        }

    }

}
