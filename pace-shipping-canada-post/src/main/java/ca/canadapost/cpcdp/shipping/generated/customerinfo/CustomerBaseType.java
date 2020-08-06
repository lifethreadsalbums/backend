//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 02:42:02 PM EDT 
//


package ca.canadapost.cpcdp.shipping.generated.customerinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerBaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customer-number" type="{http://www.canadapost.ca/ws/customer}CustomerIDType"/>
 *         &lt;element name="contracts" type="{http://www.canadapost.ca/ws/customer}ContractsType" minOccurs="0"/>
 *         &lt;element name="authorized-payers" type="{http://www.canadapost.ca/ws/customer}AuthorizedPayersType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerBaseType", propOrder = {
    "customerNumber",
    "contracts",
    "authorizedPayers"
})
@XmlSeeAlso({
    Customer.class,
    BehalfOfCustomer.class
})
public class CustomerBaseType {

    @XmlElement(name = "customer-number", required = true)
    protected String customerNumber;
    protected ContractsType contracts;
    @XmlElement(name = "authorized-payers")
    protected AuthorizedPayersType authorizedPayers;

    /**
     * Gets the value of the customerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Gets the value of the contracts property.
     * 
     * @return
     *     possible object is
     *     {@link ContractsType }
     *     
     */
    public ContractsType getContracts() {
        return contracts;
    }

    /**
     * Sets the value of the contracts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContractsType }
     *     
     */
    public void setContracts(ContractsType value) {
        this.contracts = value;
    }

    /**
     * Gets the value of the authorizedPayers property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorizedPayersType }
     *     
     */
    public AuthorizedPayersType getAuthorizedPayers() {
        return authorizedPayers;
    }

    /**
     * Sets the value of the authorizedPayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorizedPayersType }
     *     
     */
    public void setAuthorizedPayers(AuthorizedPayersType value) {
        this.authorizedPayers = value;
    }

}
