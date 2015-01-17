
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemCostingMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemCostingMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_average"/>
 *     &lt;enumeration value="_fifo"/>
 *     &lt;enumeration value="_lifo"/>
 *     &lt;enumeration value="_lotNumbered"/>
 *     &lt;enumeration value="_serialized"/>
 *     &lt;enumeration value="_standard"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemCostingMethod", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum ItemCostingMethod {

    @XmlEnumValue("_average")
    AVERAGE("_average"),
    @XmlEnumValue("_fifo")
    FIFO("_fifo"),
    @XmlEnumValue("_lifo")
    LIFO("_lifo"),
    @XmlEnumValue("_lotNumbered")
    LOT_NUMBERED("_lotNumbered"),
    @XmlEnumValue("_serialized")
    SERIALIZED("_serialized"),
    @XmlEnumValue("_standard")
    STANDARD("_standard");
    private final String value;

    ItemCostingMethod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemCostingMethod fromValue(String v) {
        for (ItemCostingMethod c: ItemCostingMethod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
