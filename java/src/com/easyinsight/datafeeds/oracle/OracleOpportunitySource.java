package com.easyinsight.datafeeds.oracle;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.oracle.client.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class OracleOpportunitySource extends OracleBaseSource {
    public OracleOpportunitySource() {
        setFeedName("Opportunity");
    }

    public static final String BUDGETAVAILABLEDATE = "BudgetAvailableDate";
    public static final String PRIMARYORGANIZATIONID = "PrimaryOrganizationId";
    public static final String CONFLICTID = "ConflictId";
    public static final String CREATEDBY = "CreatedBy";
    public static final String CREATIONDATE = "CreationDate";
    public static final String CURRENCYCODE = "CurrencyCode";
    public static final String SALESMETHODID = "SalesMethodId";
    public static final String SALESSTAGEID = "SalesStageId";
    public static final String CUSTOMERACCOUNTID = "CustomerAccountId";
    public static final String DEALHORIZONCODE = "DealHorizonCode";
    public static final String DECISIONLEVELCODE = "DecisionLevelCode";
    public static final String DESCRIPTION = "Description";
    public static final String LASTUPDATEDATE = "LastUpdateDate";
    public static final String LASTUPDATEDBY = "LastUpdatedBy";
    public static final String LASTUPDATELOGIN = "LastUpdateLogin";
    public static final String NAME = "Name";
    public static final String OPTYID = "OptyId";
    public static final String OPTYNUMBER = "OptyNumber";
    public static final String OWNERRESOURCEPARTYID = "OwnerResourcePartyId";
    public static final String PRIMARYCOMPETITORID = "PrimaryCompetitorId";
    public static final String KEYCONTACTID = "KeyContactId";
    public static final String REASONWONLOSTCODE = "ReasonWonLostCode";
    public static final String RISKLEVELCODE = "RiskLevelCode";
    public static final String STATUSCODE = "StatusCode";
    public static final String STRATEGICLEVELCODE = "StrategicLevelCode";
    public static final String PRIMARYREVENUEID = "PrimaryRevenueId";
    public static final String TARGETPARTYID = "TargetPartyId";
    public static final String USERLASTUPDATEDATE = "UserLastUpdateDate";
    public static final String TARGETPARTYNAME = "TargetPartyName";
    public static final String DUNSNUMBER = "DUNSNumber";
    public static final String SALESMETHOD = "SalesMethod";
    public static final String SALESSTAGE = "SalesStage";
    public static final String DESCRIPTIONTEXT = "DescriptionText";
    public static final String PHASECD = "PhaseCd";
    public static final String QUOTAFACTOR = "QuotaFactor";
    public static final String RCMNDWINPROB = "RcmndWinProb";
    public static final String STAGESTATUSCD = "StageStatusCd";
    public static final String STGORDER = "StgOrder";
    public static final String EFFECTIVEDATE = "EffectiveDate";
    public static final String REVENUE = "Revenue";
    public static final String REVENUETYPE = "RevenueType";
    public static final String REVNID = "RevnId";
    public static final String EMPLOYEESTOTAL = "EmployeesTotal";
    public static final String CURRENTFYPOTENTIALREVENUE = "CurrentFyPotentialRevenue";
    public static final String WINPROB = "WinProb";
    public static final String PRIMARYCONTACTPARTYNAME = "PrimaryContactPartyName";
    public static final String PRIMARYCONTACTPARTYID = "PrimaryContactPartyId";
    public static final String PRIMARYCONTACTFORMATTEDPHONENUMBER = "PrimaryContactFormattedPhoneNumber";
    public static final String PRIMARYCONTACTEMAILADDRESS = "PrimaryContactEmailAddress";
    public static final String ACCOUNTNUMBER = "AccountNumber";
    public static final String PARTYNAME = "PartyName";
    public static final String LOCATIONID = "LocationId";
    public static final String FORMATTEDADDRESS = "FormattedAddress";
    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String POSTALCODE = "PostalCode";
    public static final String STATE = "State";
    public static final String COMMENTS = "Comments";
    public static final String PARTYNAME1 = "PartyName1";
    public static final String PRSRCNUMBER = "PrSrcNumber";
    public static final String EMAILADDRESS = "EmailAddress";
    public static final String LASTASSIGNEDDATE = "LastAssignedDate";
    public static final String LOOKUPCATEGORY = "LookupCategory";
    public static final String LOOKUPVALUESID = "LookupValuesId";
    public static final String OPTYCREATIONDATE = "OptyCreationDate";
    public static final String PREFERREDFUNCTIONALCURRENCY = "PreferredFunctionalCurrency";
    public static final String PARTYUNIQUENAME = "PartyUniqueName";
    public static final String PRCONRELATIONSHIPID = "PrConRelationshipId";
    public static final String OPTYCREATEDBY = "OptyCreatedBy";
    public static final String CRMCONVERSIONRATE = "CrmConversionRate";
    public static final String CRMREVENUE = "CrmRevenue";
    public static final String PARTYTYPE = "PartyType";
    public static final String OPTYLASTUPDATEDATE = "OptyLastUpdateDate";
    public static final String OPTYLASTUPDATEDBY = "OptyLastUpdatedBy";
    public static final String SALESCHANNELCD = "SalesChannelCd";
    public static final String LINEOFBUSINESSCODE = "LineOfBusinessCode";
    public static final String PARTYUNIQUENAME1 = "PartyUniqueName1";
    public static final String PRIMARYORGANIZATIONNAME = "PrimaryOrganizationName";
    public static final String SALESACCOUNTID = "SalesAccountId";
    public static final String SALESACCOUNTUNIQUENAME = "SalesAccountUniqueName";
    public static final String CONSUMERFIRSTNAME = "ConsumerFirstName";
    public static final String CONSUMERLASTNAME = "ConsumerLastName";
    public static final String PRIMARYCONTACTFIRSTNAME = "PrimaryContactFirstName";
    public static final String PRIMARYCONTACTLASTNAME = "PrimaryContactLastName";
    public static final String LASTASSIGNEDTIMESTAMP = "LastAssignedTimestamp";

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(BUDGETAVAILABLEDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(PRIMARYORGANIZATIONID, new AnalysisDimension());
        fieldBuilder.addField(CONFLICTID, new AnalysisDimension());
        fieldBuilder.addField(CREATEDBY, new AnalysisDimension());
        fieldBuilder.addField(CREATIONDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(CURRENCYCODE, new AnalysisDimension());
        fieldBuilder.addField(SALESMETHODID, new AnalysisDimension());
        fieldBuilder.addField(SALESSTAGEID, new AnalysisDimension());
        fieldBuilder.addField(CUSTOMERACCOUNTID, new AnalysisDimension());
        fieldBuilder.addField(DEALHORIZONCODE, new AnalysisDimension());
        fieldBuilder.addField(DECISIONLEVELCODE, new AnalysisDimension());
        fieldBuilder.addField(DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(LASTUPDATEDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(LASTUPDATEDBY, new AnalysisDimension());
        fieldBuilder.addField(LASTUPDATELOGIN, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(OPTYID, new AnalysisDimension());
        fieldBuilder.addField(OPTYNUMBER, new AnalysisDimension());
        fieldBuilder.addField(OWNERRESOURCEPARTYID, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYCOMPETITORID, new AnalysisDimension());
        fieldBuilder.addField(KEYCONTACTID, new AnalysisDimension());
        fieldBuilder.addField(REASONWONLOSTCODE, new AnalysisDimension());
        fieldBuilder.addField(RISKLEVELCODE, new AnalysisDimension());
        fieldBuilder.addField(STATUSCODE, new AnalysisDimension());
        fieldBuilder.addField(STRATEGICLEVELCODE, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYREVENUEID, new AnalysisDimension());
        fieldBuilder.addField(TARGETPARTYID, new AnalysisDimension());
        fieldBuilder.addField(USERLASTUPDATEDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(TARGETPARTYNAME, new AnalysisDimension());
        fieldBuilder.addField(DUNSNUMBER, new AnalysisDimension());
        fieldBuilder.addField(SALESMETHOD, new AnalysisDimension());
        fieldBuilder.addField(SALESSTAGE, new AnalysisDimension());
        fieldBuilder.addField(DESCRIPTIONTEXT, new AnalysisDimension());
        fieldBuilder.addField(PHASECD, new AnalysisDimension());
        fieldBuilder.addField(QUOTAFACTOR, new AnalysisMeasure());
        fieldBuilder.addField(RCMNDWINPROB, new AnalysisMeasure());
        fieldBuilder.addField(STAGESTATUSCD, new AnalysisDimension());
        fieldBuilder.addField(STGORDER, new AnalysisMeasure());
        fieldBuilder.addField(EFFECTIVEDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(REVENUE, new AnalysisMeasure(FormattingConfiguration.CURRENCY));
        fieldBuilder.addField(REVENUETYPE, new AnalysisDimension());
        fieldBuilder.addField(REVNID, new AnalysisDimension());
        fieldBuilder.addField(EMPLOYEESTOTAL, new AnalysisMeasure());
        fieldBuilder.addField(CURRENTFYPOTENTIALREVENUE, new AnalysisMeasure(FormattingConfiguration.CURRENCY));
        fieldBuilder.addField(WINPROB, new AnalysisMeasure());
        fieldBuilder.addField(PRIMARYCONTACTPARTYNAME, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYCONTACTPARTYID, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYCONTACTFORMATTEDPHONENUMBER, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYCONTACTEMAILADDRESS, new AnalysisDimension());
        fieldBuilder.addField(ACCOUNTNUMBER, new AnalysisDimension());
        fieldBuilder.addField(PARTYNAME, new AnalysisDimension());
        fieldBuilder.addField(LOCATIONID, new AnalysisDimension());
        fieldBuilder.addField(FORMATTEDADDRESS, new AnalysisDimension());
        fieldBuilder.addField(CITY, new AnalysisDimension());
        fieldBuilder.addField(COUNTRY, new AnalysisDimension());
        fieldBuilder.addField(POSTALCODE, new AnalysisDimension());
        fieldBuilder.addField(STATE, new AnalysisDimension());
        fieldBuilder.addField(COMMENTS, new AnalysisDimension());
        fieldBuilder.addField(PARTYNAME1, new AnalysisDimension());
        fieldBuilder.addField(PRSRCNUMBER, new AnalysisDimension());
        fieldBuilder.addField(EMAILADDRESS, new AnalysisDimension());
        fieldBuilder.addField(LASTASSIGNEDDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(LOOKUPCATEGORY, new AnalysisDimension());
        fieldBuilder.addField(LOOKUPVALUESID, new AnalysisDimension());
        fieldBuilder.addField(OPTYCREATIONDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(PREFERREDFUNCTIONALCURRENCY, new AnalysisDimension());
        fieldBuilder.addField(PARTYUNIQUENAME, new AnalysisDimension());
        fieldBuilder.addField(PRCONRELATIONSHIPID, new AnalysisDimension());
        fieldBuilder.addField(OPTYCREATEDBY, new AnalysisDimension());
        fieldBuilder.addField(CRMCONVERSIONRATE, new AnalysisMeasure());
        fieldBuilder.addField(CRMREVENUE, new AnalysisMeasure(FormattingConfiguration.CURRENCY));
        fieldBuilder.addField(PARTYTYPE, new AnalysisDimension());
        fieldBuilder.addField(OPTYLASTUPDATEDATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(OPTYLASTUPDATEDBY, new AnalysisDimension());
        fieldBuilder.addField(SALESCHANNELCD, new AnalysisDimension());
        fieldBuilder.addField(LINEOFBUSINESSCODE, new AnalysisDimension());
        fieldBuilder.addField(PARTYUNIQUENAME1, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYORGANIZATIONNAME, new AnalysisDimension());
        fieldBuilder.addField(SALESACCOUNTID, new AnalysisDimension());
        fieldBuilder.addField(SALESACCOUNTUNIQUENAME, new AnalysisDimension());
        fieldBuilder.addField(CONSUMERFIRSTNAME, new AnalysisDimension());
        fieldBuilder.addField(CONSUMERLASTNAME, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYCONTACTFIRSTNAME, new AnalysisDimension());
        fieldBuilder.addField(PRIMARYCONTACTLASTNAME, new AnalysisDimension());
        fieldBuilder.addField(LASTASSIGNEDTIMESTAMP, new AnalysisDateDimension(true));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            List<com.easyinsight.datafeeds.oracle.client.TransientAppointment> AppointmentList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.Revenue> ChildRevenueList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.SplitRevenue> SplitRevenueList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.CategorySummaryRevenue> CategorySummaryRevenueList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityLead> OpportunityLeadList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunitySource> OpportunitySource1List = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityContact> OpportunityContactList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.RecurringRevenue> RecurringRevenueList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityResponse> OpportunityResponseList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.RevenuePartner> RevenuePartnerPrimaryList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.Note> NoteList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityResource> OpportunityResourceList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityCompetitor> OpportunityCompetitor3List = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityReference> OpportunityReferenceList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.RevenueLineSet> RevenueLineSetList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.RevenueTerritory> RevenueTerritoryList = new ArrayList<>();
            List<com.easyinsight.datafeeds.oracle.client.OpportunityCompetitor> OpportunityCompetitor2List = new ArrayList<>();
            OracleDataSource oracleDataSource = (OracleDataSource) parentDefinition;
            OpportunityService opportunityService = new OpportunityService_Service(new URL(oracleDataSource.getUrl() + "/opptyMgmtOpportunities/OpportunityService?wsdl")).getOpportunityServiceSoapHttpPort();
            BindingProvider prov = (BindingProvider) opportunityService;
            prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, oracleDataSource.getOracleUserName());
            prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, oracleDataSource.getOraclePassword());
            int count;
            int start = 0;
            do {
                count = 0;
                FindCriteria findCriteria = new FindCriteria();
                findCriteria.setFetchSize(1000);
                findCriteria.setFetchStart(start);
                start += 1000;
                FindControl findControl = new FindControl();
                List<Opportunity> opportunities = opportunityService.findOpportunity(findCriteria, findControl);
                for (Opportunity o : opportunities) {
                    count++;
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(BUDGETAVAILABLEDATE), getDate(o.getBudgetAvailableDate()));
                    row.addValue(keys.get(PRIMARYORGANIZATIONID), String.valueOf(o.getPrimaryOrganizationId()));
                    row.addValue(keys.get(CONFLICTID), String.valueOf(o.getConflictId()));
                    row.addValue(keys.get(CREATEDBY), o.getCreatedBy());
                    row.addValue(keys.get(CREATIONDATE), getDate(o.getCreationDate()));
                    row.addValue(keys.get(CURRENCYCODE), o.getCurrencyCode().getValue());
                    row.addValue(keys.get(SALESMETHODID), String.valueOf(o.getSalesMethodId().getValue()));
                    row.addValue(keys.get(SALESSTAGEID), String.valueOf(o.getSalesStageId().getValue()));
                    row.addValue(keys.get(CUSTOMERACCOUNTID), String.valueOf(o.getCustomerAccountId().getValue()));
                    row.addValue(keys.get(DEALHORIZONCODE), o.getDealHorizonCode().getValue());
                    row.addValue(keys.get(DECISIONLEVELCODE), o.getDecisionLevelCode().getValue());
                    row.addValue(keys.get(DESCRIPTION), o.getDescription().getValue());
                    row.addValue(keys.get(LASTUPDATEDATE), getDate(o.getLastUpdateDate()));
                    row.addValue(keys.get(LASTUPDATEDBY), o.getLastUpdatedBy());
                    row.addValue(keys.get(LASTUPDATELOGIN), o.getLastUpdateLogin().getValue());
                    row.addValue(keys.get(NAME), o.getName());
                    row.addValue(keys.get(OPTYID), String.valueOf(o.getOptyId()));
                    row.addValue(keys.get(OPTYNUMBER), o.getOptyNumber());
                    row.addValue(keys.get(OWNERRESOURCEPARTYID), String.valueOf(o.getOwnerResourcePartyId()));
                    row.addValue(keys.get(PRIMARYCOMPETITORID), String.valueOf(o.getPrimaryCompetitorId().getValue()));
                    row.addValue(keys.get(KEYCONTACTID), String.valueOf(o.getKeyContactId().getValue()));
                    row.addValue(keys.get(REASONWONLOSTCODE), o.getReasonWonLostCode().getValue());
                    row.addValue(keys.get(RISKLEVELCODE), o.getRiskLevelCode().getValue());
                    row.addValue(keys.get(STATUSCODE), o.getStatusCode().getValue());
                    row.addValue(keys.get(STRATEGICLEVELCODE), o.getStrategicLevelCode().getValue());
                    row.addValue(keys.get(PRIMARYREVENUEID), String.valueOf(o.getPrimaryRevenueId().getValue()));
                    row.addValue(keys.get(TARGETPARTYID), String.valueOf(o.getTargetPartyId().getValue()));
                    row.addValue(keys.get(USERLASTUPDATEDATE), getDate(o.getUserLastUpdateDate()));
                    row.addValue(keys.get(TARGETPARTYNAME), o.getTargetPartyName());
                    row.addValue(keys.get(DUNSNUMBER), o.getDUNSNumber().getValue());
                    row.addValue(keys.get(SALESMETHOD), o.getSalesMethod());
                    row.addValue(keys.get(SALESSTAGE), o.getSalesStage());
                    row.addValue(keys.get(DESCRIPTIONTEXT), o.getDescriptionText().getValue());
                    row.addValue(keys.get(PHASECD), o.getPhaseCd().getValue());
                    row.addValue(keys.get(QUOTAFACTOR), getMeasureValue(o.getQuotaFactor()));
                    row.addValue(keys.get(RCMNDWINPROB), getMeasureValue(o.getRcmndWinProb()));
                    row.addValue(keys.get(STAGESTATUSCD), o.getStageStatusCd().getValue());
                    row.addValue(keys.get(STGORDER), getMeasureValue(o.getStgOrder()));
                    row.addValue(keys.get(EFFECTIVEDATE), getDate(o.getEffectiveDate()));
                    row.addValue(keys.get(REVENUE), getMeasureValue(o.getRevenue()));
                    row.addValue(keys.get(REVENUETYPE), o.getRevenueType().getValue());
                    row.addValue(keys.get(REVNID), String.valueOf(o.getRevnId()));
                    row.addValue(keys.get(EMPLOYEESTOTAL), getMeasureValue(o.getEmployeesTotal()));
                    row.addValue(keys.get(CURRENTFYPOTENTIALREVENUE), getMeasureValue(o.getCurrentFyPotentialRevenue()));
                    row.addValue(keys.get(WINPROB), getMeasureValue(o.getWinProb()));
                    row.addValue(keys.get(PRIMARYCONTACTPARTYNAME), o.getPrimaryContactPartyName());
                    row.addValue(keys.get(PRIMARYCONTACTPARTYID), String.valueOf(o.getPrimaryContactPartyId()));
                    row.addValue(keys.get(PRIMARYCONTACTFORMATTEDPHONENUMBER), o.getPrimaryContactFormattedPhoneNumber().getValue());
                    row.addValue(keys.get(PRIMARYCONTACTEMAILADDRESS), o.getPrimaryContactEmailAddress().getValue());
                    row.addValue(keys.get(ACCOUNTNUMBER), o.getAccountNumber().getValue());
                    row.addValue(keys.get(PARTYNAME), o.getPartyName());
                    row.addValue(keys.get(LOCATIONID), String.valueOf(o.getLocationId()));
                    row.addValue(keys.get(FORMATTEDADDRESS), o.getFormattedAddress().getValue());
                    row.addValue(keys.get(CITY), o.getCity().getValue());
                    row.addValue(keys.get(COUNTRY), o.getCountry());
                    row.addValue(keys.get(POSTALCODE), o.getPostalCode().getValue());
                    row.addValue(keys.get(STATE), o.getState().getValue());
                    row.addValue(keys.get(COMMENTS), o.getComments().getValue());
                    row.addValue(keys.get(PARTYNAME1), o.getPartyName1());
                    row.addValue(keys.get(PRSRCNUMBER), o.getPrSrcNumber().getValue());
                    row.addValue(keys.get(EMAILADDRESS), o.getEmailAddress().getValue());
                    row.addValue(keys.get(LASTASSIGNEDDATE), getDate(o.getLastAssignedDate()));
                    row.addValue(keys.get(LOOKUPCATEGORY), o.getLookupCategory().getValue());
                    row.addValue(keys.get(LOOKUPVALUESID), String.valueOf(o.getLookupValuesId()));
                    row.addValue(keys.get(OPTYCREATIONDATE), getDate(o.getOptyCreationDate()));
                    row.addValue(keys.get(PREFERREDFUNCTIONALCURRENCY), o.getPreferredFunctionalCurrency().getValue());
                    row.addValue(keys.get(PARTYUNIQUENAME), o.getPartyUniqueName().getValue());
                    row.addValue(keys.get(PRCONRELATIONSHIPID), String.valueOf(o.getPrConRelationshipId().getValue()));
                    row.addValue(keys.get(OPTYCREATEDBY), o.getOptyCreatedBy().getValue());
                    row.addValue(keys.get(CRMCONVERSIONRATE), getMeasureValue(o.getCrmConversionRate()));
                    row.addValue(keys.get(CRMREVENUE), getMeasureValue(o.getCrmRevenue()));
                    row.addValue(keys.get(PARTYTYPE), o.getPartyType());
                    row.addValue(keys.get(OPTYLASTUPDATEDATE), getDate(o.getOptyLastUpdateDate()));
                    row.addValue(keys.get(OPTYLASTUPDATEDBY), o.getOptyLastUpdatedBy().getValue());
                    row.addValue(keys.get(SALESCHANNELCD), o.getSalesChannelCd().getValue());
                    row.addValue(keys.get(LINEOFBUSINESSCODE), o.getLineOfBusinessCode().getValue());
                    row.addValue(keys.get(PARTYUNIQUENAME1), o.getPartyUniqueName1().getValue());
                    row.addValue(keys.get(PRIMARYORGANIZATIONNAME), o.getPrimaryOrganizationName().getValue());
                    row.addValue(keys.get(SALESACCOUNTID), String.valueOf(o.getSalesAccountId().getValue()));
                    row.addValue(keys.get(SALESACCOUNTUNIQUENAME), o.getSalesAccountUniqueName().getValue());
                    row.addValue(keys.get(CONSUMERFIRSTNAME), o.getConsumerFirstName().getValue());
                    row.addValue(keys.get(CONSUMERLASTNAME), o.getConsumerLastName().getValue());
                    row.addValue(keys.get(PRIMARYCONTACTFIRSTNAME), o.getPrimaryContactFirstName().getValue());
                    row.addValue(keys.get(PRIMARYCONTACTLASTNAME), o.getPrimaryContactLastName().getValue());
                    row.addValue(keys.get(LASTASSIGNEDTIMESTAMP), getDate(o.getLastAssignedTimestamp()));
                    for (com.easyinsight.datafeeds.oracle.client.TransientAppointment child : o.getAppointment()) {
                        AppointmentList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.Revenue child : o.getChildRevenue()) {
                        ChildRevenueList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.SplitRevenue child : o.getSplitRevenue()) {
                        SplitRevenueList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.CategorySummaryRevenue child : o.getCategorySummaryRevenue()) {
                        CategorySummaryRevenueList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityLead child : o.getOpportunityLead()) {
                        OpportunityLeadList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunitySource child : o.getOpportunitySource1()) {
                        OpportunitySource1List.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityContact child : o.getOpportunityContact()) {
                        OpportunityContactList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.RecurringRevenue child : o.getRecurringRevenue()) {
                        RecurringRevenueList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityResponse child : o.getOpportunityResponse()) {
                        OpportunityResponseList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.RevenuePartner child : o.getRevenuePartnerPrimary()) {
                        RevenuePartnerPrimaryList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.Note child : o.getNote()) {
                        NoteList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityResource child : o.getOpportunityResource()) {
                        OpportunityResourceList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityCompetitor child : o.getOpportunityCompetitor3()) {
                        OpportunityCompetitor3List.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityReference child : o.getOpportunityReference()) {
                        OpportunityReferenceList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.RevenueLineSet child : o.getRevenueLineSet()) {
                        RevenueLineSetList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.RevenueTerritory child : o.getRevenueTerritory()) {
                        RevenueTerritoryList.add(child);
                    }

                    for (com.easyinsight.datafeeds.oracle.client.OpportunityCompetitor child : o.getOpportunityCompetitor2()) {
                        OpportunityCompetitor2List.add(child);
                    }

                }
            } while (count == 1000);

            oracleDataSource.setAppointment(AppointmentList);
            oracleDataSource.setChildRevenue(ChildRevenueList);
            oracleDataSource.setSplitRevenue(SplitRevenueList);
            oracleDataSource.setCategorySummaryRevenue(CategorySummaryRevenueList);
            oracleDataSource.setOpportunityLead(OpportunityLeadList);
            oracleDataSource.setOpportunitySource1(OpportunitySource1List);
            oracleDataSource.setOpportunityContact(OpportunityContactList);
            oracleDataSource.setRecurringRevenue(RecurringRevenueList);
            oracleDataSource.setOpportunityResponse(OpportunityResponseList);
            oracleDataSource.setRevenuePartnerPrimary(RevenuePartnerPrimaryList);
            oracleDataSource.setNote(NoteList);
            oracleDataSource.setOpportunityResource(OpportunityResourceList);
            oracleDataSource.setOpportunityCompetitor3(OpportunityCompetitor3List);
            oracleDataSource.setOpportunityReference(OpportunityReferenceList);
            oracleDataSource.setRevenueLineSet(RevenueLineSetList);
            oracleDataSource.setRevenueTerritory(RevenueTerritoryList);
            oracleDataSource.setOpportunityCompetitor2(OpportunityCompetitor2List);
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ORACLE_OPPORTUNITYSOURCE;
    }

}