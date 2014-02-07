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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/6/14
 * Time: 4:48 PM
 */
public class PivotalTrackerV5LabelSource extends PivotalTrackerV5BaseSource {

    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String PROJECT_ID = "Project ID";

    public PivotalTrackerV5LabelSource() {
        setFeedName("Labels");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.PIVOTAL_V5_LABEL;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HttpClient httpClient = new HttpClient();
        List<Map> projects = runRequestForList("projects", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
        for (Map project : projects) {
            String projectID = getJSONValue(project, "id");
            List<Map> labels = runRequestForList("/projects/" + projectID  + "/labels", (PivotalTrackerV5CompositeSource) parentDefinition, httpClient);
            for (Map label : labels) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), getJSONValue(label, "id"));
                row.addValue(keys.get(PROJECT_ID), projectID);
                row.addValue(keys.get(NAME), getJSONValue(label, "name"));
            }

        }
        return dataSet;
    }
}
