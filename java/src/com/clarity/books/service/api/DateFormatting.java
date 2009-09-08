
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dateFormatting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="dateFormatting">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MMM_DD_YYYY"/>
 *     &lt;enumeration value="DD_MMM_YYYY"/>
 *     &lt;enumeration value="DD_MM_YYYY"/>
 *     &lt;enumeration value="MM_DD_YYYY"/>
 *     &lt;enumeration value="DOTS_DD_MM_YYYY"/>
 *     &lt;enumeration value="ISO"/>
 *     &lt;enumeration value="DOTS_YYYY_MM_DD"/>
 *     &lt;enumeration value="SLASHES_YYYY_MM_DD"/>
 *     &lt;enumeration value="CHINESE"/>
 *     &lt;enumeration value="KOREAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "dateFormatting")
@XmlEnum
public enum DateFormatting {

    MMM_DD_YYYY,
    DD_MMM_YYYY,
    DD_MM_YYYY,
    MM_DD_YYYY,
    DOTS_DD_MM_YYYY,
    ISO,
    DOTS_YYYY_MM_DD,
    SLASHES_YYYY_MM_DD,
    CHINESE,
    KOREAN;

    public String value() {
        return name();
    }

    public static DateFormatting fromValue(String v) {
        return valueOf(v);
    }

}
