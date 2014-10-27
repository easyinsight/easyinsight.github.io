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
public class InsightlyProjectSource extends InsightlyBaseSource {

    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_NAME = "Project Name";
    public static final String PROJECT_DETAILS = "Project Details";

    public static final String OPPORTUNITY_ID = "Project Opportunity ID";
    public static final String STARTED_DATE = "Project Started Date";
    public static final String COMPLETED_DATE = "Project Completed Date";
    public static final String RESPONSIBLE_USER = "Project Responsible User";
    public static final String CATEGORY = "Project Category";

    public static final String DATE_CREATED = "Project Date Created";
    public static final String DATE_UPDATED = "Project Date Updated";
    public static final String PROJECT_COUNT = "Project Count";

    public static final String PROJECT_STATUS = "Project Status";
    public static final String PROJECT_CURRENT_STAGE = "Project Current Stage";
    public static final String PROJECT_PIPELINE_NAME = "Project Pipeline Name";

    public InsightlyProjectSource() {
        setFeedName("Projects");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_NAME, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_DETAILS, new AnalysisDimension());
        fieldBuilder.addField(RESPONSIBLE_USER, new AnalysisDimension());
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
        fieldBuilder.addField(CATEGORY, new AnalysisDimension());
        fieldBuilder.addField(DATE_CREATED, new AnalysisDateDimension());
        fieldBuilder.addField(DATE_UPDATED, new AnalysisDateDimension());
        fieldBuilder.addField(STARTED_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(COMPLETED_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(PROJECT_COUNT, new AnalysisMeasure());

        fieldBuilder.addField(PROJECT_STATUS, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_CURRENT_STAGE, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_PIPELINE_NAME, new AnalysisDimension());



        InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
        List customFields = runJSONRequest("customFields", insightlyCompositeSource, httpClient);
        for (Object customFieldObject : customFields) {
            Map customFieldMap = (Map) customFieldObject;
            String fieldFor = customFieldMap.get("FIELD_FOR").toString();
            if ("PROJECT".equals(fieldFor)) {
                String customFieldID = customFieldMap.get("CUSTOM_FIELD_ID").toString();


                String fieldType = customFieldMap.get("FIELD_TYPE").toString();
                if ("DATE".equals(fieldType)) {
                    fieldBuilder.addField(customFieldID, new AnalysisDateDimension(customFieldMap.get("FIELD_NAME").toString()));
                } else {
                    fieldBuilder.addField(customFieldID, new AnalysisDimension(customFieldMap.get("FIELD_NAME").toString()));
                }
            }
        }
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
            Map<String, String> pipelineMap = new HashMap<String, String>();
            List pipelineList = runJSONRequest("Pipelines", insightlyCompositeSource, httpClient);
            for (Object pipelineObject : pipelineList) {
                Map pipelineObj = (Map) pipelineObject;
                String pipelineID = pipelineObj.get("PIPELINE_ID").toString();
                pipelineMap.put(pipelineID, pipelineObj.get("PIPELINE_NAME").toString());
            }
            List pipelineStageList = runJSONRequest("PipelineStages", insightlyCompositeSource, httpClient);
            Map<String, String> pipelineStageMap = new HashMap<String, String>();
            for (Object pipelineObject : pipelineStageList) {
                Map pipelineStageObject = (Map) pipelineObject;
                pipelineStageMap.put(pipelineStageObject.get("STAGE_ID").toString(), pipelineStageObject.get("STAGE_NAME").toString());
            }
            List categoryList = runJSONRequest("projectCategories", insightlyCompositeSource, httpClient);
            Map<String, String> categoryMap = new HashMap<String, String>();
            for (Object categoryObject : categoryList) {
                Map category = (Map) categoryObject;
                categoryMap.put(category.get("CATEGORY_ID").toString(), category.get("CATEGORY_NAME").toString());
            }
            List contactList = runJSONRequest("projects", insightlyCompositeSource, httpClient);
            for (Object contactObj : contactList) {
                IRow row = dataSet.createRow();
                Map contactMap = (Map) contactObj;
                row.addValue(keys.get(PROJECT_ID), contactMap.get("PROJECT_ID").toString());
                row.addValue(keys.get(PROJECT_NAME), contactMap.get("PROJECT_NAME").toString());
                row.addValue(keys.get(PROJECT_STATUS), getValue(contactMap, "PROJECT_STATUS"));
                row.addValue(keys.get(PROJECT_DETAILS), getValue(contactMap, "PROJECT_DETAILS"));
                row.addValue(keys.get(OPPORTUNITY_ID), getValue(contactMap, "OPPORTUNITY_ID"));

                String pipelineStageID = pipelineStageMap.get(getValue(contactMap, "STAGE_ID").toString());
                if (pipelineStageID != null) {
                    row.addValue(keys.get(PROJECT_CURRENT_STAGE), pipelineStageID);
                }
                String pipelineID = pipelineMap.get(getValue(contactMap, "PIPELINE_ID").toString());
                if (pipelineID != null) {
                    row.addValue(keys.get(PROJECT_PIPELINE_NAME), pipelineID);
                }



                String responsibleUser = userMap.get(getValue(contactMap, "RESPONSIBLE_USER_ID").toString());
                if (responsibleUser != null) {
                    row.addValue(keys.get(RESPONSIBLE_USER), responsibleUser);
                }

                String category = categoryMap.get(getValue(contactMap, "CATEGORY_ID").toString());
                if (category != null) {
                    row.addValue(keys.get(CATEGORY), category);
                }
                for (AnalysisItem field : getFields()) {
                    if (field.getKey().toKeyString().startsWith("PROJECT_FIELD")) {
                        if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            Object obj = contactMap.get(field.getKey().toKeyString());
                            if (obj != null) {
                                row.addValue(field.getKey(), sdf.parse(obj.toString()));
                            }
                        } else {
                            row.addValue(field.getKey(), getValue(contactMap, field.getKey().toKeyString()));
                        }
                    }
                }
                row.addValue(keys.get(DATE_CREATED), new DateValue(sdf.parse(contactMap.get("DATE_CREATED_UTC").toString())));
                row.addValue(keys.get(DATE_UPDATED), new DateValue(sdf.parse(contactMap.get("DATE_UPDATED_UTC").toString())));
                Object forecastObj = contactMap.get("STARTED_DATE");
                if (forecastObj != null) {
                    row.addValue(keys.get(STARTED_DATE), new DateValue(sdf.parse(forecastObj.toString())));
                }
                Object actualCloseObj = contactMap.get("COMPLETED_DATE");
                if (actualCloseObj != null) {
                    row.addValue(keys.get(COMPLETED_DATE), new DateValue(sdf.parse(actualCloseObj.toString())));
                }
                row.addValue(keys.get(PROJECT_COUNT), 1);
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_PROJECTS;
    }
}
