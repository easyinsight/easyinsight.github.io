package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:45 PM
 */
public class HighRiseTaskSource extends HighRiseBaseSource {

    // body, category, created-at, done-at, due-at, id, owner-id, author-id, subject-id, subject-type, updated-at, frame, subject-name

    public static final String BODY = "Task Body";
    public static final String CATEGORY = "Task Category";
    public static final String DUE_AT = "Task Due At";
    public static final String DONE_AT = "Task Done At";
    public static final String CREATED_AT = "Task Created At";
    public static final String OWNER = "Task Owner";
    public static final String AUTHOR = "Task Author";
    public static final String CASE_ID = "Task Case ID";
    public static final String COMPANY_ID = "Task Company ID";
    public static final String DEAL_ID = "Task Deal ID";
    public static final String COUNT = "Task Count";
    public static final String TASK_ID = "Task ID";

    public HighRiseTaskSource() {
        setFeedName("Tasks");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(BODY, CATEGORY, DUE_AT, DONE_AT, CREATED_AT, COUNT, OWNER, AUTHOR, CASE_ID, COMPANY_ID, TASK_ID, DEAL_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(BODY), true));
        analysisItems.add(new AnalysisDimension(keys.get(TASK_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CATEGORY), true));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDimension(keys.get(AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(CASE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_ID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(DUE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(DONE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_TASKS;
    }

    private static class TaskInfo {
        private String taskID;
        private String category;
        private String body;
        private String owner;
        private String author;
        private String caseID;
        private String companyID;
        private String dealID;
        private Date createdAt;
        private Date dueAt;
        private Date doneAt;

        private TaskInfo(String taskID, String category, String body, String owner, String author, String caseID, String companyID, String dealID, Date createdAt, Date dueAt, Date doneAt) {
            this.taskID = taskID;
            this.category = category;
            this.body = body;
            this.owner = owner;
            this.author = author;
            this.caseID = caseID;
            this.companyID = companyID;
            this.dealID = dealID;
            this.createdAt = createdAt;
            this.dueAt = dueAt;
            this.doneAt = doneAt;
        }

        public void addToDataSet(DataSet dataSet) {
            IRow row = dataSet.createRow();
            row.addValue(TASK_ID, taskID);
            row.addValue(CATEGORY, category);
            row.addValue(COUNT, 1);
            row.addValue(BODY, body);
            row.addValue(OWNER, owner);
            row.addValue(AUTHOR, author);
            row.addValue(CASE_ID, caseID);
            row.addValue(DEAL_ID, dealID);
            row.addValue(COMPANY_ID, companyID);
            row.addValue(CREATED_AT, new DateValue(createdAt));
            row.addValue(DUE_AT, new DateValue(dueAt));
            row.addValue(DONE_AT, new DateValue(doneAt));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TaskInfo taskInfo = (TaskInfo) o;

            return !(taskID != null ? !taskID.equals(taskInfo.taskID) : taskInfo.taskID != null);

        }

        @Override
        public int hashCode() {
            return taskID != null ? taskID.hashCode() : 0;
        }
    }

    private List<TaskInfo> getTasks(String apiToken, String path, String url, FeedDefinition parentDefinition, Map<String, String> peopleCache,
                                    Map<String, String> categoryCache, DateFormat deadlineFormat) throws HighRiseLoginException, ParsingException, ParseException {
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        HttpClient client = getHttpClient(apiToken, "");
        Builder builder = new Builder();
        Document companies = runRestRequest("/tasks/"+path+".xml", client, builder, url, true, false, parentDefinition);
        Nodes companyNodes = companies.query("/tasks/task");
        for (int i = 0; i < companyNodes.size(); i++) {
            Node taskNode = companyNodes.get(i);


            String id = queryField(taskNode, "id/text()");

            String categoryID = queryField(taskNode, "category-id/text()");
            String category = retrieveCategoryInfo(client, builder, categoryCache, categoryID, url, parentDefinition);
            String authorID = queryField(taskNode, "author-id/text()");
            String author = retrieveUserInfo(client, builder, peopleCache, authorID, url, parentDefinition);
            String ownerID = queryField(taskNode, "owner-id/text()");
            String owner = retrieveUserInfo(client, builder, peopleCache, ownerID, url, parentDefinition);
            Date createdAt = deadlineFormat.parse(queryField(taskNode, "created-at/text()"));
            String doneAtString = queryField(taskNode, "done-at/text()");
            Date doneAt = null;
            if (doneAtString != null) {
                doneAt = deadlineFormat.parse(doneAtString);

            }
            String dueAtString = queryField(taskNode, "due-at/text()");
            Date dueAt = null;
            if (dueAtString != null) {
                dueAt = deadlineFormat.parse(dueAtString);
                if (dueAt == null) {
                    Calendar cal = Calendar.getInstance();
                    String frame = queryField(taskNode, "frame/text()");
                    if ("next_week".equals(frame)) {
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        cal.set(Calendar.DAY_OF_WEEK, 0);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        dueAt = cal.getTime();
                    }
                }
            }

            String body = queryField(taskNode, "body/text()");

            String subjectType = queryField(taskNode, "subject-type/text()");
            String subjectID = queryField(taskNode, "subject-id/text()");
            String caseID = null;
            String companyID = null;
            String dealID = null;
            if ("Kase".equals(subjectType)) {
                caseID = subjectID;
            } else if ("Party".equals(subjectType)) {
                companyID = subjectID;
            } else if ("Deal".equals(subjectType)) {
                dealID = subjectID;
            }

            taskInfos.add(new TaskInfo(id, category, body, owner, author, caseID, companyID, dealID, createdAt, dueAt, doneAt));
        }
        return taskInfos;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        Map<String, String> peopleCache = new HashMap<String, String>();
        Map<String, String> categoryCache = new HashMap<String, String>();
        try {
            Set<TaskInfo> tasks = new HashSet<TaskInfo>();
            loadingProgress(0, 1, "Synchronizing with tasks...", callDataID);
            tasks.addAll(getTasks(token.getTokenValue(), "upcoming", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
            tasks.addAll(getTasks(token.getTokenValue(), "assigned", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
            tasks.addAll(getTasks(token.getTokenValue(), "completed", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
            for (HighriseAdditionalToken additionalToken : highRiseCompositeSource.getAdditionalTokens()) {
                tasks.addAll(getTasks(additionalToken.getToken(), "upcoming", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
                tasks.addAll(getTasks(additionalToken.getToken(), "assigned", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
                tasks.addAll(getTasks(additionalToken.getToken(), "completed", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
            }
            for (TaskInfo task : tasks) {
                task.addToDataSet(ds);
            }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (parentDefinition.isAdjustDates()) {
            ds = adjustDates(ds);
        }
        return ds;
    }

    private String retrieveCategoryInfo(HttpClient client, Builder builder, Map<String, String> categoryCache, String categoryID, String url, FeedDefinition parentDefinition) throws HighRiseLoginException, ParsingException {
        try {
            String contactName = null;
            if(categoryID != null) {
                contactName = categoryCache.get(categoryID);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/task_categories/" + categoryID + ".xml", client, builder, url, false, false, parentDefinition);
                    Nodes dealNodes = contactInfo.query("/task-category");
                    if (dealNodes.size() > 0) {
                        Node deal = dealNodes.get(0);
                        contactName = queryField(deal, "name/text()");
                    }

                    categoryCache.put(categoryID, contactName);
                }

            }
            return contactName;
        } catch (HighRiseLoginException e) {
            categoryCache.put(categoryID, "");
            return "";
        }
    }
}
