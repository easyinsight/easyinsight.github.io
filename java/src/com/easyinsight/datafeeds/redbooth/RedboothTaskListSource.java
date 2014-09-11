package com.easyinsight.datafeeds.redbooth;

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
import java.util.Set;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 7:09 PM
 */
public class RedboothTaskListSource extends RedboothBaseSource {
    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String PROJECT_ID = "Project ID";

    public RedboothTaskListSource() {
        setFeedName("Task Lists");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_TASK_LIST;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        RedboothCompositeSource redboothCompositeSource = (RedboothCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(redboothCompositeSource);
        List<Map> organizations = (List<Map>) queryList("/api/3/task_lists", redboothCompositeSource, httpClient);
        Set<String> validIDs = redboothCompositeSource.getValidProjects();
        for (Map org : organizations) {
            String projectID = getJSONValue(org, "project_id");
            if (!validIDs.contains(projectID)) {
                continue;
            }
            IRow row = dataSet.createRow();
            row.addValue(keys.get(ID), getJSONValue(org, "id"));
            row.addValue(keys.get(NAME), getJSONValue(org, "name"));
            row.addValue(keys.get(PROJECT_ID), projectID);
        }
        return dataSet;
    }
}
