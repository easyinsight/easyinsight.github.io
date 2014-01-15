package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.DataSourceInfo;
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
 * Date: 1/10/14
 * Time: 11:45 AM
 */
public class FreshdeskCompositeSource extends CompositeServerDataSource {
    private String url;
    private String freshdeskApiKey;

    public FreshdeskCompositeSource() {
        setFeedName("Freshdesk");
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM FRESHDESK WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO FRESHDESK (DATA_SOURCE_ID, URL, API_TOKEN) VALUES (?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, url);
        saveStmt.setString(3, freshdeskApiKey);
        saveStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, API_TOKEN FROM FRESHDESK WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            freshdeskApiKey = rs.getString(2);
        }
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFreshdeskApiKey() {
        return freshdeskApiKey;
    }

    public void setFreshdeskApiKey(String freshdeskApiKey) {
        this.freshdeskApiKey = freshdeskApiKey;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.FRESHDESK_TICKET);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_COMPOSITE;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "https://") + url;
        basecampUrl = basecampUrl.replaceFirst("^http://", "https://");
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if (!basecampUrl.contains(".")) {
            basecampUrl = basecampUrl + ".freshdesk.com";
        }
        return basecampUrl;
    }
}
