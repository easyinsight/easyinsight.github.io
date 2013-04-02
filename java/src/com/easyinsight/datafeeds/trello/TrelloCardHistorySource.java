package com.easyinsight.datafeeds.trello;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/18/13
 * Time: 8:01 PM
 */
public class TrelloCardHistorySource extends TrelloBaseSource {

    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String HISTORY_CARD_ID = "History Card ID";
    public static final String HISTORY_ID = "History ID";
    public static final String FROM_LIST = "From List";
    public static final String TO_LIST = "To List";
    public static final String HISTORY_TIME = "History Date";
    public static final String HISTORY_COUNT = "History Count";


    public TrelloCardHistorySource() {
        setFeedName("Card History");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(HISTORY_CARD_ID, HISTORY_ID, FROM_LIST, TO_LIST, HISTORY_TIME, HISTORY_COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_CARD_HISTORY;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(HISTORY_CARD_ID)));
        fields.add(new AnalysisDimension(keys.get(HISTORY_ID)));
        fields.add(new AnalysisDimension(keys.get(FROM_LIST)));
        fields.add(new AnalysisDimension(keys.get(TO_LIST)));
        fields.add(new AnalysisDateDimension(keys.get(HISTORY_TIME), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisMeasure(keys.get(HISTORY_COUNT), AggregationTypes.SUM));
        return fields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(XMLDATETIMEFORMAT);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            JSONArray boards = runRequest("https://api.trello.com/1/members/me/boards", httpClient, (TrelloCompositeSource) parentDefinition);
            for (int i = 0 ; i < boards.length(); i++) {
                JSONObject board = (JSONObject) boards.get(i);
                System.out.println(board.get("id") + " - " + board.get("name") + " - " + board.get("description") + " - " + board.get("url"));

                String id = (String) board.get("id");
                JSONArray cards = runRequest("https://api.trello.com/1/boards/" + id + "/cards", httpClient, (TrelloCompositeSource) parentDefinition);
                for (int j = 0; j < cards.length(); j++) {
                    JSONObject card = (JSONObject) cards.get(j);
                    JSONArray history = runRequest("https://api.trello.com/1/cards/"+card.get("id")+"/actions?filter=updateCard:idList", httpClient, (TrelloCompositeSource) parentDefinition);
                    // for each item in history
                    for (int k = 0; k < history.length(); k++) {
                        IRow row = dataSet.createRow();
                        JSONObject historyObject = (JSONObject) history.get(k);
                        String dateString = historyObject.get("date").toString();
                        Date date = sdf.parse(dateString);
                        String oldList = ((JSONObject)((JSONObject) historyObject.get("data")).get("listBefore")).get("id").toString();
                        String newList = ((JSONObject)((JSONObject) historyObject.get("data")).get("listAfter")).get("id").toString();
                        row.addValue(HISTORY_CARD_ID, card.get("id").toString());
                        row.addValue(HISTORY_TIME, new DateValue(date));
                        row.addValue(FROM_LIST, oldList);
                        row.addValue(TO_LIST, newList);
                        row.addValue(HISTORY_COUNT, 1);
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
