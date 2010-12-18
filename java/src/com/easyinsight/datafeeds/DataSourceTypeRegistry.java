package com.easyinsight.datafeeds;

import com.easyinsight.datafeeds.admin.AdminStatsDataSource;
import com.easyinsight.datafeeds.basecamp.*;
import com.easyinsight.datafeeds.cloudwatch.CloudWatchDataSource;
import com.easyinsight.datafeeds.constantcontact.*;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.datafeeds.freshbooks.*;
import com.easyinsight.datafeeds.ganalytics.GoogleAnalyticsDataSource;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.highrise.*;
import com.easyinsight.datafeeds.linkedin.LinkedInDataSource;
import com.easyinsight.datafeeds.marketo.MarketoDataSource;
import com.easyinsight.datafeeds.meetup.MeetupDataSource;
import com.easyinsight.datafeeds.pivotaltracker.PivotalTrackerBaseSource;
import com.easyinsight.datafeeds.quickbase.QuickbaseCompositeSource;
import com.easyinsight.datafeeds.quickbase.QuickbaseDatabaseSource;
import com.easyinsight.datafeeds.redirect.RedirectDataSource;
import com.easyinsight.datafeeds.salesforce.SalesforceBaseDataSource;
import com.easyinsight.datafeeds.sendgrid.SendGridDataSource;
import com.easyinsight.datafeeds.test.TestAlphaDataSource;
import com.easyinsight.datafeeds.test.TestBetaDataSource;
import com.easyinsight.datafeeds.test.TestGammaDataSource;
import com.easyinsight.datafeeds.wholefoods.WholeFoodsSource;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 10:07:19 PM
 */
public class DataSourceTypeRegistry {

    private Map<FeedType, Class> dataSourceMap = new HashMap<FeedType, Class>();

    public DataSourceTypeRegistry() {
        registerTypes();
    }

    private void registerTypes() {
        registerType(FeedType.STATIC, FileBasedFeedDefinition.class);
        registerType(FeedType.ANALYSIS_BASED, AnalysisBasedFeedDefinition.class);
        registerType(FeedType.GOOGLE, GoogleFeedDefinition.class);
        registerType(FeedType.COMPOSITE, CompositeFeedDefinition.class);
        registerType(FeedType.SALESFORCE, SalesforceBaseDataSource.class);
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
        registerType(FeedType.BASECAMP_COMMENTS, BaseCampCommentsSource.class);
        registerType(FeedType.REDIRECT, RedirectDataSource.class);
        registerType(FeedType.WHOLE_FOODS, WholeFoodsSource.class);
        registerType(FeedType.HIGHRISE_CASE_NOTES, HighRiseCaseNotesSource.class);
        registerType(FeedType.HIGHRISE_COMPANY_NOTES, HighRiseCompanyNotesSource.class);
        registerType(FeedType.HIGHRISE_CONTACT_NOTES, HighRiseContactNotesSource.class);
        registerType(FeedType.HIGHRISE_DEAL_NOTES, HighRiseDealNotesSource.class);
        registerType(FeedType.CONSTANT_CONTACT, ConstantContactCompositeSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CONTACT_LISTS, CCContactListSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CONTACTS, CCContactSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CONTACT_TO_CONTACT_LIST, CCContactToContactListSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CAMPAIGN, CCCampaignSource.class);
        registerType(FeedType.CONSTANT_CONTACT_CAMPAIGN_RESULTS, CCCampaignResultsSource.class);
        registerType(FeedType.BASECAMP_TODO_COMMENTS, BaseCampTodoCommentsSource.class);
        registerType(FeedType.QUICKBASE_COMPOSITE, QuickbaseCompositeSource.class);
        registerType(FeedType.QUICKBASE_CHILD, QuickbaseDatabaseSource.class);
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
