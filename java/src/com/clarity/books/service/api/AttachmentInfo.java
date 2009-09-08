
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for attachmentInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attachmentInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="business" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="characterEncoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="complete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="contentLength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="contentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="initialData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicAccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="uploaderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attachmentInfo", propOrder = {
    "business",
    "characterEncoding",
    "complete",
    "contentLength",
    "contentType",
    "id",
    "initialData",
    "name",
    "publicAccess",
    "timestamp",
    "uploaderName"
})
public class AttachmentInfo {

    protected Long business;
    protected String characterEncoding;
    protected boolean complete;
    protected int contentLength;
    protected String contentType;
    protected Long id;
    protected byte[] initialData;
    protected String name;
    protected boolean publicAccess;
    protected XMLGregorianCalendar timestamp;
    protected String uploaderName;

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
     * Gets the value of the characterEncoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * Sets the value of the characterEncoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCharacterEncoding(String value) {
        this.characterEncoding = value;
    }

    /**
     * Gets the value of the complete property.
     * 
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Sets the value of the complete property.
     * 
     */
    public void setComplete(boolean value) {
        this.complete = value;
    }

    /**
     * Gets the value of the contentLength property.
     * 
     */
    public int getContentLength() {
        return contentLength;
    }

    /**
     * Sets the value of the contentLength property.
     * 
     */
    public void setContentLength(int value) {
        this.contentLength = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
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
     * Gets the value of the initialData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getInitialData() {
        return initialData;
    }

    /**
     * Sets the value of the initialData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setInitialData(byte[] value) {
        this.initialData = ((byte[]) value);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the publicAccess property.
     * 
     */
    public boolean isPublicAccess() {
        return publicAccess;
    }

    /**
     * Sets the value of the publicAccess property.
     * 
     */
    public void setPublicAccess(boolean value) {
        this.publicAccess = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the uploaderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploaderName() {
        return uploaderName;
    }

    /**
     * Sets the value of the uploaderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploaderName(String value) {
        this.uploaderName = value;
    }

}
