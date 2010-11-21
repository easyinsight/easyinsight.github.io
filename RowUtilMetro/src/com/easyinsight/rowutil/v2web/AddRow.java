
package com.easyinsight.rowutil.v2web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addRow complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataSourceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="row" type="{http://v2.api.easyinsight.com/}row" minOccurs="0"/>
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
@XmlType(name = "addRow", propOrder = {
    "dataSourceName",
    "row",
    "changeDataSourceToMatch"
})
public class AddRow {

    protected String dataSourceName;
    protected Row row;
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
     * Gets the value of the row property.
     * 
     * @return
     *     possible object is
     *     {@link Row }
     *     
     */
    public Row getRow() {
        return row;
    }

    /**
     * Sets the value of the row property.
     * 
     * @param value
     *     allowed object is
     *     {@link Row }
     *     
     */
    public void setRow(Row value) {
        this.row = value;
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
