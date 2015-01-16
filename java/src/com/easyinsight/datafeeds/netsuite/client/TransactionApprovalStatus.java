
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionApprovalStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransactionApprovalStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_approved"/>
 *     &lt;enumeration value="_pendingApproval"/>
 *     &lt;enumeration value="_rejected"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TransactionApprovalStatus", namespace = "urn:types.sales_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum TransactionApprovalStatus {

    @XmlEnumValue("_approved")
    APPROVED("_approved"),
    @XmlEnumValue("_pendingApproval")
    PENDING_APPROVAL("_pendingApproval"),
    @XmlEnumValue("_rejected")
    REJECTED("_rejected");
    private final String value;

    TransactionApprovalStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TransactionApprovalStatus fromValue(String v) {
        for (TransactionApprovalStatus c: TransactionApprovalStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
