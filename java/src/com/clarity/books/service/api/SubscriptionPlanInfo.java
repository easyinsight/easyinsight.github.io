
package com.clarity.books.service.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subscriptionPlanInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subscriptionPlanInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="annualPrice" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="business" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expiryDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="features" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="followingPlan" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="messages" type="{http://api.service.books/}collectionInfo" minOccurs="0"/>
 *         &lt;element name="monthlyPrice" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentMethods" type="{http://api.service.books/}subscriptionPaymentMethodInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="removed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="startupPrice" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="subscriptions" type="{http://api.service.books/}collectionInfo" minOccurs="0"/>
 *         &lt;element name="taxCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trialDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subscriptionPlanInfo", propOrder = {
    "account",
    "annualPrice",
    "business",
    "code",
    "currency",
    "description",
    "expiryDays",
    "features",
    "followingPlan",
    "id",
    "messages",
    "monthlyPrice",
    "name",
    "paymentMethods",
    "removed",
    "startupPrice",
    "subscriptions",
    "taxCode",
    "trialDays"
})
public class SubscriptionPlanInfo {

    protected Long account;
    protected int annualPrice;
    protected Long business;
    protected String code;
    protected String currency;
    protected String description;
    protected int expiryDays;
    protected String features;
    protected Long followingPlan;
    protected Long id;
    protected CollectionInfo messages;
    protected int monthlyPrice;
    protected String name;
    @XmlElement(nillable = true)
    protected List<SubscriptionPaymentMethodInfo> paymentMethods;
    protected boolean removed;
    protected int startupPrice;
    protected CollectionInfo subscriptions;
    protected String taxCode;
    protected int trialDays;

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
     * Gets the value of the annualPrice property.
     * 
     */
    public int getAnnualPrice() {
        return annualPrice;
    }

    /**
     * Sets the value of the annualPrice property.
     * 
     */
    public void setAnnualPrice(int value) {
        this.annualPrice = value;
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
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the expiryDays property.
     * 
     */
    public int getExpiryDays() {
        return expiryDays;
    }

    /**
     * Sets the value of the expiryDays property.
     * 
     */
    public void setExpiryDays(int value) {
        this.expiryDays = value;
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
     * Gets the value of the followingPlan property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFollowingPlan() {
        return followingPlan;
    }

    /**
     * Sets the value of the followingPlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFollowingPlan(Long value) {
        this.followingPlan = value;
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
     * Gets the value of the messages property.
     * 
     * @return
     *     possible object is
     *     {@link CollectionInfo }
     *     
     */
    public CollectionInfo getMessages() {
        return messages;
    }

    /**
     * Sets the value of the messages property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollectionInfo }
     *     
     */
    public void setMessages(CollectionInfo value) {
        this.messages = value;
    }

    /**
     * Gets the value of the monthlyPrice property.
     * 
     */
    public int getMonthlyPrice() {
        return monthlyPrice;
    }

    /**
     * Sets the value of the monthlyPrice property.
     * 
     */
    public void setMonthlyPrice(int value) {
        this.monthlyPrice = value;
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
     * Gets the value of the paymentMethods property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paymentMethods property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPaymentMethods().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubscriptionPaymentMethodInfo }
     * 
     * 
     */
    public List<SubscriptionPaymentMethodInfo> getPaymentMethods() {
        if (paymentMethods == null) {
            paymentMethods = new ArrayList<SubscriptionPaymentMethodInfo>();
        }
        return this.paymentMethods;
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
     * Gets the value of the startupPrice property.
     * 
     */
    public int getStartupPrice() {
        return startupPrice;
    }

    /**
     * Sets the value of the startupPrice property.
     * 
     */
    public void setStartupPrice(int value) {
        this.startupPrice = value;
    }

    /**
     * Gets the value of the subscriptions property.
     * 
     * @return
     *     possible object is
     *     {@link CollectionInfo }
     *     
     */
    public CollectionInfo getSubscriptions() {
        return subscriptions;
    }

    /**
     * Sets the value of the subscriptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollectionInfo }
     *     
     */
    public void setSubscriptions(CollectionInfo value) {
        this.subscriptions = value;
    }

    /**
     * Gets the value of the taxCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * Sets the value of the taxCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxCode(String value) {
        this.taxCode = value;
    }

    /**
     * Gets the value of the trialDays property.
     * 
     */
    public int getTrialDays() {
        return trialDays;
    }

    /**
     * Sets the value of the trialDays property.
     * 
     */
    public void setTrialDays(int value) {
        this.trialDays = value;
    }

}
