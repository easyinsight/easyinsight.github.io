package com.easyinsight.datafeeds.ganalytics;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.CredentialRequirement;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.*;
import com.easyinsight.users.Credentials;
import com.easyinsight.logging.LogClass;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.AccountEntry;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.util.AuthenticationException;

import java.util.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.jetbrains.annotations.NotNull;

/**
 * User: James Boe
 * Date: Jun 11, 2009
 * Time: 1:59:16 PM
 */
public class GoogleAnalyticsFeed extends Feed {

    private AnalyticsService as;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static DateFormat outboundDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public GoogleAnalyticsFeed() {
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        try {
            AnalysisItem queryItem;
            if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                queryItem = analysisHierarchyItem.getHierarchyLevel().getAnalysisItem();
            } else {
                queryItem = analysisItem;
            }
            AnalyticsService as = getAnalyticsService(insightRequestMetadata.getCredentialForDataSource(getFeedID()));

            if ("title".equals(queryItem.getKey().toKeyString())) {
                String baseUrl = "https://www.google.com/analytics/feeds/accounts/default";
                AccountFeed accountFeed = as.getFeed(new URL(baseUrl), AccountFeed.class);
                for (AccountEntry accountEntry : accountFeed.getEntries()) {
                    String title = accountEntry.getTitle().getPlainText();
                    metadata.addValue(analysisItem, new StringValue(title));
                }
            } else {
                String baseUrl = "https://www.google.com/analytics/feeds/accounts/default";
                AccountFeed accountFeed = as.getFeed(new URL(baseUrl), AccountFeed.class);
                for (AccountEntry accountEntry : accountFeed.getEntries()) {
                    String ids = accountEntry.getTableId().getValue();
                    StringBuilder urlBuilder = new StringBuilder("https://www.google.com/analytics/feeds/data?ids=");
                    urlBuilder.append(ids);

                    urlBuilder.append("&dimensions=");
                    urlBuilder.append(queryItem.getKey().toKeyString());
                    urlBuilder.append("&metrics=");
                    // TODO: update for categories other than basic
                    urlBuilder.append(GoogleAnalyticsDataSource.getMeasure(queryItem.getKey().toKeyString()));
                    urlBuilder.append("&start-date=2009-06-09&end-date=2009-06-15");
                    URL reportUrl = new URL(urlBuilder.toString());
                    DataFeed feed = as.getFeed(reportUrl, DataFeed.class);

                    for (DataEntry entry : feed.getEntries()) {
                        metadata.addValue(queryItem, getValue(queryItem, entry));
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

        return metadata;
    }

    private AnalyticsService getAnalyticsService(@NotNull Credentials credentials) throws AuthenticationException {
        if (as == null) {
            as = new AnalyticsService("gaExportAPI_acctSample_v1.0");
            as.setUserCredentials(credentials.getUserName(), credentials.getPassword());
        }
        return as;
    }

    public List<CredentialRequirement> getCredentialRequirement() {
        List<CredentialRequirement> credentials = super.getCredentialRequirement();
        CredentialRequirement requirement = new CredentialRequirement();
        requirement.setDataSourceID(getFeedID());
        requirement.setDataSourceName(getName());
        requirement.setCredentialsDefinition(CredentialsDefinition.STANDARD_USERNAME_PW);
        credentials.add(requirement);
        return credentials;
    }

    public List<FilterDefinition> getIntrinsicFilters() {
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        AnalysisItem dateField = null;
        for (AnalysisItem analysisItem : getFields()) {
            if (analysisItem.getKey().toKeyString().equals(GoogleAnalyticsDataSource.DATE)) {
                dateField = analysisItem;
            }
        }
        rollingFilterDefinition.setField(dateField);
        rollingFilterDefinition.setIntrinsic(true);
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.WEEK);
        rollingFilterDefinition.setApplyBeforeAggregation(true);
        return Arrays.asList((FilterDefinition) rollingFilterDefinition);
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) {
        try {
            Collection<AnalysisDimension> dimensions = new ArrayList<AnalysisDimension>();
            Collection<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    measures.add((AnalysisMeasure) analysisItem);
                } else {
                    dimensions.add((AnalysisDimension) analysisItem);
                }
            }
            if (measures.size() == 0 && dimensions.size() > 0) {
                measures.add(getDefaultMeasure(dimensions));
            }
            if (measures.size() == 0 && dimensions.size() == 0) {
                return new DataSet();
            }

            DataSet dataSet = new DataSet();

            Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
            if (credentials == null) {
                // user needs to log in again
            }
            AnalyticsService as = getAnalyticsService(credentials);

            Date startDate = null;
            Date endDate = null;

            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition.getField().getKey().toKeyString().equals(GoogleAnalyticsDataSource.DATE)) {
                    if (filterDefinition instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition dateRange = (FilterDateRangeDefinition) filterDefinition;
                        startDate = dateRange.getStartDate();
                        endDate = dateRange.getEndDate();
                    } else if (filterDefinition instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filterDefinition;
                        endDate = new Date();
                        startDate = new Date(MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition.getInterval(), endDate));
                    }
                }
            }
            String startDateString = outboundDateFormat.format(startDate);
            String endDateString = outboundDateFormat.format(endDate);
            String baseUrl = "https://www.google.com/analytics/feeds/accounts/default";
            AccountFeed accountFeed = as.getFeed(new URL(baseUrl), AccountFeed.class);
            for (AccountEntry accountEntry : accountFeed.getEntries()) {
                String title = accountEntry.getTitle().getPlainText();
                String ids = accountEntry.getTableId().getValue();
                StringBuilder urlBuilder = new StringBuilder("https://www.google.com/analytics/feeds/data?ids=");
                urlBuilder.append(ids);

                urlBuilder.append("&dimensions=");
                Iterator<AnalysisDimension> dimIter = dimensions.iterator();
                while (dimIter.hasNext()) {
                    AnalysisDimension analysisDimension = dimIter.next();
                    urlBuilder.append(analysisDimension.getKey().toKeyString());
                    if (dimIter.hasNext()) {
                        urlBuilder.append(",");
                    }
                }
                urlBuilder.append("&metrics=");
                Iterator<AnalysisMeasure> measureIter = measures.iterator();
                while (measureIter.hasNext()) {
                    AnalysisMeasure analysisMeasure = measureIter.next();
                    urlBuilder.append(analysisMeasure.getKey().toKeyString());
                    if (measureIter.hasNext()) {
                        urlBuilder.append(",");
                    }
                }
                urlBuilder.append("&start-date=").append(startDateString).append("&end-date=").append(endDateString);
                /*String url = "https://www.google.com/analytics/feeds/data?ids=" + ids + "&dimensions=ga:browser,ga:city,ga:date,ga:visitorType,ga:latitude,ga:longitude" +
                "&metrics=ga:pageviews,ga:bounces,ga:timeOnPage,ga:visitors,ga:visits,ga:timeOnSite" +
                "&start-date=2009-06-09&end-date=2009-06-11";*/
                URL reportUrl = new URL(urlBuilder.toString());
                DataFeed feed = as.getFeed(reportUrl, DataFeed.class);

                for (DataEntry entry : feed.getEntries()) {
                    IRow row = dataSet.createRow();

                    for (AnalysisItem analysisItem : analysisItems) {
                        if ("title".equals(analysisItem.getKey().toKeyString())) {
                            row.addValue(analysisItem.getKey(), title);
                        } else {
                            row.addValue(analysisItem.getKey(), getValue(analysisItem, entry));
                        }
                    }

                }
            }
            //String ids = "ga:16750246";
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Value getValue(AnalysisItem analysisItem, DataEntry entry) throws ParseException {
        Value value;
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            Double doubleValue = entry.doubleValueOf(analysisItem.getKey().toKeyString());
            if (doubleValue == null) {
                doubleValue = (double) 0;
            }
            value = new NumericValue(doubleValue);
        } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            Date dateValue = dateFormat.parse(entry.stringValueOf(analysisItem.getKey().toKeyString()));
            value = new DateValue(dateValue);
        } else {
            value = new StringValue(entry.stringValueOf(analysisItem.getKey().toKeyString()));
        }
        return value;
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AnalysisMeasure getDefaultMeasure(Collection<AnalysisDimension> dimensions) {
        AnalysisMeasure analysisMeasure = null;
        AnalysisDimension dimension = dimensions.iterator().next();
        String measureName = GoogleAnalyticsDataSource.getMeasure(dimension.getKey().toKeyString());
        for (AnalysisItem analysisItem : getFields()) {
            if (measureName.equals(analysisItem.getKey().toKeyString())) {
                analysisMeasure = (AnalysisMeasure) analysisItem;
            }
        }
        return analysisMeasure;
    }
}
