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
 * Date: 3/31/11
 * Time: 5:02 PM
 */
public class AmazonEBSSource extends ServerDataSourceDefinition {

    public static final String VOLUME_WRITE_BYTES = "VolumeWriteBytes";
    public static final String VOLUME_READ_OPS = "VolumeReadOps";
    public static final String VOLUME_WRITE_OPS = "VolumeWriteOps";
    public static final String VOLUME_TOTAL_READ_TIME = "VolumeTotalReadTime";
    public static final String VOLUME_TOTAL_WRITE_TIME = "VolumeTotalWriteTime";
    public static final String VOLUME_READ_BYTES = "VolumeReadBytes";
    public static final String VOLUME_QUEUE_LENGTH = "VolumeQueueLength";
    public static final String VOLUME_IDLE_TIME = "VolumeIdleTime";

    public static final String VOLUME = "Volume ID";

    public AmazonEBSSource() {
        setFeedName("EBS");
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new AmazonEBSFeed();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.AMAZON_EBS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(VOLUME_WRITE_BYTES, VOLUME_READ_OPS, VOLUME_WRITE_OPS, VOLUME_TOTAL_READ_TIME,
                VOLUME_TOTAL_WRITE_TIME, VOLUME_READ_BYTES, VOLUME_QUEUE_LENGTH, VOLUME_IDLE_TIME, VOLUME);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisMeasure(keys.get(VOLUME_WRITE_BYTES), "Volume Bytes Written", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        items.add(new AnalysisMeasure(keys.get(VOLUME_READ_BYTES), "Volume Bytes Read", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        items.add(new AnalysisMeasure(keys.get(VOLUME_READ_OPS), "Volume Read Ops", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(VOLUME_WRITE_OPS), "Volume Write Ops", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(VOLUME_QUEUE_LENGTH), "Volume Queue Length", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(VOLUME_IDLE_TIME), "Volume Idle Time", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(VOLUME_TOTAL_READ_TIME), "Volume Total Read Time", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(VOLUME_TOTAL_WRITE_TIME), "Volume Total Write Time", AggregationTypes.SUM));
        items.add(new AnalysisDimension(keys.get(VOLUME)));
        return items;
    }
}
