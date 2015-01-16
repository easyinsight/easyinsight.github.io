
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetPostingTransactionSummaryRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetPostingTransactionSummaryRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fields" type="{urn:core_2014_1.platform.webservices.netsuite.com}PostingTransactionSummaryField"/>
 *         &lt;element name="filters" type="{urn:core_2014_1.platform.webservices.netsuite.com}PostingTransactionSummaryFilter" minOccurs="0"/>
 *         &lt;element name="pageIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPostingTransactionSummaryRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "fields",
    "filters",
    "pageIndex"
})
public class GetPostingTransactionSummaryRequest {

    @XmlElement(required = true)
    protected PostingTransactionSummaryField fields;
    protected PostingTransactionSummaryFilter filters;
    protected int pageIndex;

    /**
     * Gets the value of the fields property.
     * 
     * @return
     *     possible object is
     *     {@link PostingTransactionSummaryField }
     *     
     */
    public PostingTransactionSummaryField getFields() {
        return fields;
    }

    /**
     * Sets the value of the fields property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostingTransactionSummaryField }
     *     
     */
    public void setFields(PostingTransactionSummaryField value) {
        this.fields = value;
    }

    /**
     * Gets the value of the filters property.
     * 
     * @return
     *     possible object is
     *     {@link PostingTransactionSummaryFilter }
     *     
     */
    public PostingTransactionSummaryFilter getFilters() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostingTransactionSummaryFilter }
     *     
     */
    public void setFilters(PostingTransactionSummaryFilter value) {
        this.filters = value;
    }

    /**
     * Gets the value of the pageIndex property.
     * 
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * Sets the value of the pageIndex property.
     * 
     */
    public void setPageIndex(int value) {
        this.pageIndex = value;
    }

}
