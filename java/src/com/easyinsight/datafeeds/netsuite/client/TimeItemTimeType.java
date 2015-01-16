
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeItemTimeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TimeItemTimeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_actualTime"/>
 *     &lt;enumeration value="_plannedTime"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TimeItemTimeType", namespace = "urn:types.common_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum TimeItemTimeType {

    @XmlEnumValue("_actualTime")
    ACTUAL_TIME("_actualTime"),
    @XmlEnumValue("_plannedTime")
    PLANNED_TIME("_plannedTime");
    private final String value;

    TimeItemTimeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimeItemTimeType fromValue(String v) {
        for (TimeItemTimeType c: TimeItemTimeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
