package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.ChildConnection;

import com.easyinsight.intention.*;
import com.easyinsight.security.SecurityUtil;

import com.easyinsight.users.Account;

import java.text.ParseException;
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

    private String url = "";
    private boolean includeEmails;
    private boolean joinDealsToContacts;
    private boolean includeContactNotes;
    private boolean includeCompanyNotes;
    private boolean includeDealNotes;
    private boolean includeCaseNotes;
    private boolean joinTasksToContacts;
    private String token;
    private List<HighriseAdditionalToken> additionalTokens = new ArrayList<HighriseAdditionalToken>();

    @Override
    public void beforeSave(EIConnection conn) throws Exception {
        super.beforeSave(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT INCLUDE_CASE_NOTES, INCLUDE_COMPANY_NOTES, INCLUDE_CONTACT_NOTES," +
                "INCLUDE_DEAL_NOTES, INCLUDE_EMAILS FROM HIGHRISE WHERE FEED_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            boolean cases = rs.getBoolean(1);
            boolean companies = rs.getBoolean(2);
            boolean contacts = rs.getBoolean(3);
            boolean deals = rs.getBoolean(4);
            boolean emails = rs.getBoolean(5);
            if (cases != includeCaseNotes || companies != includeCompanyNotes || deals != includeDealNotes ||
                    contacts != includeContactNotes || emails != includeEmails) {
                setLastRefreshStart(null);
            }
        }
    }

    protected void sortSources(List<IServerDataSourceDefinition> children) {
        Collections.sort(children, new Comparator<IServerDataSourceDefinition>() {

            public int compare(IServerDataSourceDefinition feedDefinition, IServerDataSourceDefinition feedDefinition1) {
                if (feedDefinition.getFeedType().getType() == FeedType.HIGHRISE_CONTACTS.getType()) {
                    return -1;
                }
                if (feedDefinition1.getFeedType().getType() == FeedType.HIGHRISE_CONTACTS.getType()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public HighRiseCompositeSource() {
        setFeedName("Highrise");
    }

    public List<HighriseAdditionalToken> getAdditionalTokens() {
        return additionalTokens;
    }

    public void setAdditionalTokens(List<HighriseAdditionalToken> additionalTokens) {
        this.additionalTokens = additionalTokens;
    }

    public boolean isJoinTasksToContacts() {
        return joinTasksToContacts;
    }

    public void setJoinTasksToContacts(boolean joinTasksToContacts) {
        this.joinTasksToContacts = joinTasksToContacts;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
    private transient HighriseRecordingsCache highriseRecordingsCache;
    private transient HighriseCompanyCache highriseCompanyCache;
    private transient HighriseCustomFieldsCache customFieldsCache;
    private transient Map<String, String> contactToCompanyCache;
    private transient HighriseTaskCache taskCache;

    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public HighriseTaskCache getOrCreateCache(EIConnection conn) throws HighRiseLoginException, ParsingException, ParseException {
        if (taskCache == null) {
            taskCache = new HighriseTaskCache();
            taskCache.populate(this, conn);
        }
        return taskCache;
    }

    public Map<String, String> getContactToCompanyCache() {
        if (contactToCompanyCache == null) {
            contactToCompanyCache = new HashMap<String, String>();
        }
        return contactToCompanyCache;
    }

    public HighriseCache getOrCreateCache(HttpClient httpClient) throws HighRiseLoginException, ParsingException {
        if (highriseCache == null) {
            highriseCache = new HighriseCache();
            highriseCache.populateCaches(httpClient, getUrl(), this);
        }
        return highriseCache;
    }

    public HighriseRecordingsCache getOrCreateRecordingsCache(HttpClient httpClient, Date date) throws HighRiseLoginException, ParsingException, ParseException {
        if (highriseRecordingsCache == null) {
            highriseRecordingsCache = new HighriseRecordingsCache();
            highriseRecordingsCache.populateCaches(httpClient, getUrl(), this, date, getOrCreateCache(httpClient), getOrCreateCompanyCache(httpClient, date).getCompanyIDs());
        }
        return highriseRecordingsCache;
    }

    public HighriseCompanyCache getOrCreateCompanyCache(HttpClient httpClient, Date date) throws HighRiseLoginException, ParsingException, ParseException {
        if (highriseCompanyCache == null) {
            highriseCompanyCache = new HighriseCompanyCache();
            highriseCompanyCache.populateCaches(httpClient, getUrl(), this, date, getOrCreateCache(httpClient));
        }
        return highriseCompanyCache;
    }

    public HighriseCustomFieldsCache getOrCreateCustomFieldCache(HttpClient httpClient, Date date) throws HighRiseLoginException, ParsingException, ParseException {
        if (customFieldsCache == null) {
            customFieldsCache = new HighriseCustomFieldsCache();
            customFieldsCache.blah(httpClient, getUrl(), this, date, getOrCreateCache(httpClient));
        }
        return customFieldsCache;
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
        feedTypes.add(FeedType.HIGHRISE_CASE_JOIN);
        feedTypes.add(FeedType.HIGHRISE_ACTIVITIES);
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

    public String validateCredentials() {
        HttpClient client = getHttpClient(token, "");
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
        // 908-400-3658 Jeanie
        connections.addAll(Arrays.asList(
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_CONTACTS, HighRiseCompanySource.COMPANY_ID,
                    HighRiseContactSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_TASKS, HighRiseCompanySource.COMPANY_ID,
                    HighRiseTaskSource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CONTACTS, HighRiseCaseSource.OWNER,
                        HighRiseContactSource.CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_CONTACTS, HighRiseTaskSource.CONTACT_ID,
                        HighRiseContactSource.CONTACT_ID),
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
                        HighRiseCaseNotesSource.NOTE_CASE_ID),
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CASES, HighRiseCaseJoinSource.CASE_ID,
                        HighRiseCaseSource.CASE_ID),
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_COMPANY, HighRiseCaseJoinSource.COMPANY_ID,
                        HighRiseCompanySource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CONTACTS, HighRiseCaseJoinSource.CONTACT_ID,
                        HighRiseContactSource.CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_ACTIVITIES, HighRiseContactSource.CONTACT_ID,
                        HighRiseActivitySource.CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_ACTIVITIES, HighRiseCompanySource.COMPANY_ID,
                        HighRiseActivitySource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_ACTIVITIES, HighRiseDealSource.DEAL_ID,
                        HighRiseActivitySource.DEAL_ID),
                new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_ACTIVITIES, HighRiseCaseSource.CASE_ID,
                        HighRiseActivitySource.CASE_ID)));
        return connections;
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
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
        dataSource.setAdditionalTokens(new ArrayList<HighriseAdditionalToken>());
        return dataSource;
    }

    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM HIGHRISE WHERE FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement basecampStmt = conn.prepareStatement("INSERT INTO HIGHRISE (FEED_ID, URL, INCLUDE_EMAILS, join_deals_to_contacts, include_contact_notes," +
                "include_company_notes, include_deal_notes, include_case_notes, join_tasks_to_contacts) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        basecampStmt.setLong(1, getDataFeedID());
        basecampStmt.setString(2, getUrl());
        basecampStmt.setBoolean(3, includeEmails);
        basecampStmt.setBoolean(4, joinDealsToContacts);
        basecampStmt.setBoolean(5, includeContactNotes);
        basecampStmt.setBoolean(6, includeCompanyNotes);
        basecampStmt.setBoolean(7, includeDealNotes);
        basecampStmt.setBoolean(8, includeCaseNotes);
        basecampStmt.setBoolean(9, joinTasksToContacts);
        basecampStmt.execute();
        basecampStmt.close();
        if (this.token != null && !"".equals(this.token.trim())) {
            Token token = new Token();
            token.setTokenValue(this.token.trim());
            token.setTokenType(TokenStorage.HIGHRISE_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, getDataFeedID(), (EIConnection) conn);
        }
        if (additionalTokens != null) {
            PreparedStatement clearTokenStmt = conn.prepareStatement("DELETE FROM HIGHRISE_ADDITIONAL_TOKEN WHERE DATA_SOURCE_ID = ?");
            clearTokenStmt.setLong(1, getDataFeedID());
            clearTokenStmt.executeUpdate();
            clearTokenStmt.close();
            PreparedStatement saveTokenStmt = conn.prepareStatement("INSERT INTO HIGHRISE_ADDITIONAL_TOKEN (TOKEN, DATA_SOURCE_ID, TOKEN_VALID) VALUES (?, ?, ?)");
            for (HighriseAdditionalToken token : additionalTokens) {
                saveTokenStmt.setString(1, token.getToken());
                saveTokenStmt.setLong(2, getDataFeedID());
                saveTokenStmt.setBoolean(3, true);
                saveTokenStmt.execute();
            }
            saveTokenStmt.close();
        }
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
        PreparedStatement additionalTokenStmt = conn.prepareStatement("SELECT TOKEN FROM highrise_additional_token where DATA_SOURCE_ID = ?");
        additionalTokenStmt.setLong(1, getDataFeedID());
        List<HighriseAdditionalToken> tokens = new ArrayList<HighriseAdditionalToken>();
        ResultSet tokenRS = additionalTokenStmt.executeQuery();
        while (tokenRS.next()) {
            String token = tokenRS.getString(1);
            HighriseAdditionalToken highriseToken = new HighriseAdditionalToken();
            highriseToken.setToken(token);
            tokens.add(highriseToken);
        }
        additionalTokenStmt.close();
        this.additionalTokens = tokens;
    }

    public boolean isLongRefresh() {
        return true;
    }

    @Override
    public int getVersion() {
        return 6;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new HighRiseComposite1To2(this), new HighRiseComposite2To3(this), new HighRiseComposite3To4(this), new HighRiseComposite4To5(this),
                new HighRiseComposite5To6(this));
    }

    public void decorateLinks(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.getLinks() == null) {
                analysisItem.setLinks(new ArrayList<Link>());
            }
            if (isContactLinkable(analysisItem)) {
                removeURLLinkIfExists("/people/["+HighRiseContactSource.CONTACT_ID+"]", analysisItem);
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/people/["+HighRiseContactSource.CONTACT_ID+"]");
                urlLink.setLabel("View Contact in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isCompanyLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                removeURLLinkIfExists("/companies/["+HighRiseCompanySource.COMPANY_ID+"]", analysisItem);
                urlLink.setUrl(getUrl() + "/companies/["+HighRiseCompanySource.COMPANY_ID+"]");
                urlLink.setLabel("View Company in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isDealLinkable(analysisItem)) {
                removeURLLinkIfExists("/deals/["+HighRiseDealSource.DEAL_ID+"]", analysisItem);
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/deals/["+HighRiseDealSource.DEAL_ID+"]");
                urlLink.setLabel("View Deal in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isCaseLinkable(analysisItem)) {
                removeURLLinkIfExists("/kases/["+HighRiseCaseSource.CASE_ID+"]", analysisItem);
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/kases/["+HighRiseCaseSource.CASE_ID+"]");
                urlLink.setLabel("View Case in Highrise...");
                analysisItem.getLinks().add(urlLink);
            } else if (isTaskLinkable(analysisItem)) {
                removeURLLinkIfExists("/tasks", analysisItem);
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

    protected void refreshDone() {
        super.refreshDone();
        highriseCache = null;
        highriseCompanyCache = null;
        highriseRecordingsCache = null;
        customFieldsCache = null;
        contactToCompanyCache = null;
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

    @Override
    public boolean checkDateTime(String name, Key key) {
        if (HighRiseTaskSource.DUE_AT.equals(name) || HighRiseTaskSource.CREATED_AT.equals(name) ||
                HighRiseTaskSource.DONE_AT.equals(name)) {
            return true;
        }
        return false;
    }



    public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report, DataSourceInfo dataSourceInfo) {
        // help set up a deal report
        // help set up a case report
        // help set up an activity report
        List<IntentionSuggestion> suggestions = super.suggestIntentions(report, dataSourceInfo);
        if (!report.isFullJoins()) {
            suggestions.add(new IntentionSuggestion("Help Me Set Up a Deal Report",
                    "This action will configure your report to connect Deals to both Contacts and Companies. It will also add a filter to exclude any records without a matching deal name.",
                    IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_DEAL_SETUP, IntentionSuggestion.OTHER));
            suggestions.add(new IntentionSuggestion("Help Me Set Up a Case Report",
                    "This action will configure your report to connect Cases to both Contacts and Companies. It will also add a filter to exclude any records without a matching case name.",
                    IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_CASE_SETUP, IntentionSuggestion.OTHER));
            suggestions.add(new IntentionSuggestion("Help Me Set Up a Task Report",
                    "This action will filter out any records not related to Tasks and add a couple of new custom fields to help you do additional reporting against Tasks.",
                    IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_TASK_SETUP, IntentionSuggestion.OTHER));
        }
        Set<AnalysisItem> analysisItems = report.getAllAnalysisItems();
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem == null) {
                continue;
            }
            if (analysisItem.toDisplay().equals(HighRiseContactNotesSource.BODY)) {
                if (!filterOn(HighRiseContactNotesSource.BODY, report)) {
                    suggestions.add(new IntentionSuggestion("Only Show the Latest Contact Note", "Create a filter to only show the latest associated note for any given contact in the report.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_LAST_CONTACT_NOTE, IntentionSuggestion.OTHER));
                }
                if (!includeContactNotes) {
                    suggestions.add(new IntentionSuggestion("Enable Contact Notes", "You're currently not synchronizing contact notes to this data source.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_NOTE_CONFIG, IntentionSuggestion.WARNING));
                }
            } else if (analysisItem.toDisplay().equals(HighRiseCompanyNotesSource.BODY)) {
                if (!filterOn(HighRiseCompanyNotesSource.BODY, report)) {
                    suggestions.add(new IntentionSuggestion("Only Show the Latest Company Note", "Create a filter to only show the latest associated note for any given company in the report.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_LAST_COMPANY_NOTE, IntentionSuggestion.OTHER));
                }
                if (!includeCompanyNotes) {
                    suggestions.add(new IntentionSuggestion("Enable Company Notes", "You're currently not synchronizing company notes to this data source.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_NOTE_CONFIG, IntentionSuggestion.WARNING));
                }
            } else if (analysisItem.toDisplay().equals(HighRiseCaseNotesSource.BODY)) {
                if (!filterOn(HighRiseCaseNotesSource.BODY, report)) {
                    suggestions.add(new IntentionSuggestion("Only Show the Latest Case Note", "Create a filter to only show the latest associated note for any given case in the report.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_LAST_CASE_NOTE, IntentionSuggestion.OTHER));
                }
                if (!includeCaseNotes) {
                    suggestions.add(new IntentionSuggestion("Enable Case Notes", "You're currently not synchronizing case notes to this data source.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_NOTE_CONFIG, IntentionSuggestion.WARNING));
                }
            } else if (analysisItem.toDisplay().equals(HighRiseDealNotesSource.BODY)) {
                if (!filterOn(HighRiseDealNotesSource.BODY, report)) {
                    suggestions.add(new IntentionSuggestion("Only Show the Latest Deal Note", "Create a filter to only show the latest associated note for any given deal in the report.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_LAST_DEAL_NOTE, IntentionSuggestion.OTHER));
                }
                if (!includeDealNotes) {
                    suggestions.add(new IntentionSuggestion("Enable Deal Notes", "You're currently not synchronizing deal notes to this data source.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_NOTE_CONFIG, IntentionSuggestion.WARNING));
                }
            } /*else if (analysisItem.toDisplay().equals(HighRiseTaskSource.BODY)) {
                if (getAdditionalTokens() == null || getAdditionalTokens().size() == 0) {
                    suggestions.add(new IntentionSuggestion("Configure Tasks", "You haven't added any additional API keys for your Highrise tasks.", IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.SUGGESTION_TASK_ADD, IntentionSuggestion.WARNING));
                }
            }*/
        }
        return suggestions;
    }

    private boolean filterOn(String body, WSAnalysisDefinition report) {
        for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
            if (filterDefinition.getField() != null && filterDefinition.getField().toDisplay().equals(body)) {
                return true;
            }
        }
        return false;
    }

    public List<Intention> createIntentions(WSAnalysisDefinition report, List<AnalysisItem> fields, int type) throws SQLException {
        List<Intention> intentions = super.createIntentions(report, fields, type);
        if (!intentions.isEmpty()) {
            return intentions;
        }
        if (type == IntentionSuggestion.SUGGESTION_DEAL_SETUP) {
            intentions.add(new CustomizeJoinsIntention(fromChildConnections(Arrays.asList(
                    new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_COMPANY, HighRiseDealSource.COMPANY_ID,
                            HighRiseCompanySource.COMPANY_ID),
                    new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_CONTACTS, HighRiseDealSource.CONTACT_ID,
                            HighRiseContactSource.CONTACT_ID),
                    new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_COMPANY, HighRiseContactSource.COMPANY_ID,
                            HighRiseCompanySource.COMPANY_ID, false, false, false, true),
                    new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_TASKS, HighRiseCompanySource.COMPANY_ID,
                            HighRiseTaskSource.COMPANY_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CONTACTS, HighRiseCaseSource.OWNER,
                            HighRiseContactSource.CONTACT_ID),
                    new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_CONTACTS, HighRiseTaskSource.CONTACT_ID,
                            HighRiseContactSource.CONTACT_ID),
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
                            HighRiseCaseNotesSource.NOTE_CASE_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CASES, HighRiseCaseJoinSource.CASE_ID,
                            HighRiseCaseSource.CASE_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_COMPANY, HighRiseCaseJoinSource.COMPANY_ID,
                            HighRiseCompanySource.COMPANY_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CONTACTS, HighRiseCaseJoinSource.CONTACT_ID,
                            HighRiseContactSource.CONTACT_ID),
                    new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_ACTIVITIES, HighRiseContactSource.CONTACT_ID,
                            HighRiseActivitySource.CONTACT_ID),
                    new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_ACTIVITIES, HighRiseCompanySource.COMPANY_ID,
                            HighRiseActivitySource.COMPANY_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_ACTIVITIES, HighRiseCaseSource.CASE_ID,
                            HighRiseActivitySource.CASE_ID),
                    new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_ACTIVITIES, HighRiseDealSource.DEAL_ID,
                            HighRiseActivitySource.DEAL_ID)
                    ), fields)));
            intentions.add(new AddFilterIntention(excludeFilter(HighRiseDealSource.DEAL_NAME, report, fields)));
            ReportPropertiesIntention reportPropertiesIntention = new ReportPropertiesIntention();
            reportPropertiesIntention.setFullJoins(true);
            Set<AnalysisItem> items = report.getAllAnalysisItems();
            if (items.isEmpty()) {
                intentions.add(new AddReportFieldIntention(findFieldFromList(HighRiseDealSource.DEAL_NAME, fields)));
            }
            intentions.add(reportPropertiesIntention);
        } else if (type == IntentionSuggestion.SUGGESTION_CASE_SETUP) {
            intentions.add(new CustomizeJoinsIntention(fromChildConnections(Arrays.asList(
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CASES, HighRiseCaseJoinSource.CASE_ID,
                        HighRiseCaseSource.CASE_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CASE_NOTES, HighRiseCaseSource.CASE_ID,
                        HighRiseCaseNotesSource.NOTE_CASE_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_COMPANY, HighRiseCaseJoinSource.COMPANY_ID,
                        HighRiseCompanySource.COMPANY_ID, false, true, false, true),
                    new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CONTACTS, HighRiseCaseJoinSource.CONTACT_ID,
                        HighRiseContactSource.CONTACT_ID),
                    new ChildConnection(FeedType.HIGHRISE_CASE_NOTES, FeedType.HIGHRISE_CONTACTS, HighRiseCaseNotesSource.CONTACT_ID,
                        HighRiseContactSource.CONTACT_ID, false, true, false, true),
                    new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_COMPANY, HighRiseContactSource.COMPANY_ID,
                        HighRiseCompanySource.COMPANY_ID, false, true, false, true)
            ), fields)));
            intentions.add(new AddFilterIntention(excludeFilter(HighRiseCaseSource.CASE_NAME, report, fields)));
            ReportPropertiesIntention reportPropertiesIntention = new ReportPropertiesIntention();
            reportPropertiesIntention.setFullJoins(true);
            intentions.add(reportPropertiesIntention);
            DerivedAnalysisDimension placeHolder = new DerivedAnalysisDimension();
            placeHolder.setConcrete(false);
            placeHolder.setKey(new NamedKey("Case Note Join Placeholder"));
            placeHolder.setDerivationCode("firstvalue(\"Placeholder\", [Case Note Body], [Join Case ID])");
            intentions.add(new CustomFieldIntention(placeHolder));
            FilterValueDefinition excludeFilter = new FilterValueDefinition(placeHolder, false, Arrays.asList((Object) EmptyValue.EMPTY_VALUE));
            excludeFilter.setShowOnReportView(false);
            Set<AnalysisItem> items = report.getAllAnalysisItems();
            if (items.isEmpty()) {
                intentions.add(new AddReportFieldIntention(findFieldFromList(HighRiseCaseSource.CASE_NAME, fields)));
            }
            intentions.add(new AddFilterIntention(excludeFilter));
        } else if (type == IntentionSuggestion.SUGGESTION_TASK_SETUP) {
            intentions.add(new CustomizeJoinsIntention(fromChildConnections(Arrays.asList(
                new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_COMPANY, HighRiseDealSource.COMPANY_ID,
                    HighRiseCompanySource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_CONTACTS, HighRiseDealSource.CONTACT_ID,
                    HighRiseContactSource.CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_CONTACTS, HighRiseTaskSource.CONTACT_ID,
                        HighRiseContactSource.CONTACT_ID, false, false, false, true),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_CASES, HighRiseTaskSource.CASE_ID,
                        HighRiseCaseSource.CASE_ID),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_DEAL, HighRiseTaskSource.DEAL_ID,
                        HighRiseDealSource.DEAL_ID),
                new ChildConnection(FeedType.HIGHRISE_TASKS, FeedType.HIGHRISE_COMPANY, HighRiseTaskSource.COMPANY_ID,
                        HighRiseCompanySource.COMPANY_ID, false, false, false, true),
                new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_COMPANY, HighRiseCompanySource.COMPANY_ID,
                    HighRiseContactSource.COMPANY_ID, false, false, false, true),
                new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_EMAILS, HighRiseContactSource.CONTACT_ID,
                        HighRiseEmailSource.EMAIL_CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_CONTACTS, FeedType.HIGHRISE_CONTACT_NOTES, HighRiseContactSource.CONTACT_ID,
                        HighRiseContactNotesSource.NOTE_CONTACT_ID),
                new ChildConnection(FeedType.HIGHRISE_DEAL, FeedType.HIGHRISE_DEAL_NOTES, HighRiseDealSource.DEAL_ID,
                        HighRiseDealNotesSource.NOTE_DEAL_ID),
                new ChildConnection(FeedType.HIGHRISE_COMPANY, FeedType.HIGHRISE_COMPANY_NOTES, HighRiseCompanySource.COMPANY_ID,
                        HighRiseCompanyNotesSource.NOTE_COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASES, FeedType.HIGHRISE_CASE_NOTES, HighRiseCaseSource.CASE_ID,
                        HighRiseCaseNotesSource.NOTE_CASE_ID),
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CASES, HighRiseCaseJoinSource.CASE_ID,
                        HighRiseCaseSource.CASE_ID),
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_COMPANY, HighRiseCaseJoinSource.COMPANY_ID,
                        HighRiseCompanySource.COMPANY_ID),
                new ChildConnection(FeedType.HIGHRISE_CASE_JOIN, FeedType.HIGHRISE_CONTACTS, HighRiseCaseJoinSource.CONTACT_ID,
                        HighRiseContactSource.CONTACT_ID)), fields)));
            ReportPropertiesIntention reportPropertiesIntention = new ReportPropertiesIntention();
            reportPropertiesIntention.setFullJoins(true);
            intentions.add(reportPropertiesIntention);
            intentions.add(new AddFilterIntention(excludeFilter(HighRiseTaskSource.BODY, report, fields)));
            DerivedAnalysisDimension taskCompleted = new DerivedAnalysisDimension();
            taskCompleted.setKey(new NamedKey("Task Completed"));
            taskCompleted.setDisplayName("Task Completed");
            taskCompleted.setConcrete(false);
            taskCompleted.setDerivationCode("greaterthan([Task Done At], 0, \"Completed\", \"Not Completed\")");
            intentions.add(new CustomFieldIntention(taskCompleted));
            AnalysisCalculation daysToDue = new AnalysisCalculation();
            daysToDue.setKey(new NamedKey("Days to Due"));
            daysToDue.setConcrete(false);
            daysToDue.setDisplayName("Days to Due");
            FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
            formattingConfiguration.setFormattingType(FormattingConfiguration.MILLISECONDS);
            daysToDue.setFormattingConfiguration(formattingConfiguration);
            daysToDue.setCalculationString("[Task Due At] - now()");
            intentions.add(new CustomFieldIntention(daysToDue));
            Set<AnalysisItem> items = report.getAllAnalysisItems();
            if (items.isEmpty()) {
                intentions.add(new AddReportFieldIntention(findFieldFromList(HighRiseTaskSource.BODY, fields)));
            }
        } else if (type == IntentionSuggestion.SUGGESTION_LAST_CONTACT_NOTE) {
            AnalysisItem noteItem = findFieldFromList(HighRiseContactNotesSource.NOTE_UPDATED_AT, fields);
            LastValueFilter lastValueFilter = new LastValueFilter();
            lastValueFilter.setField(noteItem);
            lastValueFilter.setShowOnReportView(false);
            lastValueFilter.setAbsolute(false);
            lastValueFilter.setThreshold(1);
            intentions.add(new AddFilterIntention(lastValueFilter));
        } else if (type == IntentionSuggestion.SUGGESTION_LAST_COMPANY_NOTE) {
            AnalysisItem noteItem = findFieldFromList(HighRiseCompanyNotesSource.NOTE_UPDATED_AT, fields);
            LastValueFilter lastValueFilter = new LastValueFilter();
            lastValueFilter.setField(noteItem);
            lastValueFilter.setShowOnReportView(false);
            lastValueFilter.setAbsolute(false);
            lastValueFilter.setThreshold(1);
            intentions.add(new AddFilterIntention(lastValueFilter));
        } else if (type == IntentionSuggestion.SUGGESTION_LAST_CASE_NOTE) {
            AnalysisItem noteItem = findFieldFromList(HighRiseCaseNotesSource.NOTE_UPDATED_AT, fields);
            LastValueFilter lastValueFilter = new LastValueFilter();
            lastValueFilter.setField(noteItem);
            lastValueFilter.setShowOnReportView(false);
            lastValueFilter.setAbsolute(false);
            lastValueFilter.setThreshold(1);
            intentions.add(new AddFilterIntention(lastValueFilter));
        } else if (type == IntentionSuggestion.SUGGESTION_LAST_DEAL_NOTE) {
            AnalysisItem noteItem = findFieldFromList(HighRiseDealNotesSource.NOTE_UPDATED_AT, fields);
            LastValueFilter lastValueFilter = new LastValueFilter();
            lastValueFilter.setField(noteItem);
            lastValueFilter.setShowOnReportView(false);
            lastValueFilter.setAbsolute(false);
            lastValueFilter.setThreshold(1);
            intentions.add(new AddFilterIntention(lastValueFilter));
        } else if (type == IntentionSuggestion.SUGGESTION_NOTE_CONFIG) {
            DataSourceIntention dataSourceIntention = new DataSourceIntention();
            dataSourceIntention.setAdminData(true);
            intentions.add(dataSourceIntention);
        } else {
            throw new RuntimeException("Unrecognized intention type");
        }
        return intentions;
    }
}
