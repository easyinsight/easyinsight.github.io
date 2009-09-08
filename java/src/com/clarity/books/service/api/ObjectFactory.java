
package com.clarity.books.service.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.clarity.books.service.api package. 
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

    private final static QName _StoreFileInitialContent_QNAME = new QName("", "initialContent");
    private final static QName _GetSiteTitleResponse_QNAME = new QName("http://api.service.books/", "getSiteTitleResponse");
    private final static QName _DecodeAuthToken_QNAME = new QName("http://api.service.books/", "decodeAuthToken");
    private final static QName _CountJournalEntriesResponse_QNAME = new QName("http://api.service.books/", "countJournalEntriesResponse");
    private final static QName _AddNewVendor_QNAME = new QName("http://api.service.books/", "addNewVendor");
    private final static QName _GetRoles_QNAME = new QName("http://api.service.books/", "getRoles");
    private final static QName _CountJournalEntries_QNAME = new QName("http://api.service.books/", "countJournalEntries");
    private final static QName _GetRolesResponse_QNAME = new QName("http://api.service.books/", "getRolesResponse");
    private final static QName _GetMyUser_QNAME = new QName("http://api.service.books/", "getMyUser");
    private final static QName _AddBusinessResponse_QNAME = new QName("http://api.service.books/", "addBusinessResponse");
    private final static QName _ReadFile_QNAME = new QName("http://api.service.books/", "readFile");
    private final static QName _SaveEmailSettingsResponse_QNAME = new QName("http://api.service.books/", "saveEmailSettingsResponse");
    private final static QName _GetBasicTaxInfo_QNAME = new QName("http://api.service.books/", "getBasicTaxInfo");
    private final static QName _GetBusinessesForUserResponse_QNAME = new QName("http://api.service.books/", "getBusinessesForUserResponse");
    private final static QName _GetUnclearedTransactionsResponse_QNAME = new QName("http://api.service.books/", "getUnclearedTransactionsResponse");
    private final static QName _GetRecordResponse_QNAME = new QName("http://api.service.books/", "getRecordResponse");
    private final static QName _InternalError_QNAME = new QName("http://api.service.books/", "InternalError");
    private final static QName _GetCurrencies_QNAME = new QName("http://api.service.books/", "getCurrencies");
    private final static QName _NotFound_QNAME = new QName("http://api.service.books/", "NotFound");
    private final static QName _SetTaxRateResponse_QNAME = new QName("http://api.service.books/", "setTaxRateResponse");
    private final static QName _GetAllProvincesOrStatesResponse_QNAME = new QName("http://api.service.books/", "getAllProvincesOrStatesResponse");
    private final static QName _GetTimeZones_QNAME = new QName("http://api.service.books/", "getTimeZones");
    private final static QName _SaveSubscriptionResponse_QNAME = new QName("http://api.service.books/", "saveSubscriptionResponse");
    private final static QName _BasicTaxInfo_QNAME = new QName("http://api.service.books/", "basicTaxInfo");
    private final static QName _GetUserResponse_QNAME = new QName("http://api.service.books/", "getUserResponse");
    private final static QName _GetRegionResponse_QNAME = new QName("http://api.service.books/", "getRegionResponse");
    private final static QName _SaveContactResponse_QNAME = new QName("http://api.service.books/", "saveContactResponse");
    private final static QName _CompleteAttachmentResponse_QNAME = new QName("http://api.service.books/", "completeAttachmentResponse");
    private final static QName _CountRecordsOfType_QNAME = new QName("http://api.service.books/", "countRecordsOfType");
    private final static QName _GetFuturePaySetupUrl_QNAME = new QName("http://api.service.books/", "getFuturePaySetupUrl");
    private final static QName _GetRecordsForContact_QNAME = new QName("http://api.service.books/", "getRecordsForContact");
    private final static QName _SignUp_QNAME = new QName("http://api.service.books/", "signUp");
    private final static QName _GetBasicTaxInfoResponse_QNAME = new QName("http://api.service.books/", "getBasicTaxInfoResponse");
    private final static QName _CheckEmailVerificationCode_QNAME = new QName("http://api.service.books/", "checkEmailVerificationCode");
    private final static QName _CreateRestrictedToken_QNAME = new QName("http://api.service.books/", "createRestrictedToken");
    private final static QName _PredictAccountNumberResponse_QNAME = new QName("http://api.service.books/", "predictAccountNumberResponse");
    private final static QName _GetAutomaticMessages_QNAME = new QName("http://api.service.books/", "getAutomaticMessages");
    private final static QName _GetPaymentSetupInfoForBusiness_QNAME = new QName("http://api.service.books/", "getPaymentSetupInfoForBusiness");
    private final static QName _GetItems_QNAME = new QName("http://api.service.books/", "getItems");
    private final static QName _GetAvailablePaymentMethods_QNAME = new QName("http://api.service.books/", "getAvailablePaymentMethods");
    private final static QName _GetCustomersResponse_QNAME = new QName("http://api.service.books/", "getCustomersResponse");
    private final static QName _CreateRestrictedTokenResponse_QNAME = new QName("http://api.service.books/", "createRestrictedTokenResponse");
    private final static QName _GetWorldPay_QNAME = new QName("http://api.service.books/", "getWorldPay");
    private final static QName _GetLedgerResponse_QNAME = new QName("http://api.service.books/", "getLedgerResponse");
    private final static QName _CompleteSignUpResponse_QNAME = new QName("http://api.service.books/", "completeSignUpResponse");
    private final static QName _DoLogin_QNAME = new QName("http://api.service.books/", "doLogin");
    private final static QName _GetSubcategories_QNAME = new QName("http://api.service.books/", "getSubcategories");
    private final static QName _ClearTaxRateResponse_QNAME = new QName("http://api.service.books/", "clearTaxRateResponse");
    private final static QName _GetRecord_QNAME = new QName("http://api.service.books/", "getRecord");
    private final static QName _SaveWorldPay_QNAME = new QName("http://api.service.books/", "saveWorldPay");
    private final static QName _GetBankReconciliations_QNAME = new QName("http://api.service.books/", "getBankReconciliations");
    private final static QName _BadArgument_QNAME = new QName("http://api.service.books/", "BadArgument");
    private final static QName _ReadFileResponse_QNAME = new QName("http://api.service.books/", "readFileResponse");
    private final static QName _GetPaymentSetupInfoForBusinessResponse_QNAME = new QName("http://api.service.books/", "getPaymentSetupInfoForBusinessResponse");
    private final static QName _FindRegion_QNAME = new QName("http://api.service.books/", "findRegion");
    private final static QName _ConflictsWithAnother_QNAME = new QName("http://api.service.books/", "ConflictsWithAnother");
    private final static QName _AddNewCustomerResponse_QNAME = new QName("http://api.service.books/", "addNewCustomerResponse");
    private final static QName _GetSubscriptionPlansResponse_QNAME = new QName("http://api.service.books/", "getSubscriptionPlansResponse");
    private final static QName _AlreadyExists_QNAME = new QName("http://api.service.books/", "AlreadyExists");
    private final static QName _GetAccountByNameResponse_QNAME = new QName("http://api.service.books/", "getAccountByNameResponse");
    private final static QName _RetryContractPaymentResponse_QNAME = new QName("http://api.service.books/", "retryContractPaymentResponse");
    private final static QName _GetVendorsResponse_QNAME = new QName("http://api.service.books/", "getVendorsResponse");
    private final static QName _GetCountriesResponse_QNAME = new QName("http://api.service.books/", "getCountriesResponse");
    private final static QName _GetTaxRate_QNAME = new QName("http://api.service.books/", "getTaxRate");
    private final static QName _GetCollaborationContextResponse_QNAME = new QName("http://api.service.books/", "getCollaborationContextResponse");
    private final static QName _RequestEmailVerificationCodeResponse_QNAME = new QName("http://api.service.books/", "requestEmailVerificationCodeResponse");
    private final static QName _GetBankReconciliationResponse_QNAME = new QName("http://api.service.books/", "getBankReconciliationResponse");
    private final static QName _BadPassword_QNAME = new QName("http://api.service.books/", "BadPassword");
    private final static QName _GetStatementOpeningBalancesResponse_QNAME = new QName("http://api.service.books/", "getStatementOpeningBalancesResponse");
    private final static QName _GetUserByEmail_QNAME = new QName("http://api.service.books/", "getUserByEmail");
    private final static QName _GetVatTaxInfoResponse_QNAME = new QName("http://api.service.books/", "getVatTaxInfoResponse");
    private final static QName _GetBalanceAtDate_QNAME = new QName("http://api.service.books/", "getBalanceAtDate");
    private final static QName _SaveVatTaxInfoResponse_QNAME = new QName("http://api.service.books/", "saveVatTaxInfoResponse");
    private final static QName _GetContactResponse_QNAME = new QName("http://api.service.books/", "getContactResponse");
    private final static QName _SetContractLinksResponse_QNAME = new QName("http://api.service.books/", "setContractLinksResponse");
    private final static QName _CompletePasswordResetResponse_QNAME = new QName("http://api.service.books/", "completePasswordResetResponse");
    private final static QName _SaveBankReconciliation_QNAME = new QName("http://api.service.books/", "saveBankReconciliation");
    private final static QName _GetDistrictsOrCountiesResponse_QNAME = new QName("http://api.service.books/", "getDistrictsOrCountiesResponse");
    private final static QName _GetLedger_QNAME = new QName("http://api.service.books/", "getLedger");
    private final static QName _GetSubscriptionPlan_QNAME = new QName("http://api.service.books/", "getSubscriptionPlan");
    private final static QName _GetSetting_QNAME = new QName("http://api.service.books/", "getSetting");
    private final static QName _SkipTrial_QNAME = new QName("http://api.service.books/", "skipTrial");
    private final static QName _GetItemResponse_QNAME = new QName("http://api.service.books/", "getItemResponse");
    private final static QName _SaveVatTaxInfo_QNAME = new QName("http://api.service.books/", "saveVatTaxInfo");
    private final static QName _GetHomeCurrencyResponse_QNAME = new QName("http://api.service.books/", "getHomeCurrencyResponse");
    private final static QName _CountRecordsForContact_QNAME = new QName("http://api.service.books/", "countRecordsForContact");
    private final static QName _CountBankReconciliationsResponse_QNAME = new QName("http://api.service.books/", "countBankReconciliationsResponse");
    private final static QName _CanadianTaxInfo_QNAME = new QName("http://api.service.books/", "canadianTaxInfo");
    private final static QName _GetContact_QNAME = new QName("http://api.service.books/", "getContact");
    private final static QName _AddNewCustomer_QNAME = new QName("http://api.service.books/", "addNewCustomer");
    private final static QName _GetItemSubcategoryResponse_QNAME = new QName("http://api.service.books/", "getItemSubcategoryResponse");
    private final static QName _GetCollaborationContext_QNAME = new QName("http://api.service.books/", "getCollaborationContext");
    private final static QName _GetBusinessesForUser_QNAME = new QName("http://api.service.books/", "getBusinessesForUser");
    private final static QName _CountRecordsResponse_QNAME = new QName("http://api.service.books/", "countRecordsResponse");
    private final static QName _GetItemSubcategory_QNAME = new QName("http://api.service.books/", "getItemSubcategory");
    private final static QName _SaveCanadianTaxInfo_QNAME = new QName("http://api.service.books/", "saveCanadianTaxInfo");
    private final static QName _GetDefaultRegion_QNAME = new QName("http://api.service.books/", "getDefaultRegion");
    private final static QName _PredictRecordNumber_QNAME = new QName("http://api.service.books/", "predictRecordNumber");
    private final static QName _GetEmailSettingsResponse_QNAME = new QName("http://api.service.books/", "getEmailSettingsResponse");
    private final static QName _GetAllDistrictsOrCountiesResponse_QNAME = new QName("http://api.service.books/", "getAllDistrictsOrCountiesResponse");
    private final static QName _SetContractPlanResponse_QNAME = new QName("http://api.service.books/", "setContractPlanResponse");
    private final static QName _PredictAccountNumber_QNAME = new QName("http://api.service.books/", "predictAccountNumber");
    private final static QName _RequestPasswordReset_QNAME = new QName("http://api.service.books/", "requestPasswordReset");
    private final static QName _ExtendLoginResponse_QNAME = new QName("http://api.service.books/", "extendLoginResponse");
    private final static QName _GetContractResponse_QNAME = new QName("http://api.service.books/", "getContractResponse");
    private final static QName _GetLinkedContacts_QNAME = new QName("http://api.service.books/", "getLinkedContacts");
    private final static QName _GetAvailablePaymentMethodsResponse_QNAME = new QName("http://api.service.books/", "getAvailablePaymentMethodsResponse");
    private final static QName _GetVendors_QNAME = new QName("http://api.service.books/", "getVendors");
    private final static QName _GetContactsOfType_QNAME = new QName("http://api.service.books/", "getContactsOfType");
    private final static QName _AddBusiness_QNAME = new QName("http://api.service.books/", "addBusiness");
    private final static QName _GetContacts_QNAME = new QName("http://api.service.books/", "getContacts");
    private final static QName _GetSubcategoriesResponse_QNAME = new QName("http://api.service.books/", "getSubcategoriesResponse");
    private final static QName _GetSubscriptionResponse_QNAME = new QName("http://api.service.books/", "getSubscriptionResponse");
    private final static QName _RequestEmailVerificationCode_QNAME = new QName("http://api.service.books/", "requestEmailVerificationCode");
    private final static QName _SaveBusiness_QNAME = new QName("http://api.service.books/", "saveBusiness");
    private final static QName _GetUnpaidRecordsResponse_QNAME = new QName("http://api.service.books/", "getUnpaidRecordsResponse");
    private final static QName _CountRecords_QNAME = new QName("http://api.service.books/", "countRecords");
    private final static QName _SaveContact_QNAME = new QName("http://api.service.books/", "saveContact");
    private final static QName _GetCurrency_QNAME = new QName("http://api.service.books/", "getCurrency");
    private final static QName _GetFileResponse_QNAME = new QName("http://api.service.books/", "getFileResponse");
    private final static QName _DebitFuturePayAgreementResponse_QNAME = new QName("http://api.service.books/", "debitFuturePayAgreementResponse");
    private final static QName _AppendToAttachmentResponse_QNAME = new QName("http://api.service.books/", "appendToAttachmentResponse");
    private final static QName _GetSettingResponse_QNAME = new QName("http://api.service.books/", "getSettingResponse");
    private final static QName _GetOverpaidRecordsResponse_QNAME = new QName("http://api.service.books/", "getOverpaidRecordsResponse");
    private final static QName _AppendToAttachment_QNAME = new QName("http://api.service.books/", "appendToAttachment");
    private final static QName _GetUserByEmailResponse_QNAME = new QName("http://api.service.books/", "getUserByEmailResponse");
    private final static QName _GetWorldPayResponse_QNAME = new QName("http://api.service.books/", "getWorldPayResponse");
    private final static QName _GetContactsResponse_QNAME = new QName("http://api.service.books/", "getContactsResponse");
    private final static QName _SubmitFeedbackResponse_QNAME = new QName("http://api.service.books/", "submitFeedbackResponse");
    private final static QName _GetUsers_QNAME = new QName("http://api.service.books/", "getUsers");
    private final static QName _SendTestEmailResponse_QNAME = new QName("http://api.service.books/", "sendTestEmailResponse");
    private final static QName _GetSubscriptionsWithContactResponse_QNAME = new QName("http://api.service.books/", "getSubscriptionsWithContactResponse");
    private final static QName _CheckEmailVerificationCodeResponse_QNAME = new QName("http://api.service.books/", "checkEmailVerificationCodeResponse");
    private final static QName _AddRoleResponse_QNAME = new QName("http://api.service.books/", "addRoleResponse");
    private final static QName _SetRoles_QNAME = new QName("http://api.service.books/", "setRoles");
    private final static QName _GetBalancesResponse_QNAME = new QName("http://api.service.books/", "getBalancesResponse");
    private final static QName _SaveEmailSettings_QNAME = new QName("http://api.service.books/", "saveEmailSettings");
    private final static QName _GetSubscriptionsWithContact_QNAME = new QName("http://api.service.books/", "getSubscriptionsWithContact");
    private final static QName _SaveCanadianTaxInfoResponse_QNAME = new QName("http://api.service.books/", "saveCanadianTaxInfoResponse");
    private final static QName _GetItem_QNAME = new QName("http://api.service.books/", "getItem");
    private final static QName _CountRecordsOfTypeResponse_QNAME = new QName("http://api.service.books/", "countRecordsOfTypeResponse");
    private final static QName _SaveCollaborationEventResponse_QNAME = new QName("http://api.service.books/", "saveCollaborationEventResponse");
    private final static QName _GetTax_QNAME = new QName("http://api.service.books/", "getTax");
    private final static QName _SaveBankReconciliationResponse_QNAME = new QName("http://api.service.books/", "saveBankReconciliationResponse");
    private final static QName _GetRecordsOfType_QNAME = new QName("http://api.service.books/", "getRecordsOfType");
    private final static QName _CountChangesets_QNAME = new QName("http://api.service.books/", "countChangesets");
    private final static QName _SetTaxRate_QNAME = new QName("http://api.service.books/", "setTaxRate");
    private final static QName _RetryContractPayment_QNAME = new QName("http://api.service.books/", "retryContractPayment");
    private final static QName _SkipTrialResponse_QNAME = new QName("http://api.service.books/", "skipTrialResponse");
    private final static QName _GetBusiness_QNAME = new QName("http://api.service.books/", "getBusiness");
    private final static QName _GetLedgerLengthResponse_QNAME = new QName("http://api.service.books/", "getLedgerLengthResponse");
    private final static QName _GetFile_QNAME = new QName("http://api.service.books/", "getFile");
    private final static QName _SaveBasicTaxInfoResponse_QNAME = new QName("http://api.service.books/", "saveBasicTaxInfoResponse");
    private final static QName _GetAutomaticMessageResponse_QNAME = new QName("http://api.service.books/", "getAutomaticMessageResponse");
    private final static QName _GetItemsResponse_QNAME = new QName("http://api.service.books/", "getItemsResponse");
    private final static QName _StoreFile_QNAME = new QName("http://api.service.books/", "storeFile");
    private final static QName _GetBalances_QNAME = new QName("http://api.service.books/", "getBalances");
    private final static QName _SaveUser_QNAME = new QName("http://api.service.books/", "saveUser");
    private final static QName _SaveRecord_QNAME = new QName("http://api.service.books/", "saveRecord");
    private final static QName _ExtendLogin_QNAME = new QName("http://api.service.books/", "extendLogin");
    private final static QName _NotActivated_QNAME = new QName("http://api.service.books/", "NotActivated");
    private final static QName _AppendToFile_QNAME = new QName("http://api.service.books/", "appendToFile");
    private final static QName _SaveBasicTaxInfo_QNAME = new QName("http://api.service.books/", "saveBasicTaxInfo");
    private final static QName _PredictRecordNumberResponse_QNAME = new QName("http://api.service.books/", "predictRecordNumberResponse");
    private final static QName _DecodeAuthTokenResponse_QNAME = new QName("http://api.service.books/", "decodeAuthTokenResponse");
    private final static QName _ReadAttachmentResponse_QNAME = new QName("http://api.service.books/", "readAttachmentResponse");
    private final static QName _ChangeUserPasswordResponse_QNAME = new QName("http://api.service.books/", "changeUserPasswordResponse");
    private final static QName _GetCurrencyResponse_QNAME = new QName("http://api.service.books/", "getCurrencyResponse");
    private final static QName _AddUserResponse_QNAME = new QName("http://api.service.books/", "addUserResponse");
    private final static QName _SaveRecordResponse_QNAME = new QName("http://api.service.books/", "saveRecordResponse");
    private final static QName _AddUser_QNAME = new QName("http://api.service.books/", "addUser");
    private final static QName _GetAttachmentResponse_QNAME = new QName("http://api.service.books/", "getAttachmentResponse");
    private final static QName _GetCustomers_QNAME = new QName("http://api.service.books/", "getCustomers");
    private final static QName _SaveSubscriptionPlan_QNAME = new QName("http://api.service.books/", "saveSubscriptionPlan");
    private final static QName _GetStatementOpeningBalances_QNAME = new QName("http://api.service.books/", "getStatementOpeningBalances");
    private final static QName _GetExchangeRate_QNAME = new QName("http://api.service.books/", "getExchangeRate");
    private final static QName _NotAllowed_QNAME = new QName("http://api.service.books/", "NotAllowed");
    private final static QName _SaveWorldPayResponse_QNAME = new QName("http://api.service.books/", "saveWorldPayResponse");
    private final static QName _CountBankReconciliations_QNAME = new QName("http://api.service.books/", "countBankReconciliations");
    private final static QName _GetBalance_QNAME = new QName("http://api.service.books/", "getBalance");
    private final static QName _GetEmailSettings_QNAME = new QName("http://api.service.books/", "getEmailSettings");
    private final static QName _GetSubscriptionPlans_QNAME = new QName("http://api.service.books/", "getSubscriptionPlans");
    private final static QName _SaveAutomaticMessage_QNAME = new QName("http://api.service.books/", "saveAutomaticMessage");
    private final static QName _GetTaxResponse_QNAME = new QName("http://api.service.books/", "getTaxResponse");
    private final static QName _GetMyUserResponse_QNAME = new QName("http://api.service.books/", "getMyUserResponse");
    private final static QName _GetAvailableContractPlansResponse_QNAME = new QName("http://api.service.books/", "getAvailableContractPlansResponse");
    private final static QName _GetFuturePaySetupUrlResponse_QNAME = new QName("http://api.service.books/", "getFuturePaySetupUrlResponse");
    private final static QName _GetBalanceAtDateResponse_QNAME = new QName("http://api.service.books/", "getBalanceAtDateResponse");
    private final static QName _GetRegion_QNAME = new QName("http://api.service.books/", "getRegion");
    private final static QName _GetCanadianTaxInfoResponse_QNAME = new QName("http://api.service.books/", "getCanadianTaxInfoResponse");
    private final static QName _GetProvincesOrStatesResponse_QNAME = new QName("http://api.service.books/", "getProvincesOrStatesResponse");
    private final static QName _CompleteAttachment_QNAME = new QName("http://api.service.books/", "completeAttachment");
    private final static QName _CountRecordsForContactResponse_QNAME = new QName("http://api.service.books/", "countRecordsForContactResponse");
    private final static QName _TestAutomaticMessage_QNAME = new QName("http://api.service.books/", "testAutomaticMessage");
    private final static QName _GetUser_QNAME = new QName("http://api.service.books/", "getUser");
    private final static QName _CompleteFileResponse_QNAME = new QName("http://api.service.books/", "completeFileResponse");
    private final static QName _GetDistrictsOrCounties_QNAME = new QName("http://api.service.books/", "getDistrictsOrCounties");
    private final static QName _GetAccountsResponse_QNAME = new QName("http://api.service.books/", "getAccountsResponse");
    private final static QName _GetAutomaticMessagesResponse_QNAME = new QName("http://api.service.books/", "getAutomaticMessagesResponse");
    private final static QName _GetJournalEntries_QNAME = new QName("http://api.service.books/", "getJournalEntries");
    private final static QName _GetAccountByName_QNAME = new QName("http://api.service.books/", "getAccountByName");
    private final static QName _GetBankReconciliation_QNAME = new QName("http://api.service.books/", "getBankReconciliation");
    private final static QName _DoLogout_QNAME = new QName("http://api.service.books/", "doLogout");
    private final static QName _GetTimeZonesResponse_QNAME = new QName("http://api.service.books/", "getTimeZonesResponse");
    private final static QName _SendTestEmail_QNAME = new QName("http://api.service.books/", "sendTestEmail");
    private final static QName _AddAttachment_QNAME = new QName("http://api.service.books/", "addAttachment");
    private final static QName _GetExchangeRateResponse_QNAME = new QName("http://api.service.books/", "getExchangeRateResponse");
    private final static QName _GetAllProvincesOrStates_QNAME = new QName("http://api.service.books/", "getAllProvincesOrStates");
    private final static QName _DoLogoutResponse_QNAME = new QName("http://api.service.books/", "doLogoutResponse");
    private final static QName _GetSubscriptionsWithPlan_QNAME = new QName("http://api.service.books/", "getSubscriptionsWithPlan");
    private final static QName _AddTax_QNAME = new QName("http://api.service.books/", "addTax");
    private final static QName _EncodeAuthToken_QNAME = new QName("http://api.service.books/", "encodeAuthToken");
    private final static QName _GetLinkedContactsResponse_QNAME = new QName("http://api.service.books/", "getLinkedContactsResponse");
    private final static QName _GetUsersResponse_QNAME = new QName("http://api.service.books/", "getUsersResponse");
    private final static QName _GetRecordsForContactResponse_QNAME = new QName("http://api.service.books/", "getRecordsForContactResponse");
    private final static QName _VatTaxInfo_QNAME = new QName("http://api.service.books/", "vatTaxInfo");
    private final static QName _GetRecords_QNAME = new QName("http://api.service.books/", "getRecords");
    private final static QName _GetAllItems_QNAME = new QName("http://api.service.books/", "getAllItems");
    private final static QName _SaveSubscriptionPlanResponse_QNAME = new QName("http://api.service.books/", "saveSubscriptionPlanResponse");
    private final static QName _GetAttachment_QNAME = new QName("http://api.service.books/", "getAttachment");
    private final static QName _AlreadyActivated_QNAME = new QName("http://api.service.books/", "AlreadyActivated");
    private final static QName _GetRecordsOfTypeResponse_QNAME = new QName("http://api.service.books/", "getRecordsOfTypeResponse");
    private final static QName _GetBusinessResponse_QNAME = new QName("http://api.service.books/", "getBusinessResponse");
    private final static QName _DebitFuturePayAgreement_QNAME = new QName("http://api.service.books/", "debitFuturePayAgreement");
    private final static QName _GetBalancesAtDate_QNAME = new QName("http://api.service.books/", "getBalancesAtDate");
    private final static QName _GetTaxCodes_QNAME = new QName("http://api.service.books/", "getTaxCodes");
    private final static QName _GetAllDistrictsOrCounties_QNAME = new QName("http://api.service.books/", "getAllDistrictsOrCounties");
    private final static QName _SubmitFeedback_QNAME = new QName("http://api.service.books/", "submitFeedback");
    private final static QName _TestAutomaticMessageResponse_QNAME = new QName("http://api.service.books/", "testAutomaticMessageResponse");
    private final static QName _GetBalanceResponse_QNAME = new QName("http://api.service.books/", "getBalanceResponse");
    private final static QName _GetSubscriptionPlanResponse_QNAME = new QName("http://api.service.books/", "getSubscriptionPlanResponse");
    private final static QName _GetAllItemsResponse_QNAME = new QName("http://api.service.books/", "getAllItemsResponse");
    private final static QName _GetCurrencyByCode_QNAME = new QName("http://api.service.books/", "getCurrencyByCode");
    private final static QName _FindRegionResponse_QNAME = new QName("http://api.service.books/", "findRegionResponse");
    private final static QName _SaveAccount_QNAME = new QName("http://api.service.books/", "saveAccount");
    private final static QName _GetFuturePayAgreement_QNAME = new QName("http://api.service.books/", "getFuturePayAgreement");
    private final static QName _GetMyBusinessesResponse_QNAME = new QName("http://api.service.books/", "getMyBusinessesResponse");
    private final static QName _GetChangesetsResponse_QNAME = new QName("http://api.service.books/", "getChangesetsResponse");
    private final static QName _ValidationFailed_QNAME = new QName("http://api.service.books/", "ValidationFailed");
    private final static QName _RequestPasswordResetResponse_QNAME = new QName("http://api.service.books/", "requestPasswordResetResponse");
    private final static QName _GetContract_QNAME = new QName("http://api.service.books/", "getContract");
    private final static QName _EncodeAuthTokenResponse_QNAME = new QName("http://api.service.books/", "encodeAuthTokenResponse");
    private final static QName _GetRecordsResponse_QNAME = new QName("http://api.service.books/", "getRecordsResponse");
    private final static QName _SaveUserResponse_QNAME = new QName("http://api.service.books/", "saveUserResponse");
    private final static QName _GetContactsOfTypeResponse_QNAME = new QName("http://api.service.books/", "getContactsOfTypeResponse");
    private final static QName _GetTaxCodesResponse_QNAME = new QName("http://api.service.books/", "getTaxCodesResponse");
    private final static QName _GetSubscription_QNAME = new QName("http://api.service.books/", "getSubscription");
    private final static QName _GetAccountResponse_QNAME = new QName("http://api.service.books/", "getAccountResponse");
    private final static QName _GetCurrencyByCodeResponse_QNAME = new QName("http://api.service.books/", "getCurrencyByCodeResponse");
    private final static QName _GetLedgerLength_QNAME = new QName("http://api.service.books/", "getLedgerLength");
    private final static QName _SaveItemResponse_QNAME = new QName("http://api.service.books/", "saveItemResponse");
    private final static QName _SetContractPlan_QNAME = new QName("http://api.service.books/", "setContractPlan");
    private final static QName _DoLoginResponse_QNAME = new QName("http://api.service.books/", "doLoginResponse");
    private final static QName _SaveTaxCodes_QNAME = new QName("http://api.service.books/", "saveTaxCodes");
    private final static QName _GetTaxesResponse_QNAME = new QName("http://api.service.books/", "getTaxesResponse");
    private final static QName _SaveBusinessResponse_QNAME = new QName("http://api.service.books/", "saveBusinessResponse");
    private final static QName _AddTaxResponse_QNAME = new QName("http://api.service.books/", "addTaxResponse");
    private final static QName _GetTimeInZone_QNAME = new QName("http://api.service.books/", "getTimeInZone");
    private final static QName _StoreFileResponse_QNAME = new QName("http://api.service.books/", "storeFileResponse");
    private final static QName _SaveAutomaticMessageResponse_QNAME = new QName("http://api.service.books/", "saveAutomaticMessageResponse");
    private final static QName _GetCurrenciesResponse_QNAME = new QName("http://api.service.books/", "getCurrenciesResponse");
    private final static QName _ConcurrentChangeException_QNAME = new QName("http://api.service.books/", "ConcurrentChangeException");
    private final static QName _GetBalancesAtDateResponse_QNAME = new QName("http://api.service.books/", "getBalancesAtDateResponse");
    private final static QName _GetAccounts_QNAME = new QName("http://api.service.books/", "getAccounts");
    private final static QName _GetPublicSettingResponse_QNAME = new QName("http://api.service.books/", "getPublicSettingResponse");
    private final static QName _SaveAccountResponse_QNAME = new QName("http://api.service.books/", "saveAccountResponse");
    private final static QName _SignUpResponse_QNAME = new QName("http://api.service.books/", "signUpResponse");
    private final static QName _GetPublicSetting_QNAME = new QName("http://api.service.books/", "getPublicSetting");
    private final static QName _CountSubscriptionsWithPlan_QNAME = new QName("http://api.service.books/", "countSubscriptionsWithPlan");
    private final static QName _GetUnpaidRecords_QNAME = new QName("http://api.service.books/", "getUnpaidRecords");
    private final static QName _GetAccount_QNAME = new QName("http://api.service.books/", "getAccount");
    private final static QName _CompleteSignUp_QNAME = new QName("http://api.service.books/", "completeSignUp");
    private final static QName _AddRole_QNAME = new QName("http://api.service.books/", "addRole");
    private final static QName _GetTaxRateResponse_QNAME = new QName("http://api.service.books/", "getTaxRateResponse");
    private final static QName _GetUnclearedTransactions_QNAME = new QName("http://api.service.books/", "getUnclearedTransactions");
    private final static QName _GetCanadianTaxInfo_QNAME = new QName("http://api.service.books/", "getCanadianTaxInfo");
    private final static QName _GetOverpaidRecords_QNAME = new QName("http://api.service.books/", "getOverpaidRecords");
    private final static QName _GetSiteTitle_QNAME = new QName("http://api.service.books/", "getSiteTitle");
    private final static QName _SaveItem_QNAME = new QName("http://api.service.books/", "saveItem");
    private final static QName _CompleteFile_QNAME = new QName("http://api.service.books/", "completeFile");
    private final static QName _GetAvailableContractPlans_QNAME = new QName("http://api.service.books/", "getAvailableContractPlans");
    private final static QName _GetBankReconciliationsResponse_QNAME = new QName("http://api.service.books/", "getBankReconciliationsResponse");
    private final static QName _GetTimeInZoneResponse_QNAME = new QName("http://api.service.books/", "getTimeInZoneResponse");
    private final static QName _CountChangesetsResponse_QNAME = new QName("http://api.service.books/", "countChangesetsResponse");
    private final static QName _CompletePasswordReset_QNAME = new QName("http://api.service.books/", "completePasswordReset");
    private final static QName _GetHomeCurrency_QNAME = new QName("http://api.service.books/", "getHomeCurrency");
    private final static QName _SaveSubscription_QNAME = new QName("http://api.service.books/", "saveSubscription");
    private final static QName _CountSubscriptionsWithPlanResponse_QNAME = new QName("http://api.service.books/", "countSubscriptionsWithPlanResponse");
    private final static QName _GetAutomaticMessage_QNAME = new QName("http://api.service.books/", "getAutomaticMessage");
    private final static QName _GetTaxes_QNAME = new QName("http://api.service.books/", "getTaxes");
    private final static QName _AddNewVendorResponse_QNAME = new QName("http://api.service.books/", "addNewVendorResponse");
    private final static QName _DoesNotExist_QNAME = new QName("http://api.service.books/", "DoesNotExist");
    private final static QName _ReadAttachment_QNAME = new QName("http://api.service.books/", "readAttachment");
    private final static QName _GetVatTaxInfo_QNAME = new QName("http://api.service.books/", "getVatTaxInfo");
    private final static QName _GetSubscriptionsWithPlanResponse_QNAME = new QName("http://api.service.books/", "getSubscriptionsWithPlanResponse");
    private final static QName _ChangeUserPassword_QNAME = new QName("http://api.service.books/", "changeUserPassword");
    private final static QName _AddAttachmentResponse_QNAME = new QName("http://api.service.books/", "addAttachmentResponse");
    private final static QName _GetChangesets_QNAME = new QName("http://api.service.books/", "getChangesets");
    private final static QName _GetMyBusinesses_QNAME = new QName("http://api.service.books/", "getMyBusinesses");
    private final static QName _ClearTaxRate_QNAME = new QName("http://api.service.books/", "clearTaxRate");
    private final static QName _SaveCollaborationEvent_QNAME = new QName("http://api.service.books/", "saveCollaborationEvent");
    private final static QName _GetFuturePayAgreementResponse_QNAME = new QName("http://api.service.books/", "getFuturePayAgreementResponse");
    private final static QName _SaveTaxCodesResponse_QNAME = new QName("http://api.service.books/", "saveTaxCodesResponse");
    private final static QName _SetRolesResponse_QNAME = new QName("http://api.service.books/", "setRolesResponse");
    private final static QName _SaveTaxResponse_QNAME = new QName("http://api.service.books/", "saveTaxResponse");
    private final static QName _SaveTax_QNAME = new QName("http://api.service.books/", "saveTax");
    private final static QName _SetContractLinks_QNAME = new QName("http://api.service.books/", "setContractLinks");
    private final static QName _AppendToFileResponse_QNAME = new QName("http://api.service.books/", "appendToFileResponse");
    private final static QName _GetJournalEntriesResponse_QNAME = new QName("http://api.service.books/", "getJournalEntriesResponse");
    private final static QName _GetCountries_QNAME = new QName("http://api.service.books/", "getCountries");
    private final static QName _GetProvincesOrStates_QNAME = new QName("http://api.service.books/", "getProvincesOrStates");
    private final static QName _GetDefaultRegionResponse_QNAME = new QName("http://api.service.books/", "getDefaultRegionResponse");
    private final static QName _ReadAttachmentResponseData_QNAME = new QName("", "data");
    private final static QName _AppendToAttachmentArg2_QNAME = new QName("", "arg2");
    private final static QName _AddAttachmentBuffer_QNAME = new QName("", "buffer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.clarity.books.service.api
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddNewCustomerResponse }
     * 
     */
    public AddNewCustomerResponse createAddNewCustomerResponse() {
        return new AddNewCustomerResponse();
    }

    /**
     * Create an instance of {@link TestAutomaticMessage }
     * 
     */
    public TestAutomaticMessage createTestAutomaticMessage() {
        return new TestAutomaticMessage();
    }

    /**
     * Create an instance of {@link ReadAttachmentResponse }
     * 
     */
    public ReadAttachmentResponse createReadAttachmentResponse() {
        return new ReadAttachmentResponse();
    }

    /**
     * Create an instance of {@link DoesNotExist }
     * 
     */
    public DoesNotExist createDoesNotExist() {
        return new DoesNotExist();
    }

    /**
     * Create an instance of {@link CountSubscriptionsWithPlan }
     * 
     */
    public CountSubscriptionsWithPlan createCountSubscriptionsWithPlan() {
        return new CountSubscriptionsWithPlan();
    }

    /**
     * Create an instance of {@link GetLinkedContactsResponse }
     * 
     */
    public GetLinkedContactsResponse createGetLinkedContactsResponse() {
        return new GetLinkedContactsResponse();
    }

    /**
     * Create an instance of {@link GetContractResponse }
     * 
     */
    public GetContractResponse createGetContractResponse() {
        return new GetContractResponse();
    }

    /**
     * Create an instance of {@link AddRoleResponse }
     * 
     */
    public AddRoleResponse createAddRoleResponse() {
        return new AddRoleResponse();
    }

    /**
     * Create an instance of {@link GetFuturePayAgreementResponse }
     * 
     */
    public GetFuturePayAgreementResponse createGetFuturePayAgreementResponse() {
        return new GetFuturePayAgreementResponse();
    }

    /**
     * Create an instance of {@link GetSubscriptionsWithContact }
     * 
     */
    public GetSubscriptionsWithContact createGetSubscriptionsWithContact() {
        return new GetSubscriptionsWithContact();
    }

    /**
     * Create an instance of {@link SaveTax }
     * 
     */
    public SaveTax createSaveTax() {
        return new SaveTax();
    }

    /**
     * Create an instance of {@link CountRecordsForContactResponse }
     * 
     */
    public CountRecordsForContactResponse createCountRecordsForContactResponse() {
        return new CountRecordsForContactResponse();
    }

    /**
     * Create an instance of {@link EncodeAuthTokenResponse }
     * 
     */
    public EncodeAuthTokenResponse createEncodeAuthTokenResponse() {
        return new EncodeAuthTokenResponse();
    }

    /**
     * Create an instance of {@link AddTaxResponse }
     * 
     */
    public AddTaxResponse createAddTaxResponse() {
        return new AddTaxResponse();
    }

    /**
     * Create an instance of {@link GetRecordResponse }
     * 
     */
    public GetRecordResponse createGetRecordResponse() {
        return new GetRecordResponse();
    }

    /**
     * Create an instance of {@link CreateRestrictedToken }
     * 
     */
    public CreateRestrictedToken createCreateRestrictedToken() {
        return new CreateRestrictedToken();
    }

    /**
     * Create an instance of {@link GetVatTaxInfoResponse }
     * 
     */
    public GetVatTaxInfoResponse createGetVatTaxInfoResponse() {
        return new GetVatTaxInfoResponse();
    }

    /**
     * Create an instance of {@link CanadianTaxInfo }
     * 
     */
    public CanadianTaxInfo createCanadianTaxInfo() {
        return new CanadianTaxInfo();
    }

    /**
     * Create an instance of {@link GetCanadianTaxInfo }
     * 
     */
    public GetCanadianTaxInfo createGetCanadianTaxInfo() {
        return new GetCanadianTaxInfo();
    }

    /**
     * Create an instance of {@link FuturePayAgreementInfo }
     * 
     */
    public FuturePayAgreementInfo createFuturePayAgreementInfo() {
        return new FuturePayAgreementInfo();
    }

    /**
     * Create an instance of {@link SubscriptionPlanInfo }
     * 
     */
    public SubscriptionPlanInfo createSubscriptionPlanInfo() {
        return new SubscriptionPlanInfo();
    }

    /**
     * Create an instance of {@link GetSubscriptionPlansResponse }
     * 
     */
    public GetSubscriptionPlansResponse createGetSubscriptionPlansResponse() {
        return new GetSubscriptionPlansResponse();
    }

    /**
     * Create an instance of {@link GetItem }
     * 
     */
    public GetItem createGetItem() {
        return new GetItem();
    }

    /**
     * Create an instance of {@link TaxEntryInfo }
     * 
     */
    public TaxEntryInfo createTaxEntryInfo() {
        return new TaxEntryInfo();
    }

    /**
     * Create an instance of {@link GetItemResponse }
     * 
     */
    public GetItemResponse createGetItemResponse() {
        return new GetItemResponse();
    }

    /**
     * Create an instance of {@link SaveBusinessResponse }
     * 
     */
    public SaveBusinessResponse createSaveBusinessResponse() {
        return new SaveBusinessResponse();
    }

    /**
     * Create an instance of {@link ClearTaxRate }
     * 
     */
    public ClearTaxRate createClearTaxRate() {
        return new ClearTaxRate();
    }

    /**
     * Create an instance of {@link AddTax }
     * 
     */
    public AddTax createAddTax() {
        return new AddTax();
    }

    /**
     * Create an instance of {@link GetJournalEntries }
     * 
     */
    public GetJournalEntries createGetJournalEntries() {
        return new GetJournalEntries();
    }

    /**
     * Create an instance of {@link GetWorldPayResponse }
     * 
     */
    public GetWorldPayResponse createGetWorldPayResponse() {
        return new GetWorldPayResponse();
    }

    /**
     * Create an instance of {@link GetCurrenciesResponse }
     * 
     */
    public GetCurrenciesResponse createGetCurrenciesResponse() {
        return new GetCurrenciesResponse();
    }

    /**
     * Create an instance of {@link CountSubscriptionsWithPlanResponse }
     * 
     */
    public CountSubscriptionsWithPlanResponse createCountSubscriptionsWithPlanResponse() {
        return new CountSubscriptionsWithPlanResponse();
    }

    /**
     * Create an instance of {@link BasicTaxInfo }
     * 
     */
    public BasicTaxInfo createBasicTaxInfo() {
        return new BasicTaxInfo();
    }

    /**
     * Create an instance of {@link AddAttachmentResponse }
     * 
     */
    public AddAttachmentResponse createAddAttachmentResponse() {
        return new AddAttachmentResponse();
    }

    /**
     * Create an instance of {@link GetUnclearedTransactionsResponse }
     * 
     */
    public GetUnclearedTransactionsResponse createGetUnclearedTransactionsResponse() {
        return new GetUnclearedTransactionsResponse();
    }

    /**
     * Create an instance of {@link GetAllDistrictsOrCountiesResponse }
     * 
     */
    public GetAllDistrictsOrCountiesResponse createGetAllDistrictsOrCountiesResponse() {
        return new GetAllDistrictsOrCountiesResponse();
    }

    /**
     * Create an instance of {@link SaveAutomaticMessage }
     * 
     */
    public SaveAutomaticMessage createSaveAutomaticMessage() {
        return new SaveAutomaticMessage();
    }

    /**
     * Create an instance of {@link GetCountriesResponse }
     * 
     */
    public GetCountriesResponse createGetCountriesResponse() {
        return new GetCountriesResponse();
    }

    /**
     * Create an instance of {@link FindRegionResponse }
     * 
     */
    public FindRegionResponse createFindRegionResponse() {
        return new FindRegionResponse();
    }

    /**
     * Create an instance of {@link GetTax }
     * 
     */
    public GetTax createGetTax() {
        return new GetTax();
    }

    /**
     * Create an instance of {@link SetTaxRate }
     * 
     */
    public SetTaxRate createSetTaxRate() {
        return new SetTaxRate();
    }

    /**
     * Create an instance of {@link GetSubscriptionsWithPlan }
     * 
     */
    public GetSubscriptionsWithPlan createGetSubscriptionsWithPlan() {
        return new GetSubscriptionsWithPlan();
    }

    /**
     * Create an instance of {@link CompletePasswordReset }
     * 
     */
    public CompletePasswordReset createCompletePasswordReset() {
        return new CompletePasswordReset();
    }

    /**
     * Create an instance of {@link AddNewVendorResponse }
     * 
     */
    public AddNewVendorResponse createAddNewVendorResponse() {
        return new AddNewVendorResponse();
    }

    /**
     * Create an instance of {@link SaveAccountResponse }
     * 
     */
    public SaveAccountResponse createSaveAccountResponse() {
        return new SaveAccountResponse();
    }

    /**
     * Create an instance of {@link GetBalancesResponse }
     * 
     */
    public GetBalancesResponse createGetBalancesResponse() {
        return new GetBalancesResponse();
    }

    /**
     * Create an instance of {@link SendTestEmailResponse }
     * 
     */
    public SendTestEmailResponse createSendTestEmailResponse() {
        return new SendTestEmailResponse();
    }

    /**
     * Create an instance of {@link GetRoles }
     * 
     */
    public GetRoles createGetRoles() {
        return new GetRoles();
    }

    /**
     * Create an instance of {@link SaveSubscriptionPlanResponse }
     * 
     */
    public SaveSubscriptionPlanResponse createSaveSubscriptionPlanResponse() {
        return new SaveSubscriptionPlanResponse();
    }

    /**
     * Create an instance of {@link GetJournalEntriesResponse }
     * 
     */
    public GetJournalEntriesResponse createGetJournalEntriesResponse() {
        return new GetJournalEntriesResponse();
    }

    /**
     * Create an instance of {@link GetEmailSettings }
     * 
     */
    public GetEmailSettings createGetEmailSettings() {
        return new GetEmailSettings();
    }

    /**
     * Create an instance of {@link CompleteSignUp }
     * 
     */
    public CompleteSignUp createCompleteSignUp() {
        return new CompleteSignUp();
    }

    /**
     * Create an instance of {@link NotAllowed }
     * 
     */
    public NotAllowed createNotAllowed() {
        return new NotAllowed();
    }

    /**
     * Create an instance of {@link SkipTrialResponse }
     * 
     */
    public SkipTrialResponse createSkipTrialResponse() {
        return new SkipTrialResponse();
    }

    /**
     * Create an instance of {@link GetContactsOfTypeResponse }
     * 
     */
    public GetContactsOfTypeResponse createGetContactsOfTypeResponse() {
        return new GetContactsOfTypeResponse();
    }

    /**
     * Create an instance of {@link SkipTrial }
     * 
     */
    public SkipTrial createSkipTrial() {
        return new SkipTrial();
    }

    /**
     * Create an instance of {@link GetTaxCodes }
     * 
     */
    public GetTaxCodes createGetTaxCodes() {
        return new GetTaxCodes();
    }

    /**
     * Create an instance of {@link InternalError }
     * 
     */
    public InternalError createInternalError() {
        return new InternalError();
    }

    /**
     * Create an instance of {@link CountRecordsForContact }
     * 
     */
    public CountRecordsForContact createCountRecordsForContact() {
        return new CountRecordsForContact();
    }

    /**
     * Create an instance of {@link GetChangesetsResponse }
     * 
     */
    public GetChangesetsResponse createGetChangesetsResponse() {
        return new GetChangesetsResponse();
    }

    /**
     * Create an instance of {@link SubscriptionPaymentMethodInfo }
     * 
     */
    public SubscriptionPaymentMethodInfo createSubscriptionPaymentMethodInfo() {
        return new SubscriptionPaymentMethodInfo();
    }

    /**
     * Create an instance of {@link SaveBusiness }
     * 
     */
    public SaveBusiness createSaveBusiness() {
        return new SaveBusiness();
    }

    /**
     * Create an instance of {@link AppendToFile }
     * 
     */
    public AppendToFile createAppendToFile() {
        return new AppendToFile();
    }

    /**
     * Create an instance of {@link GetBasicTaxInfo }
     * 
     */
    public GetBasicTaxInfo createGetBasicTaxInfo() {
        return new GetBasicTaxInfo();
    }

    /**
     * Create an instance of {@link GetBankReconciliation }
     * 
     */
    public GetBankReconciliation createGetBankReconciliation() {
        return new GetBankReconciliation();
    }

    /**
     * Create an instance of {@link SaveSubscription }
     * 
     */
    public SaveSubscription createSaveSubscription() {
        return new SaveSubscription();
    }

    /**
     * Create an instance of {@link GetMyUser }
     * 
     */
    public GetMyUser createGetMyUser() {
        return new GetMyUser();
    }

    /**
     * Create an instance of {@link RecordInfo.Adjustments }
     * 
     */
    public RecordInfo.Adjustments createRecordInfoAdjustments() {
        return new RecordInfo.Adjustments();
    }

    /**
     * Create an instance of {@link GetAccountByName }
     * 
     */
    public GetAccountByName createGetAccountByName() {
        return new GetAccountByName();
    }

    /**
     * Create an instance of {@link SubmitFeedback }
     * 
     */
    public SubmitFeedback createSubmitFeedback() {
        return new SubmitFeedback();
    }

    /**
     * Create an instance of {@link SaveTaxCodesResponse }
     * 
     */
    public SaveTaxCodesResponse createSaveTaxCodesResponse() {
        return new SaveTaxCodesResponse();
    }

    /**
     * Create an instance of {@link GetPublicSetting }
     * 
     */
    public GetPublicSetting createGetPublicSetting() {
        return new GetPublicSetting();
    }

    /**
     * Create an instance of {@link GetRecord }
     * 
     */
    public GetRecord createGetRecord() {
        return new GetRecord();
    }

    /**
     * Create an instance of {@link GetLedgerLengthResponse }
     * 
     */
    public GetLedgerLengthResponse createGetLedgerLengthResponse() {
        return new GetLedgerLengthResponse();
    }

    /**
     * Create an instance of {@link GetProvincesOrStatesResponse }
     * 
     */
    public GetProvincesOrStatesResponse createGetProvincesOrStatesResponse() {
        return new GetProvincesOrStatesResponse();
    }

    /**
     * Create an instance of {@link GetRecords }
     * 
     */
    public GetRecords createGetRecords() {
        return new GetRecords();
    }

    /**
     * Create an instance of {@link BadArgument }
     * 
     */
    public BadArgument createBadArgument() {
        return new BadArgument();
    }

    /**
     * Create an instance of {@link GetAutomaticMessages }
     * 
     */
    public GetAutomaticMessages createGetAutomaticMessages() {
        return new GetAutomaticMessages();
    }

    /**
     * Create an instance of {@link SaveEmailSettingsResponse }
     * 
     */
    public SaveEmailSettingsResponse createSaveEmailSettingsResponse() {
        return new SaveEmailSettingsResponse();
    }

    /**
     * Create an instance of {@link GetAttachment }
     * 
     */
    public GetAttachment createGetAttachment() {
        return new GetAttachment();
    }

    /**
     * Create an instance of {@link GetBalancesAtDateResponse }
     * 
     */
    public GetBalancesAtDateResponse createGetBalancesAtDateResponse() {
        return new GetBalancesAtDateResponse();
    }

    /**
     * Create an instance of {@link GetAllProvincesOrStatesResponse }
     * 
     */
    public GetAllProvincesOrStatesResponse createGetAllProvincesOrStatesResponse() {
        return new GetAllProvincesOrStatesResponse();
    }

    /**
     * Create an instance of {@link GetSettingResponse }
     * 
     */
    public GetSettingResponse createGetSettingResponse() {
        return new GetSettingResponse();
    }

    /**
     * Create an instance of {@link GetFile }
     * 
     */
    public GetFile createGetFile() {
        return new GetFile();
    }

    /**
     * Create an instance of {@link ReadAttachment }
     * 
     */
    public ReadAttachment createReadAttachment() {
        return new ReadAttachment();
    }

    /**
     * Create an instance of {@link GetRegionResponse }
     * 
     */
    public GetRegionResponse createGetRegionResponse() {
        return new GetRegionResponse();
    }

    /**
     * Create an instance of {@link CountBankReconciliations }
     * 
     */
    public CountBankReconciliations createCountBankReconciliations() {
        return new CountBankReconciliations();
    }

    /**
     * Create an instance of {@link GetRecordsResponse }
     * 
     */
    public GetRecordsResponse createGetRecordsResponse() {
        return new GetRecordsResponse();
    }

    /**
     * Create an instance of {@link RegionInfo }
     * 
     */
    public RegionInfo createRegionInfo() {
        return new RegionInfo();
    }

    /**
     * Create an instance of {@link CompleteFileResponse }
     * 
     */
    public CompleteFileResponse createCompleteFileResponse() {
        return new CompleteFileResponse();
    }

    /**
     * Create an instance of {@link SaveContactResponse }
     * 
     */
    public SaveContactResponse createSaveContactResponse() {
        return new SaveContactResponse();
    }

    /**
     * Create an instance of {@link GetAccountResponse }
     * 
     */
    public GetAccountResponse createGetAccountResponse() {
        return new GetAccountResponse();
    }

    /**
     * Create an instance of {@link RequestEmailVerificationCodeResponse }
     * 
     */
    public RequestEmailVerificationCodeResponse createRequestEmailVerificationCodeResponse() {
        return new RequestEmailVerificationCodeResponse();
    }

    /**
     * Create an instance of {@link DebitFuturePayAgreementResponse }
     * 
     */
    public DebitFuturePayAgreementResponse createDebitFuturePayAgreementResponse() {
        return new DebitFuturePayAgreementResponse();
    }

    /**
     * Create an instance of {@link DecodeAuthTokenResponse }
     * 
     */
    public DecodeAuthTokenResponse createDecodeAuthTokenResponse() {
        return new DecodeAuthTokenResponse();
    }

    /**
     * Create an instance of {@link PaymentAllocationInfo }
     * 
     */
    public PaymentAllocationInfo createPaymentAllocationInfo() {
        return new PaymentAllocationInfo();
    }

    /**
     * Create an instance of {@link GetUserByEmail }
     * 
     */
    public GetUserByEmail createGetUserByEmail() {
        return new GetUserByEmail();
    }

    /**
     * Create an instance of {@link GetPaymentSetupInfoForBusiness }
     * 
     */
    public GetPaymentSetupInfoForBusiness createGetPaymentSetupInfoForBusiness() {
        return new GetPaymentSetupInfoForBusiness();
    }

    /**
     * Create an instance of {@link GetAutomaticMessagesResponse }
     * 
     */
    public GetAutomaticMessagesResponse createGetAutomaticMessagesResponse() {
        return new GetAutomaticMessagesResponse();
    }

    /**
     * Create an instance of {@link AlreadyActivated }
     * 
     */
    public AlreadyActivated createAlreadyActivated() {
        return new AlreadyActivated();
    }

    /**
     * Create an instance of {@link SaveRecord }
     * 
     */
    public SaveRecord createSaveRecord() {
        return new SaveRecord();
    }

    /**
     * Create an instance of {@link GetPaymentSetupInfoForBusinessResponse }
     * 
     */
    public GetPaymentSetupInfoForBusinessResponse createGetPaymentSetupInfoForBusinessResponse() {
        return new GetPaymentSetupInfoForBusinessResponse();
    }

    /**
     * Create an instance of {@link ChangesetInfo }
     * 
     */
    public ChangesetInfo createChangesetInfo() {
        return new ChangesetInfo();
    }

    /**
     * Create an instance of {@link GetMyBusinesses }
     * 
     */
    public GetMyBusinesses createGetMyBusinesses() {
        return new GetMyBusinesses();
    }

    /**
     * Create an instance of {@link GetAvailablePaymentMethods }
     * 
     */
    public GetAvailablePaymentMethods createGetAvailablePaymentMethods() {
        return new GetAvailablePaymentMethods();
    }

    /**
     * Create an instance of {@link AddUser }
     * 
     */
    public AddUser createAddUser() {
        return new AddUser();
    }

    /**
     * Create an instance of {@link GetContactResponse }
     * 
     */
    public GetContactResponse createGetContactResponse() {
        return new GetContactResponse();
    }

    /**
     * Create an instance of {@link SaveTaxResponse }
     * 
     */
    public SaveTaxResponse createSaveTaxResponse() {
        return new SaveTaxResponse();
    }

    /**
     * Create an instance of {@link AddRole }
     * 
     */
    public AddRole createAddRole() {
        return new AddRole();
    }

    /**
     * Create an instance of {@link ConcurrentChangeException }
     * 
     */
    public ConcurrentChangeException createConcurrentChangeException() {
        return new ConcurrentChangeException();
    }

    /**
     * Create an instance of {@link ChangeUserPassword }
     * 
     */
    public ChangeUserPassword createChangeUserPassword() {
        return new ChangeUserPassword();
    }

    /**
     * Create an instance of {@link GetLinkedContacts }
     * 
     */
    public GetLinkedContacts createGetLinkedContacts() {
        return new GetLinkedContacts();
    }

    /**
     * Create an instance of {@link AlreadyExists }
     * 
     */
    public AlreadyExists createAlreadyExists() {
        return new AlreadyExists();
    }

    /**
     * Create an instance of {@link ClearTaxRateResponse }
     * 
     */
    public ClearTaxRateResponse createClearTaxRateResponse() {
        return new ClearTaxRateResponse();
    }

    /**
     * Create an instance of {@link GetTimeZonesResponse }
     * 
     */
    public GetTimeZonesResponse createGetTimeZonesResponse() {
        return new GetTimeZonesResponse();
    }

    /**
     * Create an instance of {@link AttachmentInfo }
     * 
     */
    public AttachmentInfo createAttachmentInfo() {
        return new AttachmentInfo();
    }

    /**
     * Create an instance of {@link GetPublicSettingResponse }
     * 
     */
    public GetPublicSettingResponse createGetPublicSettingResponse() {
        return new GetPublicSettingResponse();
    }

    /**
     * Create an instance of {@link CollectionInfo }
     * 
     */
    public CollectionInfo createCollectionInfo() {
        return new CollectionInfo();
    }

    /**
     * Create an instance of {@link CountBankReconciliationsResponse }
     * 
     */
    public CountBankReconciliationsResponse createCountBankReconciliationsResponse() {
        return new CountBankReconciliationsResponse();
    }

    /**
     * Create an instance of {@link GetTaxRate }
     * 
     */
    public GetTaxRate createGetTaxRate() {
        return new GetTaxRate();
    }

    /**
     * Create an instance of {@link GetSubscriptionPlan }
     * 
     */
    public GetSubscriptionPlan createGetSubscriptionPlan() {
        return new GetSubscriptionPlan();
    }

    /**
     * Create an instance of {@link StoreFileResponse }
     * 
     */
    public StoreFileResponse createStoreFileResponse() {
        return new StoreFileResponse();
    }

    /**
     * Create an instance of {@link CountChangesets }
     * 
     */
    public CountChangesets createCountChangesets() {
        return new CountChangesets();
    }

    /**
     * Create an instance of {@link CompletePasswordResetResponse }
     * 
     */
    public CompletePasswordResetResponse createCompletePasswordResetResponse() {
        return new CompletePasswordResetResponse();
    }

    /**
     * Create an instance of {@link GetTimeInZoneResponse }
     * 
     */
    public GetTimeInZoneResponse createGetTimeInZoneResponse() {
        return new GetTimeInZoneResponse();
    }

    /**
     * Create an instance of {@link SignUpResponse }
     * 
     */
    public SignUpResponse createSignUpResponse() {
        return new SignUpResponse();
    }

    /**
     * Create an instance of {@link CompleteFile }
     * 
     */
    public CompleteFile createCompleteFile() {
        return new CompleteFile();
    }

    /**
     * Create an instance of {@link GetAutomaticMessage }
     * 
     */
    public GetAutomaticMessage createGetAutomaticMessage() {
        return new GetAutomaticMessage();
    }

    /**
     * Create an instance of {@link PredictAccountNumber }
     * 
     */
    public PredictAccountNumber createPredictAccountNumber() {
        return new PredictAccountNumber();
    }

    /**
     * Create an instance of {@link DoLogout }
     * 
     */
    public DoLogout createDoLogout() {
        return new DoLogout();
    }

    /**
     * Create an instance of {@link RetryContractPayment }
     * 
     */
    public RetryContractPayment createRetryContractPayment() {
        return new RetryContractPayment();
    }

    /**
     * Create an instance of {@link GetContact }
     * 
     */
    public GetContact createGetContact() {
        return new GetContact();
    }

    /**
     * Create an instance of {@link RecordInfo.PaymentAllocationsIn }
     * 
     */
    public RecordInfo.PaymentAllocationsIn createRecordInfoPaymentAllocationsIn() {
        return new RecordInfo.PaymentAllocationsIn();
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link CountJournalEntriesResponse }
     * 
     */
    public CountJournalEntriesResponse createCountJournalEntriesResponse() {
        return new CountJournalEntriesResponse();
    }

    /**
     * Create an instance of {@link AddBusiness }
     * 
     */
    public AddBusiness createAddBusiness() {
        return new AddBusiness();
    }

    /**
     * Create an instance of {@link SaveRecordResponse }
     * 
     */
    public SaveRecordResponse createSaveRecordResponse() {
        return new SaveRecordResponse();
    }

    /**
     * Create an instance of {@link AdjustmentInfo }
     * 
     */
    public AdjustmentInfo createAdjustmentInfo() {
        return new AdjustmentInfo();
    }

    /**
     * Create an instance of {@link AppendToAttachment }
     * 
     */
    public AppendToAttachment createAppendToAttachment() {
        return new AppendToAttachment();
    }

    /**
     * Create an instance of {@link SaveWorldPay }
     * 
     */
    public SaveWorldPay createSaveWorldPay() {
        return new SaveWorldPay();
    }

    /**
     * Create an instance of {@link GetAllItemsResponse }
     * 
     */
    public GetAllItemsResponse createGetAllItemsResponse() {
        return new GetAllItemsResponse();
    }

    /**
     * Create an instance of {@link GetTaxesResponse }
     * 
     */
    public GetTaxesResponse createGetTaxesResponse() {
        return new GetTaxesResponse();
    }

    /**
     * Create an instance of {@link SaveUserResponse }
     * 
     */
    public SaveUserResponse createSaveUserResponse() {
        return new SaveUserResponse();
    }

    /**
     * Create an instance of {@link CompleteAttachmentResponse }
     * 
     */
    public CompleteAttachmentResponse createCompleteAttachmentResponse() {
        return new CompleteAttachmentResponse();
    }

    /**
     * Create an instance of {@link PaymentInfo }
     * 
     */
    public PaymentInfo createPaymentInfo() {
        return new PaymentInfo();
    }

    /**
     * Create an instance of {@link GetDistrictsOrCountiesResponse }
     * 
     */
    public GetDistrictsOrCountiesResponse createGetDistrictsOrCountiesResponse() {
        return new GetDistrictsOrCountiesResponse();
    }

    /**
     * Create an instance of {@link ItemInfo }
     * 
     */
    public ItemInfo createItemInfo() {
        return new ItemInfo();
    }

    /**
     * Create an instance of {@link SaveBasicTaxInfo }
     * 
     */
    public SaveBasicTaxInfo createSaveBasicTaxInfo() {
        return new SaveBasicTaxInfo();
    }

    /**
     * Create an instance of {@link GetBalanceAtDateResponse }
     * 
     */
    public GetBalanceAtDateResponse createGetBalanceAtDateResponse() {
        return new GetBalanceAtDateResponse();
    }

    /**
     * Create an instance of {@link ExchangeRateInfo }
     * 
     */
    public ExchangeRateInfo createExchangeRateInfo() {
        return new ExchangeRateInfo();
    }

    /**
     * Create an instance of {@link GetRecordsOfTypeResponse }
     * 
     */
    public GetRecordsOfTypeResponse createGetRecordsOfTypeResponse() {
        return new GetRecordsOfTypeResponse();
    }

    /**
     * Create an instance of {@link DoLoginResponse }
     * 
     */
    public DoLoginResponse createDoLoginResponse() {
        return new DoLoginResponse();
    }

    /**
     * Create an instance of {@link GetWorldPay }
     * 
     */
    public GetWorldPay createGetWorldPay() {
        return new GetWorldPay();
    }

    /**
     * Create an instance of {@link GetOverpaidRecords }
     * 
     */
    public GetOverpaidRecords createGetOverpaidRecords() {
        return new GetOverpaidRecords();
    }

    /**
     * Create an instance of {@link GetLedger }
     * 
     */
    public GetLedger createGetLedger() {
        return new GetLedger();
    }

    /**
     * Create an instance of {@link GetAccountByNameResponse }
     * 
     */
    public GetAccountByNameResponse createGetAccountByNameResponse() {
        return new GetAccountByNameResponse();
    }

    /**
     * Create an instance of {@link DecodeAuthToken }
     * 
     */
    public DecodeAuthToken createDecodeAuthToken() {
        return new DecodeAuthToken();
    }

    /**
     * Create an instance of {@link GetItems }
     * 
     */
    public GetItems createGetItems() {
        return new GetItems();
    }

    /**
     * Create an instance of {@link ItemCategoryInfo }
     * 
     */
    public ItemCategoryInfo createItemCategoryInfo() {
        return new ItemCategoryInfo();
    }

    /**
     * Create an instance of {@link NotActivated }
     * 
     */
    public NotActivated createNotActivated() {
        return new NotActivated();
    }

    /**
     * Create an instance of {@link GetAttachmentResponse }
     * 
     */
    public GetAttachmentResponse createGetAttachmentResponse() {
        return new GetAttachmentResponse();
    }

    /**
     * Create an instance of {@link SaveItem }
     * 
     */
    public SaveItem createSaveItem() {
        return new SaveItem();
    }

    /**
     * Create an instance of {@link BankReconciliationInfo }
     * 
     */
    public BankReconciliationInfo createBankReconciliationInfo() {
        return new BankReconciliationInfo();
    }

    /**
     * Create an instance of {@link GetItemSubcategory }
     * 
     */
    public GetItemSubcategory createGetItemSubcategory() {
        return new GetItemSubcategory();
    }

    /**
     * Create an instance of {@link GetCurrencies }
     * 
     */
    public GetCurrencies createGetCurrencies() {
        return new GetCurrencies();
    }

    /**
     * Create an instance of {@link GetUnpaidRecords }
     * 
     */
    public GetUnpaidRecords createGetUnpaidRecords() {
        return new GetUnpaidRecords();
    }

    /**
     * Create an instance of {@link AddNewVendor }
     * 
     */
    public AddNewVendor createAddNewVendor() {
        return new AddNewVendor();
    }

    /**
     * Create an instance of {@link GetCustomers }
     * 
     */
    public GetCustomers createGetCustomers() {
        return new GetCustomers();
    }

    /**
     * Create an instance of {@link SaveCollaborationEvent }
     * 
     */
    public SaveCollaborationEvent createSaveCollaborationEvent() {
        return new SaveCollaborationEvent();
    }

    /**
     * Create an instance of {@link GetContactsResponse }
     * 
     */
    public GetContactsResponse createGetContactsResponse() {
        return new GetContactsResponse();
    }

    /**
     * Create an instance of {@link CountRecords }
     * 
     */
    public CountRecords createCountRecords() {
        return new CountRecords();
    }

    /**
     * Create an instance of {@link GetTaxResponse }
     * 
     */
    public GetTaxResponse createGetTaxResponse() {
        return new GetTaxResponse();
    }

    /**
     * Create an instance of {@link BalanceInfo }
     * 
     */
    public BalanceInfo createBalanceInfo() {
        return new BalanceInfo();
    }

    /**
     * Create an instance of {@link GetSubscriptionResponse }
     * 
     */
    public GetSubscriptionResponse createGetSubscriptionResponse() {
        return new GetSubscriptionResponse();
    }

    /**
     * Create an instance of {@link SetContractLinksResponse }
     * 
     */
    public SetContractLinksResponse createSetContractLinksResponse() {
        return new SetContractLinksResponse();
    }

    /**
     * Create an instance of {@link GetAllDistrictsOrCounties }
     * 
     */
    public GetAllDistrictsOrCounties createGetAllDistrictsOrCounties() {
        return new GetAllDistrictsOrCounties();
    }

    /**
     * Create an instance of {@link GetStatementOpeningBalancesResponse }
     * 
     */
    public GetStatementOpeningBalancesResponse createGetStatementOpeningBalancesResponse() {
        return new GetStatementOpeningBalancesResponse();
    }

    /**
     * Create an instance of {@link GetBankReconciliationResponse }
     * 
     */
    public GetBankReconciliationResponse createGetBankReconciliationResponse() {
        return new GetBankReconciliationResponse();
    }

    /**
     * Create an instance of {@link GetCollaborationContextResponse }
     * 
     */
    public GetCollaborationContextResponse createGetCollaborationContextResponse() {
        return new GetCollaborationContextResponse();
    }

    /**
     * Create an instance of {@link GetCustomersResponse }
     * 
     */
    public GetCustomersResponse createGetCustomersResponse() {
        return new GetCustomersResponse();
    }

    /**
     * Create an instance of {@link GetLedgerResponse }
     * 
     */
    public GetLedgerResponse createGetLedgerResponse() {
        return new GetLedgerResponse();
    }

    /**
     * Create an instance of {@link GetStatementOpeningBalances }
     * 
     */
    public GetStatementOpeningBalances createGetStatementOpeningBalances() {
        return new GetStatementOpeningBalances();
    }

    /**
     * Create an instance of {@link PredictRecordNumber }
     * 
     */
    public PredictRecordNumber createPredictRecordNumber() {
        return new PredictRecordNumber();
    }

    /**
     * Create an instance of {@link GetUnpaidRecordsResponse }
     * 
     */
    public GetUnpaidRecordsResponse createGetUnpaidRecordsResponse() {
        return new GetUnpaidRecordsResponse();
    }

    /**
     * Create an instance of {@link SaveBasicTaxInfoResponse }
     * 
     */
    public SaveBasicTaxInfoResponse createSaveBasicTaxInfoResponse() {
        return new SaveBasicTaxInfoResponse();
    }

    /**
     * Create an instance of {@link GetUnclearedTransactions }
     * 
     */
    public GetUnclearedTransactions createGetUnclearedTransactions() {
        return new GetUnclearedTransactions();
    }

    /**
     * Create an instance of {@link GetSiteTitleResponse }
     * 
     */
    public GetSiteTitleResponse createGetSiteTitleResponse() {
        return new GetSiteTitleResponse();
    }

    /**
     * Create an instance of {@link ContactInformationInfo }
     * 
     */
    public ContactInformationInfo createContactInformationInfo() {
        return new ContactInformationInfo();
    }

    /**
     * Create an instance of {@link ValidationFailed }
     * 
     */
    public ValidationFailed createValidationFailed() {
        return new ValidationFailed();
    }

    /**
     * Create an instance of {@link GetSubscriptionsWithPlanResponse }
     * 
     */
    public GetSubscriptionsWithPlanResponse createGetSubscriptionsWithPlanResponse() {
        return new GetSubscriptionsWithPlanResponse();
    }

    /**
     * Create an instance of {@link GetDistrictsOrCounties }
     * 
     */
    public GetDistrictsOrCounties createGetDistrictsOrCounties() {
        return new GetDistrictsOrCounties();
    }

    /**
     * Create an instance of {@link PaymentSetupInfo }
     * 
     */
    public PaymentSetupInfo createPaymentSetupInfo() {
        return new PaymentSetupInfo();
    }

    /**
     * Create an instance of {@link GetCurrency }
     * 
     */
    public GetCurrency createGetCurrency() {
        return new GetCurrency();
    }

    /**
     * Create an instance of {@link SaveItemResponse }
     * 
     */
    public SaveItemResponse createSaveItemResponse() {
        return new SaveItemResponse();
    }

    /**
     * Create an instance of {@link RequestPasswordResetResponse }
     * 
     */
    public RequestPasswordResetResponse createRequestPasswordResetResponse() {
        return new RequestPasswordResetResponse();
    }

    /**
     * Create an instance of {@link GetChangesets }
     * 
     */
    public GetChangesets createGetChangesets() {
        return new GetChangesets();
    }

    /**
     * Create an instance of {@link GetBankReconciliations }
     * 
     */
    public GetBankReconciliations createGetBankReconciliations() {
        return new GetBankReconciliations();
    }

    /**
     * Create an instance of {@link GetItemsResponse }
     * 
     */
    public GetItemsResponse createGetItemsResponse() {
        return new GetItemsResponse();
    }

    /**
     * Create an instance of {@link AuthToken }
     * 
     */
    public AuthToken createAuthToken() {
        return new AuthToken();
    }

    /**
     * Create an instance of {@link SetContractLinks }
     * 
     */
    public SetContractLinks createSetContractLinks() {
        return new SetContractLinks();
    }

    /**
     * Create an instance of {@link GetAllProvincesOrStates }
     * 
     */
    public GetAllProvincesOrStates createGetAllProvincesOrStates() {
        return new GetAllProvincesOrStates();
    }

    /**
     * Create an instance of {@link SaveUser }
     * 
     */
    public SaveUser createSaveUser() {
        return new SaveUser();
    }

    /**
     * Create an instance of {@link GetRegion }
     * 
     */
    public GetRegion createGetRegion() {
        return new GetRegion();
    }

    /**
     * Create an instance of {@link GetExchangeRateResponse }
     * 
     */
    public GetExchangeRateResponse createGetExchangeRateResponse() {
        return new GetExchangeRateResponse();
    }

    /**
     * Create an instance of {@link DebitFuturePayAgreement }
     * 
     */
    public DebitFuturePayAgreement createDebitFuturePayAgreement() {
        return new DebitFuturePayAgreement();
    }

    /**
     * Create an instance of {@link SaveWorldPayResponse }
     * 
     */
    public SaveWorldPayResponse createSaveWorldPayResponse() {
        return new SaveWorldPayResponse();
    }

    /**
     * Create an instance of {@link ReadFileResponse }
     * 
     */
    public ReadFileResponse createReadFileResponse() {
        return new ReadFileResponse();
    }

    /**
     * Create an instance of {@link GetFuturePaySetupUrl }
     * 
     */
    public GetFuturePaySetupUrl createGetFuturePaySetupUrl() {
        return new GetFuturePaySetupUrl();
    }

    /**
     * Create an instance of {@link GetTimeZones }
     * 
     */
    public GetTimeZones createGetTimeZones() {
        return new GetTimeZones();
    }

    /**
     * Create an instance of {@link GetSubcategories }
     * 
     */
    public GetSubcategories createGetSubcategories() {
        return new GetSubcategories();
    }

    /**
     * Create an instance of {@link SetContractPlan }
     * 
     */
    public SetContractPlan createSetContractPlan() {
        return new SetContractPlan();
    }

    /**
     * Create an instance of {@link GetBusiness }
     * 
     */
    public GetBusiness createGetBusiness() {
        return new GetBusiness();
    }

    /**
     * Create an instance of {@link SaveAccount }
     * 
     */
    public SaveAccount createSaveAccount() {
        return new SaveAccount();
    }

    /**
     * Create an instance of {@link GetSetting }
     * 
     */
    public GetSetting createGetSetting() {
        return new GetSetting();
    }

    /**
     * Create an instance of {@link BusinessInfo }
     * 
     */
    public BusinessInfo createBusinessInfo() {
        return new BusinessInfo();
    }

    /**
     * Create an instance of {@link PredictRecordNumberResponse }
     * 
     */
    public PredictRecordNumberResponse createPredictRecordNumberResponse() {
        return new PredictRecordNumberResponse();
    }

    /**
     * Create an instance of {@link SaveAutomaticMessageResponse }
     * 
     */
    public SaveAutomaticMessageResponse createSaveAutomaticMessageResponse() {
        return new SaveAutomaticMessageResponse();
    }

    /**
     * Create an instance of {@link GetDefaultRegion }
     * 
     */
    public GetDefaultRegion createGetDefaultRegion() {
        return new GetDefaultRegion();
    }

    /**
     * Create an instance of {@link RequestEmailVerificationCode }
     * 
     */
    public RequestEmailVerificationCode createRequestEmailVerificationCode() {
        return new RequestEmailVerificationCode();
    }

    /**
     * Create an instance of {@link RequestPasswordReset }
     * 
     */
    public RequestPasswordReset createRequestPasswordReset() {
        return new RequestPasswordReset();
    }

    /**
     * Create an instance of {@link GetVendors }
     * 
     */
    public GetVendors createGetVendors() {
        return new GetVendors();
    }

    /**
     * Create an instance of {@link GetHomeCurrency }
     * 
     */
    public GetHomeCurrency createGetHomeCurrency() {
        return new GetHomeCurrency();
    }

    /**
     * Create an instance of {@link CountJournalEntries }
     * 
     */
    public CountJournalEntries createCountJournalEntries() {
        return new CountJournalEntries();
    }

    /**
     * Create an instance of {@link GetUsers }
     * 
     */
    public GetUsers createGetUsers() {
        return new GetUsers();
    }

    /**
     * Create an instance of {@link ContactInfo }
     * 
     */
    public ContactInfo createContactInfo() {
        return new ContactInfo();
    }

    /**
     * Create an instance of {@link GetUserByEmailResponse }
     * 
     */
    public GetUserByEmailResponse createGetUserByEmailResponse() {
        return new GetUserByEmailResponse();
    }

    /**
     * Create an instance of {@link GetFileResponse }
     * 
     */
    public GetFileResponse createGetFileResponse() {
        return new GetFileResponse();
    }

    /**
     * Create an instance of {@link UserInfo }
     * 
     */
    public UserInfo createUserInfo() {
        return new UserInfo();
    }

    /**
     * Create an instance of {@link AppendToAttachmentResponse }
     * 
     */
    public AppendToAttachmentResponse createAppendToAttachmentResponse() {
        return new AppendToAttachmentResponse();
    }

    /**
     * Create an instance of {@link GetCollaborationContext }
     * 
     */
    public GetCollaborationContext createGetCollaborationContext() {
        return new GetCollaborationContext();
    }

    /**
     * Create an instance of {@link SignUp }
     * 
     */
    public SignUp createSignUp() {
        return new SignUp();
    }

    /**
     * Create an instance of {@link CountRecordsResponse }
     * 
     */
    public CountRecordsResponse createCountRecordsResponse() {
        return new CountRecordsResponse();
    }

    /**
     * Create an instance of {@link GetBalances }
     * 
     */
    public GetBalances createGetBalances() {
        return new GetBalances();
    }

    /**
     * Create an instance of {@link CheckEmailVerificationCode }
     * 
     */
    public CheckEmailVerificationCode createCheckEmailVerificationCode() {
        return new CheckEmailVerificationCode();
    }

    /**
     * Create an instance of {@link CompleteAttachment }
     * 
     */
    public CompleteAttachment createCompleteAttachment() {
        return new CompleteAttachment();
    }

    /**
     * Create an instance of {@link EmailSettingsInfo }
     * 
     */
    public EmailSettingsInfo createEmailSettingsInfo() {
        return new EmailSettingsInfo();
    }

    /**
     * Create an instance of {@link AccountInfo }
     * 
     */
    public AccountInfo createAccountInfo() {
        return new AccountInfo();
    }

    /**
     * Create an instance of {@link RecordInfo.Payments }
     * 
     */
    public RecordInfo.Payments createRecordInfoPayments() {
        return new RecordInfo.Payments();
    }

    /**
     * Create an instance of {@link GetBalancesAtDate }
     * 
     */
    public GetBalancesAtDate createGetBalancesAtDate() {
        return new GetBalancesAtDate();
    }

    /**
     * Create an instance of {@link GetAccount }
     * 
     */
    public GetAccount createGetAccount() {
        return new GetAccount();
    }

    /**
     * Create an instance of {@link GetRolesResponse }
     * 
     */
    public GetRolesResponse createGetRolesResponse() {
        return new GetRolesResponse();
    }

    /**
     * Create an instance of {@link GetCanadianTaxInfoResponse }
     * 
     */
    public GetCanadianTaxInfoResponse createGetCanadianTaxInfoResponse() {
        return new GetCanadianTaxInfoResponse();
    }

    /**
     * Create an instance of {@link GetBalanceAtDate }
     * 
     */
    public GetBalanceAtDate createGetBalanceAtDate() {
        return new GetBalanceAtDate();
    }

    /**
     * Create an instance of {@link ReadFile }
     * 
     */
    public ReadFile createReadFile() {
        return new ReadFile();
    }

    /**
     * Create an instance of {@link CollaborationContextInfo }
     * 
     */
    public CollaborationContextInfo createCollaborationContextInfo() {
        return new CollaborationContextInfo();
    }

    /**
     * Create an instance of {@link SubscriptionInfo }
     * 
     */
    public SubscriptionInfo createSubscriptionInfo() {
        return new SubscriptionInfo();
    }

    /**
     * Create an instance of {@link GetLedgerLength }
     * 
     */
    public GetLedgerLength createGetLedgerLength() {
        return new GetLedgerLength();
    }

    /**
     * Create an instance of {@link SaveBankReconciliationResponse }
     * 
     */
    public SaveBankReconciliationResponse createSaveBankReconciliationResponse() {
        return new SaveBankReconciliationResponse();
    }

    /**
     * Create an instance of {@link GetFuturePayAgreement }
     * 
     */
    public GetFuturePayAgreement createGetFuturePayAgreement() {
        return new GetFuturePayAgreement();
    }

    /**
     * Create an instance of {@link SendTestEmail }
     * 
     */
    public SendTestEmail createSendTestEmail() {
        return new SendTestEmail();
    }

    /**
     * Create an instance of {@link GetBusinessResponse }
     * 
     */
    public GetBusinessResponse createGetBusinessResponse() {
        return new GetBusinessResponse();
    }

    /**
     * Create an instance of {@link GetRecordsOfType }
     * 
     */
    public GetRecordsOfType createGetRecordsOfType() {
        return new GetRecordsOfType();
    }

    /**
     * Create an instance of {@link CollaborationEventInfo }
     * 
     */
    public CollaborationEventInfo createCollaborationEventInfo() {
        return new CollaborationEventInfo();
    }

    /**
     * Create an instance of {@link RetryContractPaymentResponse }
     * 
     */
    public RetryContractPaymentResponse createRetryContractPaymentResponse() {
        return new RetryContractPaymentResponse();
    }

    /**
     * Create an instance of {@link GetBusinessesForUserResponse }
     * 
     */
    public GetBusinessesForUserResponse createGetBusinessesForUserResponse() {
        return new GetBusinessesForUserResponse();
    }

    /**
     * Create an instance of {@link GetContacts }
     * 
     */
    public GetContacts createGetContacts() {
        return new GetContacts();
    }

    /**
     * Create an instance of {@link GetBankReconciliationsResponse }
     * 
     */
    public GetBankReconciliationsResponse createGetBankReconciliationsResponse() {
        return new GetBankReconciliationsResponse();
    }

    /**
     * Create an instance of {@link GetAccountsResponse }
     * 
     */
    public GetAccountsResponse createGetAccountsResponse() {
        return new GetAccountsResponse();
    }

    /**
     * Create an instance of {@link AutomaticMessageInfo }
     * 
     */
    public AutomaticMessageInfo createAutomaticMessageInfo() {
        return new AutomaticMessageInfo();
    }

    /**
     * Create an instance of {@link AddUserResponse }
     * 
     */
    public AddUserResponse createAddUserResponse() {
        return new AddUserResponse();
    }

    /**
     * Create an instance of {@link SaveVatTaxInfoResponse }
     * 
     */
    public SaveVatTaxInfoResponse createSaveVatTaxInfoResponse() {
        return new SaveVatTaxInfoResponse();
    }

    /**
     * Create an instance of {@link GetVatTaxInfo }
     * 
     */
    public GetVatTaxInfo createGetVatTaxInfo() {
        return new GetVatTaxInfo();
    }

    /**
     * Create an instance of {@link GetBalance }
     * 
     */
    public GetBalance createGetBalance() {
        return new GetBalance();
    }

    /**
     * Create an instance of {@link PredictAccountNumberResponse }
     * 
     */
    public PredictAccountNumberResponse createPredictAccountNumberResponse() {
        return new PredictAccountNumberResponse();
    }

    /**
     * Create an instance of {@link SetRolesResponse }
     * 
     */
    public SetRolesResponse createSetRolesResponse() {
        return new SetRolesResponse();
    }

    /**
     * Create an instance of {@link GetCountries }
     * 
     */
    public GetCountries createGetCountries() {
        return new GetCountries();
    }

    /**
     * Create an instance of {@link GetAccounts }
     * 
     */
    public GetAccounts createGetAccounts() {
        return new GetAccounts();
    }

    /**
     * Create an instance of {@link GetAutomaticMessageResponse }
     * 
     */
    public GetAutomaticMessageResponse createGetAutomaticMessageResponse() {
        return new GetAutomaticMessageResponse();
    }

    /**
     * Create an instance of {@link CreateRestrictedTokenResponse }
     * 
     */
    public CreateRestrictedTokenResponse createCreateRestrictedTokenResponse() {
        return new CreateRestrictedTokenResponse();
    }

    /**
     * Create an instance of {@link GetSubscriptionPlanResponse }
     * 
     */
    public GetSubscriptionPlanResponse createGetSubscriptionPlanResponse() {
        return new GetSubscriptionPlanResponse();
    }

    /**
     * Create an instance of {@link GetMyBusinessesResponse }
     * 
     */
    public GetMyBusinessesResponse createGetMyBusinessesResponse() {
        return new GetMyBusinessesResponse();
    }

    /**
     * Create an instance of {@link SaveCanadianTaxInfo }
     * 
     */
    public SaveCanadianTaxInfo createSaveCanadianTaxInfo() {
        return new SaveCanadianTaxInfo();
    }

    /**
     * Create an instance of {@link GetSiteTitle }
     * 
     */
    public GetSiteTitle createGetSiteTitle() {
        return new GetSiteTitle();
    }

    /**
     * Create an instance of {@link GetRecordsForContact }
     * 
     */
    public GetRecordsForContact createGetRecordsForContact() {
        return new GetRecordsForContact();
    }

    /**
     * Create an instance of {@link CheckEmailVerificationCodeResponse }
     * 
     */
    public CheckEmailVerificationCodeResponse createCheckEmailVerificationCodeResponse() {
        return new CheckEmailVerificationCodeResponse();
    }

    /**
     * Create an instance of {@link JournalEntryInfo }
     * 
     */
    public JournalEntryInfo createJournalEntryInfo() {
        return new JournalEntryInfo();
    }

    /**
     * Create an instance of {@link GetCurrencyByCodeResponse }
     * 
     */
    public GetCurrencyByCodeResponse createGetCurrencyByCodeResponse() {
        return new GetCurrencyByCodeResponse();
    }

    /**
     * Create an instance of {@link GetCurrencyResponse }
     * 
     */
    public GetCurrencyResponse createGetCurrencyResponse() {
        return new GetCurrencyResponse();
    }

    /**
     * Create an instance of {@link SaveEmailSettings }
     * 
     */
    public SaveEmailSettings createSaveEmailSettings() {
        return new SaveEmailSettings();
    }

    /**
     * Create an instance of {@link SaveSubscriptionResponse }
     * 
     */
    public SaveSubscriptionResponse createSaveSubscriptionResponse() {
        return new SaveSubscriptionResponse();
    }

    /**
     * Create an instance of {@link CountRecordsOfType }
     * 
     */
    public CountRecordsOfType createCountRecordsOfType() {
        return new CountRecordsOfType();
    }

    /**
     * Create an instance of {@link SaveCollaborationEventResponse }
     * 
     */
    public SaveCollaborationEventResponse createSaveCollaborationEventResponse() {
        return new SaveCollaborationEventResponse();
    }

    /**
     * Create an instance of {@link GetSubscriptionPlans }
     * 
     */
    public GetSubscriptionPlans createGetSubscriptionPlans() {
        return new GetSubscriptionPlans();
    }

    /**
     * Create an instance of {@link EncodeAuthToken }
     * 
     */
    public EncodeAuthToken createEncodeAuthToken() {
        return new EncodeAuthToken();
    }

    /**
     * Create an instance of {@link FindRegion }
     * 
     */
    public FindRegion createFindRegion() {
        return new FindRegion();
    }

    /**
     * Create an instance of {@link ExtendLogin }
     * 
     */
    public ExtendLogin createExtendLogin() {
        return new ExtendLogin();
    }

    /**
     * Create an instance of {@link GetFuturePaySetupUrlResponse }
     * 
     */
    public GetFuturePaySetupUrlResponse createGetFuturePaySetupUrlResponse() {
        return new GetFuturePaySetupUrlResponse();
    }

    /**
     * Create an instance of {@link GetSubscriptionsWithContactResponse }
     * 
     */
    public GetSubscriptionsWithContactResponse createGetSubscriptionsWithContactResponse() {
        return new GetSubscriptionsWithContactResponse();
    }

    /**
     * Create an instance of {@link AvailablePaymentMethodInfo }
     * 
     */
    public AvailablePaymentMethodInfo createAvailablePaymentMethodInfo() {
        return new AvailablePaymentMethodInfo();
    }

    /**
     * Create an instance of {@link GetAvailableContractPlans }
     * 
     */
    public GetAvailableContractPlans createGetAvailableContractPlans() {
        return new GetAvailableContractPlans();
    }

    /**
     * Create an instance of {@link SaveContact }
     * 
     */
    public SaveContact createSaveContact() {
        return new SaveContact();
    }

    /**
     * Create an instance of {@link GetBusinessesForUser }
     * 
     */
    public GetBusinessesForUser createGetBusinessesForUser() {
        return new GetBusinessesForUser();
    }

    /**
     * Create an instance of {@link SaveBankReconciliation }
     * 
     */
    public SaveBankReconciliation createSaveBankReconciliation() {
        return new SaveBankReconciliation();
    }

    /**
     * Create an instance of {@link SaveCanadianTaxInfoResponse }
     * 
     */
    public SaveCanadianTaxInfoResponse createSaveCanadianTaxInfoResponse() {
        return new SaveCanadianTaxInfoResponse();
    }

    /**
     * Create an instance of {@link GetTaxCodesResponse }
     * 
     */
    public GetTaxCodesResponse createGetTaxCodesResponse() {
        return new GetTaxCodesResponse();
    }

    /**
     * Create an instance of {@link ExtendLoginResponse }
     * 
     */
    public ExtendLoginResponse createExtendLoginResponse() {
        return new ExtendLoginResponse();
    }

    /**
     * Create an instance of {@link GetBalanceResponse }
     * 
     */
    public GetBalanceResponse createGetBalanceResponse() {
        return new GetBalanceResponse();
    }

    /**
     * Create an instance of {@link SaveTaxCodes }
     * 
     */
    public SaveTaxCodes createSaveTaxCodes() {
        return new SaveTaxCodes();
    }

    /**
     * Create an instance of {@link GetHomeCurrencyResponse }
     * 
     */
    public GetHomeCurrencyResponse createGetHomeCurrencyResponse() {
        return new GetHomeCurrencyResponse();
    }

    /**
     * Create an instance of {@link GetTimeInZone }
     * 
     */
    public GetTimeInZone createGetTimeInZone() {
        return new GetTimeInZone();
    }

    /**
     * Create an instance of {@link NotFound }
     * 
     */
    public NotFound createNotFound() {
        return new NotFound();
    }

    /**
     * Create an instance of {@link TaxCodeInfo }
     * 
     */
    public TaxCodeInfo createTaxCodeInfo() {
        return new TaxCodeInfo();
    }

    /**
     * Create an instance of {@link SetContractPlanResponse }
     * 
     */
    public SetContractPlanResponse createSetContractPlanResponse() {
        return new SetContractPlanResponse();
    }

    /**
     * Create an instance of {@link VatTaxInfo }
     * 
     */
    public VatTaxInfo createVatTaxInfo() {
        return new VatTaxInfo();
    }

    /**
     * Create an instance of {@link GetDefaultRegionResponse }
     * 
     */
    public GetDefaultRegionResponse createGetDefaultRegionResponse() {
        return new GetDefaultRegionResponse();
    }

    /**
     * Create an instance of {@link SetTaxRateResponse }
     * 
     */
    public SetTaxRateResponse createSetTaxRateResponse() {
        return new SetTaxRateResponse();
    }

    /**
     * Create an instance of {@link ConflictsWithAnother }
     * 
     */
    public ConflictsWithAnother createConflictsWithAnother() {
        return new ConflictsWithAnother();
    }

    /**
     * Create an instance of {@link GetUser }
     * 
     */
    public GetUser createGetUser() {
        return new GetUser();
    }

    /**
     * Create an instance of {@link GetCurrencyByCode }
     * 
     */
    public GetCurrencyByCode createGetCurrencyByCode() {
        return new GetCurrencyByCode();
    }

    /**
     * Create an instance of {@link TestAutomaticMessageResponse }
     * 
     */
    public TestAutomaticMessageResponse createTestAutomaticMessageResponse() {
        return new TestAutomaticMessageResponse();
    }

    /**
     * Create an instance of {@link BadPassword }
     * 
     */
    public BadPassword createBadPassword() {
        return new BadPassword();
    }

    /**
     * Create an instance of {@link StoreFile }
     * 
     */
    public StoreFile createStoreFile() {
        return new StoreFile();
    }

    /**
     * Create an instance of {@link AddNewCustomer }
     * 
     */
    public AddNewCustomer createAddNewCustomer() {
        return new AddNewCustomer();
    }

    /**
     * Create an instance of {@link GetAllItems }
     * 
     */
    public GetAllItems createGetAllItems() {
        return new GetAllItems();
    }

    /**
     * Create an instance of {@link GetExchangeRate }
     * 
     */
    public GetExchangeRate createGetExchangeRate() {
        return new GetExchangeRate();
    }

    /**
     * Create an instance of {@link RecordInfo.PaymentAllocationsOut }
     * 
     */
    public RecordInfo.PaymentAllocationsOut createRecordInfoPaymentAllocationsOut() {
        return new RecordInfo.PaymentAllocationsOut();
    }

    /**
     * Create an instance of {@link ContractPlanInfo }
     * 
     */
    public ContractPlanInfo createContractPlanInfo() {
        return new ContractPlanInfo();
    }

    /**
     * Create an instance of {@link RecordInfo }
     * 
     */
    public RecordInfo createRecordInfo() {
        return new RecordInfo();
    }

    /**
     * Create an instance of {@link GetSubscription }
     * 
     */
    public GetSubscription createGetSubscription() {
        return new GetSubscription();
    }

    /**
     * Create an instance of {@link GetContactsOfType }
     * 
     */
    public GetContactsOfType createGetContactsOfType() {
        return new GetContactsOfType();
    }

    /**
     * Create an instance of {@link GetItemSubcategoryResponse }
     * 
     */
    public GetItemSubcategoryResponse createGetItemSubcategoryResponse() {
        return new GetItemSubcategoryResponse();
    }

    /**
     * Create an instance of {@link CurrencyInfo }
     * 
     */
    public CurrencyInfo createCurrencyInfo() {
        return new CurrencyInfo();
    }

    /**
     * Create an instance of {@link SaveVatTaxInfo }
     * 
     */
    public SaveVatTaxInfo createSaveVatTaxInfo() {
        return new SaveVatTaxInfo();
    }

    /**
     * Create an instance of {@link GetTaxes }
     * 
     */
    public GetTaxes createGetTaxes() {
        return new GetTaxes();
    }

    /**
     * Create an instance of {@link SubmitFeedbackResponse }
     * 
     */
    public SubmitFeedbackResponse createSubmitFeedbackResponse() {
        return new SubmitFeedbackResponse();
    }

    /**
     * Create an instance of {@link GetAvailablePaymentMethodsResponse }
     * 
     */
    public GetAvailablePaymentMethodsResponse createGetAvailablePaymentMethodsResponse() {
        return new GetAvailablePaymentMethodsResponse();
    }

    /**
     * Create an instance of {@link GetOverpaidRecordsResponse }
     * 
     */
    public GetOverpaidRecordsResponse createGetOverpaidRecordsResponse() {
        return new GetOverpaidRecordsResponse();
    }

    /**
     * Create an instance of {@link GetMyUserResponse }
     * 
     */
    public GetMyUserResponse createGetMyUserResponse() {
        return new GetMyUserResponse();
    }

    /**
     * Create an instance of {@link SaveSubscriptionPlan }
     * 
     */
    public SaveSubscriptionPlan createSaveSubscriptionPlan() {
        return new SaveSubscriptionPlan();
    }

    /**
     * Create an instance of {@link CompleteSignUpResponse }
     * 
     */
    public CompleteSignUpResponse createCompleteSignUpResponse() {
        return new CompleteSignUpResponse();
    }

    /**
     * Create an instance of {@link CountChangesetsResponse }
     * 
     */
    public CountChangesetsResponse createCountChangesetsResponse() {
        return new CountChangesetsResponse();
    }

    /**
     * Create an instance of {@link WorldPayInfo }
     * 
     */
    public WorldPayInfo createWorldPayInfo() {
        return new WorldPayInfo();
    }

    /**
     * Create an instance of {@link GetTaxRateResponse }
     * 
     */
    public GetTaxRateResponse createGetTaxRateResponse() {
        return new GetTaxRateResponse();
    }

    /**
     * Create an instance of {@link GetBasicTaxInfoResponse }
     * 
     */
    public GetBasicTaxInfoResponse createGetBasicTaxInfoResponse() {
        return new GetBasicTaxInfoResponse();
    }

    /**
     * Create an instance of {@link SetRoles }
     * 
     */
    public SetRoles createSetRoles() {
        return new SetRoles();
    }

    /**
     * Create an instance of {@link CountRecordsOfTypeResponse }
     * 
     */
    public CountRecordsOfTypeResponse createCountRecordsOfTypeResponse() {
        return new CountRecordsOfTypeResponse();
    }

    /**
     * Create an instance of {@link GetSubcategoriesResponse }
     * 
     */
    public GetSubcategoriesResponse createGetSubcategoriesResponse() {
        return new GetSubcategoriesResponse();
    }

    /**
     * Create an instance of {@link GetContract }
     * 
     */
    public GetContract createGetContract() {
        return new GetContract();
    }

    /**
     * Create an instance of {@link GetVendorsResponse }
     * 
     */
    public GetVendorsResponse createGetVendorsResponse() {
        return new GetVendorsResponse();
    }

    /**
     * Create an instance of {@link DoLogoutResponse }
     * 
     */
    public DoLogoutResponse createDoLogoutResponse() {
        return new DoLogoutResponse();
    }

    /**
     * Create an instance of {@link GetProvincesOrStates }
     * 
     */
    public GetProvincesOrStates createGetProvincesOrStates() {
        return new GetProvincesOrStates();
    }

    /**
     * Create an instance of {@link TaxInfo }
     * 
     */
    public TaxInfo createTaxInfo() {
        return new TaxInfo();
    }

    /**
     * Create an instance of {@link GetUsersResponse }
     * 
     */
    public GetUsersResponse createGetUsersResponse() {
        return new GetUsersResponse();
    }

    /**
     * Create an instance of {@link AppendToFileResponse }
     * 
     */
    public AppendToFileResponse createAppendToFileResponse() {
        return new AppendToFileResponse();
    }

    /**
     * Create an instance of {@link AddBusinessResponse }
     * 
     */
    public AddBusinessResponse createAddBusinessResponse() {
        return new AddBusinessResponse();
    }

    /**
     * Create an instance of {@link DoLogin }
     * 
     */
    public DoLogin createDoLogin() {
        return new DoLogin();
    }

    /**
     * Create an instance of {@link GetEmailSettingsResponse }
     * 
     */
    public GetEmailSettingsResponse createGetEmailSettingsResponse() {
        return new GetEmailSettingsResponse();
    }

    /**
     * Create an instance of {@link ChangeUserPasswordResponse }
     * 
     */
    public ChangeUserPasswordResponse createChangeUserPasswordResponse() {
        return new ChangeUserPasswordResponse();
    }

    /**
     * Create an instance of {@link GetRecordsForContactResponse }
     * 
     */
    public GetRecordsForContactResponse createGetRecordsForContactResponse() {
        return new GetRecordsForContactResponse();
    }

    /**
     * Create an instance of {@link GetAvailableContractPlansResponse }
     * 
     */
    public GetAvailableContractPlansResponse createGetAvailableContractPlansResponse() {
        return new GetAvailableContractPlansResponse();
    }

    /**
     * Create an instance of {@link EntryInfo }
     * 
     */
    public EntryInfo createEntryInfo() {
        return new EntryInfo();
    }

    /**
     * Create an instance of {@link AddAttachment }
     * 
     */
    public AddAttachment createAddAttachment() {
        return new AddAttachment();
    }

    /**
     * Create an instance of {@link RecordInfo.Entries }
     * 
     */
    public RecordInfo.Entries createRecordInfoEntries() {
        return new RecordInfo.Entries();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "initialContent", scope = StoreFile.class)
    public JAXBElement<byte[]> createStoreFileInitialContent(byte[] value) {
        return new JAXBElement<byte[]>(_StoreFileInitialContent_QNAME, byte[].class, StoreFile.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSiteTitleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSiteTitleResponse")
    public JAXBElement<GetSiteTitleResponse> createGetSiteTitleResponse(GetSiteTitleResponse value) {
        return new JAXBElement<GetSiteTitleResponse>(_GetSiteTitleResponse_QNAME, GetSiteTitleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DecodeAuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "decodeAuthToken")
    public JAXBElement<DecodeAuthToken> createDecodeAuthToken(DecodeAuthToken value) {
        return new JAXBElement<DecodeAuthToken>(_DecodeAuthToken_QNAME, DecodeAuthToken.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountJournalEntriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countJournalEntriesResponse")
    public JAXBElement<CountJournalEntriesResponse> createCountJournalEntriesResponse(CountJournalEntriesResponse value) {
        return new JAXBElement<CountJournalEntriesResponse>(_CountJournalEntriesResponse_QNAME, CountJournalEntriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewVendor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addNewVendor")
    public JAXBElement<AddNewVendor> createAddNewVendor(AddNewVendor value) {
        return new JAXBElement<AddNewVendor>(_AddNewVendor_QNAME, AddNewVendor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRoles")
    public JAXBElement<GetRoles> createGetRoles(GetRoles value) {
        return new JAXBElement<GetRoles>(_GetRoles_QNAME, GetRoles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountJournalEntries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countJournalEntries")
    public JAXBElement<CountJournalEntries> createCountJournalEntries(CountJournalEntries value) {
        return new JAXBElement<CountJournalEntries>(_CountJournalEntries_QNAME, CountJournalEntries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRolesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRolesResponse")
    public JAXBElement<GetRolesResponse> createGetRolesResponse(GetRolesResponse value) {
        return new JAXBElement<GetRolesResponse>(_GetRolesResponse_QNAME, GetRolesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMyUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getMyUser")
    public JAXBElement<GetMyUser> createGetMyUser(GetMyUser value) {
        return new JAXBElement<GetMyUser>(_GetMyUser_QNAME, GetMyUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddBusinessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addBusinessResponse")
    public JAXBElement<AddBusinessResponse> createAddBusinessResponse(AddBusinessResponse value) {
        return new JAXBElement<AddBusinessResponse>(_AddBusinessResponse_QNAME, AddBusinessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "readFile")
    public JAXBElement<ReadFile> createReadFile(ReadFile value) {
        return new JAXBElement<ReadFile>(_ReadFile_QNAME, ReadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveEmailSettingsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveEmailSettingsResponse")
    public JAXBElement<SaveEmailSettingsResponse> createSaveEmailSettingsResponse(SaveEmailSettingsResponse value) {
        return new JAXBElement<SaveEmailSettingsResponse>(_SaveEmailSettingsResponse_QNAME, SaveEmailSettingsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBasicTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBasicTaxInfo")
    public JAXBElement<GetBasicTaxInfo> createGetBasicTaxInfo(GetBasicTaxInfo value) {
        return new JAXBElement<GetBasicTaxInfo>(_GetBasicTaxInfo_QNAME, GetBasicTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBusinessesForUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBusinessesForUserResponse")
    public JAXBElement<GetBusinessesForUserResponse> createGetBusinessesForUserResponse(GetBusinessesForUserResponse value) {
        return new JAXBElement<GetBusinessesForUserResponse>(_GetBusinessesForUserResponse_QNAME, GetBusinessesForUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUnclearedTransactionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUnclearedTransactionsResponse")
    public JAXBElement<GetUnclearedTransactionsResponse> createGetUnclearedTransactionsResponse(GetUnclearedTransactionsResponse value) {
        return new JAXBElement<GetUnclearedTransactionsResponse>(_GetUnclearedTransactionsResponse_QNAME, GetUnclearedTransactionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecordResponse")
    public JAXBElement<GetRecordResponse> createGetRecordResponse(GetRecordResponse value) {
        return new JAXBElement<GetRecordResponse>(_GetRecordResponse_QNAME, GetRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternalError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "InternalError")
    public JAXBElement<InternalError> createInternalError(InternalError value) {
        return new JAXBElement<InternalError>(_InternalError_QNAME, InternalError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrencies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCurrencies")
    public JAXBElement<GetCurrencies> createGetCurrencies(GetCurrencies value) {
        return new JAXBElement<GetCurrencies>(_GetCurrencies_QNAME, GetCurrencies.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotFound }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "NotFound")
    public JAXBElement<NotFound> createNotFound(NotFound value) {
        return new JAXBElement<NotFound>(_NotFound_QNAME, NotFound.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetTaxRateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setTaxRateResponse")
    public JAXBElement<SetTaxRateResponse> createSetTaxRateResponse(SetTaxRateResponse value) {
        return new JAXBElement<SetTaxRateResponse>(_SetTaxRateResponse_QNAME, SetTaxRateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllProvincesOrStatesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAllProvincesOrStatesResponse")
    public JAXBElement<GetAllProvincesOrStatesResponse> createGetAllProvincesOrStatesResponse(GetAllProvincesOrStatesResponse value) {
        return new JAXBElement<GetAllProvincesOrStatesResponse>(_GetAllProvincesOrStatesResponse_QNAME, GetAllProvincesOrStatesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimeZones }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTimeZones")
    public JAXBElement<GetTimeZones> createGetTimeZones(GetTimeZones value) {
        return new JAXBElement<GetTimeZones>(_GetTimeZones_QNAME, GetTimeZones.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveSubscriptionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveSubscriptionResponse")
    public JAXBElement<SaveSubscriptionResponse> createSaveSubscriptionResponse(SaveSubscriptionResponse value) {
        return new JAXBElement<SaveSubscriptionResponse>(_SaveSubscriptionResponse_QNAME, SaveSubscriptionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "basicTaxInfo")
    public JAXBElement<BasicTaxInfo> createBasicTaxInfo(BasicTaxInfo value) {
        return new JAXBElement<BasicTaxInfo>(_BasicTaxInfo_QNAME, BasicTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUserResponse")
    public JAXBElement<GetUserResponse> createGetUserResponse(GetUserResponse value) {
        return new JAXBElement<GetUserResponse>(_GetUserResponse_QNAME, GetUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRegionResponse")
    public JAXBElement<GetRegionResponse> createGetRegionResponse(GetRegionResponse value) {
        return new JAXBElement<GetRegionResponse>(_GetRegionResponse_QNAME, GetRegionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveContactResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveContactResponse")
    public JAXBElement<SaveContactResponse> createSaveContactResponse(SaveContactResponse value) {
        return new JAXBElement<SaveContactResponse>(_SaveContactResponse_QNAME, SaveContactResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteAttachmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completeAttachmentResponse")
    public JAXBElement<CompleteAttachmentResponse> createCompleteAttachmentResponse(CompleteAttachmentResponse value) {
        return new JAXBElement<CompleteAttachmentResponse>(_CompleteAttachmentResponse_QNAME, CompleteAttachmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRecordsOfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countRecordsOfType")
    public JAXBElement<CountRecordsOfType> createCountRecordsOfType(CountRecordsOfType value) {
        return new JAXBElement<CountRecordsOfType>(_CountRecordsOfType_QNAME, CountRecordsOfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFuturePaySetupUrl }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getFuturePaySetupUrl")
    public JAXBElement<GetFuturePaySetupUrl> createGetFuturePaySetupUrl(GetFuturePaySetupUrl value) {
        return new JAXBElement<GetFuturePaySetupUrl>(_GetFuturePaySetupUrl_QNAME, GetFuturePaySetupUrl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsForContact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecordsForContact")
    public JAXBElement<GetRecordsForContact> createGetRecordsForContact(GetRecordsForContact value) {
        return new JAXBElement<GetRecordsForContact>(_GetRecordsForContact_QNAME, GetRecordsForContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignUp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "signUp")
    public JAXBElement<SignUp> createSignUp(SignUp value) {
        return new JAXBElement<SignUp>(_SignUp_QNAME, SignUp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBasicTaxInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBasicTaxInfoResponse")
    public JAXBElement<GetBasicTaxInfoResponse> createGetBasicTaxInfoResponse(GetBasicTaxInfoResponse value) {
        return new JAXBElement<GetBasicTaxInfoResponse>(_GetBasicTaxInfoResponse_QNAME, GetBasicTaxInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckEmailVerificationCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "checkEmailVerificationCode")
    public JAXBElement<CheckEmailVerificationCode> createCheckEmailVerificationCode(CheckEmailVerificationCode value) {
        return new JAXBElement<CheckEmailVerificationCode>(_CheckEmailVerificationCode_QNAME, CheckEmailVerificationCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRestrictedToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "createRestrictedToken")
    public JAXBElement<CreateRestrictedToken> createCreateRestrictedToken(CreateRestrictedToken value) {
        return new JAXBElement<CreateRestrictedToken>(_CreateRestrictedToken_QNAME, CreateRestrictedToken.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PredictAccountNumberResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "predictAccountNumberResponse")
    public JAXBElement<PredictAccountNumberResponse> createPredictAccountNumberResponse(PredictAccountNumberResponse value) {
        return new JAXBElement<PredictAccountNumberResponse>(_PredictAccountNumberResponse_QNAME, PredictAccountNumberResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAutomaticMessages }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAutomaticMessages")
    public JAXBElement<GetAutomaticMessages> createGetAutomaticMessages(GetAutomaticMessages value) {
        return new JAXBElement<GetAutomaticMessages>(_GetAutomaticMessages_QNAME, GetAutomaticMessages.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPaymentSetupInfoForBusiness }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getPaymentSetupInfoForBusiness")
    public JAXBElement<GetPaymentSetupInfoForBusiness> createGetPaymentSetupInfoForBusiness(GetPaymentSetupInfoForBusiness value) {
        return new JAXBElement<GetPaymentSetupInfoForBusiness>(_GetPaymentSetupInfoForBusiness_QNAME, GetPaymentSetupInfoForBusiness.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItems }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getItems")
    public JAXBElement<GetItems> createGetItems(GetItems value) {
        return new JAXBElement<GetItems>(_GetItems_QNAME, GetItems.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailablePaymentMethods }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAvailablePaymentMethods")
    public JAXBElement<GetAvailablePaymentMethods> createGetAvailablePaymentMethods(GetAvailablePaymentMethods value) {
        return new JAXBElement<GetAvailablePaymentMethods>(_GetAvailablePaymentMethods_QNAME, GetAvailablePaymentMethods.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCustomersResponse")
    public JAXBElement<GetCustomersResponse> createGetCustomersResponse(GetCustomersResponse value) {
        return new JAXBElement<GetCustomersResponse>(_GetCustomersResponse_QNAME, GetCustomersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRestrictedTokenResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "createRestrictedTokenResponse")
    public JAXBElement<CreateRestrictedTokenResponse> createCreateRestrictedTokenResponse(CreateRestrictedTokenResponse value) {
        return new JAXBElement<CreateRestrictedTokenResponse>(_CreateRestrictedTokenResponse_QNAME, CreateRestrictedTokenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWorldPay }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getWorldPay")
    public JAXBElement<GetWorldPay> createGetWorldPay(GetWorldPay value) {
        return new JAXBElement<GetWorldPay>(_GetWorldPay_QNAME, GetWorldPay.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLedgerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getLedgerResponse")
    public JAXBElement<GetLedgerResponse> createGetLedgerResponse(GetLedgerResponse value) {
        return new JAXBElement<GetLedgerResponse>(_GetLedgerResponse_QNAME, GetLedgerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteSignUpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completeSignUpResponse")
    public JAXBElement<CompleteSignUpResponse> createCompleteSignUpResponse(CompleteSignUpResponse value) {
        return new JAXBElement<CompleteSignUpResponse>(_CompleteSignUpResponse_QNAME, CompleteSignUpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "doLogin")
    public JAXBElement<DoLogin> createDoLogin(DoLogin value) {
        return new JAXBElement<DoLogin>(_DoLogin_QNAME, DoLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubcategories }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubcategories")
    public JAXBElement<GetSubcategories> createGetSubcategories(GetSubcategories value) {
        return new JAXBElement<GetSubcategories>(_GetSubcategories_QNAME, GetSubcategories.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearTaxRateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "clearTaxRateResponse")
    public JAXBElement<ClearTaxRateResponse> createClearTaxRateResponse(ClearTaxRateResponse value) {
        return new JAXBElement<ClearTaxRateResponse>(_ClearTaxRateResponse_QNAME, ClearTaxRateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecord")
    public JAXBElement<GetRecord> createGetRecord(GetRecord value) {
        return new JAXBElement<GetRecord>(_GetRecord_QNAME, GetRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveWorldPay }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveWorldPay")
    public JAXBElement<SaveWorldPay> createSaveWorldPay(SaveWorldPay value) {
        return new JAXBElement<SaveWorldPay>(_SaveWorldPay_QNAME, SaveWorldPay.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBankReconciliations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBankReconciliations")
    public JAXBElement<GetBankReconciliations> createGetBankReconciliations(GetBankReconciliations value) {
        return new JAXBElement<GetBankReconciliations>(_GetBankReconciliations_QNAME, GetBankReconciliations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BadArgument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "BadArgument")
    public JAXBElement<BadArgument> createBadArgument(BadArgument value) {
        return new JAXBElement<BadArgument>(_BadArgument_QNAME, BadArgument.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "readFileResponse")
    public JAXBElement<ReadFileResponse> createReadFileResponse(ReadFileResponse value) {
        return new JAXBElement<ReadFileResponse>(_ReadFileResponse_QNAME, ReadFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPaymentSetupInfoForBusinessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getPaymentSetupInfoForBusinessResponse")
    public JAXBElement<GetPaymentSetupInfoForBusinessResponse> createGetPaymentSetupInfoForBusinessResponse(GetPaymentSetupInfoForBusinessResponse value) {
        return new JAXBElement<GetPaymentSetupInfoForBusinessResponse>(_GetPaymentSetupInfoForBusinessResponse_QNAME, GetPaymentSetupInfoForBusinessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindRegion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "findRegion")
    public JAXBElement<FindRegion> createFindRegion(FindRegion value) {
        return new JAXBElement<FindRegion>(_FindRegion_QNAME, FindRegion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConflictsWithAnother }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "ConflictsWithAnother")
    public JAXBElement<ConflictsWithAnother> createConflictsWithAnother(ConflictsWithAnother value) {
        return new JAXBElement<ConflictsWithAnother>(_ConflictsWithAnother_QNAME, ConflictsWithAnother.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewCustomerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addNewCustomerResponse")
    public JAXBElement<AddNewCustomerResponse> createAddNewCustomerResponse(AddNewCustomerResponse value) {
        return new JAXBElement<AddNewCustomerResponse>(_AddNewCustomerResponse_QNAME, AddNewCustomerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionPlansResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionPlansResponse")
    public JAXBElement<GetSubscriptionPlansResponse> createGetSubscriptionPlansResponse(GetSubscriptionPlansResponse value) {
        return new JAXBElement<GetSubscriptionPlansResponse>(_GetSubscriptionPlansResponse_QNAME, GetSubscriptionPlansResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AlreadyExists }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "AlreadyExists")
    public JAXBElement<AlreadyExists> createAlreadyExists(AlreadyExists value) {
        return new JAXBElement<AlreadyExists>(_AlreadyExists_QNAME, AlreadyExists.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAccountByNameResponse")
    public JAXBElement<GetAccountByNameResponse> createGetAccountByNameResponse(GetAccountByNameResponse value) {
        return new JAXBElement<GetAccountByNameResponse>(_GetAccountByNameResponse_QNAME, GetAccountByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetryContractPaymentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "retryContractPaymentResponse")
    public JAXBElement<RetryContractPaymentResponse> createRetryContractPaymentResponse(RetryContractPaymentResponse value) {
        return new JAXBElement<RetryContractPaymentResponse>(_RetryContractPaymentResponse_QNAME, RetryContractPaymentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVendorsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getVendorsResponse")
    public JAXBElement<GetVendorsResponse> createGetVendorsResponse(GetVendorsResponse value) {
        return new JAXBElement<GetVendorsResponse>(_GetVendorsResponse_QNAME, GetVendorsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCountriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCountriesResponse")
    public JAXBElement<GetCountriesResponse> createGetCountriesResponse(GetCountriesResponse value) {
        return new JAXBElement<GetCountriesResponse>(_GetCountriesResponse_QNAME, GetCountriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxRate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxRate")
    public JAXBElement<GetTaxRate> createGetTaxRate(GetTaxRate value) {
        return new JAXBElement<GetTaxRate>(_GetTaxRate_QNAME, GetTaxRate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollaborationContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCollaborationContextResponse")
    public JAXBElement<GetCollaborationContextResponse> createGetCollaborationContextResponse(GetCollaborationContextResponse value) {
        return new JAXBElement<GetCollaborationContextResponse>(_GetCollaborationContextResponse_QNAME, GetCollaborationContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestEmailVerificationCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "requestEmailVerificationCodeResponse")
    public JAXBElement<RequestEmailVerificationCodeResponse> createRequestEmailVerificationCodeResponse(RequestEmailVerificationCodeResponse value) {
        return new JAXBElement<RequestEmailVerificationCodeResponse>(_RequestEmailVerificationCodeResponse_QNAME, RequestEmailVerificationCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBankReconciliationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBankReconciliationResponse")
    public JAXBElement<GetBankReconciliationResponse> createGetBankReconciliationResponse(GetBankReconciliationResponse value) {
        return new JAXBElement<GetBankReconciliationResponse>(_GetBankReconciliationResponse_QNAME, GetBankReconciliationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BadPassword }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "BadPassword")
    public JAXBElement<BadPassword> createBadPassword(BadPassword value) {
        return new JAXBElement<BadPassword>(_BadPassword_QNAME, BadPassword.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatementOpeningBalancesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getStatementOpeningBalancesResponse")
    public JAXBElement<GetStatementOpeningBalancesResponse> createGetStatementOpeningBalancesResponse(GetStatementOpeningBalancesResponse value) {
        return new JAXBElement<GetStatementOpeningBalancesResponse>(_GetStatementOpeningBalancesResponse_QNAME, GetStatementOpeningBalancesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserByEmail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUserByEmail")
    public JAXBElement<GetUserByEmail> createGetUserByEmail(GetUserByEmail value) {
        return new JAXBElement<GetUserByEmail>(_GetUserByEmail_QNAME, GetUserByEmail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVatTaxInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getVatTaxInfoResponse")
    public JAXBElement<GetVatTaxInfoResponse> createGetVatTaxInfoResponse(GetVatTaxInfoResponse value) {
        return new JAXBElement<GetVatTaxInfoResponse>(_GetVatTaxInfoResponse_QNAME, GetVatTaxInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalanceAtDate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalanceAtDate")
    public JAXBElement<GetBalanceAtDate> createGetBalanceAtDate(GetBalanceAtDate value) {
        return new JAXBElement<GetBalanceAtDate>(_GetBalanceAtDate_QNAME, GetBalanceAtDate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveVatTaxInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveVatTaxInfoResponse")
    public JAXBElement<SaveVatTaxInfoResponse> createSaveVatTaxInfoResponse(SaveVatTaxInfoResponse value) {
        return new JAXBElement<SaveVatTaxInfoResponse>(_SaveVatTaxInfoResponse_QNAME, SaveVatTaxInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContactResponse")
    public JAXBElement<GetContactResponse> createGetContactResponse(GetContactResponse value) {
        return new JAXBElement<GetContactResponse>(_GetContactResponse_QNAME, GetContactResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetContractLinksResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setContractLinksResponse")
    public JAXBElement<SetContractLinksResponse> createSetContractLinksResponse(SetContractLinksResponse value) {
        return new JAXBElement<SetContractLinksResponse>(_SetContractLinksResponse_QNAME, SetContractLinksResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompletePasswordResetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completePasswordResetResponse")
    public JAXBElement<CompletePasswordResetResponse> createCompletePasswordResetResponse(CompletePasswordResetResponse value) {
        return new JAXBElement<CompletePasswordResetResponse>(_CompletePasswordResetResponse_QNAME, CompletePasswordResetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBankReconciliation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveBankReconciliation")
    public JAXBElement<SaveBankReconciliation> createSaveBankReconciliation(SaveBankReconciliation value) {
        return new JAXBElement<SaveBankReconciliation>(_SaveBankReconciliation_QNAME, SaveBankReconciliation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDistrictsOrCountiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getDistrictsOrCountiesResponse")
    public JAXBElement<GetDistrictsOrCountiesResponse> createGetDistrictsOrCountiesResponse(GetDistrictsOrCountiesResponse value) {
        return new JAXBElement<GetDistrictsOrCountiesResponse>(_GetDistrictsOrCountiesResponse_QNAME, GetDistrictsOrCountiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLedger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getLedger")
    public JAXBElement<GetLedger> createGetLedger(GetLedger value) {
        return new JAXBElement<GetLedger>(_GetLedger_QNAME, GetLedger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionPlan")
    public JAXBElement<GetSubscriptionPlan> createGetSubscriptionPlan(GetSubscriptionPlan value) {
        return new JAXBElement<GetSubscriptionPlan>(_GetSubscriptionPlan_QNAME, GetSubscriptionPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSetting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSetting")
    public JAXBElement<GetSetting> createGetSetting(GetSetting value) {
        return new JAXBElement<GetSetting>(_GetSetting_QNAME, GetSetting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SkipTrial }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "skipTrial")
    public JAXBElement<SkipTrial> createSkipTrial(SkipTrial value) {
        return new JAXBElement<SkipTrial>(_SkipTrial_QNAME, SkipTrial.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getItemResponse")
    public JAXBElement<GetItemResponse> createGetItemResponse(GetItemResponse value) {
        return new JAXBElement<GetItemResponse>(_GetItemResponse_QNAME, GetItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveVatTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveVatTaxInfo")
    public JAXBElement<SaveVatTaxInfo> createSaveVatTaxInfo(SaveVatTaxInfo value) {
        return new JAXBElement<SaveVatTaxInfo>(_SaveVatTaxInfo_QNAME, SaveVatTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHomeCurrencyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getHomeCurrencyResponse")
    public JAXBElement<GetHomeCurrencyResponse> createGetHomeCurrencyResponse(GetHomeCurrencyResponse value) {
        return new JAXBElement<GetHomeCurrencyResponse>(_GetHomeCurrencyResponse_QNAME, GetHomeCurrencyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRecordsForContact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countRecordsForContact")
    public JAXBElement<CountRecordsForContact> createCountRecordsForContact(CountRecordsForContact value) {
        return new JAXBElement<CountRecordsForContact>(_CountRecordsForContact_QNAME, CountRecordsForContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountBankReconciliationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countBankReconciliationsResponse")
    public JAXBElement<CountBankReconciliationsResponse> createCountBankReconciliationsResponse(CountBankReconciliationsResponse value) {
        return new JAXBElement<CountBankReconciliationsResponse>(_CountBankReconciliationsResponse_QNAME, CountBankReconciliationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CanadianTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "canadianTaxInfo")
    public JAXBElement<CanadianTaxInfo> createCanadianTaxInfo(CanadianTaxInfo value) {
        return new JAXBElement<CanadianTaxInfo>(_CanadianTaxInfo_QNAME, CanadianTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContact")
    public JAXBElement<GetContact> createGetContact(GetContact value) {
        return new JAXBElement<GetContact>(_GetContact_QNAME, GetContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addNewCustomer")
    public JAXBElement<AddNewCustomer> createAddNewCustomer(AddNewCustomer value) {
        return new JAXBElement<AddNewCustomer>(_AddNewCustomer_QNAME, AddNewCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemSubcategoryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getItemSubcategoryResponse")
    public JAXBElement<GetItemSubcategoryResponse> createGetItemSubcategoryResponse(GetItemSubcategoryResponse value) {
        return new JAXBElement<GetItemSubcategoryResponse>(_GetItemSubcategoryResponse_QNAME, GetItemSubcategoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCollaborationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCollaborationContext")
    public JAXBElement<GetCollaborationContext> createGetCollaborationContext(GetCollaborationContext value) {
        return new JAXBElement<GetCollaborationContext>(_GetCollaborationContext_QNAME, GetCollaborationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBusinessesForUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBusinessesForUser")
    public JAXBElement<GetBusinessesForUser> createGetBusinessesForUser(GetBusinessesForUser value) {
        return new JAXBElement<GetBusinessesForUser>(_GetBusinessesForUser_QNAME, GetBusinessesForUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countRecordsResponse")
    public JAXBElement<CountRecordsResponse> createCountRecordsResponse(CountRecordsResponse value) {
        return new JAXBElement<CountRecordsResponse>(_CountRecordsResponse_QNAME, CountRecordsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemSubcategory }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getItemSubcategory")
    public JAXBElement<GetItemSubcategory> createGetItemSubcategory(GetItemSubcategory value) {
        return new JAXBElement<GetItemSubcategory>(_GetItemSubcategory_QNAME, GetItemSubcategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveCanadianTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveCanadianTaxInfo")
    public JAXBElement<SaveCanadianTaxInfo> createSaveCanadianTaxInfo(SaveCanadianTaxInfo value) {
        return new JAXBElement<SaveCanadianTaxInfo>(_SaveCanadianTaxInfo_QNAME, SaveCanadianTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDefaultRegion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getDefaultRegion")
    public JAXBElement<GetDefaultRegion> createGetDefaultRegion(GetDefaultRegion value) {
        return new JAXBElement<GetDefaultRegion>(_GetDefaultRegion_QNAME, GetDefaultRegion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PredictRecordNumber }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "predictRecordNumber")
    public JAXBElement<PredictRecordNumber> createPredictRecordNumber(PredictRecordNumber value) {
        return new JAXBElement<PredictRecordNumber>(_PredictRecordNumber_QNAME, PredictRecordNumber.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmailSettingsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getEmailSettingsResponse")
    public JAXBElement<GetEmailSettingsResponse> createGetEmailSettingsResponse(GetEmailSettingsResponse value) {
        return new JAXBElement<GetEmailSettingsResponse>(_GetEmailSettingsResponse_QNAME, GetEmailSettingsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllDistrictsOrCountiesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAllDistrictsOrCountiesResponse")
    public JAXBElement<GetAllDistrictsOrCountiesResponse> createGetAllDistrictsOrCountiesResponse(GetAllDistrictsOrCountiesResponse value) {
        return new JAXBElement<GetAllDistrictsOrCountiesResponse>(_GetAllDistrictsOrCountiesResponse_QNAME, GetAllDistrictsOrCountiesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetContractPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setContractPlanResponse")
    public JAXBElement<SetContractPlanResponse> createSetContractPlanResponse(SetContractPlanResponse value) {
        return new JAXBElement<SetContractPlanResponse>(_SetContractPlanResponse_QNAME, SetContractPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PredictAccountNumber }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "predictAccountNumber")
    public JAXBElement<PredictAccountNumber> createPredictAccountNumber(PredictAccountNumber value) {
        return new JAXBElement<PredictAccountNumber>(_PredictAccountNumber_QNAME, PredictAccountNumber.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestPasswordReset }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "requestPasswordReset")
    public JAXBElement<RequestPasswordReset> createRequestPasswordReset(RequestPasswordReset value) {
        return new JAXBElement<RequestPasswordReset>(_RequestPasswordReset_QNAME, RequestPasswordReset.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtendLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "extendLoginResponse")
    public JAXBElement<ExtendLoginResponse> createExtendLoginResponse(ExtendLoginResponse value) {
        return new JAXBElement<ExtendLoginResponse>(_ExtendLoginResponse_QNAME, ExtendLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContractResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContractResponse")
    public JAXBElement<GetContractResponse> createGetContractResponse(GetContractResponse value) {
        return new JAXBElement<GetContractResponse>(_GetContractResponse_QNAME, GetContractResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkedContacts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getLinkedContacts")
    public JAXBElement<GetLinkedContacts> createGetLinkedContacts(GetLinkedContacts value) {
        return new JAXBElement<GetLinkedContacts>(_GetLinkedContacts_QNAME, GetLinkedContacts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailablePaymentMethodsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAvailablePaymentMethodsResponse")
    public JAXBElement<GetAvailablePaymentMethodsResponse> createGetAvailablePaymentMethodsResponse(GetAvailablePaymentMethodsResponse value) {
        return new JAXBElement<GetAvailablePaymentMethodsResponse>(_GetAvailablePaymentMethodsResponse_QNAME, GetAvailablePaymentMethodsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVendors }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getVendors")
    public JAXBElement<GetVendors> createGetVendors(GetVendors value) {
        return new JAXBElement<GetVendors>(_GetVendors_QNAME, GetVendors.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactsOfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContactsOfType")
    public JAXBElement<GetContactsOfType> createGetContactsOfType(GetContactsOfType value) {
        return new JAXBElement<GetContactsOfType>(_GetContactsOfType_QNAME, GetContactsOfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddBusiness }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addBusiness")
    public JAXBElement<AddBusiness> createAddBusiness(AddBusiness value) {
        return new JAXBElement<AddBusiness>(_AddBusiness_QNAME, AddBusiness.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContacts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContacts")
    public JAXBElement<GetContacts> createGetContacts(GetContacts value) {
        return new JAXBElement<GetContacts>(_GetContacts_QNAME, GetContacts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubcategoriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubcategoriesResponse")
    public JAXBElement<GetSubcategoriesResponse> createGetSubcategoriesResponse(GetSubcategoriesResponse value) {
        return new JAXBElement<GetSubcategoriesResponse>(_GetSubcategoriesResponse_QNAME, GetSubcategoriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionResponse")
    public JAXBElement<GetSubscriptionResponse> createGetSubscriptionResponse(GetSubscriptionResponse value) {
        return new JAXBElement<GetSubscriptionResponse>(_GetSubscriptionResponse_QNAME, GetSubscriptionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestEmailVerificationCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "requestEmailVerificationCode")
    public JAXBElement<RequestEmailVerificationCode> createRequestEmailVerificationCode(RequestEmailVerificationCode value) {
        return new JAXBElement<RequestEmailVerificationCode>(_RequestEmailVerificationCode_QNAME, RequestEmailVerificationCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBusiness }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveBusiness")
    public JAXBElement<SaveBusiness> createSaveBusiness(SaveBusiness value) {
        return new JAXBElement<SaveBusiness>(_SaveBusiness_QNAME, SaveBusiness.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUnpaidRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUnpaidRecordsResponse")
    public JAXBElement<GetUnpaidRecordsResponse> createGetUnpaidRecordsResponse(GetUnpaidRecordsResponse value) {
        return new JAXBElement<GetUnpaidRecordsResponse>(_GetUnpaidRecordsResponse_QNAME, GetUnpaidRecordsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countRecords")
    public JAXBElement<CountRecords> createCountRecords(CountRecords value) {
        return new JAXBElement<CountRecords>(_CountRecords_QNAME, CountRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveContact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveContact")
    public JAXBElement<SaveContact> createSaveContact(SaveContact value) {
        return new JAXBElement<SaveContact>(_SaveContact_QNAME, SaveContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrency }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCurrency")
    public JAXBElement<GetCurrency> createGetCurrency(GetCurrency value) {
        return new JAXBElement<GetCurrency>(_GetCurrency_QNAME, GetCurrency.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getFileResponse")
    public JAXBElement<GetFileResponse> createGetFileResponse(GetFileResponse value) {
        return new JAXBElement<GetFileResponse>(_GetFileResponse_QNAME, GetFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DebitFuturePayAgreementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "debitFuturePayAgreementResponse")
    public JAXBElement<DebitFuturePayAgreementResponse> createDebitFuturePayAgreementResponse(DebitFuturePayAgreementResponse value) {
        return new JAXBElement<DebitFuturePayAgreementResponse>(_DebitFuturePayAgreementResponse_QNAME, DebitFuturePayAgreementResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AppendToAttachmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "appendToAttachmentResponse")
    public JAXBElement<AppendToAttachmentResponse> createAppendToAttachmentResponse(AppendToAttachmentResponse value) {
        return new JAXBElement<AppendToAttachmentResponse>(_AppendToAttachmentResponse_QNAME, AppendToAttachmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSettingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSettingResponse")
    public JAXBElement<GetSettingResponse> createGetSettingResponse(GetSettingResponse value) {
        return new JAXBElement<GetSettingResponse>(_GetSettingResponse_QNAME, GetSettingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOverpaidRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getOverpaidRecordsResponse")
    public JAXBElement<GetOverpaidRecordsResponse> createGetOverpaidRecordsResponse(GetOverpaidRecordsResponse value) {
        return new JAXBElement<GetOverpaidRecordsResponse>(_GetOverpaidRecordsResponse_QNAME, GetOverpaidRecordsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AppendToAttachment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "appendToAttachment")
    public JAXBElement<AppendToAttachment> createAppendToAttachment(AppendToAttachment value) {
        return new JAXBElement<AppendToAttachment>(_AppendToAttachment_QNAME, AppendToAttachment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserByEmailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUserByEmailResponse")
    public JAXBElement<GetUserByEmailResponse> createGetUserByEmailResponse(GetUserByEmailResponse value) {
        return new JAXBElement<GetUserByEmailResponse>(_GetUserByEmailResponse_QNAME, GetUserByEmailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWorldPayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getWorldPayResponse")
    public JAXBElement<GetWorldPayResponse> createGetWorldPayResponse(GetWorldPayResponse value) {
        return new JAXBElement<GetWorldPayResponse>(_GetWorldPayResponse_QNAME, GetWorldPayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContactsResponse")
    public JAXBElement<GetContactsResponse> createGetContactsResponse(GetContactsResponse value) {
        return new JAXBElement<GetContactsResponse>(_GetContactsResponse_QNAME, GetContactsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitFeedbackResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "submitFeedbackResponse")
    public JAXBElement<SubmitFeedbackResponse> createSubmitFeedbackResponse(SubmitFeedbackResponse value) {
        return new JAXBElement<SubmitFeedbackResponse>(_SubmitFeedbackResponse_QNAME, SubmitFeedbackResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUsers")
    public JAXBElement<GetUsers> createGetUsers(GetUsers value) {
        return new JAXBElement<GetUsers>(_GetUsers_QNAME, GetUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendTestEmailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "sendTestEmailResponse")
    public JAXBElement<SendTestEmailResponse> createSendTestEmailResponse(SendTestEmailResponse value) {
        return new JAXBElement<SendTestEmailResponse>(_SendTestEmailResponse_QNAME, SendTestEmailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionsWithContactResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionsWithContactResponse")
    public JAXBElement<GetSubscriptionsWithContactResponse> createGetSubscriptionsWithContactResponse(GetSubscriptionsWithContactResponse value) {
        return new JAXBElement<GetSubscriptionsWithContactResponse>(_GetSubscriptionsWithContactResponse_QNAME, GetSubscriptionsWithContactResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckEmailVerificationCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "checkEmailVerificationCodeResponse")
    public JAXBElement<CheckEmailVerificationCodeResponse> createCheckEmailVerificationCodeResponse(CheckEmailVerificationCodeResponse value) {
        return new JAXBElement<CheckEmailVerificationCodeResponse>(_CheckEmailVerificationCodeResponse_QNAME, CheckEmailVerificationCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addRoleResponse")
    public JAXBElement<AddRoleResponse> createAddRoleResponse(AddRoleResponse value) {
        return new JAXBElement<AddRoleResponse>(_AddRoleResponse_QNAME, AddRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetRoles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setRoles")
    public JAXBElement<SetRoles> createSetRoles(SetRoles value) {
        return new JAXBElement<SetRoles>(_SetRoles_QNAME, SetRoles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalancesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalancesResponse")
    public JAXBElement<GetBalancesResponse> createGetBalancesResponse(GetBalancesResponse value) {
        return new JAXBElement<GetBalancesResponse>(_GetBalancesResponse_QNAME, GetBalancesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveEmailSettings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveEmailSettings")
    public JAXBElement<SaveEmailSettings> createSaveEmailSettings(SaveEmailSettings value) {
        return new JAXBElement<SaveEmailSettings>(_SaveEmailSettings_QNAME, SaveEmailSettings.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionsWithContact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionsWithContact")
    public JAXBElement<GetSubscriptionsWithContact> createGetSubscriptionsWithContact(GetSubscriptionsWithContact value) {
        return new JAXBElement<GetSubscriptionsWithContact>(_GetSubscriptionsWithContact_QNAME, GetSubscriptionsWithContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveCanadianTaxInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveCanadianTaxInfoResponse")
    public JAXBElement<SaveCanadianTaxInfoResponse> createSaveCanadianTaxInfoResponse(SaveCanadianTaxInfoResponse value) {
        return new JAXBElement<SaveCanadianTaxInfoResponse>(_SaveCanadianTaxInfoResponse_QNAME, SaveCanadianTaxInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getItem")
    public JAXBElement<GetItem> createGetItem(GetItem value) {
        return new JAXBElement<GetItem>(_GetItem_QNAME, GetItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRecordsOfTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countRecordsOfTypeResponse")
    public JAXBElement<CountRecordsOfTypeResponse> createCountRecordsOfTypeResponse(CountRecordsOfTypeResponse value) {
        return new JAXBElement<CountRecordsOfTypeResponse>(_CountRecordsOfTypeResponse_QNAME, CountRecordsOfTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveCollaborationEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveCollaborationEventResponse")
    public JAXBElement<SaveCollaborationEventResponse> createSaveCollaborationEventResponse(SaveCollaborationEventResponse value) {
        return new JAXBElement<SaveCollaborationEventResponse>(_SaveCollaborationEventResponse_QNAME, SaveCollaborationEventResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTax }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTax")
    public JAXBElement<GetTax> createGetTax(GetTax value) {
        return new JAXBElement<GetTax>(_GetTax_QNAME, GetTax.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBankReconciliationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveBankReconciliationResponse")
    public JAXBElement<SaveBankReconciliationResponse> createSaveBankReconciliationResponse(SaveBankReconciliationResponse value) {
        return new JAXBElement<SaveBankReconciliationResponse>(_SaveBankReconciliationResponse_QNAME, SaveBankReconciliationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsOfType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecordsOfType")
    public JAXBElement<GetRecordsOfType> createGetRecordsOfType(GetRecordsOfType value) {
        return new JAXBElement<GetRecordsOfType>(_GetRecordsOfType_QNAME, GetRecordsOfType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountChangesets }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countChangesets")
    public JAXBElement<CountChangesets> createCountChangesets(CountChangesets value) {
        return new JAXBElement<CountChangesets>(_CountChangesets_QNAME, CountChangesets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetTaxRate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setTaxRate")
    public JAXBElement<SetTaxRate> createSetTaxRate(SetTaxRate value) {
        return new JAXBElement<SetTaxRate>(_SetTaxRate_QNAME, SetTaxRate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetryContractPayment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "retryContractPayment")
    public JAXBElement<RetryContractPayment> createRetryContractPayment(RetryContractPayment value) {
        return new JAXBElement<RetryContractPayment>(_RetryContractPayment_QNAME, RetryContractPayment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SkipTrialResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "skipTrialResponse")
    public JAXBElement<SkipTrialResponse> createSkipTrialResponse(SkipTrialResponse value) {
        return new JAXBElement<SkipTrialResponse>(_SkipTrialResponse_QNAME, SkipTrialResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBusiness }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBusiness")
    public JAXBElement<GetBusiness> createGetBusiness(GetBusiness value) {
        return new JAXBElement<GetBusiness>(_GetBusiness_QNAME, GetBusiness.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLedgerLengthResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getLedgerLengthResponse")
    public JAXBElement<GetLedgerLengthResponse> createGetLedgerLengthResponse(GetLedgerLengthResponse value) {
        return new JAXBElement<GetLedgerLengthResponse>(_GetLedgerLengthResponse_QNAME, GetLedgerLengthResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getFile")
    public JAXBElement<GetFile> createGetFile(GetFile value) {
        return new JAXBElement<GetFile>(_GetFile_QNAME, GetFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBasicTaxInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveBasicTaxInfoResponse")
    public JAXBElement<SaveBasicTaxInfoResponse> createSaveBasicTaxInfoResponse(SaveBasicTaxInfoResponse value) {
        return new JAXBElement<SaveBasicTaxInfoResponse>(_SaveBasicTaxInfoResponse_QNAME, SaveBasicTaxInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAutomaticMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAutomaticMessageResponse")
    public JAXBElement<GetAutomaticMessageResponse> createGetAutomaticMessageResponse(GetAutomaticMessageResponse value) {
        return new JAXBElement<GetAutomaticMessageResponse>(_GetAutomaticMessageResponse_QNAME, GetAutomaticMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetItemsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getItemsResponse")
    public JAXBElement<GetItemsResponse> createGetItemsResponse(GetItemsResponse value) {
        return new JAXBElement<GetItemsResponse>(_GetItemsResponse_QNAME, GetItemsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StoreFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "storeFile")
    public JAXBElement<StoreFile> createStoreFile(StoreFile value) {
        return new JAXBElement<StoreFile>(_StoreFile_QNAME, StoreFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalances }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalances")
    public JAXBElement<GetBalances> createGetBalances(GetBalances value) {
        return new JAXBElement<GetBalances>(_GetBalances_QNAME, GetBalances.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveUser")
    public JAXBElement<SaveUser> createSaveUser(SaveUser value) {
        return new JAXBElement<SaveUser>(_SaveUser_QNAME, SaveUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveRecord")
    public JAXBElement<SaveRecord> createSaveRecord(SaveRecord value) {
        return new JAXBElement<SaveRecord>(_SaveRecord_QNAME, SaveRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtendLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "extendLogin")
    public JAXBElement<ExtendLogin> createExtendLogin(ExtendLogin value) {
        return new JAXBElement<ExtendLogin>(_ExtendLogin_QNAME, ExtendLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotActivated }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "NotActivated")
    public JAXBElement<NotActivated> createNotActivated(NotActivated value) {
        return new JAXBElement<NotActivated>(_NotActivated_QNAME, NotActivated.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AppendToFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "appendToFile")
    public JAXBElement<AppendToFile> createAppendToFile(AppendToFile value) {
        return new JAXBElement<AppendToFile>(_AppendToFile_QNAME, AppendToFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBasicTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveBasicTaxInfo")
    public JAXBElement<SaveBasicTaxInfo> createSaveBasicTaxInfo(SaveBasicTaxInfo value) {
        return new JAXBElement<SaveBasicTaxInfo>(_SaveBasicTaxInfo_QNAME, SaveBasicTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PredictRecordNumberResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "predictRecordNumberResponse")
    public JAXBElement<PredictRecordNumberResponse> createPredictRecordNumberResponse(PredictRecordNumberResponse value) {
        return new JAXBElement<PredictRecordNumberResponse>(_PredictRecordNumberResponse_QNAME, PredictRecordNumberResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DecodeAuthTokenResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "decodeAuthTokenResponse")
    public JAXBElement<DecodeAuthTokenResponse> createDecodeAuthTokenResponse(DecodeAuthTokenResponse value) {
        return new JAXBElement<DecodeAuthTokenResponse>(_DecodeAuthTokenResponse_QNAME, DecodeAuthTokenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadAttachmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "readAttachmentResponse")
    public JAXBElement<ReadAttachmentResponse> createReadAttachmentResponse(ReadAttachmentResponse value) {
        return new JAXBElement<ReadAttachmentResponse>(_ReadAttachmentResponse_QNAME, ReadAttachmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeUserPasswordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "changeUserPasswordResponse")
    public JAXBElement<ChangeUserPasswordResponse> createChangeUserPasswordResponse(ChangeUserPasswordResponse value) {
        return new JAXBElement<ChangeUserPasswordResponse>(_ChangeUserPasswordResponse_QNAME, ChangeUserPasswordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrencyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCurrencyResponse")
    public JAXBElement<GetCurrencyResponse> createGetCurrencyResponse(GetCurrencyResponse value) {
        return new JAXBElement<GetCurrencyResponse>(_GetCurrencyResponse_QNAME, GetCurrencyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addUserResponse")
    public JAXBElement<AddUserResponse> createAddUserResponse(AddUserResponse value) {
        return new JAXBElement<AddUserResponse>(_AddUserResponse_QNAME, AddUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveRecordResponse")
    public JAXBElement<SaveRecordResponse> createSaveRecordResponse(SaveRecordResponse value) {
        return new JAXBElement<SaveRecordResponse>(_SaveRecordResponse_QNAME, SaveRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addUser")
    public JAXBElement<AddUser> createAddUser(AddUser value) {
        return new JAXBElement<AddUser>(_AddUser_QNAME, AddUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttachmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAttachmentResponse")
    public JAXBElement<GetAttachmentResponse> createGetAttachmentResponse(GetAttachmentResponse value) {
        return new JAXBElement<GetAttachmentResponse>(_GetAttachmentResponse_QNAME, GetAttachmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCustomers")
    public JAXBElement<GetCustomers> createGetCustomers(GetCustomers value) {
        return new JAXBElement<GetCustomers>(_GetCustomers_QNAME, GetCustomers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveSubscriptionPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveSubscriptionPlan")
    public JAXBElement<SaveSubscriptionPlan> createSaveSubscriptionPlan(SaveSubscriptionPlan value) {
        return new JAXBElement<SaveSubscriptionPlan>(_SaveSubscriptionPlan_QNAME, SaveSubscriptionPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatementOpeningBalances }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getStatementOpeningBalances")
    public JAXBElement<GetStatementOpeningBalances> createGetStatementOpeningBalances(GetStatementOpeningBalances value) {
        return new JAXBElement<GetStatementOpeningBalances>(_GetStatementOpeningBalances_QNAME, GetStatementOpeningBalances.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetExchangeRate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getExchangeRate")
    public JAXBElement<GetExchangeRate> createGetExchangeRate(GetExchangeRate value) {
        return new JAXBElement<GetExchangeRate>(_GetExchangeRate_QNAME, GetExchangeRate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotAllowed }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "NotAllowed")
    public JAXBElement<NotAllowed> createNotAllowed(NotAllowed value) {
        return new JAXBElement<NotAllowed>(_NotAllowed_QNAME, NotAllowed.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveWorldPayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveWorldPayResponse")
    public JAXBElement<SaveWorldPayResponse> createSaveWorldPayResponse(SaveWorldPayResponse value) {
        return new JAXBElement<SaveWorldPayResponse>(_SaveWorldPayResponse_QNAME, SaveWorldPayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountBankReconciliations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countBankReconciliations")
    public JAXBElement<CountBankReconciliations> createCountBankReconciliations(CountBankReconciliations value) {
        return new JAXBElement<CountBankReconciliations>(_CountBankReconciliations_QNAME, CountBankReconciliations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalance")
    public JAXBElement<GetBalance> createGetBalance(GetBalance value) {
        return new JAXBElement<GetBalance>(_GetBalance_QNAME, GetBalance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEmailSettings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getEmailSettings")
    public JAXBElement<GetEmailSettings> createGetEmailSettings(GetEmailSettings value) {
        return new JAXBElement<GetEmailSettings>(_GetEmailSettings_QNAME, GetEmailSettings.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionPlans }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionPlans")
    public JAXBElement<GetSubscriptionPlans> createGetSubscriptionPlans(GetSubscriptionPlans value) {
        return new JAXBElement<GetSubscriptionPlans>(_GetSubscriptionPlans_QNAME, GetSubscriptionPlans.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveAutomaticMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveAutomaticMessage")
    public JAXBElement<SaveAutomaticMessage> createSaveAutomaticMessage(SaveAutomaticMessage value) {
        return new JAXBElement<SaveAutomaticMessage>(_SaveAutomaticMessage_QNAME, SaveAutomaticMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxResponse")
    public JAXBElement<GetTaxResponse> createGetTaxResponse(GetTaxResponse value) {
        return new JAXBElement<GetTaxResponse>(_GetTaxResponse_QNAME, GetTaxResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMyUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getMyUserResponse")
    public JAXBElement<GetMyUserResponse> createGetMyUserResponse(GetMyUserResponse value) {
        return new JAXBElement<GetMyUserResponse>(_GetMyUserResponse_QNAME, GetMyUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableContractPlansResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAvailableContractPlansResponse")
    public JAXBElement<GetAvailableContractPlansResponse> createGetAvailableContractPlansResponse(GetAvailableContractPlansResponse value) {
        return new JAXBElement<GetAvailableContractPlansResponse>(_GetAvailableContractPlansResponse_QNAME, GetAvailableContractPlansResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFuturePaySetupUrlResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getFuturePaySetupUrlResponse")
    public JAXBElement<GetFuturePaySetupUrlResponse> createGetFuturePaySetupUrlResponse(GetFuturePaySetupUrlResponse value) {
        return new JAXBElement<GetFuturePaySetupUrlResponse>(_GetFuturePaySetupUrlResponse_QNAME, GetFuturePaySetupUrlResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalanceAtDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalanceAtDateResponse")
    public JAXBElement<GetBalanceAtDateResponse> createGetBalanceAtDateResponse(GetBalanceAtDateResponse value) {
        return new JAXBElement<GetBalanceAtDateResponse>(_GetBalanceAtDateResponse_QNAME, GetBalanceAtDateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRegion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRegion")
    public JAXBElement<GetRegion> createGetRegion(GetRegion value) {
        return new JAXBElement<GetRegion>(_GetRegion_QNAME, GetRegion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCanadianTaxInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCanadianTaxInfoResponse")
    public JAXBElement<GetCanadianTaxInfoResponse> createGetCanadianTaxInfoResponse(GetCanadianTaxInfoResponse value) {
        return new JAXBElement<GetCanadianTaxInfoResponse>(_GetCanadianTaxInfoResponse_QNAME, GetCanadianTaxInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProvincesOrStatesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getProvincesOrStatesResponse")
    public JAXBElement<GetProvincesOrStatesResponse> createGetProvincesOrStatesResponse(GetProvincesOrStatesResponse value) {
        return new JAXBElement<GetProvincesOrStatesResponse>(_GetProvincesOrStatesResponse_QNAME, GetProvincesOrStatesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteAttachment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completeAttachment")
    public JAXBElement<CompleteAttachment> createCompleteAttachment(CompleteAttachment value) {
        return new JAXBElement<CompleteAttachment>(_CompleteAttachment_QNAME, CompleteAttachment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountRecordsForContactResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countRecordsForContactResponse")
    public JAXBElement<CountRecordsForContactResponse> createCountRecordsForContactResponse(CountRecordsForContactResponse value) {
        return new JAXBElement<CountRecordsForContactResponse>(_CountRecordsForContactResponse_QNAME, CountRecordsForContactResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestAutomaticMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "testAutomaticMessage")
    public JAXBElement<TestAutomaticMessage> createTestAutomaticMessage(TestAutomaticMessage value) {
        return new JAXBElement<TestAutomaticMessage>(_TestAutomaticMessage_QNAME, TestAutomaticMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUser")
    public JAXBElement<GetUser> createGetUser(GetUser value) {
        return new JAXBElement<GetUser>(_GetUser_QNAME, GetUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completeFileResponse")
    public JAXBElement<CompleteFileResponse> createCompleteFileResponse(CompleteFileResponse value) {
        return new JAXBElement<CompleteFileResponse>(_CompleteFileResponse_QNAME, CompleteFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDistrictsOrCounties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getDistrictsOrCounties")
    public JAXBElement<GetDistrictsOrCounties> createGetDistrictsOrCounties(GetDistrictsOrCounties value) {
        return new JAXBElement<GetDistrictsOrCounties>(_GetDistrictsOrCounties_QNAME, GetDistrictsOrCounties.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAccountsResponse")
    public JAXBElement<GetAccountsResponse> createGetAccountsResponse(GetAccountsResponse value) {
        return new JAXBElement<GetAccountsResponse>(_GetAccountsResponse_QNAME, GetAccountsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAutomaticMessagesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAutomaticMessagesResponse")
    public JAXBElement<GetAutomaticMessagesResponse> createGetAutomaticMessagesResponse(GetAutomaticMessagesResponse value) {
        return new JAXBElement<GetAutomaticMessagesResponse>(_GetAutomaticMessagesResponse_QNAME, GetAutomaticMessagesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetJournalEntries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getJournalEntries")
    public JAXBElement<GetJournalEntries> createGetJournalEntries(GetJournalEntries value) {
        return new JAXBElement<GetJournalEntries>(_GetJournalEntries_QNAME, GetJournalEntries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAccountByName")
    public JAXBElement<GetAccountByName> createGetAccountByName(GetAccountByName value) {
        return new JAXBElement<GetAccountByName>(_GetAccountByName_QNAME, GetAccountByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBankReconciliation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBankReconciliation")
    public JAXBElement<GetBankReconciliation> createGetBankReconciliation(GetBankReconciliation value) {
        return new JAXBElement<GetBankReconciliation>(_GetBankReconciliation_QNAME, GetBankReconciliation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoLogout }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "doLogout")
    public JAXBElement<DoLogout> createDoLogout(DoLogout value) {
        return new JAXBElement<DoLogout>(_DoLogout_QNAME, DoLogout.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimeZonesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTimeZonesResponse")
    public JAXBElement<GetTimeZonesResponse> createGetTimeZonesResponse(GetTimeZonesResponse value) {
        return new JAXBElement<GetTimeZonesResponse>(_GetTimeZonesResponse_QNAME, GetTimeZonesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendTestEmail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "sendTestEmail")
    public JAXBElement<SendTestEmail> createSendTestEmail(SendTestEmail value) {
        return new JAXBElement<SendTestEmail>(_SendTestEmail_QNAME, SendTestEmail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddAttachment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addAttachment")
    public JAXBElement<AddAttachment> createAddAttachment(AddAttachment value) {
        return new JAXBElement<AddAttachment>(_AddAttachment_QNAME, AddAttachment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetExchangeRateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getExchangeRateResponse")
    public JAXBElement<GetExchangeRateResponse> createGetExchangeRateResponse(GetExchangeRateResponse value) {
        return new JAXBElement<GetExchangeRateResponse>(_GetExchangeRateResponse_QNAME, GetExchangeRateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllProvincesOrStates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAllProvincesOrStates")
    public JAXBElement<GetAllProvincesOrStates> createGetAllProvincesOrStates(GetAllProvincesOrStates value) {
        return new JAXBElement<GetAllProvincesOrStates>(_GetAllProvincesOrStates_QNAME, GetAllProvincesOrStates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoLogoutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "doLogoutResponse")
    public JAXBElement<DoLogoutResponse> createDoLogoutResponse(DoLogoutResponse value) {
        return new JAXBElement<DoLogoutResponse>(_DoLogoutResponse_QNAME, DoLogoutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionsWithPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionsWithPlan")
    public JAXBElement<GetSubscriptionsWithPlan> createGetSubscriptionsWithPlan(GetSubscriptionsWithPlan value) {
        return new JAXBElement<GetSubscriptionsWithPlan>(_GetSubscriptionsWithPlan_QNAME, GetSubscriptionsWithPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTax }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addTax")
    public JAXBElement<AddTax> createAddTax(AddTax value) {
        return new JAXBElement<AddTax>(_AddTax_QNAME, AddTax.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncodeAuthToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "encodeAuthToken")
    public JAXBElement<EncodeAuthToken> createEncodeAuthToken(EncodeAuthToken value) {
        return new JAXBElement<EncodeAuthToken>(_EncodeAuthToken_QNAME, EncodeAuthToken.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkedContactsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getLinkedContactsResponse")
    public JAXBElement<GetLinkedContactsResponse> createGetLinkedContactsResponse(GetLinkedContactsResponse value) {
        return new JAXBElement<GetLinkedContactsResponse>(_GetLinkedContactsResponse_QNAME, GetLinkedContactsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUsersResponse")
    public JAXBElement<GetUsersResponse> createGetUsersResponse(GetUsersResponse value) {
        return new JAXBElement<GetUsersResponse>(_GetUsersResponse_QNAME, GetUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsForContactResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecordsForContactResponse")
    public JAXBElement<GetRecordsForContactResponse> createGetRecordsForContactResponse(GetRecordsForContactResponse value) {
        return new JAXBElement<GetRecordsForContactResponse>(_GetRecordsForContactResponse_QNAME, GetRecordsForContactResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VatTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "vatTaxInfo")
    public JAXBElement<VatTaxInfo> createVatTaxInfo(VatTaxInfo value) {
        return new JAXBElement<VatTaxInfo>(_VatTaxInfo_QNAME, VatTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecords")
    public JAXBElement<GetRecords> createGetRecords(GetRecords value) {
        return new JAXBElement<GetRecords>(_GetRecords_QNAME, GetRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllItems }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAllItems")
    public JAXBElement<GetAllItems> createGetAllItems(GetAllItems value) {
        return new JAXBElement<GetAllItems>(_GetAllItems_QNAME, GetAllItems.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveSubscriptionPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveSubscriptionPlanResponse")
    public JAXBElement<SaveSubscriptionPlanResponse> createSaveSubscriptionPlanResponse(SaveSubscriptionPlanResponse value) {
        return new JAXBElement<SaveSubscriptionPlanResponse>(_SaveSubscriptionPlanResponse_QNAME, SaveSubscriptionPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttachment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAttachment")
    public JAXBElement<GetAttachment> createGetAttachment(GetAttachment value) {
        return new JAXBElement<GetAttachment>(_GetAttachment_QNAME, GetAttachment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AlreadyActivated }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "AlreadyActivated")
    public JAXBElement<AlreadyActivated> createAlreadyActivated(AlreadyActivated value) {
        return new JAXBElement<AlreadyActivated>(_AlreadyActivated_QNAME, AlreadyActivated.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsOfTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecordsOfTypeResponse")
    public JAXBElement<GetRecordsOfTypeResponse> createGetRecordsOfTypeResponse(GetRecordsOfTypeResponse value) {
        return new JAXBElement<GetRecordsOfTypeResponse>(_GetRecordsOfTypeResponse_QNAME, GetRecordsOfTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBusinessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBusinessResponse")
    public JAXBElement<GetBusinessResponse> createGetBusinessResponse(GetBusinessResponse value) {
        return new JAXBElement<GetBusinessResponse>(_GetBusinessResponse_QNAME, GetBusinessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DebitFuturePayAgreement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "debitFuturePayAgreement")
    public JAXBElement<DebitFuturePayAgreement> createDebitFuturePayAgreement(DebitFuturePayAgreement value) {
        return new JAXBElement<DebitFuturePayAgreement>(_DebitFuturePayAgreement_QNAME, DebitFuturePayAgreement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalancesAtDate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalancesAtDate")
    public JAXBElement<GetBalancesAtDate> createGetBalancesAtDate(GetBalancesAtDate value) {
        return new JAXBElement<GetBalancesAtDate>(_GetBalancesAtDate_QNAME, GetBalancesAtDate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxCodes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxCodes")
    public JAXBElement<GetTaxCodes> createGetTaxCodes(GetTaxCodes value) {
        return new JAXBElement<GetTaxCodes>(_GetTaxCodes_QNAME, GetTaxCodes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllDistrictsOrCounties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAllDistrictsOrCounties")
    public JAXBElement<GetAllDistrictsOrCounties> createGetAllDistrictsOrCounties(GetAllDistrictsOrCounties value) {
        return new JAXBElement<GetAllDistrictsOrCounties>(_GetAllDistrictsOrCounties_QNAME, GetAllDistrictsOrCounties.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitFeedback }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "submitFeedback")
    public JAXBElement<SubmitFeedback> createSubmitFeedback(SubmitFeedback value) {
        return new JAXBElement<SubmitFeedback>(_SubmitFeedback_QNAME, SubmitFeedback.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestAutomaticMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "testAutomaticMessageResponse")
    public JAXBElement<TestAutomaticMessageResponse> createTestAutomaticMessageResponse(TestAutomaticMessageResponse value) {
        return new JAXBElement<TestAutomaticMessageResponse>(_TestAutomaticMessageResponse_QNAME, TestAutomaticMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalanceResponse")
    public JAXBElement<GetBalanceResponse> createGetBalanceResponse(GetBalanceResponse value) {
        return new JAXBElement<GetBalanceResponse>(_GetBalanceResponse_QNAME, GetBalanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionPlanResponse")
    public JAXBElement<GetSubscriptionPlanResponse> createGetSubscriptionPlanResponse(GetSubscriptionPlanResponse value) {
        return new JAXBElement<GetSubscriptionPlanResponse>(_GetSubscriptionPlanResponse_QNAME, GetSubscriptionPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllItemsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAllItemsResponse")
    public JAXBElement<GetAllItemsResponse> createGetAllItemsResponse(GetAllItemsResponse value) {
        return new JAXBElement<GetAllItemsResponse>(_GetAllItemsResponse_QNAME, GetAllItemsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrencyByCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCurrencyByCode")
    public JAXBElement<GetCurrencyByCode> createGetCurrencyByCode(GetCurrencyByCode value) {
        return new JAXBElement<GetCurrencyByCode>(_GetCurrencyByCode_QNAME, GetCurrencyByCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindRegionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "findRegionResponse")
    public JAXBElement<FindRegionResponse> createFindRegionResponse(FindRegionResponse value) {
        return new JAXBElement<FindRegionResponse>(_FindRegionResponse_QNAME, FindRegionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveAccount")
    public JAXBElement<SaveAccount> createSaveAccount(SaveAccount value) {
        return new JAXBElement<SaveAccount>(_SaveAccount_QNAME, SaveAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFuturePayAgreement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getFuturePayAgreement")
    public JAXBElement<GetFuturePayAgreement> createGetFuturePayAgreement(GetFuturePayAgreement value) {
        return new JAXBElement<GetFuturePayAgreement>(_GetFuturePayAgreement_QNAME, GetFuturePayAgreement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMyBusinessesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getMyBusinessesResponse")
    public JAXBElement<GetMyBusinessesResponse> createGetMyBusinessesResponse(GetMyBusinessesResponse value) {
        return new JAXBElement<GetMyBusinessesResponse>(_GetMyBusinessesResponse_QNAME, GetMyBusinessesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetChangesetsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getChangesetsResponse")
    public JAXBElement<GetChangesetsResponse> createGetChangesetsResponse(GetChangesetsResponse value) {
        return new JAXBElement<GetChangesetsResponse>(_GetChangesetsResponse_QNAME, GetChangesetsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidationFailed }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "ValidationFailed")
    public JAXBElement<ValidationFailed> createValidationFailed(ValidationFailed value) {
        return new JAXBElement<ValidationFailed>(_ValidationFailed_QNAME, ValidationFailed.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestPasswordResetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "requestPasswordResetResponse")
    public JAXBElement<RequestPasswordResetResponse> createRequestPasswordResetResponse(RequestPasswordResetResponse value) {
        return new JAXBElement<RequestPasswordResetResponse>(_RequestPasswordResetResponse_QNAME, RequestPasswordResetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContract }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContract")
    public JAXBElement<GetContract> createGetContract(GetContract value) {
        return new JAXBElement<GetContract>(_GetContract_QNAME, GetContract.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncodeAuthTokenResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "encodeAuthTokenResponse")
    public JAXBElement<EncodeAuthTokenResponse> createEncodeAuthTokenResponse(EncodeAuthTokenResponse value) {
        return new JAXBElement<EncodeAuthTokenResponse>(_EncodeAuthTokenResponse_QNAME, EncodeAuthTokenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getRecordsResponse")
    public JAXBElement<GetRecordsResponse> createGetRecordsResponse(GetRecordsResponse value) {
        return new JAXBElement<GetRecordsResponse>(_GetRecordsResponse_QNAME, GetRecordsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveUserResponse")
    public JAXBElement<SaveUserResponse> createSaveUserResponse(SaveUserResponse value) {
        return new JAXBElement<SaveUserResponse>(_SaveUserResponse_QNAME, SaveUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactsOfTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getContactsOfTypeResponse")
    public JAXBElement<GetContactsOfTypeResponse> createGetContactsOfTypeResponse(GetContactsOfTypeResponse value) {
        return new JAXBElement<GetContactsOfTypeResponse>(_GetContactsOfTypeResponse_QNAME, GetContactsOfTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxCodesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxCodesResponse")
    public JAXBElement<GetTaxCodesResponse> createGetTaxCodesResponse(GetTaxCodesResponse value) {
        return new JAXBElement<GetTaxCodesResponse>(_GetTaxCodesResponse_QNAME, GetTaxCodesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscription }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscription")
    public JAXBElement<GetSubscription> createGetSubscription(GetSubscription value) {
        return new JAXBElement<GetSubscription>(_GetSubscription_QNAME, GetSubscription.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAccountResponse")
    public JAXBElement<GetAccountResponse> createGetAccountResponse(GetAccountResponse value) {
        return new JAXBElement<GetAccountResponse>(_GetAccountResponse_QNAME, GetAccountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrencyByCodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCurrencyByCodeResponse")
    public JAXBElement<GetCurrencyByCodeResponse> createGetCurrencyByCodeResponse(GetCurrencyByCodeResponse value) {
        return new JAXBElement<GetCurrencyByCodeResponse>(_GetCurrencyByCodeResponse_QNAME, GetCurrencyByCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLedgerLength }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getLedgerLength")
    public JAXBElement<GetLedgerLength> createGetLedgerLength(GetLedgerLength value) {
        return new JAXBElement<GetLedgerLength>(_GetLedgerLength_QNAME, GetLedgerLength.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveItemResponse")
    public JAXBElement<SaveItemResponse> createSaveItemResponse(SaveItemResponse value) {
        return new JAXBElement<SaveItemResponse>(_SaveItemResponse_QNAME, SaveItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetContractPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setContractPlan")
    public JAXBElement<SetContractPlan> createSetContractPlan(SetContractPlan value) {
        return new JAXBElement<SetContractPlan>(_SetContractPlan_QNAME, SetContractPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "doLoginResponse")
    public JAXBElement<DoLoginResponse> createDoLoginResponse(DoLoginResponse value) {
        return new JAXBElement<DoLoginResponse>(_DoLoginResponse_QNAME, DoLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveTaxCodes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveTaxCodes")
    public JAXBElement<SaveTaxCodes> createSaveTaxCodes(SaveTaxCodes value) {
        return new JAXBElement<SaveTaxCodes>(_SaveTaxCodes_QNAME, SaveTaxCodes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxesResponse")
    public JAXBElement<GetTaxesResponse> createGetTaxesResponse(GetTaxesResponse value) {
        return new JAXBElement<GetTaxesResponse>(_GetTaxesResponse_QNAME, GetTaxesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBusinessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveBusinessResponse")
    public JAXBElement<SaveBusinessResponse> createSaveBusinessResponse(SaveBusinessResponse value) {
        return new JAXBElement<SaveBusinessResponse>(_SaveBusinessResponse_QNAME, SaveBusinessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTaxResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addTaxResponse")
    public JAXBElement<AddTaxResponse> createAddTaxResponse(AddTaxResponse value) {
        return new JAXBElement<AddTaxResponse>(_AddTaxResponse_QNAME, AddTaxResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimeInZone }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTimeInZone")
    public JAXBElement<GetTimeInZone> createGetTimeInZone(GetTimeInZone value) {
        return new JAXBElement<GetTimeInZone>(_GetTimeInZone_QNAME, GetTimeInZone.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StoreFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "storeFileResponse")
    public JAXBElement<StoreFileResponse> createStoreFileResponse(StoreFileResponse value) {
        return new JAXBElement<StoreFileResponse>(_StoreFileResponse_QNAME, StoreFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveAutomaticMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveAutomaticMessageResponse")
    public JAXBElement<SaveAutomaticMessageResponse> createSaveAutomaticMessageResponse(SaveAutomaticMessageResponse value) {
        return new JAXBElement<SaveAutomaticMessageResponse>(_SaveAutomaticMessageResponse_QNAME, SaveAutomaticMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrenciesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCurrenciesResponse")
    public JAXBElement<GetCurrenciesResponse> createGetCurrenciesResponse(GetCurrenciesResponse value) {
        return new JAXBElement<GetCurrenciesResponse>(_GetCurrenciesResponse_QNAME, GetCurrenciesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConcurrentChangeException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "ConcurrentChangeException")
    public JAXBElement<ConcurrentChangeException> createConcurrentChangeException(ConcurrentChangeException value) {
        return new JAXBElement<ConcurrentChangeException>(_ConcurrentChangeException_QNAME, ConcurrentChangeException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBalancesAtDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBalancesAtDateResponse")
    public JAXBElement<GetBalancesAtDateResponse> createGetBalancesAtDateResponse(GetBalancesAtDateResponse value) {
        return new JAXBElement<GetBalancesAtDateResponse>(_GetBalancesAtDateResponse_QNAME, GetBalancesAtDateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccounts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAccounts")
    public JAXBElement<GetAccounts> createGetAccounts(GetAccounts value) {
        return new JAXBElement<GetAccounts>(_GetAccounts_QNAME, GetAccounts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPublicSettingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getPublicSettingResponse")
    public JAXBElement<GetPublicSettingResponse> createGetPublicSettingResponse(GetPublicSettingResponse value) {
        return new JAXBElement<GetPublicSettingResponse>(_GetPublicSettingResponse_QNAME, GetPublicSettingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveAccountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveAccountResponse")
    public JAXBElement<SaveAccountResponse> createSaveAccountResponse(SaveAccountResponse value) {
        return new JAXBElement<SaveAccountResponse>(_SaveAccountResponse_QNAME, SaveAccountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignUpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "signUpResponse")
    public JAXBElement<SignUpResponse> createSignUpResponse(SignUpResponse value) {
        return new JAXBElement<SignUpResponse>(_SignUpResponse_QNAME, SignUpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPublicSetting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getPublicSetting")
    public JAXBElement<GetPublicSetting> createGetPublicSetting(GetPublicSetting value) {
        return new JAXBElement<GetPublicSetting>(_GetPublicSetting_QNAME, GetPublicSetting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountSubscriptionsWithPlan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countSubscriptionsWithPlan")
    public JAXBElement<CountSubscriptionsWithPlan> createCountSubscriptionsWithPlan(CountSubscriptionsWithPlan value) {
        return new JAXBElement<CountSubscriptionsWithPlan>(_CountSubscriptionsWithPlan_QNAME, CountSubscriptionsWithPlan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUnpaidRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUnpaidRecords")
    public JAXBElement<GetUnpaidRecords> createGetUnpaidRecords(GetUnpaidRecords value) {
        return new JAXBElement<GetUnpaidRecords>(_GetUnpaidRecords_QNAME, GetUnpaidRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAccount")
    public JAXBElement<GetAccount> createGetAccount(GetAccount value) {
        return new JAXBElement<GetAccount>(_GetAccount_QNAME, GetAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteSignUp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completeSignUp")
    public JAXBElement<CompleteSignUp> createCompleteSignUp(CompleteSignUp value) {
        return new JAXBElement<CompleteSignUp>(_CompleteSignUp_QNAME, CompleteSignUp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addRole")
    public JAXBElement<AddRole> createAddRole(AddRole value) {
        return new JAXBElement<AddRole>(_AddRole_QNAME, AddRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxRateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxRateResponse")
    public JAXBElement<GetTaxRateResponse> createGetTaxRateResponse(GetTaxRateResponse value) {
        return new JAXBElement<GetTaxRateResponse>(_GetTaxRateResponse_QNAME, GetTaxRateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUnclearedTransactions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getUnclearedTransactions")
    public JAXBElement<GetUnclearedTransactions> createGetUnclearedTransactions(GetUnclearedTransactions value) {
        return new JAXBElement<GetUnclearedTransactions>(_GetUnclearedTransactions_QNAME, GetUnclearedTransactions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCanadianTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCanadianTaxInfo")
    public JAXBElement<GetCanadianTaxInfo> createGetCanadianTaxInfo(GetCanadianTaxInfo value) {
        return new JAXBElement<GetCanadianTaxInfo>(_GetCanadianTaxInfo_QNAME, GetCanadianTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOverpaidRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getOverpaidRecords")
    public JAXBElement<GetOverpaidRecords> createGetOverpaidRecords(GetOverpaidRecords value) {
        return new JAXBElement<GetOverpaidRecords>(_GetOverpaidRecords_QNAME, GetOverpaidRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSiteTitle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSiteTitle")
    public JAXBElement<GetSiteTitle> createGetSiteTitle(GetSiteTitle value) {
        return new JAXBElement<GetSiteTitle>(_GetSiteTitle_QNAME, GetSiteTitle.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveItem")
    public JAXBElement<SaveItem> createSaveItem(SaveItem value) {
        return new JAXBElement<SaveItem>(_SaveItem_QNAME, SaveItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completeFile")
    public JAXBElement<CompleteFile> createCompleteFile(CompleteFile value) {
        return new JAXBElement<CompleteFile>(_CompleteFile_QNAME, CompleteFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableContractPlans }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAvailableContractPlans")
    public JAXBElement<GetAvailableContractPlans> createGetAvailableContractPlans(GetAvailableContractPlans value) {
        return new JAXBElement<GetAvailableContractPlans>(_GetAvailableContractPlans_QNAME, GetAvailableContractPlans.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBankReconciliationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getBankReconciliationsResponse")
    public JAXBElement<GetBankReconciliationsResponse> createGetBankReconciliationsResponse(GetBankReconciliationsResponse value) {
        return new JAXBElement<GetBankReconciliationsResponse>(_GetBankReconciliationsResponse_QNAME, GetBankReconciliationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimeInZoneResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTimeInZoneResponse")
    public JAXBElement<GetTimeInZoneResponse> createGetTimeInZoneResponse(GetTimeInZoneResponse value) {
        return new JAXBElement<GetTimeInZoneResponse>(_GetTimeInZoneResponse_QNAME, GetTimeInZoneResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountChangesetsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countChangesetsResponse")
    public JAXBElement<CountChangesetsResponse> createCountChangesetsResponse(CountChangesetsResponse value) {
        return new JAXBElement<CountChangesetsResponse>(_CountChangesetsResponse_QNAME, CountChangesetsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompletePasswordReset }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "completePasswordReset")
    public JAXBElement<CompletePasswordReset> createCompletePasswordReset(CompletePasswordReset value) {
        return new JAXBElement<CompletePasswordReset>(_CompletePasswordReset_QNAME, CompletePasswordReset.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHomeCurrency }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getHomeCurrency")
    public JAXBElement<GetHomeCurrency> createGetHomeCurrency(GetHomeCurrency value) {
        return new JAXBElement<GetHomeCurrency>(_GetHomeCurrency_QNAME, GetHomeCurrency.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveSubscription }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveSubscription")
    public JAXBElement<SaveSubscription> createSaveSubscription(SaveSubscription value) {
        return new JAXBElement<SaveSubscription>(_SaveSubscription_QNAME, SaveSubscription.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountSubscriptionsWithPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "countSubscriptionsWithPlanResponse")
    public JAXBElement<CountSubscriptionsWithPlanResponse> createCountSubscriptionsWithPlanResponse(CountSubscriptionsWithPlanResponse value) {
        return new JAXBElement<CountSubscriptionsWithPlanResponse>(_CountSubscriptionsWithPlanResponse_QNAME, CountSubscriptionsWithPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAutomaticMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getAutomaticMessage")
    public JAXBElement<GetAutomaticMessage> createGetAutomaticMessage(GetAutomaticMessage value) {
        return new JAXBElement<GetAutomaticMessage>(_GetAutomaticMessage_QNAME, GetAutomaticMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTaxes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getTaxes")
    public JAXBElement<GetTaxes> createGetTaxes(GetTaxes value) {
        return new JAXBElement<GetTaxes>(_GetTaxes_QNAME, GetTaxes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewVendorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addNewVendorResponse")
    public JAXBElement<AddNewVendorResponse> createAddNewVendorResponse(AddNewVendorResponse value) {
        return new JAXBElement<AddNewVendorResponse>(_AddNewVendorResponse_QNAME, AddNewVendorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoesNotExist }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "DoesNotExist")
    public JAXBElement<DoesNotExist> createDoesNotExist(DoesNotExist value) {
        return new JAXBElement<DoesNotExist>(_DoesNotExist_QNAME, DoesNotExist.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadAttachment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "readAttachment")
    public JAXBElement<ReadAttachment> createReadAttachment(ReadAttachment value) {
        return new JAXBElement<ReadAttachment>(_ReadAttachment_QNAME, ReadAttachment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVatTaxInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getVatTaxInfo")
    public JAXBElement<GetVatTaxInfo> createGetVatTaxInfo(GetVatTaxInfo value) {
        return new JAXBElement<GetVatTaxInfo>(_GetVatTaxInfo_QNAME, GetVatTaxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSubscriptionsWithPlanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getSubscriptionsWithPlanResponse")
    public JAXBElement<GetSubscriptionsWithPlanResponse> createGetSubscriptionsWithPlanResponse(GetSubscriptionsWithPlanResponse value) {
        return new JAXBElement<GetSubscriptionsWithPlanResponse>(_GetSubscriptionsWithPlanResponse_QNAME, GetSubscriptionsWithPlanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeUserPassword }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "changeUserPassword")
    public JAXBElement<ChangeUserPassword> createChangeUserPassword(ChangeUserPassword value) {
        return new JAXBElement<ChangeUserPassword>(_ChangeUserPassword_QNAME, ChangeUserPassword.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddAttachmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "addAttachmentResponse")
    public JAXBElement<AddAttachmentResponse> createAddAttachmentResponse(AddAttachmentResponse value) {
        return new JAXBElement<AddAttachmentResponse>(_AddAttachmentResponse_QNAME, AddAttachmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetChangesets }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getChangesets")
    public JAXBElement<GetChangesets> createGetChangesets(GetChangesets value) {
        return new JAXBElement<GetChangesets>(_GetChangesets_QNAME, GetChangesets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMyBusinesses }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getMyBusinesses")
    public JAXBElement<GetMyBusinesses> createGetMyBusinesses(GetMyBusinesses value) {
        return new JAXBElement<GetMyBusinesses>(_GetMyBusinesses_QNAME, GetMyBusinesses.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearTaxRate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "clearTaxRate")
    public JAXBElement<ClearTaxRate> createClearTaxRate(ClearTaxRate value) {
        return new JAXBElement<ClearTaxRate>(_ClearTaxRate_QNAME, ClearTaxRate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveCollaborationEvent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveCollaborationEvent")
    public JAXBElement<SaveCollaborationEvent> createSaveCollaborationEvent(SaveCollaborationEvent value) {
        return new JAXBElement<SaveCollaborationEvent>(_SaveCollaborationEvent_QNAME, SaveCollaborationEvent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFuturePayAgreementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getFuturePayAgreementResponse")
    public JAXBElement<GetFuturePayAgreementResponse> createGetFuturePayAgreementResponse(GetFuturePayAgreementResponse value) {
        return new JAXBElement<GetFuturePayAgreementResponse>(_GetFuturePayAgreementResponse_QNAME, GetFuturePayAgreementResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveTaxCodesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveTaxCodesResponse")
    public JAXBElement<SaveTaxCodesResponse> createSaveTaxCodesResponse(SaveTaxCodesResponse value) {
        return new JAXBElement<SaveTaxCodesResponse>(_SaveTaxCodesResponse_QNAME, SaveTaxCodesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetRolesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setRolesResponse")
    public JAXBElement<SetRolesResponse> createSetRolesResponse(SetRolesResponse value) {
        return new JAXBElement<SetRolesResponse>(_SetRolesResponse_QNAME, SetRolesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveTaxResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveTaxResponse")
    public JAXBElement<SaveTaxResponse> createSaveTaxResponse(SaveTaxResponse value) {
        return new JAXBElement<SaveTaxResponse>(_SaveTaxResponse_QNAME, SaveTaxResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveTax }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "saveTax")
    public JAXBElement<SaveTax> createSaveTax(SaveTax value) {
        return new JAXBElement<SaveTax>(_SaveTax_QNAME, SaveTax.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetContractLinks }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "setContractLinks")
    public JAXBElement<SetContractLinks> createSetContractLinks(SetContractLinks value) {
        return new JAXBElement<SetContractLinks>(_SetContractLinks_QNAME, SetContractLinks.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AppendToFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "appendToFileResponse")
    public JAXBElement<AppendToFileResponse> createAppendToFileResponse(AppendToFileResponse value) {
        return new JAXBElement<AppendToFileResponse>(_AppendToFileResponse_QNAME, AppendToFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetJournalEntriesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getJournalEntriesResponse")
    public JAXBElement<GetJournalEntriesResponse> createGetJournalEntriesResponse(GetJournalEntriesResponse value) {
        return new JAXBElement<GetJournalEntriesResponse>(_GetJournalEntriesResponse_QNAME, GetJournalEntriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCountries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getCountries")
    public JAXBElement<GetCountries> createGetCountries(GetCountries value) {
        return new JAXBElement<GetCountries>(_GetCountries_QNAME, GetCountries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProvincesOrStates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getProvincesOrStates")
    public JAXBElement<GetProvincesOrStates> createGetProvincesOrStates(GetProvincesOrStates value) {
        return new JAXBElement<GetProvincesOrStates>(_GetProvincesOrStates_QNAME, GetProvincesOrStates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDefaultRegionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.service.books/", name = "getDefaultRegionResponse")
    public JAXBElement<GetDefaultRegionResponse> createGetDefaultRegionResponse(GetDefaultRegionResponse value) {
        return new JAXBElement<GetDefaultRegionResponse>(_GetDefaultRegionResponse_QNAME, GetDefaultRegionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "data", scope = ReadAttachmentResponse.class)
    public JAXBElement<byte[]> createReadAttachmentResponseData(byte[] value) {
        return new JAXBElement<byte[]>(_ReadAttachmentResponseData_QNAME, byte[].class, ReadAttachmentResponse.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = AppendToAttachment.class)
    public JAXBElement<byte[]> createAppendToAttachmentArg2(byte[] value) {
        return new JAXBElement<byte[]>(_AppendToAttachmentArg2_QNAME, byte[].class, AppendToAttachment.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "buffer", scope = AddAttachment.class)
    public JAXBElement<byte[]> createAddAttachmentBuffer(byte[] value) {
        return new JAXBElement<byte[]>(_AddAttachmentBuffer_QNAME, byte[].class, AddAttachment.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = AppendToFile.class)
    public JAXBElement<byte[]> createAppendToFileArg2(byte[] value) {
        return new JAXBElement<byte[]>(_AppendToAttachmentArg2_QNAME, byte[].class, AppendToFile.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "data", scope = ReadFileResponse.class)
    public JAXBElement<byte[]> createReadFileResponseData(byte[] value) {
        return new JAXBElement<byte[]>(_ReadAttachmentResponseData_QNAME, byte[].class, ReadFileResponse.class, ((byte[]) value));
    }

}
