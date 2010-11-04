package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 6:50:08 PM
 */
public class FreshbooksTimeEntrySource extends FreshbooksBaseSource {

    public static final String TIME_ENTRY_ID = "Time Entry ID";
    public static final String STAFF_ID = "Staff ID";
    public static final String PROJECT_ID = "Project ID";
    public static final String TASK_ID = "Task ID";
    public static final String HOURS = "Time Entry Hours";
    public static final String DATE = "Time Entry Date";
    public static final String NOTES = "Time Entry Notes";
    public static final String COUNT = "Time Entry Count";

    public FreshbooksTimeEntrySource() {
        setFeedName("Time Entries");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(TIME_ENTRY_ID, STAFF_ID, PROJECT_ID, TASK_ID, HOURS, DATE, NOTES, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_TIME_ENTRIES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet,
                                                  Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.TIME_ENTRY_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.STAFF_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.PROJECT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.TASK_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.NOTES), true));

        items.add(new AnalysisDateDimension(keys.get(FreshbooksTimeEntrySource.DATE), true, AnalysisDateDimension.DAY_LEVEL));

        items.add(new AnalysisMeasure(keys.get(FreshbooksTimeEntrySource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksTimeEntrySource.HOURS), AggregationTypes.SUM));        

        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksTimeFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey());
    }
}
