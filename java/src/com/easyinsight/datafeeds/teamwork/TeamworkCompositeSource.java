package com.easyinsight.datafeeds.teamwork;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
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
 * Date: 11/2/14
 * Time: 5:50 PM
 */
public class TeamworkCompositeSource extends CompositeServerDataSource {

    private String teamworkApiKey;
    private String url;

    public TeamworkCompositeSource() {
        setFeedName("Teamwork");
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if(!basecampUrl.endsWith(".teamwork.com"))
            basecampUrl = basecampUrl + ".teamwork.com";
        return basecampUrl;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TEAMWORK WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO TEAMWORK (DATA_SOURCE_ID, TEAMWORK_API_KEY, TEAMWORK_URL) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, getTeamworkApiKey());
        insertStmt.setString(3, getUrl());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TEAMWORK_API_KEY, TEAMWORK_URL FROM TEAMWORK WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setTeamworkApiKey(rs.getString(1));
            setUrl(rs.getString(2));
        }
        queryStmt.close();
    }

    public String getTeamworkApiKey() {
        return teamworkApiKey;
    }

    public void setTeamworkApiKey(String teamworkApiKey) {
        this.teamworkApiKey = teamworkApiKey;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<>();
        types.add(FeedType.TEAMWORK_PROJECT);
        types.add(FeedType.TEAMWORK_TASK);
        types.add(FeedType.TEAMWORK_TASK_LIST);
        types.add(FeedType.TEAMWORK_MILESTONE);
        types.add(FeedType.TEAMWORK_TIME);
        return types;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TEAMWORK_COMPOSITE;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<>();
    }

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.addField("Zendesk URL", "url", "Your Teamwork URL is the browser URL you normally use to connect to Teamwork. For example, if you access Teamwork as yourcompanyname.teamwork.com, put yourcompanyname in as the Teamwork URL.");
        factory.addField("Zendesk API Token:", "teamworkApiKey");
        factory.type(HTMLConnectionFactory.TYPE_BASIC_AUTH);
    }
}
