package com.easyinsight.datafeeds.teamwork;

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
 * Date: 11/2/14
 * Time: 5:50 PM
 */
public class TeamworkTaskListSource extends TeamworkBaseSource {

    public static final String TODO_LIST_NAME = "Task List Name";
    public static final String TODO_LIST_ID = "Task List ID";
    public static final String TODO_LIST_PROJECT_ID = "Task List Project ID";
    public static final String TODO_LIST_COMPLETED = "Task List Completed";
    public static final String TODO_LIST_DESCRIPTION = "Task List Description";
    public static final String TODO_LIST_MILESTONE_ID = "Task List Milestone ID";

    public TeamworkTaskListSource() {
        setFeedName("Task Lists");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TODO_LIST_NAME, new AnalysisDimension());
        fieldBuilder.addField(TODO_LIST_ID, new AnalysisDimension());
        fieldBuilder.addField(TODO_LIST_PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(TODO_LIST_COMPLETED, new AnalysisDimension());
        fieldBuilder.addField(TODO_LIST_DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(TODO_LIST_MILESTONE_ID, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            TeamworkCompositeSource teamworkCompositeSource = (TeamworkCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(teamworkCompositeSource.getTeamworkApiKey());
            Map results = runRestRequestForMap("projects.json", httpClient, teamworkCompositeSource);
            List<Map> projects = (List<Map>) results.get("projects");
            for (Map project : projects) {
                String projectID = getValue(project, "id");
                Map todoListMap = runRestRequestForMap("/projects/" + projectID + "/todo_lists.json?showTasks=no", httpClient, teamworkCompositeSource);
                List<Map> todoLists = (List<Map>) todoListMap.get("todo-lists");
                for (Map todoList : todoLists) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(TODO_LIST_ID), getValue(todoList, "id"));
                    row.addValue(keys.get(TODO_LIST_NAME), getValue(todoList, "name"));
                    row.addValue(keys.get(TODO_LIST_PROJECT_ID), getValue(todoList, "project_id"));
                    row.addValue(keys.get(TODO_LIST_COMPLETED), getValue(todoList, "complete"));
                    row.addValue(keys.get(TODO_LIST_DESCRIPTION), getValue(todoList, "description"));
                    row.addValue(keys.get(TODO_LIST_MILESTONE_ID), getValue(todoList, "milestone-id"));
                }
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TEAMWORK_TASK_LIST;
    }
}
