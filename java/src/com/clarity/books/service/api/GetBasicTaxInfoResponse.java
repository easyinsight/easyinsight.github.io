
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getBasicTaxInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getBasicTaxInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="basicTaxInfo" type="{http://api.service.books/}basicTaxInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBasicTaxInfoResponse", propOrder = {
    "basicTaxInfo"
})
public class GetBasicTaxInfoResponse {

    protected BasicTaxInfo basicTaxInfo;

    /**
     * Gets the value of the basicTaxInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BasicTaxInfo }
     *     
     */
    public BasicTaxInfo getBasicTaxInfo() {
        return basicTaxInfo;
    }

    /**
     * Sets the value of the basicTaxInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicTaxInfo }
     *     
     */
    public void setBasicTaxInfo(BasicTaxInfo value) {
        this.basicTaxInfo = value;
    }

}
