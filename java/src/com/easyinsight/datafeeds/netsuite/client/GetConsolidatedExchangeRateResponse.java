
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetConsolidatedExchangeRateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetConsolidatedExchangeRateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:core_2014_1.platform.webservices.netsuite.com}getConsolidatedExchangeRateResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetConsolidatedExchangeRateResponse", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "getConsolidatedExchangeRateResult"
})
public class GetConsolidatedExchangeRateResponse {

    @XmlElement(namespace = "urn:core_2014_1.platform.webservices.netsuite.com", required = true)
    protected GetConsolidatedExchangeRateResult getConsolidatedExchangeRateResult;

    /**
     * Gets the value of the getConsolidatedExchangeRateResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetConsolidatedExchangeRateResult }
     *     
     */
    public GetConsolidatedExchangeRateResult getGetConsolidatedExchangeRateResult() {
        return getConsolidatedExchangeRateResult;
    }

    /**
     * Sets the value of the getConsolidatedExchangeRateResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetConsolidatedExchangeRateResult }
     *     
     */
    public void setGetConsolidatedExchangeRateResult(GetConsolidatedExchangeRateResult value) {
        this.getConsolidatedExchangeRateResult = value;
    }

}
