
package com.easyinsight.rowutil.v2web;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for where complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="where">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateWheres" type="{http://v2.api.easyinsight.com/}dateWhere" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dayWheres" type="{http://v2.api.easyinsight.com/}dayWhere" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numberWheres" type="{http://v2.api.easyinsight.com/}numberWhere" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="stringWheres" type="{http://v2.api.easyinsight.com/}stringWhere" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "where", propOrder = {
    "dateWheres",
    "dayWheres",
    "numberWheres",
    "stringWheres"
})
public class Where {

    @XmlElement(nillable = true)
    protected List<DateWhere> dateWheres;
    @XmlElement(nillable = true)
    protected List<DayWhere> dayWheres;
    @XmlElement(nillable = true)
    protected List<NumberWhere> numberWheres;
    @XmlElement(nillable = true)
    protected List<StringWhere> stringWheres;

    /**
     * Gets the value of the dateWheres property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateWheres property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateWheres().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DateWhere }
     * 
     * 
     */
    public List<DateWhere> getDateWheres() {
        if (dateWheres == null) {
            dateWheres = new ArrayList<DateWhere>();
        }
        return this.dateWheres;
    }

    /**
     * Gets the value of the dayWheres property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dayWheres property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDayWheres().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DayWhere }
     * 
     * 
     */
    public List<DayWhere> getDayWheres() {
        if (dayWheres == null) {
            dayWheres = new ArrayList<DayWhere>();
        }
        return this.dayWheres;
    }

    /**
     * Gets the value of the numberWheres property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the numberWheres property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNumberWheres().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NumberWhere }
     * 
     * 
     */
    public List<NumberWhere> getNumberWheres() {
        if (numberWheres == null) {
            numberWheres = new ArrayList<NumberWhere>();
        }
        return this.numberWheres;
    }

    /**
     * Gets the value of the stringWheres property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stringWheres property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStringWheres().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringWhere }
     * 
     * 
     */
    public List<StringWhere> getStringWheres() {
        if (stringWheres == null) {
            stringWheres = new ArrayList<StringWhere>();
        }
        return this.stringWheres;
    }

}
