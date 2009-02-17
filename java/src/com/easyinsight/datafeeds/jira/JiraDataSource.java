package com.easyinsight.datafeeds.jira;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.datafeeds.jira.jiraweb.*;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.ColumnSegmentFactory;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.storage.DataRetrievalManager;
import com.easyinsight.logging.LogClass;
import com.easyinsight.*;
import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.NumericValue;

import javax.xml.rpc.ServiceException;
import java.util.*;

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
            JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
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
            JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
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

    public Map<String, Key> newDataSourceFields() {
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

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys) {
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

    public CredentialsResponse refresh(Credentials credentials) {
        DataSet dataSet = new DataSet();
        ColumnSegmentFactory columnSegmentFactory = new ColumnSegmentFactory();
        PersistableDataSetForm persistable = columnSegmentFactory.createPersistableForm(dataSet, getFields());
        DataRetrievalManager.instance().storeData(getDataFeedID(), persistable);
        return new CredentialsResponse(true);
    }
}
