package com.easyinsight.datafeeds.harvest;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.PasswordService;
import com.easyinsight.users.Account;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
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

    private String url = "";
    private String username = "";
    private String password = "";

    private Document projects;

    public Document getOrRetrieveProjects(HttpClient client, Builder builder) throws ParsingException {
        if(projects == null) {
            projects = runRestRequest("/projects", client, builder, getUrl(), true, this, true);
        }
        return projects;
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
            Document d = runRestRequest("/projects", getHttpClient(username, password), builder, getUrl(), true, null, true);
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
        types.add(FeedType.HARVEST_USERS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.HARVEST_CLIENT, FeedType.HARVEST_CONTACTS, HarvestClientSource.CLIENT_ID, HarvestClientContactSource.CLIENT_ID),
                new ChildConnection(FeedType.HARVEST_CLIENT, FeedType.HARVEST_PROJECT,  HarvestClientSource.CLIENT_ID, HarvestProjectSource.CLIENT_ID),
                new ChildConnection(FeedType.HARVEST_TASKS, FeedType.HARVEST_TIME, HarvestTaskSource.ID, HarvestTimeSource.TASK_ID),
                new ChildConnection(FeedType.HARVEST_PROJECT, FeedType.HARVEST_TIME, HarvestProjectSource.PROJECT_ID, HarvestTimeSource.PROJECT_ID),
                new ChildConnection(FeedType.HARVEST_PROJECT, FeedType.HARVEST_TASK_ASSIGNMENTS, HarvestProjectSource.PROJECT_ID, HarvestTaskAssignmentSource.PROJECT_ID),
                new ChildConnection(FeedType.HARVEST_TASKS, FeedType.HARVEST_TASK_ASSIGNMENTS, HarvestTaskSource.ID,  HarvestTaskAssignmentSource.TASK_ID),
                new ChildConnection(FeedType.HARVEST_USERS, FeedType.HARVEST_TIME, HarvestUserSource.USER_ID,  HarvestTimeSource.USER_ID)
                );
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStatement = conn.prepareStatement("delete from HARVEST where data_feed_id = ?");
        deleteStatement.setLong(1, this.getDataFeedID());
        deleteStatement.execute();
        PreparedStatement statement = conn.prepareStatement("insert into HARVEST(data_feed_id, url, username, password) VALUES (?, ?, ?, ?)");
        statement.setLong(1, this.getDataFeedID());
        statement.setString(2, getUrl());
        statement.setString(3, getUsername());
        statement.setString(4, getPassword() != null ? PasswordStorage.encryptString(getPassword()) : null);
        statement.execute();
        statement.close();
        deleteStatement.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement statement = conn. ("select url, username, password from HARVEST where data_feed_id = ?");
        statement.setLong(1, getDataFeedID());
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            this.setUrl(rs.getString(1));
            this.setUsername(rs.getString(2));
            String pw = rs.getString(3);
            this.setPassword(pw != null ? PasswordStorage.decryptString(pw) : null);
        }
        statement.close();
    }

    @Override
    public List<KPI> createKPIs() {
        return new ArrayList<KPI>();
    }

    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static Document runRestRequest(String path, HttpClient client, Builder builder, String url, boolean badCredentialsOnError, FeedDefinition parentDefinition, boolean logRequest) throws ParsingException, ReportException {
        System.out.println(url + path);
        HttpMethod restMethod = new GetMethod(url + path);
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
                if (logRequest) {
                    System.out.println(restMethod.getResponseBodyAsString());
                }
                doc = builder.build(restMethod.getResponseBodyAsStream());
                String statusLine = restMethod.getStatusLine().toString();
                System.out.println(statusLine);
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
}

