package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
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

    public RedboothTaskSource() {
        setFeedName("Tasks");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
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

        List<Map>  people = (List<Map>) queryList("/api/2/people?count=0", redboothCompositeSource, httpClient);
        List<Map>  userList = (List<Map>) queryList("/api/2/users?count=0", redboothCompositeSource, httpClient);

        Map<String, String> users = new HashMap<String, String>();
        Map<String, String> persons = new HashMap<String, String>();
        for (Map ref : userList) {
            users.put(ref.get("id").toString(), ref.get("first_name").toString() + " " + ref.get("last_name").toString());
        }
        // average # of days by project type - first due date and project creation date
        //
        for (Map ref : people) {
            persons.put(ref.get("id").toString(), ref.get("user_id").toString());
        }

        long endID = 0;
        int count;
        Map<String, Value> idToCompletionDate = new HashMap<String, Value>();

        do {
            count = 0;
            Object obj;
            if (endID == 0) {
                obj = queryList("/api/1/tasks", redboothCompositeSource, httpClient);
            } else {
                obj = queryList("/api/1/tasks?max_id="+endID, redboothCompositeSource, httpClient);
            }
            Map results = (Map) obj;

            List<Map>  v1TaskList = (List<Map>) results.get("objects");

            for (Map org : v1TaskList) {
                count++;
                String id = getJSONValue(org, "id");
                long numID = Long.parseLong(id);
                if (endID == 0) {
                    endID = numID;
                } else {
                    endID = Math.min(numID, endID);
                }
                Value completedAt = getYetAnotherDate(org, "completed_at");
                idToCompletionDate.put(id, completedAt);

            }
        }  while (count == 20);
        endID = 0;
        do {
            count = 0;
            List<Map>  taskList;
            if (endID == 0) {
                taskList = (List<Map>) queryList("/api/2/tasks?count=30&scope=all", redboothCompositeSource, httpClient);
            } else {
                taskList = (List<Map>) queryList("/api/2/tasks?count=30&scope=all&max_id="+endID, redboothCompositeSource, httpClient);
            }

            // resolved_subtasks_count
            // subtasks_count
            //List<Map> organizations = (List<Map>) base.get("objects");
            Set<String> validIDs = redboothCompositeSource.getValidProjects();
            for (Map org : taskList) {

                count++;
                String projectID = getJSONValue(org, "project_id");
                if (!validIDs.contains(projectID)) {
                    continue;
                }
                IRow row = dataSet.createRow();
                String id = getJSONValue(org, "id");
                long numID = Long.parseLong(id);
                if (endID == 0) {
                    endID = numID;
                } else {
                    endID = Math.min(numID, endID);
                }
                row.addValue(keys.get(ID), id);
                row.addValue(keys.get(NAME), getJSONValue(org, "name"));
                row.addValue(keys.get(PROJECT_ID), projectID);
                row.addValue(keys.get(TASK_LIST_ID), getJSONValue(org, "task_list_id"));
                row.addValue(keys.get(POSITION), getJSONValue(org, "position"));
                row.addValue(keys.get(COMMENTS_COUNT), getJSONValue(org, "comments_count"));
                row.addValue(keys.get(URGENT), getJSONValue(org, "urgent"));
                row.addValue(keys.get(TYPE), getJSONValue(org, "type"));
                row.addValue(keys.get(DUE_ON), getAlt(org, "due_on"));
                String statusCode = getJSONValue(org, "status");
                String status = "";
                if ("0".equals(statusCode)) {
                    status = "New";
                } else if ("1".equals(statusCode)) {
                    status = "Open";
                } else if ("2".equals(statusCode)) {
                    status = "Hold";
                } else if ("3".equals(statusCode)) {
                    status = "Resolved";
                } else if ("4".equals(statusCode)) {
                    status = "Rejected";
                }
                String assignedID = getJSONValue(org, "assigned_id");
                if (assignedID != null) {
                    String userID = persons.get(assignedID);
                    if (userID != null) {
                        row.addValue(ASSIGNED_TO, users.get(userID));
                    }
                }
                Value completionDate = idToCompletionDate.get(id);
                row.addValue(TOTAL_SUBTASKS, getJSONValue(org, "subtasks_count"));
                row.addValue(RESOLVED_SUBTASKS, getJSONValue(org, "resolved_subtasks_count"));
                row.addValue(COMPLETED_AT, completionDate);
                row.addValue(keys.get(STATUS), status);
                String url = "https://redbooth.com/a/#!/projects/" + id + "/tasks/" + id;
                row.addValue(TASK_URL, url);
                row.addValue(CREATED_AT, getDate(org, "created_at"));
                row.addValue(UPDATED_AT, getDate(org, "updated_at"));
                row.addValue(COUNT, 1);

            }
        } while (count == 30);
        return dataSet;
    }
}
