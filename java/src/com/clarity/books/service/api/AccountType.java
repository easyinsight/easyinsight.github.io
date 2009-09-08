
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accountType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="accountType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACCOUNTS_RECEIVABLE"/>
 *     &lt;enumeration value="ACCOUNTS_PAYABLE"/>
 *     &lt;enumeration value="BANK"/>
 *     &lt;enumeration value="COST_OF_GOODS_SOLD"/>
 *     &lt;enumeration value="CREDIT_CARD"/>
 *     &lt;enumeration value="EQUITY"/>
 *     &lt;enumeration value="EXPENSE"/>
 *     &lt;enumeration value="FIXED_ASSET"/>
 *     &lt;enumeration value="INCOME"/>
 *     &lt;enumeration value="GAIN_OR_LOSS_ON_EXCHANGE"/>
 *     &lt;enumeration value="LONG_TERM_LIABILITY"/>
 *     &lt;enumeration value="OTHER_CURRENT_ASSET"/>
 *     &lt;enumeration value="OTHER_CURRENT_LIABILITY"/>
 *     &lt;enumeration value="OTHER_ASSET"/>
 *     &lt;enumeration value="TAXES"/>
 *     &lt;enumeration value="CASH"/>
 *     &lt;enumeration value="RETAINED_EARNINGS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "accountType")
@XmlEnum
public enum AccountType {

    ACCOUNTS_RECEIVABLE,
    ACCOUNTS_PAYABLE,
    BANK,
    COST_OF_GOODS_SOLD,
    CREDIT_CARD,
    EQUITY,
    EXPENSE,
    FIXED_ASSET,
    INCOME,
    GAIN_OR_LOSS_ON_EXCHANGE,
    LONG_TERM_LIABILITY,
    OTHER_CURRENT_ASSET,
    OTHER_CURRENT_LIABILITY,
    OTHER_ASSET,
    TAXES,
    CASH,
    RETAINED_EARNINGS;

    public String value() {
        return name();
    }

    public static AccountType fromValue(String v) {
        return valueOf(v);
    }

}
