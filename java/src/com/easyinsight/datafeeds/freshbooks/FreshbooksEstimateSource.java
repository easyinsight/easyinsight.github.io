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
 * Time: 5:31:28 PM
 */
public class FreshbooksEstimateSource extends FreshbooksBaseSource {

    public static final String ESTIMATE_ID = "Estimate ID";
    public static final String NUMBER = "Estimate Number";
    public static final String CLIENT_ID = "Estimate Client ID";
    public static final String AMOUNT = "Estimate Amount";
    public static final String NOTES = "Estimate Notes";
    public static final String TERMS = "Estimate Terms";
    public static final String COUNT = "Estimate Count";

    public FreshbooksEstimateSource() {
        setFeedName("Estimates");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(ESTIMATE_ID, CLIENT_ID, AMOUNT, NUMBER, TERMS, NOTES, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_ESTIMATES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.ESTIMATE_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.NUMBER), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.NOTES), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.TERMS), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksEstimateSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksEstimateSource.AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));                
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksEstimateFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
