
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemFulfillmentHazmatTypeFedEx.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemFulfillmentHazmatTypeFedEx">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_dangerousGoods"/>
 *     &lt;enumeration value="_hazmat"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemFulfillmentHazmatTypeFedEx", namespace = "urn:types.sales_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum ItemFulfillmentHazmatTypeFedEx {

    @XmlEnumValue("_dangerousGoods")
    DANGEROUS_GOODS("_dangerousGoods"),
    @XmlEnumValue("_hazmat")
    HAZMAT("_hazmat");
    private final String value;

    ItemFulfillmentHazmatTypeFedEx(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemFulfillmentHazmatTypeFedEx fromValue(String v) {
        for (ItemFulfillmentHazmatTypeFedEx c: ItemFulfillmentHazmatTypeFedEx.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
