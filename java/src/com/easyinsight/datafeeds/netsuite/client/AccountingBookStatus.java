
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountingBookStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AccountingBookStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_active"/>
 *     &lt;enumeration value="_inactive"/>
 *     &lt;enumeration value="_pending"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AccountingBookStatus", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum AccountingBookStatus {

    @XmlEnumValue("_active")
    ACTIVE("_active"),
    @XmlEnumValue("_inactive")
    INACTIVE("_inactive"),
    @XmlEnumValue("_pending")
    PENDING("_pending");
    private final String value;

    AccountingBookStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccountingBookStatus fromValue(String v) {
        for (AccountingBookStatus c: AccountingBookStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
