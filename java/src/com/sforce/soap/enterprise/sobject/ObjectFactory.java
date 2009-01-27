
package com.sforce.soap.enterprise.sobject;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import com.sforce.soap.enterprise.QueryResult;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sforce.soap.enterprise.sobject package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EventReminderDateTime_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ReminderDateTime");
    private final static QName _EventIsRecurrence_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsRecurrence");
    private final static QName _EventAccountId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountId");
    private final static QName _EventSystemModstamp_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SystemModstamp");
    private final static QName _EventAttachments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Attachments");
    private final static QName _EventCreatedById_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CreatedById");
    private final static QName _EventIsDeleted_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsDeleted");
    private final static QName _EventRecurrenceActivityId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceActivityId");
    private final static QName _EventRecurrenceDayOfWeekMask_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceDayOfWeekMask");
    private final static QName _EventIsArchived_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsArchived");
    private final static QName _EventCreatedBy_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CreatedBy");
    private final static QName _EventIsAllDayEvent_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsAllDayEvent");
    private final static QName _EventLastModifiedById_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastModifiedById");
    private final static QName _EventIsChild_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsChild");
    private final static QName _EventIsGroupEvent_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsGroupEvent");
    private final static QName _EventWho_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Who");
    private final static QName _EventWhat_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "What");
    private final static QName _EventRecurrenceEndDateOnly_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceEndDateOnly");
    private final static QName _EventRecurrenceInterval_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceInterval");
    private final static QName _EventLastModifiedBy_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastModifiedBy");
    private final static QName _EventRecurrenceType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceType");
    private final static QName _EventRecurrenceDayOfMonth_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceDayOfMonth");
    private final static QName _EventWhatId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WhatId");
    private final static QName _EventLastModifiedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastModifiedDate");
    private final static QName _EventRecurrenceMonthOfYear_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceMonthOfYear");
    private final static QName _EventRecurrenceTimeZoneSidKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceTimeZoneSidKey");
    private final static QName _EventRecurrenceStartDateTime_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceStartDateTime");
    private final static QName _EventActivityDateTime_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivityDateTime");
    private final static QName _EventActivityDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivityDate");
    private final static QName _EventSubject_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Subject");
    private final static QName _EventIsReminderSet_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsReminderSet");
    private final static QName _EventRecurringEvents_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurringEvents");
    private final static QName _EventDescription_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Description");
    private final static QName _EventWhoId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WhoId");
    private final static QName _EventIsPrivate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsPrivate");
    private final static QName _EventShowAs_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShowAs");
    private final static QName _EventRecurrenceInstance_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecurrenceInstance");
    private final static QName _EventOwnerId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OwnerId");
    private final static QName _EventDurationInMinutes_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DurationInMinutes");
    private final static QName _EventLocation_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Location");
    private final static QName _EventCreatedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CreatedDate");
    private final static QName _EventOwner_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Owner");
    private final static QName _BusinessProcessIsActive_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsActive");
    private final static QName _BusinessProcessName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Name");
    private final static QName _BusinessProcessTableEnumOrId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TableEnumOrId");
    private final static QName _AccountAnnualRevenue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AnnualRevenue");
    private final static QName _AccountShares_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Shares");
    private final static QName _AccountBillingCountry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BillingCountry");
    private final static QName _AccountTasks_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Tasks");
    private final static QName _AccountMasterRecordId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MasterRecordId");
    private final static QName _AccountActivityHistories_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivityHistories");
    private final static QName _AccountNotesAndAttachments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NotesAndAttachments");
    private final static QName _AccountTickerSymbol_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TickerSymbol");
    private final static QName _AccountPartnersTo_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PartnersTo");
    private final static QName _AccountBillingPostalCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BillingPostalCode");
    private final static QName _AccountBillingState_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BillingState");
    private final static QName _AccountAccountPartnersFrom_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountPartnersFrom");
    private final static QName _AccountShippingState_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShippingState");
    private final static QName _AccountLastActivityDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastActivityDate");
    private final static QName _AccountProcessInstances_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProcessInstances");
    private final static QName _AccountOpenActivities_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpenActivities");
    private final static QName _AccountSLASerialNumberC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SLASerialNumber__c");
    private final static QName _AccountShippingPostalCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShippingPostalCode");
    private final static QName _AccountShippingCountry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShippingCountry");
    private final static QName _AccountOwnership_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Ownership");
    private final static QName _AccountPhone_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Phone");
    private final static QName _AccountParent_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Parent");
    private final static QName _AccountSite_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Site");
    private final static QName _AccountSLAExpirationDateC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SLAExpirationDate__c");
    private final static QName _AccountNotes_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Notes");
    private final static QName _AccountAssets_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Assets");
    private final static QName _AccountAccountContactRoles_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountContactRoles");
    private final static QName _AccountEvents_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Events");
    private final static QName _AccountFax_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Fax");
    private final static QName _AccountContacts_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Contacts");
    private final static QName _AccountNumberOfEmployees_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfEmployees");
    private final static QName _AccountContracts_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Contracts");
    private final static QName _AccountAccountPartnersTo_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountPartnersTo");
    private final static QName _AccountSic_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Sic");
    private final static QName _AccountParentId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ParentId");
    private final static QName _AccountWebsite_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Website");
    private final static QName _AccountShippingCity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShippingCity");
    private final static QName _AccountExternalIdC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "External_Id__c");
    private final static QName _AccountType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Type");
    private final static QName _AccountAccountTeamMembers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountTeamMembers");
    private final static QName _AccountNumberofLocationsC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberofLocations__c");
    private final static QName _AccountPartnersFrom_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PartnersFrom");
    private final static QName _AccountProcessSteps_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProcessSteps");
    private final static QName _AccountOpportunities_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Opportunities");
    private final static QName _AccountSLAC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SLA__c");
    private final static QName _AccountCases_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Cases");
    private final static QName _AccountRating_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Rating");
    private final static QName _AccountIndustry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Industry");
    private final static QName _AccountCustomerPriorityC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CustomerPriority__c");
    private final static QName _AccountUpsellOpportunityC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UpsellOpportunity__c");
    private final static QName _AccountShippingStreet_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShippingStreet");
    private final static QName _AccountAccountNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountNumber");
    private final static QName _AccountBillingCity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BillingCity");
    private final static QName _AccountActiveC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Active__c");
    private final static QName _AccountBillingStreet_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BillingStreet");
    private final static QName _AccountOpportunityPartnersTo_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityPartnersTo");
    private final static QName _PeriodQuarterLabel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "QuarterLabel");
    private final static QName _PeriodPeriodLabel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PeriodLabel");
    private final static QName _PeriodEndDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EndDate");
    private final static QName _PeriodStartDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "StartDate");
    private final static QName _PeriodNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Number");
    private final static QName _PeriodIsForecastPeriod_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsForecastPeriod");
    private final static QName _PeriodFiscalYearSettingsId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FiscalYearSettingsId");
    private final static QName _AccountContactRoleAccount_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Account");
    private final static QName _AccountContactRoleContact_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Contact");
    private final static QName _AccountContactRoleRole_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Role");
    private final static QName _AccountContactRoleContactId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContactId");
    private final static QName _AccountContactRoleIsPrimary_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsPrimary");
    private final static QName _AttachmentBody_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Body");
    private final static QName _AttachmentBodyLength_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BodyLength");
    private final static QName _AttachmentContentType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContentType");
    private final static QName _SolutionStatusMasterLabel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MasterLabel");
    private final static QName _SolutionStatusSortOrder_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SortOrder");
    private final static QName _SolutionStatusIsReviewed_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsReviewed");
    private final static QName _SolutionStatusIsDefault_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsDefault");
    private final static QName _ContactOtherState_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OtherState");
    private final static QName _ContactReportsTo_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ReportsTo");
    private final static QName _ContactOtherStreet_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OtherStreet");
    private final static QName _ContactFirstName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FirstName");
    private final static QName _ContactLevelC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Level__c");
    private final static QName _ContactEmail_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Email");
    private final static QName _ContactLastCURequestDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastCURequestDate");
    private final static QName _ContactDepartment_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Department");
    private final static QName _ContactOtherCountry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OtherCountry");
    private final static QName _ContactBirthdate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Birthdate");
    private final static QName _ContactContractsSigned_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContractsSigned");
    private final static QName _ContactMobilePhone_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MobilePhone");
    private final static QName _ContactMailingPostalCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MailingPostalCode");
    private final static QName _ContactMailingStreet_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MailingStreet");
    private final static QName _ContactMailingCountry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MailingCountry");
    private final static QName _ContactRecordTypeId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecordTypeId");
    private final static QName _ContactAssistantName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AssistantName");
    private final static QName _ContactOtherPhone_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OtherPhone");
    private final static QName _ContactReportsToId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ReportsToId");
    private final static QName _ContactRecordType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RecordType");
    private final static QName _ContactTitle_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Title");
    private final static QName _ContactOtherCity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OtherCity");
    private final static QName _ContactLeadSource_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LeadSource");
    private final static QName _ContactLanguagesC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Languages__c");
    private final static QName _ContactCaseContactRoles_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseContactRoles");
    private final static QName _ContactMailingState_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MailingState");
    private final static QName _ContactLastName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastName");
    private final static QName _ContactEmailStatuses_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EmailStatuses");
    private final static QName _ContactAssistantPhone_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AssistantPhone");
    private final static QName _ContactContractContactRoles_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContractContactRoles");
    private final static QName _ContactSalutation_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Salutation");
    private final static QName _ContactOtherPostalCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OtherPostalCode");
    private final static QName _ContactMailingCity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MailingCity");
    private final static QName _ContactLastCUUpdateDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastCUUpdateDate");
    private final static QName _ContactHomePhone_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HomePhone");
    private final static QName _ContactCampaignMembers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CampaignMembers");
    private final static QName _ContactOpportunityContactRoles_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityContactRoles");
    private final static QName _NoteAndAttachmentIsNote_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsNote");
    private final static QName _ContractHistoryNewValue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NewValue");
    private final static QName _ContractHistoryOldValue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OldValue");
    private final static QName _ContractHistoryContract_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Contract");
    private final static QName _ContractHistoryContractId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContractId");
    private final static QName _ContractHistoryField_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Field");
    private final static QName _OpportunityCompetitorStrengths_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Strengths");
    private final static QName _OpportunityCompetitorOpportunity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Opportunity");
    private final static QName _OpportunityCompetitorWeaknesses_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Weaknesses");
    private final static QName _OpportunityCompetitorOpportunityId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityId");
    private final static QName _OpportunityCompetitorCompetitorName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CompetitorName");
    private final static QName _EmailTemplateFolderId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FolderId");
    private final static QName _EmailTemplateLastUsedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastUsedDate");
    private final static QName _EmailTemplateBrandTemplateId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BrandTemplateId");
    private final static QName _EmailTemplateHtmlValue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HtmlValue");
    private final static QName _EmailTemplateTimesUsed_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TimesUsed");
    private final static QName _EmailTemplateTemplateStyle_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TemplateStyle");
    private final static QName _EmailTemplateEncoding_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Encoding");
    private final static QName _EmailTemplateTemplateType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TemplateType");
    private final static QName _EmailTemplateFolder_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Folder");
    private final static QName _PartnerRoleReverseRole_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ReverseRole");
    private final static QName _WebLinkNamespacePrefix_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NamespacePrefix");
    private final static QName _WebLinkRequireRowSelection_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RequireRowSelection");
    private final static QName _WebLinkShowsStatus_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShowsStatus");
    private final static QName _WebLinkHasScrollbars_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasScrollbars");
    private final static QName _WebLinkPosition_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Position");
    private final static QName _WebLinkPageOrSobjectType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PageOrSobjectType");
    private final static QName _WebLinkDisplayType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DisplayType");
    private final static QName _WebLinkScontrolId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ScontrolId");
    private final static QName _WebLinkShowsLocation_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ShowsLocation");
    private final static QName _WebLinkUrl_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Url");
    private final static QName _WebLinkOpenType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpenType");
    private final static QName _WebLinkLinkType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LinkType");
    private final static QName _WebLinkHeight_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Height");
    private final static QName _WebLinkWidth_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Width");
    private final static QName _WebLinkHasMenubar_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasMenubar");
    private final static QName _WebLinkIsResizable_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsResizable");
    private final static QName _WebLinkHasToolbar_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasToolbar");
    private final static QName _WebLinkEncodingKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EncodingKey");
    private final static QName _CampaignBudgetedCost_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BudgetedCost");
    private final static QName _CampaignActualCost_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActualCost");
    private final static QName _CampaignNumberOfOpportunities_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfOpportunities");
    private final static QName _CampaignAmountWonOpportunities_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AmountWonOpportunities");
    private final static QName _CampaignNumberOfContacts_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfContacts");
    private final static QName _CampaignAmountAllOpportunities_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AmountAllOpportunities");
    private final static QName _CampaignLeads_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Leads");
    private final static QName _CampaignNumberOfResponses_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfResponses");
    private final static QName _CampaignNumberOfWonOpportunities_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfWonOpportunities");
    private final static QName _CampaignNumberOfConvertedLeads_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfConvertedLeads");
    private final static QName _CampaignExpectedResponse_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ExpectedResponse");
    private final static QName _CampaignNumberOfLeads_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberOfLeads");
    private final static QName _CampaignNumberSent_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NumberSent");
    private final static QName _CampaignExpectedRevenue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ExpectedRevenue");
    private final static QName _CampaignStatus_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Status");
    private final static QName _ProcessInstanceStepProcessInstance_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProcessInstance");
    private final static QName _ProcessInstanceStepActor_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Actor");
    private final static QName _ProcessInstanceStepStepStatus_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "StepStatus");
    private final static QName _ProcessInstanceStepActorId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActorId");
    private final static QName _ProcessInstanceStepOriginalActor_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OriginalActor");
    private final static QName _ProcessInstanceStepComments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Comments");
    private final static QName _ProcessInstanceStepOriginalActorId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OriginalActorId");
    private final static QName _ProcessInstanceStepProcessInstanceId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProcessInstanceId");
    private final static QName _OpportunityHistoryAmount_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Amount");
    private final static QName _OpportunityHistoryForecastCategory_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ForecastCategory");
    private final static QName _OpportunityHistoryStageName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "StageName");
    private final static QName _OpportunityHistoryProbability_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Probability");
    private final static QName _OpportunityHistoryCloseDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CloseDate");
    private final static QName _ProcessInstanceHistoryTargetObject_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TargetObject");
    private final static QName _ProcessInstanceHistoryRemindersSent_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RemindersSent");
    private final static QName _ProcessInstanceHistoryTargetObjectId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TargetObjectId");
    private final static QName _ProcessInstanceHistoryIsPending_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsPending");
    private final static QName _SolutionHistorySolution_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Solution");
    private final static QName _SolutionHistorySolutionId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SolutionId");
    private final static QName _UserRoleRollupDescription_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RollupDescription");
    private final static QName _UserRoleParentRoleId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ParentRoleId");
    private final static QName _UserRoleUsers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Users");
    private final static QName _UserRoleOpportunityAccessForAccountOwner_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityAccessForAccountOwner");
    private final static QName _UserRoleCaseAccessForAccountOwner_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseAccessForAccountOwner");
    private final static QName _OpportunityTeamMemberUserId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserId");
    private final static QName _OpportunityTeamMemberUser_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "User");
    private final static QName _OpportunityTeamMemberOpportunityAccessLevel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityAccessLevel");
    private final static QName _OpportunityTeamMemberTeamMemberRole_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TeamMemberRole");
    private final static QName _AccountPartnerAccountToId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountToId");
    private final static QName _AccountPartnerAccountFromId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountFromId");
    private final static QName _AccountPartnerAccountTo_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountTo");
    private final static QName _AccountPartnerAccountFrom_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountFrom");
    private final static QName _ApexPackageIdentifierArgumentArgumentType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ArgumentType");
    private final static QName _ApexPackageIdentifierArgumentValueType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ValueType");
    private final static QName _ApexPackageIdentifierArgumentArgumentNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ArgumentNumber");
    private final static QName _ApexPackageIdentifierArgumentArgumentName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ArgumentName");
    private final static QName _ApexPackageIdentifierArgumentApexPackageIdentifierId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ApexPackageIdentifierId");
    private final static QName _ApexPackageIdentifierArgumentCollectionType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CollectionType");
    private final static QName _ApexPackageIdentifierArgumentApexPackageIdentifier_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ApexPackageIdentifier");
    private final static QName _ApexPackageIdentifierArgumentApexPackageId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ApexPackageId");
    private final static QName _MailmergeTemplateFilename_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Filename");
    private final static QName _LeadStatusIsConverted_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsConverted");
    private final static QName _NameAlias_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Alias");
    private final static QName _NameUserRoleId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserRoleId");
    private final static QName _NameUserRole_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserRole");
    private final static QName _SelfServiceUserLanguageLocaleKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LanguageLocaleKey");
    private final static QName _SelfServiceUserUsername_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Username");
    private final static QName _SelfServiceUserLastLoginDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastLoginDate");
    private final static QName _SelfServiceUserTimeZoneSidKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TimeZoneSidKey");
    private final static QName _SelfServiceUserSuperUser_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SuperUser");
    private final static QName _SelfServiceUserLocaleSidKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LocaleSidKey");
    private final static QName _ApprovalApproveComment_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ApproveComment");
    private final static QName _ApprovalRequestComment_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RequestComment");
    private final static QName _GroupMemberGroupId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "GroupId");
    private final static QName _GroupMemberUserOrGroupId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserOrGroupId");
    private final static QName _GroupMemberGroup_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Group");
    private final static QName _Pricebook2IsStandard_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsStandard");
    private final static QName _Pricebook2PricebookEntries_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PricebookEntries");
    private final static QName _OrganizationStreet_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Street");
    private final static QName _OrganizationDefaultCalendarAccess_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultCalendarAccess");
    private final static QName _OrganizationUsesStartDateAsFiscalYearName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsesStartDateAsFiscalYearName");
    private final static QName _OrganizationReceivesAdminInfoEmails_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ReceivesAdminInfoEmails");
    private final static QName _OrganizationPreferencesRequireOpportunityProducts_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PreferencesRequireOpportunityProducts");
    private final static QName _OrganizationOrganizationType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OrganizationType");
    private final static QName _OrganizationTrialExpirationDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TrialExpirationDate");
    private final static QName _OrganizationDefaultOpportunityAccess_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultOpportunityAccess");
    private final static QName _OrganizationDefaultCaseAccess_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultCaseAccess");
    private final static QName _OrganizationDefaultLeadAccess_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultLeadAccess");
    private final static QName _OrganizationReceivesInfoEmails_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ReceivesInfoEmails");
    private final static QName _OrganizationMaxRulesPerEntity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MaxRulesPerEntity");
    private final static QName _OrganizationPrimaryContact_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PrimaryContact");
    private final static QName _OrganizationWebToCaseDefaultOrigin_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WebToCaseDefaultOrigin");
    private final static QName _OrganizationDivision_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Division");
    private final static QName _OrganizationState_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "State");
    private final static QName _OrganizationDefaultLocaleSidKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultLocaleSidKey");
    private final static QName _OrganizationComplianceBccEmail_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ComplianceBccEmail");
    private final static QName _OrganizationPostalCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PostalCode");
    private final static QName _OrganizationDefaultAccountAndContactAccess_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultAccountAndContactAccess");
    private final static QName _OrganizationUiSkin_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UiSkin");
    private final static QName _OrganizationDefaultPricebookAccess_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultPricebookAccess");
    private final static QName _OrganizationCity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "City");
    private final static QName _OrganizationCountry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Country");
    private final static QName _OrganizationFiscalYearStartMonth_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FiscalYearStartMonth");
    private final static QName _OrganizationMaxActionsPerRule_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MaxActionsPerRule");
    private final static QName _OpportunityShareRowCause_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RowCause");
    private final static QName _QueueSobjectQueue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Queue");
    private final static QName _QueueSobjectSobjectType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SobjectType");
    private final static QName _QueueSobjectQueueId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "QueueId");
    private final static QName _ApexTriggerUsageAfterUpdate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageAfterUpdate");
    private final static QName _ApexTriggerUsageAfterInsert_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageAfterInsert");
    private final static QName _ApexTriggerUsageBeforeDelete_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageBeforeDelete");
    private final static QName _ApexTriggerBodyCrc_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BodyCrc");
    private final static QName _ApexTriggerUsageBeforeUpdate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageBeforeUpdate");
    private final static QName _ApexTriggerUsageAfterDelete_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageAfterDelete");
    private final static QName _ApexTriggerUsageBeforeInsert_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageBeforeInsert");
    private final static QName _ApexTriggerApiVersion_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ApiVersion");
    private final static QName _ApexTriggerIsValid_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsValid");
    private final static QName _ApexPackageMethods_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Methods");
    private final static QName _CategoryNodeSortStyle_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SortStyle");
    private final static QName _CaseCommentCommentBody_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CommentBody");
    private final static QName _CaseCommentIsPublished_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsPublished");
    private final static QName _ContractActivatedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivatedDate");
    private final static QName _ContractOwnerExpirationNotice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OwnerExpirationNotice");
    private final static QName _ContractActivatedBy_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivatedBy");
    private final static QName _ContractStatusCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "StatusCode");
    private final static QName _ContractCompanySignedId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CompanySignedId");
    private final static QName _ContractActivatedById_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivatedById");
    private final static QName _ContractCustomerSigned_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CustomerSigned");
    private final static QName _ContractCustomerSignedTitle_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CustomerSignedTitle");
    private final static QName _ContractSpecialTerms_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SpecialTerms");
    private final static QName _ContractContractTerm_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContractTerm");
    private final static QName _ContractCompanySigned_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CompanySigned");
    private final static QName _ContractCompanySignedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CompanySignedDate");
    private final static QName _ContractCustomerSignedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CustomerSignedDate");
    private final static QName _ContractHistories_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Histories");
    private final static QName _ContractLastApprovedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastApprovedDate");
    private final static QName _ContractCustomerSignedId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CustomerSignedId");
    private final static QName _ContractContractNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContractNumber");
    private final static QName _OpenActivityIsTask_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsTask");
    private final static QName _OpenActivityIsClosed_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsClosed");
    private final static QName _OpenActivityActivityType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ActivityType");
    private final static QName _OpenActivityPriority_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Priority");
    private final static QName _AccountShareAccountAccessLevel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountAccessLevel");
    private final static QName _AccountShareCaseAccessLevel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseAccessLevel");
    private final static QName _EventAttendeeRespondedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RespondedDate");
    private final static QName _EventAttendeeAttendeeId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AttendeeId");
    private final static QName _EventAttendeeEventId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EventId");
    private final static QName _EventAttendeeResponse_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Response");
    private final static QName _BusinessHoursSaturdayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SaturdayEnd");
    private final static QName _BusinessHoursWednesdayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WednesdayStart");
    private final static QName _BusinessHoursMondayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MondayEnd");
    private final static QName _BusinessHoursSundayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SundayEnd");
    private final static QName _BusinessHoursMondayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MondayStart");
    private final static QName _BusinessHoursFridayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FridayStart");
    private final static QName _BusinessHoursWednesdayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WednesdayEnd");
    private final static QName _BusinessHoursFridayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FridayEnd");
    private final static QName _BusinessHoursThursdayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ThursdayEnd");
    private final static QName _BusinessHoursTuesdayEnd_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TuesdayEnd");
    private final static QName _BusinessHoursSaturdayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SaturdayStart");
    private final static QName _BusinessHoursSundayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SundayStart");
    private final static QName _BusinessHoursTuesdayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TuesdayStart");
    private final static QName _BusinessHoursThursdayStart_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ThursdayStart");
    private final static QName _BrandTemplateValue_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Value");
    private final static QName _SObjectId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Id");
    private final static QName _CaseHistoryCase_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Case");
    private final static QName _CaseHistoryCaseId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseId");
    private final static QName _DocumentAttachmentMapDocumentSequence_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DocumentSequence");
    private final static QName _DocumentAttachmentMapDocumentId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DocumentId");
    private final static QName _ProcessInstanceStepsAndWorkitems_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "StepsAndWorkitems");
    private final static QName _ProcessInstanceSteps_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Steps");
    private final static QName _ProcessInstanceWorkitems_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Workitems");
    private final static QName _OpportunityStageDefaultProbability_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DefaultProbability");
    private final static QName _OpportunityStageIsWon_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsWon");
    private final static QName _SolutionSolutionName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SolutionName");
    private final static QName _SolutionIsPublishedInPublicKb_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsPublishedInPublicKb");
    private final static QName _SolutionCaseSolutions_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseSolutions");
    private final static QName _SolutionSolutionNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SolutionNumber");
    private final static QName _SolutionSolutionNote_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SolutionNote");
    private final static QName _UserEmployeeNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EmployeeNumber");
    private final static QName _UserUserPermissionsMarketingUser_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserPermissionsMarketingUser");
    private final static QName _UserUserTeams_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserTeams");
    private final static QName _UserProfileId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProfileId");
    private final static QName _UserEmailEncodingKey_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EmailEncodingKey");
    private final static QName _UserDelegatedUsers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DelegatedUsers");
    private final static QName _UserExtension_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Extension");
    private final static QName _UserCompanyName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CompanyName");
    private final static QName _UserUserPermissionsOfflineUser_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserPermissionsOfflineUser");
    private final static QName _UserUserAccountTeams_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UserAccountTeams");
    private final static QName _UserProfile_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Profile");
    private final static QName _UserAccountTeams_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountTeams");
    private final static QName _UserOfflinePdaTrialExpirationDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OfflinePdaTrialExpirationDate");
    private final static QName _UserOpportunityTeams_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityTeams");
    private final static QName _UserDelegatedApproverId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DelegatedApproverId");
    private final static QName _UserOfflineTrialExpirationDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OfflineTrialExpirationDate");
    private final static QName _OpportunityTotalOpportunityQuantity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TotalOpportunityQuantity");
    private final static QName _OpportunityFiscalYear_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FiscalYear");
    private final static QName _OpportunityFiscal_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Fiscal");
    private final static QName _OpportunityOpportunityHistories_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityHistories");
    private final static QName _OpportunityTrackingNumberC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TrackingNumber__c");
    private final static QName _OpportunityPricebook2_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Pricebook2");
    private final static QName _OpportunityOpportunityLineItems_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityLineItems");
    private final static QName _OpportunityOpportunityPartnersFrom_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityPartnersFrom");
    private final static QName _OpportunityMainCompetitorsC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "MainCompetitors__c");
    private final static QName _OpportunityOrderNumberC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OrderNumber__c");
    private final static QName _OpportunityCampaign_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Campaign");
    private final static QName _OpportunityCampaignId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CampaignId");
    private final static QName _OpportunityOpportunityTeamMembers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityTeamMembers");
    private final static QName _OpportunityNextStep_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "NextStep");
    private final static QName _OpportunityPricebook2Id_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Pricebook2Id");
    private final static QName _OpportunityAccountPartners_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccountPartners");
    private final static QName _OpportunityOpportunityCompetitors_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OpportunityCompetitors");
    private final static QName _OpportunityFiscalQuarter_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FiscalQuarter");
    private final static QName _OpportunityHasOpportunityLineItem_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasOpportunityLineItem");
    private final static QName _OpportunityDeliveryInstallationStatusC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DeliveryInstallationStatus__c");
    private final static QName _OpportunityPartners_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Partners");
    private final static QName _OpportunityCurrentGeneratorsC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CurrentGenerators__c");
    private final static QName _LeadHistoryLead_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Lead");
    private final static QName _LeadHistoryLeadId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LeadId");
    private final static QName _EmailStatusTaskId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TaskId");
    private final static QName _EmailStatusEmailTemplateName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EmailTemplateName");
    private final static QName _EmailStatusTimesOpened_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TimesOpened");
    private final static QName _EmailStatusTask_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Task");
    private final static QName _EmailStatusLastOpenDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LastOpenDate");
    private final static QName _EmailStatusFirstOpenDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FirstOpenDate");
    private final static QName _GroupQueueSobjects_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "QueueSobjects");
    private final static QName _GroupDoesSendEmailToMembers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DoesSendEmailToMembers");
    private final static QName _GroupRelatedId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RelatedId");
    private final static QName _GroupGroupMembers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "GroupMembers");
    private final static QName _FolderAccessType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccessType");
    private final static QName _FolderIsReadonly_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsReadonly");
    private final static QName _AssetIsCompetitorProduct_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsCompetitorProduct");
    private final static QName _AssetQuantity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Quantity");
    private final static QName _AssetProduct2Id_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Product2Id");
    private final static QName _AssetUsageEndDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UsageEndDate");
    private final static QName _AssetPrice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Price");
    private final static QName _AssetPurchaseDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PurchaseDate");
    private final static QName _AssetSerialNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SerialNumber");
    private final static QName _AssetProduct2_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Product2");
    private final static QName _AssetInstallDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "InstallDate");
    private final static QName _CampaignMemberHasResponded_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasResponded");
    private final static QName _CampaignMemberFirstRespondedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "FirstRespondedDate");
    private final static QName _ProfilePermissionsManageSelfService_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageSelfService");
    private final static QName _ProfilePermissionsEditTask_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditTask");
    private final static QName _ProfilePermissionsManageSolutions_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageSolutions");
    private final static QName _ProfilePermissionsInstallMultiforce_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsInstallMultiforce");
    private final static QName _ProfilePermissionsManageTerritories_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageTerritories");
    private final static QName _ProfilePermissionsPasswordNeverExpires_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsPasswordNeverExpires");
    private final static QName _ProfilePermissionsEditActivatedOrders_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditActivatedOrders");
    private final static QName _ProfilePermissionsManageCategories_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageCategories");
    private final static QName _ProfilePermissionsModifyAllData_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsModifyAllData");
    private final static QName _ProfilePermissionsCreateMultiforce_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsCreateMultiforce");
    private final static QName _ProfilePermissionsEditPublicDocuments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditPublicDocuments");
    private final static QName _ProfilePermissionsConvertLeads_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsConvertLeads");
    private final static QName _ProfilePermissionsCustomizeApplication_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsCustomizeApplication");
    private final static QName _ProfilePermissionsDisableNotifications_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsDisableNotifications");
    private final static QName _ProfilePermissionsSendSitRequests_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsSendSitRequests");
    private final static QName _ProfileLicenseType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LicenseType");
    private final static QName _ProfilePermissionsPublishMultiforce_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsPublishMultiforce");
    private final static QName _ProfilePermissionsSolutionImport_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsSolutionImport");
    private final static QName _ProfilePermissionsEditReadonlyFields_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditReadonlyFields");
    private final static QName _ProfilePermissionsManageCallCenters_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageCallCenters");
    private final static QName _ProfilePermissionsManageCases_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageCases");
    private final static QName _ProfilePermissionsEditForecast_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditForecast");
    private final static QName _ProfilePermissionsImportLeads_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsImportLeads");
    private final static QName _ProfilePermissionsIPRestrictRequests_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsIPRestrictRequests");
    private final static QName _ProfilePermissionsTransferAnyEntity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsTransferAnyEntity");
    private final static QName _ProfilePermissionsTransferAnyLead_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsTransferAnyLead");
    private final static QName _ProfilePermissionsUseTeamReassignWizards_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsUseTeamReassignWizards");
    private final static QName _ProfilePermissionsRunReports_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsRunReports");
    private final static QName _ProfilePermissionsAuthorApex_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsAuthorApex");
    private final static QName _ProfilePermissionsManageDashboards_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageDashboards");
    private final static QName _ProfilePermissionsEditOppLineItemUnitPrice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditOppLineItemUnitPrice");
    private final static QName _ProfilePermissionsManageLeads_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageLeads");
    private final static QName _ProfilePermissionsManageCssUsers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageCssUsers");
    private final static QName _ProfilePermissionsViewAllData_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsViewAllData");
    private final static QName _ProfilePermissionsViewSetup_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsViewSetup");
    private final static QName _ProfilePermissionsEditEvent_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditEvent");
    private final static QName _ProfilePermissionsManageUsers_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsManageUsers");
    private final static QName _ProfilePermissionsApiUserOnly_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsApiUserOnly");
    private final static QName _ProfilePermissionsEditReports_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PermissionsEditReports");
    private final static QName _RecordTypeBusinessProcessId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "BusinessProcessId");
    private final static QName _TestShareAccessLevel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AccessLevel");
    private final static QName _FiscalYearSettingsPeriodLabelScheme_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PeriodLabelScheme");
    private final static QName _FiscalYearSettingsPeriodId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PeriodId");
    private final static QName _FiscalYearSettingsWeekStartDay_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WeekStartDay");
    private final static QName _FiscalYearSettingsWeekLabelScheme_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "WeekLabelScheme");
    private final static QName _FiscalYearSettingsIsStandardYear_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsStandardYear");
    private final static QName _FiscalYearSettingsPeriodPrefix_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PeriodPrefix");
    private final static QName _FiscalYearSettingsQuarterPrefix_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "QuarterPrefix");
    private final static QName _FiscalYearSettingsQuarterLabelScheme_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "QuarterLabelScheme");
    private final static QName _FiscalYearSettingsYearType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "YearType");
    private final static QName _Product2ProductCode_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProductCode");
    private final static QName _Product2Family_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Family");
    private final static QName _ScontrolDeveloperName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "DeveloperName");
    private final static QName _ScontrolSupportsCaching_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SupportsCaching");
    private final static QName _ScontrolContentSource_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ContentSource");
    private final static QName _ScontrolBinary_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Binary");
    private final static QName _ScontrolHtmlWrapper_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HtmlWrapper");
    private final static QName _LeadShareLeadAccessLevel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LeadAccessLevel");
    private final static QName _CampaignMemberStatusLabel_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Label");
    private final static QName _ApexPackageIdentifierOptionsSystemDefined_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OptionsSystemDefined");
    private final static QName _ApexPackageIdentifierIdentifierName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IdentifierName");
    private final static QName _ApexPackageIdentifierOptionsFinalVariable_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OptionsFinalVariable");
    private final static QName _ApexPackageIdentifierOptionsTestMethod_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OptionsTestMethod");
    private final static QName _ApexPackageIdentifierArguments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Arguments");
    private final static QName _ApexPackageIdentifierApexPackage_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ApexPackage");
    private final static QName _ApexPackageIdentifierIdentifierType_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IdentifierType");
    private final static QName _ApexPackageIdentifierOptionsPrivateIdentifier_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OptionsPrivateIdentifier");
    private final static QName _ApexPackageIdentifierColumnNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ColumnNumber");
    private final static QName _ApexPackageIdentifierOptionsPublicIdentifier_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OptionsPublicIdentifier");
    private final static QName _ApexPackageIdentifierLineNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "LineNumber");
    private final static QName _ApexPackageIdentifierOptionsWebService_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "OptionsWebService");
    private final static QName _TaskPriorityIsHighPriority_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsHighPriority");
    private final static QName _CaseContactRoleCasesId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CasesId");
    private final static QName _AssignmentRuleActive_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Active");
    private final static QName _OpportunityLineItemPricebookEntry_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PricebookEntry");
    private final static QName _OpportunityLineItemTotalPrice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "TotalPrice");
    private final static QName _OpportunityLineItemPricebookEntryId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PricebookEntryId");
    private final static QName _OpportunityLineItemServiceDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ServiceDate");
    private final static QName _OpportunityLineItemListPrice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ListPrice");
    private final static QName _OpportunityLineItemUnitPrice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UnitPrice");
    private final static QName _LeadIsUnreadByOwner_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsUnreadByOwner");
    private final static QName _LeadConvertedContact_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedContact");
    private final static QName _LeadConvertedOpportunityId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedOpportunityId");
    private final static QName _LeadCompany_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Company");
    private final static QName _LeadConvertedAccount_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedAccount");
    private final static QName _LeadConvertedContactId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedContactId");
    private final static QName _LeadConvertedAccountId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedAccountId");
    private final static QName _LeadConvertedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedDate");
    private final static QName _LeadConvertedOpportunity_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ConvertedOpportunity");
    private final static QName _LeadPrimaryC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Primary__c");
    private final static QName _LeadSICCodeC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SICCode__c");
    private final static QName _LeadProductInterestC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ProductInterest__c");
    private final static QName _DocumentIsInternalUseOnly_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsInternalUseOnly");
    private final static QName _DocumentKeywords_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Keywords");
    private final static QName _DocumentAuthorId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AuthorId");
    private final static QName _DocumentAuthor_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Author");
    private final static QName _DocumentIsPublic_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsPublic");
    private final static QName _CaseReason_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Reason");
    private final static QName _CaseSLAViolationC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SLAViolation__c");
    private final static QName _CaseSolutions_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Solutions");
    private final static QName _CaseHasCommentsUnreadByOwner_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasCommentsUnreadByOwner");
    private final static QName _CaseAsset_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Asset");
    private final static QName _CaseSuppliedCompany_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SuppliedCompany");
    private final static QName _CaseSuppliedName_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SuppliedName");
    private final static QName _CaseHasSelfServiceComments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "HasSelfServiceComments");
    private final static QName _CaseIsEscalated_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "IsEscalated");
    private final static QName _CasePotentialLiabilityC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "PotentialLiability__c");
    private final static QName _CaseSuppliedEmail_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SuppliedEmail");
    private final static QName _CaseEngineeringReqNumberC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "EngineeringReqNumber__c");
    private final static QName _CaseClosedDate_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "ClosedDate");
    private final static QName _CaseProductC_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Product__c");
    private final static QName _CaseCaseNumber_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseNumber");
    private final static QName _CaseCaseComments_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CaseComments");
    private final static QName _CaseAssetId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "AssetId");
    private final static QName _CaseSuppliedPhone_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "SuppliedPhone");
    private final static QName _CaseOrigin_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "Origin");
    private final static QName _CategoryDataRelatedSobjectId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "RelatedSobjectId");
    private final static QName _CategoryDataCategoryNodeId_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "CategoryNodeId");
    private final static QName _PricebookEntryUseStandardPrice_QNAME = new QName("urn:sobject.enterprise.soap.sforce.com", "UseStandardPrice");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sforce.soap.enterprise.sobject
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Period }
     * 
     */
    public Period createPeriod() {
        return new Period();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link SolutionStatus }
     * 
     */
    public SolutionStatus createSolutionStatus() {
        return new SolutionStatus();
    }

    /**
     * Create an instance of {@link Contact }
     * 
     */
    public Contact createContact() {
        return new Contact();
    }

    /**
     * Create an instance of {@link PartnerRole }
     * 
     */
    public PartnerRole createPartnerRole() {
        return new PartnerRole();
    }

    /**
     * Create an instance of {@link ProcessInstanceStep }
     * 
     */
    public ProcessInstanceStep createProcessInstanceStep() {
        return new ProcessInstanceStep();
    }

    /**
     * Create an instance of {@link SolutionHistory }
     * 
     */
    public SolutionHistory createSolutionHistory() {
        return new SolutionHistory();
    }

    /**
     * Create an instance of {@link AccountPartner }
     * 
     */
    public AccountPartner createAccountPartner() {
        return new AccountPartner();
    }

    /**
     * Create an instance of {@link Partner }
     * 
     */
    public Partner createPartner() {
        return new Partner();
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link Pricebook2 }
     * 
     */
    public Pricebook2 createPricebook2() {
        return new Pricebook2();
    }

    /**
     * Create an instance of {@link OpportunityContactRole }
     * 
     */
    public OpportunityContactRole createOpportunityContactRole() {
        return new OpportunityContactRole();
    }

    /**
     * Create an instance of {@link Contract }
     * 
     */
    public Contract createContract() {
        return new Contract();
    }

    /**
     * Create an instance of {@link Task }
     * 
     */
    public Task createTask() {
        return new Task();
    }

    /**
     * Create an instance of {@link CaseHistory }
     * 
     */
    public CaseHistory createCaseHistory() {
        return new CaseHistory();
    }

    /**
     * Create an instance of {@link DocumentAttachmentMap }
     * 
     */
    public DocumentAttachmentMap createDocumentAttachmentMap() {
        return new DocumentAttachmentMap();
    }

    /**
     * Create an instance of {@link UserAccountTeamMember }
     * 
     */
    public UserAccountTeamMember createUserAccountTeamMember() {
        return new UserAccountTeamMember();
    }

    /**
     * Create an instance of {@link Note }
     * 
     */
    public Note createNote() {
        return new Note();
    }

    /**
     * Create an instance of {@link OpportunityStage }
     * 
     */
    public OpportunityStage createOpportunityStage() {
        return new OpportunityStage();
    }

    /**
     * Create an instance of {@link Solution }
     * 
     */
    public Solution createSolution() {
        return new Solution();
    }

    /**
     * Create an instance of {@link LeadHistory }
     * 
     */
    public LeadHistory createLeadHistory() {
        return new LeadHistory();
    }

    /**
     * Create an instance of {@link EmailStatus }
     * 
     */
    public EmailStatus createEmailStatus() {
        return new EmailStatus();
    }

    /**
     * Create an instance of {@link ContractContactRole }
     * 
     */
    public ContractContactRole createContractContactRole() {
        return new ContractContactRole();
    }

    /**
     * Create an instance of {@link TestShare }
     * 
     */
    public TestShare createTestShare() {
        return new TestShare();
    }

    /**
     * Create an instance of {@link Product2 }
     * 
     */
    public Product2 createProduct2() {
        return new Product2();
    }

    /**
     * Create an instance of {@link Scontrol }
     * 
     */
    public Scontrol createScontrol() {
        return new Scontrol();
    }

    /**
     * Create an instance of {@link Lead }
     * 
     */
    public Lead createLead() {
        return new Lead();
    }

    /**
     * Create an instance of {@link Case }
     * 
     */
    public Case createCase() {
        return new Case();
    }

    /**
     * Create an instance of {@link Event }
     * 
     */
    public Event createEvent() {
        return new Event();
    }

    /**
     * Create an instance of {@link Account }
     * 
     */
    public Account createAccount() {
        return new Account();
    }

    /**
     * Create an instance of {@link AccountContactRole }
     * 
     */
    public AccountContactRole createAccountContactRole() {
        return new AccountContactRole();
    }

    /**
     * Create an instance of {@link ContractHistory }
     * 
     */
    public ContractHistory createContractHistory() {
        return new ContractHistory();
    }

    /**
     * Create an instance of {@link EmailTemplate }
     * 
     */
    public EmailTemplate createEmailTemplate() {
        return new EmailTemplate();
    }

    /**
     * Create an instance of {@link LeadStatus }
     * 
     */
    public LeadStatus createLeadStatus() {
        return new LeadStatus();
    }

    /**
     * Create an instance of {@link GroupMember }
     * 
     */
    public GroupMember createGroupMember() {
        return new GroupMember();
    }

    /**
     * Create an instance of {@link Organization }
     * 
     */
    public Organization createOrganization() {
        return new Organization();
    }

    /**
     * Create an instance of {@link OpportunityShare }
     * 
     */
    public OpportunityShare createOpportunityShare() {
        return new OpportunityShare();
    }

    /**
     * Create an instance of {@link CaseComment }
     * 
     */
    public CaseComment createCaseComment() {
        return new CaseComment();
    }

    /**
     * Create an instance of {@link CategoryNode }
     * 
     */
    public CategoryNode createCategoryNode() {
        return new CategoryNode();
    }

    /**
     * Create an instance of {@link CaseStatus }
     * 
     */
    public CaseStatus createCaseStatus() {
        return new CaseStatus();
    }

    /**
     * Create an instance of {@link BusinessHours }
     * 
     */
    public BusinessHours createBusinessHours() {
        return new BusinessHours();
    }

    /**
     * Create an instance of {@link BrandTemplate }
     * 
     */
    public BrandTemplate createBrandTemplate() {
        return new BrandTemplate();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Opportunity }
     * 
     */
    public Opportunity createOpportunity() {
        return new Opportunity();
    }

    /**
     * Create an instance of {@link Group }
     * 
     */
    public Group createGroup() {
        return new Group();
    }

    /**
     * Create an instance of {@link Asset }
     * 
     */
    public Asset createAsset() {
        return new Asset();
    }

    /**
     * Create an instance of {@link RecordType }
     * 
     */
    public RecordType createRecordType() {
        return new RecordType();
    }

    /**
     * Create an instance of {@link FiscalYearSettings }
     * 
     */
    public FiscalYearSettings createFiscalYearSettings() {
        return new FiscalYearSettings();
    }

    /**
     * Create an instance of {@link LeadShare }
     * 
     */
    public LeadShare createLeadShare() {
        return new LeadShare();
    }

    /**
     * Create an instance of {@link CampaignMemberStatus }
     * 
     */
    public CampaignMemberStatus createCampaignMemberStatus() {
        return new CampaignMemberStatus();
    }

    /**
     * Create an instance of {@link CaseSolution }
     * 
     */
    public CaseSolution createCaseSolution() {
        return new CaseSolution();
    }

    /**
     * Create an instance of {@link OpportunityLineItem }
     * 
     */
    public OpportunityLineItem createOpportunityLineItem() {
        return new OpportunityLineItem();
    }

    /**
     * Create an instance of {@link CategoryData }
     * 
     */
    public CategoryData createCategoryData() {
        return new CategoryData();
    }

    /**
     * Create an instance of {@link OpportunityCompetitor }
     * 
     */
    public OpportunityCompetitor createOpportunityCompetitor() {
        return new OpportunityCompetitor();
    }

    /**
     * Create an instance of {@link ProcessInstanceHistory }
     * 
     */
    public ProcessInstanceHistory createProcessInstanceHistory() {
        return new ProcessInstanceHistory();
    }

    /**
     * Create an instance of {@link UserRole }
     * 
     */
    public UserRole createUserRole() {
        return new UserRole();
    }

    /**
     * Create an instance of {@link MailmergeTemplate }
     * 
     */
    public MailmergeTemplate createMailmergeTemplate() {
        return new MailmergeTemplate();
    }

    /**
     * Create an instance of {@link QueueSobject }
     * 
     */
    public QueueSobject createQueueSobject() {
        return new QueueSobject();
    }

    /**
     * Create an instance of {@link ApexPackage }
     * 
     */
    public ApexPackage createApexPackage() {
        return new ApexPackage();
    }

    /**
     * Create an instance of {@link OpenActivity }
     * 
     */
    public OpenActivity createOpenActivity() {
        return new OpenActivity();
    }

    /**
     * Create an instance of {@link AccountShare }
     * 
     */
    public AccountShare createAccountShare() {
        return new AccountShare();
    }

    /**
     * Create an instance of {@link SObject }
     * 
     */
    public SObject createSObject() {
        return new SObject();
    }

    /**
     * Create an instance of {@link ProcessInstance }
     * 
     */
    public ProcessInstance createProcessInstance() {
        return new ProcessInstance();
    }

    /**
     * Create an instance of {@link ActivityHistory }
     * 
     */
    public ActivityHistory createActivityHistory() {
        return new ActivityHistory();
    }

    /**
     * Create an instance of {@link ContractStatus }
     * 
     */
    public ContractStatus createContractStatus() {
        return new ContractStatus();
    }

    /**
     * Create an instance of {@link TestC }
     * 
     */
    public TestC createTestC() {
        return new TestC();
    }

    /**
     * Create an instance of {@link TaskStatus }
     * 
     */
    public TaskStatus createTaskStatus() {
        return new TaskStatus();
    }

    /**
     * Create an instance of {@link Folder }
     * 
     */
    public Folder createFolder() {
        return new Folder();
    }

    /**
     * Create an instance of {@link Profile }
     * 
     */
    public Profile createProfile() {
        return new Profile();
    }

    /**
     * Create an instance of {@link AccountTeamMember }
     * 
     */
    public AccountTeamMember createAccountTeamMember() {
        return new AccountTeamMember();
    }

    /**
     * Create an instance of {@link ApexPackageIdentifier }
     * 
     */
    public ApexPackageIdentifier createApexPackageIdentifier() {
        return new ApexPackageIdentifier();
    }

    /**
     * Create an instance of {@link OpportunityPartner }
     * 
     */
    public OpportunityPartner createOpportunityPartner() {
        return new OpportunityPartner();
    }

    /**
     * Create an instance of {@link Document }
     * 
     */
    public Document createDocument() {
        return new Document();
    }

    /**
     * Create an instance of {@link BusinessProcess }
     * 
     */
    public BusinessProcess createBusinessProcess() {
        return new BusinessProcess();
    }

    /**
     * Create an instance of {@link NoteAndAttachment }
     * 
     */
    public NoteAndAttachment createNoteAndAttachment() {
        return new NoteAndAttachment();
    }

    /**
     * Create an instance of {@link WebLink }
     * 
     */
    public WebLink createWebLink() {
        return new WebLink();
    }

    /**
     * Create an instance of {@link Campaign }
     * 
     */
    public Campaign createCampaign() {
        return new Campaign();
    }

    /**
     * Create an instance of {@link OpportunityHistory }
     * 
     */
    public OpportunityHistory createOpportunityHistory() {
        return new OpportunityHistory();
    }

    /**
     * Create an instance of {@link OpportunityTeamMember }
     * 
     */
    public OpportunityTeamMember createOpportunityTeamMember() {
        return new OpportunityTeamMember();
    }

    /**
     * Create an instance of {@link ApexPackageIdentifierArgument }
     * 
     */
    public ApexPackageIdentifierArgument createApexPackageIdentifierArgument() {
        return new ApexPackageIdentifierArgument();
    }

    /**
     * Create an instance of {@link SelfServiceUser }
     * 
     */
    public SelfServiceUser createSelfServiceUser() {
        return new SelfServiceUser();
    }

    /**
     * Create an instance of {@link Approval }
     * 
     */
    public Approval createApproval() {
        return new Approval();
    }

    /**
     * Create an instance of {@link ApexTrigger }
     * 
     */
    public ApexTrigger createApexTrigger() {
        return new ApexTrigger();
    }

    /**
     * Create an instance of {@link EventAttendee }
     * 
     */
    public EventAttendee createEventAttendee() {
        return new EventAttendee();
    }

    /**
     * Create an instance of {@link CampaignMember }
     * 
     */
    public CampaignMember createCampaignMember() {
        return new CampaignMember();
    }

    /**
     * Create an instance of {@link TaskPriority }
     * 
     */
    public TaskPriority createTaskPriority() {
        return new TaskPriority();
    }

    /**
     * Create an instance of {@link AssignmentRule }
     * 
     */
    public AssignmentRule createAssignmentRule() {
        return new AssignmentRule();
    }

    /**
     * Create an instance of {@link CaseContactRole }
     * 
     */
    public CaseContactRole createCaseContactRole() {
        return new CaseContactRole();
    }

    /**
     * Create an instance of {@link UserTeamMember }
     * 
     */
    public UserTeamMember createUserTeamMember() {
        return new UserTeamMember();
    }

    /**
     * Create an instance of {@link CaseShare }
     * 
     */
    public CaseShare createCaseShare() {
        return new CaseShare();
    }

    /**
     * Create an instance of {@link ProcessInstanceWorkitem }
     * 
     */
    public ProcessInstanceWorkitem createProcessInstanceWorkitem() {
        return new ProcessInstanceWorkitem();
    }

    /**
     * Create an instance of {@link PricebookEntry }
     * 
     */
    public PricebookEntry createPricebookEntry() {
        return new PricebookEntry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReminderDateTime", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventReminderDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventReminderDateTime_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsRecurrence", scope = Event.class)
    public JAXBElement<Boolean> createEventIsRecurrence(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsRecurrence_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Event.class)
    public JAXBElement<String> createEventAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Event.class)
    public JAXBElement<QueryResult> createEventAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Event.class)
    public JAXBElement<String> createEventCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Event.class)
    public JAXBElement<Boolean> createEventIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceActivityId", scope = Event.class)
    public JAXBElement<String> createEventRecurrenceActivityId(String value) {
        return new JAXBElement<String>(_EventRecurrenceActivityId_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceDayOfWeekMask", scope = Event.class)
    public JAXBElement<Integer> createEventRecurrenceDayOfWeekMask(Integer value) {
        return new JAXBElement<Integer>(_EventRecurrenceDayOfWeekMask_QNAME, Integer.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsArchived", scope = Event.class)
    public JAXBElement<Boolean> createEventIsArchived(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsArchived_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Event.class)
    public JAXBElement<User> createEventCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsAllDayEvent", scope = Event.class)
    public JAXBElement<Boolean> createEventIsAllDayEvent(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsAllDayEvent_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Event.class)
    public JAXBElement<String> createEventLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsChild", scope = Event.class)
    public JAXBElement<Boolean> createEventIsChild(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsChild_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsGroupEvent", scope = Event.class)
    public JAXBElement<Boolean> createEventIsGroupEvent(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsGroupEvent_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Who", scope = Event.class)
    public JAXBElement<Name> createEventWho(Name value) {
        return new JAXBElement<Name>(_EventWho_QNAME, Name.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "What", scope = Event.class)
    public JAXBElement<Name> createEventWhat(Name value) {
        return new JAXBElement<Name>(_EventWhat_QNAME, Name.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceEndDateOnly", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventRecurrenceEndDateOnly(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventRecurrenceEndDateOnly_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceInterval", scope = Event.class)
    public JAXBElement<Integer> createEventRecurrenceInterval(Integer value) {
        return new JAXBElement<Integer>(_EventRecurrenceInterval_QNAME, Integer.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Event.class)
    public JAXBElement<User> createEventLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceType", scope = Event.class)
    public JAXBElement<String> createEventRecurrenceType(String value) {
        return new JAXBElement<String>(_EventRecurrenceType_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceDayOfMonth", scope = Event.class)
    public JAXBElement<Integer> createEventRecurrenceDayOfMonth(Integer value) {
        return new JAXBElement<Integer>(_EventRecurrenceDayOfMonth_QNAME, Integer.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhatId", scope = Event.class)
    public JAXBElement<String> createEventWhatId(String value) {
        return new JAXBElement<String>(_EventWhatId_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceMonthOfYear", scope = Event.class)
    public JAXBElement<String> createEventRecurrenceMonthOfYear(String value) {
        return new JAXBElement<String>(_EventRecurrenceMonthOfYear_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceTimeZoneSidKey", scope = Event.class)
    public JAXBElement<String> createEventRecurrenceTimeZoneSidKey(String value) {
        return new JAXBElement<String>(_EventRecurrenceTimeZoneSidKey_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceStartDateTime", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventRecurrenceStartDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventRecurrenceStartDateTime_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityDateTime", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventActivityDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventActivityDateTime_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityDate", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventActivityDate_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Subject", scope = Event.class)
    public JAXBElement<String> createEventSubject(String value) {
        return new JAXBElement<String>(_EventSubject_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsReminderSet", scope = Event.class)
    public JAXBElement<Boolean> createEventIsReminderSet(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsReminderSet_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurringEvents", scope = Event.class)
    public JAXBElement<QueryResult> createEventRecurringEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventRecurringEvents_QNAME, QueryResult.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Event.class)
    public JAXBElement<String> createEventDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhoId", scope = Event.class)
    public JAXBElement<String> createEventWhoId(String value) {
        return new JAXBElement<String>(_EventWhoId_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrivate", scope = Event.class)
    public JAXBElement<Boolean> createEventIsPrivate(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsPrivate_QNAME, Boolean.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShowAs", scope = Event.class)
    public JAXBElement<String> createEventShowAs(String value) {
        return new JAXBElement<String>(_EventShowAs_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecurrenceInstance", scope = Event.class)
    public JAXBElement<String> createEventRecurrenceInstance(String value) {
        return new JAXBElement<String>(_EventRecurrenceInstance_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Event.class)
    public JAXBElement<String> createEventOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DurationInMinutes", scope = Event.class)
    public JAXBElement<Integer> createEventDurationInMinutes(Integer value) {
        return new JAXBElement<Integer>(_EventDurationInMinutes_QNAME, Integer.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Location", scope = Event.class)
    public JAXBElement<String> createEventLocation(String value) {
        return new JAXBElement<String>(_EventLocation_QNAME, String.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Event.class)
    public JAXBElement<XMLGregorianCalendar> createEventCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Event.class)
    public JAXBElement<Name> createEventOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, Event.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = BusinessProcess.class)
    public JAXBElement<String> createBusinessProcessDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = BusinessProcess.class)
    public JAXBElement<User> createBusinessProcessLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = BusinessProcess.class)
    public JAXBElement<Boolean> createBusinessProcessIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = BusinessProcess.class)
    public JAXBElement<String> createBusinessProcessName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = BusinessProcess.class)
    public JAXBElement<User> createBusinessProcessCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = BusinessProcess.class)
    public JAXBElement<XMLGregorianCalendar> createBusinessProcessLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TableEnumOrId", scope = BusinessProcess.class)
    public JAXBElement<String> createBusinessProcessTableEnumOrId(String value) {
        return new JAXBElement<String>(_BusinessProcessTableEnumOrId_QNAME, String.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = BusinessProcess.class)
    public JAXBElement<XMLGregorianCalendar> createBusinessProcessSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = BusinessProcess.class)
    public JAXBElement<String> createBusinessProcessCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = BusinessProcess.class)
    public JAXBElement<String> createBusinessProcessLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = BusinessProcess.class)
    public JAXBElement<XMLGregorianCalendar> createBusinessProcessCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, BusinessProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Account.class)
    public JAXBElement<String> createAccountName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AnnualRevenue", scope = Account.class)
    public JAXBElement<Double> createAccountAnnualRevenue(Double value) {
        return new JAXBElement<Double>(_AccountAnnualRevenue_QNAME, Double.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Shares", scope = Account.class)
    public JAXBElement<QueryResult> createAccountShares(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountShares_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingCountry", scope = Account.class)
    public JAXBElement<String> createAccountBillingCountry(String value) {
        return new JAXBElement<String>(_AccountBillingCountry_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Account.class)
    public JAXBElement<QueryResult> createAccountAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Account.class)
    public JAXBElement<QueryResult> createAccountTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Account.class)
    public JAXBElement<Boolean> createAccountIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterRecordId", scope = Account.class)
    public JAXBElement<String> createAccountMasterRecordId(String value) {
        return new JAXBElement<String>(_AccountMasterRecordId_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Account.class)
    public JAXBElement<QueryResult> createAccountActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Account.class)
    public JAXBElement<QueryResult> createAccountNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TickerSymbol", scope = Account.class)
    public JAXBElement<String> createAccountTickerSymbol(String value) {
        return new JAXBElement<String>(_AccountTickerSymbol_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PartnersTo", scope = Account.class)
    public JAXBElement<QueryResult> createAccountPartnersTo(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountPartnersTo_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingPostalCode", scope = Account.class)
    public JAXBElement<String> createAccountBillingPostalCode(String value) {
        return new JAXBElement<String>(_AccountBillingPostalCode_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Account.class)
    public JAXBElement<User> createAccountCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingState", scope = Account.class)
    public JAXBElement<String> createAccountBillingState(String value) {
        return new JAXBElement<String>(_AccountBillingState_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountPartnersFrom", scope = Account.class)
    public JAXBElement<QueryResult> createAccountAccountPartnersFrom(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAccountPartnersFrom_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShippingState", scope = Account.class)
    public JAXBElement<String> createAccountShippingState(String value) {
        return new JAXBElement<String>(_AccountShippingState_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastActivityDate", scope = Account.class)
    public JAXBElement<XMLGregorianCalendar> createAccountLastActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountLastActivityDate_QNAME, XMLGregorianCalendar.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Account.class)
    public JAXBElement<QueryResult> createAccountProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Account.class)
    public JAXBElement<QueryResult> createAccountOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SLASerialNumber__c", scope = Account.class)
    public JAXBElement<String> createAccountSLASerialNumberC(String value) {
        return new JAXBElement<String>(_AccountSLASerialNumberC_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShippingPostalCode", scope = Account.class)
    public JAXBElement<String> createAccountShippingPostalCode(String value) {
        return new JAXBElement<String>(_AccountShippingPostalCode_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Account.class)
    public JAXBElement<User> createAccountLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShippingCountry", scope = Account.class)
    public JAXBElement<String> createAccountShippingCountry(String value) {
        return new JAXBElement<String>(_AccountShippingCountry_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Ownership", scope = Account.class)
    public JAXBElement<String> createAccountOwnership(String value) {
        return new JAXBElement<String>(_AccountOwnership_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Phone", scope = Account.class)
    public JAXBElement<String> createAccountPhone(String value) {
        return new JAXBElement<String>(_AccountPhone_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Parent", scope = Account.class)
    public JAXBElement<Account> createAccountParent(Account value) {
        return new JAXBElement<Account>(_AccountParent_QNAME, Account.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Account.class)
    public JAXBElement<XMLGregorianCalendar> createAccountLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Site", scope = Account.class)
    public JAXBElement<String> createAccountSite(String value) {
        return new JAXBElement<String>(_AccountSite_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SLAExpirationDate__c", scope = Account.class)
    public JAXBElement<XMLGregorianCalendar> createAccountSLAExpirationDateC(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountSLAExpirationDateC_QNAME, XMLGregorianCalendar.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Account.class)
    public JAXBElement<QueryResult> createAccountNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Account.class)
    public JAXBElement<String> createAccountDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Assets", scope = Account.class)
    public JAXBElement<QueryResult> createAccountAssets(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAssets_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountContactRoles", scope = Account.class)
    public JAXBElement<QueryResult> createAccountAccountContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAccountContactRoles_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Account.class)
    public JAXBElement<QueryResult> createAccountEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Fax", scope = Account.class)
    public JAXBElement<String> createAccountFax(String value) {
        return new JAXBElement<String>(_AccountFax_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contacts", scope = Account.class)
    public JAXBElement<QueryResult> createAccountContacts(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountContacts_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfEmployees", scope = Account.class)
    public JAXBElement<Integer> createAccountNumberOfEmployees(Integer value) {
        return new JAXBElement<Integer>(_AccountNumberOfEmployees_QNAME, Integer.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contracts", scope = Account.class)
    public JAXBElement<QueryResult> createAccountContracts(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountContracts_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountPartnersTo", scope = Account.class)
    public JAXBElement<QueryResult> createAccountAccountPartnersTo(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAccountPartnersTo_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Account.class)
    public JAXBElement<XMLGregorianCalendar> createAccountSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Sic", scope = Account.class)
    public JAXBElement<String> createAccountSic(String value) {
        return new JAXBElement<String>(_AccountSic_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Account.class)
    public JAXBElement<String> createAccountCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = Account.class)
    public JAXBElement<String> createAccountParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Website", scope = Account.class)
    public JAXBElement<String> createAccountWebsite(String value) {
        return new JAXBElement<String>(_AccountWebsite_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShippingCity", scope = Account.class)
    public JAXBElement<String> createAccountShippingCity(String value) {
        return new JAXBElement<String>(_AccountShippingCity_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "External_Id__c", scope = Account.class)
    public JAXBElement<String> createAccountExternalIdC(String value) {
        return new JAXBElement<String>(_AccountExternalIdC_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Account.class)
    public JAXBElement<String> createAccountType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountTeamMembers", scope = Account.class)
    public JAXBElement<QueryResult> createAccountAccountTeamMembers(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAccountTeamMembers_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberofLocations__c", scope = Account.class)
    public JAXBElement<Double> createAccountNumberofLocationsC(Double value) {
        return new JAXBElement<Double>(_AccountNumberofLocationsC_QNAME, Double.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PartnersFrom", scope = Account.class)
    public JAXBElement<QueryResult> createAccountPartnersFrom(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountPartnersFrom_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Account.class)
    public JAXBElement<String> createAccountLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Account.class)
    public JAXBElement<QueryResult> createAccountProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunities", scope = Account.class)
    public JAXBElement<QueryResult> createAccountOpportunities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpportunities_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SLA__c", scope = Account.class)
    public JAXBElement<String> createAccountSLAC(String value) {
        return new JAXBElement<String>(_AccountSLAC_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Cases", scope = Account.class)
    public JAXBElement<QueryResult> createAccountCases(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountCases_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Rating", scope = Account.class)
    public JAXBElement<String> createAccountRating(String value) {
        return new JAXBElement<String>(_AccountRating_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Industry", scope = Account.class)
    public JAXBElement<String> createAccountIndustry(String value) {
        return new JAXBElement<String>(_AccountIndustry_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CustomerPriority__c", scope = Account.class)
    public JAXBElement<String> createAccountCustomerPriorityC(String value) {
        return new JAXBElement<String>(_AccountCustomerPriorityC_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UpsellOpportunity__c", scope = Account.class)
    public JAXBElement<String> createAccountUpsellOpportunityC(String value) {
        return new JAXBElement<String>(_AccountUpsellOpportunityC_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShippingStreet", scope = Account.class)
    public JAXBElement<String> createAccountShippingStreet(String value) {
        return new JAXBElement<String>(_AccountShippingStreet_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountNumber", scope = Account.class)
    public JAXBElement<String> createAccountAccountNumber(String value) {
        return new JAXBElement<String>(_AccountAccountNumber_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingCity", scope = Account.class)
    public JAXBElement<String> createAccountBillingCity(String value) {
        return new JAXBElement<String>(_AccountBillingCity_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Active__c", scope = Account.class)
    public JAXBElement<String> createAccountActiveC(String value) {
        return new JAXBElement<String>(_AccountActiveC_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Account.class)
    public JAXBElement<String> createAccountOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingStreet", scope = Account.class)
    public JAXBElement<String> createAccountBillingStreet(String value) {
        return new JAXBElement<String>(_AccountBillingStreet_QNAME, String.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityPartnersTo", scope = Account.class)
    public JAXBElement<QueryResult> createAccountOpportunityPartnersTo(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpportunityPartnersTo_QNAME, QueryResult.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Account.class)
    public JAXBElement<XMLGregorianCalendar> createAccountCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Account.class)
    public JAXBElement<User> createAccountOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Account.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "QuarterLabel", scope = Period.class)
    public JAXBElement<String> createPeriodQuarterLabel(String value) {
        return new JAXBElement<String>(_PeriodQuarterLabel_QNAME, String.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PeriodLabel", scope = Period.class)
    public JAXBElement<String> createPeriodPeriodLabel(String value) {
        return new JAXBElement<String>(_PeriodPeriodLabel_QNAME, String.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EndDate", scope = Period.class)
    public JAXBElement<XMLGregorianCalendar> createPeriodEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PeriodEndDate_QNAME, XMLGregorianCalendar.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Period.class)
    public JAXBElement<String> createPeriodType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Period.class)
    public JAXBElement<XMLGregorianCalendar> createPeriodSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StartDate", scope = Period.class)
    public JAXBElement<XMLGregorianCalendar> createPeriodStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PeriodStartDate_QNAME, XMLGregorianCalendar.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Number", scope = Period.class)
    public JAXBElement<Integer> createPeriodNumber(Integer value) {
        return new JAXBElement<Integer>(_PeriodNumber_QNAME, Integer.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsForecastPeriod", scope = Period.class)
    public JAXBElement<Boolean> createPeriodIsForecastPeriod(Boolean value) {
        return new JAXBElement<Boolean>(_PeriodIsForecastPeriod_QNAME, Boolean.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FiscalYearSettingsId", scope = Period.class)
    public JAXBElement<String> createPeriodFiscalYearSettingsId(String value) {
        return new JAXBElement<String>(_PeriodFiscalYearSettingsId_QNAME, String.class, Period.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = AccountContactRole.class)
    public JAXBElement<User> createAccountContactRoleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = AccountContactRole.class)
    public JAXBElement<Account> createAccountContactRoleAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = AccountContactRole.class)
    public JAXBElement<String> createAccountContactRoleAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = AccountContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createAccountContactRoleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = AccountContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createAccountContactRoleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = AccountContactRole.class)
    public JAXBElement<String> createAccountContactRoleCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = AccountContactRole.class)
    public JAXBElement<Contact> createAccountContactRoleContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = AccountContactRole.class)
    public JAXBElement<Boolean> createAccountContactRoleIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = AccountContactRole.class)
    public JAXBElement<User> createAccountContactRoleCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = AccountContactRole.class)
    public JAXBElement<String> createAccountContactRoleRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = AccountContactRole.class)
    public JAXBElement<String> createAccountContactRoleContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrimary", scope = AccountContactRole.class)
    public JAXBElement<Boolean> createAccountContactRoleIsPrimary(Boolean value) {
        return new JAXBElement<Boolean>(_AccountContactRoleIsPrimary_QNAME, Boolean.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = AccountContactRole.class)
    public JAXBElement<String> createAccountContactRoleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = AccountContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createAccountContactRoleCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, AccountContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Attachment.class)
    public JAXBElement<Name> createAttachmentLastModifiedBy(Name value) {
        return new JAXBElement<Name>(_EventLastModifiedBy_QNAME, Name.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Attachment.class)
    public JAXBElement<String> createAttachmentName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = Attachment.class)
    public JAXBElement<byte[]> createAttachmentBody(byte[] value) {
        return new JAXBElement<byte[]>(_AttachmentBody_QNAME, byte[].class, Attachment.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Parent", scope = Attachment.class)
    public JAXBElement<Name> createAttachmentParent(Name value) {
        return new JAXBElement<Name>(_AccountParent_QNAME, Name.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Attachment.class)
    public JAXBElement<XMLGregorianCalendar> createAttachmentLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Attachment.class)
    public JAXBElement<XMLGregorianCalendar> createAttachmentSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Attachment.class)
    public JAXBElement<String> createAttachmentCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = Attachment.class)
    public JAXBElement<String> createAttachmentParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BodyLength", scope = Attachment.class)
    public JAXBElement<Integer> createAttachmentBodyLength(Integer value) {
        return new JAXBElement<Integer>(_AttachmentBodyLength_QNAME, Integer.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Attachment.class)
    public JAXBElement<Boolean> createAttachmentIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrivate", scope = Attachment.class)
    public JAXBElement<Boolean> createAttachmentIsPrivate(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsPrivate_QNAME, Boolean.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Attachment.class)
    public JAXBElement<Name> createAttachmentCreatedBy(Name value) {
        return new JAXBElement<Name>(_EventCreatedBy_QNAME, Name.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Attachment.class)
    public JAXBElement<String> createAttachmentOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Attachment.class)
    public JAXBElement<String> createAttachmentLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Attachment.class)
    public JAXBElement<XMLGregorianCalendar> createAttachmentCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContentType", scope = Attachment.class)
    public JAXBElement<String> createAttachmentContentType(String value) {
        return new JAXBElement<String>(_AttachmentContentType_QNAME, String.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Attachment.class)
    public JAXBElement<Name> createAttachmentOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, Attachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = SolutionStatus.class)
    public JAXBElement<User> createSolutionStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = SolutionStatus.class)
    public JAXBElement<String> createSolutionStatusMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = SolutionStatus.class)
    public JAXBElement<Integer> createSolutionStatusSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = SolutionStatus.class)
    public JAXBElement<User> createSolutionStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = SolutionStatus.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = SolutionStatus.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionStatusSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = SolutionStatus.class)
    public JAXBElement<String> createSolutionStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = SolutionStatus.class)
    public JAXBElement<String> createSolutionStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsReviewed", scope = SolutionStatus.class)
    public JAXBElement<Boolean> createSolutionStatusIsReviewed(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsReviewed_QNAME, Boolean.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = SolutionStatus.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = SolutionStatus.class)
    public JAXBElement<Boolean> createSolutionStatusIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, SolutionStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OtherState", scope = Contact.class)
    public JAXBElement<String> createContactOtherState(String value) {
        return new JAXBElement<String>(_ContactOtherState_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReportsTo", scope = Contact.class)
    public JAXBElement<Contact> createContactReportsTo(Contact value) {
        return new JAXBElement<Contact>(_ContactReportsTo_QNAME, Contact.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Contact.class)
    public JAXBElement<String> createContactName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Contact.class)
    public JAXBElement<String> createContactAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OtherStreet", scope = Contact.class)
    public JAXBElement<String> createContactOtherStreet(String value) {
        return new JAXBElement<String>(_ContactOtherStreet_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Contact.class)
    public JAXBElement<QueryResult> createContactAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Contact.class)
    public JAXBElement<QueryResult> createContactTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstName", scope = Contact.class)
    public JAXBElement<String> createContactFirstName(String value) {
        return new JAXBElement<String>(_ContactFirstName_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Contact.class)
    public JAXBElement<Boolean> createContactIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Level__c", scope = Contact.class)
    public JAXBElement<String> createContactLevelC(String value) {
        return new JAXBElement<String>(_ContactLevelC_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Email", scope = Contact.class)
    public JAXBElement<String> createContactEmail(String value) {
        return new JAXBElement<String>(_ContactEmail_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterRecordId", scope = Contact.class)
    public JAXBElement<String> createContactMasterRecordId(String value) {
        return new JAXBElement<String>(_AccountMasterRecordId_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Contact.class)
    public JAXBElement<QueryResult> createContactActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastCURequestDate", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactLastCURequestDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContactLastCURequestDate_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Contact.class)
    public JAXBElement<QueryResult> createContactNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Department", scope = Contact.class)
    public JAXBElement<String> createContactDepartment(String value) {
        return new JAXBElement<String>(_ContactDepartment_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OtherCountry", scope = Contact.class)
    public JAXBElement<String> createContactOtherCountry(String value) {
        return new JAXBElement<String>(_ContactOtherCountry_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Contact.class)
    public JAXBElement<User> createContactCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastActivityDate", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactLastActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountLastActivityDate_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Contact.class)
    public JAXBElement<QueryResult> createContactProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Birthdate", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactBirthdate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContactBirthdate_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractsSigned", scope = Contact.class)
    public JAXBElement<QueryResult> createContactContractsSigned(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactContractsSigned_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Contact.class)
    public JAXBElement<QueryResult> createContactOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Contact.class)
    public JAXBElement<User> createContactLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Phone", scope = Contact.class)
    public JAXBElement<String> createContactPhone(String value) {
        return new JAXBElement<String>(_AccountPhone_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MobilePhone", scope = Contact.class)
    public JAXBElement<String> createContactMobilePhone(String value) {
        return new JAXBElement<String>(_ContactMobilePhone_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MailingPostalCode", scope = Contact.class)
    public JAXBElement<String> createContactMailingPostalCode(String value) {
        return new JAXBElement<String>(_ContactMailingPostalCode_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Contact.class)
    public JAXBElement<QueryResult> createContactNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Contact.class)
    public JAXBElement<String> createContactDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MailingStreet", scope = Contact.class)
    public JAXBElement<String> createContactMailingStreet(String value) {
        return new JAXBElement<String>(_ContactMailingStreet_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Assets", scope = Contact.class)
    public JAXBElement<QueryResult> createContactAssets(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAssets_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountContactRoles", scope = Contact.class)
    public JAXBElement<QueryResult> createContactAccountContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAccountContactRoles_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Fax", scope = Contact.class)
    public JAXBElement<String> createContactFax(String value) {
        return new JAXBElement<String>(_AccountFax_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Contact.class)
    public JAXBElement<QueryResult> createContactEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MailingCountry", scope = Contact.class)
    public JAXBElement<String> createContactMailingCountry(String value) {
        return new JAXBElement<String>(_ContactMailingCountry_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecordTypeId", scope = Contact.class)
    public JAXBElement<String> createContactRecordTypeId(String value) {
        return new JAXBElement<String>(_ContactRecordTypeId_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AssistantName", scope = Contact.class)
    public JAXBElement<String> createContactAssistantName(String value) {
        return new JAXBElement<String>(_ContactAssistantName_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OtherPhone", scope = Contact.class)
    public JAXBElement<String> createContactOtherPhone(String value) {
        return new JAXBElement<String>(_ContactOtherPhone_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReportsToId", scope = Contact.class)
    public JAXBElement<String> createContactReportsToId(String value) {
        return new JAXBElement<String>(_ContactReportsToId_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecordType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RecordType", scope = Contact.class)
    public JAXBElement<RecordType> createContactRecordType(RecordType value) {
        return new JAXBElement<RecordType>(_ContactRecordType_QNAME, RecordType.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Title", scope = Contact.class)
    public JAXBElement<String> createContactTitle(String value) {
        return new JAXBElement<String>(_ContactTitle_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Contact.class)
    public JAXBElement<String> createContactCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OtherCity", scope = Contact.class)
    public JAXBElement<String> createContactOtherCity(String value) {
        return new JAXBElement<String>(_ContactOtherCity_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadSource", scope = Contact.class)
    public JAXBElement<String> createContactLeadSource(String value) {
        return new JAXBElement<String>(_ContactLeadSource_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Languages__c", scope = Contact.class)
    public JAXBElement<String> createContactLanguagesC(String value) {
        return new JAXBElement<String>(_ContactLanguagesC_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseContactRoles", scope = Contact.class)
    public JAXBElement<QueryResult> createContactCaseContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactCaseContactRoles_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MailingState", scope = Contact.class)
    public JAXBElement<String> createContactMailingState(String value) {
        return new JAXBElement<String>(_ContactMailingState_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Contact.class)
    public JAXBElement<String> createContactLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Contact.class)
    public JAXBElement<QueryResult> createContactProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastName", scope = Contact.class)
    public JAXBElement<String> createContactLastName(String value) {
        return new JAXBElement<String>(_ContactLastName_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunities", scope = Contact.class)
    public JAXBElement<QueryResult> createContactOpportunities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpportunities_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EmailStatuses", scope = Contact.class)
    public JAXBElement<QueryResult> createContactEmailStatuses(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactEmailStatuses_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AssistantPhone", scope = Contact.class)
    public JAXBElement<String> createContactAssistantPhone(String value) {
        return new JAXBElement<String>(_ContactAssistantPhone_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = Contact.class)
    public JAXBElement<Account> createContactAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Cases", scope = Contact.class)
    public JAXBElement<QueryResult> createContactCases(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountCases_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractContactRoles", scope = Contact.class)
    public JAXBElement<QueryResult> createContactContractContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactContractContactRoles_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Salutation", scope = Contact.class)
    public JAXBElement<String> createContactSalutation(String value) {
        return new JAXBElement<String>(_ContactSalutation_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OtherPostalCode", scope = Contact.class)
    public JAXBElement<String> createContactOtherPostalCode(String value) {
        return new JAXBElement<String>(_ContactOtherPostalCode_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MailingCity", scope = Contact.class)
    public JAXBElement<String> createContactMailingCity(String value) {
        return new JAXBElement<String>(_ContactMailingCity_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastCUUpdateDate", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactLastCUUpdateDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContactLastCUUpdateDate_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Contact.class)
    public JAXBElement<String> createContactOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HomePhone", scope = Contact.class)
    public JAXBElement<String> createContactHomePhone(String value) {
        return new JAXBElement<String>(_ContactHomePhone_QNAME, String.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CampaignMembers", scope = Contact.class)
    public JAXBElement<QueryResult> createContactCampaignMembers(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactCampaignMembers_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Contact.class)
    public JAXBElement<XMLGregorianCalendar> createContactCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityContactRoles", scope = Contact.class)
    public JAXBElement<QueryResult> createContactOpportunityContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactOpportunityContactRoles_QNAME, QueryResult.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Contact.class)
    public JAXBElement<User> createContactOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Contact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = NoteAndAttachment.class)
    public JAXBElement<User> createNoteAndAttachmentLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Parent", scope = NoteAndAttachment.class)
    public JAXBElement<Name> createNoteAndAttachmentParent(Name value) {
        return new JAXBElement<Name>(_AccountParent_QNAME, Name.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = NoteAndAttachment.class)
    public JAXBElement<XMLGregorianCalendar> createNoteAndAttachmentLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Title", scope = NoteAndAttachment.class)
    public JAXBElement<String> createNoteAndAttachmentTitle(String value) {
        return new JAXBElement<String>(_ContactTitle_QNAME, String.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = NoteAndAttachment.class)
    public JAXBElement<XMLGregorianCalendar> createNoteAndAttachmentSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = NoteAndAttachment.class)
    public JAXBElement<String> createNoteAndAttachmentCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = NoteAndAttachment.class)
    public JAXBElement<String> createNoteAndAttachmentParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = NoteAndAttachment.class)
    public JAXBElement<Boolean> createNoteAndAttachmentIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrivate", scope = NoteAndAttachment.class)
    public JAXBElement<Boolean> createNoteAndAttachmentIsPrivate(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsPrivate_QNAME, Boolean.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = NoteAndAttachment.class)
    public JAXBElement<User> createNoteAndAttachmentCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = NoteAndAttachment.class)
    public JAXBElement<String> createNoteAndAttachmentOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsNote", scope = NoteAndAttachment.class)
    public JAXBElement<Boolean> createNoteAndAttachmentIsNote(Boolean value) {
        return new JAXBElement<Boolean>(_NoteAndAttachmentIsNote_QNAME, Boolean.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = NoteAndAttachment.class)
    public JAXBElement<String> createNoteAndAttachmentLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = NoteAndAttachment.class)
    public JAXBElement<XMLGregorianCalendar> createNoteAndAttachmentCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = NoteAndAttachment.class)
    public JAXBElement<User> createNoteAndAttachmentOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, NoteAndAttachment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NewValue", scope = ContractHistory.class)
    public JAXBElement<Object> createContractHistoryNewValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryNewValue_QNAME, Object.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OldValue", scope = ContractHistory.class)
    public JAXBElement<Object> createContractHistoryOldValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryOldValue_QNAME, Object.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ContractHistory.class)
    public JAXBElement<Name> createContractHistoryCreatedBy(Name value) {
        return new JAXBElement<Name>(_EventCreatedBy_QNAME, Name.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contract }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contract", scope = ContractHistory.class)
    public JAXBElement<Contract> createContractHistoryContract(Contract value) {
        return new JAXBElement<Contract>(_ContractHistoryContract_QNAME, Contract.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ContractHistory.class)
    public JAXBElement<String> createContractHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractId", scope = ContractHistory.class)
    public JAXBElement<String> createContractHistoryContractId(String value) {
        return new JAXBElement<String>(_ContractHistoryContractId_QNAME, String.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Field", scope = ContractHistory.class)
    public JAXBElement<String> createContractHistoryField(String value) {
        return new JAXBElement<String>(_ContractHistoryField_QNAME, String.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = ContractHistory.class)
    public JAXBElement<Boolean> createContractHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ContractHistory.class)
    public JAXBElement<XMLGregorianCalendar> createContractHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ContractHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityCompetitor.class)
    public JAXBElement<User> createOpportunityCompetitorLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Strengths", scope = OpportunityCompetitor.class)
    public JAXBElement<String> createOpportunityCompetitorStrengths(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorStrengths_QNAME, String.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityCompetitor.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityCompetitorLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityCompetitor.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityCompetitorSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityCompetitor.class)
    public JAXBElement<Opportunity> createOpportunityCompetitorOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityCompetitor.class)
    public JAXBElement<String> createOpportunityCompetitorCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityCompetitor.class)
    public JAXBElement<Boolean> createOpportunityCompetitorIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Weaknesses", scope = OpportunityCompetitor.class)
    public JAXBElement<String> createOpportunityCompetitorWeaknesses(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorWeaknesses_QNAME, String.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityCompetitor.class)
    public JAXBElement<String> createOpportunityCompetitorOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityCompetitor.class)
    public JAXBElement<User> createOpportunityCompetitorCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityCompetitor.class)
    public JAXBElement<String> createOpportunityCompetitorLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CompetitorName", scope = OpportunityCompetitor.class)
    public JAXBElement<String> createOpportunityCompetitorCompetitorName(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorCompetitorName_QNAME, String.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityCompetitor.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityCompetitorCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityCompetitor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = EmailTemplate.class)
    public JAXBElement<Boolean> createEmailTemplateIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = EmailTemplate.class)
    public JAXBElement<User> createEmailTemplateLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateBody(String value) {
        return new JAXBElement<String>(_AttachmentBody_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FolderId", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateFolderId(String value) {
        return new JAXBElement<String>(_EmailTemplateFolderId_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastUsedDate", scope = EmailTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createEmailTemplateLastUsedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EmailTemplateLastUsedDate_QNAME, XMLGregorianCalendar.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = EmailTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createEmailTemplateLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = EmailTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createEmailTemplateSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = EmailTemplate.class)
    public JAXBElement<QueryResult> createEmailTemplateAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Subject", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateSubject(String value) {
        return new JAXBElement<String>(_EventSubject_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BrandTemplateId", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateBrandTemplateId(String value) {
        return new JAXBElement<String>(_EmailTemplateBrandTemplateId_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HtmlValue", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateHtmlValue(String value) {
        return new JAXBElement<String>(_EmailTemplateHtmlValue_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = EmailTemplate.class)
    public JAXBElement<User> createEmailTemplateCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TimesUsed", scope = EmailTemplate.class)
    public JAXBElement<Integer> createEmailTemplateTimesUsed(Integer value) {
        return new JAXBElement<Integer>(_EmailTemplateTimesUsed_QNAME, Integer.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = EmailTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createEmailTemplateCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TemplateStyle", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateTemplateStyle(String value) {
        return new JAXBElement<String>(_EmailTemplateTemplateStyle_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = EmailTemplate.class)
    public JAXBElement<User> createEmailTemplateOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Encoding", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateEncoding(String value) {
        return new JAXBElement<String>(_EmailTemplateEncoding_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TemplateType", scope = EmailTemplate.class)
    public JAXBElement<String> createEmailTemplateTemplateType(String value) {
        return new JAXBElement<String>(_EmailTemplateTemplateType_QNAME, String.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Folder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Folder", scope = EmailTemplate.class)
    public JAXBElement<Folder> createEmailTemplateFolder(Folder value) {
        return new JAXBElement<Folder>(_EmailTemplateFolder_QNAME, Folder.class, EmailTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = PartnerRole.class)
    public JAXBElement<User> createPartnerRoleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = PartnerRole.class)
    public JAXBElement<String> createPartnerRoleMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = PartnerRole.class)
    public JAXBElement<Integer> createPartnerRoleSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReverseRole", scope = PartnerRole.class)
    public JAXBElement<String> createPartnerRoleReverseRole(String value) {
        return new JAXBElement<String>(_PartnerRoleReverseRole_QNAME, String.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = PartnerRole.class)
    public JAXBElement<User> createPartnerRoleCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = PartnerRole.class)
    public JAXBElement<XMLGregorianCalendar> createPartnerRoleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = PartnerRole.class)
    public JAXBElement<XMLGregorianCalendar> createPartnerRoleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = PartnerRole.class)
    public JAXBElement<String> createPartnerRoleCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = PartnerRole.class)
    public JAXBElement<String> createPartnerRoleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = PartnerRole.class)
    public JAXBElement<XMLGregorianCalendar> createPartnerRoleCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, PartnerRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = WebLink.class)
    public JAXBElement<String> createWebLinkMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = WebLink.class)
    public JAXBElement<String> createWebLinkName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NamespacePrefix", scope = WebLink.class)
    public JAXBElement<String> createWebLinkNamespacePrefix(String value) {
        return new JAXBElement<String>(_WebLinkNamespacePrefix_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RequireRowSelection", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkRequireRowSelection(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkRequireRowSelection_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShowsStatus", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkShowsStatus(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkShowsStatus_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasScrollbars", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkHasScrollbars(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkHasScrollbars_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = WebLink.class)
    public JAXBElement<XMLGregorianCalendar> createWebLinkSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Position", scope = WebLink.class)
    public JAXBElement<String> createWebLinkPosition(String value) {
        return new JAXBElement<String>(_WebLinkPosition_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = WebLink.class)
    public JAXBElement<String> createWebLinkCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PageOrSobjectType", scope = WebLink.class)
    public JAXBElement<String> createWebLinkPageOrSobjectType(String value) {
        return new JAXBElement<String>(_WebLinkPageOrSobjectType_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DisplayType", scope = WebLink.class)
    public JAXBElement<String> createWebLinkDisplayType(String value) {
        return new JAXBElement<String>(_WebLinkDisplayType_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = WebLink.class)
    public JAXBElement<User> createWebLinkCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ScontrolId", scope = WebLink.class)
    public JAXBElement<String> createWebLinkScontrolId(String value) {
        return new JAXBElement<String>(_WebLinkScontrolId_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = WebLink.class)
    public JAXBElement<String> createWebLinkLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ShowsLocation", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkShowsLocation(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkShowsLocation_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Url", scope = WebLink.class)
    public JAXBElement<String> createWebLinkUrl(String value) {
        return new JAXBElement<String>(_WebLinkUrl_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = WebLink.class)
    public JAXBElement<User> createWebLinkLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenType", scope = WebLink.class)
    public JAXBElement<String> createWebLinkOpenType(String value) {
        return new JAXBElement<String>(_WebLinkOpenType_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LinkType", scope = WebLink.class)
    public JAXBElement<String> createWebLinkLinkType(String value) {
        return new JAXBElement<String>(_WebLinkLinkType_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = WebLink.class)
    public JAXBElement<XMLGregorianCalendar> createWebLinkLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Height", scope = WebLink.class)
    public JAXBElement<Integer> createWebLinkHeight(Integer value) {
        return new JAXBElement<Integer>(_WebLinkHeight_QNAME, Integer.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Width", scope = WebLink.class)
    public JAXBElement<Integer> createWebLinkWidth(Integer value) {
        return new JAXBElement<Integer>(_WebLinkWidth_QNAME, Integer.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasMenubar", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkHasMenubar(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkHasMenubar_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = WebLink.class)
    public JAXBElement<String> createWebLinkDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsResizable", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkIsResizable(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkIsResizable_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasToolbar", scope = WebLink.class)
    public JAXBElement<Boolean> createWebLinkHasToolbar(Boolean value) {
        return new JAXBElement<Boolean>(_WebLinkHasToolbar_QNAME, Boolean.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = WebLink.class)
    public JAXBElement<XMLGregorianCalendar> createWebLinkCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EncodingKey", scope = WebLink.class)
    public JAXBElement<String> createWebLinkEncodingKey(String value) {
        return new JAXBElement<String>(_WebLinkEncodingKey_QNAME, String.class, WebLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = Campaign.class)
    public JAXBElement<Boolean> createCampaignIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Campaign.class)
    public JAXBElement<String> createCampaignName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Campaign.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BudgetedCost", scope = Campaign.class)
    public JAXBElement<Double> createCampaignBudgetedCost(Double value) {
        return new JAXBElement<Double>(_CampaignBudgetedCost_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActualCost", scope = Campaign.class)
    public JAXBElement<Double> createCampaignActualCost(Double value) {
        return new JAXBElement<Double>(_CampaignActualCost_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfOpportunities", scope = Campaign.class)
    public JAXBElement<Integer> createCampaignNumberOfOpportunities(Integer value) {
        return new JAXBElement<Integer>(_CampaignNumberOfOpportunities_QNAME, Integer.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Campaign.class)
    public JAXBElement<String> createCampaignCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Campaign.class)
    public JAXBElement<Boolean> createCampaignIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AmountWonOpportunities", scope = Campaign.class)
    public JAXBElement<Double> createCampaignAmountWonOpportunities(Double value) {
        return new JAXBElement<Double>(_CampaignAmountWonOpportunities_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Campaign.class)
    public JAXBElement<User> createCampaignCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfContacts", scope = Campaign.class)
    public JAXBElement<Integer> createCampaignNumberOfContacts(Integer value) {
        return new JAXBElement<Integer>(_CampaignNumberOfContacts_QNAME, Integer.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Campaign.class)
    public JAXBElement<String> createCampaignType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AmountAllOpportunities", scope = Campaign.class)
    public JAXBElement<Double> createCampaignAmountAllOpportunities(Double value) {
        return new JAXBElement<Double>(_CampaignAmountAllOpportunities_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastActivityDate", scope = Campaign.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignLastActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountLastActivityDate_QNAME, XMLGregorianCalendar.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Leads", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignLeads(QueryResult value) {
        return new JAXBElement<QueryResult>(_CampaignLeads_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfResponses", scope = Campaign.class)
    public JAXBElement<Integer> createCampaignNumberOfResponses(Integer value) {
        return new JAXBElement<Integer>(_CampaignNumberOfResponses_QNAME, Integer.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Campaign.class)
    public JAXBElement<String> createCampaignLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunities", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignOpportunities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpportunities_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfWonOpportunities", scope = Campaign.class)
    public JAXBElement<Integer> createCampaignNumberOfWonOpportunities(Integer value) {
        return new JAXBElement<Integer>(_CampaignNumberOfWonOpportunities_QNAME, Integer.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Campaign.class)
    public JAXBElement<User> createCampaignLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfConvertedLeads", scope = Campaign.class)
    public JAXBElement<Integer> createCampaignNumberOfConvertedLeads(Integer value) {
        return new JAXBElement<Integer>(_CampaignNumberOfConvertedLeads_QNAME, Integer.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Campaign.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ExpectedResponse", scope = Campaign.class)
    public JAXBElement<Double> createCampaignExpectedResponse(Double value) {
        return new JAXBElement<Double>(_CampaignExpectedResponse_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfLeads", scope = Campaign.class)
    public JAXBElement<Integer> createCampaignNumberOfLeads(Integer value) {
        return new JAXBElement<Integer>(_CampaignNumberOfLeads_QNAME, Integer.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Campaign.class)
    public JAXBElement<String> createCampaignDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberSent", scope = Campaign.class)
    public JAXBElement<Double> createCampaignNumberSent(Double value) {
        return new JAXBElement<Double>(_CampaignNumberSent_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ExpectedRevenue", scope = Campaign.class)
    public JAXBElement<Double> createCampaignExpectedRevenue(Double value) {
        return new JAXBElement<Double>(_CampaignExpectedRevenue_QNAME, Double.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EndDate", scope = Campaign.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PeriodEndDate_QNAME, XMLGregorianCalendar.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Campaign.class)
    public JAXBElement<String> createCampaignStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Campaign.class)
    public JAXBElement<String> createCampaignOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CampaignMembers", scope = Campaign.class)
    public JAXBElement<QueryResult> createCampaignCampaignMembers(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactCampaignMembers_QNAME, QueryResult.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StartDate", scope = Campaign.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PeriodStartDate_QNAME, XMLGregorianCalendar.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Campaign.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Campaign.class)
    public JAXBElement<User> createCampaignOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Campaign.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstance", scope = ProcessInstanceStep.class)
    public JAXBElement<ProcessInstance> createProcessInstanceStepProcessInstance(ProcessInstance value) {
        return new JAXBElement<ProcessInstance>(_ProcessInstanceStepProcessInstance_QNAME, ProcessInstance.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Actor", scope = ProcessInstanceStep.class)
    public JAXBElement<Name> createProcessInstanceStepActor(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceStepActor_QNAME, Name.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StepStatus", scope = ProcessInstanceStep.class)
    public JAXBElement<String> createProcessInstanceStepStepStatus(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepStepStatus_QNAME, String.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActorId", scope = ProcessInstanceStep.class)
    public JAXBElement<String> createProcessInstanceStepActorId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepActorId_QNAME, String.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OriginalActor", scope = ProcessInstanceStep.class)
    public JAXBElement<Name> createProcessInstanceStepOriginalActor(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceStepOriginalActor_QNAME, Name.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ProcessInstanceStep.class)
    public JAXBElement<User> createProcessInstanceStepCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Comments", scope = ProcessInstanceStep.class)
    public JAXBElement<String> createProcessInstanceStepComments(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepComments_QNAME, String.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ProcessInstanceStep.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceStepSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OriginalActorId", scope = ProcessInstanceStep.class)
    public JAXBElement<String> createProcessInstanceStepOriginalActorId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepOriginalActorId_QNAME, String.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ProcessInstanceStep.class)
    public JAXBElement<String> createProcessInstanceStepCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstanceId", scope = ProcessInstanceStep.class)
    public JAXBElement<String> createProcessInstanceStepProcessInstanceId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepProcessInstanceId_QNAME, String.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ProcessInstanceStep.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceStepCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ProcessInstanceStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Amount", scope = OpportunityHistory.class)
    public JAXBElement<Double> createOpportunityHistoryAmount(Double value) {
        return new JAXBElement<Double>(_OpportunityHistoryAmount_QNAME, Double.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityHistorySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityHistory.class)
    public JAXBElement<Opportunity> createOpportunityHistoryOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityHistory.class)
    public JAXBElement<String> createOpportunityHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ForecastCategory", scope = OpportunityHistory.class)
    public JAXBElement<String> createOpportunityHistoryForecastCategory(String value) {
        return new JAXBElement<String>(_OpportunityHistoryForecastCategory_QNAME, String.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityHistory.class)
    public JAXBElement<Boolean> createOpportunityHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StageName", scope = OpportunityHistory.class)
    public JAXBElement<String> createOpportunityHistoryStageName(String value) {
        return new JAXBElement<String>(_OpportunityHistoryStageName_QNAME, String.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ExpectedRevenue", scope = OpportunityHistory.class)
    public JAXBElement<Double> createOpportunityHistoryExpectedRevenue(Double value) {
        return new JAXBElement<Double>(_CampaignExpectedRevenue_QNAME, Double.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityHistory.class)
    public JAXBElement<String> createOpportunityHistoryOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Probability", scope = OpportunityHistory.class)
    public JAXBElement<Double> createOpportunityHistoryProbability(Double value) {
        return new JAXBElement<Double>(_OpportunityHistoryProbability_QNAME, Double.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityHistory.class)
    public JAXBElement<User> createOpportunityHistoryCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CloseDate", scope = OpportunityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityHistoryCloseDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_OpportunityHistoryCloseDate_QNAME, XMLGregorianCalendar.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TargetObject", scope = ProcessInstanceHistory.class)
    public JAXBElement<Name> createProcessInstanceHistoryTargetObject(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceHistoryTargetObject_QNAME, Name.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActorId", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryActorId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepActorId_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Comments", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryComments(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepComments_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ProcessInstanceHistory.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceHistorySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OriginalActorId", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryOriginalActorId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepOriginalActorId_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RemindersSent", scope = ProcessInstanceHistory.class)
    public JAXBElement<Integer> createProcessInstanceHistoryRemindersSent(Integer value) {
        return new JAXBElement<Integer>(_ProcessInstanceHistoryRemindersSent_QNAME, Integer.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstanceId", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryProcessInstanceId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepProcessInstanceId_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = ProcessInstanceHistory.class)
    public JAXBElement<Boolean> createProcessInstanceHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TargetObjectId", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryTargetObjectId(String value) {
        return new JAXBElement<String>(_ProcessInstanceHistoryTargetObjectId_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPending", scope = ProcessInstanceHistory.class)
    public JAXBElement<Boolean> createProcessInstanceHistoryIsPending(Boolean value) {
        return new JAXBElement<Boolean>(_ProcessInstanceHistoryIsPending_QNAME, Boolean.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstance", scope = ProcessInstanceHistory.class)
    public JAXBElement<ProcessInstance> createProcessInstanceHistoryProcessInstance(ProcessInstance value) {
        return new JAXBElement<ProcessInstance>(_ProcessInstanceStepProcessInstance_QNAME, ProcessInstance.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Actor", scope = ProcessInstanceHistory.class)
    public JAXBElement<Name> createProcessInstanceHistoryActor(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceStepActor_QNAME, Name.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StepStatus", scope = ProcessInstanceHistory.class)
    public JAXBElement<String> createProcessInstanceHistoryStepStatus(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepStepStatus_QNAME, String.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ProcessInstanceHistory.class)
    public JAXBElement<User> createProcessInstanceHistoryCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OriginalActor", scope = ProcessInstanceHistory.class)
    public JAXBElement<Name> createProcessInstanceHistoryOriginalActor(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceStepOriginalActor_QNAME, Name.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ProcessInstanceHistory.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ProcessInstanceHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Solution }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Solution", scope = SolutionHistory.class)
    public JAXBElement<Solution> createSolutionHistorySolution(Solution value) {
        return new JAXBElement<Solution>(_SolutionHistorySolution_QNAME, Solution.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NewValue", scope = SolutionHistory.class)
    public JAXBElement<Object> createSolutionHistoryNewValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryNewValue_QNAME, Object.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OldValue", scope = SolutionHistory.class)
    public JAXBElement<Object> createSolutionHistoryOldValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryOldValue_QNAME, Object.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = SolutionHistory.class)
    public JAXBElement<Name> createSolutionHistoryCreatedBy(Name value) {
        return new JAXBElement<Name>(_EventCreatedBy_QNAME, Name.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SolutionId", scope = SolutionHistory.class)
    public JAXBElement<String> createSolutionHistorySolutionId(String value) {
        return new JAXBElement<String>(_SolutionHistorySolutionId_QNAME, String.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = SolutionHistory.class)
    public JAXBElement<String> createSolutionHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Field", scope = SolutionHistory.class)
    public JAXBElement<String> createSolutionHistoryField(String value) {
        return new JAXBElement<String>(_ContractHistoryField_QNAME, String.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = SolutionHistory.class)
    public JAXBElement<Boolean> createSolutionHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = SolutionHistory.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, SolutionHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RollupDescription", scope = UserRole.class)
    public JAXBElement<String> createUserRoleRollupDescription(String value) {
        return new JAXBElement<String>(_UserRoleRollupDescription_QNAME, String.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentRoleId", scope = UserRole.class)
    public JAXBElement<String> createUserRoleParentRoleId(String value) {
        return new JAXBElement<String>(_UserRoleParentRoleId_QNAME, String.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = UserRole.class)
    public JAXBElement<User> createUserRoleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = UserRole.class)
    public JAXBElement<String> createUserRoleName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Users", scope = UserRole.class)
    public JAXBElement<QueryResult> createUserRoleUsers(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserRoleUsers_QNAME, QueryResult.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = UserRole.class)
    public JAXBElement<XMLGregorianCalendar> createUserRoleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = UserRole.class)
    public JAXBElement<XMLGregorianCalendar> createUserRoleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityAccessForAccountOwner", scope = UserRole.class)
    public JAXBElement<String> createUserRoleOpportunityAccessForAccountOwner(String value) {
        return new JAXBElement<String>(_UserRoleOpportunityAccessForAccountOwner_QNAME, String.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = UserRole.class)
    public JAXBElement<String> createUserRoleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseAccessForAccountOwner", scope = UserRole.class)
    public JAXBElement<String> createUserRoleCaseAccessForAccountOwner(String value) {
        return new JAXBElement<String>(_UserRoleCaseAccessForAccountOwner_QNAME, String.class, UserRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityTeamMember.class)
    public JAXBElement<User> createOpportunityTeamMemberLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserId", scope = OpportunityTeamMember.class)
    public JAXBElement<String> createOpportunityTeamMemberUserId(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberUserId_QNAME, String.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityTeamMemberLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityTeamMemberSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityTeamMember.class)
    public JAXBElement<Opportunity> createOpportunityTeamMemberOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityTeamMember.class)
    public JAXBElement<String> createOpportunityTeamMemberCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityTeamMember.class)
    public JAXBElement<Boolean> createOpportunityTeamMemberIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityTeamMember.class)
    public JAXBElement<String> createOpportunityTeamMemberOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "User", scope = OpportunityTeamMember.class)
    public JAXBElement<User> createOpportunityTeamMemberUser(User value) {
        return new JAXBElement<User>(_OpportunityTeamMemberUser_QNAME, User.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityTeamMember.class)
    public JAXBElement<User> createOpportunityTeamMemberCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityAccessLevel", scope = OpportunityTeamMember.class)
    public JAXBElement<String> createOpportunityTeamMemberOpportunityAccessLevel(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberOpportunityAccessLevel_QNAME, String.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityTeamMember.class)
    public JAXBElement<String> createOpportunityTeamMemberLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityTeamMemberCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TeamMemberRole", scope = OpportunityTeamMember.class)
    public JAXBElement<String> createOpportunityTeamMemberTeamMemberRole(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberTeamMemberRole_QNAME, String.class, OpportunityTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = AccountPartner.class)
    public JAXBElement<User> createAccountPartnerLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountToId", scope = AccountPartner.class)
    public JAXBElement<String> createAccountPartnerAccountToId(String value) {
        return new JAXBElement<String>(_AccountPartnerAccountToId_QNAME, String.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountFromId", scope = AccountPartner.class)
    public JAXBElement<String> createAccountPartnerAccountFromId(String value) {
        return new JAXBElement<String>(_AccountPartnerAccountFromId_QNAME, String.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = AccountPartner.class)
    public JAXBElement<XMLGregorianCalendar> createAccountPartnerLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = AccountPartner.class)
    public JAXBElement<XMLGregorianCalendar> createAccountPartnerSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountTo", scope = AccountPartner.class)
    public JAXBElement<Account> createAccountPartnerAccountTo(Account value) {
        return new JAXBElement<Account>(_AccountPartnerAccountTo_QNAME, Account.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = AccountPartner.class)
    public JAXBElement<Opportunity> createAccountPartnerOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = AccountPartner.class)
    public JAXBElement<String> createAccountPartnerCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = AccountPartner.class)
    public JAXBElement<Boolean> createAccountPartnerIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountFrom", scope = AccountPartner.class)
    public JAXBElement<Account> createAccountPartnerAccountFrom(Account value) {
        return new JAXBElement<Account>(_AccountPartnerAccountFrom_QNAME, Account.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = AccountPartner.class)
    public JAXBElement<String> createAccountPartnerOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = AccountPartner.class)
    public JAXBElement<User> createAccountPartnerCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = AccountPartner.class)
    public JAXBElement<String> createAccountPartnerRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrimary", scope = AccountPartner.class)
    public JAXBElement<Boolean> createAccountPartnerIsPrimary(Boolean value) {
        return new JAXBElement<Boolean>(_AccountContactRoleIsPrimary_QNAME, Boolean.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = AccountPartner.class)
    public JAXBElement<String> createAccountPartnerLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = AccountPartner.class)
    public JAXBElement<XMLGregorianCalendar> createAccountPartnerCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, AccountPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ArgumentType", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<String> createApexPackageIdentifierArgumentArgumentType(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentArgumentType_QNAME, String.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ValueType", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<String> createApexPackageIdentifierArgumentValueType(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentValueType_QNAME, String.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ArgumentNumber", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<Integer> createApexPackageIdentifierArgumentArgumentNumber(Integer value) {
        return new JAXBElement<Integer>(_ApexPackageIdentifierArgumentArgumentNumber_QNAME, Integer.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ArgumentName", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<String> createApexPackageIdentifierArgumentArgumentName(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentArgumentName_QNAME, String.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApexPackageIdentifierId", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<String> createApexPackageIdentifierArgumentApexPackageIdentifierId(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentApexPackageIdentifierId_QNAME, String.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CollectionType", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<String> createApexPackageIdentifierArgumentCollectionType(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentCollectionType_QNAME, String.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApexPackageIdentifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApexPackageIdentifier", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<ApexPackageIdentifier> createApexPackageIdentifierArgumentApexPackageIdentifier(ApexPackageIdentifier value) {
        return new JAXBElement<ApexPackageIdentifier>(_ApexPackageIdentifierArgumentApexPackageIdentifier_QNAME, ApexPackageIdentifier.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApexPackageId", scope = ApexPackageIdentifierArgument.class)
    public JAXBElement<String> createApexPackageIdentifierArgumentApexPackageId(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentApexPackageId_QNAME, String.class, ApexPackageIdentifierArgument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Partner.class)
    public JAXBElement<User> createPartnerLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountToId", scope = Partner.class)
    public JAXBElement<String> createPartnerAccountToId(String value) {
        return new JAXBElement<String>(_AccountPartnerAccountToId_QNAME, String.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountFromId", scope = Partner.class)
    public JAXBElement<String> createPartnerAccountFromId(String value) {
        return new JAXBElement<String>(_AccountPartnerAccountFromId_QNAME, String.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Partner.class)
    public JAXBElement<XMLGregorianCalendar> createPartnerLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Partner.class)
    public JAXBElement<XMLGregorianCalendar> createPartnerSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountTo", scope = Partner.class)
    public JAXBElement<Account> createPartnerAccountTo(Account value) {
        return new JAXBElement<Account>(_AccountPartnerAccountTo_QNAME, Account.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = Partner.class)
    public JAXBElement<Opportunity> createPartnerOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Partner.class)
    public JAXBElement<String> createPartnerCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Partner.class)
    public JAXBElement<Boolean> createPartnerIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountFrom", scope = Partner.class)
    public JAXBElement<Account> createPartnerAccountFrom(Account value) {
        return new JAXBElement<Account>(_AccountPartnerAccountFrom_QNAME, Account.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = Partner.class)
    public JAXBElement<String> createPartnerOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Partner.class)
    public JAXBElement<User> createPartnerCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = Partner.class)
    public JAXBElement<String> createPartnerRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrimary", scope = Partner.class)
    public JAXBElement<Boolean> createPartnerIsPrimary(Boolean value) {
        return new JAXBElement<Boolean>(_AccountContactRoleIsPrimary_QNAME, Boolean.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Partner.class)
    public JAXBElement<String> createPartnerLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Partner.class)
    public JAXBElement<XMLGregorianCalendar> createPartnerCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Partner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = MailmergeTemplate.class)
    public JAXBElement<User> createMailmergeTemplateLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = MailmergeTemplate.class)
    public JAXBElement<String> createMailmergeTemplateName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = MailmergeTemplate.class)
    public JAXBElement<byte[]> createMailmergeTemplateBody(byte[] value) {
        return new JAXBElement<byte[]>(_AttachmentBody_QNAME, byte[].class, MailmergeTemplate.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Filename", scope = MailmergeTemplate.class)
    public JAXBElement<String> createMailmergeTemplateFilename(String value) {
        return new JAXBElement<String>(_MailmergeTemplateFilename_QNAME, String.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = MailmergeTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createMailmergeTemplateLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastUsedDate", scope = MailmergeTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createMailmergeTemplateLastUsedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EmailTemplateLastUsedDate_QNAME, XMLGregorianCalendar.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = MailmergeTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createMailmergeTemplateSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = MailmergeTemplate.class)
    public JAXBElement<String> createMailmergeTemplateCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BodyLength", scope = MailmergeTemplate.class)
    public JAXBElement<Integer> createMailmergeTemplateBodyLength(Integer value) {
        return new JAXBElement<Integer>(_AttachmentBodyLength_QNAME, Integer.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = MailmergeTemplate.class)
    public JAXBElement<Boolean> createMailmergeTemplateIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = MailmergeTemplate.class)
    public JAXBElement<String> createMailmergeTemplateDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = MailmergeTemplate.class)
    public JAXBElement<User> createMailmergeTemplateCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = MailmergeTemplate.class)
    public JAXBElement<String> createMailmergeTemplateLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = MailmergeTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createMailmergeTemplateCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, MailmergeTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = LeadStatus.class)
    public JAXBElement<User> createLeadStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = LeadStatus.class)
    public JAXBElement<String> createLeadStatusMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsConverted", scope = LeadStatus.class)
    public JAXBElement<Boolean> createLeadStatusIsConverted(Boolean value) {
        return new JAXBElement<Boolean>(_LeadStatusIsConverted_QNAME, Boolean.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = LeadStatus.class)
    public JAXBElement<Integer> createLeadStatusSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = LeadStatus.class)
    public JAXBElement<User> createLeadStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = LeadStatus.class)
    public JAXBElement<XMLGregorianCalendar> createLeadStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = LeadStatus.class)
    public JAXBElement<XMLGregorianCalendar> createLeadStatusSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = LeadStatus.class)
    public JAXBElement<String> createLeadStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = LeadStatus.class)
    public JAXBElement<String> createLeadStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = LeadStatus.class)
    public JAXBElement<XMLGregorianCalendar> createLeadStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = LeadStatus.class)
    public JAXBElement<Boolean> createLeadStatusIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, LeadStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Name.class)
    public JAXBElement<String> createNameName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Name.class)
    public JAXBElement<String> createNameType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Alias", scope = Name.class)
    public JAXBElement<String> createNameAlias(String value) {
        return new JAXBElement<String>(_NameAlias_QNAME, String.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserRoleId", scope = Name.class)
    public JAXBElement<String> createNameUserRoleId(String value) {
        return new JAXBElement<String>(_NameUserRoleId_QNAME, String.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstName", scope = Name.class)
    public JAXBElement<String> createNameFirstName(String value) {
        return new JAXBElement<String>(_ContactFirstName_QNAME, String.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserRole", scope = Name.class)
    public JAXBElement<UserRole> createNameUserRole(UserRole value) {
        return new JAXBElement<UserRole>(_NameUserRole_QNAME, UserRole.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastName", scope = Name.class)
    public JAXBElement<String> createNameLastName(String value) {
        return new JAXBElement<String>(_ContactLastName_QNAME, String.class, Name.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LanguageLocaleKey", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserLanguageLocaleKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserLanguageLocaleKey_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = SelfServiceUser.class)
    public JAXBElement<Boolean> createSelfServiceUserIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = SelfServiceUser.class)
    public JAXBElement<User> createSelfServiceUserLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Username", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserUsername(String value) {
        return new JAXBElement<String>(_SelfServiceUserUsername_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = SelfServiceUser.class)
    public JAXBElement<XMLGregorianCalendar> createSelfServiceUserLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = SelfServiceUser.class)
    public JAXBElement<XMLGregorianCalendar> createSelfServiceUserSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstName", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserFirstName(String value) {
        return new JAXBElement<String>(_ContactFirstName_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastLoginDate", scope = SelfServiceUser.class)
    public JAXBElement<XMLGregorianCalendar> createSelfServiceUserLastLoginDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SelfServiceUserLastLoginDate_QNAME, XMLGregorianCalendar.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Email", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserEmail(String value) {
        return new JAXBElement<String>(_ContactEmail_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TimeZoneSidKey", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserTimeZoneSidKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserTimeZoneSidKey_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SuperUser", scope = SelfServiceUser.class)
    public JAXBElement<Boolean> createSelfServiceUserSuperUser(Boolean value) {
        return new JAXBElement<Boolean>(_SelfServiceUserSuperUser_QNAME, Boolean.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LocaleSidKey", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserLocaleSidKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserLocaleSidKey_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = SelfServiceUser.class)
    public JAXBElement<User> createSelfServiceUserCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = SelfServiceUser.class)
    public JAXBElement<XMLGregorianCalendar> createSelfServiceUserCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastName", scope = SelfServiceUser.class)
    public JAXBElement<String> createSelfServiceUserLastName(String value) {
        return new JAXBElement<String>(_ContactLastName_QNAME, String.class, SelfServiceUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Approval.class)
    public JAXBElement<User> createApprovalLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApproveComment", scope = Approval.class)
    public JAXBElement<String> createApprovalApproveComment(String value) {
        return new JAXBElement<String>(_ApprovalApproveComment_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Approval.class)
    public JAXBElement<XMLGregorianCalendar> createApprovalLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Approval.class)
    public JAXBElement<XMLGregorianCalendar> createApprovalSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Approval.class)
    public JAXBElement<String> createApprovalCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = Approval.class)
    public JAXBElement<String> createApprovalParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Approval.class)
    public JAXBElement<Boolean> createApprovalIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RequestComment", scope = Approval.class)
    public JAXBElement<String> createApprovalRequestComment(String value) {
        return new JAXBElement<String>(_ApprovalRequestComment_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Approval.class)
    public JAXBElement<User> createApprovalCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Approval.class)
    public JAXBElement<String> createApprovalStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Approval.class)
    public JAXBElement<String> createApprovalOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Approval.class)
    public JAXBElement<String> createApprovalLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Approval.class)
    public JAXBElement<XMLGregorianCalendar> createApprovalCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Approval.class)
    public JAXBElement<User> createApprovalOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Approval.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "GroupId", scope = GroupMember.class)
    public JAXBElement<String> createGroupMemberGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberGroupId_QNAME, String.class, GroupMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = GroupMember.class)
    public JAXBElement<XMLGregorianCalendar> createGroupMemberSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, GroupMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserOrGroupId", scope = GroupMember.class)
    public JAXBElement<String> createGroupMemberUserOrGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberUserOrGroupId_QNAME, String.class, GroupMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Group }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Group", scope = GroupMember.class)
    public JAXBElement<Group> createGroupMemberGroup(Group value) {
        return new JAXBElement<Group>(_GroupMemberGroup_QNAME, Group.class, GroupMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = Pricebook2 .class)
    public JAXBElement<Boolean> createPricebook2IsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Pricebook2 .class)
    public JAXBElement<User> createPricebook2LastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Pricebook2 .class)
    public JAXBElement<String> createPricebook2Name(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Pricebook2 .class)
    public JAXBElement<XMLGregorianCalendar> createPricebook2LastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Pricebook2 .class)
    public JAXBElement<XMLGregorianCalendar> createPricebook2SystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Pricebook2 .class)
    public JAXBElement<String> createPricebook2CreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Pricebook2 .class)
    public JAXBElement<Boolean> createPricebook2IsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsStandard", scope = Pricebook2 .class)
    public JAXBElement<Boolean> createPricebook2IsStandard(Boolean value) {
        return new JAXBElement<Boolean>(_Pricebook2IsStandard_QNAME, Boolean.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Pricebook2 .class)
    public JAXBElement<String> createPricebook2Description(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PricebookEntries", scope = Pricebook2 .class)
    public JAXBElement<QueryResult> createPricebook2PricebookEntries(QueryResult value) {
        return new JAXBElement<QueryResult>(_Pricebook2PricebookEntries_QNAME, QueryResult.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Pricebook2 .class)
    public JAXBElement<User> createPricebook2CreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Pricebook2 .class)
    public JAXBElement<String> createPricebook2LastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Pricebook2 .class)
    public JAXBElement<XMLGregorianCalendar> createPricebook2CreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunities", scope = Pricebook2 .class)
    public JAXBElement<QueryResult> createPricebook2Opportunities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpportunities_QNAME, QueryResult.class, Pricebook2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Street", scope = Organization.class)
    public JAXBElement<String> createOrganizationStreet(String value) {
        return new JAXBElement<String>(_OrganizationStreet_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LanguageLocaleKey", scope = Organization.class)
    public JAXBElement<String> createOrganizationLanguageLocaleKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserLanguageLocaleKey_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultCalendarAccess", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultCalendarAccess(String value) {
        return new JAXBElement<String>(_OrganizationDefaultCalendarAccess_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsesStartDateAsFiscalYearName", scope = Organization.class)
    public JAXBElement<Boolean> createOrganizationUsesStartDateAsFiscalYearName(Boolean value) {
        return new JAXBElement<Boolean>(_OrganizationUsesStartDateAsFiscalYearName_QNAME, Boolean.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Organization.class)
    public JAXBElement<String> createOrganizationName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReceivesAdminInfoEmails", scope = Organization.class)
    public JAXBElement<Boolean> createOrganizationReceivesAdminInfoEmails(Boolean value) {
        return new JAXBElement<Boolean>(_OrganizationReceivesAdminInfoEmails_QNAME, Boolean.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PreferencesRequireOpportunityProducts", scope = Organization.class)
    public JAXBElement<Boolean> createOrganizationPreferencesRequireOpportunityProducts(Boolean value) {
        return new JAXBElement<Boolean>(_OrganizationPreferencesRequireOpportunityProducts_QNAME, Boolean.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OrganizationType", scope = Organization.class)
    public JAXBElement<String> createOrganizationOrganizationType(String value) {
        return new JAXBElement<String>(_OrganizationOrganizationType_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TrialExpirationDate", scope = Organization.class)
    public JAXBElement<XMLGregorianCalendar> createOrganizationTrialExpirationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_OrganizationTrialExpirationDate_QNAME, XMLGregorianCalendar.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultOpportunityAccess", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultOpportunityAccess(String value) {
        return new JAXBElement<String>(_OrganizationDefaultOpportunityAccess_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Organization.class)
    public JAXBElement<XMLGregorianCalendar> createOrganizationSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Organization.class)
    public JAXBElement<String> createOrganizationCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultCaseAccess", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultCaseAccess(String value) {
        return new JAXBElement<String>(_OrganizationDefaultCaseAccess_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultLeadAccess", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultLeadAccess(String value) {
        return new JAXBElement<String>(_OrganizationDefaultLeadAccess_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReceivesInfoEmails", scope = Organization.class)
    public JAXBElement<Boolean> createOrganizationReceivesInfoEmails(Boolean value) {
        return new JAXBElement<Boolean>(_OrganizationReceivesInfoEmails_QNAME, Boolean.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MaxRulesPerEntity", scope = Organization.class)
    public JAXBElement<Integer> createOrganizationMaxRulesPerEntity(Integer value) {
        return new JAXBElement<Integer>(_OrganizationMaxRulesPerEntity_QNAME, Integer.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Organization.class)
    public JAXBElement<User> createOrganizationCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PrimaryContact", scope = Organization.class)
    public JAXBElement<String> createOrganizationPrimaryContact(String value) {
        return new JAXBElement<String>(_OrganizationPrimaryContact_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WebToCaseDefaultOrigin", scope = Organization.class)
    public JAXBElement<String> createOrganizationWebToCaseDefaultOrigin(String value) {
        return new JAXBElement<String>(_OrganizationWebToCaseDefaultOrigin_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Division", scope = Organization.class)
    public JAXBElement<String> createOrganizationDivision(String value) {
        return new JAXBElement<String>(_OrganizationDivision_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Organization.class)
    public JAXBElement<String> createOrganizationLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "State", scope = Organization.class)
    public JAXBElement<String> createOrganizationState(String value) {
        return new JAXBElement<String>(_OrganizationState_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Organization.class)
    public JAXBElement<User> createOrganizationLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Phone", scope = Organization.class)
    public JAXBElement<String> createOrganizationPhone(String value) {
        return new JAXBElement<String>(_AccountPhone_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Organization.class)
    public JAXBElement<XMLGregorianCalendar> createOrganizationLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultLocaleSidKey", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultLocaleSidKey(String value) {
        return new JAXBElement<String>(_OrganizationDefaultLocaleSidKey_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ComplianceBccEmail", scope = Organization.class)
    public JAXBElement<String> createOrganizationComplianceBccEmail(String value) {
        return new JAXBElement<String>(_OrganizationComplianceBccEmail_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PostalCode", scope = Organization.class)
    public JAXBElement<String> createOrganizationPostalCode(String value) {
        return new JAXBElement<String>(_OrganizationPostalCode_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultAccountAndContactAccess", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultAccountAndContactAccess(String value) {
        return new JAXBElement<String>(_OrganizationDefaultAccountAndContactAccess_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UiSkin", scope = Organization.class)
    public JAXBElement<String> createOrganizationUiSkin(String value) {
        return new JAXBElement<String>(_OrganizationUiSkin_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultPricebookAccess", scope = Organization.class)
    public JAXBElement<String> createOrganizationDefaultPricebookAccess(String value) {
        return new JAXBElement<String>(_OrganizationDefaultPricebookAccess_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "City", scope = Organization.class)
    public JAXBElement<String> createOrganizationCity(String value) {
        return new JAXBElement<String>(_OrganizationCity_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Country", scope = Organization.class)
    public JAXBElement<String> createOrganizationCountry(String value) {
        return new JAXBElement<String>(_OrganizationCountry_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FiscalYearStartMonth", scope = Organization.class)
    public JAXBElement<Integer> createOrganizationFiscalYearStartMonth(Integer value) {
        return new JAXBElement<Integer>(_OrganizationFiscalYearStartMonth_QNAME, Integer.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Fax", scope = Organization.class)
    public JAXBElement<String> createOrganizationFax(String value) {
        return new JAXBElement<String>(_AccountFax_QNAME, String.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MaxActionsPerRule", scope = Organization.class)
    public JAXBElement<Integer> createOrganizationMaxActionsPerRule(Integer value) {
        return new JAXBElement<Integer>(_OrganizationMaxActionsPerRule_QNAME, Integer.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Organization.class)
    public JAXBElement<XMLGregorianCalendar> createOrganizationCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Organization.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RowCause", scope = OpportunityShare.class)
    public JAXBElement<String> createOpportunityShareRowCause(String value) {
        return new JAXBElement<String>(_OpportunityShareRowCause_QNAME, String.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityShare.class)
    public JAXBElement<User> createOpportunityShareLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityShare.class)
    public JAXBElement<String> createOpportunityShareOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityAccessLevel", scope = OpportunityShare.class)
    public JAXBElement<String> createOpportunityShareOpportunityAccessLevel(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberOpportunityAccessLevel_QNAME, String.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityShare.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityShareLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserOrGroupId", scope = OpportunityShare.class)
    public JAXBElement<String> createOpportunityShareUserOrGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberUserOrGroupId_QNAME, String.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityShare.class)
    public JAXBElement<Opportunity> createOpportunityShareOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityShare.class)
    public JAXBElement<String> createOpportunityShareLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityShare.class)
    public JAXBElement<Boolean> createOpportunityShareIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Group }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Queue", scope = QueueSobject.class)
    public JAXBElement<Group> createQueueSobjectQueue(Group value) {
        return new JAXBElement<Group>(_QueueSobjectQueue_QNAME, Group.class, QueueSobject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = QueueSobject.class)
    public JAXBElement<User> createQueueSobjectCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, QueueSobject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = QueueSobject.class)
    public JAXBElement<XMLGregorianCalendar> createQueueSobjectSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, QueueSobject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = QueueSobject.class)
    public JAXBElement<String> createQueueSobjectCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, QueueSobject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SobjectType", scope = QueueSobject.class)
    public JAXBElement<String> createQueueSobjectSobjectType(String value) {
        return new JAXBElement<String>(_QueueSobjectSobjectType_QNAME, String.class, QueueSobject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "QueueId", scope = QueueSobject.class)
    public JAXBElement<String> createQueueSobjectQueueId(String value) {
        return new JAXBElement<String>(_QueueSobjectQueueId_QNAME, String.class, QueueSobject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = ApexTrigger.class)
    public JAXBElement<User> createApexTriggerLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = ApexTrigger.class)
    public JAXBElement<String> createApexTriggerName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = ApexTrigger.class)
    public JAXBElement<String> createApexTriggerBody(String value) {
        return new JAXBElement<String>(_AttachmentBody_QNAME, String.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NamespacePrefix", scope = ApexTrigger.class)
    public JAXBElement<String> createApexTriggerNamespacePrefix(String value) {
        return new JAXBElement<String>(_WebLinkNamespacePrefix_QNAME, String.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageAfterUpdate", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerUsageAfterUpdate(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerUsageAfterUpdate_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = ApexTrigger.class)
    public JAXBElement<XMLGregorianCalendar> createApexTriggerLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageAfterInsert", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerUsageAfterInsert(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerUsageAfterInsert_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TableEnumOrId", scope = ApexTrigger.class)
    public JAXBElement<String> createApexTriggerTableEnumOrId(String value) {
        return new JAXBElement<String>(_BusinessProcessTableEnumOrId_QNAME, String.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ApexTrigger.class)
    public JAXBElement<XMLGregorianCalendar> createApexTriggerSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ApexTrigger.class)
    public JAXBElement<String> createApexTriggerCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageBeforeDelete", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerUsageBeforeDelete(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerUsageBeforeDelete_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BodyCrc", scope = ApexTrigger.class)
    public JAXBElement<Double> createApexTriggerBodyCrc(Double value) {
        return new JAXBElement<Double>(_ApexTriggerBodyCrc_QNAME, Double.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageBeforeUpdate", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerUsageBeforeUpdate(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerUsageBeforeUpdate_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageAfterDelete", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerUsageAfterDelete(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerUsageAfterDelete_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageBeforeInsert", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerUsageBeforeInsert(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerUsageBeforeInsert_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ApexTrigger.class)
    public JAXBElement<User> createApexTriggerCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = ApexTrigger.class)
    public JAXBElement<String> createApexTriggerLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApiVersion", scope = ApexTrigger.class)
    public JAXBElement<Double> createApexTriggerApiVersion(Double value) {
        return new JAXBElement<Double>(_ApexTriggerApiVersion_QNAME, Double.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ApexTrigger.class)
    public JAXBElement<XMLGregorianCalendar> createApexTriggerCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsValid", scope = ApexTrigger.class)
    public JAXBElement<Boolean> createApexTriggerIsValid(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerIsValid_QNAME, Boolean.class, ApexTrigger.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = ApexPackage.class)
    public JAXBElement<User> createApexPackageLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = ApexPackage.class)
    public JAXBElement<String> createApexPackageName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = ApexPackage.class)
    public JAXBElement<String> createApexPackageBody(String value) {
        return new JAXBElement<String>(_AttachmentBody_QNAME, String.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NamespacePrefix", scope = ApexPackage.class)
    public JAXBElement<String> createApexPackageNamespacePrefix(String value) {
        return new JAXBElement<String>(_WebLinkNamespacePrefix_QNAME, String.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = ApexPackage.class)
    public JAXBElement<XMLGregorianCalendar> createApexPackageLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ApexPackage.class)
    public JAXBElement<XMLGregorianCalendar> createApexPackageSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Methods", scope = ApexPackage.class)
    public JAXBElement<QueryResult> createApexPackageMethods(QueryResult value) {
        return new JAXBElement<QueryResult>(_ApexPackageMethods_QNAME, QueryResult.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ApexPackage.class)
    public JAXBElement<String> createApexPackageCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BodyCrc", scope = ApexPackage.class)
    public JAXBElement<Double> createApexPackageBodyCrc(Double value) {
        return new JAXBElement<Double>(_ApexTriggerBodyCrc_QNAME, Double.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ApexPackage.class)
    public JAXBElement<User> createApexPackageCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = ApexPackage.class)
    public JAXBElement<String> createApexPackageLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApiVersion", scope = ApexPackage.class)
    public JAXBElement<Double> createApexPackageApiVersion(Double value) {
        return new JAXBElement<Double>(_ApexTriggerApiVersion_QNAME, Double.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ApexPackage.class)
    public JAXBElement<XMLGregorianCalendar> createApexPackageCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsValid", scope = ApexPackage.class)
    public JAXBElement<Boolean> createApexPackageIsValid(Boolean value) {
        return new JAXBElement<Boolean>(_ApexTriggerIsValid_QNAME, Boolean.class, ApexPackage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortStyle", scope = CategoryNode.class)
    public JAXBElement<String> createCategoryNodeSortStyle(String value) {
        return new JAXBElement<String>(_CategoryNodeSortStyle_QNAME, String.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CategoryNode.class)
    public JAXBElement<User> createCategoryNodeLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = CategoryNode.class)
    public JAXBElement<String> createCategoryNodeMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = CategoryNode.class)
    public JAXBElement<Integer> createCategoryNodeSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CategoryNode.class)
    public JAXBElement<User> createCategoryNodeCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CategoryNode.class)
    public JAXBElement<XMLGregorianCalendar> createCategoryNodeLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CategoryNode.class)
    public JAXBElement<XMLGregorianCalendar> createCategoryNodeSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CategoryNode.class)
    public JAXBElement<String> createCategoryNodeCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = CategoryNode.class)
    public JAXBElement<String> createCategoryNodeParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CategoryNode.class)
    public JAXBElement<String> createCategoryNodeLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CategoryNode.class)
    public JAXBElement<XMLGregorianCalendar> createCategoryNodeCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CategoryNode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CaseComment.class)
    public JAXBElement<Name> createCaseCommentLastModifiedBy(Name value) {
        return new JAXBElement<Name>(_EventLastModifiedBy_QNAME, Name.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CommentBody", scope = CaseComment.class)
    public JAXBElement<String> createCaseCommentCommentBody(String value) {
        return new JAXBElement<String>(_CaseCommentCommentBody_QNAME, String.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Case }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Parent", scope = CaseComment.class)
    public JAXBElement<Case> createCaseCommentParent(Case value) {
        return new JAXBElement<Case>(_AccountParent_QNAME, Case.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CaseComment.class)
    public JAXBElement<Name> createCaseCommentCreatedBy(Name value) {
        return new JAXBElement<Name>(_EventCreatedBy_QNAME, Name.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CaseComment.class)
    public JAXBElement<XMLGregorianCalendar> createCaseCommentLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPublished", scope = CaseComment.class)
    public JAXBElement<Boolean> createCaseCommentIsPublished(Boolean value) {
        return new JAXBElement<Boolean>(_CaseCommentIsPublished_QNAME, Boolean.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CaseComment.class)
    public JAXBElement<XMLGregorianCalendar> createCaseCommentSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CaseComment.class)
    public JAXBElement<String> createCaseCommentCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = CaseComment.class)
    public JAXBElement<String> createCaseCommentParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CaseComment.class)
    public JAXBElement<String> createCaseCommentLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CaseComment.class)
    public JAXBElement<Boolean> createCaseCommentIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CaseComment.class)
    public JAXBElement<XMLGregorianCalendar> createCaseCommentCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CaseComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityContactRole.class)
    public JAXBElement<User> createOpportunityContactRoleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityContactRoleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityContactRoleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityContactRole.class)
    public JAXBElement<Opportunity> createOpportunityContactRoleOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityContactRole.class)
    public JAXBElement<String> createOpportunityContactRoleCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = OpportunityContactRole.class)
    public JAXBElement<Contact> createOpportunityContactRoleContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityContactRole.class)
    public JAXBElement<Boolean> createOpportunityContactRoleIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityContactRole.class)
    public JAXBElement<String> createOpportunityContactRoleOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityContactRole.class)
    public JAXBElement<User> createOpportunityContactRoleCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = OpportunityContactRole.class)
    public JAXBElement<String> createOpportunityContactRoleRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = OpportunityContactRole.class)
    public JAXBElement<String> createOpportunityContactRoleContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrimary", scope = OpportunityContactRole.class)
    public JAXBElement<Boolean> createOpportunityContactRoleIsPrimary(Boolean value) {
        return new JAXBElement<Boolean>(_AccountContactRoleIsPrimary_QNAME, Boolean.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityContactRole.class)
    public JAXBElement<String> createOpportunityContactRoleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityContactRoleCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivatedDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractActivatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContractActivatedDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingCountry", scope = Contract.class)
    public JAXBElement<String> createContractBillingCountry(String value) {
        return new JAXBElement<String>(_AccountBillingCountry_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerExpirationNotice", scope = Contract.class)
    public JAXBElement<String> createContractOwnerExpirationNotice(String value) {
        return new JAXBElement<String>(_ContractOwnerExpirationNotice_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Contract.class)
    public JAXBElement<String> createContractAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Contract.class)
    public JAXBElement<QueryResult> createContractAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Contract.class)
    public JAXBElement<QueryResult> createContractTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivatedBy", scope = Contract.class)
    public JAXBElement<User> createContractActivatedBy(User value) {
        return new JAXBElement<User>(_ContractActivatedBy_QNAME, User.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Contract.class)
    public JAXBElement<Boolean> createContractIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Contract.class)
    public JAXBElement<QueryResult> createContractActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Contract.class)
    public JAXBElement<QueryResult> createContractNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingPostalCode", scope = Contract.class)
    public JAXBElement<String> createContractBillingPostalCode(String value) {
        return new JAXBElement<String>(_AccountBillingPostalCode_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Contract.class)
    public JAXBElement<User> createContractCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingState", scope = Contract.class)
    public JAXBElement<String> createContractBillingState(String value) {
        return new JAXBElement<String>(_AccountBillingState_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastActivityDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractLastActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountLastActivityDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Contract.class)
    public JAXBElement<QueryResult> createContractProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StatusCode", scope = Contract.class)
    public JAXBElement<String> createContractStatusCode(String value) {
        return new JAXBElement<String>(_ContractStatusCode_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CompanySignedId", scope = Contract.class)
    public JAXBElement<String> createContractCompanySignedId(String value) {
        return new JAXBElement<String>(_ContractCompanySignedId_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Contract.class)
    public JAXBElement<QueryResult> createContractOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivatedById", scope = Contract.class)
    public JAXBElement<String> createContractActivatedById(String value) {
        return new JAXBElement<String>(_ContractActivatedById_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Contract.class)
    public JAXBElement<User> createContractLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CustomerSigned", scope = Contract.class)
    public JAXBElement<Contact> createContractCustomerSigned(Contact value) {
        return new JAXBElement<Contact>(_ContractCustomerSigned_QNAME, Contact.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CustomerSignedTitle", scope = Contract.class)
    public JAXBElement<String> createContractCustomerSignedTitle(String value) {
        return new JAXBElement<String>(_ContractCustomerSignedTitle_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SpecialTerms", scope = Contract.class)
    public JAXBElement<String> createContractSpecialTerms(String value) {
        return new JAXBElement<String>(_ContractSpecialTerms_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Contract.class)
    public JAXBElement<QueryResult> createContractNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Contract.class)
    public JAXBElement<String> createContractDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractTerm", scope = Contract.class)
    public JAXBElement<Integer> createContractContractTerm(Integer value) {
        return new JAXBElement<Integer>(_ContractContractTerm_QNAME, Integer.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EndDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PeriodEndDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Contract.class)
    public JAXBElement<QueryResult> createContractEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StartDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PeriodStartDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CompanySigned", scope = Contract.class)
    public JAXBElement<User> createContractCompanySigned(User value) {
        return new JAXBElement<User>(_ContractCompanySigned_QNAME, User.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Contract.class)
    public JAXBElement<String> createContractCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Contract.class)
    public JAXBElement<String> createContractLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Contract.class)
    public JAXBElement<QueryResult> createContractProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = Contract.class)
    public JAXBElement<Account> createContractAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CompanySignedDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractCompanySignedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContractCompanySignedDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CustomerSignedDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractCustomerSignedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContractCustomerSignedDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractContactRoles", scope = Contract.class)
    public JAXBElement<QueryResult> createContractContractContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactContractContactRoles_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Histories", scope = Contract.class)
    public JAXBElement<QueryResult> createContractHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContractHistories_QNAME, QueryResult.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingCity", scope = Contract.class)
    public JAXBElement<String> createContractBillingCity(String value) {
        return new JAXBElement<String>(_AccountBillingCity_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastApprovedDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractLastApprovedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ContractLastApprovedDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Contract.class)
    public JAXBElement<String> createContractStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Contract.class)
    public JAXBElement<String> createContractOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BillingStreet", scope = Contract.class)
    public JAXBElement<String> createContractBillingStreet(String value) {
        return new JAXBElement<String>(_AccountBillingStreet_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CustomerSignedId", scope = Contract.class)
    public JAXBElement<String> createContractCustomerSignedId(String value) {
        return new JAXBElement<String>(_ContractCustomerSignedId_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Contract.class)
    public JAXBElement<XMLGregorianCalendar> createContractCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractNumber", scope = Contract.class)
    public JAXBElement<String> createContractContractNumber(String value) {
        return new JAXBElement<String>(_ContractContractNumber_QNAME, String.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Contract.class)
    public JAXBElement<User> createContractOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Contract.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsTask", scope = OpenActivity.class)
    public JAXBElement<Boolean> createOpenActivityIsTask(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsTask_QNAME, Boolean.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpenActivity.class)
    public JAXBElement<XMLGregorianCalendar> createOpenActivitySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpenActivity.class)
    public JAXBElement<Boolean> createOpenActivityIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpenActivity.class)
    public JAXBElement<User> createOpenActivityCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = OpenActivity.class)
    public JAXBElement<Boolean> createOpenActivityIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsAllDayEvent", scope = OpenActivity.class)
    public JAXBElement<Boolean> createOpenActivityIsAllDayEvent(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsAllDayEvent_QNAME, Boolean.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Who", scope = OpenActivity.class)
    public JAXBElement<Name> createOpenActivityWho(Name value) {
        return new JAXBElement<Name>(_EventWho_QNAME, Name.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "What", scope = OpenActivity.class)
    public JAXBElement<Name> createOpenActivityWhat(Name value) {
        return new JAXBElement<Name>(_EventWhat_QNAME, Name.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpenActivity.class)
    public JAXBElement<User> createOpenActivityLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = OpenActivity.class)
    public JAXBElement<Account> createOpenActivityAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpenActivity.class)
    public JAXBElement<XMLGregorianCalendar> createOpenActivityLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhatId", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityWhatId(String value) {
        return new JAXBElement<String>(_EventWhatId_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityDate", scope = OpenActivity.class)
    public JAXBElement<XMLGregorianCalendar> createOpenActivityActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventActivityDate_QNAME, XMLGregorianCalendar.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Subject", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivitySubject(String value) {
        return new JAXBElement<String>(_EventSubject_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityType", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityActivityType(String value) {
        return new JAXBElement<String>(_OpenActivityActivityType_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhoId", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityWhoId(String value) {
        return new JAXBElement<String>(_EventWhoId_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DurationInMinutes", scope = OpenActivity.class)
    public JAXBElement<Integer> createOpenActivityDurationInMinutes(Integer value) {
        return new JAXBElement<Integer>(_EventDurationInMinutes_QNAME, Integer.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Priority", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityPriority(String value) {
        return new JAXBElement<String>(_OpenActivityPriority_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Location", scope = OpenActivity.class)
    public JAXBElement<String> createOpenActivityLocation(String value) {
        return new JAXBElement<String>(_EventLocation_QNAME, String.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpenActivity.class)
    public JAXBElement<XMLGregorianCalendar> createOpenActivityCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = OpenActivity.class)
    public JAXBElement<Name> createOpenActivityOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, OpenActivity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CaseStatus.class)
    public JAXBElement<User> createCaseStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = CaseStatus.class)
    public JAXBElement<String> createCaseStatusMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = CaseStatus.class)
    public JAXBElement<Integer> createCaseStatusSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CaseStatus.class)
    public JAXBElement<User> createCaseStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CaseStatus.class)
    public JAXBElement<XMLGregorianCalendar> createCaseStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CaseStatus.class)
    public JAXBElement<XMLGregorianCalendar> createCaseStatusSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = CaseStatus.class)
    public JAXBElement<Boolean> createCaseStatusIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CaseStatus.class)
    public JAXBElement<String> createCaseStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CaseStatus.class)
    public JAXBElement<String> createCaseStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CaseStatus.class)
    public JAXBElement<XMLGregorianCalendar> createCaseStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = CaseStatus.class)
    public JAXBElement<Boolean> createCaseStatusIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, CaseStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RowCause", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareRowCause(String value) {
        return new JAXBElement<String>(_OpportunityShareRowCause_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = AccountShare.class)
    public JAXBElement<User> createAccountShareLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountAccessLevel", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareAccountAccessLevel(String value) {
        return new JAXBElement<String>(_AccountShareAccountAccessLevel_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = AccountShare.class)
    public JAXBElement<Account> createAccountShareAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityAccessLevel", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareOpportunityAccessLevel(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberOpportunityAccessLevel_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = AccountShare.class)
    public JAXBElement<XMLGregorianCalendar> createAccountShareLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseAccessLevel", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareCaseAccessLevel(String value) {
        return new JAXBElement<String>(_AccountShareCaseAccessLevel_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserOrGroupId", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareUserOrGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberUserOrGroupId_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = AccountShare.class)
    public JAXBElement<String> createAccountShareLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = AccountShare.class)
    public JAXBElement<Boolean> createAccountShareIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, AccountShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RespondedDate", scope = EventAttendee.class)
    public JAXBElement<XMLGregorianCalendar> createEventAttendeeRespondedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventAttendeeRespondedDate_QNAME, XMLGregorianCalendar.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = EventAttendee.class)
    public JAXBElement<User> createEventAttendeeLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AttendeeId", scope = EventAttendee.class)
    public JAXBElement<String> createEventAttendeeAttendeeId(String value) {
        return new JAXBElement<String>(_EventAttendeeAttendeeId_QNAME, String.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EventId", scope = EventAttendee.class)
    public JAXBElement<String> createEventAttendeeEventId(String value) {
        return new JAXBElement<String>(_EventAttendeeEventId_QNAME, String.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = EventAttendee.class)
    public JAXBElement<XMLGregorianCalendar> createEventAttendeeLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Response", scope = EventAttendee.class)
    public JAXBElement<String> createEventAttendeeResponse(String value) {
        return new JAXBElement<String>(_EventAttendeeResponse_QNAME, String.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = EventAttendee.class)
    public JAXBElement<XMLGregorianCalendar> createEventAttendeeSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = EventAttendee.class)
    public JAXBElement<String> createEventAttendeeCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = EventAttendee.class)
    public JAXBElement<Boolean> createEventAttendeeIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = EventAttendee.class)
    public JAXBElement<User> createEventAttendeeCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = EventAttendee.class)
    public JAXBElement<String> createEventAttendeeStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = EventAttendee.class)
    public JAXBElement<String> createEventAttendeeLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = EventAttendee.class)
    public JAXBElement<XMLGregorianCalendar> createEventAttendeeCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, EventAttendee.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SaturdayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursSaturdayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursSaturdayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = BusinessHours.class)
    public JAXBElement<User> createBusinessHoursLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WednesdayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursWednesdayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursWednesdayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MondayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursMondayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursMondayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SundayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursSundayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursSundayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = BusinessHours.class)
    public JAXBElement<XMLGregorianCalendar> createBusinessHoursLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = BusinessHours.class)
    public JAXBElement<XMLGregorianCalendar> createBusinessHoursSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MondayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursMondayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursMondayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FridayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursFridayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursFridayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WednesdayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursWednesdayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursWednesdayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FridayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursFridayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursFridayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ThursdayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursThursdayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursThursdayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TuesdayEnd", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursTuesdayEnd(String value) {
        return new JAXBElement<String>(_BusinessHoursTuesdayEnd_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = BusinessHours.class)
    public JAXBElement<User> createBusinessHoursCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SaturdayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursSaturdayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursSaturdayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SundayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursSundayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursSundayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TuesdayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursTuesdayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursTuesdayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ThursdayStart", scope = BusinessHours.class)
    public JAXBElement<String> createBusinessHoursThursdayStart(String value) {
        return new JAXBElement<String>(_BusinessHoursThursdayStart_QNAME, String.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = BusinessHours.class)
    public JAXBElement<XMLGregorianCalendar> createBusinessHoursCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, BusinessHours.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReminderDateTime", scope = Task.class)
    public JAXBElement<XMLGregorianCalendar> createTaskReminderDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventReminderDateTime_QNAME, XMLGregorianCalendar.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Task.class)
    public JAXBElement<String> createTaskAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Task.class)
    public JAXBElement<QueryResult> createTaskAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Task.class)
    public JAXBElement<XMLGregorianCalendar> createTaskSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Task.class)
    public JAXBElement<String> createTaskCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Task.class)
    public JAXBElement<Boolean> createTaskIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsArchived", scope = Task.class)
    public JAXBElement<Boolean> createTaskIsArchived(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsArchived_QNAME, Boolean.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Task.class)
    public JAXBElement<User> createTaskCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = Task.class)
    public JAXBElement<Boolean> createTaskIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Task.class)
    public JAXBElement<String> createTaskLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "What", scope = Task.class)
    public JAXBElement<Name> createTaskWhat(Name value) {
        return new JAXBElement<Name>(_EventWhat_QNAME, Name.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Who", scope = Task.class)
    public JAXBElement<Name> createTaskWho(Name value) {
        return new JAXBElement<Name>(_EventWho_QNAME, Name.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Task.class)
    public JAXBElement<User> createTaskLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Task.class)
    public JAXBElement<XMLGregorianCalendar> createTaskLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhatId", scope = Task.class)
    public JAXBElement<String> createTaskWhatId(String value) {
        return new JAXBElement<String>(_EventWhatId_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityDate", scope = Task.class)
    public JAXBElement<XMLGregorianCalendar> createTaskActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventActivityDate_QNAME, XMLGregorianCalendar.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsReminderSet", scope = Task.class)
    public JAXBElement<Boolean> createTaskIsReminderSet(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsReminderSet_QNAME, Boolean.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Subject", scope = Task.class)
    public JAXBElement<String> createTaskSubject(String value) {
        return new JAXBElement<String>(_EventSubject_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Task.class)
    public JAXBElement<String> createTaskDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhoId", scope = Task.class)
    public JAXBElement<String> createTaskWhoId(String value) {
        return new JAXBElement<String>(_EventWhoId_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Task.class)
    public JAXBElement<String> createTaskStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Task.class)
    public JAXBElement<String> createTaskOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Priority", scope = Task.class)
    public JAXBElement<String> createTaskPriority(String value) {
        return new JAXBElement<String>(_OpenActivityPriority_QNAME, String.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Task.class)
    public JAXBElement<XMLGregorianCalendar> createTaskCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Task.class)
    public JAXBElement<Name> createTaskOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, Task.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Value", scope = BrandTemplate.class)
    public JAXBElement<String> createBrandTemplateValue(String value) {
        return new JAXBElement<String>(_BrandTemplateValue_QNAME, String.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = BrandTemplate.class)
    public JAXBElement<String> createBrandTemplateDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = BrandTemplate.class)
    public JAXBElement<User> createBrandTemplateLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = BrandTemplate.class)
    public JAXBElement<Boolean> createBrandTemplateIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = BrandTemplate.class)
    public JAXBElement<String> createBrandTemplateName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = BrandTemplate.class)
    public JAXBElement<User> createBrandTemplateCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = BrandTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createBrandTemplateLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = BrandTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createBrandTemplateSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = BrandTemplate.class)
    public JAXBElement<String> createBrandTemplateCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = BrandTemplate.class)
    public JAXBElement<String> createBrandTemplateLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = BrandTemplate.class)
    public JAXBElement<XMLGregorianCalendar> createBrandTemplateCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, BrandTemplate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Id", scope = SObject.class)
    public JAXBElement<String> createSObjectId(String value) {
        return new JAXBElement<String>(_SObjectId_QNAME, String.class, SObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Case }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Case", scope = CaseHistory.class)
    public JAXBElement<Case> createCaseHistoryCase(Case value) {
        return new JAXBElement<Case>(_CaseHistoryCase_QNAME, Case.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NewValue", scope = CaseHistory.class)
    public JAXBElement<Object> createCaseHistoryNewValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryNewValue_QNAME, Object.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OldValue", scope = CaseHistory.class)
    public JAXBElement<Object> createCaseHistoryOldValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryOldValue_QNAME, Object.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CaseHistory.class)
    public JAXBElement<Name> createCaseHistoryCreatedBy(Name value) {
        return new JAXBElement<Name>(_EventCreatedBy_QNAME, Name.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CaseHistory.class)
    public JAXBElement<String> createCaseHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Field", scope = CaseHistory.class)
    public JAXBElement<String> createCaseHistoryField(String value) {
        return new JAXBElement<String>(_ContractHistoryField_QNAME, String.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CaseHistory.class)
    public JAXBElement<Boolean> createCaseHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CaseHistory.class)
    public JAXBElement<XMLGregorianCalendar> createCaseHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseId", scope = CaseHistory.class)
    public JAXBElement<String> createCaseHistoryCaseId(String value) {
        return new JAXBElement<String>(_CaseHistoryCaseId_QNAME, String.class, CaseHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DocumentSequence", scope = DocumentAttachmentMap.class)
    public JAXBElement<Integer> createDocumentAttachmentMapDocumentSequence(Integer value) {
        return new JAXBElement<Integer>(_DocumentAttachmentMapDocumentSequence_QNAME, Integer.class, DocumentAttachmentMap.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = DocumentAttachmentMap.class)
    public JAXBElement<User> createDocumentAttachmentMapCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, DocumentAttachmentMap.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = DocumentAttachmentMap.class)
    public JAXBElement<String> createDocumentAttachmentMapCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, DocumentAttachmentMap.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = DocumentAttachmentMap.class)
    public JAXBElement<String> createDocumentAttachmentMapParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, DocumentAttachmentMap.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DocumentId", scope = DocumentAttachmentMap.class)
    public JAXBElement<String> createDocumentAttachmentMapDocumentId(String value) {
        return new JAXBElement<String>(_DocumentAttachmentMapDocumentId_QNAME, String.class, DocumentAttachmentMap.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = DocumentAttachmentMap.class)
    public JAXBElement<XMLGregorianCalendar> createDocumentAttachmentMapCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, DocumentAttachmentMap.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = ProcessInstance.class)
    public JAXBElement<User> createProcessInstanceLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StepsAndWorkitems", scope = ProcessInstance.class)
    public JAXBElement<QueryResult> createProcessInstanceStepsAndWorkitems(QueryResult value) {
        return new JAXBElement<QueryResult>(_ProcessInstanceStepsAndWorkitems_QNAME, QueryResult.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TargetObject", scope = ProcessInstance.class)
    public JAXBElement<Name> createProcessInstanceTargetObject(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceHistoryTargetObject_QNAME, Name.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = ProcessInstance.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ProcessInstance.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ProcessInstance.class)
    public JAXBElement<String> createProcessInstanceCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Steps", scope = ProcessInstance.class)
    public JAXBElement<QueryResult> createProcessInstanceSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_ProcessInstanceSteps_QNAME, QueryResult.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = ProcessInstance.class)
    public JAXBElement<Boolean> createProcessInstanceIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TargetObjectId", scope = ProcessInstance.class)
    public JAXBElement<String> createProcessInstanceTargetObjectId(String value) {
        return new JAXBElement<String>(_ProcessInstanceHistoryTargetObjectId_QNAME, String.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ProcessInstance.class)
    public JAXBElement<User> createProcessInstanceCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = ProcessInstance.class)
    public JAXBElement<String> createProcessInstanceStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Workitems", scope = ProcessInstance.class)
    public JAXBElement<QueryResult> createProcessInstanceWorkitems(QueryResult value) {
        return new JAXBElement<QueryResult>(_ProcessInstanceWorkitems_QNAME, QueryResult.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = ProcessInstance.class)
    public JAXBElement<String> createProcessInstanceLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ProcessInstance.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ProcessInstance.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Note.class)
    public JAXBElement<User> createNoteLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = Note.class)
    public JAXBElement<String> createNoteBody(String value) {
        return new JAXBElement<String>(_AttachmentBody_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Parent", scope = Note.class)
    public JAXBElement<Name> createNoteParent(Name value) {
        return new JAXBElement<Name>(_AccountParent_QNAME, Name.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Note.class)
    public JAXBElement<XMLGregorianCalendar> createNoteLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Title", scope = Note.class)
    public JAXBElement<String> createNoteTitle(String value) {
        return new JAXBElement<String>(_ContactTitle_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Note.class)
    public JAXBElement<XMLGregorianCalendar> createNoteSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Note.class)
    public JAXBElement<String> createNoteCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = Note.class)
    public JAXBElement<String> createNoteParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Note.class)
    public JAXBElement<Boolean> createNoteIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrivate", scope = Note.class)
    public JAXBElement<Boolean> createNoteIsPrivate(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsPrivate_QNAME, Boolean.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Note.class)
    public JAXBElement<User> createNoteCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Note.class)
    public JAXBElement<String> createNoteOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Note.class)
    public JAXBElement<String> createNoteLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Note.class)
    public JAXBElement<XMLGregorianCalendar> createNoteCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Note.class)
    public JAXBElement<User> createNoteOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Note.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = UserAccountTeamMember.class)
    public JAXBElement<User> createUserAccountTeamMemberLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserId", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberUserId(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberUserId_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = UserAccountTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createUserAccountTeamMemberLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = UserAccountTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createUserAccountTeamMemberSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountAccessLevel", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberAccountAccessLevel(String value) {
        return new JAXBElement<String>(_AccountShareAccountAccessLevel_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "User", scope = UserAccountTeamMember.class)
    public JAXBElement<User> createUserAccountTeamMemberUser(User value) {
        return new JAXBElement<User>(_OpportunityTeamMemberUser_QNAME, User.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = UserAccountTeamMember.class)
    public JAXBElement<User> createUserAccountTeamMemberCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityAccessLevel", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberOpportunityAccessLevel(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberOpportunityAccessLevel_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseAccessLevel", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberCaseAccessLevel(String value) {
        return new JAXBElement<String>(_AccountShareCaseAccessLevel_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = UserAccountTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createUserAccountTeamMemberCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TeamMemberRole", scope = UserAccountTeamMember.class)
    public JAXBElement<String> createUserAccountTeamMemberTeamMemberRole(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberTeamMemberRole_QNAME, String.class, UserAccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = OpportunityStage.class)
    public JAXBElement<Boolean> createOpportunityStageIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityStage.class)
    public JAXBElement<User> createOpportunityStageLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = OpportunityStage.class)
    public JAXBElement<String> createOpportunityStageMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityStage.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityStageLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityStage.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityStageSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityStage.class)
    public JAXBElement<String> createOpportunityStageCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ForecastCategory", scope = OpportunityStage.class)
    public JAXBElement<String> createOpportunityStageForecastCategory(String value) {
        return new JAXBElement<String>(_OpportunityHistoryForecastCategory_QNAME, String.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = OpportunityStage.class)
    public JAXBElement<String> createOpportunityStageDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DefaultProbability", scope = OpportunityStage.class)
    public JAXBElement<Double> createOpportunityStageDefaultProbability(Double value) {
        return new JAXBElement<Double>(_OpportunityStageDefaultProbability_QNAME, Double.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = OpportunityStage.class)
    public JAXBElement<Integer> createOpportunityStageSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityStage.class)
    public JAXBElement<User> createOpportunityStageCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsWon", scope = OpportunityStage.class)
    public JAXBElement<Boolean> createOpportunityStageIsWon(Boolean value) {
        return new JAXBElement<Boolean>(_OpportunityStageIsWon_QNAME, Boolean.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = OpportunityStage.class)
    public JAXBElement<Boolean> createOpportunityStageIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityStage.class)
    public JAXBElement<String> createOpportunityStageLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityStage.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityStageCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityStage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Solution.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Solution.class)
    public JAXBElement<String> createSolutionCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Solution.class)
    public JAXBElement<Boolean> createSolutionIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SolutionName", scope = Solution.class)
    public JAXBElement<String> createSolutionSolutionName(String value) {
        return new JAXBElement<String>(_SolutionSolutionName_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Solution.class)
    public JAXBElement<User> createSolutionCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TimesUsed", scope = Solution.class)
    public JAXBElement<Integer> createSolutionTimesUsed(Integer value) {
        return new JAXBElement<Integer>(_EmailTemplateTimesUsed_QNAME, Integer.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Solution.class)
    public JAXBElement<String> createSolutionLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Solution.class)
    public JAXBElement<User> createSolutionLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPublishedInPublicKb", scope = Solution.class)
    public JAXBElement<Boolean> createSolutionIsPublishedInPublicKb(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionIsPublishedInPublicKb_QNAME, Boolean.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Solution.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseSolutions", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionCaseSolutions(QueryResult value) {
        return new JAXBElement<QueryResult>(_SolutionCaseSolutions_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SolutionNumber", scope = Solution.class)
    public JAXBElement<String> createSolutionSolutionNumber(String value) {
        return new JAXBElement<String>(_SolutionSolutionNumber_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Histories", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContractHistories_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Solution.class)
    public JAXBElement<String> createSolutionStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPublished", scope = Solution.class)
    public JAXBElement<Boolean> createSolutionIsPublished(Boolean value) {
        return new JAXBElement<Boolean>(_CaseCommentIsPublished_QNAME, Boolean.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Solution.class)
    public JAXBElement<QueryResult> createSolutionEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Solution.class)
    public JAXBElement<String> createSolutionOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsReviewed", scope = Solution.class)
    public JAXBElement<Boolean> createSolutionIsReviewed(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsReviewed_QNAME, Boolean.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Solution.class)
    public JAXBElement<XMLGregorianCalendar> createSolutionCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Solution.class)
    public JAXBElement<User> createSolutionOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SolutionNote", scope = Solution.class)
    public JAXBElement<String> createSolutionSolutionNote(String value) {
        return new JAXBElement<String>(_SolutionSolutionNote_QNAME, String.class, Solution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsTask", scope = ActivityHistory.class)
    public JAXBElement<Boolean> createActivityHistoryIsTask(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsTask_QNAME, Boolean.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ActivityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createActivityHistorySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = ActivityHistory.class)
    public JAXBElement<Boolean> createActivityHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ActivityHistory.class)
    public JAXBElement<User> createActivityHistoryCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = ActivityHistory.class)
    public JAXBElement<Boolean> createActivityHistoryIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsAllDayEvent", scope = ActivityHistory.class)
    public JAXBElement<Boolean> createActivityHistoryIsAllDayEvent(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsAllDayEvent_QNAME, Boolean.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Who", scope = ActivityHistory.class)
    public JAXBElement<Name> createActivityHistoryWho(Name value) {
        return new JAXBElement<Name>(_EventWho_QNAME, Name.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "What", scope = ActivityHistory.class)
    public JAXBElement<Name> createActivityHistoryWhat(Name value) {
        return new JAXBElement<Name>(_EventWhat_QNAME, Name.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = ActivityHistory.class)
    public JAXBElement<User> createActivityHistoryLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = ActivityHistory.class)
    public JAXBElement<Account> createActivityHistoryAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = ActivityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createActivityHistoryLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhatId", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryWhatId(String value) {
        return new JAXBElement<String>(_EventWhatId_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityDate", scope = ActivityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createActivityHistoryActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventActivityDate_QNAME, XMLGregorianCalendar.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Subject", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistorySubject(String value) {
        return new JAXBElement<String>(_EventSubject_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityType", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryActivityType(String value) {
        return new JAXBElement<String>(_OpenActivityActivityType_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhoId", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryWhoId(String value) {
        return new JAXBElement<String>(_EventWhoId_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DurationInMinutes", scope = ActivityHistory.class)
    public JAXBElement<Integer> createActivityHistoryDurationInMinutes(Integer value) {
        return new JAXBElement<Integer>(_EventDurationInMinutes_QNAME, Integer.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Priority", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryPriority(String value) {
        return new JAXBElement<String>(_OpenActivityPriority_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Location", scope = ActivityHistory.class)
    public JAXBElement<String> createActivityHistoryLocation(String value) {
        return new JAXBElement<String>(_EventLocation_QNAME, String.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ActivityHistory.class)
    public JAXBElement<XMLGregorianCalendar> createActivityHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = ActivityHistory.class)
    public JAXBElement<Name> createActivityHistoryOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, ActivityHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = User.class)
    public JAXBElement<Boolean> createUserIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = User.class)
    public JAXBElement<String> createUserName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstName", scope = User.class)
    public JAXBElement<String> createUserFirstName(String value) {
        return new JAXBElement<String>(_ContactFirstName_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Email", scope = User.class)
    public JAXBElement<String> createUserEmail(String value) {
        return new JAXBElement<String>(_ContactEmail_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReceivesInfoEmails", scope = User.class)
    public JAXBElement<Boolean> createUserReceivesInfoEmails(Boolean value) {
        return new JAXBElement<Boolean>(_OrganizationReceivesInfoEmails_QNAME, Boolean.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EmployeeNumber", scope = User.class)
    public JAXBElement<String> createUserEmployeeNumber(String value) {
        return new JAXBElement<String>(_UserEmployeeNumber_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserPermissionsMarketingUser", scope = User.class)
    public JAXBElement<Boolean> createUserUserPermissionsMarketingUser(Boolean value) {
        return new JAXBElement<Boolean>(_UserUserPermissionsMarketingUser_QNAME, Boolean.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Department", scope = User.class)
    public JAXBElement<String> createUserDepartment(String value) {
        return new JAXBElement<String>(_ContactDepartment_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = User.class)
    public JAXBElement<User> createUserCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Alias", scope = User.class)
    public JAXBElement<String> createUserAlias(String value) {
        return new JAXBElement<String>(_NameAlias_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Division", scope = User.class)
    public JAXBElement<String> createUserDivision(String value) {
        return new JAXBElement<String>(_OrganizationDivision_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractsSigned", scope = User.class)
    public JAXBElement<QueryResult> createUserContractsSigned(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactContractsSigned_QNAME, QueryResult.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = User.class)
    public JAXBElement<User> createUserLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Phone", scope = User.class)
    public JAXBElement<String> createUserPhone(String value) {
        return new JAXBElement<String>(_AccountPhone_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = User.class)
    public JAXBElement<XMLGregorianCalendar> createUserLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MobilePhone", scope = User.class)
    public JAXBElement<String> createUserMobilePhone(String value) {
        return new JAXBElement<String>(_ContactMobilePhone_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserRoleId", scope = User.class)
    public JAXBElement<String> createUserUserRoleId(String value) {
        return new JAXBElement<String>(_NameUserRoleId_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserTeams", scope = User.class)
    public JAXBElement<QueryResult> createUserUserTeams(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserUserTeams_QNAME, QueryResult.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TimeZoneSidKey", scope = User.class)
    public JAXBElement<String> createUserTimeZoneSidKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserTimeZoneSidKey_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LocaleSidKey", scope = User.class)
    public JAXBElement<String> createUserLocaleSidKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserLocaleSidKey_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Country", scope = User.class)
    public JAXBElement<String> createUserCountry(String value) {
        return new JAXBElement<String>(_OrganizationCountry_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Fax", scope = User.class)
    public JAXBElement<String> createUserFax(String value) {
        return new JAXBElement<String>(_AccountFax_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProfileId", scope = User.class)
    public JAXBElement<String> createUserProfileId(String value) {
        return new JAXBElement<String>(_UserProfileId_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EmailEncodingKey", scope = User.class)
    public JAXBElement<String> createUserEmailEncodingKey(String value) {
        return new JAXBElement<String>(_UserEmailEncodingKey_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DelegatedUsers", scope = User.class)
    public JAXBElement<QueryResult> createUserDelegatedUsers(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserDelegatedUsers_QNAME, QueryResult.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserRole", scope = User.class)
    public JAXBElement<UserRole> createUserUserRole(UserRole value) {
        return new JAXBElement<UserRole>(_NameUserRole_QNAME, UserRole.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LanguageLocaleKey", scope = User.class)
    public JAXBElement<String> createUserLanguageLocaleKey(String value) {
        return new JAXBElement<String>(_SelfServiceUserLanguageLocaleKey_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Street", scope = User.class)
    public JAXBElement<String> createUserStreet(String value) {
        return new JAXBElement<String>(_OrganizationStreet_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ReceivesAdminInfoEmails", scope = User.class)
    public JAXBElement<Boolean> createUserReceivesAdminInfoEmails(Boolean value) {
        return new JAXBElement<Boolean>(_OrganizationReceivesAdminInfoEmails_QNAME, Boolean.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Username", scope = User.class)
    public JAXBElement<String> createUserUsername(String value) {
        return new JAXBElement<String>(_SelfServiceUserUsername_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = User.class)
    public JAXBElement<XMLGregorianCalendar> createUserSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Title", scope = User.class)
    public JAXBElement<String> createUserTitle(String value) {
        return new JAXBElement<String>(_ContactTitle_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = User.class)
    public JAXBElement<String> createUserCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Extension", scope = User.class)
    public JAXBElement<String> createUserExtension(String value) {
        return new JAXBElement<String>(_UserExtension_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = User.class)
    public JAXBElement<String> createUserLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastName", scope = User.class)
    public JAXBElement<String> createUserLastName(String value) {
        return new JAXBElement<String>(_ContactLastName_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CompanyName", scope = User.class)
    public JAXBElement<String> createUserCompanyName(String value) {
        return new JAXBElement<String>(_UserCompanyName_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "State", scope = User.class)
    public JAXBElement<String> createUserState(String value) {
        return new JAXBElement<String>(_OrganizationState_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserPermissionsOfflineUser", scope = User.class)
    public JAXBElement<Boolean> createUserUserPermissionsOfflineUser(Boolean value) {
        return new JAXBElement<Boolean>(_UserUserPermissionsOfflineUser_QNAME, Boolean.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserAccountTeams", scope = User.class)
    public JAXBElement<QueryResult> createUserUserAccountTeams(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserUserAccountTeams_QNAME, QueryResult.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Profile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Profile", scope = User.class)
    public JAXBElement<Profile> createUserProfile(Profile value) {
        return new JAXBElement<Profile>(_UserProfile_QNAME, Profile.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastLoginDate", scope = User.class)
    public JAXBElement<XMLGregorianCalendar> createUserLastLoginDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SelfServiceUserLastLoginDate_QNAME, XMLGregorianCalendar.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PostalCode", scope = User.class)
    public JAXBElement<String> createUserPostalCode(String value) {
        return new JAXBElement<String>(_OrganizationPostalCode_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountTeams", scope = User.class)
    public JAXBElement<QueryResult> createUserAccountTeams(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserAccountTeams_QNAME, QueryResult.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "City", scope = User.class)
    public JAXBElement<String> createUserCity(String value) {
        return new JAXBElement<String>(_OrganizationCity_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OfflinePdaTrialExpirationDate", scope = User.class)
    public JAXBElement<XMLGregorianCalendar> createUserOfflinePdaTrialExpirationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_UserOfflinePdaTrialExpirationDate_QNAME, XMLGregorianCalendar.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityTeams", scope = User.class)
    public JAXBElement<QueryResult> createUserOpportunityTeams(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserOpportunityTeams_QNAME, QueryResult.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DelegatedApproverId", scope = User.class)
    public JAXBElement<String> createUserDelegatedApproverId(String value) {
        return new JAXBElement<String>(_UserDelegatedApproverId_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OfflineTrialExpirationDate", scope = User.class)
    public JAXBElement<XMLGregorianCalendar> createUserOfflineTrialExpirationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_UserOfflineTrialExpirationDate_QNAME, XMLGregorianCalendar.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = User.class)
    public JAXBElement<XMLGregorianCalendar> createUserCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = ContractStatus.class)
    public JAXBElement<User> createContractStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = ContractStatus.class)
    public JAXBElement<String> createContractStatusMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = ContractStatus.class)
    public JAXBElement<Integer> createContractStatusSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ContractStatus.class)
    public JAXBElement<User> createContractStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = ContractStatus.class)
    public JAXBElement<XMLGregorianCalendar> createContractStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ContractStatus.class)
    public JAXBElement<XMLGregorianCalendar> createContractStatusSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ContractStatus.class)
    public JAXBElement<String> createContractStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StatusCode", scope = ContractStatus.class)
    public JAXBElement<String> createContractStatusStatusCode(String value) {
        return new JAXBElement<String>(_ContractStatusCode_QNAME, String.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = ContractStatus.class)
    public JAXBElement<String> createContractStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ContractStatus.class)
    public JAXBElement<XMLGregorianCalendar> createContractStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = ContractStatus.class)
    public JAXBElement<Boolean> createContractStatusIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, ContractStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TotalOpportunityQuantity", scope = Opportunity.class)
    public JAXBElement<Double> createOpportunityTotalOpportunityQuantity(Double value) {
        return new JAXBElement<Double>(_OpportunityTotalOpportunityQuantity_QNAME, Double.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FiscalYear", scope = Opportunity.class)
    public JAXBElement<Integer> createOpportunityFiscalYear(Integer value) {
        return new JAXBElement<Integer>(_OpportunityFiscalYear_QNAME, Integer.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Fiscal", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityFiscal(String value) {
        return new JAXBElement<String>(_OpportunityFiscal_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Shares", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityShares(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountShares_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityHistories", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpportunityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityHistories_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ForecastCategory", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityForecastCategory(String value) {
        return new JAXBElement<String>(_OpportunityHistoryForecastCategory_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Opportunity.class)
    public JAXBElement<Boolean> createOpportunityIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TrackingNumber__c", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityTrackingNumberC(String value) {
        return new JAXBElement<String>(_OpportunityTrackingNumberC_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Opportunity.class)
    public JAXBElement<User> createOpportunityCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pricebook2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Pricebook2", scope = Opportunity.class)
    public JAXBElement<Pricebook2> createOpportunityPricebook2(Pricebook2 value) {
        return new JAXBElement<Pricebook2>(_OpportunityPricebook2_QNAME, Pricebook2 .class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastActivityDate", scope = Opportunity.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityLastActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountLastActivityDate_QNAME, XMLGregorianCalendar.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityLineItems", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpportunityLineItems(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityLineItems_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityPartnersFrom", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpportunityPartnersFrom(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityPartnersFrom_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MainCompetitors__c", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityMainCompetitorsC(String value) {
        return new JAXBElement<String>(_OpportunityMainCompetitorsC_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OrderNumber__c", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityOrderNumberC(String value) {
        return new JAXBElement<String>(_OpportunityOrderNumberC_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Campaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Campaign", scope = Opportunity.class)
    public JAXBElement<Campaign> createOpportunityCampaign(Campaign value) {
        return new JAXBElement<Campaign>(_OpportunityCampaign_QNAME, Campaign.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Opportunity.class)
    public JAXBElement<User> createOpportunityLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CampaignId", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityCampaignId(String value) {
        return new JAXBElement<String>(_OpportunityCampaignId_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityTeamMembers", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpportunityTeamMembers(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityTeamMembers_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Opportunity.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NextStep", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityNextStep(String value) {
        return new JAXBElement<String>(_OpportunityNextStep_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "StageName", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityStageName(String value) {
        return new JAXBElement<String>(_OpportunityHistoryStageName_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsWon", scope = Opportunity.class)
    public JAXBElement<Boolean> createOpportunityIsWon(Boolean value) {
        return new JAXBElement<Boolean>(_OpportunityStageIsWon_QNAME, Boolean.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Pricebook2Id", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityPricebook2Id(String value) {
        return new JAXBElement<String>(_OpportunityPricebook2Id_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Opportunity.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunitySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadSource", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityLeadSource(String value) {
        return new JAXBElement<String>(_ContactLeadSource_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = Opportunity.class)
    public JAXBElement<Boolean> createOpportunityIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CloseDate", scope = Opportunity.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityCloseDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_OpportunityHistoryCloseDate_QNAME, XMLGregorianCalendar.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = Opportunity.class)
    public JAXBElement<Account> createOpportunityAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Amount", scope = Opportunity.class)
    public JAXBElement<Double> createOpportunityAmount(Double value) {
        return new JAXBElement<Double>(_OpportunityHistoryAmount_QNAME, Double.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountPartners", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityAccountPartners(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityAccountPartners_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityCompetitors", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpportunityCompetitors(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityCompetitors_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FiscalQuarter", scope = Opportunity.class)
    public JAXBElement<Integer> createOpportunityFiscalQuarter(Integer value) {
        return new JAXBElement<Integer>(_OpportunityFiscalQuarter_QNAME, Integer.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasOpportunityLineItem", scope = Opportunity.class)
    public JAXBElement<Boolean> createOpportunityHasOpportunityLineItem(Boolean value) {
        return new JAXBElement<Boolean>(_OpportunityHasOpportunityLineItem_QNAME, Boolean.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Probability", scope = Opportunity.class)
    public JAXBElement<Double> createOpportunityProbability(Double value) {
        return new JAXBElement<Double>(_OpportunityHistoryProbability_QNAME, Double.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ExpectedRevenue", scope = Opportunity.class)
    public JAXBElement<Double> createOpportunityExpectedRevenue(Double value) {
        return new JAXBElement<Double>(_CampaignExpectedRevenue_QNAME, Double.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrivate", scope = Opportunity.class)
    public JAXBElement<Boolean> createOpportunityIsPrivate(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsPrivate_QNAME, Boolean.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DeliveryInstallationStatus__c", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityDeliveryInstallationStatusC(String value) {
        return new JAXBElement<String>(_OpportunityDeliveryInstallationStatusC_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Partners", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityPartners(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityPartners_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Opportunity.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CurrentGenerators__c", scope = Opportunity.class)
    public JAXBElement<String> createOpportunityCurrentGeneratorsC(String value) {
        return new JAXBElement<String>(_OpportunityCurrentGeneratorsC_QNAME, String.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityContactRoles", scope = Opportunity.class)
    public JAXBElement<QueryResult> createOpportunityOpportunityContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactOpportunityContactRoles_QNAME, QueryResult.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Opportunity.class)
    public JAXBElement<User> createOpportunityOwner(User value) {
        return new JAXBElement<User>(_EventOwner_QNAME, User.class, Opportunity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = TestC.class)
    public JAXBElement<User> createTestCLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = TestC.class)
    public JAXBElement<String> createTestCName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Shares", scope = TestC.class)
    public JAXBElement<QueryResult> createTestCShares(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountShares_QNAME, QueryResult.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = TestC.class)
    public JAXBElement<XMLGregorianCalendar> createTestCLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = TestC.class)
    public JAXBElement<XMLGregorianCalendar> createTestCSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = TestC.class)
    public JAXBElement<QueryResult> createTestCAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = TestC.class)
    public JAXBElement<String> createTestCCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = TestC.class)
    public JAXBElement<Boolean> createTestCIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = TestC.class)
    public JAXBElement<QueryResult> createTestCNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = TestC.class)
    public JAXBElement<QueryResult> createTestCNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = TestC.class)
    public JAXBElement<User> createTestCCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = TestC.class)
    public JAXBElement<String> createTestCOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = TestC.class)
    public JAXBElement<QueryResult> createTestCProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = TestC.class)
    public JAXBElement<String> createTestCLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = TestC.class)
    public JAXBElement<XMLGregorianCalendar> createTestCCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = TestC.class)
    public JAXBElement<QueryResult> createTestCProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = TestC.class)
    public JAXBElement<Name> createTestCOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, TestC.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NewValue", scope = LeadHistory.class)
    public JAXBElement<Object> createLeadHistoryNewValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryNewValue_QNAME, Object.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OldValue", scope = LeadHistory.class)
    public JAXBElement<Object> createLeadHistoryOldValue(Object value) {
        return new JAXBElement<Object>(_ContractHistoryOldValue_QNAME, Object.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Lead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Lead", scope = LeadHistory.class)
    public JAXBElement<Lead> createLeadHistoryLead(Lead value) {
        return new JAXBElement<Lead>(_LeadHistoryLead_QNAME, Lead.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = LeadHistory.class)
    public JAXBElement<Name> createLeadHistoryCreatedBy(Name value) {
        return new JAXBElement<Name>(_EventCreatedBy_QNAME, Name.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = LeadHistory.class)
    public JAXBElement<String> createLeadHistoryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Field", scope = LeadHistory.class)
    public JAXBElement<String> createLeadHistoryField(String value) {
        return new JAXBElement<String>(_ContractHistoryField_QNAME, String.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = LeadHistory.class)
    public JAXBElement<Boolean> createLeadHistoryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = LeadHistory.class)
    public JAXBElement<XMLGregorianCalendar> createLeadHistoryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadId", scope = LeadHistory.class)
    public JAXBElement<String> createLeadHistoryLeadId(String value) {
        return new JAXBElement<String>(_LeadHistoryLeadId_QNAME, String.class, LeadHistory.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Who", scope = EmailStatus.class)
    public JAXBElement<Name> createEmailStatusWho(Name value) {
        return new JAXBElement<Name>(_EventWho_QNAME, Name.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = EmailStatus.class)
    public JAXBElement<User> createEmailStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = EmailStatus.class)
    public JAXBElement<XMLGregorianCalendar> createEmailStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TaskId", scope = EmailStatus.class)
    public JAXBElement<String> createEmailStatusTaskId(String value) {
        return new JAXBElement<String>(_EmailStatusTaskId_QNAME, String.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = EmailStatus.class)
    public JAXBElement<String> createEmailStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WhoId", scope = EmailStatus.class)
    public JAXBElement<String> createEmailStatusWhoId(String value) {
        return new JAXBElement<String>(_EventWhoId_QNAME, String.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EmailTemplateName", scope = EmailStatus.class)
    public JAXBElement<String> createEmailStatusEmailTemplateName(String value) {
        return new JAXBElement<String>(_EmailStatusEmailTemplateName_QNAME, String.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = EmailStatus.class)
    public JAXBElement<User> createEmailStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TimesOpened", scope = EmailStatus.class)
    public JAXBElement<Integer> createEmailStatusTimesOpened(Integer value) {
        return new JAXBElement<Integer>(_EmailStatusTimesOpened_QNAME, Integer.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Task }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Task", scope = EmailStatus.class)
    public JAXBElement<Task> createEmailStatusTask(Task value) {
        return new JAXBElement<Task>(_EmailStatusTask_QNAME, Task.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastOpenDate", scope = EmailStatus.class)
    public JAXBElement<XMLGregorianCalendar> createEmailStatusLastOpenDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EmailStatusLastOpenDate_QNAME, XMLGregorianCalendar.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = EmailStatus.class)
    public JAXBElement<String> createEmailStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = EmailStatus.class)
    public JAXBElement<XMLGregorianCalendar> createEmailStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstOpenDate", scope = EmailStatus.class)
    public JAXBElement<XMLGregorianCalendar> createEmailStatusFirstOpenDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EmailStatusFirstOpenDate_QNAME, XMLGregorianCalendar.class, EmailStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = TaskStatus.class)
    public JAXBElement<User> createTaskStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = TaskStatus.class)
    public JAXBElement<String> createTaskStatusMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = TaskStatus.class)
    public JAXBElement<Integer> createTaskStatusSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = TaskStatus.class)
    public JAXBElement<User> createTaskStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = TaskStatus.class)
    public JAXBElement<XMLGregorianCalendar> createTaskStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = TaskStatus.class)
    public JAXBElement<XMLGregorianCalendar> createTaskStatusSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = TaskStatus.class)
    public JAXBElement<Boolean> createTaskStatusIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = TaskStatus.class)
    public JAXBElement<String> createTaskStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = TaskStatus.class)
    public JAXBElement<String> createTaskStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = TaskStatus.class)
    public JAXBElement<XMLGregorianCalendar> createTaskStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = TaskStatus.class)
    public JAXBElement<Boolean> createTaskStatusIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, TaskStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Group.class)
    public JAXBElement<User> createGroupLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Group.class)
    public JAXBElement<String> createGroupName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Group.class)
    public JAXBElement<XMLGregorianCalendar> createGroupLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Group.class)
    public JAXBElement<XMLGregorianCalendar> createGroupSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Group.class)
    public JAXBElement<String> createGroupCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "QueueSobjects", scope = Group.class)
    public JAXBElement<QueryResult> createGroupQueueSobjects(QueryResult value) {
        return new JAXBElement<QueryResult>(_GroupQueueSobjects_QNAME, QueryResult.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Email", scope = Group.class)
    public JAXBElement<String> createGroupEmail(String value) {
        return new JAXBElement<String>(_ContactEmail_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DoesSendEmailToMembers", scope = Group.class)
    public JAXBElement<Boolean> createGroupDoesSendEmailToMembers(Boolean value) {
        return new JAXBElement<Boolean>(_GroupDoesSendEmailToMembers_QNAME, Boolean.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Group.class)
    public JAXBElement<User> createGroupCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Group.class)
    public JAXBElement<String> createGroupType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RelatedId", scope = Group.class)
    public JAXBElement<String> createGroupRelatedId(String value) {
        return new JAXBElement<String>(_GroupRelatedId_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Group.class)
    public JAXBElement<String> createGroupOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "GroupMembers", scope = Group.class)
    public JAXBElement<QueryResult> createGroupGroupMembers(QueryResult value) {
        return new JAXBElement<QueryResult>(_GroupGroupMembers_QNAME, QueryResult.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Group.class)
    public JAXBElement<String> createGroupLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Group.class)
    public JAXBElement<XMLGregorianCalendar> createGroupCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DelegatedUsers", scope = Group.class)
    public JAXBElement<QueryResult> createGroupDelegatedUsers(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserDelegatedUsers_QNAME, QueryResult.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Group.class)
    public JAXBElement<Name> createGroupOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, Group.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Folder.class)
    public JAXBElement<User> createFolderLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Folder.class)
    public JAXBElement<String> createFolderName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccessType", scope = Folder.class)
    public JAXBElement<String> createFolderAccessType(String value) {
        return new JAXBElement<String>(_FolderAccessType_QNAME, String.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Folder.class)
    public JAXBElement<User> createFolderCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Folder.class)
    public JAXBElement<String> createFolderType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Folder.class)
    public JAXBElement<XMLGregorianCalendar> createFolderLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Folder.class)
    public JAXBElement<XMLGregorianCalendar> createFolderSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Folder.class)
    public JAXBElement<String> createFolderCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsReadonly", scope = Folder.class)
    public JAXBElement<Boolean> createFolderIsReadonly(Boolean value) {
        return new JAXBElement<Boolean>(_FolderIsReadonly_QNAME, Boolean.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Folder.class)
    public JAXBElement<String> createFolderLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Folder.class)
    public JAXBElement<XMLGregorianCalendar> createFolderCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Folder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsCompetitorProduct", scope = Asset.class)
    public JAXBElement<Boolean> createAssetIsCompetitorProduct(Boolean value) {
        return new JAXBElement<Boolean>(_AssetIsCompetitorProduct_QNAME, Boolean.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Asset.class)
    public JAXBElement<String> createAssetName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Asset.class)
    public JAXBElement<String> createAssetAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Asset.class)
    public JAXBElement<XMLGregorianCalendar> createAssetSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Quantity", scope = Asset.class)
    public JAXBElement<Double> createAssetQuantity(Double value) {
        return new JAXBElement<Double>(_AssetQuantity_QNAME, Double.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Asset.class)
    public JAXBElement<String> createAssetCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Product2Id", scope = Asset.class)
    public JAXBElement<String> createAssetProduct2Id(String value) {
        return new JAXBElement<String>(_AssetProduct2Id_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Asset.class)
    public JAXBElement<Boolean> createAssetIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UsageEndDate", scope = Asset.class)
    public JAXBElement<XMLGregorianCalendar> createAssetUsageEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AssetUsageEndDate_QNAME, XMLGregorianCalendar.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Asset.class)
    public JAXBElement<User> createAssetCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Price", scope = Asset.class)
    public JAXBElement<Double> createAssetPrice(Double value) {
        return new JAXBElement<Double>(_AssetPrice_QNAME, Double.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PurchaseDate", scope = Asset.class)
    public JAXBElement<XMLGregorianCalendar> createAssetPurchaseDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AssetPurchaseDate_QNAME, XMLGregorianCalendar.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Asset.class)
    public JAXBElement<String> createAssetLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Asset.class)
    public JAXBElement<User> createAssetLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = Asset.class)
    public JAXBElement<Account> createAssetAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Asset.class)
    public JAXBElement<XMLGregorianCalendar> createAssetLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Cases", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetCases(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountCases_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = Asset.class)
    public JAXBElement<Contact> createAssetContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Asset.class)
    public JAXBElement<String> createAssetDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Asset.class)
    public JAXBElement<String> createAssetStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SerialNumber", scope = Asset.class)
    public JAXBElement<String> createAssetSerialNumber(String value) {
        return new JAXBElement<String>(_AssetSerialNumber_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Product2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Product2", scope = Asset.class)
    public JAXBElement<Product2> createAssetProduct2(Product2 value) {
        return new JAXBElement<Product2>(_AssetProduct2_QNAME, Product2 .class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Asset.class)
    public JAXBElement<QueryResult> createAssetEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = Asset.class)
    public JAXBElement<String> createAssetContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "InstallDate", scope = Asset.class)
    public JAXBElement<XMLGregorianCalendar> createAssetInstallDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AssetInstallDate_QNAME, XMLGregorianCalendar.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Asset.class)
    public JAXBElement<XMLGregorianCalendar> createAssetCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Asset.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Campaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Campaign", scope = CampaignMember.class)
    public JAXBElement<Campaign> createCampaignMemberCampaign(Campaign value) {
        return new JAXBElement<Campaign>(_OpportunityCampaign_QNAME, Campaign.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CampaignMember.class)
    public JAXBElement<User> createCampaignMemberLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CampaignId", scope = CampaignMember.class)
    public JAXBElement<String> createCampaignMemberCampaignId(String value) {
        return new JAXBElement<String>(_OpportunityCampaignId_QNAME, String.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasResponded", scope = CampaignMember.class)
    public JAXBElement<Boolean> createCampaignMemberHasResponded(Boolean value) {
        return new JAXBElement<Boolean>(_CampaignMemberHasResponded_QNAME, Boolean.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CampaignMember.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CampaignMember.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CampaignMember.class)
    public JAXBElement<String> createCampaignMemberCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = CampaignMember.class)
    public JAXBElement<Contact> createCampaignMemberContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CampaignMember.class)
    public JAXBElement<Boolean> createCampaignMemberIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = CampaignMember.class)
    public JAXBElement<String> createCampaignMemberStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Lead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Lead", scope = CampaignMember.class)
    public JAXBElement<Lead> createCampaignMemberLead(Lead value) {
        return new JAXBElement<Lead>(_LeadHistoryLead_QNAME, Lead.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CampaignMember.class)
    public JAXBElement<User> createCampaignMemberCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = CampaignMember.class)
    public JAXBElement<String> createCampaignMemberContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CampaignMember.class)
    public JAXBElement<String> createCampaignMemberLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CampaignMember.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstRespondedDate", scope = CampaignMember.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberFirstRespondedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CampaignMemberFirstRespondedDate_QNAME, XMLGregorianCalendar.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadId", scope = CampaignMember.class)
    public JAXBElement<String> createCampaignMemberLeadId(String value) {
        return new JAXBElement<String>(_LeadHistoryLeadId_QNAME, String.class, CampaignMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Profile.class)
    public JAXBElement<String> createProfileName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageSelfService", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageSelfService(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageSelfService_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditTask", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditTask(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditTask_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageSolutions", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageSolutions(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageSolutions_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsInstallMultiforce", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsInstallMultiforce(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsInstallMultiforce_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageTerritories", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageTerritories(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageTerritories_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Profile.class)
    public JAXBElement<User> createProfileCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsPasswordNeverExpires", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsPasswordNeverExpires(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsPasswordNeverExpires_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditActivatedOrders", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditActivatedOrders(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditActivatedOrders_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageCategories", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageCategories(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageCategories_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsModifyAllData", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsModifyAllData(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsModifyAllData_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsCreateMultiforce", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsCreateMultiforce(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsCreateMultiforce_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Profile.class)
    public JAXBElement<User> createProfileLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditPublicDocuments", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditPublicDocuments(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditPublicDocuments_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Profile.class)
    public JAXBElement<XMLGregorianCalendar> createProfileLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsConvertLeads", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsConvertLeads(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsConvertLeads_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsCustomizeApplication", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsCustomizeApplication(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsCustomizeApplication_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsDisableNotifications", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsDisableNotifications(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsDisableNotifications_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsSendSitRequests", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsSendSitRequests(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsSendSitRequests_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Profile.class)
    public JAXBElement<String> createProfileDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LicenseType", scope = Profile.class)
    public JAXBElement<String> createProfileLicenseType(String value) {
        return new JAXBElement<String>(_ProfileLicenseType_QNAME, String.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsPublishMultiforce", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsPublishMultiforce(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsPublishMultiforce_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsSolutionImport", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsSolutionImport(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsSolutionImport_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditReadonlyFields", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditReadonlyFields(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditReadonlyFields_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageCallCenters", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageCallCenters(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageCallCenters_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageCases", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageCases(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageCases_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Profile.class)
    public JAXBElement<XMLGregorianCalendar> createProfileSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Profile.class)
    public JAXBElement<String> createProfileCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditForecast", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditForecast(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditForecast_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsImportLeads", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsImportLeads(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsImportLeads_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsIPRestrictRequests", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsIPRestrictRequests(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsIPRestrictRequests_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsTransferAnyEntity", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsTransferAnyEntity(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsTransferAnyEntity_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsTransferAnyLead", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsTransferAnyLead(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsTransferAnyLead_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsUseTeamReassignWizards", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsUseTeamReassignWizards(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsUseTeamReassignWizards_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Profile.class)
    public JAXBElement<String> createProfileLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsRunReports", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsRunReports(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsRunReports_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsAuthorApex", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsAuthorApex(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsAuthorApex_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageDashboards", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageDashboards(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageDashboards_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditOppLineItemUnitPrice", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditOppLineItemUnitPrice(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditOppLineItemUnitPrice_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageLeads", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageLeads(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageLeads_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageCssUsers", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageCssUsers(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageCssUsers_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsViewAllData", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsViewAllData(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsViewAllData_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Users", scope = Profile.class)
    public JAXBElement<QueryResult> createProfileUsers(QueryResult value) {
        return new JAXBElement<QueryResult>(_UserRoleUsers_QNAME, QueryResult.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsViewSetup", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsViewSetup(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsViewSetup_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditEvent", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditEvent(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditEvent_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Profile.class)
    public JAXBElement<XMLGregorianCalendar> createProfileCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsManageUsers", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsManageUsers(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsManageUsers_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsApiUserOnly", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsApiUserOnly(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsApiUserOnly_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PermissionsEditReports", scope = Profile.class)
    public JAXBElement<Boolean> createProfilePermissionsEditReports(Boolean value) {
        return new JAXBElement<Boolean>(_ProfilePermissionsEditReports_QNAME, Boolean.class, Profile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = RecordType.class)
    public JAXBElement<String> createRecordTypeDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = RecordType.class)
    public JAXBElement<User> createRecordTypeLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = RecordType.class)
    public JAXBElement<Boolean> createRecordTypeIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = RecordType.class)
    public JAXBElement<String> createRecordTypeName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = RecordType.class)
    public JAXBElement<User> createRecordTypeCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = RecordType.class)
    public JAXBElement<XMLGregorianCalendar> createRecordTypeLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = RecordType.class)
    public JAXBElement<XMLGregorianCalendar> createRecordTypeSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = RecordType.class)
    public JAXBElement<String> createRecordTypeCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = RecordType.class)
    public JAXBElement<String> createRecordTypeLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BusinessProcessId", scope = RecordType.class)
    public JAXBElement<String> createRecordTypeBusinessProcessId(String value) {
        return new JAXBElement<String>(_RecordTypeBusinessProcessId_QNAME, String.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SobjectType", scope = RecordType.class)
    public JAXBElement<String> createRecordTypeSobjectType(String value) {
        return new JAXBElement<String>(_QueueSobjectSobjectType_QNAME, String.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = RecordType.class)
    public JAXBElement<XMLGregorianCalendar> createRecordTypeCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, RecordType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = ContractContactRole.class)
    public JAXBElement<User> createContractContactRoleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = ContractContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createContractContactRoleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ContractContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createContractContactRoleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contract }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contract", scope = ContractContactRole.class)
    public JAXBElement<Contract> createContractContactRoleContract(Contract value) {
        return new JAXBElement<Contract>(_ContractHistoryContract_QNAME, Contract.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ContractContactRole.class)
    public JAXBElement<String> createContractContactRoleCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = ContractContactRole.class)
    public JAXBElement<Contact> createContractContactRoleContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = ContractContactRole.class)
    public JAXBElement<Boolean> createContractContactRoleIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ContractContactRole.class)
    public JAXBElement<User> createContractContactRoleCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = ContractContactRole.class)
    public JAXBElement<String> createContractContactRoleRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = ContractContactRole.class)
    public JAXBElement<String> createContractContactRoleContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContractId", scope = ContractContactRole.class)
    public JAXBElement<String> createContractContactRoleContractId(String value) {
        return new JAXBElement<String>(_ContractHistoryContractId_QNAME, String.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrimary", scope = ContractContactRole.class)
    public JAXBElement<Boolean> createContractContactRoleIsPrimary(Boolean value) {
        return new JAXBElement<Boolean>(_AccountContactRoleIsPrimary_QNAME, Boolean.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = ContractContactRole.class)
    public JAXBElement<String> createContractContactRoleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ContractContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createContractContactRoleCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ContractContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = AccountTeamMember.class)
    public JAXBElement<User> createAccountTeamMemberLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserId", scope = AccountTeamMember.class)
    public JAXBElement<String> createAccountTeamMemberUserId(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberUserId_QNAME, String.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = AccountTeamMember.class)
    public JAXBElement<Account> createAccountTeamMemberAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = AccountTeamMember.class)
    public JAXBElement<String> createAccountTeamMemberAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = AccountTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createAccountTeamMemberLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = AccountTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createAccountTeamMemberSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = AccountTeamMember.class)
    public JAXBElement<String> createAccountTeamMemberCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = AccountTeamMember.class)
    public JAXBElement<Boolean> createAccountTeamMemberIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountAccessLevel", scope = AccountTeamMember.class)
    public JAXBElement<String> createAccountTeamMemberAccountAccessLevel(String value) {
        return new JAXBElement<String>(_AccountShareAccountAccessLevel_QNAME, String.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "User", scope = AccountTeamMember.class)
    public JAXBElement<User> createAccountTeamMemberUser(User value) {
        return new JAXBElement<User>(_OpportunityTeamMemberUser_QNAME, User.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = AccountTeamMember.class)
    public JAXBElement<User> createAccountTeamMemberCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = AccountTeamMember.class)
    public JAXBElement<String> createAccountTeamMemberLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = AccountTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createAccountTeamMemberCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TeamMemberRole", scope = AccountTeamMember.class)
    public JAXBElement<String> createAccountTeamMemberTeamMemberRole(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberTeamMemberRole_QNAME, String.class, AccountTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RowCause", scope = TestShare.class)
    public JAXBElement<String> createTestShareRowCause(String value) {
        return new JAXBElement<String>(_OpportunityShareRowCause_QNAME, String.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccessLevel", scope = TestShare.class)
    public JAXBElement<String> createTestShareAccessLevel(String value) {
        return new JAXBElement<String>(_TestShareAccessLevel_QNAME, String.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = TestShare.class)
    public JAXBElement<User> createTestShareLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestC }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Parent", scope = TestShare.class)
    public JAXBElement<TestC> createTestShareParent(TestC value) {
        return new JAXBElement<TestC>(_AccountParent_QNAME, TestC.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = TestShare.class)
    public JAXBElement<XMLGregorianCalendar> createTestShareLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserOrGroupId", scope = TestShare.class)
    public JAXBElement<String> createTestShareUserOrGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberUserOrGroupId_QNAME, String.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ParentId", scope = TestShare.class)
    public JAXBElement<String> createTestShareParentId(String value) {
        return new JAXBElement<String>(_AccountParentId_QNAME, String.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = TestShare.class)
    public JAXBElement<String> createTestShareLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = TestShare.class)
    public JAXBElement<Boolean> createTestShareIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, TestShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PeriodLabelScheme", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsPeriodLabelScheme(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsPeriodLabelScheme_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PeriodId", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsPeriodId(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsPeriodId_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WeekStartDay", scope = FiscalYearSettings.class)
    public JAXBElement<Integer> createFiscalYearSettingsWeekStartDay(Integer value) {
        return new JAXBElement<Integer>(_FiscalYearSettingsWeekStartDay_QNAME, Integer.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "WeekLabelScheme", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsWeekLabelScheme(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsWeekLabelScheme_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsStandardYear", scope = FiscalYearSettings.class)
    public JAXBElement<Boolean> createFiscalYearSettingsIsStandardYear(Boolean value) {
        return new JAXBElement<Boolean>(_FiscalYearSettingsIsStandardYear_QNAME, Boolean.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = FiscalYearSettings.class)
    public JAXBElement<XMLGregorianCalendar> createFiscalYearSettingsSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PeriodPrefix", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsPeriodPrefix(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsPeriodPrefix_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "QuarterPrefix", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsQuarterPrefix(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsQuarterPrefix_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "QuarterLabelScheme", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsQuarterLabelScheme(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsQuarterLabelScheme_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "YearType", scope = FiscalYearSettings.class)
    public JAXBElement<String> createFiscalYearSettingsYearType(String value) {
        return new JAXBElement<String>(_FiscalYearSettingsYearType_QNAME, String.class, FiscalYearSettings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = Product2 .class)
    public JAXBElement<Boolean> createProduct2IsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Product2 .class)
    public JAXBElement<String> createProduct2Name(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2Attachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Product2 .class)
    public JAXBElement<XMLGregorianCalendar> createProduct2SystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProductCode", scope = Product2 .class)
    public JAXBElement<String> createProduct2ProductCode(String value) {
        return new JAXBElement<String>(_Product2ProductCode_QNAME, String.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2Tasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Product2 .class)
    public JAXBElement<String> createProduct2CreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Product2 .class)
    public JAXBElement<Boolean> createProduct2IsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2ActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2NotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Product2 .class)
    public JAXBElement<User> createProduct2CreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityLineItems", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2OpportunityLineItems(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityLineItems_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2ProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Product2 .class)
    public JAXBElement<String> createProduct2LastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2OpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2ProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Product2 .class)
    public JAXBElement<User> createProduct2LastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Product2 .class)
    public JAXBElement<XMLGregorianCalendar> createProduct2LastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2Notes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Product2 .class)
    public JAXBElement<String> createProduct2Description(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PricebookEntries", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2PricebookEntries(QueryResult value) {
        return new JAXBElement<QueryResult>(_Pricebook2PricebookEntries_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Assets", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2Assets(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountAssets_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Product2 .class)
    public JAXBElement<QueryResult> createProduct2Events(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Family", scope = Product2 .class)
    public JAXBElement<String> createProduct2Family(String value) {
        return new JAXBElement<String>(_Product2Family_QNAME, String.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Product2 .class)
    public JAXBElement<XMLGregorianCalendar> createProduct2CreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Product2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Scontrol.class)
    public JAXBElement<User> createScontrolLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Scontrol.class)
    public JAXBElement<String> createScontrolName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "DeveloperName", scope = Scontrol.class)
    public JAXBElement<String> createScontrolDeveloperName(String value) {
        return new JAXBElement<String>(_ScontrolDeveloperName_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NamespacePrefix", scope = Scontrol.class)
    public JAXBElement<String> createScontrolNamespacePrefix(String value) {
        return new JAXBElement<String>(_WebLinkNamespacePrefix_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Filename", scope = Scontrol.class)
    public JAXBElement<String> createScontrolFilename(String value) {
        return new JAXBElement<String>(_MailmergeTemplateFilename_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Scontrol.class)
    public JAXBElement<XMLGregorianCalendar> createScontrolLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Scontrol.class)
    public JAXBElement<XMLGregorianCalendar> createScontrolSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Scontrol.class)
    public JAXBElement<String> createScontrolCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BodyLength", scope = Scontrol.class)
    public JAXBElement<Integer> createScontrolBodyLength(Integer value) {
        return new JAXBElement<Integer>(_AttachmentBodyLength_QNAME, Integer.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Scontrol.class)
    public JAXBElement<String> createScontrolDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SupportsCaching", scope = Scontrol.class)
    public JAXBElement<Boolean> createScontrolSupportsCaching(Boolean value) {
        return new JAXBElement<Boolean>(_ScontrolSupportsCaching_QNAME, Boolean.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Scontrol.class)
    public JAXBElement<User> createScontrolCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContentSource", scope = Scontrol.class)
    public JAXBElement<String> createScontrolContentSource(String value) {
        return new JAXBElement<String>(_ScontrolContentSource_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Scontrol.class)
    public JAXBElement<String> createScontrolLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Scontrol.class)
    public JAXBElement<XMLGregorianCalendar> createScontrolCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Binary", scope = Scontrol.class)
    public JAXBElement<byte[]> createScontrolBinary(byte[] value) {
        return new JAXBElement<byte[]>(_ScontrolBinary_QNAME, byte[].class, Scontrol.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EncodingKey", scope = Scontrol.class)
    public JAXBElement<String> createScontrolEncodingKey(String value) {
        return new JAXBElement<String>(_WebLinkEncodingKey_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HtmlWrapper", scope = Scontrol.class)
    public JAXBElement<String> createScontrolHtmlWrapper(String value) {
        return new JAXBElement<String>(_ScontrolHtmlWrapper_QNAME, String.class, Scontrol.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RowCause", scope = LeadShare.class)
    public JAXBElement<String> createLeadShareRowCause(String value) {
        return new JAXBElement<String>(_OpportunityShareRowCause_QNAME, String.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = LeadShare.class)
    public JAXBElement<User> createLeadShareLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Lead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Lead", scope = LeadShare.class)
    public JAXBElement<Lead> createLeadShareLead(Lead value) {
        return new JAXBElement<Lead>(_LeadHistoryLead_QNAME, Lead.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = LeadShare.class)
    public JAXBElement<XMLGregorianCalendar> createLeadShareLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserOrGroupId", scope = LeadShare.class)
    public JAXBElement<String> createLeadShareUserOrGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberUserOrGroupId_QNAME, String.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadAccessLevel", scope = LeadShare.class)
    public JAXBElement<String> createLeadShareLeadAccessLevel(String value) {
        return new JAXBElement<String>(_LeadShareLeadAccessLevel_QNAME, String.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = LeadShare.class)
    public JAXBElement<String> createLeadShareLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = LeadShare.class)
    public JAXBElement<Boolean> createLeadShareIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadId", scope = LeadShare.class)
    public JAXBElement<String> createLeadShareLeadId(String value) {
        return new JAXBElement<String>(_LeadHistoryLeadId_QNAME, String.class, LeadShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CampaignMemberStatus.class)
    public JAXBElement<User> createCampaignMemberStatusLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CampaignId", scope = CampaignMemberStatus.class)
    public JAXBElement<String> createCampaignMemberStatusCampaignId(String value) {
        return new JAXBElement<String>(_OpportunityCampaignId_QNAME, String.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasResponded", scope = CampaignMemberStatus.class)
    public JAXBElement<Boolean> createCampaignMemberStatusHasResponded(Boolean value) {
        return new JAXBElement<Boolean>(_CampaignMemberHasResponded_QNAME, Boolean.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Label", scope = CampaignMemberStatus.class)
    public JAXBElement<String> createCampaignMemberStatusLabel(String value) {
        return new JAXBElement<String>(_CampaignMemberStatusLabel_QNAME, String.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CampaignMemberStatus.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberStatusLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CampaignMemberStatus.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberStatusSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CampaignMemberStatus.class)
    public JAXBElement<String> createCampaignMemberStatusCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CampaignMemberStatus.class)
    public JAXBElement<Boolean> createCampaignMemberStatusIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = CampaignMemberStatus.class)
    public JAXBElement<Boolean> createCampaignMemberStatusIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CampaignMemberStatus.class)
    public JAXBElement<User> createCampaignMemberStatusCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = CampaignMemberStatus.class)
    public JAXBElement<Integer> createCampaignMemberStatusSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CampaignMemberStatus.class)
    public JAXBElement<String> createCampaignMemberStatusLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CampaignMemberStatus.class)
    public JAXBElement<XMLGregorianCalendar> createCampaignMemberStatusCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CampaignMemberStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OptionsSystemDefined", scope = ApexPackageIdentifier.class)
    public JAXBElement<Boolean> createApexPackageIdentifierOptionsSystemDefined(Boolean value) {
        return new JAXBElement<Boolean>(_ApexPackageIdentifierOptionsSystemDefined_QNAME, Boolean.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IdentifierName", scope = ApexPackageIdentifier.class)
    public JAXBElement<String> createApexPackageIdentifierIdentifierName(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierIdentifierName_QNAME, String.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OptionsFinalVariable", scope = ApexPackageIdentifier.class)
    public JAXBElement<Boolean> createApexPackageIdentifierOptionsFinalVariable(Boolean value) {
        return new JAXBElement<Boolean>(_ApexPackageIdentifierOptionsFinalVariable_QNAME, Boolean.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OptionsTestMethod", scope = ApexPackageIdentifier.class)
    public JAXBElement<Boolean> createApexPackageIdentifierOptionsTestMethod(Boolean value) {
        return new JAXBElement<Boolean>(_ApexPackageIdentifierOptionsTestMethod_QNAME, Boolean.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Arguments", scope = ApexPackageIdentifier.class)
    public JAXBElement<QueryResult> createApexPackageIdentifierArguments(QueryResult value) {
        return new JAXBElement<QueryResult>(_ApexPackageIdentifierArguments_QNAME, QueryResult.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApexPackage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApexPackage", scope = ApexPackageIdentifier.class)
    public JAXBElement<ApexPackage> createApexPackageIdentifierApexPackage(ApexPackage value) {
        return new JAXBElement<ApexPackage>(_ApexPackageIdentifierApexPackage_QNAME, ApexPackage.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IdentifierType", scope = ApexPackageIdentifier.class)
    public JAXBElement<String> createApexPackageIdentifierIdentifierType(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierIdentifierType_QNAME, String.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OptionsPrivateIdentifier", scope = ApexPackageIdentifier.class)
    public JAXBElement<Boolean> createApexPackageIdentifierOptionsPrivateIdentifier(Boolean value) {
        return new JAXBElement<Boolean>(_ApexPackageIdentifierOptionsPrivateIdentifier_QNAME, Boolean.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ColumnNumber", scope = ApexPackageIdentifier.class)
    public JAXBElement<Integer> createApexPackageIdentifierColumnNumber(Integer value) {
        return new JAXBElement<Integer>(_ApexPackageIdentifierColumnNumber_QNAME, Integer.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OptionsPublicIdentifier", scope = ApexPackageIdentifier.class)
    public JAXBElement<Boolean> createApexPackageIdentifierOptionsPublicIdentifier(Boolean value) {
        return new JAXBElement<Boolean>(_ApexPackageIdentifierOptionsPublicIdentifier_QNAME, Boolean.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LineNumber", scope = ApexPackageIdentifier.class)
    public JAXBElement<Integer> createApexPackageIdentifierLineNumber(Integer value) {
        return new JAXBElement<Integer>(_ApexPackageIdentifierLineNumber_QNAME, Integer.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OptionsWebService", scope = ApexPackageIdentifier.class)
    public JAXBElement<Boolean> createApexPackageIdentifierOptionsWebService(Boolean value) {
        return new JAXBElement<Boolean>(_ApexPackageIdentifierOptionsWebService_QNAME, Boolean.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ApexPackageId", scope = ApexPackageIdentifier.class)
    public JAXBElement<String> createApexPackageIdentifierApexPackageId(String value) {
        return new JAXBElement<String>(_ApexPackageIdentifierArgumentApexPackageId_QNAME, String.class, ApexPackageIdentifier.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = TaskPriority.class)
    public JAXBElement<User> createTaskPriorityLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterLabel", scope = TaskPriority.class)
    public JAXBElement<String> createTaskPriorityMasterLabel(String value) {
        return new JAXBElement<String>(_SolutionStatusMasterLabel_QNAME, String.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = TaskPriority.class)
    public JAXBElement<Integer> createTaskPrioritySortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = TaskPriority.class)
    public JAXBElement<User> createTaskPriorityCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = TaskPriority.class)
    public JAXBElement<XMLGregorianCalendar> createTaskPriorityLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = TaskPriority.class)
    public JAXBElement<XMLGregorianCalendar> createTaskPrioritySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsHighPriority", scope = TaskPriority.class)
    public JAXBElement<Boolean> createTaskPriorityIsHighPriority(Boolean value) {
        return new JAXBElement<Boolean>(_TaskPriorityIsHighPriority_QNAME, Boolean.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = TaskPriority.class)
    public JAXBElement<String> createTaskPriorityCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = TaskPriority.class)
    public JAXBElement<String> createTaskPriorityLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = TaskPriority.class)
    public JAXBElement<XMLGregorianCalendar> createTaskPriorityCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDefault", scope = TaskPriority.class)
    public JAXBElement<Boolean> createTaskPriorityIsDefault(Boolean value) {
        return new JAXBElement<Boolean>(_SolutionStatusIsDefault_QNAME, Boolean.class, TaskPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CaseContactRole.class)
    public JAXBElement<User> createCaseContactRoleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CaseContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createCaseContactRoleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Case }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Cases", scope = CaseContactRole.class)
    public JAXBElement<Case> createCaseContactRoleCases(Case value) {
        return new JAXBElement<Case>(_AccountCases_QNAME, Case.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CaseContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createCaseContactRoleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CaseContactRole.class)
    public JAXBElement<String> createCaseContactRoleCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CasesId", scope = CaseContactRole.class)
    public JAXBElement<String> createCaseContactRoleCasesId(String value) {
        return new JAXBElement<String>(_CaseContactRoleCasesId_QNAME, String.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = CaseContactRole.class)
    public JAXBElement<Contact> createCaseContactRoleContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CaseContactRole.class)
    public JAXBElement<Boolean> createCaseContactRoleIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CaseContactRole.class)
    public JAXBElement<User> createCaseContactRoleCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = CaseContactRole.class)
    public JAXBElement<String> createCaseContactRoleRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = CaseContactRole.class)
    public JAXBElement<String> createCaseContactRoleContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CaseContactRole.class)
    public JAXBElement<String> createCaseContactRoleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CaseContactRole.class)
    public JAXBElement<XMLGregorianCalendar> createCaseContactRoleCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CaseContactRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Solution }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Solution", scope = CaseSolution.class)
    public JAXBElement<Solution> createCaseSolutionSolution(Solution value) {
        return new JAXBElement<Solution>(_SolutionHistorySolution_QNAME, Solution.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Case }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Case", scope = CaseSolution.class)
    public JAXBElement<Case> createCaseSolutionCase(Case value) {
        return new JAXBElement<Case>(_CaseHistoryCase_QNAME, Case.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CaseSolution.class)
    public JAXBElement<User> createCaseSolutionCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CaseSolution.class)
    public JAXBElement<XMLGregorianCalendar> createCaseSolutionSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SolutionId", scope = CaseSolution.class)
    public JAXBElement<String> createCaseSolutionSolutionId(String value) {
        return new JAXBElement<String>(_SolutionHistorySolutionId_QNAME, String.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CaseSolution.class)
    public JAXBElement<String> createCaseSolutionCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CaseSolution.class)
    public JAXBElement<Boolean> createCaseSolutionIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CaseSolution.class)
    public JAXBElement<XMLGregorianCalendar> createCaseSolutionCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseId", scope = CaseSolution.class)
    public JAXBElement<String> createCaseSolutionCaseId(String value) {
        return new JAXBElement<String>(_CaseHistoryCaseId_QNAME, String.class, CaseSolution.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityPartner.class)
    public JAXBElement<User> createOpportunityPartnerLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountToId", scope = OpportunityPartner.class)
    public JAXBElement<String> createOpportunityPartnerAccountToId(String value) {
        return new JAXBElement<String>(_AccountPartnerAccountToId_QNAME, String.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityPartner.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityPartnerLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityPartner.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityPartnerSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountTo", scope = OpportunityPartner.class)
    public JAXBElement<Account> createOpportunityPartnerAccountTo(Account value) {
        return new JAXBElement<Account>(_AccountPartnerAccountTo_QNAME, Account.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityPartner.class)
    public JAXBElement<Opportunity> createOpportunityPartnerOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityPartner.class)
    public JAXBElement<String> createOpportunityPartnerCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityPartner.class)
    public JAXBElement<Boolean> createOpportunityPartnerIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityPartner.class)
    public JAXBElement<String> createOpportunityPartnerOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityPartner.class)
    public JAXBElement<User> createOpportunityPartnerCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Role", scope = OpportunityPartner.class)
    public JAXBElement<String> createOpportunityPartnerRole(String value) {
        return new JAXBElement<String>(_AccountContactRoleRole_QNAME, String.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPrimary", scope = OpportunityPartner.class)
    public JAXBElement<Boolean> createOpportunityPartnerIsPrimary(Boolean value) {
        return new JAXBElement<Boolean>(_AccountContactRoleIsPrimary_QNAME, Boolean.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityPartner.class)
    public JAXBElement<String> createOpportunityPartnerLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityPartner.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityPartnerCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityPartner.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = AssignmentRule.class)
    public JAXBElement<User> createAssignmentRuleLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = AssignmentRule.class)
    public JAXBElement<String> createAssignmentRuleName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Active", scope = AssignmentRule.class)
    public JAXBElement<Boolean> createAssignmentRuleActive(Boolean value) {
        return new JAXBElement<Boolean>(_AssignmentRuleActive_QNAME, Boolean.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = AssignmentRule.class)
    public JAXBElement<User> createAssignmentRuleCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = AssignmentRule.class)
    public JAXBElement<XMLGregorianCalendar> createAssignmentRuleLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = AssignmentRule.class)
    public JAXBElement<XMLGregorianCalendar> createAssignmentRuleSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = AssignmentRule.class)
    public JAXBElement<String> createAssignmentRuleCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = AssignmentRule.class)
    public JAXBElement<String> createAssignmentRuleLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SobjectType", scope = AssignmentRule.class)
    public JAXBElement<String> createAssignmentRuleSobjectType(String value) {
        return new JAXBElement<String>(_QueueSobjectSobjectType_QNAME, String.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = AssignmentRule.class)
    public JAXBElement<XMLGregorianCalendar> createAssignmentRuleCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, AssignmentRule.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PricebookEntry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PricebookEntry", scope = OpportunityLineItem.class)
    public JAXBElement<PricebookEntry> createOpportunityLineItemPricebookEntry(PricebookEntry value) {
        return new JAXBElement<PricebookEntry>(_OpportunityLineItemPricebookEntry_QNAME, PricebookEntry.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = OpportunityLineItem.class)
    public JAXBElement<User> createOpportunityLineItemLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TotalPrice", scope = OpportunityLineItem.class)
    public JAXBElement<Double> createOpportunityLineItemTotalPrice(Double value) {
        return new JAXBElement<Double>(_OpportunityLineItemTotalPrice_QNAME, Double.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = OpportunityLineItem.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityLineItemLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = OpportunityLineItem.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityLineItemSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Quantity", scope = OpportunityLineItem.class)
    public JAXBElement<Double> createOpportunityLineItemQuantity(Double value) {
        return new JAXBElement<Double>(_AssetQuantity_QNAME, Double.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Opportunity", scope = OpportunityLineItem.class)
    public JAXBElement<Opportunity> createOpportunityLineItemOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_OpportunityCompetitorOpportunity_QNAME, Opportunity.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = OpportunityLineItem.class)
    public JAXBElement<String> createOpportunityLineItemCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = OpportunityLineItem.class)
    public JAXBElement<Boolean> createOpportunityLineItemIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = OpportunityLineItem.class)
    public JAXBElement<String> createOpportunityLineItemDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityId", scope = OpportunityLineItem.class)
    public JAXBElement<String> createOpportunityLineItemOpportunityId(String value) {
        return new JAXBElement<String>(_OpportunityCompetitorOpportunityId_QNAME, String.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SortOrder", scope = OpportunityLineItem.class)
    public JAXBElement<Integer> createOpportunityLineItemSortOrder(Integer value) {
        return new JAXBElement<Integer>(_SolutionStatusSortOrder_QNAME, Integer.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = OpportunityLineItem.class)
    public JAXBElement<User> createOpportunityLineItemCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PricebookEntryId", scope = OpportunityLineItem.class)
    public JAXBElement<String> createOpportunityLineItemPricebookEntryId(String value) {
        return new JAXBElement<String>(_OpportunityLineItemPricebookEntryId_QNAME, String.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ServiceDate", scope = OpportunityLineItem.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityLineItemServiceDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_OpportunityLineItemServiceDate_QNAME, XMLGregorianCalendar.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ListPrice", scope = OpportunityLineItem.class)
    public JAXBElement<Double> createOpportunityLineItemListPrice(Double value) {
        return new JAXBElement<Double>(_OpportunityLineItemListPrice_QNAME, Double.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = OpportunityLineItem.class)
    public JAXBElement<String> createOpportunityLineItemLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = OpportunityLineItem.class)
    public JAXBElement<XMLGregorianCalendar> createOpportunityLineItemCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UnitPrice", scope = OpportunityLineItem.class)
    public JAXBElement<Double> createOpportunityLineItemUnitPrice(Double value) {
        return new JAXBElement<Double>(_OpportunityLineItemUnitPrice_QNAME, Double.class, OpportunityLineItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = UserTeamMember.class)
    public JAXBElement<User> createUserTeamMemberLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserId", scope = UserTeamMember.class)
    public JAXBElement<String> createUserTeamMemberUserId(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberUserId_QNAME, String.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "User", scope = UserTeamMember.class)
    public JAXBElement<User> createUserTeamMemberUser(User value) {
        return new JAXBElement<User>(_OpportunityTeamMemberUser_QNAME, User.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityAccessLevel", scope = UserTeamMember.class)
    public JAXBElement<String> createUserTeamMemberOpportunityAccessLevel(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberOpportunityAccessLevel_QNAME, String.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = UserTeamMember.class)
    public JAXBElement<User> createUserTeamMemberCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = UserTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createUserTeamMemberLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = UserTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createUserTeamMemberSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = UserTeamMember.class)
    public JAXBElement<String> createUserTeamMemberOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = UserTeamMember.class)
    public JAXBElement<String> createUserTeamMemberCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = UserTeamMember.class)
    public JAXBElement<String> createUserTeamMemberLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "TeamMemberRole", scope = UserTeamMember.class)
    public JAXBElement<String> createUserTeamMemberTeamMemberRole(String value) {
        return new JAXBElement<String>(_OpportunityTeamMemberTeamMemberRole_QNAME, String.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = UserTeamMember.class)
    public JAXBElement<XMLGregorianCalendar> createUserTeamMemberCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, UserTeamMember.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Lead.class)
    public JAXBElement<String> createLeadName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AnnualRevenue", scope = Lead.class)
    public JAXBElement<Double> createLeadAnnualRevenue(Double value) {
        return new JAXBElement<Double>(_AccountAnnualRevenue_QNAME, Double.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Shares", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadShares(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountShares_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsUnreadByOwner", scope = Lead.class)
    public JAXBElement<Boolean> createLeadIsUnreadByOwner(Boolean value) {
        return new JAXBElement<Boolean>(_LeadIsUnreadByOwner_QNAME, Boolean.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FirstName", scope = Lead.class)
    public JAXBElement<String> createLeadFirstName(String value) {
        return new JAXBElement<String>(_ContactFirstName_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Lead.class)
    public JAXBElement<Boolean> createLeadIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedContact", scope = Lead.class)
    public JAXBElement<Contact> createLeadConvertedContact(Contact value) {
        return new JAXBElement<Contact>(_LeadConvertedContact_QNAME, Contact.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Email", scope = Lead.class)
    public JAXBElement<String> createLeadEmail(String value) {
        return new JAXBElement<String>(_ContactEmail_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MasterRecordId", scope = Lead.class)
    public JAXBElement<String> createLeadMasterRecordId(String value) {
        return new JAXBElement<String>(_AccountMasterRecordId_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NotesAndAttachments", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadNotesAndAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotesAndAttachments_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Lead.class)
    public JAXBElement<User> createLeadCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastActivityDate", scope = Lead.class)
    public JAXBElement<XMLGregorianCalendar> createLeadLastActivityDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AccountLastActivityDate_QNAME, XMLGregorianCalendar.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedOpportunityId", scope = Lead.class)
    public JAXBElement<String> createLeadConvertedOpportunityId(String value) {
        return new JAXBElement<String>(_LeadConvertedOpportunityId_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Campaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Campaign", scope = Lead.class)
    public JAXBElement<Campaign> createLeadCampaign(Campaign value) {
        return new JAXBElement<Campaign>(_OpportunityCampaign_QNAME, Campaign.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Company", scope = Lead.class)
    public JAXBElement<String> createLeadCompany(String value) {
        return new JAXBElement<String>(_LeadCompany_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Lead.class)
    public JAXBElement<User> createLeadLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Phone", scope = Lead.class)
    public JAXBElement<String> createLeadPhone(String value) {
        return new JAXBElement<String>(_AccountPhone_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Lead.class)
    public JAXBElement<XMLGregorianCalendar> createLeadLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "MobilePhone", scope = Lead.class)
    public JAXBElement<String> createLeadMobilePhone(String value) {
        return new JAXBElement<String>(_ContactMobilePhone_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedAccount", scope = Lead.class)
    public JAXBElement<Account> createLeadConvertedAccount(Account value) {
        return new JAXBElement<Account>(_LeadConvertedAccount_QNAME, Account.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Notes", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadNotes(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountNotes_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Lead.class)
    public JAXBElement<String> createLeadDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Country", scope = Lead.class)
    public JAXBElement<String> createLeadCountry(String value) {
        return new JAXBElement<String>(_OrganizationCountry_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Fax", scope = Lead.class)
    public JAXBElement<String> createLeadFax(String value) {
        return new JAXBElement<String>(_AccountFax_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Street", scope = Lead.class)
    public JAXBElement<String> createLeadStreet(String value) {
        return new JAXBElement<String>(_OrganizationStreet_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberOfEmployees", scope = Lead.class)
    public JAXBElement<Integer> createLeadNumberOfEmployees(Integer value) {
        return new JAXBElement<Integer>(_AccountNumberOfEmployees_QNAME, Integer.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedContactId", scope = Lead.class)
    public JAXBElement<String> createLeadConvertedContactId(String value) {
        return new JAXBElement<String>(_LeadConvertedContactId_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Title", scope = Lead.class)
    public JAXBElement<String> createLeadTitle(String value) {
        return new JAXBElement<String>(_ContactTitle_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Lead.class)
    public JAXBElement<XMLGregorianCalendar> createLeadSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Lead.class)
    public JAXBElement<String> createLeadCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedAccountId", scope = Lead.class)
    public JAXBElement<String> createLeadConvertedAccountId(String value) {
        return new JAXBElement<String>(_LeadConvertedAccountId_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LeadSource", scope = Lead.class)
    public JAXBElement<String> createLeadLeadSource(String value) {
        return new JAXBElement<String>(_ContactLeadSource_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedDate", scope = Lead.class)
    public JAXBElement<XMLGregorianCalendar> createLeadConvertedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_LeadConvertedDate_QNAME, XMLGregorianCalendar.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Opportunity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ConvertedOpportunity", scope = Lead.class)
    public JAXBElement<Opportunity> createLeadConvertedOpportunity(Opportunity value) {
        return new JAXBElement<Opportunity>(_LeadConvertedOpportunity_QNAME, Opportunity.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Website", scope = Lead.class)
    public JAXBElement<String> createLeadWebsite(String value) {
        return new JAXBElement<String>(_AccountWebsite_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "NumberofLocations__c", scope = Lead.class)
    public JAXBElement<Double> createLeadNumberofLocationsC(Double value) {
        return new JAXBElement<Double>(_AccountNumberofLocationsC_QNAME, Double.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Lead.class)
    public JAXBElement<String> createLeadLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastName", scope = Lead.class)
    public JAXBElement<String> createLeadLastName(String value) {
        return new JAXBElement<String>(_ContactLastName_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "State", scope = Lead.class)
    public JAXBElement<String> createLeadState(String value) {
        return new JAXBElement<String>(_OrganizationState_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EmailStatuses", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadEmailStatuses(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactEmailStatuses_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PostalCode", scope = Lead.class)
    public JAXBElement<String> createLeadPostalCode(String value) {
        return new JAXBElement<String>(_OrganizationPostalCode_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Primary__c", scope = Lead.class)
    public JAXBElement<String> createLeadPrimaryC(String value) {
        return new JAXBElement<String>(_LeadPrimaryC_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Rating", scope = Lead.class)
    public JAXBElement<String> createLeadRating(String value) {
        return new JAXBElement<String>(_AccountRating_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Industry", scope = Lead.class)
    public JAXBElement<String> createLeadIndustry(String value) {
        return new JAXBElement<String>(_AccountIndustry_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SICCode__c", scope = Lead.class)
    public JAXBElement<String> createLeadSICCodeC(String value) {
        return new JAXBElement<String>(_LeadSICCodeC_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Histories", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContractHistories_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Salutation", scope = Lead.class)
    public JAXBElement<String> createLeadSalutation(String value) {
        return new JAXBElement<String>(_ContactSalutation_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "City", scope = Lead.class)
    public JAXBElement<String> createLeadCity(String value) {
        return new JAXBElement<String>(_OrganizationCity_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsConverted", scope = Lead.class)
    public JAXBElement<Boolean> createLeadIsConverted(Boolean value) {
        return new JAXBElement<Boolean>(_LeadStatusIsConverted_QNAME, Boolean.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Lead.class)
    public JAXBElement<String> createLeadStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CampaignMembers", scope = Lead.class)
    public JAXBElement<QueryResult> createLeadCampaignMembers(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactCampaignMembers_QNAME, QueryResult.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Lead.class)
    public JAXBElement<String> createLeadOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProductInterest__c", scope = Lead.class)
    public JAXBElement<String> createLeadProductInterestC(String value) {
        return new JAXBElement<String>(_LeadProductInterestC_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CurrentGenerators__c", scope = Lead.class)
    public JAXBElement<String> createLeadCurrentGeneratorsC(String value) {
        return new JAXBElement<String>(_OpportunityCurrentGeneratorsC_QNAME, String.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Lead.class)
    public JAXBElement<XMLGregorianCalendar> createLeadCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Lead.class)
    public JAXBElement<Name> createLeadOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, Lead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Url", scope = Document.class)
    public JAXBElement<String> createDocumentUrl(String value) {
        return new JAXBElement<String>(_WebLinkUrl_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Document.class)
    public JAXBElement<User> createDocumentLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = Document.class)
    public JAXBElement<String> createDocumentName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsInternalUseOnly", scope = Document.class)
    public JAXBElement<Boolean> createDocumentIsInternalUseOnly(Boolean value) {
        return new JAXBElement<Boolean>(_DocumentIsInternalUseOnly_QNAME, Boolean.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Body", scope = Document.class)
    public JAXBElement<byte[]> createDocumentBody(byte[] value) {
        return new JAXBElement<byte[]>(_AttachmentBody_QNAME, byte[].class, Document.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "FolderId", scope = Document.class)
    public JAXBElement<String> createDocumentFolderId(String value) {
        return new JAXBElement<String>(_EmailTemplateFolderId_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Keywords", scope = Document.class)
    public JAXBElement<String> createDocumentKeywords(String value) {
        return new JAXBElement<String>(_DocumentKeywords_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Document.class)
    public JAXBElement<XMLGregorianCalendar> createDocumentLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Document.class)
    public JAXBElement<XMLGregorianCalendar> createDocumentSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Document.class)
    public JAXBElement<String> createDocumentCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AuthorId", scope = Document.class)
    public JAXBElement<String> createDocumentAuthorId(String value) {
        return new JAXBElement<String>(_DocumentAuthorId_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "BodyLength", scope = Document.class)
    public JAXBElement<Integer> createDocumentBodyLength(Integer value) {
        return new JAXBElement<Integer>(_AttachmentBodyLength_QNAME, Integer.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Document.class)
    public JAXBElement<Boolean> createDocumentIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Document.class)
    public JAXBElement<String> createDocumentDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Author", scope = Document.class)
    public JAXBElement<User> createDocumentAuthor(User value) {
        return new JAXBElement<User>(_DocumentAuthor_QNAME, User.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Document.class)
    public JAXBElement<User> createDocumentCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Document.class)
    public JAXBElement<String> createDocumentType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Document.class)
    public JAXBElement<String> createDocumentLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Document.class)
    public JAXBElement<XMLGregorianCalendar> createDocumentCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContentType", scope = Document.class)
    public JAXBElement<String> createDocumentContentType(String value) {
        return new JAXBElement<String>(_AttachmentContentType_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsPublic", scope = Document.class)
    public JAXBElement<Boolean> createDocumentIsPublic(Boolean value) {
        return new JAXBElement<Boolean>(_DocumentIsPublic_QNAME, Boolean.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Folder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Folder", scope = Document.class)
    public JAXBElement<Folder> createDocumentFolder(Folder value) {
        return new JAXBElement<Folder>(_EmailTemplateFolder_QNAME, Folder.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RowCause", scope = CaseShare.class)
    public JAXBElement<String> createCaseShareRowCause(String value) {
        return new JAXBElement<String>(_OpportunityShareRowCause_QNAME, String.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Case }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Case", scope = CaseShare.class)
    public JAXBElement<Case> createCaseShareCase(Case value) {
        return new JAXBElement<Case>(_CaseHistoryCase_QNAME, Case.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CaseShare.class)
    public JAXBElement<User> createCaseShareLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CaseShare.class)
    public JAXBElement<XMLGregorianCalendar> createCaseShareLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseAccessLevel", scope = CaseShare.class)
    public JAXBElement<String> createCaseShareCaseAccessLevel(String value) {
        return new JAXBElement<String>(_AccountShareCaseAccessLevel_QNAME, String.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UserOrGroupId", scope = CaseShare.class)
    public JAXBElement<String> createCaseShareUserOrGroupId(String value) {
        return new JAXBElement<String>(_GroupMemberUserOrGroupId_QNAME, String.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CaseShare.class)
    public JAXBElement<String> createCaseShareLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CaseShare.class)
    public JAXBElement<Boolean> createCaseShareIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseId", scope = CaseShare.class)
    public JAXBElement<String> createCaseShareCaseId(String value) {
        return new JAXBElement<String>(_CaseHistoryCaseId_QNAME, String.class, CaseShare.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Reason", scope = Case.class)
    public JAXBElement<String> createCaseReason(String value) {
        return new JAXBElement<String>(_CaseReason_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Shares", scope = Case.class)
    public JAXBElement<QueryResult> createCaseShares(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountShares_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AccountId", scope = Case.class)
    public JAXBElement<String> createCaseAccountId(String value) {
        return new JAXBElement<String>(_EventAccountId_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Attachments", scope = Case.class)
    public JAXBElement<QueryResult> createCaseAttachments(QueryResult value) {
        return new JAXBElement<QueryResult>(_EventAttachments_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Tasks", scope = Case.class)
    public JAXBElement<QueryResult> createCaseTasks(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountTasks_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = Case.class)
    public JAXBElement<Boolean> createCaseIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActivityHistories", scope = Case.class)
    public JAXBElement<QueryResult> createCaseActivityHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountActivityHistories_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SLAViolation__c", scope = Case.class)
    public JAXBElement<String> createCaseSLAViolationC(String value) {
        return new JAXBElement<String>(_CaseSLAViolationC_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Solutions", scope = Case.class)
    public JAXBElement<QueryResult> createCaseSolutions(QueryResult value) {
        return new JAXBElement<QueryResult>(_CaseSolutions_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = Case.class)
    public JAXBElement<User> createCaseCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasCommentsUnreadByOwner", scope = Case.class)
    public JAXBElement<Boolean> createCaseHasCommentsUnreadByOwner(Boolean value) {
        return new JAXBElement<Boolean>(_CaseHasCommentsUnreadByOwner_QNAME, Boolean.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Asset }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Asset", scope = Case.class)
    public JAXBElement<Asset> createCaseAsset(Asset value) {
        return new JAXBElement<Asset>(_CaseAsset_QNAME, Asset.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstances", scope = Case.class)
    public JAXBElement<QueryResult> createCaseProcessInstances(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessInstances_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SuppliedCompany", scope = Case.class)
    public JAXBElement<String> createCaseSuppliedCompany(String value) {
        return new JAXBElement<String>(_CaseSuppliedCompany_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpenActivities", scope = Case.class)
    public JAXBElement<QueryResult> createCaseOpenActivities(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountOpenActivities_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = Case.class)
    public JAXBElement<User> createCaseLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SuppliedName", scope = Case.class)
    public JAXBElement<String> createCaseSuppliedName(String value) {
        return new JAXBElement<String>(_CaseSuppliedName_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "HasSelfServiceComments", scope = Case.class)
    public JAXBElement<Boolean> createCaseHasSelfServiceComments(Boolean value) {
        return new JAXBElement<Boolean>(_CaseHasSelfServiceComments_QNAME, Boolean.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = Case.class)
    public JAXBElement<XMLGregorianCalendar> createCaseLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseSolutions", scope = Case.class)
    public JAXBElement<QueryResult> createCaseCaseSolutions(QueryResult value) {
        return new JAXBElement<QueryResult>(_SolutionCaseSolutions_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsEscalated", scope = Case.class)
    public JAXBElement<Boolean> createCaseIsEscalated(Boolean value) {
        return new JAXBElement<Boolean>(_CaseIsEscalated_QNAME, Boolean.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Contact", scope = Case.class)
    public JAXBElement<Contact> createCaseContact(Contact value) {
        return new JAXBElement<Contact>(_AccountContactRoleContact_QNAME, Contact.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "PotentialLiability__c", scope = Case.class)
    public JAXBElement<String> createCasePotentialLiabilityC(String value) {
        return new JAXBElement<String>(_CasePotentialLiabilityC_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Description", scope = Case.class)
    public JAXBElement<String> createCaseDescription(String value) {
        return new JAXBElement<String>(_EventDescription_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Events", scope = Case.class)
    public JAXBElement<QueryResult> createCaseEvents(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountEvents_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Priority", scope = Case.class)
    public JAXBElement<String> createCasePriority(String value) {
        return new JAXBElement<String>(_OpenActivityPriority_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SuppliedEmail", scope = Case.class)
    public JAXBElement<String> createCaseSuppliedEmail(String value) {
        return new JAXBElement<String>(_CaseSuppliedEmail_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "EngineeringReqNumber__c", scope = Case.class)
    public JAXBElement<String> createCaseEngineeringReqNumberC(String value) {
        return new JAXBElement<String>(_CaseEngineeringReqNumberC_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ClosedDate", scope = Case.class)
    public JAXBElement<XMLGregorianCalendar> createCaseClosedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CaseClosedDate_QNAME, XMLGregorianCalendar.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Product__c", scope = Case.class)
    public JAXBElement<String> createCaseProductC(String value) {
        return new JAXBElement<String>(_CaseProductC_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = Case.class)
    public JAXBElement<XMLGregorianCalendar> createCaseSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = Case.class)
    public JAXBElement<String> createCaseCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Type", scope = Case.class)
    public JAXBElement<String> createCaseType(String value) {
        return new JAXBElement<String>(_AccountType_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseContactRoles", scope = Case.class)
    public JAXBElement<QueryResult> createCaseCaseContactRoles(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContactCaseContactRoles_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsClosed", scope = Case.class)
    public JAXBElement<Boolean> createCaseIsClosed(Boolean value) {
        return new JAXBElement<Boolean>(_OpenActivityIsClosed_QNAME, Boolean.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = Case.class)
    public JAXBElement<String> createCaseLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessSteps", scope = Case.class)
    public JAXBElement<QueryResult> createCaseProcessSteps(QueryResult value) {
        return new JAXBElement<QueryResult>(_AccountProcessSteps_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseNumber", scope = Case.class)
    public JAXBElement<String> createCaseCaseNumber(String value) {
        return new JAXBElement<String>(_CaseCaseNumber_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Account }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Account", scope = Case.class)
    public JAXBElement<Account> createCaseAccount(Account value) {
        return new JAXBElement<Account>(_AccountContactRoleAccount_QNAME, Account.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CaseComments", scope = Case.class)
    public JAXBElement<QueryResult> createCaseCaseComments(QueryResult value) {
        return new JAXBElement<QueryResult>(_CaseCaseComments_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "AssetId", scope = Case.class)
    public JAXBElement<String> createCaseAssetId(String value) {
        return new JAXBElement<String>(_CaseAssetId_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Subject", scope = Case.class)
    public JAXBElement<String> createCaseSubject(String value) {
        return new JAXBElement<String>(_EventSubject_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Histories", scope = Case.class)
    public JAXBElement<QueryResult> createCaseHistories(QueryResult value) {
        return new JAXBElement<QueryResult>(_ContractHistories_QNAME, QueryResult.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Status", scope = Case.class)
    public JAXBElement<String> createCaseStatus(String value) {
        return new JAXBElement<String>(_CampaignStatus_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ContactId", scope = Case.class)
    public JAXBElement<String> createCaseContactId(String value) {
        return new JAXBElement<String>(_AccountContactRoleContactId_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OwnerId", scope = Case.class)
    public JAXBElement<String> createCaseOwnerId(String value) {
        return new JAXBElement<String>(_EventOwnerId_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SuppliedPhone", scope = Case.class)
    public JAXBElement<String> createCaseSuppliedPhone(String value) {
        return new JAXBElement<String>(_CaseSuppliedPhone_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = Case.class)
    public JAXBElement<XMLGregorianCalendar> createCaseCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Owner", scope = Case.class)
    public JAXBElement<Name> createCaseOwner(Name value) {
        return new JAXBElement<Name>(_EventOwner_QNAME, Name.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Origin", scope = Case.class)
    public JAXBElement<String> createCaseOrigin(String value) {
        return new JAXBElement<String>(_CaseOrigin_QNAME, String.class, Case.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstance", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<ProcessInstance> createProcessInstanceWorkitemProcessInstance(ProcessInstance value) {
        return new JAXBElement<ProcessInstance>(_ProcessInstanceStepProcessInstance_QNAME, ProcessInstance.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Actor", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<Name> createProcessInstanceWorkitemActor(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceStepActor_QNAME, Name.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ActorId", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<String> createProcessInstanceWorkitemActorId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepActorId_QNAME, String.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Name }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OriginalActor", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<Name> createProcessInstanceWorkitemOriginalActor(Name value) {
        return new JAXBElement<Name>(_ProcessInstanceStepOriginalActor_QNAME, Name.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<User> createProcessInstanceWorkitemCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceWorkitemSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OriginalActorId", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<String> createProcessInstanceWorkitemOriginalActorId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepOriginalActorId_QNAME, String.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<String> createProcessInstanceWorkitemCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProcessInstanceId", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<String> createProcessInstanceWorkitemProcessInstanceId(String value) {
        return new JAXBElement<String>(_ProcessInstanceStepProcessInstanceId_QNAME, String.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<Boolean> createProcessInstanceWorkitemIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = ProcessInstanceWorkitem.class)
    public JAXBElement<XMLGregorianCalendar> createProcessInstanceWorkitemCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, ProcessInstanceWorkitem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = CategoryData.class)
    public JAXBElement<User> createCategoryDataLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = CategoryData.class)
    public JAXBElement<User> createCategoryDataCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = CategoryData.class)
    public JAXBElement<XMLGregorianCalendar> createCategoryDataLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = CategoryData.class)
    public JAXBElement<XMLGregorianCalendar> createCategoryDataSystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = CategoryData.class)
    public JAXBElement<String> createCategoryDataCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = CategoryData.class)
    public JAXBElement<String> createCategoryDataLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "RelatedSobjectId", scope = CategoryData.class)
    public JAXBElement<String> createCategoryDataRelatedSobjectId(String value) {
        return new JAXBElement<String>(_CategoryDataRelatedSobjectId_QNAME, String.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = CategoryData.class)
    public JAXBElement<Boolean> createCategoryDataIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = CategoryData.class)
    public JAXBElement<XMLGregorianCalendar> createCategoryDataCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CategoryNodeId", scope = CategoryData.class)
    public JAXBElement<String> createCategoryDataCategoryNodeId(String value) {
        return new JAXBElement<String>(_CategoryDataCategoryNodeId_QNAME, String.class, CategoryData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsActive", scope = PricebookEntry.class)
    public JAXBElement<Boolean> createPricebookEntryIsActive(Boolean value) {
        return new JAXBElement<Boolean>(_BusinessProcessIsActive_QNAME, Boolean.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedBy", scope = PricebookEntry.class)
    public JAXBElement<User> createPricebookEntryLastModifiedBy(User value) {
        return new JAXBElement<User>(_EventLastModifiedBy_QNAME, User.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Name", scope = PricebookEntry.class)
    public JAXBElement<String> createPricebookEntryName(String value) {
        return new JAXBElement<String>(_BusinessProcessName_QNAME, String.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Pricebook2Id", scope = PricebookEntry.class)
    public JAXBElement<String> createPricebookEntryPricebook2Id(String value) {
        return new JAXBElement<String>(_OpportunityPricebook2Id_QNAME, String.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedDate", scope = PricebookEntry.class)
    public JAXBElement<XMLGregorianCalendar> createPricebookEntryLastModifiedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventLastModifiedDate_QNAME, XMLGregorianCalendar.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "SystemModstamp", scope = PricebookEntry.class)
    public JAXBElement<XMLGregorianCalendar> createPricebookEntrySystemModstamp(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventSystemModstamp_QNAME, XMLGregorianCalendar.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "ProductCode", scope = PricebookEntry.class)
    public JAXBElement<String> createPricebookEntryProductCode(String value) {
        return new JAXBElement<String>(_Product2ProductCode_QNAME, String.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedById", scope = PricebookEntry.class)
    public JAXBElement<String> createPricebookEntryCreatedById(String value) {
        return new JAXBElement<String>(_EventCreatedById_QNAME, String.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Product2Id", scope = PricebookEntry.class)
    public JAXBElement<String> createPricebookEntryProduct2Id(String value) {
        return new JAXBElement<String>(_AssetProduct2Id_QNAME, String.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "IsDeleted", scope = PricebookEntry.class)
    public JAXBElement<Boolean> createPricebookEntryIsDeleted(Boolean value) {
        return new JAXBElement<Boolean>(_EventIsDeleted_QNAME, Boolean.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedBy", scope = PricebookEntry.class)
    public JAXBElement<User> createPricebookEntryCreatedBy(User value) {
        return new JAXBElement<User>(_EventCreatedBy_QNAME, User.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pricebook2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Pricebook2", scope = PricebookEntry.class)
    public JAXBElement<Pricebook2> createPricebookEntryPricebook2(Pricebook2 value) {
        return new JAXBElement<Pricebook2>(_OpportunityPricebook2_QNAME, Pricebook2 .class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Product2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "Product2", scope = PricebookEntry.class)
    public JAXBElement<Product2> createPricebookEntryProduct2(Product2 value) {
        return new JAXBElement<Product2>(_AssetProduct2_QNAME, Product2 .class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UseStandardPrice", scope = PricebookEntry.class)
    public JAXBElement<Boolean> createPricebookEntryUseStandardPrice(Boolean value) {
        return new JAXBElement<Boolean>(_PricebookEntryUseStandardPrice_QNAME, Boolean.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "OpportunityLineItems", scope = PricebookEntry.class)
    public JAXBElement<QueryResult> createPricebookEntryOpportunityLineItems(QueryResult value) {
        return new JAXBElement<QueryResult>(_OpportunityOpportunityLineItems_QNAME, QueryResult.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "LastModifiedById", scope = PricebookEntry.class)
    public JAXBElement<String> createPricebookEntryLastModifiedById(String value) {
        return new JAXBElement<String>(_EventLastModifiedById_QNAME, String.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "CreatedDate", scope = PricebookEntry.class)
    public JAXBElement<XMLGregorianCalendar> createPricebookEntryCreatedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EventCreatedDate_QNAME, XMLGregorianCalendar.class, PricebookEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:sobject.enterprise.soap.sforce.com", name = "UnitPrice", scope = PricebookEntry.class)
    public JAXBElement<Double> createPricebookEntryUnitPrice(Double value) {
        return new JAXBElement<Double>(_OpportunityLineItemUnitPrice_QNAME, Double.class, PricebookEntry.class, value);
    }

}
