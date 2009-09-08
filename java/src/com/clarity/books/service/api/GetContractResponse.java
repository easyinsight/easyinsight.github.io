
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getContractResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getContractResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contract" type="{http://api.service.books/}subscriptionInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getContractResponse", propOrder = {
    "contract"
})
public class GetContractResponse {

    protected SubscriptionInfo contract;

    /**
     * Gets the value of the contract property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriptionInfo }
     *     
     */
    public SubscriptionInfo getContract() {
        return contract;
    }

    /**
     * Sets the value of the contract property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscriptionInfo }
     *     
     */
    public void setContract(SubscriptionInfo value) {
        this.contract = value;
    }

}
