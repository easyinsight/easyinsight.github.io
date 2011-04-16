package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.ParseException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:36 PM
 */
public class HarvestClientSource extends HarvestBaseSource {

    public static final String CLIENT_ID = "Client ID";
    public static final String NAME = "Client Name";
    public static final String ACTIVE = "Client Active";
    public static final String DETAILS = "Client Details";
    public static final String CLIENT_COUNT = "Client Count";


    public HarvestClientSource() {
        setFeedName("Clients");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_CLIENT;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CLIENT_ID, NAME, ACTIVE, DETAILS, CLIENT_COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem clientIdDim = new AnalysisDimension(keys.get(CLIENT_ID), true);
        clientIdDim.setHidden(true);
        analysisItems.add(clientIdDim);
        analysisItems.add(new AnalysisText(keys.get(DETAILS)));
        analysisItems.add(new AnalysisDimension(keys.get(NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(ACTIVE), true));
        analysisItems.add(new AnalysisMeasure(keys.get(CLIENT_COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        DataSet ds = new DataSet();
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document clients = runRestRequest("/clients", client, builder, source.getUrl(), true, parentDefinition, false);
            Nodes clientNodes = clients.query("/clients/client");
            for(int i = 0;i < clientNodes.size();i++) {
                Node curClient = clientNodes.get(i);
                String id = queryField(curClient, "id/text()");
                String name = queryField(curClient, "name/text()");
                String active = queryField(curClient, "active/text()");
                String details = queryField(curClient, "details/text()");

                IRow row = ds.createRow();
                row.addValue(keys.get(CLIENT_ID), id);
                row.addValue(keys.get(NAME), name);
                row.addValue(keys.get(ACTIVE), active);
                row.addValue(keys.get(DETAILS), details);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ds;
    }
}
