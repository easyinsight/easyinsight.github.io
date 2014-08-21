package com.easyinsight.datafeeds.happyfox;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;

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
 * Date: 8/15/14
 * Time: 11:27 AM
 */
public class HappyFoxCompositeSource extends CompositeServerDataSource {
    private String url;
    private String authKey;
    private String hfApiKey;

    public HappyFoxCompositeSource() {
        setFeedName("Happy Fox");
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM HAPPYFOX WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO HAPPYFOX (DATA_SOURCE_ID, URL, API_KEY, AUTH_TOKEN) VALUES (?, ?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, url);
        saveStmt.setString(3, hfApiKey);
        saveStmt.setString(4, authKey);
        saveStmt.execute();
        saveStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, API_KEY, AUTH_TOKEN FROM HAPPYFOX WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            hfApiKey = rs.getString(2);
            authKey = rs.getString(3);
        }
        queryStmt.close();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getHfApiKey() {
        return hfApiKey;
    }

    public void setHfApiKey(String hfApiKey) {
        this.hfApiKey = hfApiKey;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HAPPYFOX_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<>();
        types.add(FeedType.HAPPYFOX_TICKET);
        types.add(FeedType.HAPPYFOX_CONTACTS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<>();
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
            basecampUrl = basecampUrl + ".happyfox.com";
        }
        return basecampUrl;
    }

}
