
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SalesOrderOrderStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SalesOrderOrderStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_pendingApproval"/>
 *     &lt;enumeration value="_pendingFulfillment"/>
 *     &lt;enumeration value="_cancelled"/>
 *     &lt;enumeration value="_partiallyFulfilled"/>
 *     &lt;enumeration value="_pendingBillingPartFulfilled"/>
 *     &lt;enumeration value="_pendingBilling"/>
 *     &lt;enumeration value="_fullyBilled"/>
 *     &lt;enumeration value="_closed"/>
 *     &lt;enumeration value="_undefined"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SalesOrderOrderStatus", namespace = "urn:types.sales_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum SalesOrderOrderStatus {

    @XmlEnumValue("_pendingApproval")
    PENDING_APPROVAL("_pendingApproval"),
    @XmlEnumValue("_pendingFulfillment")
    PENDING_FULFILLMENT("_pendingFulfillment"),
    @XmlEnumValue("_cancelled")
    CANCELLED("_cancelled"),
    @XmlEnumValue("_partiallyFulfilled")
    PARTIALLY_FULFILLED("_partiallyFulfilled"),
    @XmlEnumValue("_pendingBillingPartFulfilled")
    PENDING_BILLING_PART_FULFILLED("_pendingBillingPartFulfilled"),
    @XmlEnumValue("_pendingBilling")
    PENDING_BILLING("_pendingBilling"),
    @XmlEnumValue("_fullyBilled")
    FULLY_BILLED("_fullyBilled"),
    @XmlEnumValue("_closed")
    CLOSED("_closed"),
    @XmlEnumValue("_undefined")
    UNDEFINED("_undefined");
    private final String value;

    SalesOrderOrderStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SalesOrderOrderStatus fromValue(String v) {
        for (SalesOrderOrderStatus c: SalesOrderOrderStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
