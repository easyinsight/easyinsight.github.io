package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.kpi.KPI;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.easyinsight.users.TokenStorage;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;
import nu.xom.Document;
import nu.xom.Builder;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:35 PM
 */
public class HighRiseCompositeSource extends CompositeServerDataSource {

    private String url;

    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public String getFilterExampleMessage() {
        return "For example, drag Status into the area to the right to restrict the KPI to only those deals that are Pending.";
    }

    @Override
    protected Map<String, String> createProperties() {
        Map<String, String> properties = super.createProperties();
        properties.put("highrise.url", url);
        return properties;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_COMPOSITE;
    }

    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> feedTypes = new HashSet<FeedType>();
        feedTypes.add(FeedType.HIGHRISE_COMPANY);
        feedTypes.add(FeedType.HIGHRISE_DEAL);
        return feedTypes;
    }

    private static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    private Document runRestRequest(String path, HttpClient client, Builder builder) throws HighRiseLoginException {
        HttpMethod restMethod = new GetMethod(getUrl() + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
        }
        catch (nu.xom.ParsingException e) {
            String statusLine = restMethod.getStatusLine().toString();
            if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                throw new HighRiseLoginException("Could not locate a Highrise instance at " + getUrl());
            } else {
                throw new HighRiseLoginException("Invalid Highrise authentication token--you can find the token under your the My Info link in the upper right corner on your Highrise page.");
            }

        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    public String validateCredentials(com.easyinsight.users.Credentials credentials) {
        HttpClient client = getHttpClient(credentials.getUserName(), credentials.getPassword());
        Pattern p = Pattern.compile("(http(s?)://)?([A-Za-z0-9]|\\-)+(\\.highrisehq\\.com)?");
        Matcher m = p.matcher(url);
        String result = null;
        if(!m.matches()) {
            result = "Invalid url. Please input a proper URL.";
        }
        else {
            try {
                runRestRequest("/companies.xml", client, new Builder());
            } catch (HighRiseLoginException e) {
               result = e.getMessage();
            }
        }
        return result;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_DEAL, HighRiseCompanySource.COMPANY_ID,
                HighRiseDealSource.COMPANY_ID));
    }

    protected IServerDataSourceDefinition createForFeedType(FeedType feedType) {
        if (feedType.equals(FeedType.HIGHRISE_COMPANY)) {
            return new HighRiseCompanySource();
        } else if (feedType.equals(FeedType.HIGHRISE_DEAL)) {
            return new HighRiseDealSource();
        } else {
            throw new RuntimeException();
        }
    }

    public String getUrl() {
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if(!basecampUrl.endsWith(".highrisehq.com"))
            basecampUrl = basecampUrl + ".highrisehq.com";
        return basecampUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        HighRiseCompositeSource dataSource = (HighRiseCompositeSource) super.clone(conn);
        dataSource.setUrl("");
        return dataSource;
    }

    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM HIGHRISE WHERE FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement basecampStmt = conn.prepareStatement("INSERT INTO HIGHRISE (FEED_ID, URL) VALUES (?, ?)");
        basecampStmt.setLong(1, getDataFeedID());
        basecampStmt.setString(2, getUrl());
        basecampStmt.execute();
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL FROM HIGHRISE WHERE FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            this.setUrl(rs.getString(1));
        }
    }

    @Override
    public int getCredentialsDefinition() {
        return new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, getDataFeedID(), false) == null ? CredentialsDefinition.STANDARD_USERNAME_PW :
                CredentialsDefinition.NO_CREDENTIALS;
    }

    public List<KPI> createKPIs() {
        KPI dealValueKPI = new KPI();
        dealValueKPI.setName("Pending Dollar Value of the Sales Pipeline");
        dealValueKPI.setIconImage("credit_card.png");
        dealValueKPI.setAnalysisMeasure((AnalysisMeasure) findAnalysisItem(HighRiseDealSource.TOTAL_DEAL_VALUE));
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
        filterValueDefinition.setInclusive(true);
        filterValueDefinition.setField(findAnalysisItem(HighRiseDealSource.STATUS));
        filterValueDefinition.setFilteredValues(Arrays.asList((Object) "pending"));
        dealValueKPI.setFilters(Arrays.asList((FilterDefinition) filterValueDefinition));
        KPI pendingDealCountKPI = new KPI();
        pendingDealCountKPI.setName("Number of Pending Deals in the Pipeline");
        pendingDealCountKPI.setIconImage("funnel.png");
        pendingDealCountKPI.setAnalysisMeasure((AnalysisMeasure) findAnalysisItem(HighRiseDealSource.COUNT));
        FilterValueDefinition pendingCountFilter = new FilterValueDefinition();
        pendingCountFilter.setField(findAnalysisItem(HighRiseDealSource.STATUS));
        pendingCountFilter.setInclusive(true);
        pendingCountFilter.setFilteredValues(Arrays.asList((Object) "pending"));
        pendingDealCountKPI.setFilters(Arrays.asList((FilterDefinition) pendingCountFilter));
        KPI dealsClosedMonthKPI = new KPI();
        dealsClosedMonthKPI.setName("Dollar Value of Deals Created in the Last 30 Days");
        dealsClosedMonthKPI.setIconImage("symbol_dollar.png");
        dealsClosedMonthKPI.setAnalysisMeasure((AnalysisMeasure) findAnalysisItem(HighRiseDealSource.TOTAL_DEAL_VALUE));
        FilterValueDefinition wonFilterDefinition = new FilterValueDefinition();
        wonFilterDefinition.setField(findAnalysisItem(HighRiseDealSource.STATUS));
        wonFilterDefinition.setFilteredValues(Arrays.asList((Object) "won"));
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(findAnalysisItem(HighRiseDealSource.CREATED_AT));
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.LAST_FULL_MONTH);
        dealsClosedMonthKPI.setFilters(Arrays.asList((FilterDefinition) rollingFilterDefinition));
        return Arrays.asList(dealValueKPI, pendingDealCountKPI, dealsClosedMonthKPI);
    }

    
}
