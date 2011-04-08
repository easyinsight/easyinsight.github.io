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
public class AmazonEC2Source extends ServerDataSourceDefinition {
    public static final String CPU_UTILIZATION = "CPUUtilization";
    public static final String NETWORK_IN = "NetworkIn";
    public static final String NETWORK_OUT = "NetworkOut";
    public static final String DISK_WRITE_OPS = "DiskWriteOps";
    public static final String DISK_READ_BYTES = "DiskReadBytes";
    public static final String DISK_READ_OPS = "DiskReadOps";
    public static final String DISK_WRITE_BYTES = "DiskWriteBytes";

    public static final String IMAGE_ID = "ImageId";
    public static final String INSTANCE_ID = "InstanceId";
    public static final String GROUP_NAME = "Security Group";
    public static final String HOST_NAME = "Host Name";

    public AmazonEC2Source() {
        setFeedName("EC2");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.AMAZON_EC2;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new AmazonEC2Feed();
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CPU_UTILIZATION, NETWORK_IN, NETWORK_OUT, DISK_WRITE_OPS, DISK_READ_BYTES, DISK_READ_OPS,
                DISK_WRITE_BYTES, IMAGE_ID, INSTANCE_ID, GROUP_NAME, HOST_NAME);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> standardItems = new ArrayList<AnalysisItem>();
        standardItems.add(new AnalysisDimension(keys.get(IMAGE_ID), "Image ID"));
        standardItems.add(new AnalysisDimension(keys.get(INSTANCE_ID), "Instance ID"));
        standardItems.add(new AnalysisDimension(keys.get(GROUP_NAME), "Security Group"));
        standardItems.add(new AnalysisDimension(keys.get(HOST_NAME), "Host Name"));
        AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(CPU_UTILIZATION), "CPU Utilization %", AggregationTypes.AVERAGE);
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
        analysisMeasure.setFormattingConfiguration(formattingConfiguration);
        standardItems.add(analysisMeasure);
        standardItems.add(new AnalysisMeasure(keys.get(NETWORK_IN), "Network Bytes Received", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        standardItems.add(new AnalysisMeasure(keys.get(NETWORK_OUT), "Network Bytes Sent", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_WRITE_BYTES), "Disk Bytes Written", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_WRITE_OPS), "Disk Write Ops", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_READ_BYTES), "Disk Bytes Read", AggregationTypes.SUM, true, FormattingConfiguration.BYTES));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_READ_OPS), "Disk Read Ops", AggregationTypes.SUM));
        return standardItems;
    }
}
