package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 6:49:58 PM
 */
public class FreshbooksTaskSource extends FreshbooksBaseSource {

    public static final String TASK_ID = "Task ID";
    public static final String NAME = "Task Name";
    public static final String DESCRIPTION = "Task Description";
    public static final String BILLABLE = "Task Billable";
    public static final String RATE = "Task Rate";
    public static final String COUNT = "Task Count";

    public FreshbooksTaskSource() {
        setFeedName("Tasks");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(TASK_ID, NAME, DESCRIPTION, BILLABLE, RATE, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_TASKS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.TASK_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.DESCRIPTION), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.BILLABLE), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksTaskSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksTaskSource.RATE), RATE, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return items;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksTaskFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey());
    }
}
