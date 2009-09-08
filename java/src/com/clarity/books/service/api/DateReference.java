
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dateReference.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="dateReference">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="START_DATE"/>
 *     &lt;enumeration value="TRIAL_END"/>
 *     &lt;enumeration value="EXPIRY_DATE"/>
 *     &lt;enumeration value="MONTHLY_INVOICE"/>
 *     &lt;enumeration value="ANNUAL_INVOICE"/>
 *     &lt;enumeration value="PAYMENT_FAILED"/>
 *     &lt;enumeration value="PAYMENT_SUCCESSFUL"/>
 *     &lt;enumeration value="INVOICE"/>
 *     &lt;enumeration value="INVOICE_DUE_DATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "dateReference")
@XmlEnum
public enum DateReference {

    START_DATE,
    TRIAL_END,
    EXPIRY_DATE,
    MONTHLY_INVOICE,
    ANNUAL_INVOICE,
    PAYMENT_FAILED,
    PAYMENT_SUCCESSFUL,
    INVOICE,
    INVOICE_DUE_DATE;

    public String value() {
        return name();
    }

    public static DateReference fromValue(String v) {
        return valueOf(v);
    }

}
