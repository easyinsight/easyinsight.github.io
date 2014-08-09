package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.StringWhere;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 8/7/14
 * Time: 9:24 AM
 */
public class FreshdeskNoteSource extends FreshdeskBaseSource {

    public static final String NOTE_TICKET_ID = "Note Ticket ID";
    public static final String NOTE_AUTHOR = "Note Author";
    public static final String NOTE_AUTHOR_IS_AGENT = "Note Author is Agent";
    public static final String NOTE_CONTENT = "Note Content";
    public static final String NOTE_CREATED_AT = "Note Created At";

    public FreshdeskNoteSource() {
        setFeedName("Notes");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_NOTE;
    }

    @Override
    protected String getUpdateKeyName() {
        return NOTE_TICKET_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(NOTE_TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(NOTE_AUTHOR, new AnalysisDimension());
        fieldBuilder.addField(NOTE_AUTHOR_IS_AGENT, new AnalysisDimension());
        fieldBuilder.addField(NOTE_CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(NOTE_CONTENT, new AnalysisText());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        try {
            FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
            for (Map.Entry<String, List<Map>> entry : freshdeskCompositeSource.getNotes().entrySet()) {
                String ticketID = entry.getKey();
                DataSet dataSet = new DataSet();
                for (Map map : entry.getValue()) {
                    IRow row = dataSet.createRow();
                    Map ticketActivity = (Map) map.get("ticket_activity");
                    Object obj = ticketActivity.get("activity");
                    String activityBody;
                    if (obj instanceof String) {
                        activityBody = obj.toString();
                    } else if (obj instanceof List) {
                        List list = (List) obj;
                        activityBody = list.get(list.size() - 1).toString();
                    } else {
                        throw new RuntimeException();
                    }
                    System.out.println(map);
                    System.out.println(ticketActivity);
                    row.addValue(keys.get(NOTE_CREATED_AT), getDate(ticketActivity, "performed_time"));
                    row.addValue(keys.get(NOTE_CONTENT), getValue(ticketActivity, "note_content"));
                    row.addValue(keys.get(NOTE_TICKET_ID), ticketID);

                    List perfList = (List) ticketActivity.get("performer");
                    if (perfList.size() > 0) {
                        Map performer = (Map) perfList.get(0);
                        row.addValue(keys.get(NOTE_AUTHOR), getJSONValue(performer, "name"));
                        row.addValue(keys.get(NOTE_AUTHOR_IS_AGENT), getJSONValue(performer, "agent"));
                    }
                }
                if (lastRefreshDate == null) {
                    IDataStorage.insertData(dataSet);
                } else {
                    StringWhere userWhere = new StringWhere(keys.get(NOTE_TICKET_ID), ticketID);
                    IDataStorage.updateData(dataSet, Arrays.asList(userWhere));
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
