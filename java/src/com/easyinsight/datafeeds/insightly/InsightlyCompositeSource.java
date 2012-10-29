package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/16/12
 * Time: 4:22 PM
 */
public class InsightlyCompositeSource extends CompositeServerDataSource {
    private String insightlyApiKey;

    public InsightlyCompositeSource() {
        setFeedName("Insightly");
    }

    public String getInsightlyApiKey() {
        return insightlyApiKey;
    }

    public void setInsightlyApiKey(String insightlyApiKey) {
        this.insightlyApiKey = insightlyApiKey;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM INSIGHTLY_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO INSIGHTLY_SOURCE (API_KEY, DATA_SOURCE_ID) VALUES (?, ?)");
        insertStmt.setString(1, insightlyApiKey);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT API_KEY FROM INSIGHTLY_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            insightlyApiKey = rs.getString(1);
        }
        getStmt.close();
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.INSIGHTLY_CONTACTS);
        types.add(FeedType.INSIGHTLY_ORGANIZATIONS);
        types.add(FeedType.INSIGHTLY_OPPORTUNITIES);
        types.add(FeedType.INSIGHTLY_PROJECTS);
        types.add(FeedType.INSIGHTLY_TASKS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.INSIGHTLY_OPPORTUNITIES, FeedType.INSIGHTLY_PROJECTS, InsightlyOpportunitySource.OPPORTUNITY_ID, InsightlyProjectSource.OPPORTUNITY_ID),
            new ChildConnection(FeedType.INSIGHTLY_PROJECTS, FeedType.INSIGHTLY_TASKS, InsightlyProjectSource.PROJECT_ID, InsightlyTaskSource.PROJECT_ID),
            new ChildConnection(FeedType.INSIGHTLY_OPPORTUNITIES, FeedType.INSIGHTLY_TASKS, InsightlyOpportunitySource.OPPORTUNITY_ID, InsightlyTaskSource.OPPORTUNITY_ID),
            new ChildConnection(FeedType.INSIGHTLY_ORGANIZATIONS, FeedType.INSIGHTLY_CONTACTS, InsightlyOrganisationSource.ORGANIZATION_ID, InsightlyContactSource.ORGANIZATION_ID));
    }
}
