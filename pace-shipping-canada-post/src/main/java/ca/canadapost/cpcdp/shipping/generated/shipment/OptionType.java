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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for OptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="option-code">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="option-amount" type="{http://www.canadapost.ca/ws/shipment-v4}CostTypeNonZero" minOccurs="0"/>
 *         &lt;element name="option-qualifier-1" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="option-qualifier-2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *               &lt;maxLength value="12"/>
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
@XmlType(name = "OptionType", propOrder = {

})
public class OptionType {

    @XmlElement(name = "option-code", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String optionCode;
    @XmlElement(name = "option-amount")
    protected BigDecimal optionAmount;
    @XmlElement(name = "option-qualifier-1")
    protected Boolean optionQualifier1;
    @XmlElement(name = "option-qualifier-2")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String optionQualifier2;

    /**
     * Gets the value of the optionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptionCode() {
        return optionCode;
    }

    /**
     * Sets the value of the optionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptionCode(String value) {
        this.optionCode = value;
    }

    /**
     * Gets the value of the optionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOptionAmount() {
        return optionAmount;
    }

    /**
     * Sets the value of the optionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOptionAmount(BigDecimal value) {
        this.optionAmount = value;
    }

    /**
     * Gets the value of the optionQualifier1 property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOptionQualifier1() {
        return optionQualifier1;
    }

    /**
     * Sets the value of the optionQualifier1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOptionQualifier1(Boolean value) {
        this.optionQualifier1 = value;
    }

    /**
     * Gets the value of the optionQualifier2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptionQualifier2() {
        return optionQualifier2;
    }

    /**
     * Sets the value of the optionQualifier2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptionQualifier2(String value) {
        this.optionQualifier2 = value;
    }

}
