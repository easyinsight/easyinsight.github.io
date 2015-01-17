
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeriodDemandPlan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeriodDemandPlan">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="dayOfTheWeek" type="{urn:types.demandplanning_2014_1.transactions.webservices.netsuite.com}DayOfTheWeek" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeriodDemandPlan", namespace = "urn:demandplanning_2014_1.transactions.webservices.netsuite.com", propOrder = {
    "quantity",
    "dayOfTheWeek"
})
public class PeriodDemandPlan {

    protected double quantity;
    protected DayOfTheWeek dayOfTheWeek;

    /**
     * Gets the value of the quantity property.
     * 
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(double value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the dayOfTheWeek property.
     * 
     * @return
     *     possible object is
     *     {@link DayOfTheWeek }
     *     
     */
    public DayOfTheWeek getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    /**
     * Sets the value of the dayOfTheWeek property.
     * 
     * @param value
     *     allowed object is
     *     {@link DayOfTheWeek }
     *     
     */
    public void setDayOfTheWeek(DayOfTheWeek value) {
        this.dayOfTheWeek = value;
    }

}
