
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for measureAggregationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="measureAggregationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUM"/>
 *     &lt;enumeration value="COUNT"/>
 *     &lt;enumeration value="AVERAGE"/>
 *     &lt;enumeration value="MIN"/>
 *     &lt;enumeration value="MAX"/>
 *     &lt;enumeration value="RANK"/>
 *     &lt;enumeration value="MEDIAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "measureAggregationType")
@XmlEnum
public enum MeasureAggregationType {

    SUM,
    COUNT,
    AVERAGE,
    MIN,
    MAX,
    RANK,
    MEDIAN;

    public String value() {
        return name();
    }

    public static MeasureAggregationType fromValue(String v) {
        return valueOf(v);
    }

}
