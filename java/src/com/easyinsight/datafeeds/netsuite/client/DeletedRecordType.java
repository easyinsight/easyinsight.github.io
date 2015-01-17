
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeletedRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DeletedRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="assemblyBuild"/>
 *     &lt;enumeration value="assemblyItem"/>
 *     &lt;enumeration value="assemblyUnbuild"/>
 *     &lt;enumeration value="billingSchedule"/>
 *     &lt;enumeration value="bin"/>
 *     &lt;enumeration value="binTransfer"/>
 *     &lt;enumeration value="binWorksheet"/>
 *     &lt;enumeration value="calendarEvent"/>
 *     &lt;enumeration value="campaign"/>
 *     &lt;enumeration value="cashRefund"/>
 *     &lt;enumeration value="cashSale"/>
 *     &lt;enumeration value="charge"/>
 *     &lt;enumeration value="check"/>
 *     &lt;enumeration value="contact"/>
 *     &lt;enumeration value="contactCategory"/>
 *     &lt;enumeration value="costCategory"/>
 *     &lt;enumeration value="couponCode"/>
 *     &lt;enumeration value="creditMemo"/>
 *     &lt;enumeration value="currencyRate"/>
 *     &lt;enumeration value="customRecord"/>
 *     &lt;enumeration value="customer"/>
 *     &lt;enumeration value="customerCategory"/>
 *     &lt;enumeration value="customerMessage"/>
 *     &lt;enumeration value="customerDeposit"/>
 *     &lt;enumeration value="customerPayment"/>
 *     &lt;enumeration value="customerRefund"/>
 *     &lt;enumeration value="customerStatus"/>
 *     &lt;enumeration value="deposit"/>
 *     &lt;enumeration value="depositApplication"/>
 *     &lt;enumeration value="descriptionItem"/>
 *     &lt;enumeration value="discountItem"/>
 *     &lt;enumeration value="downloadItem"/>
 *     &lt;enumeration value="employee"/>
 *     &lt;enumeration value="estimate"/>
 *     &lt;enumeration value="expenseReport"/>
 *     &lt;enumeration value="file"/>
 *     &lt;enumeration value="folder"/>
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
 *     &lt;enumeration value="issue"/>
 *     &lt;enumeration value="itemAccountMapping"/>
 *     &lt;enumeration value="itemDemandPlan"/>
 *     &lt;enumeration value="itemFulfillment"/>
 *     &lt;enumeration value="itemSupplyPlan"/>
 *     &lt;enumeration value="itemGroup"/>
 *     &lt;enumeration value="itemReceipt"/>
 *     &lt;enumeration value="itemRevision"/>
 *     &lt;enumeration value="job"/>
 *     &lt;enumeration value="jobStatus"/>
 *     &lt;enumeration value="journalEntry"/>
 *     &lt;enumeration value="kitItem"/>
 *     &lt;enumeration value="lotNumberedAssemblyItem"/>
 *     &lt;enumeration value="lotNumberedInventoryItem"/>
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
 *     &lt;enumeration value="otherNameCategory"/>
 *     &lt;enumeration value="partner"/>
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
 *     &lt;enumeration value="salesOrder"/>
 *     &lt;enumeration value="salesTaxItem"/>
 *     &lt;enumeration value="serializedAssemblyItem"/>
 *     &lt;enumeration value="serializedInventoryItem"/>
 *     &lt;enumeration value="servicePurchaseItem"/>
 *     &lt;enumeration value="serviceResaleItem"/>
 *     &lt;enumeration value="serviceSaleItem"/>
 *     &lt;enumeration value="statisticalJournalEntry"/>
 *     &lt;enumeration value="subtotalItem"/>
 *     &lt;enumeration value="supportCase"/>
 *     &lt;enumeration value="supportCaseIssue"/>
 *     &lt;enumeration value="supportCaseOrigin"/>
 *     &lt;enumeration value="supportCasePriority"/>
 *     &lt;enumeration value="supportCaseStatus"/>
 *     &lt;enumeration value="supportCaseType"/>
 *     &lt;enumeration value="task"/>
 *     &lt;enumeration value="term"/>
 *     &lt;enumeration value="timeSheet"/>
 *     &lt;enumeration value="transferOrder"/>
 *     &lt;enumeration value="vendor"/>
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
@XmlType(name = "DeletedRecordType", namespace = "urn:types.core_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum DeletedRecordType {

    @XmlEnumValue("assemblyBuild")
    ASSEMBLY_BUILD("assemblyBuild"),
    @XmlEnumValue("assemblyItem")
    ASSEMBLY_ITEM("assemblyItem"),
    @XmlEnumValue("assemblyUnbuild")
    ASSEMBLY_UNBUILD("assemblyUnbuild"),
    @XmlEnumValue("billingSchedule")
    BILLING_SCHEDULE("billingSchedule"),
    @XmlEnumValue("bin")
    BIN("bin"),
    @XmlEnumValue("binTransfer")
    BIN_TRANSFER("binTransfer"),
    @XmlEnumValue("binWorksheet")
    BIN_WORKSHEET("binWorksheet"),
    @XmlEnumValue("calendarEvent")
    CALENDAR_EVENT("calendarEvent"),
    @XmlEnumValue("campaign")
    CAMPAIGN("campaign"),
    @XmlEnumValue("cashRefund")
    CASH_REFUND("cashRefund"),
    @XmlEnumValue("cashSale")
    CASH_SALE("cashSale"),
    @XmlEnumValue("charge")
    CHARGE("charge"),
    @XmlEnumValue("check")
    CHECK("check"),
    @XmlEnumValue("contact")
    CONTACT("contact"),
    @XmlEnumValue("contactCategory")
    CONTACT_CATEGORY("contactCategory"),
    @XmlEnumValue("costCategory")
    COST_CATEGORY("costCategory"),
    @XmlEnumValue("couponCode")
    COUPON_CODE("couponCode"),
    @XmlEnumValue("creditMemo")
    CREDIT_MEMO("creditMemo"),
    @XmlEnumValue("currencyRate")
    CURRENCY_RATE("currencyRate"),
    @XmlEnumValue("customRecord")
    CUSTOM_RECORD("customRecord"),
    @XmlEnumValue("customer")
    CUSTOMER("customer"),
    @XmlEnumValue("customerCategory")
    CUSTOMER_CATEGORY("customerCategory"),
    @XmlEnumValue("customerMessage")
    CUSTOMER_MESSAGE("customerMessage"),
    @XmlEnumValue("customerDeposit")
    CUSTOMER_DEPOSIT("customerDeposit"),
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
    @XmlEnumValue("descriptionItem")
    DESCRIPTION_ITEM("descriptionItem"),
    @XmlEnumValue("discountItem")
    DISCOUNT_ITEM("discountItem"),
    @XmlEnumValue("downloadItem")
    DOWNLOAD_ITEM("downloadItem"),
    @XmlEnumValue("employee")
    EMPLOYEE("employee"),
    @XmlEnumValue("estimate")
    ESTIMATE("estimate"),
    @XmlEnumValue("expenseReport")
    EXPENSE_REPORT("expenseReport"),
    @XmlEnumValue("file")
    FILE("file"),
    @XmlEnumValue("folder")
    FOLDER("folder"),
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
    @XmlEnumValue("issue")
    ISSUE("issue"),
    @XmlEnumValue("itemAccountMapping")
    ITEM_ACCOUNT_MAPPING("itemAccountMapping"),
    @XmlEnumValue("itemDemandPlan")
    ITEM_DEMAND_PLAN("itemDemandPlan"),
    @XmlEnumValue("itemFulfillment")
    ITEM_FULFILLMENT("itemFulfillment"),
    @XmlEnumValue("itemSupplyPlan")
    ITEM_SUPPLY_PLAN("itemSupplyPlan"),
    @XmlEnumValue("itemGroup")
    ITEM_GROUP("itemGroup"),
    @XmlEnumValue("itemReceipt")
    ITEM_RECEIPT("itemReceipt"),
    @XmlEnumValue("itemRevision")
    ITEM_REVISION("itemRevision"),
    @XmlEnumValue("job")
    JOB("job"),
    @XmlEnumValue("jobStatus")
    JOB_STATUS("jobStatus"),
    @XmlEnumValue("journalEntry")
    JOURNAL_ENTRY("journalEntry"),
    @XmlEnumValue("kitItem")
    KIT_ITEM("kitItem"),
    @XmlEnumValue("lotNumberedAssemblyItem")
    LOT_NUMBERED_ASSEMBLY_ITEM("lotNumberedAssemblyItem"),
    @XmlEnumValue("lotNumberedInventoryItem")
    LOT_NUMBERED_INVENTORY_ITEM("lotNumberedInventoryItem"),
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
    @XmlEnumValue("otherNameCategory")
    OTHER_NAME_CATEGORY("otherNameCategory"),
    @XmlEnumValue("partner")
    PARTNER("partner"),
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
    @XmlEnumValue("salesOrder")
    SALES_ORDER("salesOrder"),
    @XmlEnumValue("salesTaxItem")
    SALES_TAX_ITEM("salesTaxItem"),
    @XmlEnumValue("serializedAssemblyItem")
    SERIALIZED_ASSEMBLY_ITEM("serializedAssemblyItem"),
    @XmlEnumValue("serializedInventoryItem")
    SERIALIZED_INVENTORY_ITEM("serializedInventoryItem"),
    @XmlEnumValue("servicePurchaseItem")
    SERVICE_PURCHASE_ITEM("servicePurchaseItem"),
    @XmlEnumValue("serviceResaleItem")
    SERVICE_RESALE_ITEM("serviceResaleItem"),
    @XmlEnumValue("serviceSaleItem")
    SERVICE_SALE_ITEM("serviceSaleItem"),
    @XmlEnumValue("statisticalJournalEntry")
    STATISTICAL_JOURNAL_ENTRY("statisticalJournalEntry"),
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
    @XmlEnumValue("term")
    TERM("term"),
    @XmlEnumValue("timeSheet")
    TIME_SHEET("timeSheet"),
    @XmlEnumValue("transferOrder")
    TRANSFER_ORDER("transferOrder"),
    @XmlEnumValue("vendor")
    VENDOR("vendor"),
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

    DeletedRecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeletedRecordType fromValue(String v) {
        for (DeletedRecordType c: DeletedRecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
