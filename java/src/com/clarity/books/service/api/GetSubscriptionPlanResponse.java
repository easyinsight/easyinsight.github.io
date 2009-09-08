
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getSubscriptionPlanResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSubscriptionPlanResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="plan" type="{http://api.service.books/}subscriptionPlanInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSubscriptionPlanResponse", propOrder = {
    "plan"
})
public class GetSubscriptionPlanResponse {

    protected SubscriptionPlanInfo plan;

    /**
     * Gets the value of the plan property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriptionPlanInfo }
     *     
     */
    public SubscriptionPlanInfo getPlan() {
        return plan;
    }

    /**
     * Sets the value of the plan property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscriptionPlanInfo }
     *     
     */
    public void setPlan(SubscriptionPlanInfo value) {
        this.plan = value;
    }

}
