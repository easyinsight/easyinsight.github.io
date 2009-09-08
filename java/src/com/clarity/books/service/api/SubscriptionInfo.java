
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for subscriptionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subscriptionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="billingContact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="business" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="expiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="failedPaymentTransactionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="features" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="invoice" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="lastAutomaticMessageSent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastPayment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastPaymentAmount" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="lastPaymentAttempt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastPaymentCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="linkedContact" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="linkedExpenseAccount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="linkedPaymentAccount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextAnnualInvoiceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextMonthlyInvoiceDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextPayment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextPaymentAmount" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="nextPaymentAttempt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="nextPaymentCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentFailed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="paymentInformationAvailable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="pendingPlan" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="plan" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="planChangeDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="planName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processor" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="removed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trialEnd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subscriptionInfo", propOrder = {
    "billingContact",
    "billingEmail",
    "billingName",
    "billingPhone",
    "business",
    "contact",
    "expired",
    "expiryDate",
    "failedPaymentTransactionId",
    "features",
    "id",
    "invoice",
    "lastAutomaticMessageSent",
    "lastPayment",
    "lastPaymentAmount",
    "lastPaymentAttempt",
    "lastPaymentCurrency",
    "linkedContact",
    "linkedExpenseAccount",
    "linkedPaymentAccount",
    "message",
    "nextAnnualInvoiceDate",
    "nextMonthlyInvoiceDate",
    "nextPayment",
    "nextPaymentAmount",
    "nextPaymentAttempt",
    "nextPaymentCurrency",
    "paymentFailed",
    "paymentInformationAvailable",
    "pendingPlan",
    "plan",
    "planChangeDate",
    "planName",
    "processor",
    "removed",
    "startDate",
    "trialEnd",
    "version"
})
public class SubscriptionInfo {

    protected String billingContact;
    protected String billingEmail;
    protected String billingName;
    protected String billingPhone;
    protected Long business;
    protected Long contact;
    protected boolean expired;
    protected String expiryDate;
    protected Long failedPaymentTransactionId;
    protected String features;
    protected Long id;
    protected Long invoice;
    protected String lastAutomaticMessageSent;
    protected String lastPayment;
    protected long lastPaymentAmount;
    protected XMLGregorianCalendar lastPaymentAttempt;
    protected String lastPaymentCurrency;
    protected Long linkedContact;
    protected Long linkedExpenseAccount;
    protected Long linkedPaymentAccount;
    protected String message;
    protected String nextAnnualInvoiceDate;
    protected String nextMonthlyInvoiceDate;
    protected String nextPayment;
    protected long nextPaymentAmount;
    protected XMLGregorianCalendar nextPaymentAttempt;
    protected String nextPaymentCurrency;
    protected boolean paymentFailed;
    protected boolean paymentInformationAvailable;
    protected Long pendingPlan;
    protected Long plan;
    protected String planChangeDate;
    protected String planName;
    protected Long processor;
    protected boolean removed;
    protected String startDate;
    protected String trialEnd;
    protected int version;

    /**
     * Gets the value of the billingContact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingContact() {
        return billingContact;
    }

    /**
     * Sets the value of the billingContact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingContact(String value) {
        this.billingContact = value;
    }

    /**
     * Gets the value of the billingEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingEmail() {
        return billingEmail;
    }

    /**
     * Sets the value of the billingEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingEmail(String value) {
        this.billingEmail = value;
    }

    /**
     * Gets the value of the billingName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingName() {
        return billingName;
    }

    /**
     * Sets the value of the billingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingName(String value) {
        this.billingName = value;
    }

    /**
     * Gets the value of the billingPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPhone() {
        return billingPhone;
    }

    /**
     * Sets the value of the billingPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPhone(String value) {
        this.billingPhone = value;
    }

    /**
     * Gets the value of the business property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBusiness() {
        return business;
    }

    /**
     * Sets the value of the business property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBusiness(Long value) {
        this.business = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setContact(Long value) {
        this.contact = value;
    }

    /**
     * Gets the value of the expired property.
     * 
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Sets the value of the expired property.
     * 
     */
    public void setExpired(boolean value) {
        this.expired = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryDate(String value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the failedPaymentTransactionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFailedPaymentTransactionId() {
        return failedPaymentTransactionId;
    }

    /**
     * Sets the value of the failedPaymentTransactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFailedPaymentTransactionId(Long value) {
        this.failedPaymentTransactionId = value;
    }

    /**
     * Gets the value of the features property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeatures() {
        return features;
    }

    /**
     * Sets the value of the features property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeatures(String value) {
        this.features = value;
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
     * Gets the value of the invoice property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInvoice() {
        return invoice;
    }

    /**
     * Sets the value of the invoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInvoice(Long value) {
        this.invoice = value;
    }

    /**
     * Gets the value of the lastAutomaticMessageSent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastAutomaticMessageSent() {
        return lastAutomaticMessageSent;
    }

    /**
     * Sets the value of the lastAutomaticMessageSent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastAutomaticMessageSent(String value) {
        this.lastAutomaticMessageSent = value;
    }

    /**
     * Gets the value of the lastPayment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastPayment() {
        return lastPayment;
    }

    /**
     * Sets the value of the lastPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastPayment(String value) {
        this.lastPayment = value;
    }

    /**
     * Gets the value of the lastPaymentAmount property.
     * 
     */
    public long getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    /**
     * Sets the value of the lastPaymentAmount property.
     * 
     */
    public void setLastPaymentAmount(long value) {
        this.lastPaymentAmount = value;
    }

    /**
     * Gets the value of the lastPaymentAttempt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastPaymentAttempt() {
        return lastPaymentAttempt;
    }

    /**
     * Sets the value of the lastPaymentAttempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastPaymentAttempt(XMLGregorianCalendar value) {
        this.lastPaymentAttempt = value;
    }

    /**
     * Gets the value of the lastPaymentCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastPaymentCurrency() {
        return lastPaymentCurrency;
    }

    /**
     * Sets the value of the lastPaymentCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastPaymentCurrency(String value) {
        this.lastPaymentCurrency = value;
    }

    /**
     * Gets the value of the linkedContact property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLinkedContact() {
        return linkedContact;
    }

    /**
     * Sets the value of the linkedContact property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLinkedContact(Long value) {
        this.linkedContact = value;
    }

    /**
     * Gets the value of the linkedExpenseAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLinkedExpenseAccount() {
        return linkedExpenseAccount;
    }

    /**
     * Sets the value of the linkedExpenseAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLinkedExpenseAccount(Long value) {
        this.linkedExpenseAccount = value;
    }

    /**
     * Gets the value of the linkedPaymentAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLinkedPaymentAccount() {
        return linkedPaymentAccount;
    }

    /**
     * Sets the value of the linkedPaymentAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLinkedPaymentAccount(Long value) {
        this.linkedPaymentAccount = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the nextAnnualInvoiceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextAnnualInvoiceDate() {
        return nextAnnualInvoiceDate;
    }

    /**
     * Sets the value of the nextAnnualInvoiceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextAnnualInvoiceDate(String value) {
        this.nextAnnualInvoiceDate = value;
    }

    /**
     * Gets the value of the nextMonthlyInvoiceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextMonthlyInvoiceDate() {
        return nextMonthlyInvoiceDate;
    }

    /**
     * Sets the value of the nextMonthlyInvoiceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextMonthlyInvoiceDate(String value) {
        this.nextMonthlyInvoiceDate = value;
    }

    /**
     * Gets the value of the nextPayment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextPayment() {
        return nextPayment;
    }

    /**
     * Sets the value of the nextPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextPayment(String value) {
        this.nextPayment = value;
    }

    /**
     * Gets the value of the nextPaymentAmount property.
     * 
     */
    public long getNextPaymentAmount() {
        return nextPaymentAmount;
    }

    /**
     * Sets the value of the nextPaymentAmount property.
     * 
     */
    public void setNextPaymentAmount(long value) {
        this.nextPaymentAmount = value;
    }

    /**
     * Gets the value of the nextPaymentAttempt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNextPaymentAttempt() {
        return nextPaymentAttempt;
    }

    /**
     * Sets the value of the nextPaymentAttempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNextPaymentAttempt(XMLGregorianCalendar value) {
        this.nextPaymentAttempt = value;
    }

    /**
     * Gets the value of the nextPaymentCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextPaymentCurrency() {
        return nextPaymentCurrency;
    }

    /**
     * Sets the value of the nextPaymentCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextPaymentCurrency(String value) {
        this.nextPaymentCurrency = value;
    }

    /**
     * Gets the value of the paymentFailed property.
     * 
     */
    public boolean isPaymentFailed() {
        return paymentFailed;
    }

    /**
     * Sets the value of the paymentFailed property.
     * 
     */
    public void setPaymentFailed(boolean value) {
        this.paymentFailed = value;
    }

    /**
     * Gets the value of the paymentInformationAvailable property.
     * 
     */
    public boolean isPaymentInformationAvailable() {
        return paymentInformationAvailable;
    }

    /**
     * Sets the value of the paymentInformationAvailable property.
     * 
     */
    public void setPaymentInformationAvailable(boolean value) {
        this.paymentInformationAvailable = value;
    }

    /**
     * Gets the value of the pendingPlan property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPendingPlan() {
        return pendingPlan;
    }

    /**
     * Sets the value of the pendingPlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPendingPlan(Long value) {
        this.pendingPlan = value;
    }

    /**
     * Gets the value of the plan property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPlan() {
        return plan;
    }

    /**
     * Sets the value of the plan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPlan(Long value) {
        this.plan = value;
    }

    /**
     * Gets the value of the planChangeDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanChangeDate() {
        return planChangeDate;
    }

    /**
     * Sets the value of the planChangeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanChangeDate(String value) {
        this.planChangeDate = value;
    }

    /**
     * Gets the value of the planName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * Sets the value of the planName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanName(String value) {
        this.planName = value;
    }

    /**
     * Gets the value of the processor property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProcessor() {
        return processor;
    }

    /**
     * Sets the value of the processor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProcessor(Long value) {
        this.processor = value;
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
     * Gets the value of the trialEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrialEnd() {
        return trialEnd;
    }

    /**
     * Sets the value of the trialEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrialEnd(String value) {
        this.trialEnd = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(int value) {
        this.version = value;
    }

}
