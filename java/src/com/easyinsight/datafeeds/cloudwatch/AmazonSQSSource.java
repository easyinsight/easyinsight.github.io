package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/7/11
 * Time: 5:37 PM
 */
public class AmazonSQSSource extends ServerDataSourceDefinition {

    public static final String QUEUE_CREATION_DATE = "Queue Creation Date";
    public static final String LAST_MODIFIED = "Queue Last Modified On";
    public static final String APPROXIMATE_MESSAGES = "Approximate Number of Messages";
    public static final String QUEUE_URI = "Queue URI";

    public static final String QUEUE_NAME = "Queue Name";
    public static final String COUNT = "Queue Count";

    public AmazonSQSSource() {
        setFeedName("SQS");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.AMAZON_SQS;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new AmazonSQSFeed();
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(QUEUE_NAME, QUEUE_URI, QUEUE_CREATION_DATE, LAST_MODIFIED, COUNT, APPROXIMATE_MESSAGES);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> standardItems = new ArrayList<AnalysisItem>();
        standardItems.add(new AnalysisDimension(keys.get(QUEUE_NAME)));
        standardItems.add(new AnalysisDimension(keys.get(QUEUE_URI)));
        standardItems.add(new AnalysisDateDimension(keys.get(QUEUE_CREATION_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        standardItems.add(new AnalysisDateDimension(keys.get(LAST_MODIFIED), true, AnalysisDateDimension.DAY_LEVEL));
        standardItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(APPROXIMATE_MESSAGES), AggregationTypes.SUM));
        return standardItems;
    }
}
