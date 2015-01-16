
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemDemandSource.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemDemandSource">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_enteredAndPlannedOrders"/>
 *     &lt;enumeration value="_forecastAndOrders"/>
 *     &lt;enumeration value="_forecastConsumption"/>
 *     &lt;enumeration value="_forecastFromDemandPlan"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemDemandSource", namespace = "urn:types.accounting_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum ItemDemandSource {

    @XmlEnumValue("_enteredAndPlannedOrders")
    ENTERED_AND_PLANNED_ORDERS("_enteredAndPlannedOrders"),
    @XmlEnumValue("_forecastAndOrders")
    FORECAST_AND_ORDERS("_forecastAndOrders"),
    @XmlEnumValue("_forecastConsumption")
    FORECAST_CONSUMPTION("_forecastConsumption"),
    @XmlEnumValue("_forecastFromDemandPlan")
    FORECAST_FROM_DEMAND_PLAN("_forecastFromDemandPlan");
    private final String value;

    ItemDemandSource(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemDemandSource fromValue(String v) {
        for (ItemDemandSource c: ItemDemandSource.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
