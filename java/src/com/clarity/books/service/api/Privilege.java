
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for privilege.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="privilege">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADD"/>
 *     &lt;enumeration value="ADD_ACCOUNTS"/>
 *     &lt;enumeration value="ADD_CONTACTS"/>
 *     &lt;enumeration value="ADD_ITEMS"/>
 *     &lt;enumeration value="ADD_JOURNAL_ENTRIES"/>
 *     &lt;enumeration value="ADD_RECORDS"/>
 *     &lt;enumeration value="ADD_USERS"/>
 *     &lt;enumeration value="CHANGE"/>
 *     &lt;enumeration value="CHANGE_ACCOUNTS"/>
 *     &lt;enumeration value="CHANGE_CONTACT_INFO"/>
 *     &lt;enumeration value="CHANGE_CURRENCY"/>
 *     &lt;enumeration value="CHANGE_YEAR_END"/>
 *     &lt;enumeration value="CHANGE_ITEMS"/>
 *     &lt;enumeration value="CHANGE_JOURNAL_ENTRIES"/>
 *     &lt;enumeration value="CHANGE_NAME"/>
 *     &lt;enumeration value="CHANGE_RECORDS"/>
 *     &lt;enumeration value="CHANGE_REGION"/>
 *     &lt;enumeration value="REMOVE"/>
 *     &lt;enumeration value="REMOVE_ACCOUNTS"/>
 *     &lt;enumeration value="REMOVE_BUSINESS"/>
 *     &lt;enumeration value="REMOVE_ITEMS"/>
 *     &lt;enumeration value="REMOVE_JOURNAL_ENTRIES"/>
 *     &lt;enumeration value="REMOVE_RECORDS"/>
 *     &lt;enumeration value="REMOVE_RELATIONSHIPS"/>
 *     &lt;enumeration value="REMOVE_ROLES"/>
 *     &lt;enumeration value="REMOVE_USERS"/>
 *     &lt;enumeration value="VIEW"/>
 *     &lt;enumeration value="VIEW_ACCOUNTS"/>
 *     &lt;enumeration value="VIEW_RECORDS"/>
 *     &lt;enumeration value="VIEW_ITEMS"/>
 *     &lt;enumeration value="VIEW_JOURNAL_ENTRIES"/>
 *     &lt;enumeration value="VIEW_RELATIONSHIPS"/>
 *     &lt;enumeration value="VIEW_USERS"/>
 *     &lt;enumeration value="CHANGE_CONTACTS"/>
 *     &lt;enumeration value="REMOVE_CONTACTS"/>
 *     &lt;enumeration value="VIEW_CONTACTS"/>
 *     &lt;enumeration value="ADD_TAXES"/>
 *     &lt;enumeration value="REMOVE_TAXES"/>
 *     &lt;enumeration value="CHANGE_TAXES"/>
 *     &lt;enumeration value="VIEW_TAXES"/>
 *     &lt;enumeration value="CHANGE_TAX_CODES"/>
 *     &lt;enumeration value="VIEW_CHANGESETS"/>
 *     &lt;enumeration value="ADD_COMMENTS"/>
 *     &lt;enumeration value="ADD_ATTACHMENTS"/>
 *     &lt;enumeration value="ADD_TAGS"/>
 *     &lt;enumeration value="REMOVE_TAGS"/>
 *     &lt;enumeration value="VIEW_REPORTS"/>
 *     &lt;enumeration value="CHANGE_ROLES"/>
 *     &lt;enumeration value="BANK_RECONCILIATION"/>
 *     &lt;enumeration value="CHANGE_TIME_ZONE"/>
 *     &lt;enumeration value="CHANGE_CONTRACT"/>
 *     &lt;enumeration value="VIEW_ACCOUNT_BALANCES"/>
 *     &lt;enumeration value="VIEW_CONTRACT"/>
 *     &lt;enumeration value="ADD_PAYMENT_PROCESSORS"/>
 *     &lt;enumeration value="VIEW_PAYMENT_PROCESSORS"/>
 *     &lt;enumeration value="CHANGE_PAYMENT_PROCESSORS"/>
 *     &lt;enumeration value="REMOVE_PAYMENT_PROCESSORS"/>
 *     &lt;enumeration value="ADD_PLANS"/>
 *     &lt;enumeration value="CHANGE_PLANS"/>
 *     &lt;enumeration value="REMOVE_PLANS"/>
 *     &lt;enumeration value="VIEW_PLANS"/>
 *     &lt;enumeration value="ADD_SUBSCRIPTIONS"/>
 *     &lt;enumeration value="CHANGE_SUBSCRIPTIONS"/>
 *     &lt;enumeration value="VIEW_SUBSCRIPTIONS"/>
 *     &lt;enumeration value="REMOVE_SUBSCRIPTIONS"/>
 *     &lt;enumeration value="VIEW_PAYMENT_INFORMATION"/>
 *     &lt;enumeration value="CHANGE_PAYMENT_INFORMATION"/>
 *     &lt;enumeration value="REMOVE_PAYMENT_INFORMATION"/>
 *     &lt;enumeration value="COMMENT_ON_RECORDS"/>
 *     &lt;enumeration value="ATTACH_FILES_TO_RECORDS"/>
 *     &lt;enumeration value="COMMENT_ON_CONTACTS"/>
 *     &lt;enumeration value="ATTACH_FILES_TO_CONTACTS"/>
 *     &lt;enumeration value="STORE_FILES"/>
 *     &lt;enumeration value="READ_FILES"/>
 *     &lt;enumeration value="VIEW_EMAIL_SETTINGS"/>
 *     &lt;enumeration value="CHANGE_EMAIL_SETTINGS"/>
 *     &lt;enumeration value="SEND_EMAIL"/>
 *     &lt;enumeration value="VIEW_FRESHBOOKS"/>
 *     &lt;enumeration value="CHANGE_FRESHBOOKS"/>
 *     &lt;enumeration value="REMOVE_FRESHBOOKS"/>
 *     &lt;enumeration value="CHANGE_FEATURES"/>
 *     &lt;enumeration value="VIEW_SITE_CONFIGURATION"/>
 *     &lt;enumeration value="CHANGE_SITE_CONFIGURATION"/>
 *     &lt;enumeration value="VIEW_BOOKINGCALENDAR"/>
 *     &lt;enumeration value="CHANGE_BOOKINGCALENDAR"/>
 *     &lt;enumeration value="VIEW_TEXT_FILE_DATA"/>
 *     &lt;enumeration value="CHANGE_TEXT_FILE_DATA"/>
 *     &lt;enumeration value="VIEW_TEMPLATES"/>
 *     &lt;enumeration value="CHANGE_TEMPLATES"/>
 *     &lt;enumeration value="CHANGE_BUSINESS_SETTINGS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "privilege")
@XmlEnum
public enum Privilege {

    ADD,
    ADD_ACCOUNTS,
    ADD_CONTACTS,
    ADD_ITEMS,
    ADD_JOURNAL_ENTRIES,
    ADD_RECORDS,
    ADD_USERS,
    CHANGE,
    CHANGE_ACCOUNTS,
    CHANGE_CONTACT_INFO,
    CHANGE_CURRENCY,
    CHANGE_YEAR_END,
    CHANGE_ITEMS,
    CHANGE_JOURNAL_ENTRIES,
    CHANGE_NAME,
    CHANGE_RECORDS,
    CHANGE_REGION,
    REMOVE,
    REMOVE_ACCOUNTS,
    REMOVE_BUSINESS,
    REMOVE_ITEMS,
    REMOVE_JOURNAL_ENTRIES,
    REMOVE_RECORDS,
    REMOVE_RELATIONSHIPS,
    REMOVE_ROLES,
    REMOVE_USERS,
    VIEW,
    VIEW_ACCOUNTS,
    VIEW_RECORDS,
    VIEW_ITEMS,
    VIEW_JOURNAL_ENTRIES,
    VIEW_RELATIONSHIPS,
    VIEW_USERS,
    CHANGE_CONTACTS,
    REMOVE_CONTACTS,
    VIEW_CONTACTS,
    ADD_TAXES,
    REMOVE_TAXES,
    CHANGE_TAXES,
    VIEW_TAXES,
    CHANGE_TAX_CODES,
    VIEW_CHANGESETS,
    ADD_COMMENTS,
    ADD_ATTACHMENTS,
    ADD_TAGS,
    REMOVE_TAGS,
    VIEW_REPORTS,
    CHANGE_ROLES,
    BANK_RECONCILIATION,
    CHANGE_TIME_ZONE,
    CHANGE_CONTRACT,
    VIEW_ACCOUNT_BALANCES,
    VIEW_CONTRACT,
    ADD_PAYMENT_PROCESSORS,
    VIEW_PAYMENT_PROCESSORS,
    CHANGE_PAYMENT_PROCESSORS,
    REMOVE_PAYMENT_PROCESSORS,
    ADD_PLANS,
    CHANGE_PLANS,
    REMOVE_PLANS,
    VIEW_PLANS,
    ADD_SUBSCRIPTIONS,
    CHANGE_SUBSCRIPTIONS,
    VIEW_SUBSCRIPTIONS,
    REMOVE_SUBSCRIPTIONS,
    VIEW_PAYMENT_INFORMATION,
    CHANGE_PAYMENT_INFORMATION,
    REMOVE_PAYMENT_INFORMATION,
    COMMENT_ON_RECORDS,
    ATTACH_FILES_TO_RECORDS,
    COMMENT_ON_CONTACTS,
    ATTACH_FILES_TO_CONTACTS,
    STORE_FILES,
    READ_FILES,
    VIEW_EMAIL_SETTINGS,
    CHANGE_EMAIL_SETTINGS,
    SEND_EMAIL,
    VIEW_FRESHBOOKS,
    CHANGE_FRESHBOOKS,
    REMOVE_FRESHBOOKS,
    CHANGE_FEATURES,
    VIEW_SITE_CONFIGURATION,
    CHANGE_SITE_CONFIGURATION,
    VIEW_BOOKINGCALENDAR,
    CHANGE_BOOKINGCALENDAR,
    VIEW_TEXT_FILE_DATA,
    CHANGE_TEXT_FILE_DATA,
    VIEW_TEMPLATES,
    CHANGE_TEMPLATES,
    CHANGE_BUSINESS_SETTINGS;

    public String value() {
        return name();
    }

    public static Privilege fromValue(String v) {
        return valueOf(v);
    }

}
