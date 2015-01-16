
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomRecordTypeAccessType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CustomRecordTypeAccessType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_noPermissionRequired"/>
 *     &lt;enumeration value="_requireCustomRecordEntriesPermission"/>
 *     &lt;enumeration value="_usePermissionList"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CustomRecordTypeAccessType", namespace = "urn:types.customization_2014_1.setup.webservices.netsuite.com")
@XmlEnum
public enum CustomRecordTypeAccessType {

    @XmlEnumValue("_noPermissionRequired")
    NO_PERMISSION_REQUIRED("_noPermissionRequired"),
    @XmlEnumValue("_requireCustomRecordEntriesPermission")
    REQUIRE_CUSTOM_RECORD_ENTRIES_PERMISSION("_requireCustomRecordEntriesPermission"),
    @XmlEnumValue("_usePermissionList")
    USE_PERMISSION_LIST("_usePermissionList");
    private final String value;

    CustomRecordTypeAccessType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CustomRecordTypeAccessType fromValue(String v) {
        for (CustomRecordTypeAccessType c: CustomRecordTypeAccessType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
