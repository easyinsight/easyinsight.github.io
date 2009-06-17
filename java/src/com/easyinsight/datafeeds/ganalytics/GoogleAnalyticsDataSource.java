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
                BOUNCES, ENTRANCES, EXITS, NEW_VISITS, TIME_ON_PAGE, TIME_ON_SITE, VISITS);
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
        return analysisItems;
    }

    public void customStorage(Connection conn) throws SQLException {
    }

    public void customLoad(Connection conn) throws SQLException {
    }
}
