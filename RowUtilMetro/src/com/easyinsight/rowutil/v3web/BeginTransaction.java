
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for beginTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="beginTransaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataSourceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionOperation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="changeDataSourceToMatch" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "beginTransaction", propOrder = {
    "dataSourceName",
    "transactionOperation",
    "changeDataSourceToMatch"
})
public class BeginTransaction {

    protected String dataSourceName;
    protected boolean transactionOperation;
    protected boolean changeDataSourceToMatch;

    /**
     * Gets the value of the dataSourceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * Sets the value of the dataSourceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSourceName(String value) {
        this.dataSourceName = value;
    }

    /**
     * Gets the value of the transactionOperation property.
     * 
     */
    public boolean isTransactionOperation() {
        return transactionOperation;
    }

    /**
     * Sets the value of the transactionOperation property.
     * 
     */
    public void setTransactionOperation(boolean value) {
        this.transactionOperation = value;
    }

    /**
     * Gets the value of the changeDataSourceToMatch property.
     * 
     */
    public boolean isChangeDataSourceToMatch() {
        return changeDataSourceToMatch;
    }

    /**
     * Sets the value of the changeDataSourceToMatch property.
     * 
     */
    public void setChangeDataSourceToMatch(boolean value) {
        this.changeDataSourceToMatch = value;
    }

}
