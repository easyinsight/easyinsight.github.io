
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentSetupInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paymentSetupInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logoUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="method" type="{http://api.service.books/}paymentMethod" minOccurs="0"/>
 *         &lt;element name="paymentInformationAvailable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="processorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="setupUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentSetupInfo", propOrder = {
    "errorString",
    "label",
    "logoUrl",
    "method",
    "paymentInformationAvailable",
    "processorName",
    "setupUrl",
    "statusString"
})
public class PaymentSetupInfo {

    protected String errorString;
    protected String label;
    protected String logoUrl;
    protected PaymentMethod method;
    protected boolean paymentInformationAvailable;
    protected String processorName;
    protected String setupUrl;
    protected String statusString;

    /**
     * Gets the value of the errorString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorString() {
        return errorString;
    }

    /**
     * Sets the value of the errorString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorString(String value) {
        this.errorString = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the logoUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * Sets the value of the logoUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogoUrl(String value) {
        this.logoUrl = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentMethod }
     *     
     */
    public PaymentMethod getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentMethod }
     *     
     */
    public void setMethod(PaymentMethod value) {
        this.method = value;
    }

    /**
     * Gets the value of the paymentInformationAvailable property.
     * 
     */
    public boolean isPaymentInformationAvailable() {
        return paymentInformationAvailable;
    }

    /**
     * Sets the value of the paymentInformationAvailable property.
     * 
     */
    public void setPaymentInformationAvailable(boolean value) {
        this.paymentInformationAvailable = value;
    }

    /**
     * Gets the value of the processorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessorName() {
        return processorName;
    }

    /**
     * Sets the value of the processorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessorName(String value) {
        this.processorName = value;
    }

    /**
     * Gets the value of the setupUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetupUrl() {
        return setupUrl;
    }

    /**
     * Sets the value of the setupUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetupUrl(String value) {
        this.setupUrl = value;
    }

    /**
     * Gets the value of the statusString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusString() {
        return statusString;
    }

    /**
     * Sets the value of the statusString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusString(String value) {
        this.statusString = value;
    }

}
