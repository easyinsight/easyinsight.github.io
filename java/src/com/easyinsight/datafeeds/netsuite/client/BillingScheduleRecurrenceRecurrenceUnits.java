
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingScheduleRecurrenceRecurrenceUnits.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BillingScheduleRecurrenceRecurrenceUnits">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_custom"/>
 *     &lt;enumeration value="_days"/>
 *     &lt;enumeration value="_months"/>
 *     &lt;enumeration value="_weeks"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BillingScheduleRecurrenceRecurrenceUnits", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum BillingScheduleRecurrenceRecurrenceUnits {

    @XmlEnumValue("_custom")
    CUSTOM("_custom"),
    @XmlEnumValue("_days")
    DAYS("_days"),
    @XmlEnumValue("_months")
    MONTHS("_months"),
    @XmlEnumValue("_weeks")
    WEEKS("_weeks");
    private final String value;

    BillingScheduleRecurrenceRecurrenceUnits(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BillingScheduleRecurrenceRecurrenceUnits fromValue(String v) {
        for (BillingScheduleRecurrenceRecurrenceUnits c: BillingScheduleRecurrenceRecurrenceUnits.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
