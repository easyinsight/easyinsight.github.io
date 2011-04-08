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
public class AmazonRDSSource extends ServerDataSourceDefinition {

    public static final String FREE_STORAGE_SPACE = "FreeStorageSpace";
    public static final String CPU_UTILIZATION = "CPUUtilization";
    public static final String READ_IOPS = "ReadIOPS";
    public static final String SWAP_USAGE = "SwapUsage";
    public static final String WRITE_THROUGHPUT = "WriteThroughput";
    public static final String FREEABLE_MEMORY = "FreeableMemory";
    public static final String DATABASE_CONNECTIONS = "DatabaseConnections";
    public static final String READ_THROUGHPUT = "ReadThroughput";
    public static final String WRITE_IOPS = "WriteIOPS";
    public static final String READ_LATENCY = "ReadLatency";
    public static final String WRITE_LATENCY = "WriteLatency";

    public static final String INSTANCE_IDENTIFIER = "DBInstanceIdentifier";

    public AmazonRDSSource() {
        setFeedName("RDS");
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new AmazonRDSFeed();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.AMAZON_RDS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(FREE_STORAGE_SPACE, CPU_UTILIZATION, READ_IOPS, SWAP_USAGE, WRITE_THROUGHPUT,
                FREEABLE_MEMORY, DATABASE_CONNECTIONS, READ_THROUGHPUT, WRITE_IOPS, READ_LATENCY, WRITE_LATENCY,
                INSTANCE_IDENTIFIER);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisMeasure(keys.get(FREE_STORAGE_SPACE), "Volume Bytes Written", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        items.add(new AnalysisMeasure(keys.get(FREEABLE_MEMORY), "Volume Bytes Read", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        items.add(new AnalysisMeasure(keys.get(CPU_UTILIZATION), "CPU Utilization", AggregationTypes.AVERAGE, true, FormattingConfiguration.PERCENTAGE));
        items.add(new AnalysisMeasure(keys.get(READ_IOPS), "Read Ops", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(WRITE_IOPS), "Write Ops", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(READ_LATENCY), "Read Latency", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(WRITE_LATENCY), "Write Latency", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(SWAP_USAGE), "Swap Usage", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(READ_THROUGHPUT), "Read Throughput", AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(WRITE_THROUGHPUT), "Write Throughput", AggregationTypes.SUM));
        items.add(new AnalysisDimension(keys.get(INSTANCE_IDENTIFIER)));

        return items;
    }
}
