
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RevRecSchedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RevRecSchedule">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}Record">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amortizationType" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}RevRecScheduleAmortizationType" minOccurs="0"/>
 *         &lt;element name="recurrenceType" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}RevRecScheduleRecurrenceType" minOccurs="0"/>
 *         &lt;element name="recogIntervalSrc" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}RevRecScheduleRecogIntervalSrc" minOccurs="0"/>
 *         &lt;element name="amortizationPeriod" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="periodOffset" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="revRecOffset" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="initialAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="isInactive" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="recurrenceList" type="{urn:accounting_2014_1.lists.webservices.netsuite.com}RevRecScheduleRecurrenceList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="internalId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="externalId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RevRecSchedule", propOrder = {
    "name",
    "amortizationType",
    "recurrenceType",
    "recogIntervalSrc",
    "amortizationPeriod",
    "periodOffset",
    "revRecOffset",
    "initialAmount",
    "isInactive",
    "recurrenceList"
})
public class RevRecSchedule
    extends Record
{

    protected String name;
    protected RevRecScheduleAmortizationType amortizationType;
    protected RevRecScheduleRecurrenceType recurrenceType;
    protected RevRecScheduleRecogIntervalSrc recogIntervalSrc;
    protected Long amortizationPeriod;
    protected Long periodOffset;
    protected Long revRecOffset;
    protected Double initialAmount;
    protected Boolean isInactive;
    protected RevRecScheduleRecurrenceList recurrenceList;
    @XmlAttribute
    protected String internalId;
    @XmlAttribute
    protected String externalId;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the amortizationType property.
     * 
     * @return
     *     possible object is
     *     {@link RevRecScheduleAmortizationType }
     *     
     */
    public RevRecScheduleAmortizationType getAmortizationType() {
        return amortizationType;
    }

    /**
     * Sets the value of the amortizationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevRecScheduleAmortizationType }
     *     
     */
    public void setAmortizationType(RevRecScheduleAmortizationType value) {
        this.amortizationType = value;
    }

    /**
     * Gets the value of the recurrenceType property.
     * 
     * @return
     *     possible object is
     *     {@link RevRecScheduleRecurrenceType }
     *     
     */
    public RevRecScheduleRecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    /**
     * Sets the value of the recurrenceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevRecScheduleRecurrenceType }
     *     
     */
    public void setRecurrenceType(RevRecScheduleRecurrenceType value) {
        this.recurrenceType = value;
    }

    /**
     * Gets the value of the recogIntervalSrc property.
     * 
     * @return
     *     possible object is
     *     {@link RevRecScheduleRecogIntervalSrc }
     *     
     */
    public RevRecScheduleRecogIntervalSrc getRecogIntervalSrc() {
        return recogIntervalSrc;
    }

    /**
     * Sets the value of the recogIntervalSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevRecScheduleRecogIntervalSrc }
     *     
     */
    public void setRecogIntervalSrc(RevRecScheduleRecogIntervalSrc value) {
        this.recogIntervalSrc = value;
    }

    /**
     * Gets the value of the amortizationPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAmortizationPeriod() {
        return amortizationPeriod;
    }

    /**
     * Sets the value of the amortizationPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAmortizationPeriod(Long value) {
        this.amortizationPeriod = value;
    }

    /**
     * Gets the value of the periodOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPeriodOffset() {
        return periodOffset;
    }

    /**
     * Sets the value of the periodOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPeriodOffset(Long value) {
        this.periodOffset = value;
    }

    /**
     * Gets the value of the revRecOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRevRecOffset() {
        return revRecOffset;
    }

    /**
     * Sets the value of the revRecOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRevRecOffset(Long value) {
        this.revRecOffset = value;
    }

    /**
     * Gets the value of the initialAmount property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getInitialAmount() {
        return initialAmount;
    }

    /**
     * Sets the value of the initialAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setInitialAmount(Double value) {
        this.initialAmount = value;
    }

    /**
     * Gets the value of the isInactive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsInactive() {
        return isInactive;
    }

    /**
     * Sets the value of the isInactive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsInactive(Boolean value) {
        this.isInactive = value;
    }

    /**
     * Gets the value of the recurrenceList property.
     * 
     * @return
     *     possible object is
     *     {@link RevRecScheduleRecurrenceList }
     *     
     */
    public RevRecScheduleRecurrenceList getRecurrenceList() {
        return recurrenceList;
    }

    /**
     * Sets the value of the recurrenceList property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevRecScheduleRecurrenceList }
     *     
     */
    public void setRecurrenceList(RevRecScheduleRecurrenceList value) {
        this.recurrenceList = value;
    }

    /**
     * Gets the value of the internalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalId() {
        return internalId;
    }

    /**
     * Sets the value of the internalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalId(String value) {
        this.internalId = value;
    }

    /**
     * Gets the value of the externalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalId(String value) {
        this.externalId = value;
    }

}
