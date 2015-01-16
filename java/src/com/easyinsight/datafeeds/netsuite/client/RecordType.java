
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="account"/>
 *     &lt;enumeration value="accountingPeriod"/>
 *     &lt;enumeration value="appDefinition"/>
 *     &lt;enumeration value="appPackage"/>
 *     &lt;enumeration value="assemblyBuild"/>
 *     &lt;enumeration value="assemblyUnbuild"/>
 *     &lt;enumeration value="assemblyItem"/>
 *     &lt;enumeration value="billingSchedule"/>
 *     &lt;enumeration value="bin"/>
 *     &lt;enumeration value="binTransfer"/>
 *     &lt;enumeration value="binWorksheet"/>
 *     &lt;enumeration value="budget"/>
 *     &lt;enumeration value="budgetCategory"/>
 *     &lt;enumeration value="calendarEvent"/>
 *     &lt;enumeration value="campaign"/>
 *     &lt;enumeration value="campaignAudience"/>
 *     &lt;enumeration value="campaignCategory"/>
 *     &lt;enumeration value="campaignChannel"/>
 *     &lt;enumeration value="campaignFamily"/>
 *     &lt;enumeration value="campaignOffer"/>
 *     &lt;enumeration value="campaignResponse"/>
 *     &lt;enumeration value="campaignSearchEngine"/>
 *     &lt;enumeration value="campaignSubscription"/>
 *     &lt;enumeration value="campaignVertical"/>
 *     &lt;enumeration value="cashRefund"/>
 *     &lt;enumeration value="cashSale"/>
 *     &lt;enumeration value="check"/>
 *     &lt;enumeration value="charge"/>
 *     &lt;enumeration value="classification"/>
 *     &lt;enumeration value="contact"/>
 *     &lt;enumeration value="contactCategory"/>
 *     &lt;enumeration value="contactRole"/>
 *     &lt;enumeration value="costCategory"/>
 *     &lt;enumeration value="couponCode"/>
 *     &lt;enumeration value="creditMemo"/>
 *     &lt;enumeration value="crmCustomField"/>
 *     &lt;enumeration value="currency"/>
 *     &lt;enumeration value="currencyRate"/>
 *     &lt;enumeration value="customList"/>
 *     &lt;enumeration value="customRecord"/>
 *     &lt;enumeration value="customRecordCustomField"/>
 *     &lt;enumeration value="customRecordType"/>
 *     &lt;enumeration value="customer"/>
 *     &lt;enumeration value="customerCategory"/>
 *     &lt;enumeration value="customerDeposit"/>
 *     &lt;enumeration value="customerMessage"/>
 *     &lt;enumeration value="customerPayment"/>
 *     &lt;enumeration value="customerRefund"/>
 *     &lt;enumeration value="customerStatus"/>
 *     &lt;enumeration value="deposit"/>
 *     &lt;enumeration value="depositApplication"/>
 *     &lt;enumeration value="department"/>
 *     &lt;enumeration value="descriptionItem"/>
 *     &lt;enumeration value="discountItem"/>
 *     &lt;enumeration value="downloadItem"/>
 *     &lt;enumeration value="employee"/>
 *     &lt;enumeration value="entityCustomField"/>
 *     &lt;enumeration value="entityGroup"/>
 *     &lt;enumeration value="estimate"/>
 *     &lt;enumeration value="expenseCategory"/>
 *     &lt;enumeration value="expenseReport"/>
 *     &lt;enumeration value="file"/>
 *     &lt;enumeration value="folder"/>
 *     &lt;enumeration value="giftCertificate"/>
 *     &lt;enumeration value="giftCertificateItem"/>
 *     &lt;enumeration value="globalAccountMapping"/>
 *     &lt;enumeration value="interCompanyJournalEntry"/>
 *     &lt;enumeration value="interCompanyTransferOrder"/>
 *     &lt;enumeration value="inventoryAdjustment"/>
 *     &lt;enumeration value="inventoryCostRevaluation"/>
 *     &lt;enumeration value="inventoryItem"/>
 *     &lt;enumeration value="inventoryNumber"/>
 *     &lt;enumeration value="inventoryTransfer"/>
 *     &lt;enumeration value="invoice"/>
 *     &lt;enumeration value="itemAccountMapping"/>
 *     &lt;enumeration value="itemCustomField"/>
 *     &lt;enumeration value="itemDemandPlan"/>
 *     &lt;enumeration value="itemFulfillment"/>
 *     &lt;enumeration value="itemGroup"/>
 *     &lt;enumeration value="itemNumberCustomField"/>
 *     &lt;enumeration value="itemOptionCustomField"/>
 *     &lt;enumeration value="itemSupplyPlan"/>
 *     &lt;enumeration value="itemRevision"/>
 *     &lt;enumeration value="issue"/>
 *     &lt;enumeration value="job"/>
 *     &lt;enumeration value="jobStatus"/>
 *     &lt;enumeration value="jobType"/>
 *     &lt;enumeration value="itemReceipt"/>
 *     &lt;enumeration value="journalEntry"/>
 *     &lt;enumeration value="kitItem"/>
 *     &lt;enumeration value="leadSource"/>
 *     &lt;enumeration value="location"/>
 *     &lt;enumeration value="lotNumberedInventoryItem"/>
 *     &lt;enumeration value="lotNumberedAssemblyItem"/>
 *     &lt;enumeration value="markupItem"/>
 *     &lt;enumeration value="message"/>
 *     &lt;enumeration value="manufacturingCostTemplate"/>
 *     &lt;enumeration value="manufacturingOperationTask"/>
 *     &lt;enumeration value="manufacturingRouting"/>
 *     &lt;enumeration value="nexus"/>
 *     &lt;enumeration value="nonInventoryPurchaseItem"/>
 *     &lt;enumeration value="nonInventoryResaleItem"/>
 *     &lt;enumeration value="nonInventorySaleItem"/>
 *     &lt;enumeration value="note"/>
 *     &lt;enumeration value="noteType"/>
 *     &lt;enumeration value="opportunity"/>
 *     &lt;enumeration value="otherChargePurchaseItem"/>
 *     &lt;enumeration value="otherChargeResaleItem"/>
 *     &lt;enumeration value="otherChargeSaleItem"/>
 *     &lt;enumeration value="otherCustomField"/>
 *     &lt;enumeration value="otherNameCategory"/>
 *     &lt;enumeration value="partner"/>
 *     &lt;enumeration value="partnerCategory"/>
 *     &lt;enumeration value="paycheckJournal"/>
 *     &lt;enumeration value="paymentItem"/>
 *     &lt;enumeration value="paymentMethod"/>
 *     &lt;enumeration value="payrollItem"/>
 *     &lt;enumeration value="phoneCall"/>
 *     &lt;enumeration value="priceLevel"/>
 *     &lt;enumeration value="pricingGroup"/>
 *     &lt;enumeration value="projectTask"/>
 *     &lt;enumeration value="promotionCode"/>
 *     &lt;enumeration value="purchaseOrder"/>
 *     &lt;enumeration value="resourceAllocation"/>
 *     &lt;enumeration value="returnAuthorization"/>
 *     &lt;enumeration value="revRecSchedule"/>
 *     &lt;enumeration value="revRecTemplate"/>
 *     &lt;enumeration value="salesOrder"/>
 *     &lt;enumeration value="salesRole"/>
 *     &lt;enumeration value="salesTaxItem"/>
 *     &lt;enumeration value="serializedInventoryItem"/>
 *     &lt;enumeration value="serializedAssemblyItem"/>
 *     &lt;enumeration value="servicePurchaseItem"/>
 *     &lt;enumeration value="serviceResaleItem"/>
 *     &lt;enumeration value="serviceSaleItem"/>
 *     &lt;enumeration value="solution"/>
 *     &lt;enumeration value="siteCategory"/>
 *     &lt;enumeration value="state"/>
 *     &lt;enumeration value="statisticalJournalEntry"/>
 *     &lt;enumeration value="subsidiary"/>
 *     &lt;enumeration value="subtotalItem"/>
 *     &lt;enumeration value="supportCase"/>
 *     &lt;enumeration value="supportCaseIssue"/>
 *     &lt;enumeration value="supportCaseOrigin"/>
 *     &lt;enumeration value="supportCasePriority"/>
 *     &lt;enumeration value="supportCaseStatus"/>
 *     &lt;enumeration value="supportCaseType"/>
 *     &lt;enumeration value="task"/>
 *     &lt;enumeration value="taxAcct"/>
 *     &lt;enumeration value="taxGroup"/>
 *     &lt;enumeration value="taxType"/>
 *     &lt;enumeration value="term"/>
 *     &lt;enumeration value="timeBill"/>
 *     &lt;enumeration value="timeSheet"/>
 *     &lt;enumeration value="topic"/>
 *     &lt;enumeration value="transferOrder"/>
 *     &lt;enumeration value="transactionBodyCustomField"/>
 *     &lt;enumeration value="transactionColumnCustomField"/>
 *     &lt;enumeration value="unitsType"/>
 *     &lt;enumeration value="vendor"/>
 *     &lt;enumeration value="vendorCategory"/>
 *     &lt;enumeration value="vendorBill"/>
 *     &lt;enumeration value="vendorCredit"/>
 *     &lt;enumeration value="vendorPayment"/>
 *     &lt;enumeration value="vendorReturnAuthorization"/>
 *     &lt;enumeration value="winLossReason"/>
 *     &lt;enumeration value="workOrder"/>
 *     &lt;enumeration value="workOrderIssue"/>
 *     &lt;enumeration value="workOrderCompletion"/>
 *     &lt;enumeration value="workOrderClose"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RecordType", namespace = "urn:types.core_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum RecordType {

    @XmlEnumValue("account")
    ACCOUNT("account"),
    @XmlEnumValue("accountingPeriod")
    ACCOUNTING_PERIOD("accountingPeriod"),
    @XmlEnumValue("appDefinition")
    APP_DEFINITION("appDefinition"),
    @XmlEnumValue("appPackage")
    APP_PACKAGE("appPackage"),
    @XmlEnumValue("assemblyBuild")
    ASSEMBLY_BUILD("assemblyBuild"),
    @XmlEnumValue("assemblyUnbuild")
    ASSEMBLY_UNBUILD("assemblyUnbuild"),
    @XmlEnumValue("assemblyItem")
    ASSEMBLY_ITEM("assemblyItem"),
    @XmlEnumValue("billingSchedule")
    BILLING_SCHEDULE("billingSchedule"),
    @XmlEnumValue("bin")
    BIN("bin"),
    @XmlEnumValue("binTransfer")
    BIN_TRANSFER("binTransfer"),
    @XmlEnumValue("binWorksheet")
    BIN_WORKSHEET("binWorksheet"),
    @XmlEnumValue("budget")
    BUDGET("budget"),
    @XmlEnumValue("budgetCategory")
    BUDGET_CATEGORY("budgetCategory"),
    @XmlEnumValue("calendarEvent")
    CALENDAR_EVENT("calendarEvent"),
    @XmlEnumValue("campaign")
    CAMPAIGN("campaign"),
    @XmlEnumValue("campaignAudience")
    CAMPAIGN_AUDIENCE("campaignAudience"),
    @XmlEnumValue("campaignCategory")
    CAMPAIGN_CATEGORY("campaignCategory"),
    @XmlEnumValue("campaignChannel")
    CAMPAIGN_CHANNEL("campaignChannel"),
    @XmlEnumValue("campaignFamily")
    CAMPAIGN_FAMILY("campaignFamily"),
    @XmlEnumValue("campaignOffer")
    CAMPAIGN_OFFER("campaignOffer"),
    @XmlEnumValue("campaignResponse")
    CAMPAIGN_RESPONSE("campaignResponse"),
    @XmlEnumValue("campaignSearchEngine")
    CAMPAIGN_SEARCH_ENGINE("campaignSearchEngine"),
    @XmlEnumValue("campaignSubscription")
    CAMPAIGN_SUBSCRIPTION("campaignSubscription"),
    @XmlEnumValue("campaignVertical")
    CAMPAIGN_VERTICAL("campaignVertical"),
    @XmlEnumValue("cashRefund")
    CASH_REFUND("cashRefund"),
    @XmlEnumValue("cashSale")
    CASH_SALE("cashSale"),
    @XmlEnumValue("check")
    CHECK("check"),
    @XmlEnumValue("charge")
    CHARGE("charge"),
    @XmlEnumValue("classification")
    CLASSIFICATION("classification"),
    @XmlEnumValue("contact")
    CONTACT("contact"),
    @XmlEnumValue("contactCategory")
    CONTACT_CATEGORY("contactCategory"),
    @XmlEnumValue("contactRole")
    CONTACT_ROLE("contactRole"),
    @XmlEnumValue("costCategory")
    COST_CATEGORY("costCategory"),
    @XmlEnumValue("couponCode")
    COUPON_CODE("couponCode"),
    @XmlEnumValue("creditMemo")
    CREDIT_MEMO("creditMemo"),
    @XmlEnumValue("crmCustomField")
    CRM_CUSTOM_FIELD("crmCustomField"),
    @XmlEnumValue("currency")
    CURRENCY("currency"),
    @XmlEnumValue("currencyRate")
    CURRENCY_RATE("currencyRate"),
    @XmlEnumValue("customList")
    CUSTOM_LIST("customList"),
    @XmlEnumValue("customRecord")
    CUSTOM_RECORD("customRecord"),
    @XmlEnumValue("customRecordCustomField")
    CUSTOM_RECORD_CUSTOM_FIELD("customRecordCustomField"),
    @XmlEnumValue("customRecordType")
    CUSTOM_RECORD_TYPE("customRecordType"),
    @XmlEnumValue("customer")
    CUSTOMER("customer"),
    @XmlEnumValue("customerCategory")
    CUSTOMER_CATEGORY("customerCategory"),
    @XmlEnumValue("customerDeposit")
    CUSTOMER_DEPOSIT("customerDeposit"),
    @XmlEnumValue("customerMessage")
    CUSTOMER_MESSAGE("customerMessage"),
    @XmlEnumValue("customerPayment")
    CUSTOMER_PAYMENT("customerPayment"),
    @XmlEnumValue("customerRefund")
    CUSTOMER_REFUND("customerRefund"),
    @XmlEnumValue("customerStatus")
    CUSTOMER_STATUS("customerStatus"),
    @XmlEnumValue("deposit")
    DEPOSIT("deposit"),
    @XmlEnumValue("depositApplication")
    DEPOSIT_APPLICATION("depositApplication"),
    @XmlEnumValue("department")
    DEPARTMENT("department"),
    @XmlEnumValue("descriptionItem")
    DESCRIPTION_ITEM("descriptionItem"),
    @XmlEnumValue("discountItem")
    DISCOUNT_ITEM("discountItem"),
    @XmlEnumValue("downloadItem")
    DOWNLOAD_ITEM("downloadItem"),
    @XmlEnumValue("employee")
    EMPLOYEE("employee"),
    @XmlEnumValue("entityCustomField")
    ENTITY_CUSTOM_FIELD("entityCustomField"),
    @XmlEnumValue("entityGroup")
    ENTITY_GROUP("entityGroup"),
    @XmlEnumValue("estimate")
    ESTIMATE("estimate"),
    @XmlEnumValue("expenseCategory")
    EXPENSE_CATEGORY("expenseCategory"),
    @XmlEnumValue("expenseReport")
    EXPENSE_REPORT("expenseReport"),
    @XmlEnumValue("file")
    FILE("file"),
    @XmlEnumValue("folder")
    FOLDER("folder"),
    @XmlEnumValue("giftCertificate")
    GIFT_CERTIFICATE("giftCertificate"),
    @XmlEnumValue("giftCertificateItem")
    GIFT_CERTIFICATE_ITEM("giftCertificateItem"),
    @XmlEnumValue("globalAccountMapping")
    GLOBAL_ACCOUNT_MAPPING("globalAccountMapping"),
    @XmlEnumValue("interCompanyJournalEntry")
    INTER_COMPANY_JOURNAL_ENTRY("interCompanyJournalEntry"),
    @XmlEnumValue("interCompanyTransferOrder")
    INTER_COMPANY_TRANSFER_ORDER("interCompanyTransferOrder"),
    @XmlEnumValue("inventoryAdjustment")
    INVENTORY_ADJUSTMENT("inventoryAdjustment"),
    @XmlEnumValue("inventoryCostRevaluation")
    INVENTORY_COST_REVALUATION("inventoryCostRevaluation"),
    @XmlEnumValue("inventoryItem")
    INVENTORY_ITEM("inventoryItem"),
    @XmlEnumValue("inventoryNumber")
    INVENTORY_NUMBER("inventoryNumber"),
    @XmlEnumValue("inventoryTransfer")
    INVENTORY_TRANSFER("inventoryTransfer"),
    @XmlEnumValue("invoice")
    INVOICE("invoice"),
    @XmlEnumValue("itemAccountMapping")
    ITEM_ACCOUNT_MAPPING("itemAccountMapping"),
    @XmlEnumValue("itemCustomField")
    ITEM_CUSTOM_FIELD("itemCustomField"),
    @XmlEnumValue("itemDemandPlan")
    ITEM_DEMAND_PLAN("itemDemandPlan"),
    @XmlEnumValue("itemFulfillment")
    ITEM_FULFILLMENT("itemFulfillment"),
    @XmlEnumValue("itemGroup")
    ITEM_GROUP("itemGroup"),
    @XmlEnumValue("itemNumberCustomField")
    ITEM_NUMBER_CUSTOM_FIELD("itemNumberCustomField"),
    @XmlEnumValue("itemOptionCustomField")
    ITEM_OPTION_CUSTOM_FIELD("itemOptionCustomField"),
    @XmlEnumValue("itemSupplyPlan")
    ITEM_SUPPLY_PLAN("itemSupplyPlan"),
    @XmlEnumValue("itemRevision")
    ITEM_REVISION("itemRevision"),
    @XmlEnumValue("issue")
    ISSUE("issue"),
    @XmlEnumValue("job")
    JOB("job"),
    @XmlEnumValue("jobStatus")
    JOB_STATUS("jobStatus"),
    @XmlEnumValue("jobType")
    JOB_TYPE("jobType"),
    @XmlEnumValue("itemReceipt")
    ITEM_RECEIPT("itemReceipt"),
    @XmlEnumValue("journalEntry")
    JOURNAL_ENTRY("journalEntry"),
    @XmlEnumValue("kitItem")
    KIT_ITEM("kitItem"),
    @XmlEnumValue("leadSource")
    LEAD_SOURCE("leadSource"),
    @XmlEnumValue("location")
    LOCATION("location"),
    @XmlEnumValue("lotNumberedInventoryItem")
    LOT_NUMBERED_INVENTORY_ITEM("lotNumberedInventoryItem"),
    @XmlEnumValue("lotNumberedAssemblyItem")
    LOT_NUMBERED_ASSEMBLY_ITEM("lotNumberedAssemblyItem"),
    @XmlEnumValue("markupItem")
    MARKUP_ITEM("markupItem"),
    @XmlEnumValue("message")
    MESSAGE("message"),
    @XmlEnumValue("manufacturingCostTemplate")
    MANUFACTURING_COST_TEMPLATE("manufacturingCostTemplate"),
    @XmlEnumValue("manufacturingOperationTask")
    MANUFACTURING_OPERATION_TASK("manufacturingOperationTask"),
    @XmlEnumValue("manufacturingRouting")
    MANUFACTURING_ROUTING("manufacturingRouting"),
    @XmlEnumValue("nexus")
    NEXUS("nexus"),
    @XmlEnumValue("nonInventoryPurchaseItem")
    NON_INVENTORY_PURCHASE_ITEM("nonInventoryPurchaseItem"),
    @XmlEnumValue("nonInventoryResaleItem")
    NON_INVENTORY_RESALE_ITEM("nonInventoryResaleItem"),
    @XmlEnumValue("nonInventorySaleItem")
    NON_INVENTORY_SALE_ITEM("nonInventorySaleItem"),
    @XmlEnumValue("note")
    NOTE("note"),
    @XmlEnumValue("noteType")
    NOTE_TYPE("noteType"),
    @XmlEnumValue("opportunity")
    OPPORTUNITY("opportunity"),
    @XmlEnumValue("otherChargePurchaseItem")
    OTHER_CHARGE_PURCHASE_ITEM("otherChargePurchaseItem"),
    @XmlEnumValue("otherChargeResaleItem")
    OTHER_CHARGE_RESALE_ITEM("otherChargeResaleItem"),
    @XmlEnumValue("otherChargeSaleItem")
    OTHER_CHARGE_SALE_ITEM("otherChargeSaleItem"),
    @XmlEnumValue("otherCustomField")
    OTHER_CUSTOM_FIELD("otherCustomField"),
    @XmlEnumValue("otherNameCategory")
    OTHER_NAME_CATEGORY("otherNameCategory"),
    @XmlEnumValue("partner")
    PARTNER("partner"),
    @XmlEnumValue("partnerCategory")
    PARTNER_CATEGORY("partnerCategory"),
    @XmlEnumValue("paycheckJournal")
    PAYCHECK_JOURNAL("paycheckJournal"),
    @XmlEnumValue("paymentItem")
    PAYMENT_ITEM("paymentItem"),
    @XmlEnumValue("paymentMethod")
    PAYMENT_METHOD("paymentMethod"),
    @XmlEnumValue("payrollItem")
    PAYROLL_ITEM("payrollItem"),
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
    @XmlEnumValue("purchaseOrder")
    PURCHASE_ORDER("purchaseOrder"),
    @XmlEnumValue("resourceAllocation")
    RESOURCE_ALLOCATION("resourceAllocation"),
    @XmlEnumValue("returnAuthorization")
    RETURN_AUTHORIZATION("returnAuthorization"),
    @XmlEnumValue("revRecSchedule")
    REV_REC_SCHEDULE("revRecSchedule"),
    @XmlEnumValue("revRecTemplate")
    REV_REC_TEMPLATE("revRecTemplate"),
    @XmlEnumValue("salesOrder")
    SALES_ORDER("salesOrder"),
    @XmlEnumValue("salesRole")
    SALES_ROLE("salesRole"),
    @XmlEnumValue("salesTaxItem")
    SALES_TAX_ITEM("salesTaxItem"),
    @XmlEnumValue("serializedInventoryItem")
    SERIALIZED_INVENTORY_ITEM("serializedInventoryItem"),
    @XmlEnumValue("serializedAssemblyItem")
    SERIALIZED_ASSEMBLY_ITEM("serializedAssemblyItem"),
    @XmlEnumValue("servicePurchaseItem")
    SERVICE_PURCHASE_ITEM("servicePurchaseItem"),
    @XmlEnumValue("serviceResaleItem")
    SERVICE_RESALE_ITEM("serviceResaleItem"),
    @XmlEnumValue("serviceSaleItem")
    SERVICE_SALE_ITEM("serviceSaleItem"),
    @XmlEnumValue("solution")
    SOLUTION("solution"),
    @XmlEnumValue("siteCategory")
    SITE_CATEGORY("siteCategory"),
    @XmlEnumValue("state")
    STATE("state"),
    @XmlEnumValue("statisticalJournalEntry")
    STATISTICAL_JOURNAL_ENTRY("statisticalJournalEntry"),
    @XmlEnumValue("subsidiary")
    SUBSIDIARY("subsidiary"),
    @XmlEnumValue("subtotalItem")
    SUBTOTAL_ITEM("subtotalItem"),
    @XmlEnumValue("supportCase")
    SUPPORT_CASE("supportCase"),
    @XmlEnumValue("supportCaseIssue")
    SUPPORT_CASE_ISSUE("supportCaseIssue"),
    @XmlEnumValue("supportCaseOrigin")
    SUPPORT_CASE_ORIGIN("supportCaseOrigin"),
    @XmlEnumValue("supportCasePriority")
    SUPPORT_CASE_PRIORITY("supportCasePriority"),
    @XmlEnumValue("supportCaseStatus")
    SUPPORT_CASE_STATUS("supportCaseStatus"),
    @XmlEnumValue("supportCaseType")
    SUPPORT_CASE_TYPE("supportCaseType"),
    @XmlEnumValue("task")
    TASK("task"),
    @XmlEnumValue("taxAcct")
    TAX_ACCT("taxAcct"),
    @XmlEnumValue("taxGroup")
    TAX_GROUP("taxGroup"),
    @XmlEnumValue("taxType")
    TAX_TYPE("taxType"),
    @XmlEnumValue("term")
    TERM("term"),
    @XmlEnumValue("timeBill")
    TIME_BILL("timeBill"),
    @XmlEnumValue("timeSheet")
    TIME_SHEET("timeSheet"),
    @XmlEnumValue("topic")
    TOPIC("topic"),
    @XmlEnumValue("transferOrder")
    TRANSFER_ORDER("transferOrder"),
    @XmlEnumValue("transactionBodyCustomField")
    TRANSACTION_BODY_CUSTOM_FIELD("transactionBodyCustomField"),
    @XmlEnumValue("transactionColumnCustomField")
    TRANSACTION_COLUMN_CUSTOM_FIELD("transactionColumnCustomField"),
    @XmlEnumValue("unitsType")
    UNITS_TYPE("unitsType"),
    @XmlEnumValue("vendor")
    VENDOR("vendor"),
    @XmlEnumValue("vendorCategory")
    VENDOR_CATEGORY("vendorCategory"),
    @XmlEnumValue("vendorBill")
    VENDOR_BILL("vendorBill"),
    @XmlEnumValue("vendorCredit")
    VENDOR_CREDIT("vendorCredit"),
    @XmlEnumValue("vendorPayment")
    VENDOR_PAYMENT("vendorPayment"),
    @XmlEnumValue("vendorReturnAuthorization")
    VENDOR_RETURN_AUTHORIZATION("vendorReturnAuthorization"),
    @XmlEnumValue("winLossReason")
    WIN_LOSS_REASON("winLossReason"),
    @XmlEnumValue("workOrder")
    WORK_ORDER("workOrder"),
    @XmlEnumValue("workOrderIssue")
    WORK_ORDER_ISSUE("workOrderIssue"),
    @XmlEnumValue("workOrderCompletion")
    WORK_ORDER_COMPLETION("workOrderCompletion"),
    @XmlEnumValue("workOrderClose")
    WORK_ORDER_CLOSE("workOrderClose");
    private final String value;

    RecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RecordType fromValue(String v) {
        for (RecordType c: RecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
