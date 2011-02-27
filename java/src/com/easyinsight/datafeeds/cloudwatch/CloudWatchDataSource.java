package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.PasswordStorage;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.core.Key;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private String cwUserName;
    private String cwPassword;

    public String getCwUserName() {
        return cwUserName;
    }

    public void setCwUserName(String cwUserName) {
        this.cwUserName = cwUserName;
    }

    public String getCwPassword() {
        return cwPassword;
    }

    public void setCwPassword(String cwPassword) {
        this.cwPassword = cwPassword;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        CloudWatchDataSource cloudWatchDataSource = (CloudWatchDataSource) super.clone(conn);
        cloudWatchDataSource.setCwUserName(null);
        cloudWatchDataSource.setCwPassword(null);
        return cloudWatchDataSource;
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CLOUD_WATCH;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new CloudWatchFeed();
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        return new DataSet();
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(CPU_UTILIZATION, NETWORK_IN, NETWORK_OUT, DISK_WRITE_BYTES, DISK_WRITE_OPS, DISK_READ_BYTES, DISK_READ_OPS,
                IMAGE_ID, INSTANCE_ID, GROUP_NAME, HOST_NAME, DATE);
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
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

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM cloudwatch WHERE data_source_id = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO cloudwatch (data_source_id, cg_username, cg_password) values (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, cwUserName);
        insertStmt.setString(3, cwPassword != null ? PasswordStorage.encryptString(cwPassword) : null);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement stmt = conn.prepareStatement("SELECT CG_USERNAME, CG_PASSWORD FROM cloudwatch WHERE DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            cwUserName = rs.getString(1);
            String password = rs.getString(2);
            if (password != null) {
                cwPassword = PasswordStorage.decryptString(password);
            }
        }
    }

    @Override
    public String validateCredentials() {
        try {
            EC2Util.getInstances(cwUserName, cwPassword);
        } catch (RuntimeException re) {
            if (re.getMessage().indexOf("401") != -1) {
                return "Your credentials were invalid.";
            } else {
                return re.getMessage();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new CloudWatch1To2(this));
    }

    @Override
    public String getFilterExampleMessage() {
        return "For example, drag Security Group into the area to the right to restrict the KPI to a particular security group.";
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIForDateFilter("Average CPU Utilization in the Last 24 Hours", "cpu.png", (AnalysisMeasure) findAnalysisItem(CloudWatchDataSource.CPU_UTILIZATION),
                (AnalysisDimension) findAnalysisItem(CloudWatchDataSource.DATE), MaterializedRollingFilterDefinition.LAST_DAY,
                null, 0, 1));
        return kpis;
    }
}
