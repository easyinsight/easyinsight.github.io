
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingScheduleMonthDow.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BillingScheduleMonthDow">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_sunday"/>
 *     &lt;enumeration value="_monday"/>
 *     &lt;enumeration value="_tuesday"/>
 *     &lt;enumeration value="_wednesday"/>
 *     &lt;enumeration value="_thursday"/>
 *     &lt;enumeration value="_friday"/>
 *     &lt;enumeration value="_saturday"/>
 *     &lt;enumeration value="_day"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BillingScheduleMonthDow", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum BillingScheduleMonthDow {

    @XmlEnumValue("_sunday")
    SUNDAY("_sunday"),
    @XmlEnumValue("_monday")
    MONDAY("_monday"),
    @XmlEnumValue("_tuesday")
    TUESDAY("_tuesday"),
    @XmlEnumValue("_wednesday")
    WEDNESDAY("_wednesday"),
    @XmlEnumValue("_thursday")
    THURSDAY("_thursday"),
    @XmlEnumValue("_friday")
    FRIDAY("_friday"),
    @XmlEnumValue("_saturday")
    SATURDAY("_saturday"),
    @XmlEnumValue("_day")
    DAY("_day");
    private final String value;

    BillingScheduleMonthDow(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BillingScheduleMonthDow fromValue(String v) {
        for (BillingScheduleMonthDow c: BillingScheduleMonthDow.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
