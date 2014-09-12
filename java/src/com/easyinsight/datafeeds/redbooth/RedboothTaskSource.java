package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.*;
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
 * Date: 2/19/14
 * Time: 7:09 PM
 */
public class RedboothTaskSource extends RedboothBaseSource {
    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String PROJECT_ID = "Project ID";
    public static final String TASK_LIST_ID = "Task List ID";
    public static final String STATUS = "Status";
    public static final String TYPE = "Type";
    public static final String ASSIGNED_TO = "Assignee";
    public static final String CREATED_AT = "Created At";
    public static final String POSITION = "Position";
    public static final String COMMENTS_COUNT = "Comment Count";
    public static final String URGENT = "Urgent";
    public static final String DUE_ON = "Due On";
    public static final String UPDATED_AT = "Updated At";
    public static final String COMPLETED_AT = "Completed At";
    public static final String COUNT = "Task Count";
    public static final String TOTAL_SUBTASKS = "Total Subtasks";
    public static final String RESOLVED_SUBTASKS = "Resolved Subtasks";
    public static final String TASK_URL = "Task URL";
    public static final String DESCRIPTION = "Task Description";

    public RedboothTaskSource() {
        setFeedName("Tasks");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(TASK_LIST_ID, new AnalysisDimension());
        fieldBuilder.addField(POSITION, new AnalysisDimension());
        fieldBuilder.addField(URGENT, new AnalysisDimension());
        fieldBuilder.addField(TYPE, new AnalysisDimension());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(ASSIGNED_TO, new AnalysisDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(COMPLETED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(DUE_ON, new AnalysisDateDimension(true));
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
        fieldBuilder.addField(COMMENTS_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(TOTAL_SUBTASKS, new AnalysisMeasure());
        fieldBuilder.addField(RESOLVED_SUBTASKS, new AnalysisMeasure());
        fieldBuilder.addField(TASK_URL, new AnalysisDimension());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_TASK;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        RedboothCompositeSource redboothCompositeSource = (RedboothCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(redboothCompositeSource);

        List<Map>  people = (List<Map>) queryList("/api/3/users", redboothCompositeSource, httpClient);
        List<Map>  personList = (List<Map>) queryList("/api/3/people", redboothCompositeSource, httpClient);

        Map<String, String> users = new HashMap<>();

        Map<String, String> persons = new HashMap<>();
        for (Map ref : people) {
            users.put(ref.get("id").toString(), ref.get("first_name").toString() + " " + ref.get("last_name").toString());
        }
        for (Map ref : personList) {
            persons.put(ref.get("id").toString(), ref.get("user_id").toString());
        }

        int count;
        int page = 1;
        do {
            count = 0;
            String debug = "/api/3/tasks?page="+page+"&per_page=1000";
            List<Map> taskList = (List<Map>) queryList("/api/3/tasks?page="+page+"&per_page=1000", redboothCompositeSource, httpClient);
            Set<String> validIDs = redboothCompositeSource.getValidProjects();
            for (Map org : taskList) {
                count++;
                String projectID = getJSONValue(org, "project_id");
                if (!validIDs.contains(projectID)) {
                    continue;
                }
                IRow row = dataSet.createRow();
                String id = getJSONValue(org, "id");
                row.addValue(keys.get(ID), id);
                row.addValue(keys.get(NAME), getJSONValue(org, "name"));
                //row.addValue(keys.get(NAME), getJSONValue(org, "name"));
                row.addValue(keys.get(PROJECT_ID), projectID);
                row.addValue(keys.get(TASK_LIST_ID), getJSONValue(org, "task_list_id"));
                row.addValue(keys.get(POSITION), getJSONValue(org, "position"));
                row.addValue(keys.get(COMMENTS_COUNT), getJSONValue(org, "comments_count"));
                row.addValue(keys.get(URGENT), getJSONValue(org, "urgent"));
                row.addValue(keys.get(TYPE), getJSONValue(org, "type"));
                row.addValue(keys.get(DUE_ON), getAlt(org, "due_on"));
                String status = getJSONValue(org, "status");
                if (status != null && status.length() > 0) {
                    status = Character.toUpperCase(status.charAt(0)) + status.substring(1);
                }
                String assignedID = getJSONValue(org, "assigned_id");
                if (assignedID != null) {
                    String userID = persons.get(assignedID);
                    if (userID != null) {
                        row.addValue(ASSIGNED_TO, users.get(userID));
                    }
                }
                row.addValue(TOTAL_SUBTASKS, getJSONValue(org, "subtasks_count"));
                row.addValue(DESCRIPTION, getJSONValue(org, "description"));
                row.addValue(RESOLVED_SUBTASKS, getJSONValue(org, "resolved_subtasks_count"));
                row.addValue(keys.get(STATUS), status);
                String url = "https://redbooth.com/a/#!/projects/" + projectID + "/tasks/" + id;
                row.addValue(TASK_URL, url);
                row.addValue(CREATED_AT, getDateFromLong(org, "created_at"));
                row.addValue(UPDATED_AT, getDateFromLong(org, "updated_at"));
                if ("Resolved".equals(status)) {
                    row.addValue(COMPLETED_AT, getDateFromLong(org, "updated_at"));
                }
                row.addValue(COUNT, 1);

            }
            page++;
        } while (count == 1000);
        return dataSet;
    }
}
