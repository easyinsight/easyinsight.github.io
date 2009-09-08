
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveCollaborationEventResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveCollaborationEventResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collaborationEvent" type="{http://api.service.books/}collaborationEventInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveCollaborationEventResponse", propOrder = {
    "collaborationEvent"
})
public class SaveCollaborationEventResponse {

    protected CollaborationEventInfo collaborationEvent;

    /**
     * Gets the value of the collaborationEvent property.
     * 
     * @return
     *     possible object is
     *     {@link CollaborationEventInfo }
     *     
     */
    public CollaborationEventInfo getCollaborationEvent() {
        return collaborationEvent;
    }

    /**
     * Sets the value of the collaborationEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollaborationEventInfo }
     *     
     */
    public void setCollaborationEvent(CollaborationEventInfo value) {
        this.collaborationEvent = value;
    }

}
