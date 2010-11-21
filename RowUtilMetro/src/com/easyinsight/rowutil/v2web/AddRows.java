
package com.easyinsight.rowutil.v2web;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addRows complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addRows">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataSourceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rows" type="{http://v2.api.easyinsight.com/}row" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "addRows", propOrder = {
    "dataSourceName",
    "rows",
    "changeDataSourceToMatch"
})
public class AddRows {

    protected String dataSourceName;
    protected List<Row> rows;
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
     * Gets the value of the rows property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rows property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRows().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Row }
     * 
     * 
     */
    public List<Row> getRows() {
        if (rows == null) {
            rows = new ArrayList<Row>();
        }
        return this.rows;
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
