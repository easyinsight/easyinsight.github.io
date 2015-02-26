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
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.http.impl.client.DefaultHttpClient;


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
    public static final String CARD_LAST_ACTIVITY_DATE = "Card Last Activity Date";
    public static final String CARD_COUNT = "Card Count";
    public static final String CARD_URL = "Card URL";
    public static final String CARD_CREATED = "Card Created At";

    public TrelloCardSource() {
        setFeedName("Cards");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CARD_NAME, new AnalysisDimension());
        fieldBuilder.addField(CARD_ID, new AnalysisDimension());
        fieldBuilder.addField(CARD_BOARD_ID, new AnalysisDimension());
        fieldBuilder.addField(CARD_LIST_ID, new AnalysisDimension());
        fieldBuilder.addField(CARD_DUE_AT, new AnalysisDateDimension());
        fieldBuilder.addField(CARD_DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(CARD_CLOSED, new AnalysisDimension());
        fieldBuilder.addField(CARD_LAST_ACTIVITY_DATE, new AnalysisDateDimension());
        fieldBuilder.addField(CARD_CREATED, new AnalysisDateDimension());
        fieldBuilder.addField(CARD_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(CARD_URL, new AnalysisDimension());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_CARD;
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
            SimpleDateFormat dueDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            JSONArray boards = runRequest("https://api.trello.com/1/members/me/boards", httpClient, (TrelloCompositeSource) parentDefinition);
            for (int i = 0 ; i < boards.size(); i++) {
                JSONObject board = (JSONObject) boards.get(i);
                String id = (String) board.get("id");
                JSONArray cards = runRequest("https://api.trello.com/1/boards/" + id + "/cards?checklists=all", httpClient, (TrelloCompositeSource) parentDefinition);
                for (int j = 0; j < cards.size(); j++) {
                    JSONObject card = (JSONObject) cards.get(j);
                    IRow row = dataSet.createRow();
                    String cardID = card.get("id").toString();
                    row.addValue(CARD_ID, cardID);
                    String hexString = cardID.substring(0, 8);
                    long hexDate = Long.parseLong(hexString, 16) * 1000L;

                    JSONArray checklists = (JSONArray) card.get("checklists");
                    for (int k = 0; k < checklists.size(); k++) {
                        JSONObject checkListObject = (JSONObject) checklists.get(k);
                        String name = checkListObject.get("name").toString();
                        JSONArray checkListItems = (JSONArray) checkListObject.get("checkItems");
                        for (int l = 0; l < checkListItems.size(); l++) {
                            JSONObject checkListItem = (JSONObject) checkListItems.get(l);
                            String itemName = checkListItem.get("name").toString();
                            String state = checkListItem.get("state").toString();
                            cardCheckListData.add(new CheckListData(name, itemName, state, card.get("id").toString()));
                        }
                    }
                    JSONArray labels = (JSONArray) card.get("labels");
                    for (int k = 0; k < labels.size(); k++) {
                        JSONObject jsonObject = (JSONObject) labels.get(k);
                        try {
                            String color = jsonObject.get("color").toString();
                            String name = jsonObject.get("name").toString();
                            labelData.add(new LabelObject(card.get("id").toString(), name, color));
                        } catch (Exception e) {
                            LogClass.error(e);
                        }
                    }

                    JSONArray members = (JSONArray) card.get("idMembers");
                    for (int k = 0; k < members.size(); k++) {
                        String memberID = members.get(k).toString();
                        try {
                            memberData.add(new Member(card.get("id").toString(), memberID));
                        } catch (Exception e) {
                            LogClass.error(e);
                        }
                    }
                    row.addValue(CARD_CREATED, new DateValue(new Date(hexDate)));
                    row.addValue(CARD_NAME, card.get("name").toString());
                    row.addValue(CARD_BOARD_ID, card.get("idBoard").toString());
                    row.addValue(CARD_LIST_ID, getValue(card, "idList"));
                    row.addValue(CARD_CLOSED, getValue(card, "closed"));
                    row.addValue(CARD_DESCRIPTION, getValue(card, "description"));
                    row.addValue(CARD_URL, getValue(card, "shortUrl"));
                    try {
                        Date dueDate = getDate(card, "due", dueDF);
                        row.addValue(CARD_DUE_AT, new DateValue(dueDate));
                    } catch (ParseException e) {
                        // ignore
                    }
                    try {
                        Date dueDate = getDate(card, "dateLastActivity", dueDF);
                        row.addValue(CARD_LAST_ACTIVITY_DATE, new DateValue(dueDate));
                    } catch (ParseException e) {
                        // ignore
                    }
                    row.addValue(CARD_COUNT, 1);
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
