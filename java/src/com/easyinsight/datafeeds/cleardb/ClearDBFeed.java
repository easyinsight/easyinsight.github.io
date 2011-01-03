package com.easyinsight.datafeeds.cleardb;

import com.cleardb.app.Client;
import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/30/10
 * Time: 10:49 PM
 */
public class ClearDBFeed extends Feed {
    private String table;
    private String apiKey;
    private String appID;

    public ClearDBFeed(String table, String apiKey, String appID) {
        this.table = table;
        this.apiKey = apiKey;
        this.appID = appID;
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        return null;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            Client client = new Client(apiKey, appID);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT ");
            for (AnalysisItem analysisItem : analysisItems) {
                stringBuilder.append(analysisItem.getKey().toKeyString()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(" FROM ").append(table);
            JSONObject results = client.query(stringBuilder.toString());
            JSONArray response = results.getJSONArray("response");
            for (int i = 0; i < response.length(); i++) {
                JSONObject responseRow = (JSONObject) response.get(i);
                IRow row = dataSet.createRow();
                for (AnalysisItem analysisItem : analysisItems) {
                    if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {

                    } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        row.addValue(analysisItem.createAggregateKey(), responseRow.getDouble(analysisItem.getKey().toKeyString()));
                    } else {
                        row.addValue(analysisItem.createAggregateKey(), responseRow.getString(analysisItem.getKey().toKeyString()));
                    }
                }
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
