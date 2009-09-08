
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getWorldPayResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getWorldPayResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
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
@XmlType(name = "getWorldPayResponse", propOrder = {
    "worldPay"
})
public class GetWorldPayResponse {

    protected WorldPayInfo worldPay;

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
