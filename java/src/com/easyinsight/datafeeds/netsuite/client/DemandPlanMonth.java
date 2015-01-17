
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DemandPlanMonth.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DemandPlanMonth">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_april"/>
 *     &lt;enumeration value="_august"/>
 *     &lt;enumeration value="_december"/>
 *     &lt;enumeration value="_february"/>
 *     &lt;enumeration value="_january"/>
 *     &lt;enumeration value="_july"/>
 *     &lt;enumeration value="_june"/>
 *     &lt;enumeration value="_march"/>
 *     &lt;enumeration value="_may"/>
 *     &lt;enumeration value="_november"/>
 *     &lt;enumeration value="_october"/>
 *     &lt;enumeration value="_september"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DemandPlanMonth", namespace = "urn:types.demandplanning_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum DemandPlanMonth {

    @XmlEnumValue("_april")
    APRIL("_april"),
    @XmlEnumValue("_august")
    AUGUST("_august"),
    @XmlEnumValue("_december")
    DECEMBER("_december"),
    @XmlEnumValue("_february")
    FEBRUARY("_february"),
    @XmlEnumValue("_january")
    JANUARY("_january"),
    @XmlEnumValue("_july")
    JULY("_july"),
    @XmlEnumValue("_june")
    JUNE("_june"),
    @XmlEnumValue("_march")
    MARCH("_march"),
    @XmlEnumValue("_may")
    MAY("_may"),
    @XmlEnumValue("_november")
    NOVEMBER("_november"),
    @XmlEnumValue("_october")
    OCTOBER("_october"),
    @XmlEnumValue("_september")
    SEPTEMBER("_september");
    private final String value;

    DemandPlanMonth(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DemandPlanMonth fromValue(String v) {
        for (DemandPlanMonth c: DemandPlanMonth.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
