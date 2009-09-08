
package com.clarity.books.service.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for collaborationContextInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="collaborationContextInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="events" type="{http://api.service.books/}collaborationEventInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="mayAddAttachments" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="mayAddComments" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="record" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tags" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collaborationContextInfo", propOrder = {
    "contact",
    "events",
    "id",
    "mayAddAttachments",
    "mayAddComments",
    "record",
    "tags"
})
public class CollaborationContextInfo {

    protected Long contact;
    @XmlElement(nillable = true)
    protected List<CollaborationEventInfo> events;
    protected Long id;
    protected boolean mayAddAttachments;
    protected boolean mayAddComments;
    protected Long record;
    protected String tags;

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setContact(Long value) {
        this.contact = value;
    }

    /**
     * Gets the value of the events property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the events property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CollaborationEventInfo }
     * 
     * 
     */
    public List<CollaborationEventInfo> getEvents() {
        if (events == null) {
            events = new ArrayList<CollaborationEventInfo>();
        }
        return this.events;
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
     * Gets the value of the mayAddAttachments property.
     * 
     */
    public boolean isMayAddAttachments() {
        return mayAddAttachments;
    }

    /**
     * Sets the value of the mayAddAttachments property.
     * 
     */
    public void setMayAddAttachments(boolean value) {
        this.mayAddAttachments = value;
    }

    /**
     * Gets the value of the mayAddComments property.
     * 
     */
    public boolean isMayAddComments() {
        return mayAddComments;
    }

    /**
     * Sets the value of the mayAddComments property.
     * 
     */
    public void setMayAddComments(boolean value) {
        this.mayAddComments = value;
    }

    /**
     * Gets the value of the record property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRecord(Long value) {
        this.record = value;
    }

    /**
     * Gets the value of the tags property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the value of the tags property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTags(String value) {
        this.tags = value;
    }

}
