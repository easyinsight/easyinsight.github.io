package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 7:24 PM
 */
public class InsightlyTaskSource extends InsightlyBaseSource {

    public static final String TASK_ID = "Task ID";
    public static final String TITLE = "Task Title";
    public static final String CATEGORY = "Task Category";
    public static final String DUE_DATE = "Task Due Date";
    public static final String COMPLETED = "Task Completed";
    public static final String PERCENT_COMPLETE = "Task Percent Complete";
    public static final String PROJECT_ID = "Task Project ID";
    public static final String OPPORTUNITY_ID = "Task Opportunity ID";
    public static final String DETAILS = "Task Details";
    public static final String STATUS = "Task Status";
    public static final String PRIORITY = "Task Priority";
    public static final String START_DATE = "Task Start Date";
    public static final String ASSIGNED_BY = "Task Assigned By";
    public static final String RECURRENCE = "Task Recurrence";
    public static final String RESPONSIBLE_USER = "Task Responsible User";
    public static final String TASK_COUNT = "Task Count";
    public static final String DATE_CREATED = "Task Created On";
    public static final String DATE_UPDATED = "Task Updated On";

    public InsightlyTaskSource() {
        setFeedName("Tasks");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(DATE_CREATED, DATE_UPDATED, TASK_ID, TITLE, CATEGORY, DUE_DATE, COMPLETED, PERCENT_COMPLETE,
                PROJECT_ID, DETAILS, STATUS, PRIORITY, START_DATE, ASSIGNED_BY, RECURRENCE, RESPONSIBLE_USER, TASK_COUNT, OPPORTUNITY_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(TASK_ID)));
        fields.add(new AnalysisDimension(keys.get(TITLE)));
        fields.add(new AnalysisDimension(keys.get(CATEGORY)));
        fields.add(new AnalysisDimension(keys.get(COMPLETED)));
        fields.add(new AnalysisDimension(keys.get(PROJECT_ID)));
        fields.add(new AnalysisDimension(keys.get(DETAILS)));
        fields.add(new AnalysisDimension(keys.get(STATUS)));
        fields.add(new AnalysisDimension(keys.get(PRIORITY)));
        fields.add(new AnalysisDimension(keys.get(ASSIGNED_BY)));
        fields.add(new AnalysisDimension(keys.get(RECURRENCE)));
        fields.add(new AnalysisDimension(keys.get(RESPONSIBLE_USER)));
        Key opportunityIDKey = keys.get(OPPORTUNITY_ID);
        if (opportunityIDKey == null) {
            opportunityIDKey = new NamedKey(OPPORTUNITY_ID);
        }
        fields.add(new AnalysisDimension(opportunityIDKey));
        fields.add(new AnalysisDateDimension(keys.get(DATE_CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(DATE_UPDATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(START_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(DUE_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisMeasure(keys.get(TASK_COUNT), AggregationTypes.SUM));
        fields.add(new AnalysisMeasure(keys.get(PERCENT_COMPLETE), AggregationTypes.SUM));
        return fields;
    }

    private Value getValue(Map map, String param) {
        Object obj = map.get(param);
        if (obj == null) {
            return new EmptyValue();
        } else {
            return new StringValue(obj.toString());
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DataSet dataSet = new DataSet();
            InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
            List userList = runJSONRequest("users", insightlyCompositeSource, httpClient);
            Map<String, String> userMap = new HashMap<String, String>();
            for (Object userObject : userList) {
                Map user = (Map) userObject;
                userMap.put(user.get("USER_ID").toString(), user.get("FIRST_NAME").toString() + " " + user.get("LAST_NAME").toString());
            }
            List categoryList = runJSONRequest("taskCategories", insightlyCompositeSource, httpClient);
            Map<String, String> categoryMap = new HashMap<String, String>();
            for (Object categoryObject : categoryList) {
                Map category = (Map) categoryObject;
                categoryMap.put(category.get("CATEGORY_ID").toString(), category.get("CATEGORY_NAME").toString());
            }
            List contactList = runJSONRequest("tasks", insightlyCompositeSource, httpClient);
            for (Object contactObj : contactList) {
                IRow row = dataSet.createRow();
                Map contactMap = (Map) contactObj;
                row.addValue(keys.get(TASK_ID), contactMap.get("TASK_ID").toString());
                Object taskLinks = contactMap.get("TASKLINKS");
                if (taskLinks != null) {
                    List taskLinkList = (List) taskLinks;
                    if (taskLinkList.size() > 0) {
                        Map taskLinkMap = (Map) taskLinkList.get(0);
                        Object opportunityIDObj = taskLinkMap.get("OPPORTUNITY_ID");
                        if (opportunityIDObj != null) {
                            row.addValue(keys.get(OPPORTUNITY_ID), opportunityIDObj.toString());
                        }
                    }
                }
                row.addValue(keys.get(TITLE), getValue(contactMap, "TITLE"));
                row.addValue(keys.get(PROJECT_ID), getValue(contactMap, "PROJECT_ID"));
                row.addValue(keys.get(COMPLETED), getValue(contactMap, "COMPLETED"));
                row.addValue(keys.get(DETAILS), getValue(contactMap, "DETAILS"));
                row.addValue(keys.get(STATUS), getValue(contactMap, "STATUS"));
                row.addValue(keys.get(PRIORITY), getValue(contactMap, "PRIORITY"));
                row.addValue(keys.get(RECURRENCE), getValue(contactMap, "RECURRENCE"));
                String responsibleUser = userMap.get(getValue(contactMap, "RESPONSIBLE_USER_ID").toString());
                if (responsibleUser != null) {
                    row.addValue(keys.get(RESPONSIBLE_USER), responsibleUser);
                }

                String dealCreator = userMap.get(getValue(contactMap, "ASSIGNED_BY_USER_ID").toString());
                if (dealCreator != null) {
                    row.addValue(keys.get(ASSIGNED_BY), dealCreator);
                }

                String category = categoryMap.get(getValue(contactMap, "CATEGORY_ID").toString());
                if (category != null) {
                    row.addValue(keys.get(CATEGORY), category);
                }
                row.addValue(keys.get(DATE_CREATED), new DateValue(sdf.parse(contactMap.get("DATE_CREATED_UTC").toString())));
                row.addValue(keys.get(DATE_UPDATED), new DateValue(sdf.parse(contactMap.get("DATE_UPDATED_UTC").toString())));
                Object forecastObj = contactMap.get("DUE_DATE");
                if (forecastObj != null) {
                    row.addValue(keys.get(DUE_DATE), new DateValue(sdf.parse(forecastObj.toString())));
                }
                Object actualCloseObj = contactMap.get("START_DATE");
                if (actualCloseObj != null) {
                    row.addValue(keys.get(START_DATE), new DateValue(sdf.parse(actualCloseObj.toString())));
                }
                row.addValue(keys.get(TASK_COUNT), 1);
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_TASKS;
    }
}
