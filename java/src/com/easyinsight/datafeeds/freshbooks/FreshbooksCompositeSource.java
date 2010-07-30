package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.TokenStorage;
import flex.messaging.FlexContext;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 6:46:43 PM
 */
public class FreshbooksCompositeSource extends CompositeServerDataSource {

    public static final String CONSUMER_KEY = "easyinsight";
    public static final String CONSUMER_SECRET = "3gKm7ivgkPCeQZChh7ig9CDMBGratLg6yS";

    private String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    private String tokenKey;
    private String tokenSecretKey;

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getTokenSecretKey() {
        return tokenSecretKey;
    }

    public void setTokenSecretKey(String tokenSecretKey) {
        this.tokenSecretKey = tokenSecretKey;
    }

    @Override
    public String validateCredentials(Credentials credentials) {
        return null;
    }

    private String url;

    public String getUrl() {
        String freshbooksURL = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "https://") + url;
        if(freshbooksURL.endsWith("/")) {
            freshbooksURL = freshbooksURL.substring(0, freshbooksURL.length() - 1);
        }
        if(!(freshbooksURL.endsWith(".freshbooks.com")))
            freshbooksURL = freshbooksURL + ".freshbooks.com";
        return freshbooksURL;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    public void customStorage(Connection conn) throws SQLException {
        try {
            if (pin != null && !"".equals(pin) && tokenKey == null && tokenSecretKey == null) {
                OAuthConsumer consumer = (OAuthConsumer) FlexContext.getHttpRequest().getSession().getAttribute("oauthConsumer");
                OAuthProvider provider = (OAuthProvider) FlexContext.getHttpRequest().getSession().getAttribute("oauthProvider");
                provider.retrieveAccessToken(consumer, pin);
                tokenKey = consumer.getToken();
                tokenSecretKey = consumer.getTokenSecret();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM FRESHBOOKS WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement basecampStmt = conn.prepareStatement("INSERT INTO FRESHBOOKS (DATA_SOURCE_ID, TOKEN_KEY, TOKEN_SECRET_KEY, URL) VALUES (?, ?, ?, ?)");
        basecampStmt.setLong(1, getDataFeedID());
        basecampStmt.setString(2, tokenKey);
        basecampStmt.setString(3, tokenSecretKey);
        basecampStmt.setString(4, getUrl());
        basecampStmt.execute();
        basecampStmt.close();
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL, TOKEN_KEY, TOKEN_SECRET_KEY FROM FRESHBOOKS WHERE DATA_SOURCE_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            this.setUrl(rs.getString(1));
            setTokenKey(rs.getString(2));
            setTokenSecretKey(rs.getString(3));
        }
        loadStmt.close();
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        return new HashSet<FeedType>(Arrays.asList(FeedType.FRESHBOOKS_INVOICE, FeedType.FRESHBOOKS_CLIENTS,
                FeedType.FRESHBOOKS_EXPENSES, FeedType.FRESHBOOKS_CATEGORIES, FeedType.FRESHBOOKS_STAFF,
                FeedType.FRESHBOOKS_PAYMENTS, FeedType.FRESHBOOKS_TASKS, FeedType.FRESHBOOKS_TIME_ENTRIES,
                FeedType.FRESHBOOKS_PROJECTS, FeedType.FRESHBOOKS_ESTIMATES));
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.FRESHBOOKS_INVOICE, FeedType.FRESHBOOKS_CLIENTS, FreshbooksInvoiceSource.CLIENT_ID, FreshbooksClientSource.CLIENT_ID),
                new ChildConnection(FeedType.FRESHBOOKS_EXPENSES, FeedType.FRESHBOOKS_CATEGORIES, FreshbooksExpenseSource.CATEGORY_ID, FreshbooksCategorySource.CATEGORY_NAME),
                new ChildConnection(FeedType.FRESHBOOKS_EXPENSES, FeedType.FRESHBOOKS_STAFF, FreshbooksExpenseSource.STAFF_ID, FreshbooksStaffSource.STAFF_ID),
                new ChildConnection(FeedType.FRESHBOOKS_EXPENSES, FeedType.FRESHBOOKS_PROJECTS, FreshbooksExpenseSource.PROJECT_ID, FreshbooksProjectSource.PROJECT_ID),
                new ChildConnection(FeedType.FRESHBOOKS_EXPENSES, FeedType.FRESHBOOKS_CLIENTS, FreshbooksExpenseSource.CLIENT_ID, FreshbooksClientSource.CLIENT_ID),
                new ChildConnection(FeedType.FRESHBOOKS_ESTIMATES, FeedType.FRESHBOOKS_CLIENTS, FreshbooksEstimateSource.CLIENT_ID, FreshbooksClientSource.CLIENT_ID),
                new ChildConnection(FeedType.FRESHBOOKS_PAYMENTS, FeedType.FRESHBOOKS_CLIENTS, FreshbooksPaymentSource.CLIENT_ID, FreshbooksClientSource.CLIENT_ID),
                new ChildConnection(FeedType.FRESHBOOKS_PAYMENTS, FeedType.FRESHBOOKS_INVOICE, FreshbooksPaymentSource.INVOICE_ID, FreshbooksInvoiceSource.INVOICE_ID),
                new ChildConnection(FeedType.FRESHBOOKS_PROJECTS, FeedType.FRESHBOOKS_CLIENTS, FreshbooksProjectSource.CLIENT_ID, FreshbooksClientSource.CLIENT_ID),
                new ChildConnection(FeedType.FRESHBOOKS_TIME_ENTRIES, FeedType.FRESHBOOKS_STAFF, FreshbooksTimeEntrySource.STAFF_ID, FreshbooksStaffSource.STAFF_ID),
                new ChildConnection(FeedType.FRESHBOOKS_TASKS, FeedType.FRESHBOOKS_TIME_ENTRIES, FreshbooksTaskSource.TASK_ID, FreshbooksTimeEntrySource.TASK_ID));
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = Arrays.asList(KPIUtil.createKPIWithFilters("Outstanding Invoice Amount", "symbol_dollar.png", (AnalysisMeasure) findAnalysisItem(FreshbooksInvoiceSource.AMOUNT_OUTSTANDING),
                new ArrayList<FilterDefinition>(), KPI.GOOD, 1));        
        return kpis;
    }
}
