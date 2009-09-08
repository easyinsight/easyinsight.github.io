
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="paymentMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CASH"/>
 *     &lt;enumeration value="CHEQUE"/>
 *     &lt;enumeration value="CREDIT_CARD"/>
 *     &lt;enumeration value="DEBIT_CARD"/>
 *     &lt;enumeration value="WIRE_TRANSFER"/>
 *     &lt;enumeration value="EFT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "paymentMethod")
@XmlEnum
public enum PaymentMethod {

    CASH,
    CHEQUE,
    CREDIT_CARD,
    DEBIT_CARD,
    WIRE_TRANSFER,
    EFT;

    public String value() {
        return name();
    }

    public static PaymentMethod fromValue(String v) {
        return valueOf(v);
    }

}
