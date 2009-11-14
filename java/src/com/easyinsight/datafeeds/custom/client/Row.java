
package com.easyinsight.datafeeds.custom.client;

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
 *         &lt;element name="dateValues" type="{http://sampleimpl.easyinsight.com/}dateValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numberValues" type="{http://sampleimpl.easyinsight.com/}numberValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="stringValues" type="{http://sampleimpl.easyinsight.com/}stringValue" maxOccurs="unbounded" minOccurs="0"/>
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
    "dateValues",
    "numberValues",
    "stringValues"
})
public class Row {

    @XmlElement(nillable = true)
    protected List<DateValue> dateValues;
    @XmlElement(nillable = true)
    protected List<NumberValue> numberValues;
    @XmlElement(nillable = true)
    protected List<StringValue> stringValues;

    /**
     * Gets the value of the dateValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DateValue }
     * 
     * 
     */
    public List<DateValue> getDateValues() {
        if (dateValues == null) {
            dateValues = new ArrayList<DateValue>();
        }
        return this.dateValues;
    }

    /**
     * Gets the value of the numberValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the numberValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNumberValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NumberValue }
     * 
     * 
     */
    public List<NumberValue> getNumberValues() {
        if (numberValues == null) {
            numberValues = new ArrayList<NumberValue>();
        }
        return this.numberValues;
    }

    /**
     * Gets the value of the stringValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stringValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStringValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringValue }
     * 
     * 
     */
    public List<StringValue> getStringValues() {
        if (stringValues == null) {
            stringValues = new ArrayList<StringValue>();
        }
        return this.stringValues;
    }

}
