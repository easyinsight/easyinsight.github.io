
package com.clarity.books.service.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for businessInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="businessInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="beanStreamEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="bookingCalendarEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="contactInformation" type="{http://api.service.books/}contactInformationInfo" minOccurs="0"/>
 *         &lt;element name="creationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencies" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateFormatting" type="{http://api.service.books/}dateFormatting" minOccurs="0"/>
 *         &lt;element name="defaultAccountsPayable" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="defaultAccountsReceivable" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="defaultBankAccount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="defaultCashAccount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="defaultEquityAccount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="emailEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="fiscalStartDay" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fiscalStartMonth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fiscalStartYear" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="freshbooksEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="futurePayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="gainOrLossOnExchange" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="privileges" type="{http://api.service.books/}privilege" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="removed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="siteOwner" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="standardTerms" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subscriptionsEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="timeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "businessInfo", propOrder = {
    "beanStreamEnabled",
    "bookingCalendarEnabled",
    "contactInformation",
    "creationDate",
    "currencies",
    "currency",
    "dateFormatting",
    "defaultAccountsPayable",
    "defaultAccountsReceivable",
    "defaultBankAccount",
    "defaultCashAccount",
    "defaultEquityAccount",
    "emailEnabled",
    "fiscalStartDay",
    "fiscalStartMonth",
    "fiscalStartYear",
    "freshbooksEnabled",
    "futurePayEnabled",
    "gainOrLossOnExchange",
    "id",
    "privileges",
    "removed",
    "siteOwner",
    "standardTerms",
    "startDate",
    "subscriptionsEnabled",
    "timeZone"
})
public class BusinessInfo {

    protected boolean beanStreamEnabled;
    protected boolean bookingCalendarEnabled;
    protected ContactInformationInfo contactInformation;
    protected String creationDate;
    protected String currencies;
    protected String currency;
    protected DateFormatting dateFormatting;
    protected Long defaultAccountsPayable;
    protected Long defaultAccountsReceivable;
    protected Long defaultBankAccount;
    protected Long defaultCashAccount;
    protected Long defaultEquityAccount;
    protected boolean emailEnabled;
    protected int fiscalStartDay;
    protected int fiscalStartMonth;
    protected int fiscalStartYear;
    protected boolean freshbooksEnabled;
    protected boolean futurePayEnabled;
    protected Long gainOrLossOnExchange;
    protected Long id;
    @XmlElement(nillable = true)
    protected List<Privilege> privileges;
    protected boolean removed;
    protected boolean siteOwner;
    protected String standardTerms;
    protected String startDate;
    protected boolean subscriptionsEnabled;
    protected String timeZone;

    /**
     * Gets the value of the beanStreamEnabled property.
     * 
     */
    public boolean isBeanStreamEnabled() {
        return beanStreamEnabled;
    }

    /**
     * Sets the value of the beanStreamEnabled property.
     * 
     */
    public void setBeanStreamEnabled(boolean value) {
        this.beanStreamEnabled = value;
    }

    /**
     * Gets the value of the bookingCalendarEnabled property.
     * 
     */
    public boolean isBookingCalendarEnabled() {
        return bookingCalendarEnabled;
    }

    /**
     * Sets the value of the bookingCalendarEnabled property.
     * 
     */
    public void setBookingCalendarEnabled(boolean value) {
        this.bookingCalendarEnabled = value;
    }

    /**
     * Gets the value of the contactInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInformationInfo }
     *     
     */
    public ContactInformationInfo getContactInformation() {
        return contactInformation;
    }

    /**
     * Sets the value of the contactInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInformationInfo }
     *     
     */
    public void setContactInformation(ContactInformationInfo value) {
        this.contactInformation = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreationDate(String value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the currencies property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencies() {
        return currencies;
    }

    /**
     * Sets the value of the currencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencies(String value) {
        this.currencies = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the dateFormatting property.
     * 
     * @return
     *     possible object is
     *     {@link DateFormatting }
     *     
     */
    public DateFormatting getDateFormatting() {
        return dateFormatting;
    }

    /**
     * Sets the value of the dateFormatting property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormatting }
     *     
     */
    public void setDateFormatting(DateFormatting value) {
        this.dateFormatting = value;
    }

    /**
     * Gets the value of the defaultAccountsPayable property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDefaultAccountsPayable() {
        return defaultAccountsPayable;
    }

    /**
     * Sets the value of the defaultAccountsPayable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDefaultAccountsPayable(Long value) {
        this.defaultAccountsPayable = value;
    }

    /**
     * Gets the value of the defaultAccountsReceivable property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDefaultAccountsReceivable() {
        return defaultAccountsReceivable;
    }

    /**
     * Sets the value of the defaultAccountsReceivable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDefaultAccountsReceivable(Long value) {
        this.defaultAccountsReceivable = value;
    }

    /**
     * Gets the value of the defaultBankAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDefaultBankAccount() {
        return defaultBankAccount;
    }

    /**
     * Sets the value of the defaultBankAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDefaultBankAccount(Long value) {
        this.defaultBankAccount = value;
    }

    /**
     * Gets the value of the defaultCashAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDefaultCashAccount() {
        return defaultCashAccount;
    }

    /**
     * Sets the value of the defaultCashAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDefaultCashAccount(Long value) {
        this.defaultCashAccount = value;
    }

    /**
     * Gets the value of the defaultEquityAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDefaultEquityAccount() {
        return defaultEquityAccount;
    }

    /**
     * Sets the value of the defaultEquityAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDefaultEquityAccount(Long value) {
        this.defaultEquityAccount = value;
    }

    /**
     * Gets the value of the emailEnabled property.
     * 
     */
    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    /**
     * Sets the value of the emailEnabled property.
     * 
     */
    public void setEmailEnabled(boolean value) {
        this.emailEnabled = value;
    }

    /**
     * Gets the value of the fiscalStartDay property.
     * 
     */
    public int getFiscalStartDay() {
        return fiscalStartDay;
    }

    /**
     * Sets the value of the fiscalStartDay property.
     * 
     */
    public void setFiscalStartDay(int value) {
        this.fiscalStartDay = value;
    }

    /**
     * Gets the value of the fiscalStartMonth property.
     * 
     */
    public int getFiscalStartMonth() {
        return fiscalStartMonth;
    }

    /**
     * Sets the value of the fiscalStartMonth property.
     * 
     */
    public void setFiscalStartMonth(int value) {
        this.fiscalStartMonth = value;
    }

    /**
     * Gets the value of the fiscalStartYear property.
     * 
     */
    public int getFiscalStartYear() {
        return fiscalStartYear;
    }

    /**
     * Sets the value of the fiscalStartYear property.
     * 
     */
    public void setFiscalStartYear(int value) {
        this.fiscalStartYear = value;
    }

    /**
     * Gets the value of the freshbooksEnabled property.
     * 
     */
    public boolean isFreshbooksEnabled() {
        return freshbooksEnabled;
    }

    /**
     * Sets the value of the freshbooksEnabled property.
     * 
     */
    public void setFreshbooksEnabled(boolean value) {
        this.freshbooksEnabled = value;
    }

    /**
     * Gets the value of the futurePayEnabled property.
     * 
     */
    public boolean isFuturePayEnabled() {
        return futurePayEnabled;
    }

    /**
     * Sets the value of the futurePayEnabled property.
     * 
     */
    public void setFuturePayEnabled(boolean value) {
        this.futurePayEnabled = value;
    }

    /**
     * Gets the value of the gainOrLossOnExchange property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGainOrLossOnExchange() {
        return gainOrLossOnExchange;
    }

    /**
     * Sets the value of the gainOrLossOnExchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGainOrLossOnExchange(Long value) {
        this.gainOrLossOnExchange = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the privileges property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the privileges property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrivileges().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Privilege }
     * 
     * 
     */
    public List<Privilege> getPrivileges() {
        if (privileges == null) {
            privileges = new ArrayList<Privilege>();
        }
        return this.privileges;
    }

    /**
     * Gets the value of the removed property.
     * 
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Sets the value of the removed property.
     * 
     */
    public void setRemoved(boolean value) {
        this.removed = value;
    }

    /**
     * Gets the value of the siteOwner property.
     * 
     */
    public boolean isSiteOwner() {
        return siteOwner;
    }

    /**
     * Sets the value of the siteOwner property.
     * 
     */
    public void setSiteOwner(boolean value) {
        this.siteOwner = value;
    }

    /**
     * Gets the value of the standardTerms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardTerms() {
        return standardTerms;
    }

    /**
     * Sets the value of the standardTerms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardTerms(String value) {
        this.standardTerms = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the subscriptionsEnabled property.
     * 
     */
    public boolean isSubscriptionsEnabled() {
        return subscriptionsEnabled;
    }

    /**
     * Sets the value of the subscriptionsEnabled property.
     * 
     */
    public void setSubscriptionsEnabled(boolean value) {
        this.subscriptionsEnabled = value;
    }

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
    }

}
