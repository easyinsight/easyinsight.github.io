
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemCostAccountingStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemCostAccountingStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_complete"/>
 *     &lt;enumeration value="_failed"/>
 *     &lt;enumeration value="_pending"/>
 *     &lt;enumeration value="_processing"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemCostAccountingStatus", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum ItemCostAccountingStatus {

    @XmlEnumValue("_complete")
    COMPLETE("_complete"),
    @XmlEnumValue("_failed")
    FAILED("_failed"),
    @XmlEnumValue("_pending")
    PENDING("_pending"),
    @XmlEnumValue("_processing")
    PROCESSING("_processing");
    private final String value;

    ItemCostAccountingStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemCostAccountingStatus fromValue(String v) {
        for (ItemCostAccountingStatus c: ItemCostAccountingStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
