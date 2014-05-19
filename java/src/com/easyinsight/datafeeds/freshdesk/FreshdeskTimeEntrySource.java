package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
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

    public static final String ID = "ID";

    public FreshdeskTimeEntrySource() {
        setFeedName("Time");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
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
                blah = runRestRequestForList("time_sheets.json", client, freshdeskCompositeSource);
            } else {
                blah = runRestRequestForList("time_sheets.json?page=" + page, client, freshdeskCompositeSource);
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
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_TIME;
    }
}
