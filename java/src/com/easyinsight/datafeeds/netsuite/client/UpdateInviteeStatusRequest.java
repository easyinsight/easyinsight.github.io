
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateInviteeStatusRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateInviteeStatusRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="updateInviteeStatusReference" type="{urn:core_2014_1.platform.webservices.netsuite.com}UpdateInviteeStatusReference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateInviteeStatusRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "updateInviteeStatusReference"
})
public class UpdateInviteeStatusRequest {

    @XmlElement(required = true)
    protected UpdateInviteeStatusReference updateInviteeStatusReference;

    /**
     * Gets the value of the updateInviteeStatusReference property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateInviteeStatusReference }
     *     
     */
    public UpdateInviteeStatusReference getUpdateInviteeStatusReference() {
        return updateInviteeStatusReference;
    }

    /**
     * Sets the value of the updateInviteeStatusReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateInviteeStatusReference }
     *     
     */
    public void setUpdateInviteeStatusReference(UpdateInviteeStatusReference value) {
        this.updateInviteeStatusReference = value;
    }

}
