
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contactType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="contactType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CUSTOMER"/>
 *     &lt;enumeration value="VENDOR"/>
 *     &lt;enumeration value="PARTNER"/>
 *     &lt;enumeration value="OWNER"/>
 *     &lt;enumeration value="GOVERNMENT"/>
 *     &lt;enumeration value="OTHER"/>
 *     &lt;enumeration value="EMPLOYEE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "contactType")
@XmlEnum
public enum ContactType {

    CUSTOMER,
    VENDOR,
    PARTNER,
    OWNER,
    GOVERNMENT,
    OTHER,
    EMPLOYEE;

    public String value() {
        return name();
    }

    public static ContactType fromValue(String v) {
        return valueOf(v);
    }

}
