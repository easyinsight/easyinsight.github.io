
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for userInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="userInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountingProfessional" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="activationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="betaTester" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="contactInformation" type="{http://api.service.books/}contactInformationInfo" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="lastLogin" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastPasswordReset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="readOnly" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="removed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="signUpDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="staff" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userInfo", propOrder = {
    "accountingProfessional",
    "activationDate",
    "betaTester",
    "contactInformation",
    "id",
    "lastLogin",
    "lastPasswordReset",
    "readOnly",
    "removed",
    "signUpDate",
    "staff"
})
public class UserInfo {

    protected boolean accountingProfessional;
    protected String activationDate;
    protected boolean betaTester;
    protected ContactInformationInfo contactInformation;
    protected Long id;
    protected XMLGregorianCalendar lastLogin;
    protected String lastPasswordReset;
    protected boolean readOnly;
    protected boolean removed;
    protected String signUpDate;
    protected boolean staff;

    /**
     * Gets the value of the accountingProfessional property.
     * 
     */
    public boolean isAccountingProfessional() {
        return accountingProfessional;
    }

    /**
     * Sets the value of the accountingProfessional property.
     * 
     */
    public void setAccountingProfessional(boolean value) {
        this.accountingProfessional = value;
    }

    /**
     * Gets the value of the activationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivationDate() {
        return activationDate;
    }

    /**
     * Sets the value of the activationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivationDate(String value) {
        this.activationDate = value;
    }

    /**
     * Gets the value of the betaTester property.
     * 
     */
    public boolean isBetaTester() {
        return betaTester;
    }

    /**
     * Sets the value of the betaTester property.
     * 
     */
    public void setBetaTester(boolean value) {
        this.betaTester = value;
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
     * Gets the value of the lastLogin property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets the value of the lastLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastLogin(XMLGregorianCalendar value) {
        this.lastLogin = value;
    }

    /**
     * Gets the value of the lastPasswordReset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastPasswordReset() {
        return lastPasswordReset;
    }

    /**
     * Sets the value of the lastPasswordReset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastPasswordReset(String value) {
        this.lastPasswordReset = value;
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
     * Gets the value of the signUpDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignUpDate() {
        return signUpDate;
    }

    /**
     * Sets the value of the signUpDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignUpDate(String value) {
        this.signUpDate = value;
    }

    /**
     * Gets the value of the staff property.
     * 
     */
    public boolean isStaff() {
        return staff;
    }

    /**
     * Sets the value of the staff property.
     * 
     */
    public void setStaff(boolean value) {
        this.staff = value;
    }

}
