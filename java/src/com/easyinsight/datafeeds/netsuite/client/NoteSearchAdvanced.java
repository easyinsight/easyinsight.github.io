
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NoteSearchAdvanced complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NoteSearchAdvanced">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}SearchRecord">
 *       &lt;sequence>
 *         &lt;element name="criteria" type="{urn:communication_2014_1.general.webservices.netsuite.com}NoteSearch" minOccurs="0"/>
 *         &lt;element name="columns" type="{urn:communication_2014_1.general.webservices.netsuite.com}NoteSearchRow" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="savedSearchId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="savedSearchScriptId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoteSearchAdvanced", namespace = "urn:communication_2014_1.general.webservices.netsuite.com", propOrder = {
    "criteria",
    "columns"
})
public class NoteSearchAdvanced
    extends SearchRecord
{

    protected NoteSearch criteria;
    protected NoteSearchRow columns;
    @XmlAttribute
    protected String savedSearchId;
    @XmlAttribute
    protected String savedSearchScriptId;

    /**
     * Gets the value of the criteria property.
     * 
     * @return
     *     possible object is
     *     {@link NoteSearch }
     *     
     */
    public NoteSearch getCriteria() {
        return criteria;
    }

    /**
     * Sets the value of the criteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoteSearch }
     *     
     */
    public void setCriteria(NoteSearch value) {
        this.criteria = value;
    }

    /**
     * Gets the value of the columns property.
     * 
     * @return
     *     possible object is
     *     {@link NoteSearchRow }
     *     
     */
    public NoteSearchRow getColumns() {
        return columns;
    }

    /**
     * Sets the value of the columns property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoteSearchRow }
     *     
     */
    public void setColumns(NoteSearchRow value) {
        this.columns = value;
    }

    /**
     * Gets the value of the savedSearchId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSavedSearchId() {
        return savedSearchId;
    }

    /**
     * Sets the value of the savedSearchId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSavedSearchId(String value) {
        this.savedSearchId = value;
    }

    /**
     * Gets the value of the savedSearchScriptId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSavedSearchScriptId() {
        return savedSearchScriptId;
    }

    /**
     * Sets the value of the savedSearchScriptId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSavedSearchScriptId(String value) {
        this.savedSearchScriptId = value;
    }

}