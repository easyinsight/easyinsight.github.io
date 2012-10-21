package com.easyinsight.datafeeds.youtrack;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/16/12
 * Time: 5:51 PM
 */
public class YouTrackIssueSource extends YouTrackBaseSource {

    public static final String SUMMARY = "Summary";
    public static final String PROJECT = "Issue Project ID";
    public static final String REPORTER_NAME = "Reporter Name";
    public static final String DESCRIPTION = "Description";
    public static final String CREATED = "Issue Created At";
    public static final String UPDATED = "Issue Updated At";
    public static final String RESOLVED = "Issue Resolved At";
    public static final String COUNT = "Issue Count";
    public static final String ID = "Issue ID";

    public YouTrackIssueSource() {
        setFeedName("Issues");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(SUMMARY, REPORTER_NAME, DESCRIPTION, CREATED, RESOLVED, UPDATED, COUNT, ID, PROJECT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.YOUTRACK_TASKS;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        try {
            Document doc = runRestRequest("/rest/admin/project", new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
            Nodes projects = doc.query("/projectRefs/project");
            Set<String> fieldNameSet = new HashSet<String>();
            for (int i = 0; i < projects.size(); i++) {
                Element project = (Element) projects.get(i);
                String id = project.getAttribute("id").getValue();
                Document customFieldsDoc = runRestRequest("/rest/admin/project/"+id+"/customfield", new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
                Nodes customFieldNodes = customFieldsDoc.query("/projectCustomFieldRefs/projectCustomField");
                for (int j = 0; j < customFieldNodes.size(); j++) {
                    Element customFieldElement = (Element) customFieldNodes.get(j);
                    fieldNameSet.add(customFieldElement.getAttribute("name").getValue());
                }
            }
            for (String name : fieldNameSet) {
                Key key = keys.get(name);
                if (key == null) {
                    key = new NamedKey(name);
                }
                fields.add(new AnalysisDimension(key));
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        fields.add(new AnalysisDimension(keys.get(SUMMARY)));
        fields.add(new AnalysisDimension(keys.get(PROJECT)));
        fields.add(new AnalysisDimension(keys.get(REPORTER_NAME)));
        fields.add(new AnalysisDimension(keys.get(DESCRIPTION)));
        fields.add(new AnalysisDimension(keys.get(ID)));

        fields.add(new AnalysisDateDimension(keys.get(CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(RESOLVED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(UPDATED), true, AnalysisDateDimension.DAY_LEVEL));

        fields.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return fields;
    }

    public String getValue(Node node, String path) {
        Nodes nodes = node.query(path);
        if (nodes.size() == 0) {
            return "";
        } else {
            return nodes.get(0).getValue();
        }
    }

    public Value getDate(Node node, String path) {
        Nodes nodes = node.query(path);
        if (nodes.size() == 0) {
            return new EmptyValue();
        } else {
            return new DateValue(new Date(Long.parseLong(nodes.get(0).getValue())));
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Document doc = runRestRequest("/rest/admin/project", new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
            Nodes projects = doc.query("/projectRefs/project");
            DataSet dataSet = new DataSet();
            for (int i = 0; i < projects.size(); i++) {
                Element project = (Element) projects.get(i);
                String id = project.getAttribute("id").getValue();
                Document customFieldsDoc = runRestRequest("/rest/admin/project/"+id+"/customfield", new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
                Nodes customFieldNodes = customFieldsDoc.query("/projectCustomFieldRefs/projectCustomField");
                Set<String> fieldNameSet = new HashSet<String>();
                for (int j = 0; j < customFieldNodes.size(); j++) {
                    Element customFieldElement = (Element) customFieldNodes.get(j);
                    fieldNameSet.add(customFieldElement.getAttribute("name").getValue());
                }
                int masterCount = 0;
                int count;
                do {
                    count = 0;
                    Document issuesDoc;
                    if (masterCount == 0) {
                        issuesDoc = runRestRequest("/rest/export/" + id + "/issues?max=20", new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
                    } else {
                        issuesDoc = runRestRequest("/rest/export/" + id + "/issues?max=20&after=" + masterCount, new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
                    }
                    Nodes issues = issuesDoc.query("/issues/issue");
                    for (int j = 0; j < issues.size(); j++) {
                        IRow row = dataSet.createRow();
                        Node issueNode = issues.get(j);
                        for (String field : fieldNameSet) {
                            row.addValue(field, getValue(issueNode, "field[@name='"+field+"']/value/text()"));
                        }
                        row.addValue(ID, getValue(issueNode, "field[@name='numberInProject']/value/text()"));
                        row.addValue(REPORTER_NAME, getValue(issueNode, "field[@name='reporterName']/value/text()"));
                        row.addValue(SUMMARY, getValue(issueNode, "field[@name='summary']/value/text()"));
                        row.addValue(DESCRIPTION, getValue(issueNode, "field[@name='description']/value/text()"));
                        row.addValue(CREATED, getDate(issueNode, "field[@name='created']/value/text()"));
                        row.addValue(UPDATED, getDate(issueNode, "field[@name='updated']/value/text()"));
                        row.addValue(RESOLVED, getDate(issueNode, "field[@name='resolved']/value/text()"));
                        row.addValue(PROJECT, id);
                        row.addValue(COUNT, 1);
                        count++;
                        masterCount++;
                    }
                } while (count == 20);
            }

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
