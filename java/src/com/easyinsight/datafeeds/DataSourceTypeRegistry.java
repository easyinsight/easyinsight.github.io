package com.easyinsight.datafeeds;

import com.easyinsight.datafeeds.admin.AdminStatsDataSource;
import com.easyinsight.datafeeds.basecamp.*;
import com.easyinsight.datafeeds.basecampnext.*;
import com.easyinsight.datafeeds.batchbook.*;
import com.easyinsight.datafeeds.campaignmonitor.CMClientSource;
import com.easyinsight.datafeeds.campaignmonitor.CampaignMonitorDataSource;
import com.easyinsight.datafeeds.cleardb.ClearDBCompositeSource;
import com.easyinsight.datafeeds.cleardb.ClearDBDataSource;
import com.easyinsight.datafeeds.cloudwatch.*;
import com.easyinsight.datafeeds.composite.FederatedDataSource;
import com.easyinsight.datafeeds.constantcontact.*;
/*import com.easyinsight.datafeeds.custom.CustomRestSource;*/
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.datafeeds.freshbooks.*;
import com.easyinsight.datafeeds.ganalytics.GoogleAnalyticsDataSource;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.harvest.*;
import com.easyinsight.datafeeds.highrise.*;
/*import com.easyinsight.datafeeds.kashoo.KashooBusinessSource;
import com.easyinsight.datafeeds.kashoo.KashooCompositeSource;*/
import com.easyinsight.datafeeds.linkedin.LinkedInDataSource;
import com.easyinsight.datafeeds.marketo.MarketoDataSource;
import com.easyinsight.datafeeds.meetup.MeetupDataSource;
import com.easyinsight.datafeeds.pivotaltracker.PivotalTrackerBaseSource;
import com.easyinsight.datafeeds.quickbase.QuickbaseCompositeSource;
import com.easyinsight.datafeeds.quickbase.QuickbaseDatabaseSource;
import com.easyinsight.datafeeds.redirect.RedirectDataSource;
import com.easyinsight.datafeeds.salesforce.SalesforceBaseDataSource;
import com.easyinsight.datafeeds.salesforce.SalesforceSObjectSource;
import com.easyinsight.datafeeds.sample.SampleCustomerDataSource;
import com.easyinsight.datafeeds.sample.SampleDataSource;
import com.easyinsight.datafeeds.sample.SampleProductDataSource;
import com.easyinsight.datafeeds.sample.SampleSalesDataSource;
import com.easyinsight.datafeeds.sendgrid.SendGridDataSource;
/*import com.easyinsight.datafeeds.solve360.Solve360ActivitiesSource;
import com.easyinsight.datafeeds.solve360.Solve360CompositeSource;
import com.easyinsight.datafeeds.solve360.Solve360ContactsSource;
import com.easyinsight.datafeeds.solve360.Solve360OpportunitiesSource;*/
import com.easyinsight.datafeeds.test.TestAlphaDataSource;
import com.easyinsight.datafeeds.test.TestBetaDataSource;
import com.easyinsight.datafeeds.test.TestGammaDataSource;
/*import com.easyinsight.datafeeds.twilio.TwilioCompositeSource;
import com.easyinsight.datafeeds.twilio.TwilioSomethingSource;*/
import com.easyinsight.datafeeds.wholefoods.WholeFoodsSource;
/*import com.easyinsight.datafeeds.xero.XeroAccountSource;
import com.easyinsight.datafeeds.xero.XeroBankTransactionSource;
import com.easyinsight.datafeeds.xero.XeroCompositeSource;*/
import com.easyinsight.datafeeds.zendesk.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 10:07:19 PM
 */
public class DataSourceTypeRegistry {

    private Map<FeedType, Class> dataSourceMap = new HashMap<FeedType, Class>();
    private Set<Integer> exchangeTypes = new HashSet<Integer>();

    public DataSourceTypeRegistry() {
        registerTypes();
        registerExchangeTypes();
    }

    public boolean isExchangeType(int type) {
        return exchangeTypes.contains(type);
    }

    private void registerExchangeTypes() {
        exchangeTypes.add(FeedType.SALESFORCE.getType());
        exchangeTypes.add(FeedType.BASECAMP_MASTER.getType());
        exchangeTypes.add(FeedType.GOOGLE_ANALYTICS.getType());
        exchangeTypes.add(FeedType.CLOUD_WATCH.getType());
        exchangeTypes.add(FeedType.HIGHRISE_COMPOSITE.getType());
        exchangeTypes.add(FeedType.PIVOTAL_TRACKER.getType());
        exchangeTypes.add(FeedType.SENDGRID.getType());
        exchangeTypes.add(FeedType.LINKEDIN.getType());
        exchangeTypes.add(FeedType.FRESHBOOKS_COMPOSITE.getType());
        exchangeTypes.add(FeedType.CONSTANT_CONTACT.getType());
        exchangeTypes.add(FeedType.BATCHBOOK_COMPOSITE.getType());
        exchangeTypes.add(FeedType.ZENDESK_COMPOSITE.getType());
        exchangeTypes.add(FeedType.HARVEST_COMPOSITE.getType());
    }

    public Set<Integer> getExchangeTypes() {
        return exchangeTypes;
    }

    private void registerTypes() {
        registerType(FeedType.STATIC, FileBasedFeedDefinition.class);
        registerType(FeedType.ANALYSIS_BASED, AnalysisBasedFeedDefinition.class);
        registerType(FeedType.GOOGLE, GoogleFeedDefinition.class);
        registerType(FeedType.COMPOSITE, CompositeFeedDefinition.class);
        registerType(FeedType.SALESFORCE, SalesforceBaseDataSource.class);
        registerType(FeedType.SALESFORCE_SUB, SalesforceSObjectSource.class);
        registerType(FeedType.DEFAULT, FeedDefinition.class);
        registerType(FeedType.BASECAMP_MASTER, BaseCampCompositeSource.class);
        registerType(FeedType.ADMIN_STATS, AdminStatsDataSource.class);
        registerType(FeedType.GOOGLE_ANALYTICS, GoogleAnalyticsDataSource.class);
        registerType(FeedType.TEST_ALPHA, TestAlphaDataSource.class);
        registerType(FeedType.TEST_BETA, TestBetaDataSource.class);
        registerType(FeedType.TEST_GAMMA, TestGammaDataSource.class);
        registerType(FeedType.BASECAMP, BaseCampTodoSource.class);
        registerType(FeedType.BASECAMP_TIME, BaseCampTimeSource.class);
        registerType(FeedType.BASECAMP_COMPANY, BaseCampCompanySource.class);
        registerType(FeedType.BASECAMP_COMPANY_PROJECT_JOIN, BaseCampCompanyProjectJoinSource.class);
        registerType(FeedType.CLOUD_WATCH, CloudWatchDataSource.class);
        registerType(FeedType.HIGHRISE_COMPOSITE, HighRiseCompositeSource.class);
        registerType(FeedType.HIGHRISE_COMPANY, HighRiseCompanySource.class);
        registerType(FeedType.HIGHRISE_DEAL, HighRiseDealSource.class);
        registerType(FeedType.HIGHRISE_CONTACTS, HighRiseContactSource.class);
        registerType(FeedType.MARKETO, MarketoDataSource.class);
        registerType(FeedType.PIVOTAL_TRACKER, PivotalTrackerBaseSource.class);
        registerType(FeedType.SENDGRID, SendGridDataSource.class);
        registerType(FeedType.MEETUP, MeetupDataSource.class);
        registerType(FeedType.LINKEDIN, LinkedInDataSource.class);
        registerType(FeedType.HIGHRISE_CASES, HighRiseCaseSource.class);
        registerType(FeedType.HIGHRISE_TASKS, HighRiseTaskSource.class);
        registerType(FeedType.HIGHRISE_EMAILS, HighRiseEmailSource.class);
        registerType(FeedType.FRESHBOOKS_INVOICE, FreshbooksInvoiceSource.class);
        registerType(FeedType.FRESHBOOKS_CLIENTS, FreshbooksClientSource.class);
        registerType(FeedType.FRESHBOOKS_EXPENSES, FreshbooksExpenseSource.class);
        registerType(FeedType.FRESHBOOKS_PAYMENTS, FreshbooksPaymentSource.class);
        registerType(FeedType.FRESHBOOKS_STAFF, FreshbooksStaffSource.class);
        registerType(FeedType.FRESHBOOKS_TASKS, FreshbooksTaskSource.class);
        registerType(FeedType.FRESHBOOKS_TIME_ENTRIES, FreshbooksTimeEntrySource.class);
        registerType(FeedType.FRESHBOOKS_COMPOSITE, FreshbooksCompositeSource.class);
        registerType(FeedType.FRESHBOOKS_CATEGORIES, FreshbooksCategorySource.class);
        registerType(FeedType.FRESHBOOKS_ESTIMATES, FreshbooksEstimateSource.class);
        registerType(FeedType.FRESHBOOKS_PROJECTS, FreshbooksProjectSource.class);
        registerType(FeedType.FRESHBOOKS_LINE_ITEMS, FreshbooksInvoiceLineSource.class);
        registerType(FeedType.BASECAMP_COMMENTS, BaseCampCommentsSource.class);
        registerType(FeedType.REDIRECT, RedirectDataSource.class);
        registerType(FeedType.WHOLE_FOODS, WholeFoodsSource.class);
        registerType(FeedType.HIGHRISE_CASE_NOTES, HighRiseCaseNotesSource.class);
        registerType(FeedType.HIGHRISE_COMPANY_NOTES, HighRiseCompanyNotesSource.class);
        registerType(FeedType.HIGHRISE_CONTACT_NOTES, HighRiseContactNotesSource.class);
        registerType(FeedType.HIGHRISE_DEAL_NOTES, HighRiseDealNotesSource.class);
        registerType(FeedType.HIGHRISE_CASE_JOIN, HighRiseCaseJoinSource.class);
        registerType(FeedType.CONSTANT_CONTACT, ConstantContactCompositeSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CONTACT_LISTS, CCContactListSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CONTACTS, CCContactSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CONTACT_TO_CONTACT_LIST, CCContactToContactListSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CAMPAIGN, CCCampaignSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS, CCCampaignResultsSource.class);
        registerType(FeedType.BASECAMP_TODO_COMMENTS, BaseCampTodoCommentsSource.class);
        registerType(FeedType.QUICKBASE_COMPOSITE, QuickbaseCompositeSource.class);
        registerType(FeedType.QUICKBASE_CHILD, QuickbaseDatabaseSource.class);
        registerType(FeedType.CLEARDB_COMPOSITE, ClearDBCompositeSource.class);
        registerType(FeedType.CLEARDB_CHILD, ClearDBDataSource.class);
        registerType(FeedType.BATCHBOOK_COMPOSITE, BatchbookCompositeSource.class);
        registerType(FeedType.BATCHBOOK_DEALS, BatchbookDealSource.class);
        registerType(FeedType.BATCHBOOK_PEOPLE, BatchbookPeopleSource.class);
        registerType(FeedType.BATCHBOOK_COMPANIES, BatchbookCompanySource.class);
        registerType(FeedType.BATCHBOOK_COMMUNICATIONS, BatchbookCommunicationsSource.class);
        registerType(FeedType.BATCHBOOK_TODOS, BatchbookTodoSource.class);
        registerType(FeedType.BATCHBOOK_USERS, BatchbookUserSource.class);
        registerType(FeedType.BATCHBOOK_COMMUNICATION_PARTIES, BatchbookCommunicationsPartySource.class);
        registerType(FeedType.CAMPAIGN_MONITOR_COMPOSITE, CampaignMonitorDataSource.class);
        registerType(FeedType.CAMPAIGN_MONITOR_CLIENTS, CMClientSource.class);
        registerType(FeedType.FEDERATED, FederatedDataSource.class);
        registerType(FeedType.HARVEST_COMPOSITE, HarvestCompositeSource.class);
        registerType(FeedType.HARVEST_CLIENT, HarvestClientSource.class);
        registerType(FeedType.HARVEST_PROJECT, HarvestProjectSource.class);
        registerType(FeedType.HARVEST_TIME, HarvestTimeSource.class);
        registerType(FeedType.HARVEST_CONTACTS, HarvestClientContactSource.class);
        registerType(FeedType.HARVEST_TASKS, HarvestTaskSource.class);
        registerType(FeedType.HARVEST_TASK_ASSIGNMENTS, HarvestTaskAssignmentSource.class);
        registerType(FeedType.HARVEST_USER_ASSIGNMENTS, HarvestUserAssignmentSource.class);
        registerType(FeedType.HARVEST_USERS, HarvestUserSource.class);
        registerType(FeedType.HARVEST_EXPENSES, HarvestExpenseSource.class);
        registerType(FeedType.HARVEST_EXPENSE_CATEGORIES, HarvestExpenseCategoriesSource.class);
        registerType(FeedType.HARVEST_INVOICES, HarvestInvoiceSource.class);
        registerType(FeedType.ZENDESK_COMPOSITE, ZendeskCompositeSource.class);
        registerType(FeedType.ZENDESK_GROUP, ZendeskGroupSource.class);
        registerType(FeedType.ZENDESK_USER, ZendeskUserSource.class);
        registerType(FeedType.ZENDESK_ORGANIZATION, ZendeskOrganizationSource.class);
        registerType(FeedType.ZENDESK_TICKET, ZendeskTicketSource.class);
        registerType(FeedType.ZENDESK_GROUP_TO_USER, ZendeskGroupToUserJoinSource.class);
        registerType(FeedType.AMAZON_EC2, AmazonEC2Source.class);
        registerType(FeedType.AMAZON_EBS, AmazonEBSSource.class);
        registerType(FeedType.AMAZON_SQS, AmazonSQSSource.class);
        registerType(FeedType.AMAZON_RDS, AmazonRDSSource.class);
        registerType(FeedType.SAMPLE_COMPOSITE, SampleDataSource.class);
        registerType(FeedType.SAMPLE_CUSTOMER, SampleCustomerDataSource.class);
        registerType(FeedType.SAMPLE_PRODUCT, SampleProductDataSource.class);
        registerType(FeedType.SAMPLE_SALES, SampleSalesDataSource.class);
        /*registerType(FeedType.KASHOO_COMPOSITE, KashooCompositeSource.class);
        registerType(FeedType.KASHOO_BUSINESSES, KashooBusinessSource.class);
        registerType(FeedType.SOLVE360_COMPOSITE, Solve360CompositeSource.class);
        registerType(FeedType.SOLVE360_CONTACTS, Solve360ContactsSource.class);
        registerType(FeedType.SOLVE360_OPPORTUNITIES, Solve360OpportunitiesSource.class);
        registerType(FeedType.SOLVE360_ACTIVITIES, Solve360ActivitiesSource.class);
        registerType(FeedType.XERO_COMPOSITE, XeroCompositeSource.class);
        registerType(FeedType.XERO_ACCOUNTS, XeroAccountSource.class);
        registerType(FeedType.XERO_BANK_TRANSACTIONS, XeroBankTransactionSource.class);
        registerType(FeedType.CUSTOM_REST_LIVE, CustomRestSource.class);
        registerType(FeedType.TWILIO_COMPOSITE, TwilioCompositeSource.class);
        registerType(FeedType.TWILIO_SOMETHING, TwilioSomethingSource.class);*/
        registerType(FeedType.BASECAMP_NEXT_COMPOSITE, BasecampNextCompositeSource.class);
        registerType(FeedType.BASECAMP_NEXT_PROJECTS, BasecampNextProjectSource.class);
        registerType(FeedType.BASECAMP_NEXT_TODOS, BasecampNextTodoSource.class);
        registerType(FeedType.BASECAMP_NEXT_CALENDAR, BasecampNextCalendarSource.class);
        registerType(FeedType.BASECAMP_NEXT_PEOPLE, BasecampNextPeopleSource.class);
    }

    public Map<FeedType, Class> getDataSourceMap() {
        return dataSourceMap;
    }

    private void registerType(FeedType feedType, Class dataSourceClass) {
        dataSourceMap.put(feedType, dataSourceClass);
    }

    public FeedDefinition createDataSource(FeedType feedType) {
        try {
            Class clazz = dataSourceMap.get(feedType);
            return (FeedDefinition) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}