
package com.easyinsight.rowutil.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for row complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="row">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datePairs" type="{http://basicauth.api.easyinsight.com/}datePair" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numberPairs" type="{http://basicauth.api.easyinsight.com/}numberPair" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="stringPairs" type="{http://basicauth.api.easyinsight.com/}stringPair" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "row", propOrder = {
    "datePairs",
    "numberPairs",
    "stringPairs"
})
public class Row {

    @XmlElement(nillable = true)
    protected List<DatePair> datePairs;
    @XmlElement(nillable = true)
    protected List<NumberPair> numberPairs;
    @XmlElement(nillable = true)
    protected List<StringPair> stringPairs;

    /**
     * Gets the value of the datePairs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datePairs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatePairs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatePair }
     * 
     * 
     */
    public List<DatePair> getDatePairs() {
        if (datePairs == null) {
            datePairs = new ArrayList<DatePair>();
        }
        return this.datePairs;
    }

    /**
     * Gets the value of the numberPairs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the numberPairs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNumberPairs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NumberPair }
     * 
     * 
     */
    public List<NumberPair> getNumberPairs() {
        if (numberPairs == null) {
            numberPairs = new ArrayList<NumberPair>();
        }
        return this.numberPairs;
    }

    /**
     * Gets the value of the stringPairs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stringPairs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStringPairs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPair }
     * 
     * 
     */
    public List<StringPair> getStringPairs() {
        if (stringPairs == null) {
            stringPairs = new ArrayList<StringPair>();
        }
        return this.stringPairs;
    }

}
