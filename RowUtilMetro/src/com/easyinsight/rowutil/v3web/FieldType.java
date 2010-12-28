
package com.easyinsight.rowutil.v3web;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fieldType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fieldType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GROUPING"/>
 *     &lt;enumeration value="MEASURE"/>
 *     &lt;enumeration value="DATE"/>
 *     &lt;enumeration value="POSTAL_CODE"/>
 *     &lt;enumeration value="LONGITUDE"/>
 *     &lt;enumeration value="LATITUDE"/>
 *     &lt;enumeration value="TAGS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fieldType")
@XmlEnum
public enum FieldType {

    GROUPING,
    MEASURE,
    DATE,
    POSTAL_CODE,
    LONGITUDE,
    LATITUDE,
    TAGS;

    public String value() {
        return name();
    }

    public static FieldType fromValue(String v) {
        return valueOf(v);
    }

}
