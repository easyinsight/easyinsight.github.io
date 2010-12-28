
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fieldDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fieldDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defaultGroupBy" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fieldType" type="{http://v3.api.easyinsight.com/}fieldType" minOccurs="0"/>
 *         &lt;element name="internalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyGrouping" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="measureAggregationType" type="{http://v3.api.easyinsight.com/}measureAggregationType" minOccurs="0"/>
 *         &lt;element name="measureFormattingType" type="{http://v3.api.easyinsight.com/}measureFormattingType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fieldDefinition", propOrder = {
    "defaultGroupBy",
    "displayName",
    "fieldType",
    "internalName",
    "keyGrouping",
    "measureAggregationType",
    "measureFormattingType"
})
public class FieldDefinition {

    protected boolean defaultGroupBy;
    protected String displayName;
    protected FieldType fieldType;
    protected String internalName;
    protected String keyGrouping;
    protected MeasureAggregationType measureAggregationType;
    protected MeasureFormattingType measureFormattingType;

    /**
     * Gets the value of the defaultGroupBy property.
     * 
     */
    public boolean isDefaultGroupBy() {
        return defaultGroupBy;
    }

    /**
     * Sets the value of the defaultGroupBy property.
     * 
     */
    public void setDefaultGroupBy(boolean value) {
        this.defaultGroupBy = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the fieldType property.
     * 
     * @return
     *     possible object is
     *     {@link FieldType }
     *     
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * Sets the value of the fieldType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldType }
     *     
     */
    public void setFieldType(FieldType value) {
        this.fieldType = value;
    }

    /**
     * Gets the value of the internalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalName() {
        return internalName;
    }

    /**
     * Sets the value of the internalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalName(String value) {
        this.internalName = value;
    }

    /**
     * Gets the value of the keyGrouping property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyGrouping() {
        return keyGrouping;
    }

    /**
     * Sets the value of the keyGrouping property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyGrouping(String value) {
        this.keyGrouping = value;
    }

    /**
     * Gets the value of the measureAggregationType property.
     * 
     * @return
     *     possible object is
     *     {@link MeasureAggregationType }
     *     
     */
    public MeasureAggregationType getMeasureAggregationType() {
        return measureAggregationType;
    }

    /**
     * Sets the value of the measureAggregationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasureAggregationType }
     *     
     */
    public void setMeasureAggregationType(MeasureAggregationType value) {
        this.measureAggregationType = value;
    }

    /**
     * Gets the value of the measureFormattingType property.
     * 
     * @return
     *     possible object is
     *     {@link MeasureFormattingType }
     *     
     */
    public MeasureFormattingType getMeasureFormattingType() {
        return measureFormattingType;
    }

    /**
     * Sets the value of the measureFormattingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasureFormattingType }
     *     
     */
    public void setMeasureFormattingType(MeasureFormattingType value) {
        this.measureFormattingType = value;
    }

}
