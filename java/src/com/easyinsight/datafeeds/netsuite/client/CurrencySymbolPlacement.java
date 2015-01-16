
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrencySymbolPlacement.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CurrencySymbolPlacement">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_afterNumber"/>
 *     &lt;enumeration value="_beforeNumber"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CurrencySymbolPlacement", namespace = "urn:types.common_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum CurrencySymbolPlacement {

    @XmlEnumValue("_afterNumber")
    AFTER_NUMBER("_afterNumber"),
    @XmlEnumValue("_beforeNumber")
    BEFORE_NUMBER("_beforeNumber");
    private final String value;

    CurrencySymbolPlacement(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CurrencySymbolPlacement fromValue(String v) {
        for (CurrencySymbolPlacement c: CurrencySymbolPlacement.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
