
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RevRecScheduleAmortizationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RevRecScheduleAmortizationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_standard"/>
 *     &lt;enumeration value="_variable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RevRecScheduleAmortizationType", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum RevRecScheduleAmortizationType {

    @XmlEnumValue("_standard")
    STANDARD("_standard"),
    @XmlEnumValue("_variable")
    VARIABLE("_variable");
    private final String value;

    RevRecScheduleAmortizationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RevRecScheduleAmortizationType fromValue(String v) {
        for (RevRecScheduleAmortizationType c: RevRecScheduleAmortizationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
