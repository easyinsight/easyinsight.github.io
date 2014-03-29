package com.easyinsight.datafeeds.trello;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/24/14
 * Time: 9:29 PM
 */
public class TrelloLabelSource extends ServerDataSourceDefinition {
    public static final String LABEL_NAME = "Label Name";
    public static final String CARD_ID = "Label Card ID";
    public static final String LABEL_COLOR = "Label Color";

    public TrelloLabelSource() {
        setFeedName("Labels");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_LABELS;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(LABEL_NAME, new AnalysisDimension());
        fieldBuilder.addField(CARD_ID, new AnalysisDimension());
        fieldBuilder.addField(LABEL_COLOR, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            TrelloCompositeSource trelloCompositeSource = (TrelloCompositeSource) parentDefinition;
            List<LabelObject> c = trelloCompositeSource.getLabelData();
            for (LabelObject d : c) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(LABEL_NAME), d.getLabel());
                row.addValue(keys.get(CARD_ID), d.getCardID());
                row.addValue(keys.get(LABEL_COLOR), d.getColor());
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
