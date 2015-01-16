
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VendorReturnAuthorizationOrderStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VendorReturnAuthorizationOrderStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_cancelled"/>
 *     &lt;enumeration value="_closed"/>
 *     &lt;enumeration value="_credited"/>
 *     &lt;enumeration value="_partiallyReturned"/>
 *     &lt;enumeration value="_pendingApproval"/>
 *     &lt;enumeration value="_pendingCredit"/>
 *     &lt;enumeration value="_pendingCreditPartiallyReturned"/>
 *     &lt;enumeration value="_pendingReturn"/>
 *     &lt;enumeration value="_undefined"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VendorReturnAuthorizationOrderStatus", namespace = "urn:types.purchases_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum VendorReturnAuthorizationOrderStatus {

    @XmlEnumValue("_cancelled")
    CANCELLED("_cancelled"),
    @XmlEnumValue("_closed")
    CLOSED("_closed"),
    @XmlEnumValue("_credited")
    CREDITED("_credited"),
    @XmlEnumValue("_partiallyReturned")
    PARTIALLY_RETURNED("_partiallyReturned"),
    @XmlEnumValue("_pendingApproval")
    PENDING_APPROVAL("_pendingApproval"),
    @XmlEnumValue("_pendingCredit")
    PENDING_CREDIT("_pendingCredit"),
    @XmlEnumValue("_pendingCreditPartiallyReturned")
    PENDING_CREDIT_PARTIALLY_RETURNED("_pendingCreditPartiallyReturned"),
    @XmlEnumValue("_pendingReturn")
    PENDING_RETURN("_pendingReturn"),
    @XmlEnumValue("_undefined")
    UNDEFINED("_undefined");
    private final String value;

    VendorReturnAuthorizationOrderStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VendorReturnAuthorizationOrderStatus fromValue(String v) {
        for (VendorReturnAuthorizationOrderStatus c: VendorReturnAuthorizationOrderStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
