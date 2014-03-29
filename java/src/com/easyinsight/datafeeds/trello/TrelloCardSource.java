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
                /*
                [{"id":"513f9715b57837ed1f0009e4",
                "badges":{"votes":0,"viewingMemberVoted":false,"subscribed":false,"fogbugz":"","checkItems":0,"checkItemsChecked":0,"comments":0,"attachments":0,"description":false,"due":null},
                "checkItemStates":[],"closed":false,"dateLastActivity":"2013-03-12T20:59:01.034Z","desc":"","due":null,
                "idBoard":"513f971071b738690e00044b","idChecklists":[],
                "idList":"513f971071b738690e00044c","idMembers":[],"idMembersVoted":[],"idShort":1,
                "idAttachmentCover":null,"manualCoverAttachment":false,"labels":[],"name":"Card1","pos":65535,
                "shortUrl":"https://trello.com/c/L6QNH8jP","subscribed":false,"url":"https://trello.com/card/card1/513f971071b738690e00044b/1"},{"id":"513f97241293ff7f0e00088a","badges":{"votes":0,"viewingMemberVoted":false,"subscribed":false,"fogbugz":"","checkItems":0,"checkItemsChecked":0,"comments":0,"attachments":0,"description":false,"due":null},"checkItemStates":[],"closed":false,"dateLastActivity":"2013-03-12T20:59:16.303Z","desc":"","due":null,"idBoard":"513f971071b738690e00044b","idChecklists":[],"idList":"513f971071b738690e00044c","idMembers":[],"idMembersVoted":[],"idShort":4,"idAttachmentCover":null,"manualCoverAttachment":false,"labels":[],"name":"Do Something Third","pos":262143,"shortUrl":"https://trello.com/c/ptUv8i8X","subscribed":false,"url":"https://trello.com/card/do-something-third/513f971071b738690e00044b/4"},{"id":"513f972f0aa20d1c15000772","badges":{"votes":0,"viewingMemberVoted":false,"subscribed":false,"fogbugz":"","checkItems":0,"checkItemsChecked":0,"comments":0,"attachments":0,"description":false,"due":null},"checkItemStates":[],"closed":false,"dateLastActivity":"2013-03-12T20:59:27.368Z","desc":"","due":null,"idBoard":"513f971071b738690e00044b","idChecklists":[],"idList":"513f971071b738690e00044d","idMembers":[],"idMembersVoted":[],"idShort":5,"idAttachmentCover":null,"manualCoverAttachment":false,"labels":[],"name":"Blah","pos":65535,"shortUrl":"https://trello.com/c/e6yCzVq1","subscribed":false,"url":"https://trello.com/card/blah/513f971071b738690e00044b/5"},{"id":"513f971adfcb0dde1f0007d4","badges":{"votes":0,"viewingMemberVoted":false,"subscribed":false,"fogbugz":"","checkItems":0,"checkItemsChecked":0,"comments":0,"attachments":0,"description":false,"due":null},"checkItemStates":[],"closed":false,"dateLastActivity":"2013-03-12T20:59:06.459Z","desc":"","due":null,"idBoard":"513f971071b738690e00044b","idChecklists":[],"idList":"513f971071b738690e00044d","idMembers":["513f97d645ba390a0e0008f3"],"idMembersVoted":[],"idShort":2,"idAttachmentCover":null,"manualCoverAttachment":false,"labels":[],"name":"Do Something!","pos":131071,"shortUrl":"https://trello.com/c/iGiwUO8P","subscribed":false,"url":"https://trello.com/card/do-something/513f971071b738690e00044b/2"},{"id":"513f971dc47f1953270008d1","badges":{"votes":0,"viewingMemberVoted":false,"subscribed":false,"fogbugz":"","checkItems":0,"checkItemsChecked":0,"comments":0,"attachments":0,"description":false,"due":null},"checkItemStates":[],"closed":false,"dateLastActivity":"2013-03-12T20:59:09.622Z","desc":"","due":null,"idBoard":"513f971071b738690e00044b","idChecklists":[],"idList":"513f971071b738690e00044e","idMembers":[],"idMembersVoted":[],"idShort":3,"idAttachmentCover":null,"manualCoverAttachment":false,"labels":[],"name":"Do Something Else","pos":196607,"shortUrl":"https://trello.com/c/qkX6xBMm","subscribed":false,"url":"https://trello.com/card/do-something-else/513f971071b738690e00044b/3"}]
                 */
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
