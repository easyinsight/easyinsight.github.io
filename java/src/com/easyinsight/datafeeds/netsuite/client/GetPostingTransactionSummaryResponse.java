
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetPostingTransactionSummaryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetPostingTransactionSummaryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:core_2014_1.platform.webservices.netsuite.com}getPostingTransactionSummaryResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPostingTransactionSummaryResponse", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "getPostingTransactionSummaryResult"
})
public class GetPostingTransactionSummaryResponse {

    @XmlElement(namespace = "urn:core_2014_1.platform.webservices.netsuite.com", required = true)
    protected GetPostingTransactionSummaryResult getPostingTransactionSummaryResult;

    /**
     * Gets the value of the getPostingTransactionSummaryResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetPostingTransactionSummaryResult }
     *     
     */
    public GetPostingTransactionSummaryResult getGetPostingTransactionSummaryResult() {
        return getPostingTransactionSummaryResult;
    }

    /**
     * Sets the value of the getPostingTransactionSummaryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPostingTransactionSummaryResult }
     *     
     */
    public void setGetPostingTransactionSummaryResult(GetPostingTransactionSummaryResult value) {
        this.getPostingTransactionSummaryResult = value;
    }

}
