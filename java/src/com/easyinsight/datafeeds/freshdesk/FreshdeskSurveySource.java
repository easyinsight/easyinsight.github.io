package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.StringWhere;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 8/7/14
 * Time: 9:24 AM
 */
public class FreshdeskSurveySource extends FreshdeskBaseSource {

    public static final String SURVEY_TICKET_ID = "Survey Ticket ID";
    public static final String SURVEY_ID = "Survey ID";
    public static final String SURVEY_INTERNAL_ID = "Survey Internal ID";
    public static final String SURVEY_CUSTOMER_ID = "Survey Customer ID";
    public static final String SURVEY_CUSTOMER_NAME = "Survey Customer Name";
    public static final String SURVEY_GROUP_ID = "Survey Group ID";
    public static final String SURVEY_RATING = "Survey Rating";
    public static final String SURVEY_RATING_NAME = "Survey Rating Name";
    public static final String SURVEY_AGENT = "Survey Agent";
    public static final String SURVEY_AGENT_ID = "Survey Agent ID";

    public FreshdeskSurveySource() {
        setFeedName("Surveys");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_SURVEY;
    }

    @Override
    protected String getUpdateKeyName() {
        return SURVEY_TICKET_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(SURVEY_TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_ID, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_INTERNAL_ID, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_CUSTOMER_ID, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_CUSTOMER_NAME, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_GROUP_ID, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_RATING, new AnalysisMeasure());
        fieldBuilder.addField(SURVEY_RATING_NAME, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_AGENT, new AnalysisDimension());
        fieldBuilder.addField(SURVEY_AGENT_ID, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        try {
            FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
            HttpClient client = getHttpClient(freshdeskCompositeSource.getFreshdeskApiKey());

            Map<String, String> agentMap = new HashMap<>();
            List<Map> agents = runRestRequestForListNoHelp("agents.json", client, freshdeskCompositeSource);
            for (Map agent : agents) {
                Map agentObj = (Map) agent.get("agent");
                String contactID = getValue(agentObj, "user_id");
                String name = getValue((Map) agentObj.get("user"), "name");
                agentMap.put(contactID, name);
            }

            /*List<Map> contacts = runRestRequestForListNoHelp("customers.json", client, freshdeskCompositeSource);
            Map<String, String> contactMap = new HashMap<>();
            for (Map contact : contacts) {
                Map contactObj = (Map) contact.get("customer");
                System.out.println(contactObj);
                String contactID = getValue(contactObj, "id");
                String name = getValue(contactObj, "name");
                contactMap.put(contactID, name);
            }*/
            for (Map.Entry<String, List<Map>> entry : freshdeskCompositeSource.getSurveys().entrySet()) {
                String ticketID = entry.getKey();
                DataSet dataSet = new DataSet();
                for (Map map : entry.getValue()) {
                    IRow row = dataSet.createRow();
                    Map surveyResult = (Map) map.get("survey_result");
                    row.addValue(keys.get(SURVEY_TICKET_ID), ticketID);
                    row.addValue(keys.get(SURVEY_INTERNAL_ID), getValue(surveyResult, "survey_id"));
                    row.addValue(keys.get(SURVEY_ID), getValue(surveyResult, "id"));
                    String rating = getValue(surveyResult, "rating");
                    if ("1".equals(rating)) {
                        row.addValue(keys.get(SURVEY_RATING_NAME), "Awesome");
                    } else if ("2".equals(rating)) {
                        row.addValue(keys.get(SURVEY_RATING_NAME), "Just Okay");
                    } else if ("3".equals(rating)) {
                        row.addValue(keys.get(SURVEY_RATING_NAME), "Not Good");
                    }
                    row.addValue(SURVEY_RATING, rating);
                    String customerID = getValue(surveyResult, "customer_id");
                    row.addValue(SURVEY_CUSTOMER_ID, customerID);
                    //row.addValue(SURVEY_CUSTOMER_NAME, contactMap.get(customerID));
                    String agentID = getValue(surveyResult, "agent_id");
                    String agentName = agentMap.get(agentID);
                    row.addValue(keys.get(SURVEY_AGENT), agentName);
                    row.addValue(keys.get(SURVEY_AGENT_ID), agentID);
                }
                if (lastRefreshDate == null) {
                    IDataStorage.insertData(dataSet);
                } else {
                    StringWhere userWhere = new StringWhere(keys.get(SURVEY_TICKET_ID), ticketID);
                    IDataStorage.updateData(dataSet, Arrays.asList(userWhere));
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
