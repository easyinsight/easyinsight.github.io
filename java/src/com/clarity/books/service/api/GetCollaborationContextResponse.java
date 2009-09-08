
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCollaborationContextResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCollaborationContextResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="context" type="{http://api.service.books/}collaborationContextInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCollaborationContextResponse", propOrder = {
    "context"
})
public class GetCollaborationContextResponse {

    protected CollaborationContextInfo context;

    /**
     * Gets the value of the context property.
     * 
     * @return
     *     possible object is
     *     {@link CollaborationContextInfo }
     *     
     */
    public CollaborationContextInfo getContext() {
        return context;
    }

    /**
     * Sets the value of the context property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollaborationContextInfo }
     *     
     */
    public void setContext(CollaborationContextInfo value) {
        this.context = value;
    }

}
