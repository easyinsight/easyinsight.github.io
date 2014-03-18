package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
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
import java.util.Date;

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

    private String applicationId;
    private boolean rebuildFields = true;

    private boolean inlineUsers;

    public boolean isRebuildFields() {
        return rebuildFields;
    }

    public void setRebuildFields(boolean rebuildFields) {
        this.rebuildFields = rebuildFields;
    }

    public boolean isInlineUsers() {
        return inlineUsers;
    }

    public void setInlineUsers(boolean inlineUsers) {
        this.inlineUsers = inlineUsers;
    }

    @Override
    public void beforeSave(EIConnection conn) throws Exception {
        Map<Long, FeedDefinition> childMap = new HashMap<Long, FeedDefinition>();
        for (AnalysisItem analysisItem : getFields()) {
            Key key = analysisItem.getKey();
            //if (key.toBaseKey().indexed()) {
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                FeedDefinition child = childMap.get(derivedKey.getFeedID());
                if (child == null) {
                    child = new FeedStorage().getFeedDefinitionData(derivedKey.getFeedID(), conn);
                    childMap.put(derivedKey.getFeedID(), child);
                }
                for (AnalysisItem item : child.getFields()) {
                    if (item.getKey().getKeyID() == derivedKey.getParentKey().getKeyID()) {
                        NamedKey namedKey = (NamedKey) item.getKey().toBaseKey();
                        namedKey.setIndexed(key.toBaseKey().indexed());
                    }
                }
            }
            //}
        }
        for (FeedDefinition dataSource : childMap.values()) {
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
        }
        super.beforeSave(conn);
    }

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
        return false;
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

    private transient Map<String, String> userCache;

    public Map<String, String> getOrCreateUserCache() {
        if (isInlineUsers()) {
            if (userCache == null) {
                userCache = createUserCache();
            }
            return userCache;
        } else {
            return new HashMap<String, String>();
        }
    }

    @Override
    protected void beforeRefresh(Date lastRefreshTime) {
        super.beforeRefresh(lastRefreshTime);
        //userCache = createUserCache();
    }

    private static final String REQUEST = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken></qdbapi>";



    private Map<String, String> createUserCache() {
        String fullPath = "https://" + host + "/db/" + applicationId;
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", "API_UserRoles");
        BasicHttpEntity entity = new BasicHttpEntity();
        Map<String, String> cache = new HashMap<String, String>();
        try {
            String requestBody;
            requestBody = MessageFormat.format(REQUEST, sessionTicket, applicationToken, "");
            byte[] contentBytes = requestBody.getBytes();
            entity.setContent(new ByteArrayInputStream(contentBytes));
            entity.setContentLength(contentBytes.length);
            httpRequest.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string;
            try {
                string = client.execute(httpRequest, responseHandler);
            } catch (HttpResponseException hre) {
                // let's just ignore this for now
                return new HashMap<String, String>();
            }
            Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));

            Nodes errors = doc.query("/qdbapi/errcode/text()");
            if (errors.size() > 0) {
                Node error = errors.get(0);
                if (!"0".equals(error.getValue())) {
                    String errorDetail = doc.query("/qdbapi/errdetail/text()").get(0).getValue();
                    throw new ReportException(new DataSourceConnectivityReportFault(errorDetail, this));
                }
            }

            Nodes records = doc.query("/qdbapi/users/user");
            for (int i = 0; i < records.size(); i++) {
                Element record = (Element) records.get(i);
                cache.put(getValue("./@id", record), getValue("./name/text()", record));
            }
            return cache;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private static String getValue(String query, Element record) {
        Nodes nodes = record.query(query);
        if(nodes.size() > 0) {
            return nodes.get(0).getValue();
        } else {
            return "";
        }
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        userCache = null;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM QUICKBASE_COMPOSITE_SOURCE where data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUICKBASE_COMPOSITE_SOURCE (DATA_SOURCE_ID, APPLICATION_TOKEN," +
                "SESSION_TICKET, HOST_NAME, qb_username, qb_password, support_index, preserve_credentials, application_id, rebuild_fields, inline_fields) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
        insertStmt.setString(9, applicationId);
        insertStmt.setBoolean(10, rebuildFields);
        insertStmt.setBoolean(11, inlineUsers);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT APPLICATION_TOKEN, SESSION_TICKET, HOST_NAME, qb_username, " +
                "qb_password, support_index, preserve_credentials, application_id, rebuild_fields, inline_fields FROM " +
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
        applicationId = rs.getString(8);
        rebuildFields = rs.getBoolean(9);
        inlineUsers = rs.getBoolean(10);
        queryStmt.close();
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
        String contentString = "<qdbapi>" + requestBody + "</qdbapi>";
        byte[] contentBytes = contentString.getBytes();
        entity.setContent(new ByteArrayInputStream(contentBytes));
        entity.setContentLength(contentBytes.length);
        httpRequest.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String string = client.execute(httpRequest, responseHandler);
        return new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
    }

    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        boolean usersDefined = false;
        if (applicationId != null && !"".equals(applicationId)) {
            for (CompositeFeedNode existing : getCompositeFeedNodes()) {
                if (existing.getDataSourceType() == FeedType.QUICKBASE_USER_CHILD.getType()) {
                    usersDefined = true;
                }
            }

            if (!usersDefined) {
                QuickbaseUserSource source = new QuickbaseUserSource();
                newDefinition(source, conn, "", getUploadPolicy());
                CompositeFeedNode node = new CompositeFeedNode();
                node.setDataFeedID(source.getDataFeedID());
                node.setDataSourceType(source.getFeedType().getType());
                getCompositeFeedNodes().add(node);
                defaultChildren.add(source);
            }
        }


        return defaultChildren;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
