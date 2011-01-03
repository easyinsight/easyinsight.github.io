package com.easyinsight.datafeeds.ganalytics;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

import flex.messaging.FlexContext;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
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
    public static final String SOURCE = "ga:source";
    public static final String SUB_CONTINENT = "ga:subContinent";
    public static final String VISITOR_TYPE = "ga:visitorType";
    public static final String TITLE = "title";
    public static final String BOUNCE_RATE = "Bounce Rate";


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
    public static final String SEARCH_SEARCH_START = "ga:searchStartPage";
    public static final String SEARCH_SEARCH_USED = "ga:searchUsed";

    public static final String SEARCH_DEPTH = "ga:searchDepth";
    public static final String SEARCH_DURATION = "ga:searchDuration";
    public static final String SEARCH_EXITS = "ga:searchExits";
    public static final String SEARCH_REFINEMENTS = "ga:searchRefinements";
    public static final String SEARCH_UNIQUES = "ga:searchUniques";
    public static final String SEARCH_VISITS = "ga:searchVisits";

    public static final String EVENT_CATEGORY = "ga:eventCategory";
    public static final String EVENT_ACTION = "ga:eventAction";
    public static final String EVENT_LABEL = "ga:eventLabel";

    public static final String TOTAL_EVENTS = "ga:totalEvents";
    public static final String UNIQUE_EVENTS = "ga:uniqueEvents";
    public static final String EVENT_VALUE = "ga:eventValue";

    public static String[] generalDimensions = { BROWSER, BROWSER_VERSION, CITY, CONNECTION_SPEED, CONTINENT, CONTINENT,
        COUNT_OF_VISITS, COUNTRY, DATE, DAYS_SINCE_LAST_VISIT, HOSTNAME, HOUR, JAVA_ENABLED, FLASH_VERSION, LATITUDE,
        LONGITUDE, NETWORK_DOMAIN, LANGUAGE, NETWORK_DOMAIN, NETWORK_LOCATION, PAGE_DEPTH, OPERATING_SYSTEM,
        OPERATING_SYSTEM_VERSION, REGION, SCREEN_COLORS, SCREEN_RESOLUTION, SOURCE, SUB_CONTINENT, VISITOR_TYPE, TITLE};

    public static String[] generalMeasures = { VISITS };

    public static String[] adDimensions = { AD_CONTENT, AD_GROUP, AD_SLOT, AD_SLOT_POSITION, AD_CAMPAIGN, AD_KEYWORD,
        AD_MEDIUM, AD_REFERRAL_PATH, AD_SOURCE };

    public static String[] adMeasures = { AD_CLICKS };

    public static String[] contentDimensions = { CONTENT_EXIT_PAGE_PATH, CONTENT_LANDING_PAGE_PATH, CONTENT_PAGE_PATH,
        CONTENT_PAGE_TITLE };

    public static String[] contentMeasures = { CONTENT_UNIQUE_VIEWS };

    public static String[] ecommerceDimensions = { EC_AFFILIATION, EC_DAYS_TO, EC_CATEGORY, EC_NAME, EC_SKU, EC_TID };

    public static String[] ecommerceMeasures = { EC_ITEM_QUANTITY };

    public static String[] searchDimensions = { SEARCH_CATEGORY, SEARCH_DESTINATION, SEARCH_KEYWORD, SEARCH_KEYWORD_REFINEMENT,
        SEARCH_SEARCH_START, SEARCH_SEARCH_USED };

    public static String[] searchMeasures = { SEARCH_VISITS };

    public static String[] eventDimensions = { EVENT_CATEGORY, EVENT_ACTION, EVENT_LABEL };

    public static String[] eventMeasures = { TOTAL_EVENTS, UNIQUE_EVENTS, EVENT_VALUE };

    private static Map<String, String> compiledMap;

    private static void compileMap() {
        if (compiledMap == null) {
            compiledMap = new HashMap<String, String>();
            for (String dimension : generalDimensions) {
                compiledMap.put(dimension, generalMeasures[0]);
            }
            for (String dimension : adDimensions) {
                compiledMap.put(dimension, adMeasures[0]);
            }
            for (String dimension : contentDimensions) {
                compiledMap.put(dimension, contentMeasures[0]);
            }
            for (String dimension : ecommerceDimensions) {
                compiledMap.put(dimension, ecommerceMeasures[0]);
            }
            for (String dimension : searchDimensions) {
                compiledMap.put(dimension, searchMeasures[0]);
            }
            for (String dimension : eventDimensions) {
                compiledMap.put(dimension, eventMeasures[0]);
            }
        }
    }

    public static String getMeasure(String dimension) {
        compileMap();
        return compiledMap.get(dimension);
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        GoogleAnalyticsDataSource googleAnalyticsDataSource = (GoogleAnalyticsDataSource) super.clone(conn);
        googleAnalyticsDataSource.setTokenKey(null);
        googleAnalyticsDataSource.setTokenSecret(null);
        return googleAnalyticsDataSource;
    }

    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public String getFilterExampleMessage() {
        return "For example, drag Visitor Type into the area to the right to restrict the KPI to only show metrics around Returning Visitors.";
    }

    public FeedType getFeedType() {
        return FeedType.GOOGLE_ANALYTICS;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new GoogleAnalyticsFeed(tokenKey, tokenSecret);
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) {
        return new DataSet();
    }

    @Override
    public void exchangeTokens(EIConnection conn) throws Exception {
        try {
            if (pin != null && !"".equals(pin)) {
                OAuthConsumer consumer = (OAuthConsumer) FlexContext.getHttpRequest().getSession().getAttribute("oauthConsumer");
                OAuthProvider provider = (OAuthProvider) FlexContext.getHttpRequest().getSession().getAttribute("oauthProvider");
                provider.retrieveAccessToken(consumer, pin.trim());
                tokenKey = consumer.getToken();
                tokenSecret = consumer.getTokenSecret();
                pin = null;
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
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
                SEARCH_UNIQUES, SEARCH_VISITS, SOURCE, BOUNCE_RATE, EVENT_CATEGORY, EVENT_ACTION, EVENT_LABEL,
                TOTAL_EVENTS, EVENT_VALUE, UNIQUE_EVENTS);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        List<AnalysisItem> standardItems = new ArrayList<AnalysisItem>();
        List<AnalysisItem> adItems = new ArrayList<AnalysisItem>();
        List<AnalysisItem> contentItems = new ArrayList<AnalysisItem>();
        List<AnalysisItem> ecommerceItems = new ArrayList<AnalysisItem>();
        List<AnalysisItem> searchItems = new ArrayList<AnalysisItem>();
        List<AnalysisItem> eventItems = new ArrayList<AnalysisItem>();

        standardItems.add(new AnalysisDimension(keys.get(BROWSER), "Browser"));
        standardItems.add(new AnalysisDimension(keys.get(BROWSER_VERSION), "Browser Version"));
        standardItems.add(new AnalysisDimension(keys.get(CITY), "City"));
        standardItems.add(new AnalysisDimension(keys.get(CONNECTION_SPEED), "Connection Speed"));
        standardItems.add(new AnalysisDimension(keys.get(CONTINENT), "Continent"));
        standardItems.add(new AnalysisDimension(keys.get(COUNT_OF_VISITS), "Count of Visits"));
        standardItems.add(new AnalysisDimension(keys.get(COUNTRY), "Country"));
        standardItems.add(new AnalysisDateDimension(keys.get(DATE), "Date", AnalysisDateDimension.DAY_LEVEL));
        standardItems.add(new AnalysisDimension(keys.get(DAYS_SINCE_LAST_VISIT), "Days Since Last Visit"));
        standardItems.add(new AnalysisDimension(keys.get(HOSTNAME), "Host Name"));
        standardItems.add(new AnalysisDimension(keys.get(HOUR), "Hour"));
        standardItems.add(new AnalysisDimension(keys.get(JAVA_ENABLED), "Java Enabled"));
        standardItems.add(new AnalysisDimension(keys.get(FLASH_VERSION), "Flash Version"));
        standardItems.add(new AnalysisDimension(keys.get(LANGUAGE), "Language"));
        standardItems.add(new AnalysisDimension(keys.get(LONGITUDE), "Longitude"));
        standardItems.add(new AnalysisDimension(keys.get(LATITUDE), "Latitude"));
        standardItems.add(new AnalysisDimension(keys.get(NETWORK_DOMAIN), "Network Domain"));
        standardItems.add(new AnalysisDimension(keys.get(NETWORK_LOCATION), "Network Location"));
        standardItems.add(new AnalysisDimension(keys.get(OPERATING_SYSTEM), "Operating System"));
        standardItems.add(new AnalysisDimension(keys.get(OPERATING_SYSTEM_VERSION), "Operating System Version"));
        standardItems.add(new AnalysisDimension(keys.get(REGION), "Region"));
        standardItems.add(new AnalysisDimension(keys.get(SCREEN_COLORS), "Screen Colors"));
        standardItems.add(new AnalysisDimension(keys.get(SCREEN_RESOLUTION), "Screen Resolution"));
        standardItems.add(new AnalysisDimension(keys.get(SOURCE), "Source"));
        standardItems.add(new AnalysisDimension(keys.get(SUB_CONTINENT), "Sub Continent"));
        standardItems.add(new AnalysisDimension(keys.get(VISITOR_TYPE), "Visitor Type"));
        standardItems.add(new AnalysisDimension(keys.get(TITLE), "Title"));
        standardItems.add(new AnalysisMeasure(keys.get(PAGEVIEWS), "Page Views", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(BOUNCES), "Bounces", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(ENTRANCES), "Entrances", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(EXITS), "Exits", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(NEW_VISITS), "New Visits", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(TIME_ON_PAGE), "Time on Page", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(TIME_ON_SITE), "Time on Site", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(VISITS), "Visits", AggregationTypes.SUM));
        AnalysisCalculation bounceRate = new AnalysisCalculation();
        bounceRate.setKey(keys.get(BOUNCE_RATE));
        bounceRate.setCalculationString("([ga:bounces] / [ga:visits]) * 100");
        standardItems.add(bounceRate);
        adItems.add(new AnalysisDimension(keys.get(AD_CONTENT), "Ad Content"));
        adItems.add(new AnalysisDimension(keys.get(AD_GROUP), "Ad Group"));
        adItems.add(new AnalysisDimension(keys.get(AD_SLOT), "Ad Slot"));
        adItems.add(new AnalysisDimension(keys.get(AD_SLOT_POSITION), "Ad Slot Position"));
        adItems.add(new AnalysisDimension(keys.get(AD_CAMPAIGN), "Ad Campaign"));
        adItems.add(new AnalysisDimension(keys.get(AD_KEYWORD), "Ad Keyword"));
        adItems.add(new AnalysisDimension(keys.get(AD_MEDIUM), "Ad Medium"));
        adItems.add(new AnalysisDimension(keys.get(AD_REFERRAL_PATH), "Ad Referral Path"));
        adItems.add(new AnalysisDimension(keys.get(AD_SOURCE), "Ad Source"));
        adItems.add(new AnalysisMeasure(keys.get(AD_CLICKS), "Ad Clicks", AggregationTypes.SUM));
        adItems.add(new AnalysisMeasure(keys.get(AD_COST), "Ad Cost", AggregationTypes.SUM));
        adItems.add(new AnalysisMeasure(keys.get(AD_CPC), "Cost to Advertiser Per Click", AggregationTypes.SUM));
        adItems.add(new AnalysisMeasure(keys.get(AD_CPM), "Cost per Thousand Impressions", AggregationTypes.SUM));
        adItems.add(new AnalysisMeasure(keys.get(AD_CTR), "Click-Through Rate", AggregationTypes.SUM));
        adItems.add(new AnalysisMeasure(keys.get(AD_IMPRESSIONS), "Impressions", AggregationTypes.SUM));
        contentItems.add(new AnalysisDimension(keys.get(CONTENT_EXIT_PAGE_PATH), "Exit Page Path"));
        contentItems.add(new AnalysisDimension(keys.get(CONTENT_LANDING_PAGE_PATH), "Landing Page Path"));
        contentItems.add(new AnalysisDimension(keys.get(CONTENT_PAGE_PATH), "Page Path"));
        contentItems.add(new AnalysisDimension(keys.get(CONTENT_PAGE_TITLE), "Page Title"));
        contentItems.add(new AnalysisMeasure(keys.get(CONTENT_UNIQUE_VIEWS), "Unique Page Views", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisDimension(keys.get(EC_AFFILIATION), "Product Affiliate"));
        ecommerceItems.add(new AnalysisDimension(keys.get(EC_DAYS_TO), "Days to Transaction"));
        ecommerceItems.add(new AnalysisDimension(keys.get(EC_CATEGORY), "Product Category"));
        ecommerceItems.add(new AnalysisDimension(keys.get(EC_NAME), "Product Name"));
        ecommerceItems.add(new AnalysisDimension(keys.get(EC_SKU), "Product Sku"));
        ecommerceItems.add(new AnalysisDimension(keys.get(EC_TID), "Transaction ID"));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_ITEM_REVENUE), "Item Revenue", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_ITEM_QUANTITY), "Item Quantity", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTION_REVENUE), "Transaction Revenue", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTIONS), "Transactions", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTION_SHIPPING), "Transaction Shipping", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_TRANSACTION_TAX), "Transaction Tax", AggregationTypes.SUM));
        ecommerceItems.add(new AnalysisMeasure(keys.get(EC_UNIQUE_PURCHASES), "Unique Purchases", AggregationTypes.SUM));
        searchItems.add(new AnalysisDimension(keys.get(SEARCH_CATEGORY), "Search Category"));
        searchItems.add(new AnalysisDimension(keys.get(SEARCH_DESTINATION), "Search Destination"));
        searchItems.add(new AnalysisDimension(keys.get(SEARCH_KEYWORD), "Search Keyword"));
        searchItems.add(new AnalysisDimension(keys.get(SEARCH_KEYWORD_REFINEMENT), "Search Keyword Refinement"));
        searchItems.add(new AnalysisDimension(keys.get(SEARCH_SEARCH_START), "Search Start Page"));
        searchItems.add(new AnalysisDimension(keys.get(SEARCH_SEARCH_USED), "Search Used"));
        searchItems.add(new AnalysisMeasure(keys.get(SEARCH_DEPTH), "Search Depth", AggregationTypes.SUM));
        searchItems.add(new AnalysisMeasure(keys.get(SEARCH_DURATION), "Search Duration", AggregationTypes.SUM));
        searchItems.add(new AnalysisMeasure(keys.get(SEARCH_EXITS), "Search Exits", AggregationTypes.SUM));
        searchItems.add(new AnalysisMeasure(keys.get(SEARCH_REFINEMENTS), "Search Refinements", AggregationTypes.SUM));
        searchItems.add(new AnalysisMeasure(keys.get(SEARCH_UNIQUES), "Unique Searches", AggregationTypes.SUM));
        searchItems.add(new AnalysisMeasure(keys.get(SEARCH_VISITS), "Search Visits", AggregationTypes.SUM));
        eventItems.add(new AnalysisDimension(keys.get(EVENT_ACTION), "Event Action"));
        eventItems.add(new AnalysisDimension(keys.get(EVENT_LABEL), "Event Label"));
        eventItems.add(new AnalysisDimension(keys.get(EVENT_CATEGORY), "Event Category"));
        eventItems.add(new AnalysisMeasure(keys.get(TOTAL_EVENTS), "Total Events", AggregationTypes.SUM));
        eventItems.add(new AnalysisMeasure(keys.get(UNIQUE_EVENTS), "Unique Events", AggregationTypes.SUM));
        eventItems.add(new AnalysisMeasure(keys.get(EVENT_VALUE), "Event Value", AggregationTypes.SUM));
        if (getDataFeedID() == 0) {
            FeedFolder visitorFolder = defineFolder("Visitor");
            visitorFolder.getChildItems().addAll(standardItems);
            FeedFolder adFolder = defineFolder("Ad Words");
            adFolder.getChildItems().addAll(adItems);
            FeedFolder contentFolder = defineFolder("Content");
            contentFolder.getChildItems().addAll(contentItems);
            FeedFolder ecommerceFolder = defineFolder("eCommerce");
            ecommerceFolder.getChildItems().addAll(ecommerceItems);
            FeedFolder searchFolder = defineFolder("Search");
            searchFolder.getChildItems().addAll(searchItems);
            FeedFolder eventFolder = defineFolder("Events");
            eventFolder.getChildItems().addAll(eventItems);
        }
        analysisItems.addAll(standardItems);
        analysisItems.addAll(adItems);
        analysisItems.addAll(contentItems);
        analysisItems.addAll(ecommerceItems);
        analysisItems.addAll(searchItems);
        analysisItems.addAll(eventItems);
        return analysisItems;
    }

    private String pin;
    private String tokenKey;
    private String tokenSecret;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GOOGLE_ANALYTICS WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO GOOGLE_ANALYTICS (DATA_SOURCE_ID, TOKEN_KEY, TOKEN_SECRET) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, tokenKey);
        insertStmt.setString(3, tokenSecret);
        insertStmt.execute();
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TOKEN_KEY, TOKEN_SECRET FROM GOOGLE_ANALYTICS WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            tokenKey = rs.getString(1);
            tokenSecret = rs.getString(2);
        }
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIForDateFilter("Visits in the Last Week", "user.png", (AnalysisMeasure) findAnalysisItem(GoogleAnalyticsDataSource.VISITS),
                (AnalysisDimension) findAnalysisItem(GoogleAnalyticsDataSource.DATE), MaterializedRollingFilterDefinition.LAST_FULL_WEEK,
                null, KPI.GOOD, 7));
        kpis.add(KPIUtil.createKPIForDateFilter("New Visits in the Last Week", "user3.png", (AnalysisMeasure) findAnalysisItem(GoogleAnalyticsDataSource.NEW_VISITS),
                (AnalysisDimension) findAnalysisItem(GoogleAnalyticsDataSource.DATE), MaterializedRollingFilterDefinition.LAST_FULL_WEEK,
                null, KPI.GOOD, 7));
        return kpis;
    }
}
