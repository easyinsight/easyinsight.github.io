package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;

import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.ChildConnection;

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

import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:35 PM
 */
public class HighRiseCompositeSource extends CompositeServerDataSource {

    private String url;
    private boolean includeEmails;
    private boolean joinDealsToContacts;
    private boolean includeContactNotes;
    private boolean includeCompanyNotes;
    private boolean includeDealNotes;
    private boolean includeCaseNotes;

    public boolean isIncludeCompanyNotes() {
        return includeCompanyNotes;
    }

    public void setIncludeCompanyNotes(boolean includeCompanyNotes) {
        this.includeCompanyNotes = includeCompanyNotes;
    }

    public boolean isIncludeDealNotes() {
        return includeDealNotes;
    }

    public void setIncludeDealNotes(boolean includeDealNotes) {
        this.includeDealNotes = includeDealNotes;
    }

    public boolean isIncludeCaseNotes() {
        return includeCaseNotes;
    }

    public void setIncludeCaseNotes(boolean includeCaseNotes) {
        this.includeCaseNotes = includeCaseNotes;
    }

    public boolean isIncludeContactNotes() {
        return includeContactNotes;
    }

    public void setIncludeContactNotes(boolean includeContactNotes) {
        this.includeContactNotes = includeContactNotes;
    }

    public boolean isJoinDealsToContacts() {
        return joinDealsToContacts;
    }

    public void setJoinDealsToContacts(boolean joinDealsToContacts) {
        this.joinDealsToContacts = joinDealsToContacts;
    }

    private transient HighriseCache highriseCache;

    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public HighriseCache getOrCreateCache(HttpClient httpClient) throws HighRiseLoginException, ParsingException {
        if (highriseCache == null) {
            highriseCache = new HighriseCache();
            highriseCache.populateCaches(httpClient, getUrl());
        }
        return highriseCache;
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
        feedTypes.add(FeedType.HIGHRISE_CONTACTS);
        feedTypes.add(FeedType.HIGHRISE_TASKS);
        feedTypes.add(FeedType.HIGHRISE_CASES);
        feedTypes.add(FeedType.HIGHRISE_EMAILS);
        feedTypes.add(FeedType.HIGHRISE_CONTACT_NOTES);
        feedTypes.add(FeedType.HIGHRISE_COMPANY_NOTES);
        feedTypes.add(FeedType.HIGHRISE_CASE_NOTES);
        feedTypes.add(FeedType.HIGHRISE_DEAL_NOTES);
        return feedTypes;
    }

    public boolean needsCredentials(List<CredentialFulfillment> existingCredentials) {
        String userName = null;
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, getDataFeedID(), false);
        CredentialFulfillment ourFulfillment = null;
        for (CredentialFulfillment credentialFulfillment : existingCredentials) {
            if (credentialFulfillment.getDataSourceID() == getDataFeedID()) {
                ourFulfillment = credentialFulfillment;
            }
        }
        if (token == null && ourFulfillment != null) {
            userName = ourFulfillment.getCredentials().getUserName();
        } else if (token != null && ourFulfillment != null) {
            com.easyinsight.users.Credentials credentials = ourFulfillment.getCredentials();
            if (credentials.getUserName() != null && !"".equals(credentials.getUserName()) &&
                !credentials.getUserName().equals(token.getTokenValue())) {
                token.setTokenValue(credentials.getUserName());
                new TokenStorage().saveToken(token, getDataFeedID());
            }
            userName = token.getTokenValue();
        } else if (token != null) {
            userName = token.getTokenValue();
        }
        if (userName == null) {
            return true;
        }
        HttpClient client = getHttpClient(userName, "");
        try {
            runRestRequest("/companies.xml", client, new Builder());
        } catch (HighRiseLoginException e) {
            return true;
        }
        return false;
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
        return Arrays.asList(
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_DEAL, HighRiseCompanySource.COMPANY_ID,
                HighRiseDealSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_CONTACTS, HighRiseCompanySource.COMPANY_ID,
                    HighRiseContactSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_TASKS, HighRiseCompanySource.COMPANY_ID,
                    HighRiseTaskSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CONTACTS, HighRiseCaseSource.OWNER,
                        HighRiseContactSource.OWNER));
    }

    protected Collection<ChildConnection> getLiveChildConnections() {
        List<ChildConnection> connections = new ArrayList<ChildConnection>();
        if (joinDealsToContacts) {
            connections.add(new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_CONTACTS, HighRiseDealSource.CONTACT_ID,
                   HighRiseContactSource.CONTACT_ID));
        } else {
            connections.add(new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_DEAL, HighRiseCompanySource.COMPANY_ID,
                HighRiseDealSource.COMPANY_ID));
        }
        connections.addAll(Arrays.asList(
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_CONTACTS, HighRiseCompanySource.COMPANY_ID,
                    HighRiseContactSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_TASKS, HighRiseCompanySource.COMPANY_ID,
                    HighRiseTaskSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CONTACTS, HighRiseCaseSource.OWNER,
                        HighRiseContactSource.OWNER),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_CONTACTS, HighRiseTaskSource.OWNER,
                        HighRiseContactSource.OWNER),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_CASES, HighRiseTaskSource.CASE_ID,
                        HighRiseCaseSource.CASE_ID),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_DEAL, HighRiseTaskSource.DEAL_ID,
                        HighRiseDealSource.DEAL_ID),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_COMPANY, HighRiseTaskSource.COMPANY_ID,
                        HighRiseCompanySource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_EMAILS, HighRiseContactSource.CONTACT_ID,
                        HighRiseEmailSource.EMAIL_CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_CONTACT_NOTES, HighRiseContactSource.CONTACT_ID,
                        HighRiseContactNotesSource.NOTE_CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_DEAL_NOTES, HighRiseDealSource.DEAL_ID,
                        HighRiseDealNotesSource.NOTE_DEAL_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_COMPANY_NOTES, HighRiseCompanySource.COMPANY_ID,
                        HighRiseCompanyNotesSource.NOTE_COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CASE_NOTES, HighRiseCaseSource.CASE_ID,
                        HighRiseCaseNotesSource.NOTE_CASE_ID)));
        return connections;
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

    public boolean isIncludeEmails() {
        return includeEmails;
    }

    public void setIncludeEmails(boolean includeEmails) {
        this.includeEmails = includeEmails;
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
        clearStmt.close();
        PreparedStatement basecampStmt = conn.prepareStatement("INSERT INTO HIGHRISE (FEED_ID, URL, INCLUDE_EMAILS, join_deals_to_contacts, include_contact_notes," +
                "include_company_notes, include_deal_notes, include_case_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        basecampStmt.setLong(1, getDataFeedID());
        basecampStmt.setString(2, getUrl());
        basecampStmt.setBoolean(3, includeEmails);
        basecampStmt.setBoolean(4, joinDealsToContacts);
        basecampStmt.setBoolean(5, includeContactNotes);
        basecampStmt.setBoolean(6, includeCompanyNotes);
        basecampStmt.setBoolean(7, includeDealNotes);
        basecampStmt.setBoolean(8, includeCaseNotes);
        basecampStmt.execute();
        basecampStmt.close();
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL, INCLUDE_EMAILS, join_deals_to_contacts, include_contact_notes," +
                "include_company_notes, include_deal_notes, include_case_notes FROM HIGHRISE WHERE FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            this.setUrl(rs.getString(1));
            this.setIncludeEmails(rs.getBoolean(2));
            this.setJoinDealsToContacts(rs.getBoolean(3));
            this.setIncludeContactNotes(rs.getBoolean(4));
            this.setIncludeCompanyNotes(rs.getBoolean(5));
            this.setIncludeDealNotes(rs.getBoolean(6));
            this.setIncludeCaseNotes(rs.getBoolean(7));
        }
        loadStmt.close();
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
        dealsClosedMonthKPI.setDayWindow(30);
        FilterValueDefinition wonFilterDefinition = new FilterValueDefinition();
        wonFilterDefinition.setField(findAnalysisItem(HighRiseDealSource.STATUS));
        wonFilterDefinition.setFilteredValues(Arrays.asList((Object) "won"));
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(findAnalysisItem(HighRiseDealSource.CREATED_AT));
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.LAST_FULL_MONTH);
        dealsClosedMonthKPI.setFilters(Arrays.asList((FilterDefinition) rollingFilterDefinition));
        return Arrays.asList(dealValueKPI, pendingDealCountKPI, dealsClosedMonthKPI);
    }

    public boolean isLongRefresh() {
        return true;
    }

    @Override
    public int getVersion() {
        return 4;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new HighRiseComposite1To2(this), new HighRiseComposite2To3(this), new HighRiseComposite3To4(this));
    }

    public void decorateLinks(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.getLinks() == null) {
                analysisItem.setLinks(new ArrayList<Link>());
            }
            if (isContactLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/people/["+HighRiseContactSource.CONTACT_ID+"]");
                urlLink.setLabel("View Contact in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isCompanyLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/companies/["+HighRiseCompanySource.COMPANY_ID+"]");
                urlLink.setLabel("View Company in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isDealLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/deals/["+HighRiseDealSource.DEAL_ID+"]");
                urlLink.setLabel("View Deal in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isCaseLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/kases/["+HighRiseCaseSource.CASE_ID+"]");
                urlLink.setLabel("View Case in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isTaskLinkable(analysisItem)) {
                URLLink upcomingTasks = new URLLink();
                upcomingTasks.setUrl(getUrl() + "/tasks");
                upcomingTasks.setLabel("View Upcoming Tasks in Highrise...");
                analysisItem.getLinks().add(upcomingTasks);
                URLLink completedTasks = new URLLink();
                completedTasks.setUrl(getUrl() + "/tasks?collection=completed");
                completedTasks.setLabel("View Completed Tasks in Highrise...");
                analysisItem.getLinks().add(completedTasks);
                URLLink assignedTasks = new URLLink();
                assignedTasks.setUrl(getUrl() + "/tasks?collection=assigned");
                assignedTasks.setLabel("View Assigned Tasks in Highrise...");
                analysisItem.getLinks().add(assignedTasks);
            }

        }
    }

    private boolean isContactLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toKeyString();
        if (HighRiseContactSource.CONTACT_NAME.equals(keyString)) {
            return true;
        }
        return false;
    }

    private boolean isCompanyLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toKeyString();
        if (HighRiseCompanySource.COMPANY_NAME.equals(keyString)) {
            return true;
        }
        return false;
    }

    private boolean isDealLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toKeyString();
        if (HighRiseDealSource.DEAL_NAME.equals(keyString)) {
            return true;
        }
        return false;
    }

    private boolean isCaseLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toKeyString();
        if (HighRiseCaseSource.CASE_NAME.equals(keyString)) {
            return true;
        }
        return false;
    }

    private boolean isTaskLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toKeyString();
        if (HighRiseTaskSource.BODY.equals(keyString)) {
            return true;
        }
        return false;
    }
}
