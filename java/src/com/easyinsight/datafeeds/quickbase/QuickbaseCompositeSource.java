package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.DataSourceInfo;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void exchangeTokens(EIConnection conn) throws Exception {
        if (qbUserName != null && qbPassword != null) {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, qbUserName, qbPassword);
            Document doc = executeRequest("www.quickbase.com", null, "API_Authenticate", requestBody);
            String errorCode = doc.query("/qdbapi/errcode/text()").get(0).getValue();
            if ("0".equals(errorCode)) {
                sessionTicket = doc.query("/qdbapi/ticket/text()").get(0).getValue();
            }
            qbUserName = null;
            qbPassword = null;
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM QUICKBASE_COMPOSITE_SOURCE where data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUICKBASE_COMPOSITE_SOURCE (DATA_SOURCE_ID, APPLICATION_TOKEN," +
                "SESSION_TICKET, HOST_NAME) VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, applicationToken);
        insertStmt.setString(3, sessionTicket);
        insertStmt.setString(4, host);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT APPLICATION_TOKEN, SESSION_TICKET, HOST_NAME FROM " +
                "QUICKBASE_COMPOSITE_SOURCE where data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        applicationToken = rs.getString(1);
        sessionTicket = rs.getString(2);
        host = rs.getString(3);
    }

    @Override
    public String validateCredentials() {
        try {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, qbUserName, qbPassword);
            Document doc = executeRequest("www.quickbase.com", null, "API_Authenticate", requestBody);
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

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
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
