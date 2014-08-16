package com.easyinsight.datafeeds.happyfox;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 8/15/14
 * Time: 11:27 AM
 */
public class HappyFoxTicketSource extends HappyFoxBaseSource {

    public static final String TICKET_ID = "Ticket ID";
    public static final String TICKET_COUNT = "Ticket Count";
    public static final String CREATED_AT = "Ticket Created At";
    public static final String UPDATED_AT = "Ticket Updated At";
    public static final String LAST_REPLY_AT = "Last Reply At";
    public static final String ASSIGNED_TO = "Assigned To";
    public static final String DUE_DATE = "Due Date";
    public static final String STATUS = "Status";
    public static final String PRIORITY = "Priority";
    public static final String CATEGORY = "Category";
    public static final String TIME_SPENT = "Time Spent";
    public static final String SUBJECT = "Subject";

    public HappyFoxTicketSource() {
        setFeedName("Tickets");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(ASSIGNED_TO, new AnalysisDimension());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(PRIORITY, new AnalysisDimension());
        fieldBuilder.addField(CATEGORY, new AnalysisDimension());
        fieldBuilder.addField(SUBJECT, new AnalysisDimension());
        fieldBuilder.addField(DUE_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(LAST_REPLY_AT, new AnalysisDateDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(TICKET_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(TIME_SPENT, new AnalysisMeasure());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            HappyFoxCompositeSource happyFoxCompositeSource = (HappyFoxCompositeSource) parentDefinition;
            HttpClient client = getHttpClient(happyFoxCompositeSource.getHfApiKey(), happyFoxCompositeSource.getAuthKey());
            int page = 1;
            int pageCount;
            do {
                Map response;
                if (page == 1) {
                    response = runRestRequestForMap("tickets/?size=50", client, happyFoxCompositeSource);
                } else {
                    response = runRestRequestForMap("tickets/?size=50&page=" + page, client, happyFoxCompositeSource);
                }
                Map pageInfo = (Map) response.get("page_info");
                pageCount = (int) pageInfo.get("page_count");
                page++;
                List<Map> data = (List<Map>) response.get("data");
                for (Map ticket : data) {
                    IRow row = dataSet.createRow();
                    String categoryName = ((Map) ticket.get("category")).get("name").toString();
                    row.addValue(keys.get(CATEGORY), categoryName);
                    String status = ((Map) ticket.get("status")).get("name").toString();
                    row.addValue(keys.get(STATUS), status);
                    List<Map> updates = (List<Map>) ticket.get("updates");
                    for (Map update : updates) {
                        update.get("timestamp");
                        Map message = (Map) update.get("message");
                        update.get("status_change");
                        update.get("assignee_change");
                    }
                    row.addValue(keys.get(TIME_SPENT), getJSONValue(ticket, "time_spent"));
                    row.addValue(keys.get(LAST_REPLY_AT), getDate(ticket, "last_user_reply_at"));
                    row.addValue(keys.get(UPDATED_AT), getDate(ticket, "last_updated_at"));
                    row.addValue(keys.get(CREATED_AT), getDate(ticket, "created_at"));
                    row.addValue(keys.get(DUE_DATE), getDate(ticket, "due_date"));
                    row.addValue(keys.get(PRIORITY), ((Map) ticket.get("priority")).get("name").toString());
                    //row.addValue(keys.get(), ((Map) ticket.get("user")).get("name").toString());
                    row.addValue(keys.get(ASSIGNED_TO), getJSONValue(ticket, "assignedTo"));
                    row.addValue(keys.get(TICKET_ID), getJSONValue(ticket, "id"));
                    row.addValue(keys.get(SUBJECT), getJSONValue(ticket, "subject"));
                    row.addValue(keys.get(TICKET_COUNT), 1);


                }
            } while (page <= pageCount);
            return dataSet;
        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HAPPYFOX_TICKET;
    }
}
