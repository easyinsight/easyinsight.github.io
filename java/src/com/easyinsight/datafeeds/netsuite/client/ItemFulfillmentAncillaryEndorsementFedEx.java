
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemFulfillmentAncillaryEndorsementFedEx.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemFulfillmentAncillaryEndorsementFedEx">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_addressCorrection"/>
 *     &lt;enumeration value="_carrierLeaveIfNoResponse"/>
 *     &lt;enumeration value="_changeService"/>
 *     &lt;enumeration value="_forwardingService"/>
 *     &lt;enumeration value="_returnService"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemFulfillmentAncillaryEndorsementFedEx", namespace = "urn:types.sales_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum ItemFulfillmentAncillaryEndorsementFedEx {

    @XmlEnumValue("_addressCorrection")
    ADDRESS_CORRECTION("_addressCorrection"),
    @XmlEnumValue("_carrierLeaveIfNoResponse")
    CARRIER_LEAVE_IF_NO_RESPONSE("_carrierLeaveIfNoResponse"),
    @XmlEnumValue("_changeService")
    CHANGE_SERVICE("_changeService"),
    @XmlEnumValue("_forwardingService")
    FORWARDING_SERVICE("_forwardingService"),
    @XmlEnumValue("_returnService")
    RETURN_SERVICE("_returnService");
    private final String value;

    ItemFulfillmentAncillaryEndorsementFedEx(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemFulfillmentAncillaryEndorsementFedEx fromValue(String v) {
        for (ItemFulfillmentAncillaryEndorsementFedEx c: ItemFulfillmentAncillaryEndorsementFedEx.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
