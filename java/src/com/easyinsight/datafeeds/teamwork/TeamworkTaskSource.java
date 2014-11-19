package com.easyinsight.datafeeds.teamwork;

import com.easyinsight.analysis.*;
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
public class TeamworkTaskSource extends TeamworkBaseSource {

    public static final String TASK_CONTENT = "Task Name";
    public static final String TASK_ID = "Task ID";
    public static final String TASK_PROJECT_ID = "Task Project ID";
    public static final String TASK_COMMENTS = "Task Comment Count";
    public static final String TASK_COUNT = "Task Count";
    public static final String TASK_CREATED_ON = "Task Created On";
    public static final String TASK_COMPLETED = "Task Completed";
    public static final String TASK_POSITION = "Task Position";
    public static final String TASK_ESTIMATED_MINUTES = "Task Estimated Minutes";
    public static final String TASK_DESCRIPTION = "Task Description";
    public static final String TASK_PROGRESS = "Task Progress";
    public static final String TASK_RESPONSIBLE_PARTY = "Task Responsible Party";
    public static final String TASK_CREATOR = "Task Creator";
    public static final String TASK_TODO_LIST_ID = "Task List ID";
    public static final String TASK_DUE_DATE = "Task Due Date";
    public static final String TASK_STATUS = "Task Status";
    public static final String TASK_PRIORITY = "Task Priority";

    public TeamworkTaskSource() {
        setFeedName("Tasks");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TASK_CONTENT, new AnalysisDimension());
        fieldBuilder.addField(TASK_ID, new AnalysisDimension());
        fieldBuilder.addField(TASK_PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(TASK_COMPLETED, new AnalysisDimension());
        fieldBuilder.addField(TASK_DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(TASK_RESPONSIBLE_PARTY, new AnalysisDimension());
        fieldBuilder.addField(TASK_CREATOR, new AnalysisDimension());
        fieldBuilder.addField(TASK_TODO_LIST_ID, new AnalysisDimension());
        fieldBuilder.addField(TASK_STATUS, new AnalysisDimension());
        fieldBuilder.addField(TASK_PRIORITY, new AnalysisDimension());
        fieldBuilder.addField(TASK_CREATED_ON, new AnalysisDateDimension());
        fieldBuilder.addField(TASK_DUE_DATE, new AnalysisDateDimension(true));
        fieldBuilder.addField(TASK_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(TASK_POSITION, new AnalysisMeasure());
        fieldBuilder.addField(TASK_COMMENTS, new AnalysisMeasure());
        fieldBuilder.addField(TASK_ESTIMATED_MINUTES, new AnalysisMeasure());
        fieldBuilder.addField(TASK_PROGRESS, new AnalysisMeasure());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            TeamworkCompositeSource teamworkCompositeSource = (TeamworkCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(teamworkCompositeSource.getTeamworkApiKey());
            int resultCount;
            int page = 1;
            do {
                resultCount = 0;
                Map results = runRestRequestForMap("tasks.json?page=" + page + "&pageSize=250&filter=all", httpClient, teamworkCompositeSource);
                List<Map> projects = (List<Map>) results.get("todo-items");
                for (Map project : projects) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(TASK_CONTENT), getValue(project, "content"));
                    row.addValue(keys.get(TASK_ID), getValue(project, "id"));
                    row.addValue(keys.get(TASK_PROJECT_ID), getValue(project, "project-id"));
                    row.addValue(keys.get(TASK_COMMENTS), getValue(project, "comments-count"));
                    row.addValue(keys.get(TASK_POSITION), getValue(project, "position"));
                    row.addValue(keys.get(TASK_ESTIMATED_MINUTES), getValue(project, "estimated-minutes"));
                    row.addValue(keys.get(TASK_DESCRIPTION), getValue(project, "description"));
                    row.addValue(keys.get(TASK_COUNT), 1);
                    row.addValue(keys.get(TASK_CREATED_ON), getDate(project, "created-on"));
                    row.addValue(keys.get(TASK_PROGRESS), getValue(project, "progress"));
                    row.addValue(keys.get(TASK_RESPONSIBLE_PARTY), getValue(project, "responsible-party-summary"));
                    row.addValue(keys.get(TASK_CREATOR), getValue(project, "creator-firstname") + " " + getValue(project, "creator-lastname"));
                    row.addValue(keys.get(TASK_TODO_LIST_ID), getValue(project, "todo-list-id"));
                    row.addValue(keys.get(TASK_PRIORITY), getValue(project, "priority"));
                    row.addValue(keys.get(TASK_STATUS), getValue(project, "status"));
                    row.addValue(keys.get(TASK_DUE_DATE), getDeadlineDate(project, "due-date"));
                    row.addValue(keys.get(TASK_COMPLETED), getValue(project, "completed"));
                    resultCount++;
                }
            } while (resultCount == 250);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TEAMWORK_TASK;
    }
}
