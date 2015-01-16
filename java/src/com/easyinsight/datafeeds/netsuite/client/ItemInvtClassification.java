
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemInvtClassification.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemInvtClassification">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_a"/>
 *     &lt;enumeration value="_b"/>
 *     &lt;enumeration value="_c"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemInvtClassification", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum ItemInvtClassification {

    @XmlEnumValue("_a")
    A("_a"),
    @XmlEnumValue("_b")
    B("_b"),
    @XmlEnumValue("_c")
    C("_c");
    private final String value;

    ItemInvtClassification(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemInvtClassification fromValue(String v) {
        for (ItemInvtClassification c: ItemInvtClassification.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
