//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:02 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.manifest;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManifestPricingInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManifestPricingInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="base-cost" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsZero"/>
 *         &lt;element name="automation-discount" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsNegative"/>
 *         &lt;element name="options-and-surcharges" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsNegative"/>
 *         &lt;element name="gst" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsZero"/>
 *         &lt;element name="pst" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsZero"/>
 *         &lt;element name="hst" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsZero"/>
 *         &lt;element name="total-due-cpc" type="{http://www.canadapost.ca/ws/manifest-v4}CostTypeAllowsZero"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManifestPricingInfoType", propOrder = {

})
public class ManifestPricingInfoType {

    @XmlElement(name = "base-cost", required = true)
    protected BigDecimal baseCost;
    @XmlElement(name = "automation-discount", required = true)
    protected BigDecimal automationDiscount;
    @XmlElement(name = "options-and-surcharges", required = true)
    protected BigDecimal optionsAndSurcharges;
    @XmlElement(required = true)
    protected BigDecimal gst;
    @XmlElement(required = true)
    protected BigDecimal pst;
    @XmlElement(required = true)
    protected BigDecimal hst;
    @XmlElement(name = "total-due-cpc", required = true)
    protected BigDecimal totalDueCpc;

    /**
     * Gets the value of the baseCost property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseCost() {
        return baseCost;
    }

    /**
     * Sets the value of the baseCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseCost(BigDecimal value) {
        this.baseCost = value;
    }

    /**
     * Gets the value of the automationDiscount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAutomationDiscount() {
        return automationDiscount;
    }

    /**
     * Sets the value of the automationDiscount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAutomationDiscount(BigDecimal value) {
        this.automationDiscount = value;
    }

    /**
     * Gets the value of the optionsAndSurcharges property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOptionsAndSurcharges() {
        return optionsAndSurcharges;
    }

    /**
     * Sets the value of the optionsAndSurcharges property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOptionsAndSurcharges(BigDecimal value) {
        this.optionsAndSurcharges = value;
    }

    /**
     * Gets the value of the gst property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGst() {
        return gst;
    }

    /**
     * Sets the value of the gst property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGst(BigDecimal value) {
        this.gst = value;
    }

    /**
     * Gets the value of the pst property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPst() {
        return pst;
    }

    /**
     * Sets the value of the pst property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPst(BigDecimal value) {
        this.pst = value;
    }

    /**
     * Gets the value of the hst property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHst() {
        return hst;
    }

    /**
     * Sets the value of the hst property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHst(BigDecimal value) {
        this.hst = value;
    }

    /**
     * Gets the value of the totalDueCpc property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalDueCpc() {
        return totalDueCpc;
    }

    /**
     * Sets the value of the totalDueCpc property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalDueCpc(BigDecimal value) {
        this.totalDueCpc = value;
    }

}
