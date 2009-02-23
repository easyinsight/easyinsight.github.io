package com.easyinsight.datafeeds.jira;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.CredentialsDefinition;
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
public class JiraDataSource extends FeedDefinition {

    private String url;

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

    public FeedDefinition ddl() {
        return new JiraDataSource();
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
            jiraSoapServiceGetter.setJirasoapserviceV2EndpointAddress((url.startsWith("http://") ? "" : "http://") + url + (url.endsWith("/") ? "" : "/") + "rpc/soap/jirasoapservice-v2");
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
                IRow row = dataSet.createRow();
                row.addValue(keys.get("Reporter"), reporter);
                row.addValue(keys.get("Components"), componentString);
                row.addValue(keys.get("Date Created"), new DateValue(created.getTime()));
                row.addValue(keys.get("Priority"), priority);
                row.addValue(keys.get("Status"), status);
                row.addValue(keys.get("Assignee"), assignee);
                row.addValue(keys.get("Count"), new NumericValue(1));
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
            keyMap.put("Reporter", new NamedKey("Reporter"));
            keyMap.put("Components", new NamedKey("Components"));
            keyMap.put("Date Created", new NamedKey("Date Created"));
            keyMap.put("Priority", new NamedKey("Priority"));
            keyMap.put("Status", new NamedKey("Status"));
            keyMap.put("Assignee", new NamedKey("Assignee"));
            keyMap.put("Count", new NamedKey("Count"));
        } else {
            for (AnalysisItem field : getFields()) {
                keyMap.put(field.getKey().toKeyString(), field.getKey());
            }
        }
        return keyMap;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get("Reporter"), true));
        analysisItems.add(new AnalysisList(keys.get("Components"), true, ","));
        analysisItems.add(new AnalysisDateDimension(keys.get("Date Created"), true, AnalysisItemTypes.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get("Priority"), true));
        analysisItems.add(new AnalysisDimension(keys.get("Status"), true));
        analysisItems.add(new AnalysisDimension(keys.get("Assignee"), true));
        analysisItems.add(new AnalysisMeasure(keys.get("Count"), AggregationTypes.SUM));
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
}
