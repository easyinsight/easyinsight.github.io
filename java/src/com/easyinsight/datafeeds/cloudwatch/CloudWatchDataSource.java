package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.core.Key;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 3:08:31 PM
 */
public class CloudWatchDataSource extends ServerDataSourceDefinition {

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

    public static final String LATENCY = "Latency";
    public static final String REQUEST_COUNT = "RequestCount";
    public static final String HEALTHY_HOST_COUNT = "HealthyHostCount";
    public static final String UNHEALTHY_HOST_COUNT = "UnHealthyHostCount";

    public static final String LOAD_BALANCER_NAME = "LoadBalancerName";
    public static final String AVAILABILITY_ZONE = "AvailabilityZone";

    public static final String DATE = "Date";

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CLOUD_WATCH;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    @Override
    public Feed createFeedObject() {
        return new CloudWatchFeed();
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        return new DataSet();
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(CPU_UTILIZATION, NETWORK_IN, NETWORK_OUT, DISK_WRITE_BYTES, DISK_WRITE_OPS, DISK_READ_BYTES, DISK_READ_OPS,
                IMAGE_ID, INSTANCE_ID, GROUP_NAME, HOST_NAME, DATE);
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.INDIVIDUAL;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> standardItems = new ArrayList<AnalysisItem>();

        standardItems.add(new AnalysisDimension(keys.get(IMAGE_ID), "Image ID"));
        standardItems.add(new AnalysisDimension(keys.get(INSTANCE_ID), "Instance ID"));
        standardItems.add(new AnalysisDimension(keys.get(GROUP_NAME), "Security Group"));
        standardItems.add(new AnalysisDimension(keys.get(HOST_NAME), "Host Name"));
        standardItems.add(new AnalysisDateDimension(keys.get(DATE), "Date", AnalysisDateDimension.MINUTE_LEVEL));
        AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(CPU_UTILIZATION), "CPU Utilization %", AggregationTypes.AVERAGE);
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
        analysisMeasure.setFormattingConfiguration(formattingConfiguration);
        standardItems.add(analysisMeasure);
        standardItems.add(new AnalysisMeasure(keys.get(NETWORK_IN), "Network Bytes Received", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(NETWORK_OUT), "Network Bytes Sent", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_WRITE_BYTES), "Disk Bytes Written", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_WRITE_OPS), "Disk Write Ops", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_READ_BYTES), "Disk Bytes Read", AggregationTypes.SUM));
        standardItems.add(new AnalysisMeasure(keys.get(DISK_READ_OPS), "Disk Read Ops", AggregationTypes.SUM));


        if (getDataFeedID() == 0) {
            FeedFolder visitorFolder = defineFolder("Standard");
            visitorFolder.getChildItems().addAll(standardItems);
        }
        return standardItems;
    }

    public int getVersion() {
        return 2;
    }

    public void customStorage(Connection conn) throws SQLException {
    }

    public void customLoad(Connection conn) throws SQLException {
    }

    @Override
    public String validateCredentials(Credentials credentials) {
        return null;
    }

    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new CloudWatch1To2(this));
    }
}
