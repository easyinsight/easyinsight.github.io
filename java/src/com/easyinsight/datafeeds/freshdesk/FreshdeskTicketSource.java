package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/10/14
 * Time: 11:45 AM
 */
public class FreshdeskTicketSource extends FreshdeskBaseSource {

    public static final String ID = "ID";
    public static final String DESCRIPTION = "Description";
    public static final String REQUESTER_NAME ="Requester";
    public static final String DUE_BY = "Due By";
    public static final String STATUS = "Status";
    public static final String PRIORITY = "Priority";
    public static final String CREATED_AT = "Created At";
    public static final String SOURCE_NAME = "Source Name";
    public static final String TRAINED = "Trained";
    public static final String TICKET_TYPE = "Ticket Type";
    public static final String URGENT = "Urgent";
    public static final String GROUP_ID = "Group ID";
    public static final String REQUESTER_STATUS = "Requester Status";
    public static final String DELETED = "Deleted";
    public static final String ESCALATED = "Escalated";
    public static final String UPDATED_AT = "Updated At";
    public static final String OWNER_ID = "Owner ID";
    public static final String RESPONDER_NAME = "Responder Name";
    public static final String SPAM = "Spam";
    public static final String COUNT = "Ticket Count";

    public FreshdeskTicketSource() {
        setFeedName("Tickets");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, DESCRIPTION, REQUESTER_NAME, DUE_BY, STATUS, PRIORITY, CREATED_AT, SOURCE_NAME, TRAINED, TICKET_TYPE,
                URGENT, GROUP_ID, REQUESTER_STATUS, DELETED, ESCALATED, UPDATED_AT, OWNER_ID, RESPONDER_NAME, SPAM, COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(ID), true));
        items.add(new AnalysisText(keys.get(DESCRIPTION)));
        items.add(new AnalysisDimension(keys.get(STATUS)));
        items.add(new AnalysisDimension(keys.get(PRIORITY)));
        items.add(new AnalysisDimension(keys.get(SOURCE_NAME)));
        items.add(new AnalysisDimension(keys.get(TRAINED)));
        items.add(new AnalysisDimension(keys.get(TICKET_TYPE)));
        items.add(new AnalysisDimension(keys.get(URGENT)));
        items.add(new AnalysisDimension(keys.get(GROUP_ID)));
        items.add(new AnalysisDimension(keys.get(REQUESTER_NAME)));
        items.add(new AnalysisDimension(keys.get(REQUESTER_STATUS)));
        items.add(new AnalysisDimension(keys.get(DELETED)));
        items.add(new AnalysisDimension(keys.get(ESCALATED)));
        items.add(new AnalysisDimension(keys.get(OWNER_ID)));
        items.add(new AnalysisDimension(keys.get(SPAM)));
        items.add(new AnalysisDimension(keys.get(RESPONDER_NAME)));
        items.add(new AnalysisDateDimension(keys.get(DUE_BY), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient client = getHttpClient(freshdeskCompositeSource.getFreshdeskApiKey());
        int ctr;
        int page = 1;
        do {
            ctr = 0;
            List blah;
            if (page == 1) {
                blah = runRestRequestForList("tickets.json?filter_name=all_tickets", client, freshdeskCompositeSource);
            } else {
                blah = runRestRequestForList("tickets.json?filter_name=all_tickets&page=" + page, client, freshdeskCompositeSource);
            }
            for (Object obj : blah) {
                ctr++;
                Map map = (Map) obj;
                String id = map.get("id").toString();
                IRow row = dataSet.createRow();
                createTicket(keys, map, id, row);
            }
            page++;
        } while (ctr == 30);
        return dataSet;
    }

    private void createTicket(Map<String, Key> keys, Map map, String id, IRow row) {
        row.addValue(keys.get(ID), id);
        row.addValue(keys.get(DESCRIPTION), getValue(map, "description"));
        row.addValue(keys.get(REQUESTER_NAME), getValue(map, "requester_name"));
        row.addValue(keys.get(DUE_BY), getDate(map, "due_by"));
        row.addValue(keys.get(STATUS), getValue(map, "status_name"));
        row.addValue(keys.get(PRIORITY), getValue(map, "priority_name"));
        row.addValue(keys.get(CREATED_AT), getDate(map, "created_at"));
        row.addValue(keys.get(UPDATED_AT), getDate(map, "updated_at"));
        row.addValue(keys.get(SOURCE_NAME), getValue(map, "source_name"));
        row.addValue(keys.get(TRAINED), getValue(map, "trained"));
        row.addValue(keys.get(TICKET_TYPE), getValue(map, "ticket_type"));
        row.addValue(keys.get(GROUP_ID), getValue(map, "group_id"));
        row.addValue(keys.get(REQUESTER_STATUS), getValue(map, "requester_status_name"));
        row.addValue(keys.get(DELETED), getValue(map, "deleted"));
        row.addValue(keys.get(OWNER_ID), getValue(map, "owner_id"));
        row.addValue(keys.get(RESPONDER_NAME), getValue(map, "responder_name"));
        row.addValue(keys.get(SPAM), getValue(map, "spam"));
        row.addValue(keys.get(COUNT), 1);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_TICKET;
    }
}
