package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/26/12
 * Time: 11:06 AM
 */
public class BasecampNextTodoSource extends BasecampNextBaseSource {
    
    public static final String TODO_LIST_NAME = "Todo List Name";
    public static final String TODO_LIST_PROJECT_ID = "Todo List Project ID";
    public static final String TODO_LIST_DESCRIPTION = "Todo List Description";
    public static final String TODO_LIST_ID = "Todo List ID";
    public static final String TODO_LIST_URL = "Todo List URL";
    public static final String TODO_LIST_UPDATED_AT = "Todo List Updated At";
    public static final String TODO_NAME = "Todo Name";
    public static final String TODO_ID = "Todo ID";
    public static final String TODO_CREATED_AT = "Todo Created At";
    public static final String TODO_UPDATED_AT = "Todo Updated At";
    public static final String TODO_COMPLETED = "Todo Completed";
    public static final String TODO_COMPLETED_AT = "Todo Completed At";
    public static final String TODO_ASSIGNEE = "Todo Assignee";
    public static final String TODO_ASSIGNEE_ID = "Todo Assignee ID";
    public static final String TODO_COMPLETER = "Todo Completer";
    public static final String TODO_COMPLETER_ID = "Todo Completer ID";
    public static final String TODO_URL = "Todo URL";
    public static final String TODO_DUE_AT = "Todo Due At";
    public static final String TODO_COUNT = "Todo Count";

    public BasecampNextTodoSource() {
        setFeedName("Todos");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_NEXT_TODOS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(TODO_LIST_NAME, TODO_LIST_DESCRIPTION, TODO_LIST_ID, TODO_LIST_URL, TODO_LIST_UPDATED_AT,
                TODO_NAME, TODO_ID, TODO_CREATED_AT, TODO_UPDATED_AT, TODO_COMPLETED_AT, TODO_ASSIGNEE,
                TODO_COMPLETER, TODO_URL, TODO_DUE_AT, TODO_COUNT, TODO_LIST_PROJECT_ID, TODO_COMPLETER_ID, TODO_ASSIGNEE_ID,
                TODO_COMPLETED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(TODO_LIST_NAME)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_LIST_DESCRIPTION)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_LIST_PROJECT_ID)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_LIST_ID)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_LIST_URL)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_NAME)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_ID)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_ASSIGNEE)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_ASSIGNEE_ID)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_COMPLETER)));
        AnalysisDimension completed = new AnalysisDimension(keys.get(TODO_COMPLETED));
        analysisitems.add(completed);
        analysisitems.add(new AnalysisDimension(keys.get(TODO_COMPLETER_ID)));
        analysisitems.add(new AnalysisDimension(keys.get(TODO_URL)));
        analysisitems.add(new AnalysisMeasure(TODO_COUNT, AggregationTypes.SUM));
        AnalysisCalculation timeToDue = new AnalysisCalculation();
        timeToDue.getFormattingConfiguration().setFormattingType(FormattingConfiguration.MILLISECONDS);
        timeToDue.setKey(new NamedKey("Time to Due"));
        timeToDue.setCalculationString("equalto([Todo Completed], \"Completed\", 0, [Todo Due At] - now())");
        AnalysisCalculation overdueTodoCount = new AnalysisCalculation();
        overdueTodoCount.setKey(new NamedKey("Overdue Todo Count"));
        overdueTodoCount.setCalculationString("equalto([Todo Completed], \"Completed\", 0, greaterthan([Todo Due At], 0, greaterthan([Todo Due At], now(), 0, 1), 0))");
        analysisitems.add(timeToDue);
        analysisitems.add(overdueTodoCount);
        analysisitems.add(new AnalysisDateDimension(keys.get(TODO_LIST_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(TODO_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(TODO_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(TODO_COMPLETED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(TODO_DUE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }
    
    private Date parseDate(String string) {
        if (string != null && !"null".equals(string) && !"".equals(string)) {
            try {
                return format.parseDateTime(string).toDate();
            } catch (Exception e) {
                LogClass.error("Parse failure on " + string);
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(format.parseDateTime("2012-05-01T10:32:45Z"));
    }

    private Date parseDueDate(String string) {
        if (string != null && !"null".equals(string) && !"".equals(string)) {
            return dueAtFormat.parseDateTime(string).toDate();
        }
        return null;
    }
    
    private static final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final DateTimeFormatter dueAtFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    protected String getUpdateKeyName() {
        return TODO_LIST_PROJECT_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            

            JSONArray jsonArray = runJSONRequest("projects.json", (BasecampNextCompositeSource) parentDefinition);
            for (int i = 0; i < jsonArray.length(); i++) {
                DataSet dataSet = new DataSet();
                JSONObject projectObject = jsonArray.getJSONObject(i);
                String projectID = String.valueOf(projectObject.getInt("id"));
                JSONArray todoListArray = runJSONRequest("projects/"+projectID+"/todolists.json", (BasecampNextCompositeSource) parentDefinition, lastRefreshDate);
                if (todoListArray == null) {
                    System.out.println("No need to retrieve todos for " + projectID);
                    continue;
                }
                System.out.println("Retrieving todos for " + projectID);
                for (int j = 0; j < todoListArray.length(); j++) {

                    JSONObject todoList = todoListArray.getJSONObject(j);
                    String todoListID = String.valueOf(todoList.getInt("id"));
                    String todoListName = todoList.getString("name");

                    String todoListDescription = todoList.getString("description");
                    String todoListURL = todoList.getString("url");
                    Date todoListUpdatedAt = null;
                    try {
                        todoListUpdatedAt = format.parseDateTime(todoList.getString("updated_at")).toDate();
                    } catch (Exception e) {
                        LogClass.error("Parse failure on " + todoList.getString("updated_at"));
                    }
                    JSONObject todoListDetail = runJSONRequestForObject("projects/" + projectID + "/todolists/" + todoListID + ".json", (BasecampNextCompositeSource) parentDefinition);
                    JSONObject todoMasterObject = todoListDetail.getJSONObject("todos");

                    JSONArray remainingArray = todoMasterObject.getJSONArray("remaining");
                    parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, remainingArray);

                    JSONArray completedArray = todoMasterObject.getJSONArray("completed");
                    parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, completedArray);
                }
                JSONArray completedTodoListArray = runJSONRequest("projects/"+projectID+"/todolists/completed.json", (BasecampNextCompositeSource) parentDefinition);
                for (int j = 0; j < completedTodoListArray.length(); j++) {

                    JSONObject todoList = completedTodoListArray.getJSONObject(j);
                    String todoListID = String.valueOf(todoList.getInt("id"));
                    String todoListName = todoList.getString("name");

                    String todoListDescription = todoList.getString("description");
                    String todoListURL = todoList.getString("url");
                    Date todoListUpdatedAt = null;
                    try {
                        todoListUpdatedAt = format.parseDateTime(todoList.getString("updated_at")).toDate();
                    } catch (Exception e) {
                        LogClass.error("Parse failure on " + todoList.getString("updated_at"));
                    }
                    JSONObject todoListDetail = runJSONRequestForObject("projects/" + projectID + "/todolists/" + todoListID + ".json", (BasecampNextCompositeSource) parentDefinition);
                    JSONObject todoMasterObject = todoListDetail.getJSONObject("todos");

                    JSONArray remainingArray = todoMasterObject.getJSONArray("remaining");
                    parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, remainingArray);

                    JSONArray completedArray = todoMasterObject.getJSONArray("completed");
                    parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, completedArray);
                }
                if (lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                    IDataStorage.insertData(dataSet);
                } else {
                    StringWhere stringWhere = new StringWhere(keys.get(TODO_LIST_PROJECT_ID), projectID);
                    IDataStorage.updateData(dataSet, Arrays.asList((IWhere) stringWhere));
                }
            }
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseTodoList(Map<String, Key> keys, DataSet dataSet, String projectID, String todoListID, String todoListName, String todoListDescription, String todoListURL, Date todoListUpdatedAt, JSONArray todoArray) throws JSONException {
        for (int k = 0; k < todoArray.length(); k++) {
            JSONObject todoObject = todoArray.getJSONObject(k);
            IRow row = dataSet.createRow();
            String todoID = String.valueOf(todoObject.getInt("id"));
            String todoContent = todoObject.getString("content");
            String dueAtString = todoObject.getString("due_at");
            Date dueAt = parseDueDate(dueAtString);
            String completedAtString = todoObject.getString("completed_at");
            Date completedAt = parseDate(completedAtString);
            String createdAtString = todoObject.getString("created_at");
            Date createdAt = parseDate(createdAtString);
            String updatedAtString = todoObject.getString("updated_at");
            Date updatedAt = parseDate(updatedAtString);
            Object obj = todoObject.get("assignee");
            String assigneeString = null;
            if (obj != null && obj instanceof JSONObject) {
                JSONObject assignee = (JSONObject) obj;
                assigneeString = assignee.getString("name");
            }
            String completer = null;
            Object obj2 = todoObject.get("completer");
            if (obj2 != null && obj2 instanceof JSONObject) {
                JSONObject completerObject = (JSONObject) obj2;
                completer = completerObject.getString("name");
            }
            row.addValue(keys.get(TODO_LIST_NAME), todoListName);
            row.addValue(keys.get(TODO_LIST_ID), todoListID);
            row.addValue(keys.get(TODO_LIST_PROJECT_ID), projectID);
            row.addValue(keys.get(TODO_LIST_DESCRIPTION), todoListDescription);
            row.addValue(keys.get(TODO_LIST_UPDATED_AT), todoListUpdatedAt);
            row.addValue(keys.get(TODO_LIST_URL), todoListURL);
            row.addValue(keys.get(TODO_ASSIGNEE), assigneeString);
            row.addValue(keys.get(TODO_COMPLETER), completer);
            row.addValue(keys.get(TODO_COUNT), 1);
            row.addValue(keys.get(TODO_COMPLETED_AT), completedAt);
            row.addValue(keys.get(TODO_CREATED_AT), createdAt);
            row.addValue(keys.get(TODO_UPDATED_AT), updatedAt);
            row.addValue(keys.get(TODO_DUE_AT), dueAt);
            row.addValue(keys.get(TODO_ID), todoID);
            row.addValue(keys.get(TODO_NAME), todoContent);
            row.addValue(keys.get(TODO_COMPLETED), completedAt != null ? "Completed" : "Not Completed");
        }
    }
}
