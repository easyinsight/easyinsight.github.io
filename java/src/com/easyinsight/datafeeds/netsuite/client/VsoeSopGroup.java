
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VsoeSopGroup.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VsoeSopGroup">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_exclude"/>
 *     &lt;enumeration value="_normal"/>
 *     &lt;enumeration value="_software"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VsoeSopGroup", namespace = "urn:types.common_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum VsoeSopGroup {

    @XmlEnumValue("_exclude")
    EXCLUDE("_exclude"),
    @XmlEnumValue("_normal")
    NORMAL("_normal"),
    @XmlEnumValue("_software")
    SOFTWARE("_software");
    private final String value;

    VsoeSopGroup(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VsoeSopGroup fromValue(String v) {
        for (VsoeSopGroup c: VsoeSopGroup.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
