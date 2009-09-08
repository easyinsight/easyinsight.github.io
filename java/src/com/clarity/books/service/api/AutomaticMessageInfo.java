
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for automaticMessageInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="automaticMessageInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dateReference" type="{http://api.service.books/}dateReference" minOccurs="0"/>
 *         &lt;element name="days" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="htmlMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="plainMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="plan" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="removed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "automaticMessageInfo", propOrder = {
    "active",
    "dateReference",
    "days",
    "htmlMessage",
    "id",
    "plainMessage",
    "plan",
    "removed",
    "startDate",
    "subject"
})
public class AutomaticMessageInfo {

    protected boolean active;
    protected DateReference dateReference;
    protected int days;
    protected String htmlMessage;
    protected Long id;
    protected String plainMessage;
    protected Long plan;
    protected boolean removed;
    protected String startDate;
    protected String subject;

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the dateReference property.
     * 
     * @return
     *     possible object is
     *     {@link DateReference }
     *     
     */
    public DateReference getDateReference() {
        return dateReference;
    }

    /**
     * Sets the value of the dateReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateReference }
     *     
     */
    public void setDateReference(DateReference value) {
        this.dateReference = value;
    }

    /**
     * Gets the value of the days property.
     * 
     */
    public int getDays() {
        return days;
    }

    /**
     * Sets the value of the days property.
     * 
     */
    public void setDays(int value) {
        this.days = value;
    }

    /**
     * Gets the value of the htmlMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHtmlMessage() {
        return htmlMessage;
    }

    /**
     * Sets the value of the htmlMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHtmlMessage(String value) {
        this.htmlMessage = value;
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
     * Gets the value of the plainMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlainMessage() {
        return plainMessage;
    }

    /**
     * Sets the value of the plainMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlainMessage(String value) {
        this.plainMessage = value;
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
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

}
