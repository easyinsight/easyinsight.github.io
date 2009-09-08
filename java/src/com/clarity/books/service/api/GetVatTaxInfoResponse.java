
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getVatTaxInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getVatTaxInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vatTaxInfo" type="{http://api.service.books/}vatTaxInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getVatTaxInfoResponse", propOrder = {
    "vatTaxInfo"
})
public class GetVatTaxInfoResponse {

    protected VatTaxInfo vatTaxInfo;

    /**
     * Gets the value of the vatTaxInfo property.
     * 
     * @return
     *     possible object is
     *     {@link VatTaxInfo }
     *     
     */
    public VatTaxInfo getVatTaxInfo() {
        return vatTaxInfo;
    }

    /**
     * Sets the value of the vatTaxInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link VatTaxInfo }
     *     
     */
    public void setVatTaxInfo(VatTaxInfo value) {
        this.vatTaxInfo = value;
    }

}
