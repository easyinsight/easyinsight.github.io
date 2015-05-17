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
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
        timeToDue.setFormattingType(FormattingConfiguration.MILLISECONDS);
        timeToDue.setKey(new NamedKey("Time to Due"));
        timeToDue.setCalculationString("equalto([Todo Completed], \"Completed\", 0, [Todo Due At] - now())");
        AnalysisCalculation overdueTodoCount = new AnalysisCalculation();
        overdueTodoCount.setKey(new NamedKey("Overdue Todo Count"));
        overdueTodoCount.setCalculationString("notnull([Todo ID], equalto([Todo Completed], \"Completed\", 0, greaterthan([Todo Due At], 0, greaterthanorequal([Todo Due At], now(), 0, 1), 0)))");
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
                try {
                    return altFormat.parseDateTime(string).toDate();
                } catch (Exception e1) {
                    LogClass.error("Parse failure on " + string);
                    return null;
                }
            }
        }
        return null;
    }

    private Date parseDueDate(String string) {
        if (string != null && !"null".equals(string) && !"".equals(string)) {
            return dueAtFormat.parseDateTime(string).toDate();
        }
        return null;
    }

    private static final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final DateTimeFormatter altFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
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
            HttpClient httpClient = new HttpClient();
            BasecampNextCompositeSource basecampNextCompositeSource = (BasecampNextCompositeSource) parentDefinition;
            List<Project> projects = basecampNextCompositeSource.getOrCreateProjectCache().getProjects();

            basecampNextCompositeSource.startTemp(BasecampNextCompositeSource.COMMENTS, callDataID);

            Date baseRefreshDate = lastRefreshDate;

            for (Project project : projects) {
                if (basecampNextCompositeSource.isUseProjectUpdatedAt()) {

                    if (baseRefreshDate != null && callDataID == null) {
                        // repull last data
                        lastRefreshDate = new Date(baseRefreshDate.getTime() - (1000 * 60 * 60 * 24));
                    }

                    if (lastRefreshDate != null && project.getUpdatedAt().before(lastRefreshDate)) {
                        continue;
                    }
                    DataSet dataSet = new DataSet();
                    String projectID = project.getId();
                    List<BasecampComment> comments = new ArrayList<>();

                    int count;
                    int page = 1;
                    do {
                        JSONArray todos = runJSONRequest("projects/" + projectID + "/todos.json?page=" + page, (BasecampNextCompositeSource) parentDefinition, lastRefreshDate, httpClient);
                        count = todos.size();
                        parseTodoList2(keys, dataSet, projectID, todos, basecampNextCompositeSource,
                                project.isArchived(), httpClient, lastRefreshDate, comments);
                        page++;
                    } while (count == 50);

                    basecampNextCompositeSource.tempPersist(BasecampNextCompositeSource.COMMENTS, comments);

                    if (lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                        IDataStorage.insertData(dataSet);
                    } else {
                        StringWhere stringWhere = new StringWhere(keys.get(TODO_LIST_PROJECT_ID), projectID);
                        IDataStorage.updateData(dataSet, Arrays.asList((IWhere) stringWhere));
                    }

                } else {
                    DataSet dataSet = new DataSet();
                    String projectID = project.getId();
                    JSONArray todoListArray = runJSONRequest("projects/"+projectID+"/todolists.json", (BasecampNextCompositeSource) parentDefinition, lastRefreshDate, httpClient);
                    if (todoListArray == null) {

                        continue;
                    }
                    List<BasecampComment> comments = new ArrayList<>();
                    for (int j = 0; j < todoListArray.size(); j++) {

                        JSONObject todoList = (JSONObject) todoListArray.get(j);
                        String todoListID = String.valueOf(todoList.get("id"));
                        String todoListName = getValue(todoList, "name");

                        String todoListDescription = getValue(todoList, "description");
                        String todoListURL = getValue(todoList, "url");
                        Date todoListUpdatedAt = null;
                        try {
                            todoListUpdatedAt = getDate(todoList, "updated_at", format);
                        } catch (Exception e) {
                            try {
                                todoListUpdatedAt = getDate(todoList, "updated_at", altFormat);
                            } catch (Exception e1) {
                                LogClass.error("Parse failure on " + todoList.get("updated_at"));
                            }
                        }
                        JSONObject todoListDetail = runJSONRequestForObject("projects/" + projectID + "/todolists/" + todoListID + ".json", (BasecampNextCompositeSource) parentDefinition, httpClient);
                        JSONObject todoMasterObject = (JSONObject) todoListDetail.get("todos");

                        JSONArray remainingArray = (JSONArray) todoMasterObject.get("remaining");
                        parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, remainingArray, basecampNextCompositeSource,
                                project.isArchived(), httpClient, comments);

                        JSONArray completedArray = (JSONArray) todoMasterObject.get("completed");
                        parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, completedArray, basecampNextCompositeSource,
                                project.isArchived(), httpClient, comments);
                    }
                    JSONArray completedTodoListArray = runJSONRequest("projects/"+projectID+"/todolists/completed.json", (BasecampNextCompositeSource) parentDefinition, httpClient);
                    for (int j = 0; j < completedTodoListArray.size(); j++) {

                        JSONObject todoList = (JSONObject) completedTodoListArray.get(j);
                        String todoListID = String.valueOf(todoList.get("id"));
                        String todoListName = getValue(todoList, "name");

                        String todoListDescription = getValue(todoList, "description");
                        String todoListURL = getValue(todoList, "url");
                        Date todoListUpdatedAt = null;
                        try {
                            todoListUpdatedAt = getDate(todoList, "updated_at", format);
                        } catch (Exception e) {
                            try {
                                todoListUpdatedAt = getDate(todoList, "updated_at", altFormat);
                            } catch (Exception e1) {
                                LogClass.error("Parse failure on " + todoList.get("updated_at"));
                            }
                        }
                        JSONObject todoListDetail = runJSONRequestForObject("projects/" + projectID + "/todolists/" + todoListID + ".json", (BasecampNextCompositeSource) parentDefinition, httpClient);
                        JSONObject todoMasterObject = (JSONObject) todoListDetail.get("todos");

                        JSONArray remainingArray = (JSONArray) todoMasterObject.get("remaining");
                        parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, remainingArray, basecampNextCompositeSource,
                                project.isArchived(), httpClient, comments);

                        JSONArray completedArray = (JSONArray) todoMasterObject.get("completed");
                        parseTodoList(keys, dataSet, projectID, todoListID, todoListName, todoListDescription, todoListURL, todoListUpdatedAt, completedArray, basecampNextCompositeSource,
                                project.isArchived(), httpClient, comments);
                    }

                    basecampNextCompositeSource.tempPersist(BasecampNextCompositeSource.COMMENTS, comments);

                    if (lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                        IDataStorage.insertData(dataSet);
                    } else {
                        StringWhere stringWhere = new StringWhere(keys.get(TODO_LIST_PROJECT_ID), projectID);
                        IDataStorage.updateData(dataSet, Arrays.asList((IWhere) stringWhere));
                    }
                }
            }
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseTodoList2(Map<String, Key> keys, DataSet dataSet, String projectID, JSONArray todoArray,
                               BasecampNextCompositeSource parentSource, boolean projectArchived, HttpClient httpClient, Date lastRefreshDate, List<BasecampComment> bComments) {
        for (int k = 0; k < todoArray.size(); k++) {
            JSONObject todoObject = (JSONObject) todoArray.get(k);
            IRow row = dataSet.createRow();
            String todoID = getValue(todoObject, "id");
            String todoContent = getValue(todoObject, "content");

            String updatedAtString = getValue(todoObject, "updated_at");
            Date updatedAt = parseDate(updatedAtString);

            try {

                Object commentsCountObj = todoObject.get("comments_count");
                if (commentsCountObj != null) {
                    int commentCount = Integer.parseInt(commentsCountObj.toString());
                    if (commentCount > 0) {
                        if (lastRefreshDate == null || updatedAt.after(lastRefreshDate)) {
                            JSONObject detail = runJSONRequestForObject("projects/" + projectID + "/todos/" + todoID + ".json", parentSource, httpClient);
                            JSONArray comments = (JSONArray) detail.get("comments");

                            for (int m = 0; m < comments.size(); m++) {
                                JSONObject comment = (JSONObject) comments.get(m);
                                String createdAtString = getValue(comment, "created_at");
                                String commentID = comment.get("id").toString();
                                String commentCreator = getValue(comment, "content");
                                String creator = ((JSONObject) comment.get("creator")).get("name").toString();
                                Date date = parseDate(createdAtString);
                                bComments.add(new BasecampComment(commentID, date, todoID, creator, commentCreator, projectID));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogClass.error(e);
            }
            String dueAtString = getValue(todoObject, "due_at");
            Date dueAt = parseDueDate(dueAtString);
            String completedAtString = getValue(todoObject, "completed_at");
            Date completedAt = parseDate(completedAtString);
            String createdAtString = getValue(todoObject, "created_at");
            Date createdAt = parseDate(createdAtString);

            Object obj = todoObject.get("assignee");
            String assigneeString = null;
            String completer = null;
            Object obj2 = todoObject.get("completer");
            if (obj2 != null && obj2 instanceof JSONObject) {
                JSONObject completerObject = (JSONObject) obj2;
                completer = getValue(completerObject, "name");
            }
            if (projectArchived) {
                if (completedAt != null) {
                    assigneeString = completer;
                }
            } else {
                if (obj != null && obj instanceof JSONObject) {
                    JSONObject assignee = (JSONObject) obj;
                    assigneeString = getValue(assignee, "name");
                }
            }


            row.addValue(keys.get(TODO_LIST_PROJECT_ID), projectID);

            JSONObject todoList = (JSONObject) todoObject.get("todolist");
            String todoListID = getValue(todoList, "id");
            String todoListName = getValue(todoList, "name");

            String todoListDescription = getValue(todoList, "description");
            String todoListURL = getValue(todoList, "url");
            Date todoListUpdatedAt = null;
            try {
                todoListUpdatedAt = getDate(todoList, "updated_at", format);
            } catch (Exception e) {
                try {
                    todoListUpdatedAt = getDate(todoList, "updated_at", altFormat);
                } catch (Exception e1) {
                    LogClass.error("Parse failure on " + todoList.get("updated_at"));
                }
            }


                row.addValue(keys.get(TODO_LIST_NAME), todoListName);
                row.addValue(keys.get(TODO_LIST_ID), todoListID);

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
            row.addValue(keys.get(TODO_URL), "https://basecamp.com/"+parentSource.getEndpoint()+"/projects/"+projectID+"/todos/"+todoID);
        }
    }

    private void parseTodoList(Map<String, Key> keys, DataSet dataSet, String projectID, String todoListID, String todoListName,
                               String todoListDescription, String todoListURL, Date todoListUpdatedAt, JSONArray todoArray,
                               BasecampNextCompositeSource parentSource, boolean projectArchived, HttpClient httpClient, List<BasecampComment> bComments)  {
        for (int k = 0; k < todoArray.size(); k++) {
            JSONObject todoObject = (JSONObject) todoArray.get(k);
            IRow row = dataSet.createRow();
            String todoID = getValue(todoObject, "id");
            String todoContent = getValue(todoObject, "content");

            try {
                Object commentsCountObj = todoObject.get("comments_count");
                if (commentsCountObj != null) {
                    int commentCount = Integer.parseInt(commentsCountObj.toString());
                    if (commentCount > 0) {
                        JSONObject detail = runJSONRequestForObject("projects/" + projectID + "/todos/" + todoID + ".json", parentSource, httpClient);
                        JSONArray comments = (JSONArray) detail.get("comments");

                        for (int m = 0; m < comments.size(); m++) {
                            JSONObject comment = (JSONObject) comments.get(m);
                            String createdAtString = getValue(comment, "created_at");
                            String commentID = comment.get("id").toString();
                            String commentCreator = getValue(comment, "content");
                            String creator = ((JSONObject) comment.get("creator")).get("name").toString();
                            Date date = parseDate(createdAtString);
                            bComments.add(new BasecampComment(commentID, date, todoID, creator, commentCreator, projectID));
                            //parentSource.tempPersist(BasecampNextCompositeSource.COMMENTS, );
                            //parentSource.addComment(new BasecampComment(commentID, date, todoID, creator, commentCreator, projectID));
                        }
                    }
                }
            } catch (Exception e) {
                LogClass.error(e);
            }
            String dueAtString = getValue(todoObject, "due_at");
            Date dueAt = parseDueDate(dueAtString);
            String completedAtString = getValue(todoObject, "completed_at");
            Date completedAt = parseDate(completedAtString);
            String createdAtString = getValue(todoObject, "created_at");
            Date createdAt = parseDate(createdAtString);
            String updatedAtString = getValue(todoObject, "updated_at");
            Date updatedAt = parseDate(updatedAtString);
            Object obj = todoObject.get("assignee");
            String assigneeString = null;
            String completer = null;
            Object obj2 = todoObject.get("completer");
            if (obj2 != null && obj2 instanceof JSONObject) {
                JSONObject completerObject = (JSONObject) obj2;
                completer = getValue(completerObject, "name");
            }
            if (projectArchived) {
                if (completedAt != null) {
                    assigneeString = completer;
                }
            } else {
                if (obj != null && obj instanceof JSONObject) {
                    JSONObject assignee = (JSONObject) obj;
                    assigneeString = getValue(assignee, "name");
                }
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
            row.addValue(keys.get(TODO_URL), "https://basecamp.com/"+parentSource.getEndpoint()+"/projects/"+projectID+"/todos/"+todoID);
        }
    }
}