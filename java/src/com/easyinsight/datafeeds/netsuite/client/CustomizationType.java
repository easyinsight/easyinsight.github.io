
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomizationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomizationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="getCustomizationType" type="{urn:types.core_2014_1.platform.webservices.netsuite.com}GetCustomizationType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomizationType", namespace = "urn:core_2014_1.platform.webservices.netsuite.com")
public class CustomizationType {

    @XmlAttribute
    protected GetCustomizationType getCustomizationType;

    /**
     * Gets the value of the getCustomizationType property.
     * 
     * @return
     *     possible object is
     *     {@link GetCustomizationType }
     *     
     */
    public GetCustomizationType getGetCustomizationType() {
        return getCustomizationType;
    }

    /**
     * Sets the value of the getCustomizationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetCustomizationType }
     *     
     */
    public void setGetCustomizationType(GetCustomizationType value) {
        this.getCustomizationType = value;
    }

}
