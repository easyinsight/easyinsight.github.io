
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomSearchRowBasic complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomSearchRowBasic">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customizationRef" type="{urn:core_2014_1.platform.webservices.netsuite.com}CustomizationRef"/>
 *         &lt;element name="searchRowBasic" type="{urn:core_2014_1.platform.webservices.netsuite.com}SearchRowBasic"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomSearchRowBasic", namespace = "urn:common_2014_1.platform.webservices.netsuite.com", propOrder = {
    "customizationRef",
    "searchRowBasic"
})
public class CustomSearchRowBasic {

    @XmlElement(required = true)
    protected CustomizationRef customizationRef;
    @XmlElement(required = true)
    protected SearchRowBasic searchRowBasic;

    /**
     * Gets the value of the customizationRef property.
     * 
     * @return
     *     possible object is
     *     {@link CustomizationRef }
     *     
     */
    public CustomizationRef getCustomizationRef() {
        return customizationRef;
    }

    /**
     * Sets the value of the customizationRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomizationRef }
     *     
     */
    public void setCustomizationRef(CustomizationRef value) {
        this.customizationRef = value;
    }

    /**
     * Gets the value of the searchRowBasic property.
     * 
     * @return
     *     possible object is
     *     {@link SearchRowBasic }
     *     
     */
    public SearchRowBasic getSearchRowBasic() {
        return searchRowBasic;
    }

    /**
     * Sets the value of the searchRowBasic property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchRowBasic }
     *     
     */
    public void setSearchRowBasic(SearchRowBasic value) {
        this.searchRowBasic = value;
    }

}
