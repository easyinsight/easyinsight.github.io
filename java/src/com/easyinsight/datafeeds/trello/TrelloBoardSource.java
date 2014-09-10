package com.easyinsight.datafeeds.trello;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/12/13
 * Time: 8:32 PM
 */
public class TrelloBoardSource extends TrelloBaseSource {

    public static final String BOARD_NAME = "Board Name";
    public static final String BOARD_ID = "Board ID";
    public static final String BOARD_DESCRIPTION = "Board Description";
    public static final String BOARD_URL = "Board URL";

    public TrelloBoardSource() {
        setFeedName("Boards");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(BOARD_NAME, BOARD_ID, BOARD_DESCRIPTION, BOARD_URL);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_BOARD;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(BOARD_NAME)));
        fields.add(new AnalysisDimension(keys.get(BOARD_ID)));
        fields.add(new AnalysisDimension(keys.get(BOARD_DESCRIPTION)));
        fields.add(new AnalysisDimension(keys.get(BOARD_URL)));
        return fields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            JSONArray boards = runRequest("https://api.trello.com/1/members/me/boards", httpClient, (TrelloCompositeSource) parentDefinition);
            for (int i = 0 ; i < boards.length(); i++) {
                JSONObject board = (JSONObject) boards.get(i);
                IRow row = dataSet.createRow();
                row.addValue(BOARD_ID, board.get("id").toString());
                row.addValue(BOARD_NAME, board.get("name").toString());
                row.addValue(BOARD_DESCRIPTION, board.get("description").toString());
                row.addValue(BOARD_URL, board.get("url").toString());
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
