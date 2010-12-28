
package com.easyinsight.rowutil.v3web;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for commitResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="commitResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataSourceAPIKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataSourceURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="failedRows" type="{http://v3.api.easyinsight.com/}rowStatus" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="failureMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commitResult", propOrder = {
    "dataSourceAPIKey",
    "dataSourceURL",
    "failedRows",
    "failureMessage",
    "successful"
})
public class CommitResult {

    protected String dataSourceAPIKey;
    protected String dataSourceURL;
    @XmlElement(nillable = true)
    protected List<RowStatus> failedRows;
    protected String failureMessage;
    protected boolean successful;

    /**
     * Gets the value of the dataSourceAPIKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSourceAPIKey() {
        return dataSourceAPIKey;
    }

    /**
     * Sets the value of the dataSourceAPIKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSourceAPIKey(String value) {
        this.dataSourceAPIKey = value;
    }

    /**
     * Gets the value of the dataSourceURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSourceURL() {
        return dataSourceURL;
    }

    /**
     * Sets the value of the dataSourceURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSourceURL(String value) {
        this.dataSourceURL = value;
    }

    /**
     * Gets the value of the failedRows property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the failedRows property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFailedRows().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RowStatus }
     * 
     * 
     */
    public List<RowStatus> getFailedRows() {
        if (failedRows == null) {
            failedRows = new ArrayList<RowStatus>();
        }
        return this.failedRows;
    }

    /**
     * Gets the value of the failureMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Sets the value of the failureMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureMessage(String value) {
        this.failureMessage = value;
    }

    /**
     * Gets the value of the successful property.
     * 
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Sets the value of the successful property.
     * 
     */
    public void setSuccessful(boolean value) {
        this.successful = value;
    }

}
