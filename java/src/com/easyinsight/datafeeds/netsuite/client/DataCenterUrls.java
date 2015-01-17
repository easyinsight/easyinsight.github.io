
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataCenterUrls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataCenterUrls">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="restDomain" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="webservicesDomain" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="systemDomain" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataCenterUrls", namespace = "urn:core_2014_1.platform.webservices.netsuite.com", propOrder = {
    "restDomain",
    "webservicesDomain",
    "systemDomain"
})
public class DataCenterUrls {

    @XmlElement(required = true)
    protected String restDomain;
    @XmlElement(required = true)
    protected String webservicesDomain;
    @XmlElement(required = true)
    protected String systemDomain;

    /**
     * Gets the value of the restDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestDomain() {
        return restDomain;
    }

    /**
     * Sets the value of the restDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestDomain(String value) {
        this.restDomain = value;
    }

    /**
     * Gets the value of the webservicesDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebservicesDomain() {
        return webservicesDomain;
    }

    /**
     * Sets the value of the webservicesDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebservicesDomain(String value) {
        this.webservicesDomain = value;
    }

    /**
     * Gets the value of the systemDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemDomain() {
        return systemDomain;
    }

    /**
     * Sets the value of the systemDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemDomain(String value) {
        this.systemDomain = value;
    }

}
