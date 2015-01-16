
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DemandPlanCalendarType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DemandPlanCalendarType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_daily"/>
 *     &lt;enumeration value="_monthly"/>
 *     &lt;enumeration value="_weekly"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DemandPlanCalendarType", namespace = "urn:types.demandplanning_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum DemandPlanCalendarType {

    @XmlEnumValue("_daily")
    DAILY("_daily"),
    @XmlEnumValue("_monthly")
    MONTHLY("_monthly"),
    @XmlEnumValue("_weekly")
    WEEKLY("_weekly");
    private final String value;

    DemandPlanCalendarType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DemandPlanCalendarType fromValue(String v) {
        for (DemandPlanCalendarType c: DemandPlanCalendarType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
