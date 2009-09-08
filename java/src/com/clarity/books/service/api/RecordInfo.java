
package com.clarity.books.service.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recordInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recordInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="adjustments" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="adjustment" type="{http://api.service.books/}adjustmentInfo" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="balanceDue" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="business" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="businessContactInformation" type="{http://api.service.books/}contactInformationInfo" minOccurs="0"/>
 *         &lt;element name="collaborationContext" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="contactInformation" type="{http://api.service.books/}contactInformationInfo" minOccurs="0"/>
 *         &lt;element name="currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="depositEntry" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="draft" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entries" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" type="{http://api.service.books/}entryInfo" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="exchangeRate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="journalEntry" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="memo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overpayment" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="paid" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="paymentAllocationsIn" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="paymentAllocation" type="{http://api.service.books/}paymentAllocationInfo" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="paymentAllocationsOut" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="paymentAllocation" type="{http://api.service.books/}paymentAllocationInfo" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="payments" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="payment" type="{http://api.service.books/}paymentInfo" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="poNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="readOnly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="removed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="taxes" type="{http://api.service.books/}taxEntryInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="terms" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalDue" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="transferAccount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="transferAmount" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="transferCurrency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://api.service.books/}recordType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recordInfo", propOrder = {
    "account",
    "adjustments",
    "balanceDue",
    "business",
    "businessContactInformation",
    "collaborationContext",
    "contact",
    "contactInformation",
    "currency",
    "date",
    "depositEntry",
    "draft",
    "dueDate",
    "entries",
    "exchangeRate",
    "id",
    "journalEntry",
    "memo",
    "number",
    "overpayment",
    "paid",
    "paymentAllocationsIn",
    "paymentAllocationsOut",
    "payments",
    "poNumber",
    "readOnly",
    "removed",
    "taxes",
    "terms",
    "totalDue",
    "transferAccount",
    "transferAmount",
    "transferCurrency",
    "type"
})
public class RecordInfo {

    protected Long account;
    protected RecordInfo.Adjustments adjustments;
    protected long balanceDue;
    protected Long business;
    protected ContactInformationInfo businessContactInformation;
    protected Long collaborationContext;
    protected Long contact;
    protected ContactInformationInfo contactInformation;
    protected String currency;
    protected String date;
    protected Long depositEntry;
    protected boolean draft;
    protected String dueDate;
    protected RecordInfo.Entries entries;
    protected Double exchangeRate;
    protected Long id;
    protected Long journalEntry;
    protected String memo;
    protected String number;
    protected long overpayment;
    protected boolean paid;
    protected RecordInfo.PaymentAllocationsIn paymentAllocationsIn;
    protected RecordInfo.PaymentAllocationsOut paymentAllocationsOut;
    protected RecordInfo.Payments payments;
    protected String poNumber;
    protected boolean readOnly;
    protected boolean removed;
    @XmlElement(nillable = true)
    protected List<TaxEntryInfo> taxes;
    protected String terms;
    protected long totalDue;
    protected Long transferAccount;
    protected long transferAmount;
    protected String transferCurrency;
    protected RecordType type;

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAccount(Long value) {
        this.account = value;
    }

    /**
     * Gets the value of the adjustments property.
     * 
     * @return
     *     possible object is
     *     {@link RecordInfo.Adjustments }
     *     
     */
    public RecordInfo.Adjustments getAdjustments() {
        return adjustments;
    }

    /**
     * Sets the value of the adjustments property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordInfo.Adjustments }
     *     
     */
    public void setAdjustments(RecordInfo.Adjustments value) {
        this.adjustments = value;
    }

    /**
     * Gets the value of the balanceDue property.
     * 
     */
    public long getBalanceDue() {
        return balanceDue;
    }

    /**
     * Sets the value of the balanceDue property.
     * 
     */
    public void setBalanceDue(long value) {
        this.balanceDue = value;
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
     * Gets the value of the businessContactInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInformationInfo }
     *     
     */
    public ContactInformationInfo getBusinessContactInformation() {
        return businessContactInformation;
    }

    /**
     * Sets the value of the businessContactInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInformationInfo }
     *     
     */
    public void setBusinessContactInformation(ContactInformationInfo value) {
        this.businessContactInformation = value;
    }

    /**
     * Gets the value of the collaborationContext property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCollaborationContext() {
        return collaborationContext;
    }

    /**
     * Sets the value of the collaborationContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCollaborationContext(Long value) {
        this.collaborationContext = value;
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
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the depositEntry property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDepositEntry() {
        return depositEntry;
    }

    /**
     * Sets the value of the depositEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDepositEntry(Long value) {
        this.depositEntry = value;
    }

    /**
     * Gets the value of the draft property.
     * 
     */
    public boolean isDraft() {
        return draft;
    }

    /**
     * Sets the value of the draft property.
     * 
     */
    public void setDraft(boolean value) {
        this.draft = value;
    }

    /**
     * Gets the value of the dueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Sets the value of the dueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDueDate(String value) {
        this.dueDate = value;
    }

    /**
     * Gets the value of the entries property.
     * 
     * @return
     *     possible object is
     *     {@link RecordInfo.Entries }
     *     
     */
    public RecordInfo.Entries getEntries() {
        return entries;
    }

    /**
     * Sets the value of the entries property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordInfo.Entries }
     *     
     */
    public void setEntries(RecordInfo.Entries value) {
        this.entries = value;
    }

    /**
     * Gets the value of the exchangeRate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * Sets the value of the exchangeRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setExchangeRate(Double value) {
        this.exchangeRate = value;
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
     * Gets the value of the journalEntry property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getJournalEntry() {
        return journalEntry;
    }

    /**
     * Sets the value of the journalEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setJournalEntry(Long value) {
        this.journalEntry = value;
    }

    /**
     * Gets the value of the memo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Sets the value of the memo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemo(String value) {
        this.memo = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the overpayment property.
     * 
     */
    public long getOverpayment() {
        return overpayment;
    }

    /**
     * Sets the value of the overpayment property.
     * 
     */
    public void setOverpayment(long value) {
        this.overpayment = value;
    }

    /**
     * Gets the value of the paid property.
     * 
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Sets the value of the paid property.
     * 
     */
    public void setPaid(boolean value) {
        this.paid = value;
    }

    /**
     * Gets the value of the paymentAllocationsIn property.
     * 
     * @return
     *     possible object is
     *     {@link RecordInfo.PaymentAllocationsIn }
     *     
     */
    public RecordInfo.PaymentAllocationsIn getPaymentAllocationsIn() {
        return paymentAllocationsIn;
    }

    /**
     * Sets the value of the paymentAllocationsIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordInfo.PaymentAllocationsIn }
     *     
     */
    public void setPaymentAllocationsIn(RecordInfo.PaymentAllocationsIn value) {
        this.paymentAllocationsIn = value;
    }

    /**
     * Gets the value of the paymentAllocationsOut property.
     * 
     * @return
     *     possible object is
     *     {@link RecordInfo.PaymentAllocationsOut }
     *     
     */
    public RecordInfo.PaymentAllocationsOut getPaymentAllocationsOut() {
        return paymentAllocationsOut;
    }

    /**
     * Sets the value of the paymentAllocationsOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordInfo.PaymentAllocationsOut }
     *     
     */
    public void setPaymentAllocationsOut(RecordInfo.PaymentAllocationsOut value) {
        this.paymentAllocationsOut = value;
    }

    /**
     * Gets the value of the payments property.
     * 
     * @return
     *     possible object is
     *     {@link RecordInfo.Payments }
     *     
     */
    public RecordInfo.Payments getPayments() {
        return payments;
    }

    /**
     * Sets the value of the payments property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordInfo.Payments }
     *     
     */
    public void setPayments(RecordInfo.Payments value) {
        this.payments = value;
    }

    /**
     * Gets the value of the poNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * Sets the value of the poNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoNumber(String value) {
        this.poNumber = value;
    }

    /**
     * Gets the value of the readOnly property.
     * 
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the value of the readOnly property.
     * 
     */
    public void setReadOnly(boolean value) {
        this.readOnly = value;
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
     * Gets the value of the taxes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taxes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaxes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaxEntryInfo }
     * 
     * 
     */
    public List<TaxEntryInfo> getTaxes() {
        if (taxes == null) {
            taxes = new ArrayList<TaxEntryInfo>();
        }
        return this.taxes;
    }

    /**
     * Gets the value of the terms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerms() {
        return terms;
    }

    /**
     * Sets the value of the terms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerms(String value) {
        this.terms = value;
    }

    /**
     * Gets the value of the totalDue property.
     * 
     */
    public long getTotalDue() {
        return totalDue;
    }

    /**
     * Sets the value of the totalDue property.
     * 
     */
    public void setTotalDue(long value) {
        this.totalDue = value;
    }

    /**
     * Gets the value of the transferAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTransferAccount() {
        return transferAccount;
    }

    /**
     * Sets the value of the transferAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTransferAccount(Long value) {
        this.transferAccount = value;
    }

    /**
     * Gets the value of the transferAmount property.
     * 
     */
    public long getTransferAmount() {
        return transferAmount;
    }

    /**
     * Sets the value of the transferAmount property.
     * 
     */
    public void setTransferAmount(long value) {
        this.transferAmount = value;
    }

    /**
     * Gets the value of the transferCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferCurrency() {
        return transferCurrency;
    }

    /**
     * Sets the value of the transferCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferCurrency(String value) {
        this.transferCurrency = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setType(RecordType value) {
        this.type = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="adjustment" type="{http://api.service.books/}adjustmentInfo" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "adjustment"
    })
    public static class Adjustments {

        protected List<AdjustmentInfo> adjustment;

        /**
         * Gets the value of the adjustment property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the adjustment property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAdjustment().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdjustmentInfo }
         * 
         * 
         */
        public List<AdjustmentInfo> getAdjustment() {
            if (adjustment == null) {
                adjustment = new ArrayList<AdjustmentInfo>();
            }
            return this.adjustment;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" type="{http://api.service.books/}entryInfo" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class Entries {

        protected List<EntryInfo> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link EntryInfo }
         * 
         * 
         */
        public List<EntryInfo> getEntry() {
            if (entry == null) {
                entry = new ArrayList<EntryInfo>();
            }
            return this.entry;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="paymentAllocation" type="{http://api.service.books/}paymentAllocationInfo" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "paymentAllocation"
    })
    public static class PaymentAllocationsIn {

        protected List<PaymentAllocationInfo> paymentAllocation;

        /**
         * Gets the value of the paymentAllocation property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the paymentAllocation property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPaymentAllocation().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PaymentAllocationInfo }
         * 
         * 
         */
        public List<PaymentAllocationInfo> getPaymentAllocation() {
            if (paymentAllocation == null) {
                paymentAllocation = new ArrayList<PaymentAllocationInfo>();
            }
            return this.paymentAllocation;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="paymentAllocation" type="{http://api.service.books/}paymentAllocationInfo" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "paymentAllocation"
    })
    public static class PaymentAllocationsOut {

        protected List<PaymentAllocationInfo> paymentAllocation;

        /**
         * Gets the value of the paymentAllocation property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the paymentAllocation property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPaymentAllocation().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PaymentAllocationInfo }
         * 
         * 
         */
        public List<PaymentAllocationInfo> getPaymentAllocation() {
            if (paymentAllocation == null) {
                paymentAllocation = new ArrayList<PaymentAllocationInfo>();
            }
            return this.paymentAllocation;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="payment" type="{http://api.service.books/}paymentInfo" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "payment"
    })
    public static class Payments {

        protected List<PaymentInfo> payment;

        /**
         * Gets the value of the payment property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the payment property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPayment().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PaymentInfo }
         * 
         * 
         */
        public List<PaymentInfo> getPayment() {
            if (payment == null) {
                payment = new ArrayList<PaymentInfo>();
            }
            return this.payment;
        }

    }

}
