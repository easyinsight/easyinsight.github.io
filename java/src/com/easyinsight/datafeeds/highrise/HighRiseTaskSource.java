package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
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

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
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

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        if (token == null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.HIGHRISE_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        Map<String, String> categoryCache = new HashMap<String, String>();
        try {
           /* EIPageInfo info = new EIPageInfo();
            info.currentPage = 1;
            do {*/
                loadingProgress(0, 1, "Synchronizing with tasks...", true);
                Document companies = runRestRequest("/tasks/upcoming.xml", client, builder, url, true, false);
                Nodes companyNodes = companies.query("/tasks/task");
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node taskNode = companyNodes.get(i);


                    String id = queryField(taskNode, "id/text()");
                    row.addValue(TASK_ID, id);
                    String categoryID = queryField(taskNode, "category-id/text()");
                    row.addValue(CATEGORY, retrieveCategoryInfo(client, builder, categoryCache, categoryID, url));
                    String authorID = queryField(taskNode, "author-id/text()");
                    row.addValue(AUTHOR, retrieveUserInfo(client, builder, peopleCache, authorID, url));
                    String ownerID = queryField(taskNode, "owner-id/text()");
                    row.addValue(OWNER, retrieveUserInfo(client, builder, peopleCache, ownerID, url));
                    Date createdAt = deadlineFormat.parse(queryField(taskNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    String doneAtString = queryField(taskNode, "done-at/text()");
                    if (doneAtString != null) {
                        Date doneAt = deadlineFormat.parse(doneAtString);
                        row.addValue(DUE_AT, new DateValue(doneAt));
                    }
                    String dueAtString = queryField(taskNode, "due-at/text()");
                    if (dueAtString != null) {
                        Date dueAt = deadlineFormat.parse(dueAtString);
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
                        row.addValue(DUE_AT, new DateValue(dueAt));
                    }

                    row.addValue(BODY, queryField(taskNode, "body/text()"));

                    String subjectType = queryField(taskNode, "subject-type/text()");
                    String subjectID = queryField(taskNode, "subject-id/text()");
                    if ("Kase".equals(subjectType)) {
                        row.addValue(CASE_ID, subjectID);
                    } else if ("Party".equals(subjectType)) {
                        row.addValue(COMPANY_ID, subjectID);
                    } else if ("Deal".equals(subjectType)) {
                        row.addValue(DEAL_ID, subjectID);
                    }

                    row.addValue(COUNT, new NumericValue(1));
                }
            companies = runRestRequest("/tasks/assigned.xml", client, builder, url, true, false);
                companyNodes = companies.query("/tasks/task");
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node taskNode = companyNodes.get(i);


                    String id = queryField(taskNode, "id/text()");
                    row.addValue(TASK_ID, id);
                    String categoryID = queryField(taskNode, "category-id/text()");
                    row.addValue(CATEGORY, retrieveCategoryInfo(client, builder, categoryCache, categoryID, url));
                    String authorID = queryField(taskNode, "author-id/text()");
                    row.addValue(AUTHOR, retrieveUserInfo(client, builder, peopleCache, authorID, url));
                    String ownerID = queryField(taskNode, "owner-id/text()");
                    row.addValue(OWNER, retrieveUserInfo(client, builder, peopleCache, ownerID, url));
                    Date createdAt = deadlineFormat.parse(queryField(taskNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    String doneAtString = queryField(taskNode, "done-at/text()");
                    if (doneAtString != null) {
                        Date doneAt = deadlineFormat.parse(doneAtString);
                        row.addValue(DUE_AT, new DateValue(doneAt));
                    }
                    String dueAtString = queryField(taskNode, "due-at/text()");
                    if (dueAtString != null) {
                        Date dueAt = deadlineFormat.parse(dueAtString);
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
                        row.addValue(DUE_AT, new DateValue(dueAt));
                    }

                    row.addValue(BODY, queryField(taskNode, "body/text()"));

                    String subjectType = queryField(taskNode, "subject-type/text()");
                    String subjectID = queryField(taskNode, "subject-id/text()");
                    if ("Kase".equals(subjectType)) {
                        row.addValue(CASE_ID, subjectID);
                    } else if ("Party".equals(subjectType)) {
                        row.addValue(COMPANY_ID, subjectID);
                    } else if ("Deal".equals(subjectType)) {
                        row.addValue(DEAL_ID, subjectID);
                    }

                    row.addValue(COUNT, new NumericValue(1));
                }
            companies = runRestRequest("/tasks/completed.xml", client, builder, url, true, false);
                companyNodes = companies.query("/tasks/task");
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node taskNode = companyNodes.get(i);


                    String id = queryField(taskNode, "id/text()");
                    row.addValue(TASK_ID, id);
                    String categoryID = queryField(taskNode, "category-id/text()");
                    row.addValue(CATEGORY, retrieveCategoryInfo(client, builder, categoryCache, categoryID, url));
                    String authorID = queryField(taskNode, "author-id/text()");
                    row.addValue(AUTHOR, retrieveUserInfo(client, builder, peopleCache, authorID, url));
                    String ownerID = queryField(taskNode, "owner-id/text()");
                    row.addValue(OWNER, retrieveUserInfo(client, builder, peopleCache, ownerID, url));
                    Date createdAt = deadlineFormat.parse(queryField(taskNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    String doneAtString = queryField(taskNode, "done-at/text()");
                    if (doneAtString != null) {
                        Date doneAt = deadlineFormat.parse(doneAtString);
                        row.addValue(DUE_AT, new DateValue(doneAt));
                    }
                    String dueAtString = queryField(taskNode, "due-at/text()");
                    if (dueAtString != null) {
                        Date dueAt = deadlineFormat.parse(dueAtString);
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
                        row.addValue(DUE_AT, new DateValue(dueAt));
                    }

                    row.addValue(BODY, queryField(taskNode, "body/text()"));

                    String subjectType = queryField(taskNode, "subject-type/text()");
                    String subjectID = queryField(taskNode, "subject-id/text()");
                    if ("Kase".equals(subjectType)) {
                        row.addValue(CASE_ID, subjectID);
                    } else if ("Party".equals(subjectType)) {
                        row.addValue(COMPANY_ID, subjectID);
                    } else if ("Deal".equals(subjectType)) {
                        row.addValue(DEAL_ID, subjectID);
                    }

                    row.addValue(COUNT, new NumericValue(1));
                }
            //} while(info.currentPage++ < info.MaxPages);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    private String retrieveCategoryInfo(HttpClient client, Builder builder, Map<String, String> categoryCache, String categoryID, String url) throws HighRiseLoginException, ParsingException {
        try {
            String contactName = null;
            if(categoryID != null) {
                contactName = categoryCache.get(categoryID);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/task_categories/" + categoryID + ".xml", client, builder, url, false, false);
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
