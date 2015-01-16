
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SsoLoginRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SsoLoginRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ssoPassport" type="{urn:core_2014_1.platform.webservices.netsuite.com}SsoPassport"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SsoLoginRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "ssoPassport"
})
public class SsoLoginRequest {

    @XmlElement(required = true)
    protected SsoPassport ssoPassport;

    /**
     * Gets the value of the ssoPassport property.
     * 
     * @return
     *     possible object is
     *     {@link SsoPassport }
     *     
     */
    public SsoPassport getSsoPassport() {
        return ssoPassport;
    }

    /**
     * Sets the value of the ssoPassport property.
     * 
     * @param value
     *     allowed object is
     *     {@link SsoPassport }
     *     
     */
    public void setSsoPassport(SsoPassport value) {
        this.ssoPassport = value;
    }

}
