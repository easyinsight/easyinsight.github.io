
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for measureFormattingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="measureFormattingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CURRENCY"/>
 *     &lt;enumeration value="INTERVAL_MILLISECONDS"/>
 *     &lt;enumeration value="INTERVAL_SECONDS"/>
 *     &lt;enumeration value="PERCENTAGE"/>
 *     &lt;enumeration value="BYTES"/>
 *     &lt;enumeration value="NONE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "measureFormattingType")
@XmlEnum
public enum MeasureFormattingType {

    CURRENCY,
    INTERVAL_MILLISECONDS,
    INTERVAL_SECONDS,
    PERCENTAGE,
    BYTES,
    NONE;

    public String value() {
        return name();
    }

    public static MeasureFormattingType fromValue(String v) {
        return valueOf(v);
    }

}
