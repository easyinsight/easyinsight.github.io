
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LandedCostMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LandedCostMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_quantity"/>
 *     &lt;enumeration value="_value"/>
 *     &lt;enumeration value="_weight"/>
 *     &lt;enumeration value="_line"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LandedCostMethod", namespace = "urn:types.common_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum LandedCostMethod {

    @XmlEnumValue("_quantity")
    QUANTITY("_quantity"),
    @XmlEnumValue("_value")
    VALUE("_value"),
    @XmlEnumValue("_weight")
    WEIGHT("_weight"),
    @XmlEnumValue("_line")
    LINE("_line");
    private final String value;

    LandedCostMethod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LandedCostMethod fromValue(String v) {
        for (LandedCostMethod c: LandedCostMethod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
