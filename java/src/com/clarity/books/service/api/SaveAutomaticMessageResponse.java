
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveAutomaticMessageResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveAutomaticMessageResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="message" type="{http://api.service.books/}automaticMessageInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveAutomaticMessageResponse", propOrder = {
    "message"
})
public class SaveAutomaticMessageResponse {

    protected AutomaticMessageInfo message;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link AutomaticMessageInfo }
     *     
     */
    public AutomaticMessageInfo getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link AutomaticMessageInfo }
     *     
     */
    public void setMessage(AutomaticMessageInfo value) {
        this.message = value;
    }

}
