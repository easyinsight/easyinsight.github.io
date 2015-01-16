
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SearchRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SearchRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="account"/>
 *     &lt;enumeration value="accountingPeriod"/>
 *     &lt;enumeration value="appDefinition"/>
 *     &lt;enumeration value="appPackage"/>
 *     &lt;enumeration value="assemblyItem"/>
 *     &lt;enumeration value="bin"/>
 *     &lt;enumeration value="budget"/>
 *     &lt;enumeration value="calendarEvent"/>
 *     &lt;enumeration value="campaign"/>
 *     &lt;enumeration value="classification"/>
 *     &lt;enumeration value="contact"/>
 *     &lt;enumeration value="customer"/>
 *     &lt;enumeration value="customerMessage"/>
 *     &lt;enumeration value="customRecord"/>
 *     &lt;enumeration value="department"/>
 *     &lt;enumeration value="employee"/>
 *     &lt;enumeration value="entityGroup"/>
 *     &lt;enumeration value="file"/>
 *     &lt;enumeration value="folder"/>
 *     &lt;enumeration value="giftCertificate"/>
 *     &lt;enumeration value="groupMember"/>
 *     &lt;enumeration value="inventoryNumber"/>
 *     &lt;enumeration value="item"/>
 *     &lt;enumeration value="issue"/>
 *     &lt;enumeration value="job"/>
 *     &lt;enumeration value="location"/>
 *     &lt;enumeration value="lotNumberedAssemblyItem"/>
 *     &lt;enumeration value="manufacturingCostTemplate"/>
 *     &lt;enumeration value="manufacturingOperationTask"/>
 *     &lt;enumeration value="manufacturingRouting"/>
 *     &lt;enumeration value="message"/>
 *     &lt;enumeration value="nexus"/>
 *     &lt;enumeration value="note"/>
 *     &lt;enumeration value="opportunity"/>
 *     &lt;enumeration value="otherNameCategory"/>
 *     &lt;enumeration value="partner"/>
 *     &lt;enumeration value="phoneCall"/>
 *     &lt;enumeration value="priceLevel"/>
 *     &lt;enumeration value="pricingGroup"/>
 *     &lt;enumeration value="projectTask"/>
 *     &lt;enumeration value="promotionCode"/>
 *     &lt;enumeration value="resourceAllocation"/>
 *     &lt;enumeration value="revRecSchedule"/>
 *     &lt;enumeration value="revRecTemplate"/>
 *     &lt;enumeration value="salesRole"/>
 *     &lt;enumeration value="serializedAssemblyItem"/>
 *     &lt;enumeration value="solution"/>
 *     &lt;enumeration value="siteCategory"/>
 *     &lt;enumeration value="subsidiary"/>
 *     &lt;enumeration value="supportCase"/>
 *     &lt;enumeration value="task"/>
 *     &lt;enumeration value="timeBill"/>
 *     &lt;enumeration value="topic"/>
 *     &lt;enumeration value="transaction"/>
 *     &lt;enumeration value="vendor"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SearchRecordType", namespace = "urn:types.core_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum SearchRecordType {

    @XmlEnumValue("account")
    ACCOUNT("account"),
    @XmlEnumValue("accountingPeriod")
    ACCOUNTING_PERIOD("accountingPeriod"),
    @XmlEnumValue("appDefinition")
    APP_DEFINITION("appDefinition"),
    @XmlEnumValue("appPackage")
    APP_PACKAGE("appPackage"),
    @XmlEnumValue("assemblyItem")
    ASSEMBLY_ITEM("assemblyItem"),
    @XmlEnumValue("bin")
    BIN("bin"),
    @XmlEnumValue("budget")
    BUDGET("budget"),
    @XmlEnumValue("calendarEvent")
    CALENDAR_EVENT("calendarEvent"),
    @XmlEnumValue("campaign")
    CAMPAIGN("campaign"),
    @XmlEnumValue("classification")
    CLASSIFICATION("classification"),
    @XmlEnumValue("contact")
    CONTACT("contact"),
    @XmlEnumValue("customer")
    CUSTOMER("customer"),
    @XmlEnumValue("customerMessage")
    CUSTOMER_MESSAGE("customerMessage"),
    @XmlEnumValue("customRecord")
    CUSTOM_RECORD("customRecord"),
    @XmlEnumValue("department")
    DEPARTMENT("department"),
    @XmlEnumValue("employee")
    EMPLOYEE("employee"),
    @XmlEnumValue("entityGroup")
    ENTITY_GROUP("entityGroup"),
    @XmlEnumValue("file")
    FILE("file"),
    @XmlEnumValue("folder")
    FOLDER("folder"),
    @XmlEnumValue("giftCertificate")
    GIFT_CERTIFICATE("giftCertificate"),
    @XmlEnumValue("groupMember")
    GROUP_MEMBER("groupMember"),
    @XmlEnumValue("inventoryNumber")
    INVENTORY_NUMBER("inventoryNumber"),
    @XmlEnumValue("item")
    ITEM("item"),
    @XmlEnumValue("issue")
    ISSUE("issue"),
    @XmlEnumValue("job")
    JOB("job"),
    @XmlEnumValue("location")
    LOCATION("location"),
    @XmlEnumValue("lotNumberedAssemblyItem")
    LOT_NUMBERED_ASSEMBLY_ITEM("lotNumberedAssemblyItem"),
    @XmlEnumValue("manufacturingCostTemplate")
    MANUFACTURING_COST_TEMPLATE("manufacturingCostTemplate"),
    @XmlEnumValue("manufacturingOperationTask")
    MANUFACTURING_OPERATION_TASK("manufacturingOperationTask"),
    @XmlEnumValue("manufacturingRouting")
    MANUFACTURING_ROUTING("manufacturingRouting"),
    @XmlEnumValue("message")
    MESSAGE("message"),
    @XmlEnumValue("nexus")
    NEXUS("nexus"),
    @XmlEnumValue("note")
    NOTE("note"),
    @XmlEnumValue("opportunity")
    OPPORTUNITY("opportunity"),
    @XmlEnumValue("otherNameCategory")
    OTHER_NAME_CATEGORY("otherNameCategory"),
    @XmlEnumValue("partner")
    PARTNER("partner"),
    @XmlEnumValue("phoneCall")
    PHONE_CALL("phoneCall"),
    @XmlEnumValue("priceLevel")
    PRICE_LEVEL("priceLevel"),
    @XmlEnumValue("pricingGroup")
    PRICING_GROUP("pricingGroup"),
    @XmlEnumValue("projectTask")
    PROJECT_TASK("projectTask"),
    @XmlEnumValue("promotionCode")
    PROMOTION_CODE("promotionCode"),
    @XmlEnumValue("resourceAllocation")
    RESOURCE_ALLOCATION("resourceAllocation"),
    @XmlEnumValue("revRecSchedule")
    REV_REC_SCHEDULE("revRecSchedule"),
    @XmlEnumValue("revRecTemplate")
    REV_REC_TEMPLATE("revRecTemplate"),
    @XmlEnumValue("salesRole")
    SALES_ROLE("salesRole"),
    @XmlEnumValue("serializedAssemblyItem")
    SERIALIZED_ASSEMBLY_ITEM("serializedAssemblyItem"),
    @XmlEnumValue("solution")
    SOLUTION("solution"),
    @XmlEnumValue("siteCategory")
    SITE_CATEGORY("siteCategory"),
    @XmlEnumValue("subsidiary")
    SUBSIDIARY("subsidiary"),
    @XmlEnumValue("supportCase")
    SUPPORT_CASE("supportCase"),
    @XmlEnumValue("task")
    TASK("task"),
    @XmlEnumValue("timeBill")
    TIME_BILL("timeBill"),
    @XmlEnumValue("topic")
    TOPIC("topic"),
    @XmlEnumValue("transaction")
    TRANSACTION("transaction"),
    @XmlEnumValue("vendor")
    VENDOR("vendor");
    private final String value;

    SearchRecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SearchRecordType fromValue(String v) {
        for (SearchRecordType c: SearchRecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
