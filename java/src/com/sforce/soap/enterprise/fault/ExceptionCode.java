
package com.sforce.soap.enterprise.fault;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExceptionCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExceptionCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="API_CURRENTLY_DISABLED"/>
 *     &lt;enumeration value="API_DISABLED_FOR_ORG"/>
 *     &lt;enumeration value="CLIENT_NOT_ACCESSIBLE_FOR_USER"/>
 *     &lt;enumeration value="CLIENT_REQUIRE_UPDATE_FOR_USER"/>
 *     &lt;enumeration value="EMAIL_BATCH_SIZE_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="EMAIL_TO_CASE_INVALID_ROUTING"/>
 *     &lt;enumeration value="EMAIL_TO_CASE_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="EMAIL_TO_CASE_NOT_ENABLED"/>
 *     &lt;enumeration value="EXCEEDED_ID_LIMIT"/>
 *     &lt;enumeration value="EXCEEDED_LEAD_CONVERT_LIMIT"/>
 *     &lt;enumeration value="EXCEEDED_MAX_SIZE_REQUEST"/>
 *     &lt;enumeration value="EXCEEDED_MAX_TYPES_LIMIT"/>
 *     &lt;enumeration value="EXCEEDED_QUOTA"/>
 *     &lt;enumeration value="FUNCTIONALITY_NOT_ENABLED"/>
 *     &lt;enumeration value="INACTIVE_OWNER_OR_USER"/>
 *     &lt;enumeration value="INSUFFICIENT_ACCESS"/>
 *     &lt;enumeration value="INVALID_ASSIGNMENT_RULE"/>
 *     &lt;enumeration value="INVALID_BATCH_SIZE"/>
 *     &lt;enumeration value="INVALID_CLIENT"/>
 *     &lt;enumeration value="INVALID_CROSS_REFERENCE_KEY"/>
 *     &lt;enumeration value="INVALID_FIELD"/>
 *     &lt;enumeration value="INVALID_ID_FIELD"/>
 *     &lt;enumeration value="INVALID_LOCATOR"/>
 *     &lt;enumeration value="INVALID_LOGIN"/>
 *     &lt;enumeration value="INVALID_NEW_PASSWORD"/>
 *     &lt;enumeration value="INVALID_OPERATION"/>
 *     &lt;enumeration value="INVALID_OPERATION_WITH_EXPIRED_PASSWORD"/>
 *     &lt;enumeration value="INVALID_QUERY_FILTER_OPERATOR"/>
 *     &lt;enumeration value="INVALID_QUERY_LOCATOR"/>
 *     &lt;enumeration value="INVALID_QUERY_SCOPE"/>
 *     &lt;enumeration value="INVALID_REPLICATION_DATE"/>
 *     &lt;enumeration value="INVALID_SEARCH"/>
 *     &lt;enumeration value="INVALID_SEARCH_SCOPE"/>
 *     &lt;enumeration value="INVALID_SESSION_ID"/>
 *     &lt;enumeration value="INVALID_SOAP_HEADER"/>
 *     &lt;enumeration value="INVALID_SSO_GATEWAY_URL"/>
 *     &lt;enumeration value="INVALID_TYPE"/>
 *     &lt;enumeration value="INVALID_TYPE_FOR_OPERATION"/>
 *     &lt;enumeration value="LOGIN_DURING_RESTRICTED_DOMAIN"/>
 *     &lt;enumeration value="LOGIN_DURING_RESTRICTED_TIME"/>
 *     &lt;enumeration value="MALFORMED_ID"/>
 *     &lt;enumeration value="MALFORMED_QUERY"/>
 *     &lt;enumeration value="MALFORMED_SEARCH"/>
 *     &lt;enumeration value="MISSING_ARGUMENT"/>
 *     &lt;enumeration value="NOT_MODIFIED"/>
 *     &lt;enumeration value="NUMBER_OUTSIDE_VALID_RANGE"/>
 *     &lt;enumeration value="OPERATION_TOO_LARGE"/>
 *     &lt;enumeration value="ORG_LOCKED"/>
 *     &lt;enumeration value="PASSWORD_LOCKOUT"/>
 *     &lt;enumeration value="QUERY_TIMEOUT"/>
 *     &lt;enumeration value="QUERY_TOO_COMPLICATED"/>
 *     &lt;enumeration value="REQUEST_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="REQUEST_RUNNING_TOO_LONG"/>
 *     &lt;enumeration value="SERVER_UNAVAILABLE"/>
 *     &lt;enumeration value="SSO_SERVICE_DOWN"/>
 *     &lt;enumeration value="TRIAL_EXPIRED"/>
 *     &lt;enumeration value="UNKNOWN_EXCEPTION"/>
 *     &lt;enumeration value="UNSUPPORTED_API_VERSION"/>
 *     &lt;enumeration value="UNSUPPORTED_CLIENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExceptionCode")
@XmlEnum
public enum ExceptionCode {

    API_CURRENTLY_DISABLED,
    API_DISABLED_FOR_ORG,
    CLIENT_NOT_ACCESSIBLE_FOR_USER,
    CLIENT_REQUIRE_UPDATE_FOR_USER,
    EMAIL_BATCH_SIZE_LIMIT_EXCEEDED,
    EMAIL_TO_CASE_INVALID_ROUTING,
    EMAIL_TO_CASE_LIMIT_EXCEEDED,
    EMAIL_TO_CASE_NOT_ENABLED,
    EXCEEDED_ID_LIMIT,
    EXCEEDED_LEAD_CONVERT_LIMIT,
    EXCEEDED_MAX_SIZE_REQUEST,
    EXCEEDED_MAX_TYPES_LIMIT,
    EXCEEDED_QUOTA,
    FUNCTIONALITY_NOT_ENABLED,
    INACTIVE_OWNER_OR_USER,
    INSUFFICIENT_ACCESS,
    INVALID_ASSIGNMENT_RULE,
    INVALID_BATCH_SIZE,
    INVALID_CLIENT,
    INVALID_CROSS_REFERENCE_KEY,
    INVALID_FIELD,
    INVALID_ID_FIELD,
    INVALID_LOCATOR,
    INVALID_LOGIN,
    INVALID_NEW_PASSWORD,
    INVALID_OPERATION,
    INVALID_OPERATION_WITH_EXPIRED_PASSWORD,
    INVALID_QUERY_FILTER_OPERATOR,
    INVALID_QUERY_LOCATOR,
    INVALID_QUERY_SCOPE,
    INVALID_REPLICATION_DATE,
    INVALID_SEARCH,
    INVALID_SEARCH_SCOPE,
    INVALID_SESSION_ID,
    INVALID_SOAP_HEADER,
    INVALID_SSO_GATEWAY_URL,
    INVALID_TYPE,
    INVALID_TYPE_FOR_OPERATION,
    LOGIN_DURING_RESTRICTED_DOMAIN,
    LOGIN_DURING_RESTRICTED_TIME,
    MALFORMED_ID,
    MALFORMED_QUERY,
    MALFORMED_SEARCH,
    MISSING_ARGUMENT,
    NOT_MODIFIED,
    NUMBER_OUTSIDE_VALID_RANGE,
    OPERATION_TOO_LARGE,
    ORG_LOCKED,
    PASSWORD_LOCKOUT,
    QUERY_TIMEOUT,
    QUERY_TOO_COMPLICATED,
    REQUEST_LIMIT_EXCEEDED,
    REQUEST_RUNNING_TOO_LONG,
    SERVER_UNAVAILABLE,
    SSO_SERVICE_DOWN,
    TRIAL_EXPIRED,
    UNKNOWN_EXCEPTION,
    UNSUPPORTED_API_VERSION,
    UNSUPPORTED_CLIENT;

    public String value() {
        return name();
    }

    public static ExceptionCode fromValue(String v) {
        return valueOf(v);
    }

}
