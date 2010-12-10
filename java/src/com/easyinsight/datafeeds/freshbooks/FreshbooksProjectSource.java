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
 * Date: Jul 29, 2010
 * Time: 2:58:17 PM
 */
public class FreshbooksProjectSource extends FreshbooksBaseSource {

    public static final String PROJECT_ID = "Project ID";
    public static final String NAME = "Project Name";
    public static final String DESCRIPTION = "Project Description";
    public static final String RATE = "Project Rate";
    public static final String BILL_METHOD = "Project Billing Method";
    public static final String CLIENT_ID = "Client ID";    
    public static final String COUNT = "Project Count";

    public FreshbooksProjectSource() {
        setFeedName("Projects");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(PROJECT_ID, CLIENT_ID, NAME, DESCRIPTION, RATE, BILL_METHOD, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_PROJECTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.PROJECT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.DESCRIPTION), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.BILL_METHOD), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksProjectSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksProjectSource.RATE), AggregationTypes.SUM));       
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksProjectFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
