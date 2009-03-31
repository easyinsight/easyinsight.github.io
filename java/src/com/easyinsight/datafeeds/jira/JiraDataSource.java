package com.easyinsight.datafeeds.jira;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.jira.jiraweb.*;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.NumericValue;

import javax.xml.rpc.ServiceException;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Feb 15, 2009
 * Time: 8:38:08 PM
 */
public class JiraDataSource extends ServerDataSourceDefinition {

    private String url;
    public static final String REPORTER = "Reporter";
    public static final String COMPONENTS = "Components";
    public static final String DATE_CREATED = "Date Created";
    public static final String PRIORITY = "Priority";
    public static final String STATUS = "Status";
    public static final String ASSIGNEE = "Assignee";
    public static final String COUNT = "Count";
    public static final String PROJECT = "Project";
    public static final String TYPE = "Type";
    public static final String VERSIONS = "Versions";
    public static final String UPDATED = "Date Updated";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequiredAccountTier() {
        return Account.INDIVIDUAL;
    }

    public FeedType getFeedType() {
        return FeedType.JIRA;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    @Override
    public String validateCredentials(Credentials credentials) {
        try {
            JiraSoapServiceServiceLocator jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
            String localURL = (url.startsWith("http://") ? "" : "http://") + url + (url.endsWith("/") ? "" : "/") + "rpc/soap/jirasoapservice-v2";
            System.out.println(localURL);
            jiraSoapServiceGetter.setJirasoapserviceV2EndpointAddress(localURL);
            JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
            jiraSoapService.login(credentials.getUserName(), credentials.getPassword());
            return null;
        } catch (ServiceException e) {
            return e.getMessage();
        } catch (java.rmi.RemoteException e) {
            return e.getMessage();
        }
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys) {
        DataSet dataSet = new DataSet();
        try {
            String userName = credentials.getUserName();
            String password = credentials.getPassword();
            JiraSoapServiceServiceLocator jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
            String jiraURL = (url.startsWith("http://") ? "" : "http://") + url + (url.endsWith("/") ? "" : "/") + "rpc/soap/jirasoapservice-v2";
            jiraSoapServiceGetter.setJirasoapserviceV2EndpointAddress(jiraURL);
            JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
            String token = jiraSoapService.login(userName, password);
            RemoteStatus[] statuses = jiraSoapService.getStatuses(token);
            Map<String, String> statusMap = new HashMap<String, String>();
            for (RemoteStatus remoteStatus : statuses) {
                statusMap.put(remoteStatus.getId(), remoteStatus.getName());
            }
            Map<String, String> priorityMap = new HashMap<String, String>();
            for (RemotePriority remotePriority : jiraSoapService.getPriorities(token)) {
                priorityMap.put(remotePriority.getId(), remotePriority.getName());
            }
            Map<String, String> typeMap = new HashMap<String, String>();
            for (RemoteIssueType type : jiraSoapService.getIssueTypes(token)) {
                typeMap.put(type.getId(), type.getName());
            }
            RemoteIssue[] issues = jiraSoapService.getIssuesFromTextSearch(token, null);
            for (RemoteIssue issue : issues) {
                String reporter = issue.getReporter();
                String componentString = null;
                RemoteComponent[] components = issue.getComponents();
                if (components.length > 0) {
                    StringBuilder componentBuilder = new StringBuilder();
                    for (RemoteComponent component : components) {
                        componentBuilder.append(component.getName());
                        componentBuilder.append(",");
                    }
                    componentString = componentBuilder.substring(0, componentBuilder.length() - 1);
                }
                Calendar created = issue.getCreated();
                String status = statusMap.get(issue.getStatus());
                String priority = priorityMap.get(issue.getPriority());
                String assignee = issue.getAssignee();
                String type = issue.getType();
                String versionString = null;
                RemoteVersion[] versions = issue.getAffectsVersions();
                if (versions.length > 0) {
                    StringBuilder versionBuilder = new StringBuilder();
                    for (RemoteVersion version : versions) {
                        versionBuilder.append(version.getName());
                        versionBuilder.append(",");
                    }
                    versionString = versionBuilder.substring(0, versionBuilder.length() - 1);    
                }
                String project = issue.getProject();
                IRow row = dataSet.createRow();
                row.addValue(keys.get(REPORTER), reporter);
                row.addValue(keys.get(COMPONENTS), componentString);
                row.addValue(keys.get(DATE_CREATED), new DateValue(created.getTime()));
                row.addValue(keys.get(PRIORITY), priority);
                row.addValue(keys.get(STATUS), status);
                row.addValue(keys.get(ASSIGNEE), assignee);
                row.addValue(keys.get(COUNT), new NumericValue(1));
                row.addValue(keys.get(PROJECT), project);
                row.addValue(keys.get(VERSIONS), versionString);
                row.addValue(keys.get(TYPE), typeMap.get(type));
                row.addValue(keys.get(UPDATED), new DateValue(issue.getUpdated().getTime()));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    public Map<String, Key> newDataSourceFields(Credentials credentials) {
        Map<String, Key> keyMap = new HashMap<String, Key>();
        if (getDataFeedID() == 0) {
            keyMap.put(REPORTER, new NamedKey(REPORTER));
            keyMap.put(COMPONENTS, new NamedKey(COMPONENTS));
            keyMap.put(DATE_CREATED, new NamedKey(DATE_CREATED));
            keyMap.put(PRIORITY, new NamedKey(PRIORITY));
            keyMap.put(STATUS, new NamedKey(STATUS));
            keyMap.put(ASSIGNEE, new NamedKey(ASSIGNEE));
            keyMap.put(COUNT, new NamedKey(COUNT));
            keyMap.put(PROJECT, new NamedKey(PROJECT));
            keyMap.put(TYPE, new NamedKey(TYPE));
            keyMap.put(VERSIONS, new NamedKey(VERSIONS));
            keyMap.put(UPDATED, new NamedKey(UPDATED));
        } else {
            for (AnalysisItem field : getFields()) {
                keyMap.put(field.getKey().toKeyString(), field.getKey());
            }
        }
        return keyMap;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(REPORTER), true));
        analysisItems.add(new AnalysisList(keys.get(COMPONENTS), true, ","));
        analysisItems.add(new AnalysisList(keys.get(VERSIONS), true, ","));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(UPDATED), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(PRIORITY), true));
        analysisItems.add(new AnalysisDimension(keys.get(STATUS), true));
        analysisItems.add(new AnalysisDimension(keys.get(ASSIGNEE), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT), true));
        analysisItems.add(new AnalysisDimension(keys.get(TYPE), true));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM JIRA WHERE DATA_FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement jiraStmt = conn.prepareStatement("INSERT INTO JIRA (DATA_FEED_ID, URL) VALUES (?, ?)");
        jiraStmt.setLong(1, getDataFeedID());
        jiraStmt.setString(2, getUrl());
        jiraStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL FROM JIRA WHERE DATA_FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            this.setUrl(rs.getString(1));
        }
    }

    @Override
    public FeedDefinition clone() throws CloneNotSupportedException {
        JiraDataSource jiraDataSource = (JiraDataSource) super.clone();
        jiraDataSource.setUrl("");
        return jiraDataSource;
    }
}
