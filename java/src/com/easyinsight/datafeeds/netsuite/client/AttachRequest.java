
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttachRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttachRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attachReference" type="{urn:core_2014_1.platform.webservices.netsuite.com}AttachReference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttachRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "attachReference"
})
public class AttachRequest {

    @XmlElement(required = true)
    protected AttachReference attachReference;

    /**
     * Gets the value of the attachReference property.
     * 
     * @return
     *     possible object is
     *     {@link AttachReference }
     *     
     */
    public AttachReference getAttachReference() {
        return attachReference;
    }

    /**
     * Sets the value of the attachReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttachReference }
     *     
     */
    public void setAttachReference(AttachReference value) {
        this.attachReference = value;
    }

}
