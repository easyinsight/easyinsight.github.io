package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
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
public class FreshdeskStatusSource extends FreshdeskBaseSource {

    public static final String STATUS_TICKET_ID = "Status Ticket ID";
    public static final String STATUS_CHANGED_TO = "Status Changed To";
    public static final String STATUS_CHANGED_AT = "Status Changed At";

    public FreshdeskStatusSource() {
        setFeedName("Status History");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_STATUS;
    }

    @Override
    protected String getUpdateKeyName() {
        return STATUS_TICKET_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(STATUS_TICKET_ID, new AnalysisDimension());
        fieldBuilder.addField(STATUS_CHANGED_TO, new AnalysisDimension());
        fieldBuilder.addField(STATUS_CHANGED_AT, new AnalysisDateDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        try {
            FreshdeskCompositeSource freshdeskCompositeSource = (FreshdeskCompositeSource) parentDefinition;
            for (Map.Entry<String, List<Map>> entry : freshdeskCompositeSource.getStatusUpdates().entrySet()) {
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
                    int index = activityBody.lastIndexOf(" ");
                    String status = activityBody.substring(index).trim();
                    row.addValue(keys.get(STATUS_CHANGED_AT), getDate(ticketActivity, "performed_time"));
                    row.addValue(keys.get(STATUS_TICKET_ID), ticketID);
                    row.addValue(keys.get(STATUS_CHANGED_TO), status);
                }
                if (lastRefreshDate == null) {
                    IDataStorage.insertData(dataSet);
                } else {
                    StringWhere userWhere = new StringWhere(keys.get(STATUS_TICKET_ID), ticketID);
                    IDataStorage.updateData(dataSet, Arrays.asList(userWhere));
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
