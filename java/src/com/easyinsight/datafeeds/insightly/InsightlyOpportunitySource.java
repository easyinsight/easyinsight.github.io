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
public class InsightlyOpportunitySource extends InsightlyBaseSource {

    public static final String OPPORTUNITY_ID = "Opportunity ID";

    public static final String NAME = "Opportunity Name";
    public static final String DETAILS = "Opportunity Details";
    public static final String PROBABILITY = "Probability";
    public static final String BID_CURRENCY = "Bid Currency";
    public static final String BID_AMOUNT = "Bid Amount";
    public static final String BID_TYPE = "Bid Type";
    public static final String BID_DURATION = "Bid Duration";
    public static final String FORECAST_CLOSE_DATE = "Forecast Close Date";
    public static final String ACTUAL_CLOSE_DATE = "Actual Close Date";
    public static final String CATEGORY = "Opportunity Category";
    public static final String PIPELINE = "Opportunity Pipeline";
    public static final String STAGE = "Opportunity Stage";
    public static final String STATE = "Opportunity State";
    public static final String RESPONSIBLE_USER = "Responsible User";
    public static final String RESPONSIBLE_CREATOR = "Opportunity Created By";
    public static final String DATE_CREATED = "Opportunity Date Created";
    public static final String DATE_UPDATED = "Opportunity Date Updated";
    public static final String OPPORTUNITY_COUNT = "Opportunity Count";
    public static final String LINKED_ORGANIZATION = "Linked Organization";
    public static final String LINKED_CONTACT = "Linked Contact";
    public static final String OPPORTUNITY_URL = "Opportunity URL";

    public InsightlyOpportunitySource() {
        setFeedName("Opportunities");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(DETAILS, new AnalysisDimension());
        fieldBuilder.addField(BID_CURRENCY, new AnalysisDimension());
        fieldBuilder.addField(LINKED_CONTACT, new AnalysisDimension());
        fieldBuilder.addField(LINKED_ORGANIZATION, new AnalysisDimension());
        fieldBuilder.addField(OPPORTUNITY_URL, new AnalysisDimension());
        fieldBuilder.addField(BID_TYPE, new AnalysisDimension());
        fieldBuilder.addField(CATEGORY, new AnalysisDimension());
        fieldBuilder.addField(PIPELINE, new AnalysisDimension());
        fieldBuilder.addField(STAGE, new AnalysisDimension());
        fieldBuilder.addField(STATE, new AnalysisDimension());
        fieldBuilder.addField(RESPONSIBLE_USER, new AnalysisDimension());
        fieldBuilder.addField(RESPONSIBLE_CREATOR, new AnalysisDimension());
        fieldBuilder.addField(DATE_CREATED, new AnalysisDateDimension());
        fieldBuilder.addField(DATE_UPDATED, new AnalysisDateDimension());
        fieldBuilder.addField(FORECAST_CLOSE_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(ACTUAL_CLOSE_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(OPPORTUNITY_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(BID_AMOUNT, new AnalysisMeasure(FormattingConfiguration.CURRENCY));
        fieldBuilder.addField(BID_DURATION, new AnalysisMeasure());
        fieldBuilder.addField(PROBABILITY, new AnalysisMeasure(FormattingConfiguration.PERCENTAGE));
        InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
        List customFields = runJSONRequest("customFields", insightlyCompositeSource, httpClient);
        for (Object customFieldObject : customFields) {
            Map customFieldMap = (Map) customFieldObject;
            String fieldFor = customFieldMap.get("FIELD_FOR").toString();
            if ("OPPORTUNITY".equals(fieldFor)) {
                String customFieldID = customFieldMap.get("CUSTOM_FIELD_ID").toString();
                String fieldType = customFieldMap.get("FIELD_TYPE").toString();
                if ("DATE".equals(fieldType)) {
                    AnalysisDateDimension date = new AnalysisDateDimension(customFieldMap.get("FIELD_NAME").toString());
                    date.setDateOnlyField(true);
                    fieldBuilder.addField(customFieldID, date);
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
            Map<String, List<InsightlyLink>> linkedOrgMap = new HashMap<String, List<InsightlyLink>>();
            Map<String, List<InsightlyLink>> linkedContactMap = new HashMap<String, List<InsightlyLink>>();
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
            List categoryList = runJSONRequest("opportunityCategories", insightlyCompositeSource, httpClient);
            Map<String, String> categoryMap = new HashMap<String, String>();
            for (Object categoryObject : categoryList) {
                Map category = (Map) categoryObject;
                categoryMap.put(category.get("CATEGORY_ID").toString(), category.get("CATEGORY_NAME").toString());
            }
            List contactList = runJSONRequest("opportunities", insightlyCompositeSource, httpClient);
            for (Object contactObj : contactList) {
                IRow row = dataSet.createRow();
                Map contactMap = (Map) contactObj;
                row.addValue(keys.get(OPPORTUNITY_ID), contactMap.get("OPPORTUNITY_ID").toString());
                row.addValue(keys.get(NAME), getValue(contactMap, "OPPORTUNITY_NAME"));
                if (contactMap.get("LINKS") != null) {
                    List<Map> links = (List<Map>) contactMap.get("LINKS");
                    if (links.size() > 0) {
                        Value linkedOrganizationID = null;
                        Value linkedContactID = null;
                        for (Map linkMap : links) {
                            if (linkedOrganizationID == null) {
                                linkedOrganizationID = getValue(linkMap, "ORGANISATION_ID");
                            }
                            if (getValue(linkMap, "ORGANISATION_ID").type() != Value.EMPTY) {
                                List<InsightlyLink> insightlyLinks = linkedOrgMap.get(contactMap.get("OPPORTUNITY_ID").toString());
                                if (insightlyLinks == null) {
                                    insightlyLinks = new ArrayList<InsightlyLink>();
                                    linkedOrgMap.put(contactMap.get("OPPORTUNITY_ID").toString(), insightlyLinks);
                                }
                                Value roleValue = getValue(linkMap, "ROLE");
                                insightlyLinks.add(new InsightlyLink(roleValue.type() == Value.EMPTY ? "" : roleValue.toString(), getValue(linkMap, "ORGANISATION_ID").toString()));
                            }
                            if (linkedContactID == null) {
                                linkedContactID = getValue(linkMap, "CONTACT_ID");
                            }
                            if (getValue(linkMap, "CONTACT_ID").type() != Value.EMPTY) {
                                List<InsightlyLink> insightlyLinks = linkedContactMap.get(contactMap.get("OPPORTUNITY_ID").toString());
                                if (insightlyLinks == null) {
                                    insightlyLinks = new ArrayList<InsightlyLink>();
                                    linkedContactMap.put(contactMap.get("OPPORTUNITY_ID").toString(), insightlyLinks);
                                }
                                Value roleValue = getValue(linkMap, "ROLE");
                                insightlyLinks.add(new InsightlyLink(roleValue.type() == Value.EMPTY ? "" : roleValue.toString(), getValue(linkMap, "CONTACT_ID").toString()));
                            }
                        }
                        row.addValue(keys.get(LINKED_ORGANIZATION), linkedOrganizationID);
                        row.addValue(keys.get(LINKED_CONTACT), linkedContactID);
                    }
                }
                row.addValue(keys.get(DETAILS), getValue(contactMap, "OPPORTUNITY_DETAILS"));
                row.addValue(keys.get(BID_TYPE), getValue(contactMap, "BID_TYPE"));
                Value opportunityState = getValue(contactMap, "OPPORTUNITY_STATE");
                if ("LOST".equals(opportunityState.toString())) {
                    opportunityState = new StringValue("Lost");
                } else if ("OPEN".equals(opportunityState.toString())) {
                    opportunityState = new StringValue("Open");
                } else if ("WON".equals(opportunityState.toString())) {
                    opportunityState = new StringValue("Won");
                }
                row.addValue(keys.get(STATE), opportunityState);
                row.addValue(keys.get(OPPORTUNITY_URL), "https://googleapps.insight.ly/opportunities/details/" + contactMap.get("OPPORTUNITY_ID").toString());
                row.addValue(keys.get(PROBABILITY), getValue(contactMap, "PROBABILITY"));
                row.addValue(keys.get(BID_CURRENCY), getValue(contactMap, "BID_CURRENCY"));
                row.addValue(keys.get(BID_AMOUNT), getValue(contactMap, "BID_AMOUNT"));
                row.addValue(keys.get(BID_DURATION), getValue(contactMap, "BID_DURATION"));

                String responsibleUser = userMap.get(getValue(contactMap, "RESPONSIBLE_USER_ID").toString());
                if (responsibleUser != null) {
                    row.addValue(keys.get(RESPONSIBLE_USER), responsibleUser);
                }

                String dealCreator = userMap.get(getValue(contactMap, "OWNER_USER_ID").toString());
                if (dealCreator != null) {
                    row.addValue(keys.get(RESPONSIBLE_CREATOR), dealCreator);
                }

                String category = categoryMap.get(getValue(contactMap, "CATEGORY_ID").toString());
                if (category != null) {
                    row.addValue(keys.get(CATEGORY), category);
                }
                String pipelineStageID = pipelineStageMap.get(getValue(contactMap, "STAGE_ID").toString());
                if (pipelineStageID != null) {
                    row.addValue(keys.get(STAGE), pipelineStageID);
                }
                String pipelineID = pipelineMap.get(getValue(contactMap, "PIPELINE_ID").toString());
                if (pipelineID != null) {
                    row.addValue(keys.get(PIPELINE), pipelineID);
                }
                for (AnalysisItem field : getFields()) {
                    if (field.getKey().toKeyString().startsWith("OPPORTUNITY_FIELD")) {
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
                Object forecastObj = contactMap.get("FORECAST_CLOSE_DATE");
                if (forecastObj != null) {
                    row.addValue(keys.get(FORECAST_CLOSE_DATE), new DateValue(sdf.parse(forecastObj.toString())));
                }
                /*Object actualCloseObj = contactMap.get("ACTUAL_CLOSE_DATE");
                System.out.println(actualCloseObj);
                if (actualCloseObj != null) {*/
                if ("Won".equals(opportunityState.toString())) {
                    row.addValue(keys.get(ACTUAL_CLOSE_DATE), new DateValue(sdf.parse(contactMap.get("DATE_UPDATED_UTC").toString())));
                } else {
                    //row.addValue(keys.get(ACTUAL_CLOSE_DATE), new DateValue(sdf.parse(actualCloseObj.toString())));
                }
                //}
                row.addValue(keys.get(OPPORTUNITY_COUNT), 1);
            }
            insightlyCompositeSource.setLinkedContactMap(linkedContactMap);
            insightlyCompositeSource.setLinkedOrgMap(linkedOrgMap);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_OPPORTUNITIES;
    }
}
