package com.easyinsight.datafeeds.ganalytics;

import com.easyinsight.datafeeds.*;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.*;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.util.ServiceException;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.net.URL;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

/**
 * User: James Boe
 * Date: Jun 11, 2009
 * Time: 12:01:56 PM
 */
public class GoogleAnalyticsDataSource extends ServerDataSourceDefinition {

    public static final String BROWSER = "ga:browser";
    public static final String BROWSER_VERSION = "ga:browserVersion";
    public static final String CITY = "ga:city";
    public static final String CONNECTION_SPEED = "ga:connectionSpeed";
    public static final String CONTINENT = "ga:continent";
    public static final String COUNT_OF_VISITS = "ga:countOfVisits";
    public static final String COUNTRY = "ga:country";
    public static final String DATE = "ga:date";
    public static final String DAYS_SINCE_LAST_VISIT = "ga:daysSinceLastVisit";
    public static final String HOSTNAME = "ga:hostname";
    public static final String HOUR = "ga:hour";
    public static final String JAVA_ENABLED = "ga:javaEnabled";
    public static final String FLASH_VERSION = "ga:flashVersion";
    public static final String LANGUAGE = "ga:language";
    public static final String LATITUDE = "ga:latitude";
    public static final String LONGITUDE = "ga:longitude";
    public static final String NETWORK_DOMAIN = "ga:networkDomain";
    public static final String NETWORK_LOCATION = "ga:networkLocation";
    public static final String PAGE_DEPTH = "ga:pageDepth";
    public static final String OPERATING_SYSTEM = "ga:operatingSystem";
    public static final String OPERATING_SYSTEM_VERSION = "ga:operatingSystemVersion";
    public static final String REGION = "ga:region";
    public static final String SCREEN_COLORS = "ga:screenColors";
    public static final String SCREEN_RESOLUTION = "ga:screenResolution";
    public static final String SUB_CONTINENT = "ga:subContinent";
    public static final String VISITOR_TYPE = "ga:visitorType";
    public static final String TITLE = "title";


    public static final String PAGEVIEWS = "ga:pageviews";
    public static final String BOUNCES = "ga:bounces";
    public static final String ENTRANCES = "ga:entrances";
    public static final String EXITS = "ga:exits";
    public static final String NEW_VISITS = "ga:newVisits";
    public static final String TIME_ON_PAGE = "ga:timeOnPage";
    public static final String TIME_ON_SITE = "ga:timeOnSite";
    public static final String VISITS = "ga:visits";

    public static final String AD_CONTENT = "ga:adContent";
    public static final String AD_GROUP = "ga:adGroup";
    public static final String AD_SLOT = "ga:adSlot";
    public static final String AD_SLOT_POSITION = "ga:adSlotPosition";
    public static final String AD_CAMPAIGN = "ga:campaign";
    public static final String AD_KEYWORD = "ga:keyword";
    public static final String AD_MEDIUM = "ga:medium";
    public static final String AD_REFERRAL_PATH = "ga:referralPath";
    public static final String AD_SOURCE = "ga:source";

    public static final String AD_CLICKS = "ga:adClicks";
    public static final String AD_COST = "ga:adCost";
    public static final String AD_CPC = "ga:CPC";
    public static final String AD_CPM = "ga:CPM";
    public static final String AD_CTR = "ga:CTR";
    public static final String AD_IMPRESSIONS = "ga:impression";

    public static final String CONTENT_EXIT_PAGE_PATH = "ga:exitPagePath";
    public static final String CONTENT_LANDING_PAGE_PATH = "ga:landingPagePath";
    public static final String CONTENT_PAGE_PATH = "ga:pagePath";
    public static final String CONTENT_PAGE_TITLE = "ga:pageTitle";

    public static final String CONTENT_UNIQUE_VIEWS = "ga:uniquePageViews";

    public static final String EC_AFFILIATION = "ga:affiliation";
    public static final String EC_DAYS_TO = "ga:daysToTransaction";
    public static final String EC_CATEGORY = "ga:productCategory";
    public static final String EC_NAME = "ga:productName";
    public static final String EC_SKU = "ga:productSku";    
    public static final String EC_TID = "ga:transactionId";

    public static final String EC_ITEM_REVENUE = "ga:itemRevenue";
    public static final String EC_ITEM_QUANTITY = "ga:itemQuantity";
    public static final String EC_TRANSACTION_REVENUE = "ga:transactionRevenue";
    public static final String EC_TRANSACTIONS = "ga:transactions";
    public static final String EC_TRANSACTION_SHIPPING = "ga:transactionShipping";
    public static final String EC_TRANSACTION_TAX = "ga:transactionTax";
    public static final String EC_UNIQUE_PURCHASES = "ga:uniquePurchases";

    public static final String SEARCH_CATEGORY = "ga:searchCategory";
    public static final String SEARCH_DESTINATION = "ga:searchDestinationPage";
    public static final String SEARCH_KEYWORD = "ga:searchKeyword";
    public static final String SEARCH_KEYWORD_REFINEMENT = "ga:searchKeywordRefinement";
    public static final String SEARCH_SEARCH_START = "ga:searchStart";
    public static final String SEARCH_SEARCH_USED = "ga:searchUed";

    public static final String SEARCH_DEPTH = "ga:searchDepth";
    public static final String SEARCH_DURATION = "ga:searchDuration";
    public static final String SEARCH_EXITS = "ga:searchExits";
    public static final String SEARCH_REFINEMENTS = "ga:searchRefinements";
    public static final String SEARCH_UNIQUES = "ga:searchUniques";
    public static final String SEARCH_VISITS = "ga:searchVisits";

    public int getRequiredAccountTier() {
        return Account.GROUP;
    }

    public FeedType getFeedType() {
        return FeedType.GOOGLE_ANALYTICS;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    @Override
    public Feed createFeedObject() {
        return new GoogleAnalyticsFeed();
    }

    public String validateCredentials(Credentials credentials) {
        try {
            AnalyticsService as = new AnalyticsService("gaExportAPI_acctSample_v1.0");
            as.setUserCredentials(credentials.getUserName(), credentials.getPassword());
            String baseUrl = "https://www.google.com/analytics/feeds/accounts/default";
            as.getFeed(new URL(baseUrl), AccountFeed.class);
            return null;
        } catch (IOException e) {
            return e.getMessage();
        } catch (ServiceException e) {
            return e.getMessage();
        }
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        return new DataSet();
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(BROWSER, BROWSER_VERSION, CITY, CONNECTION_SPEED, CONTINENT, COUNT_OF_VISITS, COUNTRY,
                DATE, DAYS_SINCE_LAST_VISIT, HOSTNAME, HOUR, JAVA_ENABLED, FLASH_VERSION, LANGUAGE, LONGITUDE,
                LATITUDE, NETWORK_DOMAIN, NETWORK_LOCATION, OPERATING_SYSTEM, OPERATING_SYSTEM_VERSION,
                REGION, SCREEN_COLORS, SCREEN_RESOLUTION, SUB_CONTINENT, VISITOR_TYPE, PAGEVIEWS, TITLE,
                BOUNCES, ENTRANCES, EXITS, NEW_VISITS, TIME_ON_PAGE, TIME_ON_SITE, VISITS,
                AD_CONTENT, AD_GROUP, AD_SLOT, AD_SLOT_POSITION, AD_CAMPAIGN, AD_KEYWORD, AD_MEDIUM,
                AD_REFERRAL_PATH, AD_SOURCE, AD_CLICKS, AD_COST, AD_CPC, AD_CPM, AD_CTR, AD_IMPRESSIONS,
                CONTENT_EXIT_PAGE_PATH, CONTENT_LANDING_PAGE_PATH, CONTENT_PAGE_PATH, CONTENT_PAGE_TITLE,
                CONTENT_UNIQUE_VIEWS, EC_AFFILIATION, EC_DAYS_TO, EC_CATEGORY, EC_NAME, EC_SKU, EC_TID,
                EC_ITEM_REVENUE, EC_ITEM_QUANTITY, EC_TRANSACTION_REVENUE, EC_TRANSACTIONS,
                EC_TRANSACTION_SHIPPING, EC_TRANSACTION_TAX, EC_UNIQUE_PURCHASES, SEARCH_CATEGORY,
                SEARCH_DESTINATION, SEARCH_KEYWORD, SEARCH_KEYWORD_REFINEMENT, SEARCH_SEARCH_START,
                SEARCH_SEARCH_USED, SEARCH_DEPTH, SEARCH_DURATION, SEARCH_EXITS, SEARCH_REFINEMENTS,
                SEARCH_UNIQUES, SEARCH_VISITS);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(BROWSER), "Browser"));
        analysisItems.add(new AnalysisDimension(keys.get(BROWSER_VERSION), "Browser Version"));
        analysisItems.add(new AnalysisDimension(keys.get(CITY), "City"));
        analysisItems.add(new AnalysisDimension(keys.get(CONNECTION_SPEED), "Connection Speed"));
        analysisItems.add(new AnalysisDimension(keys.get(CONTINENT), "Continent"));
        analysisItems.add(new AnalysisDimension(keys.get(COUNT_OF_VISITS), "Count of Visits"));
        analysisItems.add(new AnalysisDimension(keys.get(COUNTRY), "Country"));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), "Date", AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(DAYS_SINCE_LAST_VISIT), "Days Since Last Visit"));
        analysisItems.add(new AnalysisDimension(keys.get(HOSTNAME), "Host Name"));
        analysisItems.add(new AnalysisDimension(keys.get(HOUR), "HOUR"));
        analysisItems.add(new AnalysisDimension(keys.get(JAVA_ENABLED), "Java Enabled"));
        analysisItems.add(new AnalysisDimension(keys.get(FLASH_VERSION), "Flash Version"));
        analysisItems.add(new AnalysisDimension(keys.get(LANGUAGE), "Language"));
        analysisItems.add(new AnalysisDimension(keys.get(LONGITUDE), "Longitude"));
        analysisItems.add(new AnalysisDimension(keys.get(LATITUDE), "Latitude"));
        analysisItems.add(new AnalysisDimension(keys.get(NETWORK_DOMAIN), "Network Domain"));
        analysisItems.add(new AnalysisDimension(keys.get(NETWORK_LOCATION), "Network Location"));
        analysisItems.add(new AnalysisDimension(keys.get(OPERATING_SYSTEM), "Operating System"));
        analysisItems.add(new AnalysisDimension(keys.get(OPERATING_SYSTEM_VERSION), "Operating System Version"));
        analysisItems.add(new AnalysisDimension(keys.get(REGION), "Region"));
        analysisItems.add(new AnalysisDimension(keys.get(SCREEN_COLORS), "Screen Colors"));
        analysisItems.add(new AnalysisDimension(keys.get(SCREEN_RESOLUTION), "Screen Resolution"));
        analysisItems.add(new AnalysisDimension(keys.get(SUB_CONTINENT), "Sub Continent"));
        analysisItems.add(new AnalysisDimension(keys.get(VISITOR_TYPE), "Visitor Type"));
        analysisItems.add(new AnalysisDimension(keys.get(TITLE), "Title"));
        analysisItems.add(new AnalysisMeasure(keys.get(PAGEVIEWS), "Page Views", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(BOUNCES), "Bounces", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(ENTRANCES), "Entrances", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EXITS), "Exits", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(NEW_VISITS), "New Visits", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(TIME_ON_PAGE), "Time on Page", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(TIME_ON_SITE), "Time on Site", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(VISITS), "Visits", AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(AD_CONTENT), "Ad Content"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_GROUP), "Ad Group"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_SLOT), "Ad Slot"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_SLOT_POSITION), "Ad Slot Position"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_CAMPAIGN), "Ad Campaign"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_KEYWORD), "Ad Keyword"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_MEDIUM), "Ad Medium"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_REFERRAL_PATH), "Ad Referral Path"));
        analysisItems.add(new AnalysisDimension(keys.get(AD_SOURCE), "Ad Source"));
        analysisItems.add(new AnalysisMeasure(keys.get(AD_CLICKS), "Ad Clicks", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(AD_COST), "Ad Cost", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(AD_CPC), "Cost to Advertiser Per Click", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(AD_CPM), "Cost per Thousand Impressions", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(AD_CTR), "Click-Through Rate", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(AD_IMPRESSIONS), "Impressions", AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT_EXIT_PAGE_PATH), "Exit Page Path"));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT_LANDING_PAGE_PATH), "Landing Page Path"));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT_PAGE_PATH), "Page Path"));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT_PAGE_TITLE), "Page Title"));
        analysisItems.add(new AnalysisMeasure(keys.get(CONTENT_UNIQUE_VIEWS), "Unique Page Views", AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(EC_AFFILIATION), "Product Affiliate"));
        analysisItems.add(new AnalysisDimension(keys.get(EC_DAYS_TO), "Days to Transaction"));
        analysisItems.add(new AnalysisDimension(keys.get(EC_CATEGORY), "Product Category"));
        analysisItems.add(new AnalysisDimension(keys.get(EC_NAME), "Product Name"));
        analysisItems.add(new AnalysisDimension(keys.get(EC_SKU), "Product Sku"));
        analysisItems.add(new AnalysisDimension(keys.get(EC_TID), "Transaction ID"));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_ITEM_REVENUE), "Item Revenue", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_ITEM_QUANTITY), "Item Quantity", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTION_REVENUE), "Transaction Revenue", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTIONS), "Transactions", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTION_SHIPPING), "Transaction Shipping", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTION_TAX), "Transaction Tax", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(EC_UNIQUE_PURCHASES), "Unique Purchases", AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(SEARCH_CATEGORY), "Search Category"));
        analysisItems.add(new AnalysisDimension(keys.get(SEARCH_DESTINATION), "Search Destination"));
        analysisItems.add(new AnalysisDimension(keys.get(SEARCH_KEYWORD), "Search Keyword"));
        analysisItems.add(new AnalysisDimension(keys.get(SEARCH_KEYWORD_REFINEMENT), "Search Keyword Refinement"));
        analysisItems.add(new AnalysisDimension(keys.get(SEARCH_SEARCH_START), "Search Start Page"));
        analysisItems.add(new AnalysisDimension(keys.get(SEARCH_SEARCH_USED), "Search Used"));
        analysisItems.add(new AnalysisMeasure(keys.get(SEARCH_DEPTH), "Search Depth", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(SEARCH_DURATION), "Search Duration", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(SEARCH_EXITS), "Search Exits", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(SEARCH_REFINEMENTS), "Search Refinements", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(SEARCH_UNIQUES), "Unique Searches", AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(SEARCH_VISITS), "Search Visits", AggregationTypes.SUM));
        return analysisItems;
    }

    public void customStorage(Connection conn) throws SQLException {
    }

    public void customLoad(Connection conn) throws SQLException {
    }
}
