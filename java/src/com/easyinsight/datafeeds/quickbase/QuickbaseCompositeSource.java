package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.CompositeFeedConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.logging.LogClass;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/16/10
 * Time: 12:30 PM
 */
public class QuickbaseCompositeSource extends CompositeServerDataSource {

    private static final String AUTHENTICATE_XML = "<username>{0}</username><password>{1}</password><hours>144</hours>";

    private String applicationToken;
    private String sessionTicket;
    private String host;

    private String qbUserName;
    private String qbPassword;

    private boolean supportIndex;
    private boolean preserveCredentials;

    @Override
    public boolean fullNightlyRefresh() {
        return true;
    }

    public boolean isPreserveCredentials() {
        return preserveCredentials;
    }

    public void setPreserveCredentials(boolean preserveCredentials) {
        this.preserveCredentials = preserveCredentials;
    }

    public boolean isSupportIndex() {
        return supportIndex;
    }

    public void setSupportIndex(boolean supportIndex) {
        this.supportIndex = supportIndex;
    }

    public String getQbUserName() {
        return qbUserName;
    }

    public void setQbUserName(String qbUserName) {
        this.qbUserName = qbUserName;
    }

    public String getQbPassword() {
        return qbPassword;
    }

    public void setQbPassword(String qbPassword) {
        this.qbPassword = qbPassword;
    }

    @Override
    public boolean checkDateTime(String name, Key key) {
        return true;
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        if (qbUserName != null && qbPassword != null) {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, qbUserName, qbPassword);
            Document doc = executeRequest(host, null, "API_Authenticate", requestBody);
            String errorCode = doc.query("/qdbapi/errcode/text()").get(0).getValue();
            if ("0".equals(errorCode)) {
                sessionTicket = doc.query("/qdbapi/ticket/text()").get(0).getValue();
            }
            if (!preserveCredentials) {
                qbUserName = null;
                qbPassword = null;
            }
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM QUICKBASE_COMPOSITE_SOURCE where data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUICKBASE_COMPOSITE_SOURCE (DATA_SOURCE_ID, APPLICATION_TOKEN," +
                "SESSION_TICKET, HOST_NAME, qb_username, qb_password, support_index, preserve_credentials) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, applicationToken);
        insertStmt.setString(3, sessionTicket);
        insertStmt.setString(4, host);
        if (preserveCredentials) {
            insertStmt.setString(5, qbUserName);
            insertStmt.setString(6, qbPassword != null ? PasswordStorage.encryptString(qbPassword) : null);
        } else {
            insertStmt.setNull(5, Types.VARCHAR);
            insertStmt.setNull(6, Types.VARCHAR);
        }
        insertStmt.setBoolean(7, supportIndex);
        insertStmt.setBoolean(8, preserveCredentials);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT APPLICATION_TOKEN, SESSION_TICKET, HOST_NAME, qb_username, qb_password, support_index, preserve_credentials FROM " +
                "QUICKBASE_COMPOSITE_SOURCE where data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        applicationToken = rs.getString(1);
        sessionTicket = rs.getString(2);
        host = rs.getString(3);
        supportIndex = rs.getBoolean(6);
        preserveCredentials = rs.getBoolean(7);
        if (preserveCredentials) {
            qbUserName = rs.getString(4);
            String encryptedQBPassword = rs.getString(5);
            if (!rs.wasNull()) {
                qbPassword = PasswordStorage.decryptString(encryptedQBPassword);
            }
        }
    }

    @Override
    public String validateCredentials() {
        try {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, qbUserName, qbPassword);
            Document doc = executeRequest(host, null, "API_Authenticate", requestBody);
            String errorCode = doc.query("/qdbapi/errcode/text()").get(0).getValue();
            if ("0".equals(errorCode)) {
                return null;
            } else {
                return doc.query("/qdbapi/errdetail/text()").get(0).getValue();
            }
        } catch (Exception e) {
            LogClass.error(e);
            return e.getMessage();
        }
    }

    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
    }

    public String getSessionTicket() {
        return sessionTicket;
    }

    public void setSessionTicket(String sessionTicket) {
        this.sessionTicket = sessionTicket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        return new HashSet<FeedType>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.QUICKBASE_COMPOSITE;
    }

    @Override
    public int getDataSourceType() {
        if (supportIndex) {
            return DataSourceInfo.COMPOSITE_PULL;
        } else {
            return DataSourceInfo.LIVE;
        }
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    @Override
    public List<CompositeFeedConnection> obtainChildConnections() throws SQLException {
        return getConnections();
    }

    private Document executeRequest(String host, String path, String action, String requestBody) throws IOException, ParsingException {
        if (path == null) {
            path = "main";
        }
        String fullPath = "https://" + host + "/db/" + path;
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", action);
        BasicHttpEntity entity = new BasicHttpEntity();
        String contentString = "<qdbapi>"+requestBody+"</qdbapi>";
        byte[] contentBytes = contentString.getBytes();
        entity.setContent(new ByteArrayInputStream(contentBytes));
        entity.setContentLength(contentBytes.length);
        httpRequest.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String string = client.execute(httpRequest, responseHandler);
        return new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
    }
}
