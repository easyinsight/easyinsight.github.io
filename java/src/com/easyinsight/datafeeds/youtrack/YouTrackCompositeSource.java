package com.easyinsight.datafeeds.youtrack;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.http.HttpServletRequest;
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
public class YouTrackCompositeSource extends CompositeServerDataSource {

    private String ytUserName;
    private String ytPassword;

    private String cookie;
    private String url;

    public YouTrackCompositeSource() {
        setFeedName("YouTrack");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getYtPassword() {
        return ytPassword;
    }

    public void setYtPassword(String ytPassword) {
        this.ytPassword = ytPassword;
    }

    public String getYtUserName() {
        return ytUserName;
    }

    public void setYtUserName(String ytUserName) {
        this.ytUserName = ytUserName;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        super.exchangeTokens(conn, request, externalPin);
        if (ytUserName != null && !"".equals(ytUserName) && ytPassword != null && !"".equals(ytPassword)) {
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(url+"/rest/user/login?login="+ytUserName+"&password="+ytPassword);
            httpClient.executeMethod(postMethod);
            cookie = postMethod.getResponseHeader("Set-Cookie").getValue();
            ytUserName = null;
            ytPassword = null;
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM YOUTRACK_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO YOUTRACK_SOURCE (URL, COOKIE, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insertStmt.setString(1, url);
        insertStmt.setString(2, cookie);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT URL, COOKIE FROM YOUTRACK_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            cookie = rs.getString(2);
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
        return FeedType.YOUTRACK_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.YOUTRACK_PROJECTS);
        types.add(FeedType.YOUTRACK_TASKS);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.YOUTRACK_PROJECTS, FeedType.YOUTRACK_TASKS, YouTrackProjectSource.PROJECT_ID, YouTrackIssueSource.PROJECT));
    }
}
