package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.StringWhere;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 8/4/14
 * Time: 8:36 AM
 */
public class FreshdeskActivitySource extends FreshdeskBaseSource {

    public static final String ACTIVITY_TICKET_ID = "Activity Ticket ID";
    public static final String ACTIVITY_DATE = "Activity Date";
    public static final String ACTIVITY_PERFORMER = "Activity Performer";
    public static final String ACTIVITY_PERFORMER_AGENT = "Activity Performer is Agent";
    public static final String ACTIVITY = "Activity Body";

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ACTIVITY, new AnalysisDimension());
        fieldBuilder.addField(ACTIVITY_PERFORMER, new AnalysisDimension());
        fieldBuilder.addField(ACTIVITY_PERFORMER_AGENT, new AnalysisDimension());
        fieldBuilder.addField(ACTIVITY_TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(ACTIVITY_DATE, new AnalysisDateDimension());
    }

    public FreshdeskActivitySource() {
        setFeedName("Activities");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_ACTIVITY;
    }

    @Override
    protected String getUpdateKeyName() {
        return ACTIVITY_TICKET_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
            List<String> ticketIDs = freshdeskCompositeSource.getTicketIDs();
            HttpClient client = getHttpClient(freshdeskCompositeSource.getFreshdeskApiKey());
            Map<String, List<Map>> statusUpdates = new HashMap<>();
            Map<String, List<Map>> assignmentUpdates = new HashMap<>();
            Map<String, List<Map>> addedNotes = new HashMap<>();
            for (String ticketID : ticketIDs) {
                DataSet dataSet = new DataSet();
                List<Map> activities = runRestRequestForList("tickets/activities/" + ticketID + ".json", client, freshdeskCompositeSource);
                List<Map> statusUpdateList = new LinkedList<>();
                List<Map> assignmentUpdateList = new LinkedList<>();
                List<Map> noteList = new LinkedList<>();
                for (Map activity : activities) {
                    Map ticketActivity = (Map) activity.get("ticket_activity");
                    IRow row = dataSet.createRow();
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
                    if (activityBody.startsWith("updated status to ")) {
                        statusUpdateList.add(activity);
                    } else if (activityBody.startsWith("assigned to ")) {
                        assignmentUpdateList.add(activity);
                    } else if (activityBody.startsWith("Added a note")) {
                        noteList.add(activity);
                    }
                    row.addValue(keys.get(ACTIVITY), activityBody);
                    row.addValue(keys.get(ACTIVITY_DATE), getDate(ticketActivity, "performed_time"));
                    row.addValue(keys.get(ACTIVITY_TICKET_ID), ticketID);
                    List perfList = (List) ticketActivity.get("performer");
                    if (perfList.size() > 0) {
                        Map performer = (Map) perfList.get(0);
                        row.addValue(keys.get(ACTIVITY_PERFORMER), getJSONValue(performer, "name"));
                        row.addValue(keys.get(ACTIVITY_PERFORMER_AGENT), getJSONValue(performer, "agent"));
                    }
                }
                statusUpdates.put(ticketID, statusUpdateList);
                assignmentUpdates.put(ticketID, assignmentUpdateList);
                addedNotes.put(ticketID, noteList);
                if (lastRefreshDate == null) {
                    IDataStorage.insertData(dataSet);
                } else {
                    StringWhere userWhere = new StringWhere(keys.get(ACTIVITY_TICKET_ID), ticketID);
                    IDataStorage.updateData(dataSet, Arrays.asList(userWhere));
                }
            }
            freshdeskCompositeSource.setAssignmentUpdates(assignmentUpdates);
            freshdeskCompositeSource.setStatusUpdates(statusUpdates);
            freshdeskCompositeSource.setNotes(addedNotes);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
