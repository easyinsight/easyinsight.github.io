
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataSourceConnection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataSourceConnection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourceDataSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sourceDataSourceField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="targetDataSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="targetDataSourceField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataSourceConnection", propOrder = {
    "sourceDataSource",
    "sourceDataSourceField",
    "targetDataSource",
    "targetDataSourceField"
})
public class DataSourceConnection {

    protected String sourceDataSource;
    protected String sourceDataSourceField;
    protected String targetDataSource;
    protected String targetDataSourceField;

    /**
     * Gets the value of the sourceDataSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceDataSource() {
        return sourceDataSource;
    }

    /**
     * Sets the value of the sourceDataSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceDataSource(String value) {
        this.sourceDataSource = value;
    }

    /**
     * Gets the value of the sourceDataSourceField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceDataSourceField() {
        return sourceDataSourceField;
    }

    /**
     * Sets the value of the sourceDataSourceField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceDataSourceField(String value) {
        this.sourceDataSourceField = value;
    }

    /**
     * Gets the value of the targetDataSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetDataSource() {
        return targetDataSource;
    }

    /**
     * Sets the value of the targetDataSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetDataSource(String value) {
        this.targetDataSource = value;
    }

    /**
     * Gets the value of the targetDataSourceField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetDataSourceField() {
        return targetDataSourceField;
    }

    /**
     * Sets the value of the targetDataSourceField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetDataSourceField(String value) {
        this.targetDataSourceField = value;
    }

}
