
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RecurrenceDowim.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RecurrenceDowim">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_first"/>
 *     &lt;enumeration value="_second"/>
 *     &lt;enumeration value="_third"/>
 *     &lt;enumeration value="_fourth"/>
 *     &lt;enumeration value="_last"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RecurrenceDowim", namespace = "urn:types.common_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum RecurrenceDowim {

    @XmlEnumValue("_first")
    FIRST("_first"),
    @XmlEnumValue("_second")
    SECOND("_second"),
    @XmlEnumValue("_third")
    THIRD("_third"),
    @XmlEnumValue("_fourth")
    FOURTH("_fourth"),
    @XmlEnumValue("_last")
    LAST("_last");
    private final String value;

    RecurrenceDowim(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RecurrenceDowim fromValue(String v) {
        for (RecurrenceDowim c: RecurrenceDowim.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
