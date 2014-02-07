package com.easyinsight.datafeeds.pivotaltrackerv5;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 2/6/14
 * Time: 4:48 PM
 */
public class PivotalTrackerV5EpicSource extends PivotalTrackerV5BaseSource {

    public static final String ID = "ID";
    public static final String PROJECT_ID = "Project ID";
    public static final String NAME = "Name";
    public static final String LABEL_ID = "Label ID";

    public PivotalTrackerV5EpicSource() {
        setFeedName("Epics");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_EPIC;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(LABEL_ID, new AnalysisDimension());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = new HttpClient();
        Map<String, List<String>> epicIDToLabelMap = new HashMap<String, List<String>>();
        List<Map> projects = runRequestForList("projects", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
        for (Map project : projects) {
            String projectID = getJSONValue(project, "id");
            List<Map> epics = runRequestForList("/projects/" + projectID  + "/epics", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
            for (Map epic : epics) {
                IRow row = dataSet.createRow();
                String epicID = getJSONValue(epic, "id");
                row.addValue(keys.get(ID), epicID);
                row.addValue(keys.get(PROJECT_ID), projectID);
                row.addValue(keys.get(NAME), getJSONValue(epic, "name"));
                Map label = (Map) epic.get("label");
                String labelID = label.get("id").toString();
                row.addValue(keys.get(LABEL_ID), labelID);
            }

        }
        PivotalTrackerV5CompositeSource p = (PivotalTrackerV5CompositeSource) parentDefinition;
        p.setEpicIDToLabelMap(epicIDToLabelMap);
        return dataSet;
    }
}
