package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.MultiChildConnection;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;
import nu.xom.Builder;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:36:35 PM
 */
public class BaseCampCompositeSource extends CompositeServerDataSource {

    private String url = "";
    private boolean includeArchived;
    private boolean includeInactive;
    private boolean includeMilestoneComments;
    private boolean includeTodoComments;
    private String token;

    private transient BaseCampCache basecampCache;

    public BaseCampCompositeSource() {
        setFeedName("Basecamp");
    }

    public BaseCampCache getOrCreateCache(HttpClient httpClient) throws ParsingException, BaseCampLoginException, ReportException {
        if (basecampCache == null) {
            basecampCache = new BaseCampCache();
            basecampCache.populateCaches(httpClient, getUrl(), this);
        }
        return basecampCache;
    }

    public boolean customJoinsAllowed() {
        return false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIncludeInactive() {
        return includeInactive;
    }

    public void setIncludeInactive(boolean includeInactive) {
        this.includeInactive = includeInactive;
    }

    public boolean isIncludeArchived() {
        return includeArchived;
    }

    public void setIncludeArchived(boolean includeArchived) {
        this.includeArchived = includeArchived;
    }

    public boolean isIncludeMilestoneComments() {
        return includeMilestoneComments;
    }

    public void setIncludeMilestoneComments(boolean includeMilestoneComments) {
        this.includeMilestoneComments = includeMilestoneComments;
    }

    public boolean isIncludeTodoComments() {
        return includeTodoComments;
    }

    public void setIncludeTodoComments(boolean includeTodoComments) {
        this.includeTodoComments = includeTodoComments;
    }

    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    protected Map<String, String> createProperties() {
        Map<String, String> properties = super.createProperties();
        properties.put("basecamp.url", url);
        return properties;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_MASTER;
    }

    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> feedTypes = new HashSet<FeedType>();
        feedTypes.add(FeedType.BASECAMP);
        feedTypes.add(FeedType.BASECAMP_TIME);
        feedTypes.add(FeedType.BASECAMP_COMPANY);
        feedTypes.add(FeedType.BASECAMP_COMPANY_PROJECT_JOIN);
        feedTypes.add(FeedType.BASECAMP_COMMENTS);
        feedTypes.add(FeedType.BASECAMP_TODO_COMMENTS);
        return feedTypes;
    }
    
    public boolean isConfigured() {
        return url != null && !url.isEmpty();
    }

    private static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    private String runRestRequest(String path, HttpClient client, Builder builder) {
        String failureMessage = null;
        HttpMethod restMethod = new GetMethod(getUrl() + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        try {
            client.executeMethod(restMethod);
            builder.build(restMethod.getResponseBodyAsStream());
        } catch (UnknownHostException uhe) {
            failureMessage = "Could not recognize host " + getUrl() + " ";
        }
        catch (nu.xom.ParsingException e) {
            String statusLine = restMethod.getStatusLine().toString();
            if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                failureMessage = "Could not locate a Basecamp instance at " + getUrl();
            } else {
                failureMessage = "Invalid Basecamp authentication token connecting to " + getUrl() + "--you can find the token under your the My Info link in the upper right corner on your Basecamp page.";
            }

        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return failureMessage;
    }

    public String validateCredentials() {
        HttpClient client = getHttpClient(token, "");
        Pattern p = Pattern.compile("(http(s?)://)?([A-Za-z0-9]|\\-)+(\\.(basecamphq|projectpath|seework|clientsection|grouphub|updatelog)\\.com)?");
        Matcher m = p.matcher(url);
        String result;
        if(!m.matches()) {
            result = "Invalid url. Please input a proper URL.";
        } else {
            result = runRestRequest("/projects.xml", client, new Builder());
        }
        return result;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.BASECAMP,  FeedType.BASECAMP_TIME, BaseCampTodoSource.ITEMID,  BaseCampTimeSource.TODOID),
                new ChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_COMPANY_PROJECT_JOIN, BaseCampTodoSource.PROJECTID, BaseCampCompanyProjectJoinSource.PROJECT_ID),
                new ChildConnection(FeedType.BASECAMP_COMPANY_PROJECT_JOIN, FeedType.BASECAMP_COMPANY, BaseCampCompanyProjectJoinSource.COMPANY_ID, BaseCampCompanySource.COMPANY_ID));
    }

    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(
                new ChildConnection(FeedType.BASECAMP_COMPANY_PROJECT_JOIN, FeedType.BASECAMP_COMPANY, BaseCampCompanyProjectJoinSource.COMPANY_ID, BaseCampCompanySource.COMPANY_ID),
                new ChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_COMPANY_PROJECT_JOIN, BaseCampTodoSource.PROJECTID, BaseCampCompanyProjectJoinSource.PROJECT_ID),
                new ChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_COMMENTS, BaseCampTodoSource.MILESTONE_ID, BaseCampCommentsSource.MILESTONE_ID),
                new ChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_TODO_COMMENTS, BaseCampTodoSource.ITEMID, BaseCampTodoCommentsSource.TODO_ID),
                new MultiChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_TIME, Arrays.asList(BaseCampTodoSource.ITEMID, BaseCampTodoSource.PROJECTID),
                Arrays.asList(BaseCampTimeSource.TODOID, BaseCampTimeSource.PROJECTID), Arrays.asList(Arrays.asList(BaseCampTodoSource.TODOLISTNAME,
                        BaseCampTodoSource.TODOLISTID, BaseCampTodoSource.CONTENT, BaseCampTodoSource.ITEMID, BaseCampTodoSource.MILESTONENAME,
                        BaseCampTodoSource.COMPLETED, BaseCampTodoSource.MILESTONE_COMPLETED_ON, BaseCampTodoSource.MILESTONE_CREATED_ON,
                        BaseCampTodoSource.MILESTONE_CREATED_ON, BaseCampTodoSource.DEADLINE, BaseCampTodoSource.CONTENT,
                        BaseCampTodoSource.CREATEDDATE, BaseCampTodoSource.CREATORID, BaseCampTodoSource.CREATORNAME, BaseCampTodoSource.RESPONSIBLEPARTYNAME,
                        BaseCampTodoSource.RESPONSIBLEPARTYID, BaseCampTodoSource.DUEON, BaseCampTodoSource.COMPLETEDDATE,
                        BaseCampTodoSource.COMPLETERID, BaseCampTodoSource.COMPLETERNAME, BaseCampTodoSource.COUNT, BaseCampTodoSource.MILESTONE_OWNER,
                        BaseCampTodoSource.ANNOUNCEMENT, BaseCampTodoSource.TODOLISTDESC), new ArrayList<String>()),
                Arrays.asList(Arrays.asList(BaseCampTimeSource.TODOID), new ArrayList<String>()), true, false));
    }
    
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM BASECAMP WHERE DATA_FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement basecampStmt = conn.prepareStatement("INSERT INTO BASECAMP (DATA_FEED_ID, URL, INCLUDE_ARCHIVED," +
                "include_inactive, INCLUDE_COMMENTS, INCLUDE_TODO_COMMENTS) VALUES (?, ?, ?, ?, ?, ?)");
        basecampStmt.setLong(1, getDataFeedID());
        basecampStmt.setString(2, getUrl());
        basecampStmt.setBoolean(3, isIncludeArchived());
        basecampStmt.setBoolean(4, isIncludeInactive());
        basecampStmt.setBoolean(5, isIncludeMilestoneComments());
        basecampStmt.setBoolean(6, isIncludeTodoComments());
        basecampStmt.execute();
        basecampStmt.close();
        if (this.token != null && !"".equals(this.token.trim())) {
            Token token = new Token();
            token.setTokenValue(this.token.trim());
            token.setTokenType(TokenStorage.BASECAMP_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, getDataFeedID(), (EIConnection) conn);
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL, INCLUDE_ARCHIVED, INCLUDE_INACTIVE, INCLUDE_COMMENTS, INCLUDE_TODO_COMMENTS FROM " +
                "BASECAMP WHERE DATA_FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            this.setUrl(rs.getString(1));
            this.setIncludeArchived(rs.getBoolean(2));
            this.setIncludeInactive(rs.getBoolean(3));
            this.setIncludeMilestoneComments(rs.getBoolean(4));
            this.setIncludeTodoComments(rs.getBoolean(5));
        }
        loadStmt.close();
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if(!(basecampUrl.endsWith(".basecamphq.com") || basecampUrl.endsWith(".projectpath.com") || basecampUrl.endsWith(".seework.com")
        || basecampUrl.endsWith(".clientsection.com") || basecampUrl.endsWith(".grouphub.com") || basecampUrl.endsWith(".updatelog.com")))
            basecampUrl = basecampUrl + ".basecamphq.com";
        return basecampUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        BaseCampCompositeSource dataSource = (BaseCampCompositeSource) super.clone(conn);
        dataSource.setUrl("");      
        return dataSource;
    }

    @Override
    public int getVersion() {
        return 5;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new BaseCamp1To2(this), new BaseCamp2To3(this), new BaseCamp3To4(this), new BaseCamp4To5(this));
    }

    @Override
    public String getFilterExampleMessage() {
        return "On the left, you'll see the list of fields available to you. Drag a field from that list into the area to the right to create a filter. For example, drag Project Name into the area to the right to restrict the KPI to a particular project.";
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        FilterValueDefinition openFilter = new FilterValueDefinition();
        openFilter.setField(findAnalysisItem(BaseCampTodoSource.COMPLETED));
        openFilter.setFilteredValues(Arrays.asList((Object) "false"));
        openFilter.setInclusive(true);
        kpis.add(KPIUtil.createKPIWithFilters("Total Open Todo Items", "inbox.png", (AnalysisMeasure) findAnalysisItemByDisplayName("Todo - Count"),
                Arrays.asList((FilterDefinition) openFilter), KPI.BAD, 0));
        FilterValueDefinition closedFilter = new FilterValueDefinition();
        closedFilter.setField(findAnalysisItem(BaseCampTodoSource.COMPLETED));
        closedFilter.setInclusive(true);
        closedFilter.setFilteredValues(Arrays.asList((Object) "true"));
        kpis.add(KPIUtil.createKPIForDateFilter("Todo Items Closed in the Last Seven Days", "inbox.png", (AnalysisMeasure) findAnalysisItemByDisplayName("Todo - Count"),
                (AnalysisDimension) findAnalysisItem(BaseCampTodoSource.COMPLETEDDATE), MaterializedRollingFilterDefinition.WEEK,
                Arrays.asList((FilterDefinition) closedFilter), KPI.GOOD, 7));
        kpis.add(KPIUtil.createKPIForDateFilter("Hours Worked Month to Date", "clock.png", (AnalysisMeasure) findAnalysisItem(BaseCampTimeSource.HOURS),
                (AnalysisDimension) findAnalysisItem(BaseCampTimeSource.DATE), MaterializedRollingFilterDefinition.MONTH_TO_NOW,
                null, KPI.GOOD, 7));
        return kpis;
    }

    public boolean isLongRefresh() {
        return true;
    }

    public void decorateLinks(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.getLinks() == null) {
                analysisItem.setLinks(new ArrayList<Link>());
            }
            if (isProjectLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/projects/["+BaseCampTodoSource.PROJECTID+"]");
                urlLink.setLabel("View Project in Basecamp...");
                analysisItem.getLinks().add(urlLink);
            } else if (isTodoListLinkable(analysisItem)) {
                URLLink urlLink = new URLLink();
                urlLink.setUrl(getUrl() + "/projects/["+BaseCampTodoSource.PROJECTID+"]/todo_lists/[" + BaseCampTodoSource.TODOLISTID + "]");
                urlLink.setLabel("View Todo List in Basecamp...");
                analysisItem.getLinks().add(urlLink);
            }
        }
    }

    private boolean isProjectLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toBaseKey().toKeyString();
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            return false;
        }
        if (BaseCampTodoSource.PROJECTNAME.equals(keyString) ||
                BaseCampTimeSource.PROJECTNAME.equals(keyString)) {
            return true;
        }
        return false;
    }

    private boolean isTodoListLinkable(AnalysisItem analysisItem) {
        String keyString = analysisItem.getKey().toKeyString();
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            return false;
        }
        if (BaseCampTodoSource.TODOLISTNAME.equals(keyString)) {
            return true;
        }
        return false;
    }
}
