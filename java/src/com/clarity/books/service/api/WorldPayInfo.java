
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for worldPayInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="worldPayInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="account" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="authPW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="business" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="callbackPW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="instId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="md5Secret" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remoteAdminInstId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="testMode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "worldPayInfo", propOrder = {
    "account",
    "authPW",
    "business",
    "callbackPW",
    "id",
    "instId",
    "md5Secret",
    "remoteAdminInstId",
    "testMode"
})
public class WorldPayInfo {

    protected Long account;
    protected String authPW;
    protected Long business;
    protected String callbackPW;
    protected Long id;
    protected int instId;
    protected String md5Secret;
    protected int remoteAdminInstId;
    protected boolean testMode;

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
     * Gets the value of the authPW property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthPW() {
        return authPW;
    }

    /**
     * Sets the value of the authPW property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthPW(String value) {
        this.authPW = value;
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
     * Gets the value of the callbackPW property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallbackPW() {
        return callbackPW;
    }

    /**
     * Sets the value of the callbackPW property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallbackPW(String value) {
        this.callbackPW = value;
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
     * Gets the value of the instId property.
     * 
     */
    public int getInstId() {
        return instId;
    }

    /**
     * Sets the value of the instId property.
     * 
     */
    public void setInstId(int value) {
        this.instId = value;
    }

    /**
     * Gets the value of the md5Secret property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMd5Secret() {
        return md5Secret;
    }

    /**
     * Sets the value of the md5Secret property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMd5Secret(String value) {
        this.md5Secret = value;
    }

    /**
     * Gets the value of the remoteAdminInstId property.
     * 
     */
    public int getRemoteAdminInstId() {
        return remoteAdminInstId;
    }

    /**
     * Sets the value of the remoteAdminInstId property.
     * 
     */
    public void setRemoteAdminInstId(int value) {
        this.remoteAdminInstId = value;
    }

    /**
     * Gets the value of the testMode property.
     * 
     */
    public boolean isTestMode() {
        return testMode;
    }

    /**
     * Sets the value of the testMode property.
     * 
     */
    public void setTestMode(boolean value) {
        this.testMode = value;
    }

}
