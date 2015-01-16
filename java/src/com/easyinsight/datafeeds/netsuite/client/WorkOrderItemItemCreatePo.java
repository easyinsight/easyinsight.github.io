
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WorkOrderItemItemCreatePo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="WorkOrderItemItemCreatePo">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_dropShipment"/>
 *     &lt;enumeration value="_specialOrder"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "WorkOrderItemItemCreatePo", namespace = "urn:types.inventory_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum WorkOrderItemItemCreatePo {

    @XmlEnumValue("_dropShipment")
    DROP_SHIPMENT("_dropShipment"),
    @XmlEnumValue("_specialOrder")
    SPECIAL_ORDER("_specialOrder");
    private final String value;

    WorkOrderItemItemCreatePo(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WorkOrderItemItemCreatePo fromValue(String v) {
        for (WorkOrderItemItemCreatePo c: WorkOrderItemItemCreatePo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
