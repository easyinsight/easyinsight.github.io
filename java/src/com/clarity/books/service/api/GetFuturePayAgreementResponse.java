
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getFuturePayAgreementResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getFuturePayAgreementResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agreement" type="{http://api.service.books/}futurePayAgreementInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFuturePayAgreementResponse", propOrder = {
    "agreement"
})
public class GetFuturePayAgreementResponse {

    protected FuturePayAgreementInfo agreement;

    /**
     * Gets the value of the agreement property.
     * 
     * @return
     *     possible object is
     *     {@link FuturePayAgreementInfo }
     *     
     */
    public FuturePayAgreementInfo getAgreement() {
        return agreement;
    }

    /**
     * Sets the value of the agreement property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuturePayAgreementInfo }
     *     
     */
    public void setAgreement(FuturePayAgreementInfo value) {
        this.agreement = value;
    }

}
