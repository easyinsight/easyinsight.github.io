
package com.sforce.soap.enterprise;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatusCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StatusCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALREADY_IN_PROCESS"/>
 *     &lt;enumeration value="ASSIGNEE_TYPE_REQUIRED"/>
 *     &lt;enumeration value="BAD_CUSTOM_ENTITY_PARENT_DOMAIN"/>
 *     &lt;enumeration value="CANNOT_CASCADE_PRODUCT_ACTIVE"/>
 *     &lt;enumeration value="CANNOT_CREATE_ANOTHER_MANAGED_PACKAGE"/>
 *     &lt;enumeration value="CANNOT_DEACTIVATE_DIVISION"/>
 *     &lt;enumeration value="CANNOT_DELETE_MANAGED_OBJECT"/>
 *     &lt;enumeration value="CANNOT_DISABLE_LAST_ADMIN"/>
 *     &lt;enumeration value="CANNOT_ENABLE_IP_RESTRICT_REQUESTS"/>
 *     &lt;enumeration value="CANNOT_INSERT_UPDATE_ACTIVATE_ENTITY"/>
 *     &lt;enumeration value="CANNOT_MODIFY_MANAGED_OBJECT"/>
 *     &lt;enumeration value="CANNOT_REPARENT_RECORD"/>
 *     &lt;enumeration value="CANNOT_RESOLVE_NAME"/>
 *     &lt;enumeration value="CANNOT_UPDATE_CONVERTED_LEAD"/>
 *     &lt;enumeration value="CANT_DISABLE_CORP_CURRENCY"/>
 *     &lt;enumeration value="CANT_UNSET_CORP_CURRENCY"/>
 *     &lt;enumeration value="CHILD_SHARE_FAILS_PARENT"/>
 *     &lt;enumeration value="CIRCULAR_DEPENDENCY"/>
 *     &lt;enumeration value="CUSTOM_CLOB_FIELD_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="CUSTOM_ENTITY_OR_FIELD_LIMIT"/>
 *     &lt;enumeration value="CUSTOM_FIELD_INDEX_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="CUSTOM_INDEX_EXISTS"/>
 *     &lt;enumeration value="CUSTOM_LINK_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="CUSTOM_TAB_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="DELETE_FAILED"/>
 *     &lt;enumeration value="DELETE_REQUIRED_ON_CASCADE"/>
 *     &lt;enumeration value="DEPENDENCY_EXISTS"/>
 *     &lt;enumeration value="DUPLICATE_CASE_SOLUTION"/>
 *     &lt;enumeration value="DUPLICATE_CUSTOM_ENTITY_DEFINITION"/>
 *     &lt;enumeration value="DUPLICATE_CUSTOM_TAB_MOTIF"/>
 *     &lt;enumeration value="DUPLICATE_DEVELOPER_NAME"/>
 *     &lt;enumeration value="DUPLICATE_EXTERNAL_ID"/>
 *     &lt;enumeration value="DUPLICATE_MASTER_LABEL"/>
 *     &lt;enumeration value="DUPLICATE_USERNAME"/>
 *     &lt;enumeration value="DUPLICATE_VALUE"/>
 *     &lt;enumeration value="EMPTY_SCONTROL_FILE_NAME"/>
 *     &lt;enumeration value="ENTITY_FAILED_IFLASTMODIFIED_ON_UPDATE"/>
 *     &lt;enumeration value="ENTITY_IS_ARCHIVED"/>
 *     &lt;enumeration value="ENTITY_IS_DELETED"/>
 *     &lt;enumeration value="ENTITY_IS_LOCKED"/>
 *     &lt;enumeration value="FAILED_ACTIVATION"/>
 *     &lt;enumeration value="FIELD_CUSTOM_VALIDATION_EXCEPTION"/>
 *     &lt;enumeration value="FIELD_INTEGRITY_EXCEPTION"/>
 *     &lt;enumeration value="IMAGE_TOO_LARGE"/>
 *     &lt;enumeration value="INACTIVE_OWNER_OR_USER"/>
 *     &lt;enumeration value="INSUFFICIENT_ACCESS_ON_CROSS_REFERENCE_ENTITY"/>
 *     &lt;enumeration value="INSUFFICIENT_ACCESS_OR_READONLY"/>
 *     &lt;enumeration value="INVALID_ACCESS_LEVEL"/>
 *     &lt;enumeration value="INVALID_ARGUMENT_TYPE"/>
 *     &lt;enumeration value="INVALID_ASSIGNEE_TYPE"/>
 *     &lt;enumeration value="INVALID_ASSIGNMENT_RULE"/>
 *     &lt;enumeration value="INVALID_BATCH_OPERATION"/>
 *     &lt;enumeration value="INVALID_CREDIT_CARD_INFO"/>
 *     &lt;enumeration value="INVALID_CROSS_REFERENCE_KEY"/>
 *     &lt;enumeration value="INVALID_CROSS_REFERENCE_TYPE_FOR_FIELD"/>
 *     &lt;enumeration value="INVALID_CURRENCY_CONV_RATE"/>
 *     &lt;enumeration value="INVALID_CURRENCY_ISO"/>
 *     &lt;enumeration value="INVALID_EMAIL_ADDRESS"/>
 *     &lt;enumeration value="INVALID_EMPTY_KEY_OWNER"/>
 *     &lt;enumeration value="INVALID_FIELD"/>
 *     &lt;enumeration value="INVALID_FIELD_FOR_INSERT_UPDATE"/>
 *     &lt;enumeration value="INVALID_FILTER_ACTION"/>
 *     &lt;enumeration value="INVALID_ID_FIELD"/>
 *     &lt;enumeration value="INVALID_INET_ADDRESS"/>
 *     &lt;enumeration value="INVALID_LINEITEM_CLONE_STATE"/>
 *     &lt;enumeration value="INVALID_MASTER_OR_TRANSLATED_SOLUTION"/>
 *     &lt;enumeration value="INVALID_OPERATION"/>
 *     &lt;enumeration value="INVALID_OPERATOR"/>
 *     &lt;enumeration value="INVALID_OR_NULL_FOR_RESTRICTED_PICKLIST"/>
 *     &lt;enumeration value="INVALID_PERSON_ACCOUNT_OPERATION"/>
 *     &lt;enumeration value="INVALID_STATUS"/>
 *     &lt;enumeration value="INVALID_TYPE"/>
 *     &lt;enumeration value="INVALID_TYPE_FOR_OPERATION"/>
 *     &lt;enumeration value="INVALID_TYPE_ON_FIELD_IN_RECORD"/>
 *     &lt;enumeration value="IP_RANGE_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="LAST_MODIFIED_SINCE_TOO_OLD"/>
 *     &lt;enumeration value="LICENSE_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="MALFORMED_ID"/>
 *     &lt;enumeration value="MANAGER_NOT_DEFINED"/>
 *     &lt;enumeration value="MAXIMUM_CCEMAILS_EXCEEDED"/>
 *     &lt;enumeration value="MAXIMUM_DASHBOARD_COMPONENTS_EXCEEDED"/>
 *     &lt;enumeration value="MAXIMUM_SIZE_OF_ATTACHMENT"/>
 *     &lt;enumeration value="MAXIMUM_SIZE_OF_DOCUMENT"/>
 *     &lt;enumeration value="MAX_ACTIONS_PER_RULE_EXCEEDED"/>
 *     &lt;enumeration value="MAX_ACTIVE_RULES_EXCEEDED"/>
 *     &lt;enumeration value="MAX_APPROVAL_STEPS_EXCEEDED"/>
 *     &lt;enumeration value="MAX_RULES_EXCEEDED"/>
 *     &lt;enumeration value="MAX_RULE_ENTRIES_EXCEEDED"/>
 *     &lt;enumeration value="MAX_TASK_DESCRIPTION_EXCEEEDED"/>
 *     &lt;enumeration value="MAX_TM_RULES_EXCEEDED"/>
 *     &lt;enumeration value="MAX_TM_RULE_ITEMS_EXCEEDED"/>
 *     &lt;enumeration value="MISSING_ARGUMENT"/>
 *     &lt;enumeration value="NONUNIQUE_SHIPPING_ADDRESS"/>
 *     &lt;enumeration value="NO_APPLICABLE_PROCESS"/>
 *     &lt;enumeration value="NUMBER_OUTSIDE_VALID_RANGE"/>
 *     &lt;enumeration value="NUM_HISTORY_FIELDS_BY_SOBJECT_EXCEEDED"/>
 *     &lt;enumeration value="PRIVATE_CONTACT_ON_ASSET"/>
 *     &lt;enumeration value="REQUIRED_FIELD_MISSING"/>
 *     &lt;enumeration value="SHARE_NEEDED_FOR_CHILD_OWNER"/>
 *     &lt;enumeration value="STANDARD_PRICE_NOT_DEFINED"/>
 *     &lt;enumeration value="STORAGE_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="TABSET_LIMIT_EXCEEDED"/>
 *     &lt;enumeration value="TERRITORY_REALIGN_IN_PROGRESS"/>
 *     &lt;enumeration value="TEXT_DATA_OUTSIDE_SUPPORTED_CHARSET"/>
 *     &lt;enumeration value="TOO_MANY_ENUM_VALUE"/>
 *     &lt;enumeration value="TRANSFER_REQUIRES_READ"/>
 *     &lt;enumeration value="UNAVAILABLE_RECORDTYPE_EXCEPTION"/>
 *     &lt;enumeration value="UNDELETE_FAILED"/>
 *     &lt;enumeration value="UNKNOWN_EXCEPTION"/>
 *     &lt;enumeration value="UNSPECIFIED_EMAIL_ADDRESS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StatusCode")
@XmlEnum
public enum StatusCode {

    ALREADY_IN_PROCESS,
    ASSIGNEE_TYPE_REQUIRED,
    BAD_CUSTOM_ENTITY_PARENT_DOMAIN,
    CANNOT_CASCADE_PRODUCT_ACTIVE,
    CANNOT_CREATE_ANOTHER_MANAGED_PACKAGE,
    CANNOT_DEACTIVATE_DIVISION,
    CANNOT_DELETE_MANAGED_OBJECT,
    CANNOT_DISABLE_LAST_ADMIN,
    CANNOT_ENABLE_IP_RESTRICT_REQUESTS,
    CANNOT_INSERT_UPDATE_ACTIVATE_ENTITY,
    CANNOT_MODIFY_MANAGED_OBJECT,
    CANNOT_REPARENT_RECORD,
    CANNOT_RESOLVE_NAME,
    CANNOT_UPDATE_CONVERTED_LEAD,
    CANT_DISABLE_CORP_CURRENCY,
    CANT_UNSET_CORP_CURRENCY,
    CHILD_SHARE_FAILS_PARENT,
    CIRCULAR_DEPENDENCY,
    CUSTOM_CLOB_FIELD_LIMIT_EXCEEDED,
    CUSTOM_ENTITY_OR_FIELD_LIMIT,
    CUSTOM_FIELD_INDEX_LIMIT_EXCEEDED,
    CUSTOM_INDEX_EXISTS,
    CUSTOM_LINK_LIMIT_EXCEEDED,
    CUSTOM_TAB_LIMIT_EXCEEDED,
    DELETE_FAILED,
    DELETE_REQUIRED_ON_CASCADE,
    DEPENDENCY_EXISTS,
    DUPLICATE_CASE_SOLUTION,
    DUPLICATE_CUSTOM_ENTITY_DEFINITION,
    DUPLICATE_CUSTOM_TAB_MOTIF,
    DUPLICATE_DEVELOPER_NAME,
    DUPLICATE_EXTERNAL_ID,
    DUPLICATE_MASTER_LABEL,
    DUPLICATE_USERNAME,
    DUPLICATE_VALUE,
    EMPTY_SCONTROL_FILE_NAME,
    ENTITY_FAILED_IFLASTMODIFIED_ON_UPDATE,
    ENTITY_IS_ARCHIVED,
    ENTITY_IS_DELETED,
    ENTITY_IS_LOCKED,
    FAILED_ACTIVATION,
    FIELD_CUSTOM_VALIDATION_EXCEPTION,
    FIELD_INTEGRITY_EXCEPTION,
    IMAGE_TOO_LARGE,
    INACTIVE_OWNER_OR_USER,
    INSUFFICIENT_ACCESS_ON_CROSS_REFERENCE_ENTITY,
    INSUFFICIENT_ACCESS_OR_READONLY,
    INVALID_ACCESS_LEVEL,
    INVALID_ARGUMENT_TYPE,
    INVALID_ASSIGNEE_TYPE,
    INVALID_ASSIGNMENT_RULE,
    INVALID_BATCH_OPERATION,
    INVALID_CREDIT_CARD_INFO,
    INVALID_CROSS_REFERENCE_KEY,
    INVALID_CROSS_REFERENCE_TYPE_FOR_FIELD,
    INVALID_CURRENCY_CONV_RATE,
    INVALID_CURRENCY_ISO,
    INVALID_EMAIL_ADDRESS,
    INVALID_EMPTY_KEY_OWNER,
    INVALID_FIELD,
    INVALID_FIELD_FOR_INSERT_UPDATE,
    INVALID_FILTER_ACTION,
    INVALID_ID_FIELD,
    INVALID_INET_ADDRESS,
    INVALID_LINEITEM_CLONE_STATE,
    INVALID_MASTER_OR_TRANSLATED_SOLUTION,
    INVALID_OPERATION,
    INVALID_OPERATOR,
    INVALID_OR_NULL_FOR_RESTRICTED_PICKLIST,
    INVALID_PERSON_ACCOUNT_OPERATION,
    INVALID_STATUS,
    INVALID_TYPE,
    INVALID_TYPE_FOR_OPERATION,
    INVALID_TYPE_ON_FIELD_IN_RECORD,
    IP_RANGE_LIMIT_EXCEEDED,
    LAST_MODIFIED_SINCE_TOO_OLD,
    LICENSE_LIMIT_EXCEEDED,
    LIMIT_EXCEEDED,
    MALFORMED_ID,
    MANAGER_NOT_DEFINED,
    MAXIMUM_CCEMAILS_EXCEEDED,
    MAXIMUM_DASHBOARD_COMPONENTS_EXCEEDED,
    MAXIMUM_SIZE_OF_ATTACHMENT,
    MAXIMUM_SIZE_OF_DOCUMENT,
    MAX_ACTIONS_PER_RULE_EXCEEDED,
    MAX_ACTIVE_RULES_EXCEEDED,
    MAX_APPROVAL_STEPS_EXCEEDED,
    MAX_RULES_EXCEEDED,
    MAX_RULE_ENTRIES_EXCEEDED,
    MAX_TASK_DESCRIPTION_EXCEEEDED,
    MAX_TM_RULES_EXCEEDED,
    MAX_TM_RULE_ITEMS_EXCEEDED,
    MISSING_ARGUMENT,
    NONUNIQUE_SHIPPING_ADDRESS,
    NO_APPLICABLE_PROCESS,
    NUMBER_OUTSIDE_VALID_RANGE,
    NUM_HISTORY_FIELDS_BY_SOBJECT_EXCEEDED,
    PRIVATE_CONTACT_ON_ASSET,
    REQUIRED_FIELD_MISSING,
    SHARE_NEEDED_FOR_CHILD_OWNER,
    STANDARD_PRICE_NOT_DEFINED,
    STORAGE_LIMIT_EXCEEDED,
    TABSET_LIMIT_EXCEEDED,
    TERRITORY_REALIGN_IN_PROGRESS,
    TEXT_DATA_OUTSIDE_SUPPORTED_CHARSET,
    TOO_MANY_ENUM_VALUE,
    TRANSFER_REQUIRES_READ,
    UNAVAILABLE_RECORDTYPE_EXCEPTION,
    UNDELETE_FAILED,
    UNKNOWN_EXCEPTION,
    UNSPECIFIED_EMAIL_ADDRESS;

    public String value() {
        return name();
    }

    public static StatusCode fromValue(String v) {
        return valueOf(v);
    }

}
