package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/10/14
 * Time: 11:45 AM
 */
public class FreshdeskTimeEntrySource extends FreshdeskBaseSource {

    public static final String ID = "Time Entry ID";
    public static final String TIME_SPENT = "Time Spent";
    public static final String BILLABLE = "Billable";
    public static final String START_TIME = "Time Entry Start Time";
    public static final String EXECUTED_AT = "Time Entry Executed At";
    public static final String CREATED_AT = "Time Entry Created At";
    public static final String UPDATED_AT = "Time Entry Updated At";
    public static final String WORKABLE_TYPE = "Workable Type";
    public static final String TICKET_ID = "Time Entry Ticket ID";
    public static final String AGENT_NAME = "Time Entry Agent Name";
    public static final String TIME_ENTRY_NOTE = "Time Entry Note";
    public static final String CUSTOMER_NAME = "Time Entry Customer ID";

    public FreshdeskTimeEntrySource() {
        setFeedName("Time");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(BILLABLE, new AnalysisDimension());
        fieldBuilder.addField(AGENT_NAME, new AnalysisDimension());
        fieldBuilder.addField(TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(CUSTOMER_NAME, new AnalysisDimension());
        fieldBuilder.addField(WORKABLE_TYPE, new AnalysisDimension());
        fieldBuilder.addField(TIME_ENTRY_NOTE, new AnalysisDimension());
        fieldBuilder.addField(TIME_SPENT, new AnalysisMeasure());
        fieldBuilder.addField(EXECUTED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(START_TIME, new AnalysisDateDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            HttpClient client = getHttpClient(freshdeskCompositeSource.getFreshdeskApiKey());
            int ctr;
            int page = 1;
            do {
                ctr = 0;
                List<Map> blah;
                if (page == 1) {
                    blah = runRestRequestForList("time_sheets.json", client, freshdeskCompositeSource);
                } else {
                    blah = runRestRequestForList("time_sheets.json?page=" + page, client, freshdeskCompositeSource);
                }
                for (Map map : blah) {
                    ctr++;
                    Map timeEntry = (Map) map.get("time_entry");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(ID), getValue(timeEntry, "id"));
                    row.addValue(keys.get(TIME_SPENT), getValue(timeEntry, "timespent"));
                    row.addValue(keys.get(WORKABLE_TYPE), getValue(timeEntry, "workable_type"));
                    row.addValue(keys.get(AGENT_NAME), getValue(timeEntry, "agent_name"));
                    row.addValue(keys.get(EXECUTED_AT), getDate(timeEntry, "executed_at"));
                    row.addValue(keys.get(CREATED_AT), getDate(timeEntry, "created_at"));
                    row.addValue(keys.get(UPDATED_AT), getDate(timeEntry, "updated_at"));
                    row.addValue(keys.get(START_TIME), getDate(timeEntry, "start_time"));
                    row.addValue(keys.get(CUSTOMER_NAME), getDate(timeEntry, "customer_name"));
                    row.addValue(keys.get(TICKET_ID), getValue(timeEntry, "ticket_id"));
                    row.addValue(keys.get(BILLABLE), getValue(timeEntry, "billable"));
                    row.addValue(keys.get(TIME_ENTRY_NOTE), getValue(timeEntry, "note"));
                }
                page++;
            } while (ctr == 30);
            return dataSet;
        } catch (Exception e) {
            // we can ignore for now because it may not be supported for their freshdesk account
            //LogClass.error(e);
            return new DataSet();
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_TIME;
    }
}
