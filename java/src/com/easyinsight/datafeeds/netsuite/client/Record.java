
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nullFieldList" type="{urn:core_2014_1.platform.webservices.netsuite.com}NullField" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Record", namespace = "urn:core_2014_1.platform.webservices.netsuite.com", propOrder = {
    "nullFieldList"
})
@XmlSeeAlso({
    LandedCost.class,
    InventoryDetail.class,
    ResourceAllocation.class,
    ProjectTask.class,
    Task.class,
    PhoneCall.class,
    CalendarEvent.class,
    Message.class,
    Note.class,
    Folder.class,
    File.class,
    Customer.class,
    Contact.class,
    JobStatus.class,
    EntityGroup.class,
    CustomerStatus.class,
    Job.class,
    JobType.class,
    Vendor.class,
    Partner.class,
    SupportCaseIssue.class,
    SupportCasePriority.class,
    SupportCase.class,
    SupportCaseOrigin.class,
    Issue.class,
    SupportCaseType.class,
    Topic.class,
    Solution.class,
    SupportCaseStatus.class,
    NonInventoryPurchaseItem.class,
    SalesRole.class,
    LeadSource.class,
    OtherChargeResaleItem.class,
    ServicePurchaseItem.class,
    Classification.class,
    Currency.class,
    NonInventorySaleItem.class,
    ContactRole.class,
    Nexus.class,
    CostCategory.class,
    SerializedInventoryItem.class,
    LotNumberedAssemblyItem.class,
    AssemblyItem.class,
    Term.class,
    PriceLevel.class,
    SerializedAssemblyItem.class,
    Bin.class,
    CustomerCategory.class,
    BillingSchedule.class,
    ContactCategory.class,
    KitItem.class,
    SalesTaxItem.class,
    BudgetCategory.class,
    RevRecSchedule.class,
    WinLossReason.class,
    ItemRevision.class,
    MarkupItem.class,
    InventoryNumber.class,
    Subsidiary.class,
    AccountingPeriod.class,
    GiftCertificateItem.class,
    PaymentItem.class,
    ServiceResaleItem.class,
    NoteType.class,
    TaxAcct.class,
    PricingGroup.class,
    ItemGroup.class,
    GlobalAccountMapping.class,
    ServiceSaleItem.class,
    PartnerCategory.class,
    CustomerMessage.class,
    DiscountItem.class,
    CurrencyRate.class,
    ItemAccountMapping.class,
    GiftCertificate.class,
    PaymentMethod.class,
    TaxGroup.class,
    LotNumberedInventoryItem.class,
    TaxType.class,
    DownloadItem.class,
    InventoryItem.class,
    RevRecTemplate.class,
    Account.class,
    State.class,
    VendorCategory.class,
    OtherNameCategory.class,
    OtherChargeSaleItem.class,
    Department.class,
    NonInventoryResaleItem.class,
    DescriptionItem.class,
    Location.class,
    UnitsType.class,
    ExpenseCategory.class,
    OtherChargePurchaseItem.class,
    SubtotalItem.class,
    Estimate.class,
    ItemFulfillment.class,
    SalesOrder.class,
    Opportunity.class,
    CashSale.class,
    Invoice.class,
    VendorBill.class,
    VendorCredit.class,
    VendorReturnAuthorization.class,
    ItemReceipt.class,
    VendorPayment.class,
    PurchaseOrder.class,
    Charge.class,
    CreditMemo.class,
    ReturnAuthorization.class,
    DepositApplication.class,
    CustomerDeposit.class,
    CashRefund.class,
    CustomerPayment.class,
    CustomerRefund.class,
    Budget.class,
    Check.class,
    Deposit.class,
    AssemblyUnbuild.class,
    WorkOrderClose.class,
    AssemblyBuild.class,
    InventoryCostRevaluation.class,
    BinWorksheet.class,
    WorkOrderCompletion.class,
    TransferOrder.class,
    WorkOrderIssue.class,
    BinTransfer.class,
    InventoryAdjustment.class,
    InterCompanyTransferOrder.class,
    WorkOrder.class,
    InventoryTransfer.class,
    InterCompanyJournalEntry.class,
    StatisticalJournalEntry.class,
    JournalEntry.class,
    CustomRecordType.class,
    AppPackage.class,
    CustomRecord.class,
    CustomList.class,
    AppDefinition.class,
    CustomFieldType.class,
    PayrollItem.class,
    Employee.class,
    SiteCategory.class,
    TimeEntry.class,
    ExpenseReport.class,
    PaycheckJournal.class,
    TimeBill.class,
    TimeSheet.class,
    CampaignResponse.class,
    PromotionCode.class,
    CampaignChannel.class,
    CampaignOffer.class,
    CouponCode.class,
    CampaignVertical.class,
    CampaignCategory.class,
    Campaign.class,
    CampaignSearchEngine.class,
    CampaignAudience.class,
    CampaignFamily.class,
    CampaignSubscription.class,
    ItemSupplyPlan.class,
    ItemDemandPlan.class,
    ManufacturingCostTemplate.class,
    ManufacturingRouting.class,
    ManufacturingOperationTask.class
})
public abstract class Record {

    protected NullField nullFieldList;

    /**
     * Gets the value of the nullFieldList property.
     * 
     * @return
     *     possible object is
     *     {@link NullField }
     *     
     */
    public NullField getNullFieldList() {
        return nullFieldList;
    }

    /**
     * Sets the value of the nullFieldList property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullField }
     *     
     */
    public void setNullFieldList(NullField value) {
        this.nullFieldList = value;
    }

}
