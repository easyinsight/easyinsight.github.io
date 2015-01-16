
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomRecordTypeParents complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomRecordTypeParents">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="childDescr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomRecordTypeParents", namespace = "urn:customization_2014_1.setup.webservices.netsuite.com", propOrder = {
    "childDescr"
})
public class CustomRecordTypeParents {

    protected String childDescr;

    /**
     * Gets the value of the childDescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildDescr() {
        return childDescr;
    }

    /**
     * Sets the value of the childDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildDescr(String value) {
        this.childDescr = value;
    }

}
