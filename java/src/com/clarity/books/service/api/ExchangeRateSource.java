
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for exchangeRateSource.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="exchangeRateSource">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DERIVED"/>
 *     &lt;enumeration value="FIXED_RATE"/>
 *     &lt;enumeration value="MANUAL_ENTRY"/>
 *     &lt;enumeration value="XE_DOT_COM"/>
 *     &lt;enumeration value="WORLDPAY"/>
 *     &lt;enumeration value="BANK_OF_CANADA"/>
 *     &lt;enumeration value="NEW_YORK_FED"/>
 *     &lt;enumeration value="SWISS_NATIONAL_BANK"/>
 *     &lt;enumeration value="EUROPEAN_CENTRAL_BANK"/>
 *     &lt;enumeration value="ROYAL_BANK_OF_AUSTRALIA"/>
 *     &lt;enumeration value="BANCO_DE_MEXICO"/>
 *     &lt;enumeration value="BANGKO_SENTRAL_NG_PHILIPINAS"/>
 *     &lt;enumeration value="IMF"/>
 *     &lt;enumeration value="NATIONAL_BANK_OF_UKRAINE"/>
 *     &lt;enumeration value="CENTRAL_BANK_OF_NIGERIA"/>
 *     &lt;enumeration value="NATIONAL_BANK_OF_RWANDA"/>
 *     &lt;enumeration value="CENTRAL_BANK_OF_KENYA"/>
 *     &lt;enumeration value="BANK_OF_UGANDA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "exchangeRateSource")
@XmlEnum
public enum ExchangeRateSource {

    DERIVED,
    FIXED_RATE,
    MANUAL_ENTRY,
    XE_DOT_COM,
    WORLDPAY,
    BANK_OF_CANADA,
    NEW_YORK_FED,
    SWISS_NATIONAL_BANK,
    EUROPEAN_CENTRAL_BANK,
    ROYAL_BANK_OF_AUSTRALIA,
    BANCO_DE_MEXICO,
    BANGKO_SENTRAL_NG_PHILIPINAS,
    IMF,
    NATIONAL_BANK_OF_UKRAINE,
    CENTRAL_BANK_OF_NIGERIA,
    NATIONAL_BANK_OF_RWANDA,
    CENTRAL_BANK_OF_KENYA,
    BANK_OF_UGANDA;

    public String value() {
        return name();
    }

    public static ExchangeRateSource fromValue(String v) {
        return valueOf(v);
    }

}
