package com.easyinsight.datafeeds.happyfox;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.StringWhere;
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
    public static final String TICKET_CREATED_BY = "Ticket Created By";
    public static final String TICKET_CREATED_BY_ID = "Ticket Created By ID";

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
        fieldBuilder.addField(TICKET_CREATED_BY, new AnalysisDimension());
        fieldBuilder.addField(TICKET_CREATED_BY_ID, new AnalysisDimension());
    }

    @Override
    protected String getUpdateKeyName() {
        return TICKET_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            //DataSet dataSet = new DataSet();
            HappyFoxCompositeSource happyFoxCompositeSource = (HappyFoxCompositeSource) parentDefinition;
            HttpClient client = getHttpClient(happyFoxCompositeSource.getHfApiKey(), happyFoxCompositeSource.getAuthKey());
            int page = 1;
            if (lastRefreshDate == null) {
                // new refresh
            }
            int pageCount;
            do {
                Map response;
                if (page == 1) {
                    response = runRestRequestForMap("tickets/?size=5000?show_updates=0", client, happyFoxCompositeSource);
                } else {
                    response = runRestRequestForMap("tickets/?size=5000&page=" + page + "?show_updates=0", client, happyFoxCompositeSource);
                }
                Map pageInfo = (Map) response.get("page_info");
                pageCount = (int) pageInfo.get("page_count");
                page++;
                List<Map> data = (List<Map>) response.get("data");
                Set<String> postUpdate = new HashSet<>();
                DataSet fullSet = new DataSet();
                Map<String, DataSet> map = new HashMap<>();
                for (Map ticket : data) {
                    String ticketID = getJSONValue(ticket, "id");
                    IRow row;

                    if (lastRefreshDate == null) {
                        row = fullSet.createRow();
                    } else {
                        DataSet dataSet = new DataSet();
                        row = dataSet.createRow();
                        map.put(ticketID, dataSet);
                    }


                    String categoryName = ((Map) ticket.get("category")).get("name").toString();
                    row.addValue(keys.get(CATEGORY), categoryName);
                    String status = ((Map) ticket.get("status")).get("name").toString();
                    row.addValue(keys.get(STATUS), status);

                    row.addValue(keys.get(TIME_SPENT), getJSONValue(ticket, "time_spent"));
                    row.addValue(keys.get(LAST_REPLY_AT), getDate(ticket, "last_user_reply_at"));
                    Value updatedAtValue = getDate(ticket, "last_updated_at");
                    Date updateDate = ((DateValue) updatedAtValue).getDate();

                    row.addValue(keys.get(UPDATED_AT), updatedAtValue);

                    row.addValue(keys.get(CREATED_AT), getDate(ticket, "created_at"));
                    row.addValue(keys.get(DUE_DATE), getDate(ticket, "due_date"));
                    row.addValue(keys.get(PRIORITY), ((Map) ticket.get("priority")).get("name").toString());
                    Map user = (Map) ticket.get("user");
                    if (user != null) {
                        row.addValue(keys.get(TICKET_CREATED_BY), getValue(user, "name"));
                        row.addValue(keys.get(TICKET_CREATED_BY_ID), getValue(user, "id"));
                    }
                    row.addValue(keys.get(ASSIGNED_TO), getJSONValue(ticket, "assignedTo"));

                    row.addValue(keys.get(TICKET_ID), ticketID);
                    //if (lastRefreshDate != null && lastRefreshDate.before(updateDate)) {
                        postUpdate.add(ticketID);
                    //}

                    row.addValue(keys.get(SUBJECT), getJSONValue(ticket, "subject"));
                    row.addValue(keys.get(TICKET_COUNT), 1);
                }

                if (postUpdate.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String ticketID : postUpdate) {
                        sb.append(ticketID).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    Map details = runRestRequestForMap("tickets/?q=id:" + sb.toString(), client, happyFoxCompositeSource);
                    System.out.println(details);
                    List<Map> detailData = (List<Map>) response.get("data");
                    for (Map ticket : detailData) {
                        List<Map> updates = (List<Map>) ticket.get("updates");
                        for (Map update : updates) {
                            update.get("timestamp");
                            Map message = (Map) update.get("message");
                            update.get("status_change");
                            update.get("assignee_change");
                        }
                    }
                    /*
                    List<Map> updates = (List<Map>) ticket.get("updates");
                    for (Map update : updates) {
                        update.get("timestamp");
                        Map message = (Map) update.get("message");
                        update.get("status_change");
                        update.get("assignee_change");
                    }
                     */
                }

                if (lastRefreshDate == null) {
                    IDataStorage.insertData(fullSet);
                } else {
                    for (Map.Entry<String, DataSet> entry : map.entrySet()) {
                        IDataStorage.updateData(entry.getValue(), Arrays.asList(new StringWhere(keys.get(TICKET_ID), entry.getKey())));
                    }
                }
            } while (page <= pageCount);
            return null;
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
