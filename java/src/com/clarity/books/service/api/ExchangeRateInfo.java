
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for exchangeRateInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exchangeRateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="asOf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="baseCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateEntered" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="derived" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="exchangeRate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="source" type="{http://api.service.books/}exchangeRateSource" minOccurs="0"/>
 *         &lt;element name="sourceComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exchangeRateInfo", propOrder = {
    "asOf",
    "baseCurrencyCode",
    "currencyCode",
    "dateEntered",
    "derived",
    "exchangeRate",
    "source",
    "sourceComment"
})
public class ExchangeRateInfo {

    protected String asOf;
    protected String baseCurrencyCode;
    protected String currencyCode;
    protected XMLGregorianCalendar dateEntered;
    protected boolean derived;
    protected double exchangeRate;
    protected ExchangeRateSource source;
    protected String sourceComment;

    /**
     * Gets the value of the asOf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsOf() {
        return asOf;
    }

    /**
     * Sets the value of the asOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsOf(String value) {
        this.asOf = value;
    }

    /**
     * Gets the value of the baseCurrencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    /**
     * Sets the value of the baseCurrencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseCurrencyCode(String value) {
        this.baseCurrencyCode = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the dateEntered property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateEntered() {
        return dateEntered;
    }

    /**
     * Sets the value of the dateEntered property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateEntered(XMLGregorianCalendar value) {
        this.dateEntered = value;
    }

    /**
     * Gets the value of the derived property.
     * 
     */
    public boolean isDerived() {
        return derived;
    }

    /**
     * Sets the value of the derived property.
     * 
     */
    public void setDerived(boolean value) {
        this.derived = value;
    }

    /**
     * Gets the value of the exchangeRate property.
     * 
     */
    public double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * Sets the value of the exchangeRate property.
     * 
     */
    public void setExchangeRate(double value) {
        this.exchangeRate = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link ExchangeRateSource }
     *     
     */
    public ExchangeRateSource getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExchangeRateSource }
     *     
     */
    public void setSource(ExchangeRateSource value) {
        this.source = value;
    }

    /**
     * Gets the value of the sourceComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceComment() {
        return sourceComment;
    }

    /**
     * Sets the value of the sourceComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceComment(String value) {
        this.sourceComment = value;
    }

}
