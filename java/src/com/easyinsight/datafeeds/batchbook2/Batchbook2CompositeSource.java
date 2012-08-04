package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import org.apache.commons.httpclient.HttpClient;

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
 * Date: 7/16/12
 * Time: 9:31 AM
 */
public class Batchbook2CompositeSource extends CompositeServerDataSource {

    private String url;
    private String token;

    public Batchbook2CompositeSource() {
        setFeedName("Batchbook");
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public String getUrl() {
        String batchbookURL = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(batchbookURL.endsWith("/")) {
            batchbookURL = batchbookURL.substring(0, batchbookURL.length() - 1);
        }
        if(!(batchbookURL.endsWith(".batchbook.com")))
            batchbookURL = batchbookURL + ".batchbook.com";
        batchbookURL = batchbookURL.trim();
        return batchbookURL;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM BATCHBOOK2 WHERE DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        stmt.executeUpdate();
        PreparedStatement insert = conn.prepareStatement("INSERT INTO BATCHBOOK2 (URL, AUTH_TOKEN, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insert.setString(1, url);
        insert.setString(2, token);
        insert.setLong(3, getDataFeedID());
        insert.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement query = conn.prepareStatement("SELECT URL, AUTH_TOKEN FROM BATCHBOOK2 WHERE DATA_SOURCE_ID = ?");
        query.setLong(1, getDataFeedID());
        ResultSet rs = query.executeQuery();
        if (rs.next()) {
            setUrl(rs.getString(1));
            setToken(rs.getString(2));
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.BATCHBOOK2_COMPANIES);
        types.add(FeedType.BATCHBOOK2_PEOPLE);
        types.add(FeedType.BATCHBOOK2_PHONES);
        types.add(FeedType.BATCHBOOK2_WEBSITES);
        types.add(FeedType.BATCHBOOK2_ADDRESSES);
        types.add(FeedType.BATCHBOOK2_EMAILS);
        return types;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    private transient BatchbookCache cache;

    public BatchbookCache getOrCreateCache(HttpClient httpClient) throws Exception {
        if (cache == null) {
            cache = new BatchbookCache();
            cache.populate(httpClient, this);
        }
        return cache;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        cache = null;
    }
}
