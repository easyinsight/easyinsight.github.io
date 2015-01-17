
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillingScheduleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BillingScheduleType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_chargeBased"/>
 *     &lt;enumeration value="_fixedBidInterval"/>
 *     &lt;enumeration value="_fixedBidMilestone"/>
 *     &lt;enumeration value="_standard"/>
 *     &lt;enumeration value="_timeAndMaterials"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BillingScheduleType", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum BillingScheduleType {

    @XmlEnumValue("_chargeBased")
    CHARGE_BASED("_chargeBased"),
    @XmlEnumValue("_fixedBidInterval")
    FIXED_BID_INTERVAL("_fixedBidInterval"),
    @XmlEnumValue("_fixedBidMilestone")
    FIXED_BID_MILESTONE("_fixedBidMilestone"),
    @XmlEnumValue("_standard")
    STANDARD("_standard"),
    @XmlEnumValue("_timeAndMaterials")
    TIME_AND_MATERIALS("_timeAndMaterials");
    private final String value;

    BillingScheduleType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BillingScheduleType fromValue(String v) {
        for (BillingScheduleType c: BillingScheduleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
