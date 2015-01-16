
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetConsolidatedExchangeRateRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetConsolidatedExchangeRateRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="consolidatedExchangeRateFilter" type="{urn:core_2014_1.platform.webservices.netsuite.com}ConsolidatedExchangeRateFilter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetConsolidatedExchangeRateRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "consolidatedExchangeRateFilter"
})
public class GetConsolidatedExchangeRateRequest {

    @XmlElement(required = true)
    protected ConsolidatedExchangeRateFilter consolidatedExchangeRateFilter;

    /**
     * Gets the value of the consolidatedExchangeRateFilter property.
     * 
     * @return
     *     possible object is
     *     {@link ConsolidatedExchangeRateFilter }
     *     
     */
    public ConsolidatedExchangeRateFilter getConsolidatedExchangeRateFilter() {
        return consolidatedExchangeRateFilter;
    }

    /**
     * Sets the value of the consolidatedExchangeRateFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsolidatedExchangeRateFilter }
     *     
     */
    public void setConsolidatedExchangeRateFilter(ConsolidatedExchangeRateFilter value) {
        this.consolidatedExchangeRateFilter = value;
    }

}
