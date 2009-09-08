
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for canadianTaxInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="canadianTaxInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="gstNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gstRegistered" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="pstNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pstRegistered" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "canadianTaxInfo", propOrder = {
    "active",
    "gstNumber",
    "gstRegistered",
    "pstNumber",
    "pstRegistered"
})
public class CanadianTaxInfo {

    protected boolean active;
    protected String gstNumber;
    protected boolean gstRegistered;
    protected String pstNumber;
    protected boolean pstRegistered;

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the gstNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGstNumber() {
        return gstNumber;
    }

    /**
     * Sets the value of the gstNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGstNumber(String value) {
        this.gstNumber = value;
    }

    /**
     * Gets the value of the gstRegistered property.
     * 
     */
    public boolean isGstRegistered() {
        return gstRegistered;
    }

    /**
     * Sets the value of the gstRegistered property.
     * 
     */
    public void setGstRegistered(boolean value) {
        this.gstRegistered = value;
    }

    /**
     * Gets the value of the pstNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPstNumber() {
        return pstNumber;
    }

    /**
     * Sets the value of the pstNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPstNumber(String value) {
        this.pstNumber = value;
    }

    /**
     * Gets the value of the pstRegistered property.
     * 
     */
    public boolean isPstRegistered() {
        return pstRegistered;
    }

    /**
     * Sets the value of the pstRegistered property.
     * 
     */
    public void setPstRegistered(boolean value) {
        this.pstRegistered = value;
    }

}
