
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChangeEmailRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChangeEmailRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="changeEmail" type="{urn:core_2014_1.platform.webservices.netsuite.com}ChangeEmail"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeEmailRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "changeEmail"
})
public class ChangeEmailRequest {

    @XmlElement(required = true)
    protected ChangeEmail changeEmail;

    /**
     * Gets the value of the changeEmail property.
     * 
     * @return
     *     possible object is
     *     {@link ChangeEmail }
     *     
     */
    public ChangeEmail getChangeEmail() {
        return changeEmail;
    }

    /**
     * Sets the value of the changeEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangeEmail }
     *     
     */
    public void setChangeEmail(ChangeEmail value) {
        this.changeEmail = value;
    }

}
