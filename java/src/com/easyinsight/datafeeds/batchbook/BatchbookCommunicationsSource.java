package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/17/11
 * Time: 10:13 PM
 */
public class BatchbookCommunicationsSource extends BatchbookBaseSource {
    public static final String COMMUNICATION_ID = "Communication ID";
    public static final String SUBJECT = "Subject";
    public static final String BODY = "Body";
    public static final String DATE = "Communication Date";
    public static final String TYPE = "Communication Type";

    public static final String TAGS = "Communication Tags";
    public static final String COMMUNICATION_COUNT = "Communication Count";

    public BatchbookCommunicationsSource() {
        setFeedName("Communications");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_COMMUNICATIONS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(COMMUNICATION_ID, SUBJECT, BODY, DATE, TYPE, TAGS, COMMUNICATION_COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMMUNICATION_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(SUBJECT), true));
        analysisItems.add(new AnalysisDimension(keys.get(BODY), true));
        analysisItems.add(new AnalysisDimension(keys.get(TYPE), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisMeasure(keys.get(COMMUNICATION_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();

        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        try {
            List<Communication> communications = batchbookCompositeSource.getOrCreateCache().getCommunications();
            for (Communication communication : communications) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(COMMUNICATION_ID), communication.getId());
                row.addValue(keys.get(SUBJECT), communication.getSubject());
                row.addValue(keys.get(BODY), communication.getBody());
                row.addValue(keys.get(TYPE), communication.getType());
                row.addValue(keys.get(COMMUNICATION_COUNT), 1);
                row.addValue(keys.get(DATE), communication.getDate());
                row.addValue(keys.get(TAGS), communication.getTags());
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
