package com.easyinsight.datafeeds.cleardb;

import com.cleardb.app.Client;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/30/10
 * Time: 10:28 PM
 */
public class ClearDBCompositeSource extends CompositeServerDataSource {
    private String cdApiKey;
    private String appToken;

    public String getCdApiKey() {
        return cdApiKey;
    }

    public void setCdApiKey(String cdApiKey) {
        this.cdApiKey = cdApiKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public String validateCredentials() {
        try {
            Client client = new Client(cdApiKey, appToken);
            client.query("SELECT 1");
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        return new HashSet<FeedType>();
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CLEARDB_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM CLEARDB WHERE data_source_id = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO CLEARDB (api_key, app_id, data_source_id) VALUES (?, ?, ?)");
        insertStmt.setString(1, cdApiKey);
        insertStmt.setString(2, appToken);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT API_KEY, APP_ID FROM CLEARDB WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            cdApiKey = rs.getString(1);
            appToken = rs.getString(2);
        }
    }
}
