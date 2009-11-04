
package com.easyinsight.rowutil.webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for comparison.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="comparison">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GREATER_THAN"/>
 *     &lt;enumeration value="EQUAL_TO"/>
 *     &lt;enumeration value="LESS_THAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "comparison")
@XmlEnum
public enum Comparison {

    GREATER_THAN,
    EQUAL_TO,
    LESS_THAN;

    public String value() {
        return name();
    }

    public static Comparison fromValue(String v) {
        return valueOf(v);
    }

}
