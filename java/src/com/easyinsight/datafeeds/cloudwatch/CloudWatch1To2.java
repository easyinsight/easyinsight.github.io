package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FormattingConfiguration;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 5:26:53 PM
 */
public class CloudWatch1To2 extends DataSourceMigration {
    public CloudWatch1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    public void migrate(Map<String, Key> keys, EIConnection conn) {
        migrateAnalysisItem(CloudWatchDataSource.NETWORK_IN, new AnalysisMeasure(keys.get(CloudWatchDataSource.NETWORK_IN), "Network Bytes Received", AggregationTypes.SUM,
                false, FormattingConfiguration.BYTES));
        migrateAnalysisItem(CloudWatchDataSource.NETWORK_OUT, new AnalysisMeasure(keys.get(CloudWatchDataSource.NETWORK_OUT), "Network Bytes Sent", AggregationTypes.SUM,
                false, FormattingConfiguration.BYTES));
        migrateAnalysisItem(CloudWatchDataSource.DISK_READ_BYTES, new AnalysisMeasure(keys.get(CloudWatchDataSource.DISK_READ_BYTES), "Disk Bytes Read", AggregationTypes.SUM,
                false, FormattingConfiguration.BYTES));
        migrateAnalysisItem(CloudWatchDataSource.DISK_WRITE_BYTES, new AnalysisMeasure(keys.get(CloudWatchDataSource.DISK_WRITE_BYTES), "Disk Bytes Written", AggregationTypes.SUM,
                false, FormattingConfiguration.BYTES));
    }

    public int fromVersion() {
        return 1;
    }

    public int toVersion() {
        return 2;
    }
}
