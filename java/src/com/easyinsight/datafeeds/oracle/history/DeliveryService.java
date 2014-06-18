
package com.easyinsight.datafeeds.oracle.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deliveryRequest" type="{http://xmlns.oracle.com/oxp/service/PublicReportService}DeliveryRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "deliveryRequest"
})
@XmlRootElement(name = "deliveryService")
public class DeliveryService {

    @XmlElement(required = true)
    protected DeliveryRequest deliveryRequest;

    /**
     * Gets the value of the deliveryRequest property.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryRequest }
     *     
     */
    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    /**
     * Sets the value of the deliveryRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryRequest }
     *     
     */
    public void setDeliveryRequest(DeliveryRequest value) {
        this.deliveryRequest = value;
    }

}
