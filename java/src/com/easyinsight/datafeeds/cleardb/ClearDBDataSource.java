package com.easyinsight.datafeeds.cleardb;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/30/10
 * Time: 10:28 PM
 */
public class ClearDBDataSource extends ServerDataSourceDefinition {

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CLEARDB_CHILD;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM cleardb_child WHERE data_source_id = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO cleardb_child (cleardb_table, data_source_id) VALUES (?, ?)");
        insertStmt.setString(1, tableName);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT cleardb_table FROM CLEARDB_CHILD WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            tableName = rs.getString(1);
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        ClearDBCompositeSource clearDBCompositeSource = (ClearDBCompositeSource) parent;
        return new ClearDBFeed(tableName, clearDBCompositeSource.getCdApiKey(), clearDBCompositeSource.getAppToken());
    }
}
