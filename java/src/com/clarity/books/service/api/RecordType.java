
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="recordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INVOICE"/>
 *     &lt;enumeration value="TRANSFER"/>
 *     &lt;enumeration value="ADJUSTMENT"/>
 *     &lt;enumeration value="BILL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "recordType")
@XmlEnum
public enum RecordType {

    INVOICE,
    TRANSFER,
    ADJUSTMENT,
    BILL;

    public String value() {
        return name();
    }

    public static RecordType fromValue(String v) {
        return valueOf(v);
    }

}
