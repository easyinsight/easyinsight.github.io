
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingScheduleRecurrenceMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BillingScheduleRecurrenceMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_dom"/>
 *     &lt;enumeration value="_dowim"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BillingScheduleRecurrenceMode", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum BillingScheduleRecurrenceMode {

    @XmlEnumValue("_dom")
    DOM("_dom"),
    @XmlEnumValue("_dowim")
    DOWIM("_dowim");
    private final String value;

    BillingScheduleRecurrenceMode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BillingScheduleRecurrenceMode fromValue(String v) {
        for (BillingScheduleRecurrenceMode c: BillingScheduleRecurrenceMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
