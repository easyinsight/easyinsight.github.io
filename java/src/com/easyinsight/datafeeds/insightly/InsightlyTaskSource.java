package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
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
    public static final String CONTACT_ID = "Task Contact ID";
    public static final String ORGANIZATION_ID = "Task Organization ID";
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

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TASK_ID, new AnalysisDimension());
        fieldBuilder.addField(TITLE, new AnalysisDimension());
        fieldBuilder.addField(CATEGORY, new AnalysisDimension());
        fieldBuilder.addField(COMPLETED, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(DETAILS, new AnalysisDimension());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(PRIORITY, new AnalysisDimension());
        fieldBuilder.addField(ASSIGNED_BY, new AnalysisDimension());
        fieldBuilder.addField(RECURRENCE, new AnalysisDimension());
        fieldBuilder.addField(RESPONSIBLE_USER, new AnalysisDimension());
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
        fieldBuilder.addField(ORGANIZATION_ID, new AnalysisDimension());
        fieldBuilder.addField(CONTACT_ID, new AnalysisDimension());
        fieldBuilder.addField(DATE_CREATED, new AnalysisDateDimension());
        fieldBuilder.addField(DATE_UPDATED, new AnalysisDateDimension());
        fieldBuilder.addField(START_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(DUE_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(TASK_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(PERCENT_COMPLETE, new AnalysisMeasure(FormattingConfiguration.PERCENTAGE));
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
                if (user.get("USER_ID") != null) {
                    String userID = user.get("USER_ID").toString();
                    Object firstNameObj = user.get("FIRST_NAME");
                    Object lastNameObj = user.get("LAST_NAME");
                    String name;
                    if (firstNameObj == null && lastNameObj == null) {
                        name = null;
                    } else if (firstNameObj != null && lastNameObj == null) {
                        name = firstNameObj.toString();
                    } else if (firstNameObj == null && lastNameObj != null) {
                        name = lastNameObj.toString();
                    } else {
                        name = firstNameObj.toString() + " " + lastNameObj.toString();
                    }
                    if (name != null) {
                        userMap.put(userID, name);
                    }

                }
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
                    List<Map> taskLinkList = (List<Map>) taskLinks;
                    String opportunityID = null;
                    String organizationID = null;
                    String contactID = null;
                    for (Map taskLinkMap : taskLinkList) {
                        Object opportunityIDObj = taskLinkMap.get("OPPORTUNITY_ID");
                        if (opportunityID == null && opportunityIDObj != null) {
                            opportunityID = opportunityIDObj.toString();
                        }
                        Object contactIDObj = taskLinkMap.get("CONTACT_ID");
                        if (contactID == null && contactIDObj != null) {
                            contactID = contactIDObj.toString();
                        }
                        Object organizationIDObj = taskLinkMap.get("ORGANISATION_ID");
                        if (organizationID == null && organizationIDObj != null) {
                            organizationID = organizationIDObj.toString();
                        }
                    }
                    row.addValue(keys.get(OPPORTUNITY_ID), opportunityID);
                    row.addValue(keys.get(ORGANIZATION_ID), organizationID);
                    row.addValue(keys.get(CONTACT_ID), contactID);
                }
                row.addValue(keys.get(TITLE), getValue(contactMap, "Title"));
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
