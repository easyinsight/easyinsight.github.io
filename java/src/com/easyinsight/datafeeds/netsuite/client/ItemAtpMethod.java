
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemAtpMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemAtpMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_cumulativeAtpWithLookAhead"/>
 *     &lt;enumeration value="_discreteAtp"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemAtpMethod", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum ItemAtpMethod {

    @XmlEnumValue("_cumulativeAtpWithLookAhead")
    CUMULATIVE_ATP_WITH_LOOK_AHEAD("_cumulativeAtpWithLookAhead"),
    @XmlEnumValue("_discreteAtp")
    DISCRETE_ATP("_discreteAtp");
    private final String value;

    ItemAtpMethod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemAtpMethod fromValue(String v) {
        for (ItemAtpMethod c: ItemAtpMethod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
