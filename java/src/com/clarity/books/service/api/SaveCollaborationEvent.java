
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveCollaborationEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveCollaborationEvent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://api.service.books/}authToken" minOccurs="0"/>
 *         &lt;element name="event" type="{http://api.service.books/}collaborationEventInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveCollaborationEvent", propOrder = {
    "token",
    "event"
})
public class SaveCollaborationEvent {

    protected AuthToken token;
    protected CollaborationEventInfo event;

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
     * Gets the value of the event property.
     * 
     * @return
     *     possible object is
     *     {@link CollaborationEventInfo }
     *     
     */
    public CollaborationEventInfo getEvent() {
        return event;
    }

    /**
     * Sets the value of the event property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollaborationEventInfo }
     *     
     */
    public void setEvent(CollaborationEventInfo value) {
        this.event = value;
    }

}
