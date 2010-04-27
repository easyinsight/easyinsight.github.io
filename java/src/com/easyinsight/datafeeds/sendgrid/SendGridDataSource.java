package com.easyinsight.datafeeds.sendgrid;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Apr 14, 2010
 * Time: 9:08:39 PM
 */
public class SendGridDataSource extends ServerDataSourceDefinition {
    public static final String CATEGORY = "Category";
    public static final String DATE = "Date";
    public static final String REQUESTS = "Requests";
    public static final String BOUNCES = "Bounces";
    public static final String REPEAT_BOUNCES = "Repeat Bounces";
    public static final String CLICKS = "Clicks";
    public static final String DELIVERED = "Delivered";
    public static final String OPENS = "Opens";
    public static final String SPAM_REPORTS = "Spam Reports";
    public static final String REPEAT_SPAM_REPORTS = "Repeat Spam Reports";
    public static final String UNSUBSCRIBES = "Unsubscribes";
    public static final String REPEAT_UNSUBSCRIBES = "Repeat Unsubscribes";
    public static final String INVALID_EMAILS = "Invalid Emails";

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CATEGORY, DATE, REQUESTS, BOUNCES, CLICKS, OPENS, SPAM_REPORTS, DELIVERED, UNSUBSCRIBES, INVALID_EMAILS,
                REPEAT_BOUNCES, REPEAT_SPAM_REPORTS, REPEAT_UNSUBSCRIBES);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CATEGORY), true));
        items.add(new AnalysisDateDimension(keys.get(DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(REQUESTS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(DELIVERED), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(BOUNCES), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(CLICKS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(OPENS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(UNSUBSCRIBES), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(SPAM_REPORTS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(INVALID_EMAILS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(REPEAT_BOUNCES), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(REPEAT_UNSUBSCRIBES), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(REPEAT_SPAM_REPORTS), AggregationTypes.SUM));
        return items;
    }

    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    public FeedType getFeedType() {
        return FeedType.SENDGRID;
    }

    @Override
    public Feed createFeedObject() {
        return new SendGridFeed();
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    @Override
    public String validateCredentials(Credentials credentials) {
        return null;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIForDateFilter("Clicks in the Last Week", "user.png", (AnalysisMeasure) findAnalysisItem(SendGridDataSource.CLICKS),
                (AnalysisDimension) findAnalysisItem(SendGridDataSource.DATE), MaterializedRollingFilterDefinition.LAST_FULL_WEEK,
                null, KPI.GOOD, 7));
        kpis.add(KPIUtil.createKPIForDateFilter("Delivered in the Last Week", "user.png", (AnalysisMeasure) findAnalysisItem(SendGridDataSource.DELIVERED),
                (AnalysisDimension) findAnalysisItem(SendGridDataSource.DATE), MaterializedRollingFilterDefinition.LAST_FULL_WEEK,
                null, KPI.GOOD, 7));
        kpis.add(KPIUtil.createKPIForDateFilter("Bounces in the Last Week", "user.png", (AnalysisMeasure) findAnalysisItem(SendGridDataSource.BOUNCES),
                (AnalysisDimension) findAnalysisItem(SendGridDataSource.DATE), MaterializedRollingFilterDefinition.LAST_FULL_WEEK,
                null, KPI.GOOD, 7));
        kpis.add(KPIUtil.createKPIForDateFilter("Opens in the Last Week", "user.png", (AnalysisMeasure) findAnalysisItem(SendGridDataSource.OPENS),
                (AnalysisDimension) findAnalysisItem(SendGridDataSource.DATE), MaterializedRollingFilterDefinition.LAST_FULL_WEEK,
                null, KPI.GOOD, 7));
        return kpis;
    }
}
