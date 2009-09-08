
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveWorldPay complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveWorldPay">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://api.service.books/}authToken" minOccurs="0"/>
 *         &lt;element name="worldPay" type="{http://api.service.books/}worldPayInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveWorldPay", propOrder = {
    "token",
    "worldPay"
})
public class SaveWorldPay {

    protected AuthToken token;
    protected WorldPayInfo worldPay;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link AuthToken }
     *     
     */
    public AuthToken getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthToken }
     *     
     */
    public void setToken(AuthToken value) {
        this.token = value;
    }

    /**
     * Gets the value of the worldPay property.
     * 
     * @return
     *     possible object is
     *     {@link WorldPayInfo }
     *     
     */
    public WorldPayInfo getWorldPay() {
        return worldPay;
    }

    /**
     * Sets the value of the worldPay property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorldPayInfo }
     *     
     */
    public void setWorldPay(WorldPayInfo value) {
        this.worldPay = value;
    }

}
