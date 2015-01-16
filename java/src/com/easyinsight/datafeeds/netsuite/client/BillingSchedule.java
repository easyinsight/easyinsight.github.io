
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BillingSchedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BillingSchedule">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}Record">
 *       &lt;sequence>
 *         &lt;element name="scheduleType" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleType" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="project" type="{urn:core_2014_1.platform.webservices.netsuite.com}RecordRef" minOccurs="0"/>
 *         &lt;element name="initialAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="initialTerms" type="{urn:core_2014_1.platform.webservices.netsuite.com}RecordRef" minOccurs="0"/>
 *         &lt;element name="frequency" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleFrequency" minOccurs="0"/>
 *         &lt;element name="recurrenceDowMaskList" type="{urn:common_2014_1.platform.webservices.netsuite.com}RecurrenceDowMaskList" minOccurs="0"/>
 *         &lt;element name="yearMode" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleRecurrenceMode" minOccurs="0"/>
 *         &lt;element name="yearDowim" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleYearDowim" minOccurs="0"/>
 *         &lt;element name="yearDow" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleYearDow" minOccurs="0"/>
 *         &lt;element name="yearDowimMonth" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleYearDowimMonth" minOccurs="0"/>
 *         &lt;element name="yearMonth" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleYearMonth" minOccurs="0"/>
 *         &lt;element name="yearDom" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="monthMode" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleRecurrenceMode" minOccurs="0"/>
 *         &lt;element name="monthDowim" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleMonthDowim" minOccurs="0"/>
 *         &lt;element name="monthDow" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleMonthDow" minOccurs="0"/>
 *         &lt;element name="monthDom" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="dayPeriod" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="repeatEvery" type="{urn:types.accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleRepeatEvery" minOccurs="0"/>
 *         &lt;element name="billForActuals" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="numberRemaining" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="inArrears" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="recurrenceTerms" type="{urn:core_2014_1.platform.webservices.netsuite.com}RecordRef" minOccurs="0"/>
 *         &lt;element name="isPublic" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="transaction" type="{urn:core_2014_1.platform.webservices.netsuite.com}RecordRef" minOccurs="0"/>
 *         &lt;element name="isInactive" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="seriesStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="recurrenceList" type="{urn:accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleRecurrenceList" minOccurs="0"/>
 *         &lt;element name="milestoneList" type="{urn:accounting_2014_1.lists.webservices.netsuite.com}BillingScheduleMilestoneList" minOccurs="0"/>
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
@XmlType(name = "BillingSchedule", propOrder = {
    "scheduleType",
    "name",
    "project",
    "initialAmount",
    "initialTerms",
    "frequency",
    "recurrenceDowMaskList",
    "yearMode",
    "yearDowim",
    "yearDow",
    "yearDowimMonth",
    "yearMonth",
    "yearDom",
    "monthMode",
    "monthDowim",
    "monthDow",
    "monthDom",
    "dayPeriod",
    "repeatEvery",
    "billForActuals",
    "numberRemaining",
    "inArrears",
    "recurrenceTerms",
    "isPublic",
    "transaction",
    "isInactive",
    "seriesStartDate",
    "recurrenceList",
    "milestoneList"
})
public class BillingSchedule
    extends Record
{

    protected BillingScheduleType scheduleType;
    protected String name;
    protected RecordRef project;
    protected String initialAmount;
    protected RecordRef initialTerms;
    protected BillingScheduleFrequency frequency;
    protected RecurrenceDowMaskList recurrenceDowMaskList;
    protected BillingScheduleRecurrenceMode yearMode;
    protected BillingScheduleYearDowim yearDowim;
    protected BillingScheduleYearDow yearDow;
    protected BillingScheduleYearDowimMonth yearDowimMonth;
    protected BillingScheduleYearMonth yearMonth;
    protected Long yearDom;
    protected BillingScheduleRecurrenceMode monthMode;
    protected BillingScheduleMonthDowim monthDowim;
    protected BillingScheduleMonthDow monthDow;
    protected Long monthDom;
    protected Long dayPeriod;
    protected String repeatEvery;
    protected Boolean billForActuals;
    protected Long numberRemaining;
    protected Boolean inArrears;
    protected RecordRef recurrenceTerms;
    protected Boolean isPublic;
    protected RecordRef transaction;
    protected Boolean isInactive;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar seriesStartDate;
    protected BillingScheduleRecurrenceList recurrenceList;
    protected BillingScheduleMilestoneList milestoneList;
    @XmlAttribute
    protected String internalId;
    @XmlAttribute
    protected String externalId;

    /**
     * Gets the value of the scheduleType property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleType }
     *     
     */
    public BillingScheduleType getScheduleType() {
        return scheduleType;
    }

    /**
     * Sets the value of the scheduleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleType }
     *     
     */
    public void setScheduleType(BillingScheduleType value) {
        this.scheduleType = value;
    }

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
     * Gets the value of the project property.
     * 
     * @return
     *     possible object is
     *     {@link RecordRef }
     *     
     */
    public RecordRef getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordRef }
     *     
     */
    public void setProject(RecordRef value) {
        this.project = value;
    }

    /**
     * Gets the value of the initialAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialAmount() {
        return initialAmount;
    }

    /**
     * Sets the value of the initialAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialAmount(String value) {
        this.initialAmount = value;
    }

    /**
     * Gets the value of the initialTerms property.
     * 
     * @return
     *     possible object is
     *     {@link RecordRef }
     *     
     */
    public RecordRef getInitialTerms() {
        return initialTerms;
    }

    /**
     * Sets the value of the initialTerms property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordRef }
     *     
     */
    public void setInitialTerms(RecordRef value) {
        this.initialTerms = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleFrequency }
     *     
     */
    public BillingScheduleFrequency getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleFrequency }
     *     
     */
    public void setFrequency(BillingScheduleFrequency value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the recurrenceDowMaskList property.
     * 
     * @return
     *     possible object is
     *     {@link RecurrenceDowMaskList }
     *     
     */
    public RecurrenceDowMaskList getRecurrenceDowMaskList() {
        return recurrenceDowMaskList;
    }

    /**
     * Sets the value of the recurrenceDowMaskList property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecurrenceDowMaskList }
     *     
     */
    public void setRecurrenceDowMaskList(RecurrenceDowMaskList value) {
        this.recurrenceDowMaskList = value;
    }

    /**
     * Gets the value of the yearMode property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleRecurrenceMode }
     *     
     */
    public BillingScheduleRecurrenceMode getYearMode() {
        return yearMode;
    }

    /**
     * Sets the value of the yearMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleRecurrenceMode }
     *     
     */
    public void setYearMode(BillingScheduleRecurrenceMode value) {
        this.yearMode = value;
    }

    /**
     * Gets the value of the yearDowim property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleYearDowim }
     *     
     */
    public BillingScheduleYearDowim getYearDowim() {
        return yearDowim;
    }

    /**
     * Sets the value of the yearDowim property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleYearDowim }
     *     
     */
    public void setYearDowim(BillingScheduleYearDowim value) {
        this.yearDowim = value;
    }

    /**
     * Gets the value of the yearDow property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleYearDow }
     *     
     */
    public BillingScheduleYearDow getYearDow() {
        return yearDow;
    }

    /**
     * Sets the value of the yearDow property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleYearDow }
     *     
     */
    public void setYearDow(BillingScheduleYearDow value) {
        this.yearDow = value;
    }

    /**
     * Gets the value of the yearDowimMonth property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleYearDowimMonth }
     *     
     */
    public BillingScheduleYearDowimMonth getYearDowimMonth() {
        return yearDowimMonth;
    }

    /**
     * Sets the value of the yearDowimMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleYearDowimMonth }
     *     
     */
    public void setYearDowimMonth(BillingScheduleYearDowimMonth value) {
        this.yearDowimMonth = value;
    }

    /**
     * Gets the value of the yearMonth property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleYearMonth }
     *     
     */
    public BillingScheduleYearMonth getYearMonth() {
        return yearMonth;
    }

    /**
     * Sets the value of the yearMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleYearMonth }
     *     
     */
    public void setYearMonth(BillingScheduleYearMonth value) {
        this.yearMonth = value;
    }

    /**
     * Gets the value of the yearDom property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getYearDom() {
        return yearDom;
    }

    /**
     * Sets the value of the yearDom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setYearDom(Long value) {
        this.yearDom = value;
    }

    /**
     * Gets the value of the monthMode property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleRecurrenceMode }
     *     
     */
    public BillingScheduleRecurrenceMode getMonthMode() {
        return monthMode;
    }

    /**
     * Sets the value of the monthMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleRecurrenceMode }
     *     
     */
    public void setMonthMode(BillingScheduleRecurrenceMode value) {
        this.monthMode = value;
    }

    /**
     * Gets the value of the monthDowim property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleMonthDowim }
     *     
     */
    public BillingScheduleMonthDowim getMonthDowim() {
        return monthDowim;
    }

    /**
     * Sets the value of the monthDowim property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleMonthDowim }
     *     
     */
    public void setMonthDowim(BillingScheduleMonthDowim value) {
        this.monthDowim = value;
    }

    /**
     * Gets the value of the monthDow property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleMonthDow }
     *     
     */
    public BillingScheduleMonthDow getMonthDow() {
        return monthDow;
    }

    /**
     * Sets the value of the monthDow property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleMonthDow }
     *     
     */
    public void setMonthDow(BillingScheduleMonthDow value) {
        this.monthDow = value;
    }

    /**
     * Gets the value of the monthDom property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMonthDom() {
        return monthDom;
    }

    /**
     * Sets the value of the monthDom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMonthDom(Long value) {
        this.monthDom = value;
    }

    /**
     * Gets the value of the dayPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDayPeriod() {
        return dayPeriod;
    }

    /**
     * Sets the value of the dayPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDayPeriod(Long value) {
        this.dayPeriod = value;
    }

    /**
     * Gets the value of the repeatEvery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepeatEvery() {
        return repeatEvery;
    }

    /**
     * Sets the value of the repeatEvery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepeatEvery(String value) {
        this.repeatEvery = value;
    }

    /**
     * Gets the value of the billForActuals property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBillForActuals() {
        return billForActuals;
    }

    /**
     * Sets the value of the billForActuals property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBillForActuals(Boolean value) {
        this.billForActuals = value;
    }

    /**
     * Gets the value of the numberRemaining property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumberRemaining() {
        return numberRemaining;
    }

    /**
     * Sets the value of the numberRemaining property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumberRemaining(Long value) {
        this.numberRemaining = value;
    }

    /**
     * Gets the value of the inArrears property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInArrears() {
        return inArrears;
    }

    /**
     * Sets the value of the inArrears property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInArrears(Boolean value) {
        this.inArrears = value;
    }

    /**
     * Gets the value of the recurrenceTerms property.
     * 
     * @return
     *     possible object is
     *     {@link RecordRef }
     *     
     */
    public RecordRef getRecurrenceTerms() {
        return recurrenceTerms;
    }

    /**
     * Sets the value of the recurrenceTerms property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordRef }
     *     
     */
    public void setRecurrenceTerms(RecordRef value) {
        this.recurrenceTerms = value;
    }

    /**
     * Gets the value of the isPublic property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPublic() {
        return isPublic;
    }

    /**
     * Sets the value of the isPublic property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPublic(Boolean value) {
        this.isPublic = value;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * @return
     *     possible object is
     *     {@link RecordRef }
     *     
     */
    public RecordRef getTransaction() {
        return transaction;
    }

    /**
     * Sets the value of the transaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordRef }
     *     
     */
    public void setTransaction(RecordRef value) {
        this.transaction = value;
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
     * Gets the value of the seriesStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSeriesStartDate() {
        return seriesStartDate;
    }

    /**
     * Sets the value of the seriesStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSeriesStartDate(XMLGregorianCalendar value) {
        this.seriesStartDate = value;
    }

    /**
     * Gets the value of the recurrenceList property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleRecurrenceList }
     *     
     */
    public BillingScheduleRecurrenceList getRecurrenceList() {
        return recurrenceList;
    }

    /**
     * Sets the value of the recurrenceList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleRecurrenceList }
     *     
     */
    public void setRecurrenceList(BillingScheduleRecurrenceList value) {
        this.recurrenceList = value;
    }

    /**
     * Gets the value of the milestoneList property.
     * 
     * @return
     *     possible object is
     *     {@link BillingScheduleMilestoneList }
     *     
     */
    public BillingScheduleMilestoneList getMilestoneList() {
        return milestoneList;
    }

    /**
     * Sets the value of the milestoneList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingScheduleMilestoneList }
     *     
     */
    public void setMilestoneList(BillingScheduleMilestoneList value) {
        this.milestoneList = value;
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
