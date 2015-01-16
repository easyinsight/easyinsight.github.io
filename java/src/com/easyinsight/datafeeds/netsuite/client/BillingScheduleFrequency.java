
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingScheduleFrequency.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BillingScheduleFrequency">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_annually"/>
 *     &lt;enumeration value="_custom"/>
 *     &lt;enumeration value="_daily"/>
 *     &lt;enumeration value="_endOfPeriod"/>
 *     &lt;enumeration value="_monthly"/>
 *     &lt;enumeration value="_never"/>
 *     &lt;enumeration value="_oneTime"/>
 *     &lt;enumeration value="_startOfPeriod"/>
 *     &lt;enumeration value="_weekly"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BillingScheduleFrequency", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum BillingScheduleFrequency {

    @XmlEnumValue("_annually")
    ANNUALLY("_annually"),
    @XmlEnumValue("_custom")
    CUSTOM("_custom"),
    @XmlEnumValue("_daily")
    DAILY("_daily"),
    @XmlEnumValue("_endOfPeriod")
    END_OF_PERIOD("_endOfPeriod"),
    @XmlEnumValue("_monthly")
    MONTHLY("_monthly"),
    @XmlEnumValue("_never")
    NEVER("_never"),
    @XmlEnumValue("_oneTime")
    ONE_TIME("_oneTime"),
    @XmlEnumValue("_startOfPeriod")
    START_OF_PERIOD("_startOfPeriod"),
    @XmlEnumValue("_weekly")
    WEEKLY("_weekly");
    private final String value;

    BillingScheduleFrequency(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BillingScheduleFrequency fromValue(String v) {
        for (BillingScheduleFrequency c: BillingScheduleFrequency.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
