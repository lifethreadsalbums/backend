//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.31 at 09:45:21 AM CET 
//


package com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserFormsModel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserFormsModel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserFormItem" type="{http://www.ups.com/XMLSchema/CT/WorldShip/ImpExp/ShipmentImport/v1_0_0}UserFormItemModel" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserFormsModel", propOrder = {
    "userFormItem"
})
public class UserFormsModel {

    @XmlElement(name = "UserFormItem")
    protected List<UserFormItemModel> userFormItem;

    /**
     * Gets the value of the userFormItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userFormItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserFormItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserFormItemModel }
     * 
     * 
     */
    public List<UserFormItemModel> getUserFormItem() {
        if (userFormItem == null) {
            userFormItem = new ArrayList<UserFormItemModel>();
        }
        return this.userFormItem;
    }

}