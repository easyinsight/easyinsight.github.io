package com.easyinsight.datafeeds.trello;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/24/14
 * Time: 9:29 PM
 */
public class TrelloChecklistSource extends TrelloBaseSource {

    public static final String CHECKLIST_NAME = "Check List Name";
    public static final String CARD_ID = "Check List Card ID";
    public static final String ITEM_NAME = "Check List Item Name";
    public static final String ITEM_STATE = "Check List Item State";
    public static final String ITEM_COUNT = "Check List Item Count";

    public TrelloChecklistSource() {
        setFeedName("Checklists");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_CHECKLISTS;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CHECKLIST_NAME, new AnalysisDimension());
        fieldBuilder.addField(CARD_ID, new AnalysisDimension());
        fieldBuilder.addField(ITEM_NAME, new AnalysisDimension());
        fieldBuilder.addField(ITEM_STATE, new AnalysisDimension());
        fieldBuilder.addField(ITEM_COUNT, new AnalysisMeasure());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            TrelloCompositeSource trelloCompositeSource = (TrelloCompositeSource) parentDefinition;
            List<CheckListData> c = trelloCompositeSource.getCardCheckListData();
            for (CheckListData d : c) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(CHECKLIST_NAME), d.getChecklistName());
                row.addValue(keys.get(CARD_ID), d.getCardID());
                row.addValue(keys.get(ITEM_NAME), d.getItemName());
                row.addValue(keys.get(ITEM_STATE), d.getItemState());
                row.addValue(keys.get(ITEM_COUNT), 1);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
