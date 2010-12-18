package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.users.Account;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/15/10
 * Time: 3:52 PM
 */
public class QuickbaseDatabaseSource extends ServerDataSourceDefinition {

    private String databaseID;

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM QUICKBASE_DATA_SOURCE where data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUICKBASE_DATA_SOURCE (DATA_SOURCE_ID, DATABASE_ID) " +
                "VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, databaseID);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT database_id FROM " +
                "QUICKBASE_DATA_SOURCE where data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        databaseID = rs.getString(1);
    }

    public String getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(String databaseID) {
        this.databaseID = databaseID;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) parent;
        return new QuickbaseFeed(databaseID, quickbaseCompositeSource.getApplicationToken(), quickbaseCompositeSource.getSessionTicket(),
                quickbaseCompositeSource.getHost(), quickbaseCompositeSource);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.QUICKBASE_CHILD;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        throw new UnsupportedOperationException();
    }
}
