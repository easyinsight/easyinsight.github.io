
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCanadianTaxInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCanadianTaxInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="canadianTaxInfo" type="{http://api.service.books/}canadianTaxInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCanadianTaxInfoResponse", propOrder = {
    "canadianTaxInfo"
})
public class GetCanadianTaxInfoResponse {

    protected CanadianTaxInfo canadianTaxInfo;

    /**
     * Gets the value of the canadianTaxInfo property.
     * 
     * @return
     *     possible object is
     *     {@link CanadianTaxInfo }
     *     
     */
    public CanadianTaxInfo getCanadianTaxInfo() {
        return canadianTaxInfo;
    }

    /**
     * Sets the value of the canadianTaxInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CanadianTaxInfo }
     *     
     */
    public void setCanadianTaxInfo(CanadianTaxInfo value) {
        this.canadianTaxInfo = value;
    }

}
