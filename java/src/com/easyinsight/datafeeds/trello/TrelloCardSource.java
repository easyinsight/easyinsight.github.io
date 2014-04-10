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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/12/13
 * Time: 8:32 PM
 */
public class TrelloCardSource extends TrelloBaseSource {

    public static final String CARD_NAME = "Card Name";
    public static final String CARD_DESCRIPTION = "Card Description";
    public static final String CARD_CLOSED = "Card Closed";
    public static final String CARD_DUE_AT = "Card Due At";
    public static final String CARD_ID = "Card ID";
    public static final String CARD_BOARD_ID = "Card Board ID";
    public static final String CARD_LIST_ID = "Card List ID";

    public TrelloCardSource() {
        setFeedName("Cards");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CARD_NAME, CARD_ID, CARD_BOARD_ID, CARD_LIST_ID, CARD_DUE_AT, CARD_DESCRIPTION, CARD_CLOSED);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_CARD;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(CARD_NAME)));
        fields.add(new AnalysisDimension(keys.get(CARD_DESCRIPTION)));
        fields.add(new AnalysisDimension(keys.get(CARD_CLOSED)));
        fields.add(new AnalysisDimension(keys.get(CARD_ID)));
        fields.add(new AnalysisDimension(keys.get(CARD_LIST_ID)));
        fields.add(new AnalysisDimension(keys.get(CARD_BOARD_ID)));
        fields.add(new AnalysisDateDimension(keys.get(CARD_DUE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return fields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        TrelloCompositeSource trelloCompositeSource = (TrelloCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        List<CheckListData> cardCheckListData = new ArrayList<CheckListData>();
        List<LabelObject> labelData = new ArrayList<LabelObject>();
        List<Member> memberData = new ArrayList<Member>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            JSONArray boards = runRequest("https://api.trello.com/1/members/me/boards", httpClient, (TrelloCompositeSource) parentDefinition);
            for (int i = 0 ; i < boards.length(); i++) {
                JSONObject board = (JSONObject) boards.get(i);
                System.out.println(board.get("id") + " - " + board.get("name") + " - " + board.get("description") + " - " + board.get("url"));

                String id = (String) board.get("id");
                JSONArray cards = runRequest("https://api.trello.com/1/boards/" + id + "/cards?checklists=all", httpClient, (TrelloCompositeSource) parentDefinition);
                for (int j = 0; j < cards.length(); j++) {
                    JSONObject card = (JSONObject) cards.get(j);
                    IRow row = dataSet.createRow();
                    row.addValue(CARD_ID, card.get("id").toString());
                    if ("Card 4".equals(card.get("name"))) {
                        System.out.println("argh");
                    }
                    JSONArray checklists = (JSONArray) card.get("checklists");
                    for (int k = 0; k < checklists.length(); k++) {
                        JSONObject checkListObject = (JSONObject) checklists.get(k);
                        String name = checkListObject.get("name").toString();
                        JSONArray checkListItems = (JSONArray) checkListObject.get("checkItems");
                        for (int l = 0; l < checkListItems.length(); l++) {
                            JSONObject checkListItem = (JSONObject) checkListItems.get(l);
                            String itemName = checkListItem.get("name").toString();
                            String state = checkListItem.get("state").toString();
                            cardCheckListData.add(new CheckListData(name, itemName, state, card.get("id").toString()));
                        }
                    }
                    JSONArray labels = (JSONArray) card.get("labels");
                    for (int k = 0; k < labels.length(); k++) {
                        JSONObject jsonObject = (JSONObject) labels.get(k);
                        String color = jsonObject.get("color").toString();
                        String name = jsonObject.get("name").toString();
                        labelData.add(new LabelObject(card.get("id").toString(), name, color));
                    }

                    JSONArray members = (JSONArray) card.get("idMembers");
                    for (int k = 0; k < members.length(); k++) {
                        String memberID = members.get(k).toString();
                        memberData.add(new Member(card.get("id").toString(), memberID));
                    }

                    row.addValue(CARD_NAME, card.get("name").toString());
                    row.addValue(CARD_BOARD_ID, card.get("idBoard").toString());
                    row.addValue(CARD_LIST_ID, card.get("idList").toString());
                    row.addValue(CARD_CLOSED, card.get("closed").toString());
                    try {
                        Date dueDate = sdf.parse(card.get("due").toString());
                        row.addValue(CARD_DUE_AT, new DateValue(dueDate));
                    } catch (ParseException e) {
                        // ignore
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        trelloCompositeSource.setCardCheckListData(cardCheckListData);
        trelloCompositeSource.setMemberData(memberData);
        trelloCompositeSource.setLabelData(labelData);
        return dataSet;
    }
}
