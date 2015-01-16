
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaxAcctType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TaxAcctType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_sale"/>
 *     &lt;enumeration value="_purchase"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TaxAcctType", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum TaxAcctType {

    @XmlEnumValue("_sale")
    SALE("_sale"),
    @XmlEnumValue("_purchase")
    PURCHASE("_purchase");
    private final String value;

    TaxAcctType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TaxAcctType fromValue(String v) {
        for (TaxAcctType c: TaxAcctType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
