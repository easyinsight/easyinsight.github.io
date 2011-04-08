package com.easyinsight.datafeeds.cloudwatch;

import com.easyinsight.PasswordStorage;

import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;

import com.easyinsight.kpi.KPI;

import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.users.Account;

import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.QueueAttribute;
import com.xerox.amazonws.sqs2.QueueService;


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
public class CloudWatchDataSource extends CompositeServerDataSource {

    public static final String DATE = "Date";

    public CloudWatchDataSource() {
        setFeedName("Amazon");
    }

    @Override
    public long create(EIConnection conn, List<AnalysisItem> externalAnalysisItems, FeedDefinition parentDefinition) throws Exception {
        long id = super.create(conn, externalAnalysisItems, parentDefinition);
        getFields().add(new AnalysisDateDimension(new NamedKey(DATE), true, AnalysisDateDimension.HOUR_LEVEL));
        new FeedStorage().updateDataFeedConfiguration(this, conn);
        return id;
    }

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
    public Feed createFeedObject(FeedDefinition parent) {
        try {
            return new CloudWatchFeed(getCompositeFeedNodes(), obtainChildConnections());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.AMAZON_EC2);
        types.add(FeedType.AMAZON_EBS);
        types.add(FeedType.AMAZON_SQS);
        types.add(FeedType.AMAZON_RDS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.AMAZON_EC2, FeedType.AMAZON_EBS, getField(DATE)));
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

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIForDateFilter("Average CPU Utilization in the Last 24 Hours", "cpu.png", (AnalysisMeasure) findAnalysisItem(AmazonEC2Source.CPU_UTILIZATION),
                (AnalysisDimension) findAnalysisItem(CloudWatchDataSource.DATE), MaterializedRollingFilterDefinition.LAST_DAY,
                null, 0, 1));
        return kpis;
    }

}
