package com.easyinsight.core;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 5/17/12
 * Time: 10:52 AM
 */
public class XMLImportMetadata {
    private EIConnection conn;
    private FeedDefinition dataSource;

    public EIConnection getConn() {
        return conn;
    }

    public void setConn(EIConnection conn) {
        this.conn = conn;
    }

    public FeedDefinition getDataSource() {
        return dataSource;
    }

    public void setDataSource(FeedDefinition dataSource) {
        this.dataSource = dataSource;
    }

    public FeedDefinition dataSourceForURLKey(String urlKey) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE API_KEY = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            long id = rs.getLong(1);
            stmt.close();
            return new FeedStorage().getFeedDefinitionData(id, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
