package com.easyinsight.datafeeds.sendgrid;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private String sgUserName;
    private String sgPassword;

    public String getSgUserName() {
        return sgUserName;
    }

    public void setSgUserName(String sgUserName) {
        this.sgUserName = sgUserName;
    }

    public String getSgPassword() {
        return sgPassword;
    }

    public void setSgPassword(String sgPassword) {
        this.sgPassword = sgPassword;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        SendGridDataSource sendGridDataSource = (SendGridDataSource) super.clone(conn);
        sendGridDataSource.setSgUserName(null);
        sendGridDataSource.setSgPassword(null);
        return sendGridDataSource;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CATEGORY, DATE, REQUESTS, BOUNCES, CLICKS, OPENS, SPAM_REPORTS, DELIVERED, UNSUBSCRIBES, INVALID_EMAILS,
                REPEAT_BOUNCES, REPEAT_SPAM_REPORTS, REPEAT_UNSUBSCRIBES);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
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
    public Feed createFeedObject(FeedDefinition parent) {
        return new SendGridFeed();
    }

    @Override
    public String validateCredentials() {
        try {
            String url = "https://sendgrid.com/api/stats.get.xml";
            GetMethod getMethod = new GetMethod(url);
            getMethod.setQueryString(new NameValuePair[] { new NameValuePair("api_user", sgUserName),
                new NameValuePair("api_key", sgPassword), new NameValuePair("list", "true")});
            HttpClient httpClient = new HttpClient();
            httpClient.executeMethod(getMethod);
            Document doc = new Builder().build(getMethod.getResponseBodyAsStream());
            if (doc.toString().indexOf("error") != -1) {
                return "Your credentials were invalid.";
            }
        } catch (IOException e) {
            return e.getMessage();
        } catch (ParsingException e) {
            return e.getMessage();
        }
        return null;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
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

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM sendgrid WHERE data_source_id = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO sendgrid (data_source_id, sg_username, sg_password) values (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, sgUserName);
        insertStmt.setString(3, sgPassword != null ? PasswordStorage.encryptString(sgPassword) : null);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement stmt = conn.prepareStatement("SELECT SG_USERNAME, SG_PASSWORD FROM SENDGRID WHERE DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            sgUserName = rs.getString(1);
            String password = rs.getString(2);
            if (password != null) {
                sgPassword = PasswordStorage.decryptString(password);
            }
        }
    }
}
