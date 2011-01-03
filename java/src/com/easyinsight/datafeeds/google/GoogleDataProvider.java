package com.easyinsight.datafeeds.google;

import com.cleardb.app.Client;
import com.easyinsight.analysis.*;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.cleardb.ClearDBCompositeSource;
import com.easyinsight.datafeeds.cleardb.ClearDBDataSource;
import com.easyinsight.datafeeds.quickbase.QuickbaseCompositeSource;
import com.easyinsight.datafeeds.quickbase.QuickbaseDatabaseSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.database.Database;
import com.easyinsight.users.Account;
import com.easyinsight.userupload.UploadPolicy;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import flex.messaging.FlexContext;
import nu.xom.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jboe
 * Date: Jan 6, 2008
 * Time: 7:44:31 PM
 */
public class GoogleDataProvider {

    private Map<String, List<Spreadsheet>> cachedSpreadsheetResults = new WeakHashMap<String, List<Spreadsheet>>();
    
    private static GoogleDataProvider instance = null;

    public GoogleDataProvider() {
        instance = this;
    }

    public static GoogleDataProvider instance() {
        return instance;
    }

    public GoogleSpreadsheetResponse registerToken(String verifier) {
        EIConnection conn = Database.instance().getConnection();
        try {
            OAuthConsumer consumer = (OAuthConsumer) FlexContext.getHttpRequest().getSession().getAttribute("oauthConsumer");
            OAuthProvider provider = (OAuthProvider) FlexContext.getHttpRequest().getSession().getAttribute("oauthProvider");

            provider.retrieveAccessToken(consumer, verifier.trim());
            String tokenKey = consumer.getToken();
            String tokenSecret = consumer.getTokenSecret();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO GOOGLE_DOCS_TOKEN (TOKEN_KEY, TOKEN_SECRET, USER_ID) VALUES (?, ?, ?)");
            insertStmt.setString(1, tokenKey);
            insertStmt.setString(2, tokenSecret);
            insertStmt.setLong(3, SecurityUtil.getUserID());
            insertStmt.execute();
            return getAvailableGoogleSpreadsheets(conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public GoogleSpreadsheetResponse getAvailableGoogleSpreadsheets() {
        EIConnection conn = Database.instance().getConnection();
        try {
            return getAvailableGoogleSpreadsheets(conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private GoogleSpreadsheetResponse getAvailableGoogleSpreadsheets(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GOOGLE_DOCS_TOKEN.token_key, GOOGLE_DOCS_TOKEN.token_secret FROM " +
                "GOOGLE_DOCS_TOKEN WHERE GOOGLE_DOCS_TOKEN.user_id = ?");
        queryStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            List<Spreadsheet> spreadsheets = getSpreadsheets(rs.getString(1), rs.getString(2));
            return new GoogleSpreadsheetResponse(spreadsheets, true);
        } else {
            return new GoogleSpreadsheetResponse(false);
        }
    }

    private List<Spreadsheet> getSpreadsheets(String tokenKey, String tokenSecret) {
        List<Spreadsheet> worksheets = new ArrayList<Spreadsheet>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement existsStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, WORKSHEETURL FROM GOOGLE_FEED, DATA_FEED, UPLOAD_POLICY_USERS " +
                    "WHERE GOOGLE_FEED.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND UPLOAD_POLICY_USERS.FEED_ID = " +
                    "UPLOAD_POLICY_USERS.FEED_ID AND USER_ID = ? AND UPLOAD_POLICY_USERS.USER_ID = ?");
            existsStmt.setLong(1, SecurityUtil.getUserID());
            existsStmt.setInt(2, Roles.OWNER);
            ResultSet rs = existsStmt.executeQuery();
            Map<String, FeedDescriptor> worksheetToFeedMap = new HashMap<String, FeedDescriptor>();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String worksheetURL = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setId(dataFeedID);
                worksheetToFeedMap.put(worksheetURL, feedDescriptor);
            }
            existsStmt.close();            
            URL feedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(tokenKey, tokenSecret);
            SpreadsheetFeed spreadsheetFeed = myService.getFeed(feedUrl, SpreadsheetFeed.class);
            for (SpreadsheetEntry entry : spreadsheetFeed.getEntries()) {
                try {
                    List<WorksheetEntry> worksheetEntries = entry.getWorksheets();
                    List<Worksheet> worksheetList = new ArrayList<Worksheet>();
                    for (WorksheetEntry worksheetEntry : worksheetEntries) {
                        String title = worksheetEntry.getTitle().getPlainText();
                        Worksheet worksheet = new Worksheet();
                        worksheet.setSpreadsheet(entry.getTitle().getPlainText());
                        worksheet.setTitle(title);
                        String url = worksheetEntry.getListFeedUrl().toString();
                        worksheet.setUrl(url);
                        worksheet.setFeedDescriptor(worksheetToFeedMap.get(url));
                        worksheetList.add(worksheet);
                    }
                    Spreadsheet spreadsheet = new Spreadsheet();
                    spreadsheet.setTitle(entry.getTitle().getPlainText());
                    spreadsheet.setChildren(worksheetList);
                    worksheets.add(spreadsheet);
                } catch (Exception e) {
                    LogClass.error(e);
                    LogClass.debug("Skipping over spreadsheet");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);        
        } finally {
            Database.closeConnection(conn);
        }
        return worksheets;
    }

    private static final String AUTHENTICATE_XML = "<username>{0}</username><password>{1}</password><hours>24</hours>";
    private static final String GET_APPLICATIONS_XML = "<ticket>{0}</ticket>";
    private static final String GET_SCHEMA_XML = "<ticket>{0}</ticket><apptoken>{1}</apptoken>";

    public EIDescriptor createClearDBDataSource(String apiKey, String appID) {
        SecurityUtil.authorizeAccountTier(Account.BASIC);
        EIConnection conn = Database.instance().getConnection();
        try {
            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            ClearDBCompositeSource clearDBCompositeSource = new ClearDBCompositeSource();
            clearDBCompositeSource.setFeedName("ClearDB");
            clearDBCompositeSource.setCdApiKey(apiKey);
            clearDBCompositeSource.setAppToken(appID);
            clearDBCompositeSource.setUploadPolicy(uploadPolicy);
            new FeedCreation().createFeed(clearDBCompositeSource, conn, new DataSet(), uploadPolicy);
            Client client = new Client(apiKey, appID);
            JSONObject result = client.query("SHOW TABLES");
            JSONArray tables = result.getJSONArray("response");
            Map<String, String> tableSet = new HashMap<String, String>();
            for (int i = 0; i < tables.length(); i++) {
                JSONObject tablePair = (JSONObject) tables.get(i);
                String value = (String) tablePair.get((String) tablePair.keys().next());
                System.out.println(value);
                if (value != null && !"".equals(value.trim())) {
                    tableSet.put(value, (String) tablePair.keys().next());
                }
            }
            List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();
            for (String table : tableSet.keySet()) {
                ClearDBDataSource clearDBDataSource = new ClearDBDataSource();
                clearDBDataSource.setFeedName(table);
                clearDBDataSource.setVisible(false);
                clearDBDataSource.setParentSourceID(clearDBCompositeSource.getDataFeedID());
                clearDBDataSource.setTableName(table);
                String command = "DESCRIBE " + table;
                System.out.println(command);
                JSONObject description = client.query(command);
                List<AnalysisItem> fieldList = new ArrayList<AnalysisItem>();
                JSONArray fields = description.getJSONArray("response");
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject field = (JSONObject) fields.get(i);
                    String fieldName = (String) field.get("Field");
                    String type = (String) field.get("Type");
                    if (type.indexOf("char") != -1 || type.indexOf("text") != -1) {
                        fieldList.add(new AnalysisDimension(fieldName, true));
                    } else if (type.indexOf("date") != -1 || type.indexOf("timestamp") != -1) {
                        fieldList.add(new AnalysisDateDimension(fieldName,  true, AnalysisDateDimension.DAY_LEVEL));
                    } else if (type.indexOf("int") != -1 || type.indexOf("double") != -1 || type.indexOf("float") != -1 ||
                            type.indexOf("numeric") != -1) {
                        fieldList.add(new AnalysisMeasure(fieldName, AggregationTypes.SUM));
                    }
                }
                clearDBDataSource.setFields(fieldList);
                System.out.println(description.toString());
                UploadPolicy childPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
                clearDBDataSource.setUploadPolicy(childPolicy);
                new FeedCreation().createFeed(clearDBDataSource, conn, new DataSet(), childPolicy);
                CompositeFeedNode node = new CompositeFeedNode(clearDBDataSource.getDataFeedID(), 0, 0);
                nodes.add(node);
            }
            clearDBCompositeSource.setCompositeFeedNodes(nodes);
            clearDBCompositeSource.populateFields(conn);

            new FeedStorage().updateDataFeedConfiguration(clearDBCompositeSource, conn);
            return new DataSourceDescriptor(clearDBCompositeSource.getFeedName(), clearDBCompositeSource.getDataFeedID(),
                    clearDBCompositeSource.getFeedType().getType());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public QuickbaseResponse authenticate(String userName, String password) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        try {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, userName, password);
            Document doc = executeRequest("www.quickbase.com", null, "API_Authenticate", requestBody);
            String errorCode = doc.query("/qdbapi/errcode/text()").get(0).getValue();
            if ("0".equals(errorCode)) {
                String ticket = doc.query("/qdbapi/ticket/text()").get(0).getValue();
                QuickbaseResponse quickbaseResponse = new QuickbaseResponse();
                quickbaseResponse.setSessionTicket(ticket);
                return quickbaseResponse;
            } else {
                String errorText = doc.query("/qdbapi/errtext/text()").get(0).getValue();
                QuickbaseResponse quickbaseResponse = new QuickbaseResponse();
                quickbaseResponse.setErrorMessage(errorText);
                return quickbaseResponse;
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public EIDescriptor createDataSource(String sessionTicket) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        EIConnection conn = Database.instance().getConnection();
        try {
            String applicationToken = "txxe32vjyuwicxbjpnpdwjatqb";
            String requestBody = MessageFormat.format(GET_APPLICATIONS_XML, sessionTicket);
            Document doc = executeRequest("www.quickbase.com", null, "API_GrantedDBs", requestBody);
            Nodes databases = doc.query("/qdbapi/databases/dbinfo");
            List<Connection> connections = new ArrayList<Connection>();
            QuickbaseCompositeSource quickbaseCompositeSource = new QuickbaseCompositeSource();
            quickbaseCompositeSource.setHost("www.quickbase.com");
            quickbaseCompositeSource.setSessionTicket(sessionTicket);
            quickbaseCompositeSource.setApplicationToken(applicationToken);
            quickbaseCompositeSource.setFeedName("Quickbase");

            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            quickbaseCompositeSource.setUploadPolicy(uploadPolicy);
            new FeedCreation().createFeed(quickbaseCompositeSource, conn, new DataSet(), uploadPolicy);

            List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();

            Map<String, QuickbaseDatabaseSource> map = new HashMap<String, QuickbaseDatabaseSource>();
            for (int i = 0; i < databases.size(); i++) {
                Node database = databases.get(i);
                QuickbaseDatabaseSource quickbaseDatabaseSource = createDataSource(sessionTicket, applicationToken, database, connections);
                quickbaseDatabaseSource.setVisible(false);
                map.put(quickbaseDatabaseSource.getDatabaseID(), quickbaseDatabaseSource);
                quickbaseDatabaseSource.setParentSourceID(quickbaseCompositeSource.getDataFeedID());
                UploadPolicy childPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
                quickbaseDatabaseSource.setUploadPolicy(childPolicy);
                new FeedCreation().createFeed(quickbaseDatabaseSource, conn, new DataSet(), childPolicy);
                CompositeFeedNode node = new CompositeFeedNode(quickbaseDatabaseSource.getDataFeedID(), 0, 0);
                nodes.add(node);
            }

            quickbaseCompositeSource.setCompositeFeedNodes(nodes);

            List<CompositeFeedConnection> compositeFeedConnectionList = new ArrayList<CompositeFeedConnection>();
            for (Connection connection : connections) {
                QuickbaseDatabaseSource source = map.get(connection.sourceDatabaseID);
                QuickbaseDatabaseSource target = map.get(connection.targetDatabaseID);
                Key sourceKey = null;
                for (AnalysisItem field : source.getFields()) {
                    Key key = field.getKey();
                    if (key.toKeyString().split("\\.")[1].equals(connection.sourceDatabaseFieldID)) {
                        sourceKey = key;
                    }
                }
                Key targetKey = null;
                for (AnalysisItem field : target.getFields()) {
                    Key key = field.getKey();
                    if (key.toKeyString().split("\\.")[1].equals(connection.targetDatabaseFieldID)) {
                        targetKey = key;
                    }
                }
                if (sourceKey == null) {
                    throw new RuntimeException("Couldn't find " + connection.sourceDatabaseFieldID + " on " + source.getFeedName());
                }
                if (targetKey == null) {
                    throw new RuntimeException("Couldn't find " + connection.targetDatabaseFieldID + " on " + target.getFeedName());
                }
                compositeFeedConnectionList.add(new CompositeFeedConnection(source.getDataFeedID(), target.getDataFeedID(), sourceKey, targetKey));
            }
            quickbaseCompositeSource.setConnections(compositeFeedConnectionList);
            quickbaseCompositeSource.populateFields(conn);

            new FeedStorage().updateDataFeedConfiguration(quickbaseCompositeSource, conn);

            return new DataSourceDescriptor(quickbaseCompositeSource.getFeedName(), quickbaseCompositeSource.getDataFeedID(),
                    quickbaseCompositeSource.getFeedType().getType());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private QuickbaseDatabaseSource createDataSource(String sessionTicket, String applicationToken, Node database, List<Connection> connections) throws IOException, ParsingException {
        QuickbaseDatabaseSource quickbaseDatabaseSource = new QuickbaseDatabaseSource();


        String databaseID = database.query("dbid/text()").get(0).getValue();
        quickbaseDatabaseSource.setDatabaseID(databaseID);

        List<AnalysisItem> items = new ArrayList<AnalysisItem>();

        String tableName = getSchema(sessionTicket, applicationToken, databaseID, items, connections);
        quickbaseDatabaseSource.setFeedName(tableName);

        quickbaseDatabaseSource.setFields(items);

        return quickbaseDatabaseSource;
    }

    private String getSchema(String sessionTicket, String applicationToken, String databaseID, List<AnalysisItem> items, List<Connection> connections) throws IOException, ParsingException {
        String schemaRequest = MessageFormat.format(GET_SCHEMA_XML, sessionTicket, applicationToken);
        Document schemaDoc = executeRequest("www.quickbase.com", databaseID, "API_GetSchema", schemaRequest);
        String tableName = schemaDoc.query("/qdbapi/table/name/text()").get(0).getValue();
        Nodes fields = schemaDoc.query("/qdbapi/table/fields/field");
        // determine URLs


        for (int j = 0; j < fields.size(); j++) {
            Element field = (Element) fields.get(j);
            String fieldID = field.getAttribute("id").getValue();
            String keyField = databaseID + "." + fieldID;
            NamedKey namedKey = new NamedKey(keyField);
            String fieldType = field.getAttribute("field_type").getValue();
            Attribute attributeMode = field.getAttribute("mode");
            if (attributeMode != null && "lookup".equals(attributeMode.getValue())) {
                continue;
            }
            String label = field.query("label/text()").get(0).getValue();
            if ("text".equals(fieldType) || "checkbox".equals(fieldType) || "phone".equals(fieldType) ||
                    "userid".equals(fieldType)) {
                items.add(new AnalysisDimension(namedKey, label));
            } else if ("recordid".equals(fieldType)) {
                AnalysisDimension dim = new AnalysisDimension(namedKey, label);
                dim.setHidden(true);
                items.add(dim);
            } else if ("url".equals(fieldType)) {
                AnalysisDimension dim = new AnalysisDimension(namedKey, label);
                items.add(dim);
            } else if ("dblink".equals(fieldType)) {
                String targetDatabaseID = field.query("target_dbid/text()").get(0).getValue();
                String targetFieldID = field.query("target_fid/text()").get(0).getValue();
                String sourceFieldID = field.query("source_fid/text()").get(0).getValue();
                connections.add(new Connection(databaseID, sourceFieldID, targetDatabaseID, targetFieldID));
            } else if ("date".equals(fieldType) || "timestamp".equals(fieldType)) {
                items.add(new AnalysisDateDimension(namedKey, label, AnalysisDateDimension.DAY_LEVEL));
            } else if ("currency".equals(fieldType)) {
                items.add(new AnalysisMeasure(namedKey, label, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
            } else if ("duration".equals(fieldType)) {
                items.add(new AnalysisMeasure(namedKey, label, AggregationTypes.SUM, true, FormattingConfiguration.MILLISECONDS));
            } else if ("float".equals(fieldType)) {
                items.add(new AnalysisMeasure(namedKey, label, AggregationTypes.SUM));
            }
        }

        return tableName;
    }

    private static class Connection {
        String sourceDatabaseID;
        String sourceDatabaseFieldID;
        String targetDatabaseID;
        String targetDatabaseFieldID;

        private Connection(String sourceDatabaseID, String sourceDatabaseFieldID, String targetDatabaseID, String targetDatabaseFieldID) {
            this.sourceDatabaseID = sourceDatabaseID;
            this.sourceDatabaseFieldID = sourceDatabaseFieldID;
            this.targetDatabaseID = targetDatabaseID;
            this.targetDatabaseFieldID = targetDatabaseFieldID;
        }

        @Override
        public String toString() {
            return "Connection{" +
                    "sourceDatabaseID='" + sourceDatabaseID + '\'' +
                    ", sourceDatabaseFieldID='" + sourceDatabaseFieldID + '\'' +
                    ", targetDatabaseID='" + targetDatabaseID + '\'' +
                    ", targetDatabaseFieldID='" + targetDatabaseFieldID + '\'' +
                    '}';
        }
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
