package com.easyinsight.datafeeds.harvest;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:36 PM
 */
public class HarvestCompositeSource extends CompositeServerDataSource {

    public static final String CONSUMER_KEY = "7wBqPVAr2om0aWwNbHjFHQ==";
    public static final String SECRET_KEY = "qwnEmV0EuIptVUjYRNbgM+D80IdUqBYYkonIGDifTXAmhI8+AS2UXf0xPhzQsAVK8xTp02++C5HDXqAa/g8i5A==";

    private String url = "";
    private String username = "";
    private String password = "";

    private String accessToken;
    private String refreshToken;


    private Document projects;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Document getOrRetrieveProjects(HttpClient client, Builder builder, boolean logRequest) throws ParsingException {
        if(projects == null) {
            projects = runRestRequest("/projects", client, builder, getUrl(), true, this, true);
        }
        return projects;
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new Harvest1To2(this));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String harvestUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "https://") + url;
        if(harvestUrl.endsWith("/")) {
            harvestUrl = harvestUrl.substring(0, harvestUrl.length() - 1);
        }
        if(!(harvestUrl.endsWith(".harvestapp.com")))
            harvestUrl = harvestUrl
                    + ".harvestapp.com";
        return harvestUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HarvestCompositeSource() {
        setFeedName("Harvest");
    }


    public String validateCredentials() {
        Builder builder = new Builder();
        try {
            Document d = runRestRequest("/projects", getHttpClient(username, password), builder, getUrl(), true, null, false);
        } catch(Exception e) {
            return e.getMessage();
        }
        return null;
    }


    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_COMPOSITE;
    }

    public boolean isLongRefresh() {
        return true;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.HARVEST_CLIENT);
        types.add(FeedType.HARVEST_PROJECT);
        types.add(FeedType.HARVEST_TIME);
        types.add(FeedType.HARVEST_CONTACTS);
        types.add(FeedType.HARVEST_TASKS);
        types.add(FeedType.HARVEST_TASK_ASSIGNMENTS);
        types.add(FeedType.HARVEST_USER_ASSIGNMENTS);
        types.add(FeedType.HARVEST_USERS);
        types.add(FeedType.HARVEST_EXPENSES);
        types.add(FeedType.HARVEST_EXPENSE_CATEGORIES);
        types.add(FeedType.HARVEST_INVOICES);
        return types;
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    OAuthClientRequest request;
                    if (ConfigLoader.instance().isProduction()) {
                        request = OAuthClientRequest.tokenLocation(url + "/oauth2/token").
                                    setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(CONSUMER_KEY).
                                    setClientSecret(SECRET_KEY).
                                    setRedirectURI("https://www.easy-insight.com/app/oauth").
                                    setCode(code).buildBodyMessage();
                    } else {
                        request = OAuthClientRequest.tokenLocation(url + "/oauth2/token").
                                setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(CONSUMER_KEY).
                                setClientSecret(SECRET_KEY).
                                setRedirectURI("https://staging.easy-insight.com/app/oauth").
                                setCode(code).buildBodyMessage();
                    }
                    OAuthClient client = new OAuthClient(new URLConnectionClient());
                    OAuthJSONAccessTokenResponse response = client.accessToken(request);
                    accessToken = response.getAccessToken();
                    refreshToken = response.getRefreshToken();
                }
            } /*else if (refreshToken != null && !"".equals(refreshToken)) {
                try {
                    refreshTokenInfo();
                } catch (OAuthProblemException e) {
                    // no joy on refresh token
                }
            }*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void beforeRefresh(Date lastRefreshTime) {
        super.beforeRefresh(lastRefreshTime); 
        if (lastRefreshTime != null) {
            try {
                refreshTokenInfo();
            } catch (Exception e) {
                throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), this));
            }
        }
    }

    public void refreshTokenInfo() throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest request = OAuthClientRequest.tokenLocation(url + "/oauth2/token").
                        setGrantType(GrantType.REFRESH_TOKEN).setClientId(CONSUMER_KEY).
                        setClientSecret(SECRET_KEY).setRefreshToken(refreshToken).buildBodyMessage();
        OAuthClient client = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse response = client.accessToken(request);

        accessToken = response.getAccessToken();
        refreshToken = response.getRefreshToken();
        System.out.println("updated access token to " + accessToken + " and refresh token to " + refreshToken);
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.HARVEST_CLIENT, FeedType.HARVEST_CONTACTS, HarvestClientSource.CLIENT_ID, HarvestClientContactSource.CLIENT_ID),
                new ChildConnection(FeedType.HARVEST_CLIENT, FeedType.HARVEST_PROJECT,  HarvestClientSource.CLIENT_ID, HarvestProjectSource.CLIENT_ID),
                new ChildConnection(FeedType.HARVEST_TASKS, FeedType.HARVEST_TIME, HarvestTaskSource.ID, HarvestTimeSource.TASK_ID),
                new ChildConnection(FeedType.HARVEST_PROJECT, FeedType.HARVEST_TIME, HarvestProjectSource.PROJECT_ID, HarvestTimeSource.PROJECT_ID),
                new ChildConnection(FeedType.HARVEST_PROJECT, FeedType.HARVEST_TASK_ASSIGNMENTS, HarvestProjectSource.PROJECT_ID, HarvestTaskAssignmentSource.PROJECT_ID),
                new ChildConnection(FeedType.HARVEST_TASKS, FeedType.HARVEST_TASK_ASSIGNMENTS, HarvestTaskSource.ID,  HarvestTaskAssignmentSource.TASK_ID),
                new ChildConnection(FeedType.HARVEST_USERS, FeedType.HARVEST_TIME, HarvestUserSource.USER_ID,  HarvestTimeSource.USER_ID),
                new ChildConnection(FeedType.HARVEST_USER_ASSIGNMENTS, FeedType.HARVEST_USERS, HarvestUserAssignmentSource.USER_ID,  HarvestUserSource.USER_ID),
                new ChildConnection(FeedType.HARVEST_USER_ASSIGNMENTS, FeedType.HARVEST_PROJECT, HarvestUserAssignmentSource.PROJECT_ID,  HarvestProjectSource.PROJECT_ID),
                new ChildConnection(FeedType.HARVEST_PROJECT, FeedType.HARVEST_EXPENSES, HarvestProjectSource.PROJECT_ID, HarvestExpenseSource.PROJECT_ID),
                new ChildConnection(FeedType.HARVEST_USERS,  FeedType.HARVEST_EXPENSES, HarvestUserSource.USER_ID, HarvestExpenseSource.USER_ID),
                new ChildConnection(FeedType.HARVEST_EXPENSE_CATEGORIES, FeedType.HARVEST_EXPENSES, HarvestExpenseCategoriesSource.ID, HarvestExpenseSource.EXPENSE_CATEGORY_ID),
                new ChildConnection(FeedType.HARVEST_CLIENT, FeedType.HARVEST_INVOICES, HarvestClientSource.CLIENT_ID, HarvestInvoiceSource.CLIENT_ID)
                );
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStatement = conn.prepareStatement("delete from HARVEST where data_feed_id = ?");
        deleteStatement.setLong(1, this.getDataFeedID());
        deleteStatement.execute();
        PreparedStatement statement = conn.prepareStatement("insert into HARVEST(data_feed_id, url, username, password, access_token, refresh_token) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setLong(1, this.getDataFeedID());
        statement.setString(2, getUrl());
        statement.setString(3, getUsername());
        statement.setString(4, getPassword() != null ? PasswordStorage.encryptString(getPassword()) : null);
        statement.setString(5, getAccessToken());
        statement.setString(6, getRefreshToken());
        statement.execute();
        statement.close();
        deleteStatement.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement statement = conn.prepareStatement("select url, username, password, access_token, refresh_token from HARVEST where data_feed_id = ?");
        statement.setLong(1, getDataFeedID());
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            this.setUrl(rs.getString(1));
            this.setUsername(rs.getString(2));
            String pw = rs.getString(3);
            this.setPassword(pw != null ? PasswordStorage.decryptString(pw) : null);
            setAccessToken(rs.getString(4));
            setRefreshToken(rs.getString(5));
        }
        statement.close();
    }

    @Override
    public List<KPI> createKPIs() {
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(findAnalysisItem(HarvestInvoiceSource.ISSUED_AT));
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.QUARTER);
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIWithFilters("Invoiced Dollars in the Last 90 Days", "document.png", (AnalysisMeasure) findAnalysisItem(HarvestInvoiceSource.AMOUNT),
            Arrays.asList((FilterDefinition) rollingFilterDefinition), KPI.GOOD, 90));
        kpis.add(KPIUtil.createKPIForDateFilter("Hours Tracked in the Last 90 Days", "clock.png", (AnalysisMeasure) findAnalysisItem(HarvestTimeSource.HOURS),
            (AnalysisDimension) findAnalysisItem(HarvestTimeSource.CREATED_AT), MaterializedRollingFilterDefinition.QUARTER, new ArrayList<FilterDefinition>(), KPI.GOOD, 90));
        kpis.add(KPIUtil.createKPIForDateFilter("Expenses in the Last 90 Days", "money.png", (AnalysisMeasure) findAnalysisItem(HarvestExpenseSource.TOTAL_COST),
            (AnalysisDimension) findAnalysisItem(HarvestExpenseSource.SPENT_AT), MaterializedRollingFilterDefinition.QUARTER, new ArrayList<FilterDefinition>(), KPI.GOOD, 90));
        return kpis;
    }

    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        return client;
    }

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, boolean badCredentialsOnError, FeedDefinition parentDefinition, boolean logRequest) throws ParsingException, ReportException {
        HarvestCompositeSource harvestCompositeSource = (HarvestCompositeSource) parentDefinition;
        String accessToken;
        try {
            accessToken = URLEncoder.encode(harvestCompositeSource.getAccessToken(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String target = url + path;
        if (target.contains("?")) {
            target = target + "&" + "access_token=" + accessToken;
        } else {
            target = target + "?" + "access_token=" + accessToken;
        }
        HttpMethod restMethod = new GetMethod(target);
        /*try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }*/
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        Document doc = null;
        int retryCount = 0;
        do {

            try {
                client.executeMethod(restMethod);
                /*if (logRequest) {
                    System.out.println(restMethod.getResponseBodyAsString());
                }*/
                doc = builder.build(restMethod.getResponseBodyAsStream());
                String statusLine = restMethod.getStatusLine().toString();
                //System.out.println(statusLine);
                if(statusLine.indexOf("503") != -1) {
                    retryCount++;
                    Header retryHeader = restMethod.getResponseHeader("Retry-After");
                    if (retryHeader == null) {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        int retryTime = Integer.parseInt(retryHeader.getValue());
                        int time = retryTime * 1000;
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                        }
                    }
                } else {
                    String rootValue = doc.getRootElement().getValue();
                    successful = true;
                }
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
                retryCount++;
                if (e.getMessage().contains("503")) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ParsingException e) {
                System.out.println(e.getMessage());
                retryCount++;
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Harvest instance at " + url, parentDefinition));
                } else if (statusLine.indexOf("503") != -1) {
                    Header retryHeader = restMethod.getResponseHeader("Retry-After");
                    if (retryHeader == null) {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        int retryTime = Integer.parseInt(retryHeader.getValue());
                        int time = retryTime * 1000;
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                        }
                    }
                } else if (statusLine.indexOf("403") != -1) {
                    throw new RuntimeException("403 error");
                } else {
                    if (badCredentialsOnError) {
                        throw new ReportException(new DataSourceConnectivityReportFault("Invalid Harvest credentials in connecting to " + url + ".", parentDefinition));
                    } else {
                        LogClass.error("Unrelated parse error with status line " + statusLine);
                        throw e;
                    }
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Harvest could not be reached due to a large number of current users, please try again in a bit.");
        }
        return doc;
    }

    @Override
    public boolean checkDateTime(String name, Key key) {
        return false;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }
}

