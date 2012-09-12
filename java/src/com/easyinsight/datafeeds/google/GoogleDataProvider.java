package com.easyinsight.datafeeds.google;

import com.cleardb.app.ClearDBQueryException;
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
import com.easyinsight.datafeeds.cleardb.ClearDBResponse;
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
import com.google.gdata.util.AuthenticationException;
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
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GOOGLE_DOCS_TOKEN WHERE USER_ID = ?");
            clearStmt.setLong(1, SecurityUtil.getUserID());
            clearStmt.executeUpdate();
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
            try {
                List<Spreadsheet> spreadsheets = getSpreadsheets(rs.getString(1), rs.getString(2));
                return new GoogleSpreadsheetResponse(spreadsheets, true);
            } catch (AuthenticationException ae) {
                return new GoogleSpreadsheetResponse(false);
            }
        } else {
            return new GoogleSpreadsheetResponse(false);
        }
    }

    private List<Spreadsheet> getSpreadsheets(String tokenKey, String tokenSecret) throws AuthenticationException {
        List<Spreadsheet> worksheets = new ArrayList<Spreadsheet>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement existsStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, WORKSHEETURL FROM GOOGLE_FEED, DATA_FEED, UPLOAD_POLICY_USERS " +
                    "WHERE GOOGLE_FEED.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND UPLOAD_POLICY_USERS.FEED_ID = " +
                    "UPLOAD_POLICY_USERS.FEED_ID AND USER_ID = ? AND UPLOAD_POLICY_USERS.USER_ID = ?");
            existsStmt.setLong(1, SecurityUtil.getUserID());
            existsStmt.setInt(2, Roles.OWNER);
            ResultSet rs = existsStmt.executeQuery();
            Map<String, DataSourceDescriptor> worksheetToFeedMap = new HashMap<String, DataSourceDescriptor>();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String worksheetURL = rs.getString(2);
                DataSourceDescriptor feedDescriptor = new DataSourceDescriptor();
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
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);        
        } finally {
            Database.closeConnection(conn);
        }
        return worksheets;
    }

    private static final String AUTHENTICATE_XML = "<username>{0}</username><password>{1}</password><hours>144</hours>";
    private static final String GET_APPLICATIONS_XML = "<ticket>{0}</ticket><withembeddedtables>0</withembeddedtables>";
    private static final String GET_DATABASES_XML = "<ticket>{0}</ticket><excludeparents>1</excludeparents>";
    private static final String GET_SCHEMA_XML = "<ticket>{0}</ticket><apptoken>{1}</apptoken>";

    public ClearDBResponse createClearDBDataSource(String apiKey, String appID, boolean accountVisible) {
        SecurityUtil.authorizeAccountTier(Account.BASIC);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            ClearDBCompositeSource clearDBCompositeSource = new ClearDBCompositeSource();
            clearDBCompositeSource.setFeedName("ClearDB");
            clearDBCompositeSource.setCdApiKey(apiKey);
            clearDBCompositeSource.setAppToken(appID);
            clearDBCompositeSource.setUploadPolicy(uploadPolicy);
            new FeedCreation().createFeed(clearDBCompositeSource, conn, new DataSet(), uploadPolicy);
            Client client = new Client(apiKey, appID);
            Map<String, String> tableSet = null;
            JSONObject result = client.query("SHOW TABLES");
            JSONArray tables = result.getJSONArray("response");
            tableSet = new HashMap<String, String>();
            for (int i = 0; i < tables.length(); i++) {
                JSONObject tablePair = (JSONObject) tables.get(i);
                String value = (String) tablePair.get((String) tablePair.keys().next());
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
                UploadPolicy childPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
                clearDBDataSource.setUploadPolicy(childPolicy);
                new FeedCreation().createFeed(clearDBDataSource, conn, new DataSet(), childPolicy);
                CompositeFeedNode node = new CompositeFeedNode(clearDBDataSource.getDataFeedID(), 0, 0, clearDBDataSource.getFeedName(), clearDBDataSource.getFeedType().getType(),
                        clearDBDataSource.getDataSourceBehavior());
                nodes.add(node);
            }
            clearDBCompositeSource.setCompositeFeedNodes(nodes);
            clearDBCompositeSource.populateFields(conn);

            new FeedStorage().updateDataFeedConfiguration(clearDBCompositeSource, conn);
            ClearDBResponse response = new ClearDBResponse();
            response.setSuccessful(true);
            response.setEiDescriptor(new DataSourceDescriptor(clearDBCompositeSource.getFeedName(), clearDBCompositeSource.getDataFeedID(),
                    clearDBCompositeSource.getFeedType().getType(), false, clearDBCompositeSource.getDataSourceBehavior()));
            conn.commit();
            return response;
        } catch (ClearDBQueryException e) {
            conn.rollback();
            ClearDBResponse response = new ClearDBResponse();
            response.setSuccessful(false);
            if (e.getMessage().indexOf("Request failed security check") != -1) {
                response.setErrorMessage("The rules on this API key in ClearDB are insufficient for Easy Insight to create the data source. You need to adjust the rules on the API key to allow for READ operations on the database.");
            } else {
                response.setErrorMessage(e.getMessage());
            }
            return response;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public QuickbaseResponse authenticate(String userName, String password, String host) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        try {
            String requestBody = MessageFormat.format(AUTHENTICATE_XML, userName, password);
            Document doc = executeRequest(host, null, "API_Authenticate", requestBody);
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

    public List<String> getApplications(String sessionTicket, String host) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        EIConnection conn = Database.instance().getConnection();
        try {
            String requestBody = MessageFormat.format(GET_APPLICATIONS_XML, sessionTicket);
            Document doc = executeRequest(host, null, "API_GrantedDBs", requestBody);
            Nodes databases = doc.query("/qdbapi/databases/dbinfo/dbname/text()");
            List<String> databaseNames = new ArrayList<String>();
            for (int i = 0; i < databases.size(); i++) {
                databaseNames.add(databases.get(i).getValue());
            }
            return databaseNames;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public EIDescriptor createDataSource(String sessionTicket, String applicationToken, List<String> appNames, String host, boolean accountVisible,
                                         String userName, String password, boolean supportIndex, boolean saveCredentials) {
        SecurityUtil.authorizeAccountTier(Account.PROFESSIONAL);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            //String applicationToken = "txxe32vjyuwicxbjpnpdwjatqb";
            String requestBody = MessageFormat.format(GET_DATABASES_XML, sessionTicket);
            Document doc = executeRequest(host, null, "API_GrantedDBs", requestBody);
            Nodes databases = doc.query("/qdbapi/databases/dbinfo");
            List<Connection> connections = new ArrayList<Connection>();
            QuickbaseCompositeSource quickbaseCompositeSource = new QuickbaseCompositeSource();
            quickbaseCompositeSource.setAccountVisible(accountVisible);
            quickbaseCompositeSource.setHost(host);
            quickbaseCompositeSource.setSessionTicket(sessionTicket);
            quickbaseCompositeSource.setApplicationToken(applicationToken);
            quickbaseCompositeSource.setFeedName(appNames.get(0));
            quickbaseCompositeSource.setPreserveCredentials(saveCredentials);
            quickbaseCompositeSource.setSupportIndex(supportIndex);
            if (saveCredentials) {
                quickbaseCompositeSource.setQbUserName(userName);
                quickbaseCompositeSource.setQbPassword(password);
            }

            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            quickbaseCompositeSource.setUploadPolicy(uploadPolicy);
            new FeedCreation().createFeed(quickbaseCompositeSource, conn, new DataSet(), uploadPolicy);

            List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();

            Map<String, QuickbaseDatabaseSource> map = new HashMap<String, QuickbaseDatabaseSource>();
            for (int i = 0; i < databases.size(); i++) {
                Node database = databases.get(i);
                String dbName = database.query("dbname/text()").get(0).getValue();
                if (dbName.indexOf(":") != 1) {
                    String appName = dbName.split(":")[0].trim();
                    if (appNames.contains(appName)) {
                        QuickbaseDatabaseSource quickbaseDatabaseSource = createDataSource(sessionTicket, applicationToken, database, connections, host);
                        if (quickbaseDatabaseSource == null) {
                            continue;
                        }
                        quickbaseDatabaseSource.setVisible(false);
                        quickbaseDatabaseSource.setIndexEnabled(supportIndex);
                        map.put(quickbaseDatabaseSource.getDatabaseID(), quickbaseDatabaseSource);
                        quickbaseDatabaseSource.setParentSourceID(quickbaseCompositeSource.getDataFeedID());
                        UploadPolicy childPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
                        quickbaseDatabaseSource.setUploadPolicy(childPolicy);
                        FeedCreationResult result = new FeedCreation().createFeed(quickbaseDatabaseSource, conn, new DataSet(), childPolicy);
                        result.getTableDefinitionMetadata().commit();
                        result.getTableDefinitionMetadata().closeConnection();
                        CompositeFeedNode node = new CompositeFeedNode(quickbaseDatabaseSource.getDataFeedID(), 0, 0, quickbaseDatabaseSource.getFeedName(),
                                quickbaseDatabaseSource.getFeedType().getType(), quickbaseDatabaseSource.getDataSourceBehavior());
                        nodes.add(node);
                    }
                }
            }

            quickbaseCompositeSource.setCompositeFeedNodes(nodes);

            List<CompositeFeedConnection> compositeFeedConnectionList = new ArrayList<CompositeFeedConnection>();
            for (Connection connection : connections) {
                QuickbaseDatabaseSource source = map.get(connection.sourceDatabaseID);
                QuickbaseDatabaseSource target = map.get(connection.targetDatabaseID);
                if (source != null && target != null) {
                    Key sourceKey = null;
                    for (AnalysisItem field : source.getFields()) {
                        Key key = field.getKey();
                        String[] tokens = key.toKeyString().split("\\.");
                        if (tokens.length > 1 && tokens[1].equals(connection.sourceDatabaseFieldID)) {
                            sourceKey = key;
                        }
                    }
                    Key targetKey = null;
                    for (AnalysisItem field : target.getFields()) {
                        Key key = field.getKey();
                        String[] tokens = key.toKeyString().split("\\.");
                        if (tokens.length > 1 && tokens[1].equals(connection.targetDatabaseFieldID)) {
                            targetKey = key;
                        }
                    }
                    if (sourceKey == null) {
                        continue;
                        //throw new RuntimeException("Couldn't find " + connection.sourceDatabaseFieldID + " on " + source.getFeedName());
                    }
                    if (targetKey == null) {
                        continue;
                        //throw new RuntimeException("Couldn't find " + connection.targetDatabaseFieldID + " on " + target.getFeedName());
                    }
                    if (source.getDataFeedID() == target.getDataFeedID()) {
                        continue;
                    }
                    compositeFeedConnectionList.add(new CompositeFeedConnection(source.getDataFeedID(), target.getDataFeedID(), sourceKey, targetKey, source.getFeedName(),
                            target.getFeedName(), false, false, false, false));
                }
            }
            quickbaseCompositeSource.setConnections(compositeFeedConnectionList);
            quickbaseCompositeSource.populateFields(conn);

            new FeedStorage().updateDataFeedConfiguration(quickbaseCompositeSource, conn);

            conn.commit();

            return new DataSourceDescriptor(quickbaseCompositeSource.getFeedName(), quickbaseCompositeSource.getDataFeedID(),
                    quickbaseCompositeSource.getFeedType().getType(), false, quickbaseCompositeSource.getDataSourceBehavior());
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private QuickbaseDatabaseSource createDataSource(String sessionTicket, String applicationToken, Node database, List<Connection> connections, String host) throws IOException, ParsingException {
        QuickbaseDatabaseSource quickbaseDatabaseSource = new QuickbaseDatabaseSource();


        String databaseID = database.query("dbid/text()").get(0).getValue();
        quickbaseDatabaseSource.setDatabaseID(databaseID);

        List<AnalysisItem> items = new ArrayList<AnalysisItem>();

        String tableName;
        try {
            tableName = getSchema(sessionTicket, applicationToken, databaseID, items, connections, host, new HashMap<String, Key>());
        } catch (QuickbaseSetupException e) {
            return null;
        }
        if (tableName == null) {
            return null;
        }
        quickbaseDatabaseSource.setFeedName(tableName);

        quickbaseDatabaseSource.setFields(items);

        return quickbaseDatabaseSource;
    }

    public static String getSchema(String sessionTicket, String applicationToken, String databaseID, List<AnalysisItem> items, List<Connection> connections, String host, Map<String, Key> keyMap) throws IOException, ParsingException, QuickbaseSetupException {
        String schemaRequest = MessageFormat.format(GET_SCHEMA_XML, sessionTicket, applicationToken);
        Document schemaDoc = executeRequest(host, databaseID, "API_GetSchema", schemaRequest);
        Nodes tableNameNodes = schemaDoc.query("/qdbapi/table/name/text()");
        Nodes errors = schemaDoc.query("/qdbapi/errcode/text()");
        if (errors.size() > 0) {
            Node error = errors.get(0);
            if (!"0".equals(error.getValue())) {
                String errorDetail = schemaDoc.query("/qdbapi/errdetail/text()").get(0).getValue();
                throw new QuickbaseSetupException(errorDetail);
            }
        }
        if (tableNameNodes.size() == 0) {
            return null;
        }
        String tableName = tableNameNodes.get(0).getValue();
        Nodes fields = schemaDoc.query("/qdbapi/table/fields/field");
        // determine URLs


        for (int j = 0; j < fields.size(); j++) {
            Element field = (Element) fields.get(j);
            String fieldID = field.getAttribute("id").getValue();
            String keyField = databaseID + "." + fieldID;
            NamedKey namedKey;
            namedKey = (NamedKey) keyMap.get(keyField);
            if (namedKey == null) {
                namedKey = new NamedKey(keyField);
            }
            String fieldType = field.getAttribute("field_type").getValue();
            Attribute attributeMode = field.getAttribute("mode");
            if (attributeMode != null && "lookup".equals(attributeMode.getValue())) {
                continue;
            }
            Nodes uniqueNodes = field.query("unique/text()");
            boolean unique = false;
            if (uniqueNodes.size() > 0) {
                unique = uniqueNodes.get(0).getValue().equals("1");
            }
            String label = field.query("label/text()").get(0).getValue();
            Nodes mastagNodes = field.query("mastag/text()");
            if (unique || mastagNodes.size() > 0 || "text".equals(fieldType) || "checkbox".equals(fieldType) || "phone".equals(fieldType) ||
                    "userid".equals(fieldType) || "email".equals(fieldType)) {
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
            } else if ("float".equals(fieldType) || "percent".equals(fieldType)) {
                items.add(new AnalysisMeasure(namedKey, label, AggregationTypes.SUM));
            } else {
                System.out.println("Unrecognized type = " + fieldType + " for " + fieldID);
            }
        }

        if ("beutk2zd6".equals(databaseID)) {
            AnalysisMeasure weightedProcedures = new AnalysisMeasure(new NamedKey("Wtd Procedures"), AggregationTypes.SUM);
            items.add(weightedProcedures);
            /*AnalysisCalculation cachedWeightedProcedures = new AnalysisCalculation();
            cachedWeightedProcedures.setKey(new NamedKey("Cached Wtd Procedures"));
            cachedWeightedProcedures.setCalculationString("([97001-PT/OT] * [97001WPT/OT])+([97002-PT/OT] * [97002WPT/OT])+([97003-PT/OT] * [97003WPT/OT])+([97004-PT/OT] * [97004WPT/OT])+([97010-PT/OT] * [97010WPT/OT])+([97012-PT/OT] * [97012WPT/OT])+([97014-PT/OT] * [97014WPT/OT])+([97016-PT/OT] * [97016WPT/OT])+([97018-PT/OT] * [97018WPT/OT])+([97022-PT/OT] * [97022WPT/OT])+([97024-PT/OT] * [97024WPT/OT])+([97026-PT/OT] * [97026WPT/OT])+([97032-PT/OT] * [97032WPT/OT])+([97033-PT/OT] * [97033WPT/OT])+([97034-PT/OT] * [97034WPT/OT])+([97035-PT/OT] * [97035WPT/OT])+([97036-PT/OT] * [97036WPT/OT])+([97039-PT/OT] * [97039WPT/OT])+([97110-PT/OT] * [97110WPT/OT])+([97112-PT/OT] * [97112WPT/OT])+([97113-PT/OT] * [97113WPT/OT])+([97116-PT/OT] * [97116WPT/OT])+([97124-PT/OT] * [97124WPT/OT])+([97139-PT/OT] * [97139WPT/OT])+([97140-PT/OT] * [97140WPT/OT])+([97150-PT/OT] * [97150WPT/OT])+([97530-PT/OT] * [97530WPT/OT])+([97532-PT/OT] * [97532WPT/OT])+([97533-PT/OT] * [97533WPT/OT])+([97535-PT/OT] * [97535WPT/OT])+([97537-PT/OT] * [97537WPT/OT])+([97542-PT/OT] * [97542WPT/OT])+([97545-PT/OT] * [97545WPT/OT])+([97546-PT/OT] * [97546WPT/OT])+([97597-PT/OT] * [97597WPT/OT])+([97598-PT/OT] * [97598WPT/OT])+([97602-PT/OT] * [97602WPT/OT])+([97605-PT/OT] * [97605WPT/OT])+([97606-PT/OT] * [97606WPT/OT])+([97750-PT/OT] * [97750WPT/OT])+([97760-PT/OT] * [97760WPT/OT])+([97761-PT/OT] * [97761WPT/OT])+([97762-PT/OT] * [97762WPT/OT])+([95831-PT/OT] * [95831WPT/OT])+([95832-PT/OT] * [95832WPT/OT])+([95852-PT/OT] * [95852WPT/OT])+([95900-PT/OT] * [95900WPT/OT])+([PT100-PT/OT] * [PT100WPT/OT])+([90911-PT/OT] * [90911WPT/OT])+([G0283-PT/OT] * [G0283WPT/OT])+([16020-PT/OT] * [16020WPT/OT])+([16025-PT/OT] * [16025WPT/OT])+([64550-PT/OT] * [64550WPT/OT])+([29105-PT/OT] * [29105WPT/OT])+([29125-PT/OT] * [29125WPT/OT])+([29126-PT/OT] * [29126WPT/OT])+([29130-PT/OT] * [29130WPT/OT])+([29131-PT/OT] * [29131WPT/OT])+([29200-PT/OT] * [29200WPT/OT])+([29220-PT/OT] * [29220WPT/OT])+([29240-PT/OT] * [29240WPT/OT])+([29260-PT/OT] * [29260WPT/OT])+([29280-PT/OT] * [29280WPT/OT])+([29505-PT/OT] * [29505WPT/OT])+([29515-PT/OT] * [29515WPT/OT])+([29520-PT/OT] * [29520WPT/OT])+([29530-PT/OT] * [29530WPT/OT])+([29540-PT/OT] * [29540WPT/OT])+([29550-PT/OT] * [29550WPT/OT])+([29580-PT/OT] * [29580WPT/OT])+([TTP-PT/OT] * [TTPWPT/OT])+([L Codes-Orthotics] * [L CodesWOrthotics])+([L1846-Orthotics] * [L1846WOrthotics])+([L1850-Orthotics] * [L1850WOrthotics])+([L1900-Orthotics] * [L1900WOrthotics])+([L1901-Orthotics] * [L1901WOrthotics])+([L1902-Orthotics] * [L1902WOrthotics])+([L1904-Orthotics] * [L1904WOrthotics])+([L1906-Orthotics] * [L1906WOrthotics])+([L1907-Orthotics] * [L1907WOrthotics])+([L1910-Orthotics] * [L1910WOrthotics])+([L1920-Orthotics] * [L1920WOrthotics])+([L1930-Orthotics] * [L1930WOrthotics])+([L1906-Orthotics] * [L1906WOrthotics])+([L3700-Orthotics] * [L3700WOrthotics])+([L3701-Orthotics] * [L3701WOrthotics])+([L3702-Orthotics] * [L3702WOrthotics])+([L3710-Orthotics] * [L3710WOrthotics])+([L3720-Orthotics] * [L3720WOrthotics])+([L3730-Orthotics] * [L3730WOrthotics])+([L3740-Orthotics] * [L3740WOrthotics])+([L3760-Orthotics] * [L3760WOrthotics])+([L3762-Orthotics] * [L3762WOrthotics])+([L3763-Orthotics] * [L3763WOrthotics])+([L3764-Orthotics] * [L3764WOrthotics])+([L3765-Orthotics] * [L3765WOrthotics])+([L3766-Orthotics] * [L3766WOrthotics])+([L3806-Orthotics] * [L3806WOrthotics])+([L3807-Orthotics] * [L3807WOrthotics])+([L3808-Orthotics] * [L3808WOrthotics])+([L3900-Orthotics] * [L3900WOrthotics])+([L3901-Orthotics] * [L3901WOrthotics])+([L3905-Orthotics] * [L3905WOrthotics])+([L3906-Orthotics] * [L3906WOrthotics])+([L3908-Orthotics] * [L3908WOrthotics])+([L3909-Orthotics] * [L3909WOrthotics])+([L3911-Orthotics] * [L3911WOrthotics])+([L3912-Orthotics] * [L3912WOrthotics])+([L3913-Orthotics] * [L3913WOrthotics])+([L3915-Orthotics] * [L3915WOrthotics])+([L3917-Orthotics] * [L3917WOrthotics])+([L3919-Orthotics] * [L3919WOrthotics])+([L3921-Orthotics] * [L3921WOrthotics])+([L3923-Orthotics] * [L3923WOrthotics])+([L3925-Orthotics] * [L3925WOrthotics])+([L3927-Orthotics] * [L3927WOrthotics])+([L3929-Orthotics] * [L3929WOrthotics])+([L3931-Orthotics] * [L3931WOrthotics])+([L3933-Orthotics] * [L3933WOrthotics])+([L3935-Orthotics] * [L3935WOrthotics])+([L3956-Orthotics] * [L3956WOrthotics])+([L4350-Orthotics] * [L4350WOrthotics])+([Q4049-Orthotics] * [Q4049WOrthotics])+([S8450-Orthotics] * [S8450WOrthotics])+([S8452-Orthotics] * [S8452WOrthotics])+([S8451-Orthotics] * [S8451WOrthotics])+([98770-CA WC] * [98770WCA WC])+([98771-CA WC] * [98771WCA WC])+([98772-CA WC] * [98772WCA WC])+([98773-CA WC] * [98773WCA WC])+([98774-CA WC] * [98774WCA WC])+([98775-CA WC] * [98775WCA WC])+([98776-CA WC] * [98776WCA WC])+([98777-CA WC] * [98777WCA WC])+([98778-CA WC] * [98778WCA WC])+([95831-CA WC] * [95831WCA WC])+([95832-CA WC] * [95832WCA WC])+([95851-CA WC] * [95851WCA WC])+([95852-CA WC] * [95852WCA WC])+([97690-CA WC] * [97690WCA WC])+([97691-CA WC] * [97691WCA WC])+([11000-CA WC] * [11000WCA WC])+([95900-CA WC] * [95900WCA WC])+([95904-CA WC] * [95904WCA WC])+([97012-CA WC] * [97012WCA WC])+([97014-CA WC] * [97014WCA WC])+([97016-CA WC] * [97016WCA WC])+([97018-CA WC] * [97018WCA WC])+([97022-CA WC] * [97022WCA WC])+([97250-CA WC] * [97250WCA WC])+([97110-CA WC] * [97110WCA WC])+([97112-CA WC] * [97112WCA WC])+([97116-CA WC] * [97116WCA WC])+([97118-CA WC] * [97118WCA WC])+([97120-CA WC] * [97120WCA WC])+([97124-CA WC] * [97124WCA WC])+([97126-CA WC] * [97126WCA WC])+([97128-CA WC] * [97128WCA WC])+([97145-CA WC] * [97145WCA WC])+([97240-CA WC] * [97240WCA WC])+([97241-CA WC] * [97241WCA WC])+([97500-CA WC] * [97500WCA WC])+([97501-CA WC] * [97501WCA WC])+([97530-CA WC] * [97530WCA WC])+([97531-CA WC] * [97531WCA WC])+([97540-CA WC] * [97540WCA WC])+([97541-CA WC] * [97541WCA WC])+([97614-CA WC] * [97614WCA WC])+([97616-CA WC] * [97616WCA WC])+([97700-CA WC] * [97700WCA WC])+([x3900-MediCal] * [x3900WMediCal])+([x3902-MediCal] * [x3902WMediCal])+([x3904-MediCal] * [x3904WMediCal])+([x3906-MediCal] * [x3906WMediCal])+([x3908-MediCal] * [x3908WMediCal])+([x3910-MediCal] * [x3910WMediCal])+([x3920-MediCal] * [x3920WMediCal])+([x3922-MediCal] * [x3922WMediCal])+([92506-SPEECH] * [92506WSPEECH])+([92507-SPEECH] * [92507WSPEECH])+([92508-SPEECH] * [92508WSPEECH])+([92511-SPEECH] * [92511WSPEECH])+([92520-SPEECH] * [92520WSPEECH])+([92626-SPEECH] * [92626WSPEECH])+([92627-SPEECH] * [92627WSPEECH])+([92630-SPEECH] * [92630WSPEECH])+([92633-SPEECH] * [92633WSPEECH])+([96105-SPEECH] * [96105WSPEECH])+([96110-SPEECH] * [96110WSPEECH])+([96125-SPEECH] * [96125WSPEECH])+([31575-SPEECH] * [31575WSPEECH])+([31579-SPEECH] * [31579WSPEECH])+([92526-SPEECH] * [92526WSPEECH])+([92610-SPEECH] * [92610WSPEECH])+([92611-SPEECH] * [92611WSPEECH])+([92613-SPEECH] * [92613WSPEECH])+([92616-SPEECH] * [92616WSPEECH])+([92617-SPEECH] * [92617WSPEECH])+([92597-SPEECH] * [92597WSPEECH])+([92605-SPEECH] * [92605WSPEECH])+([92606-SPEECH] * [92606WSPEECH])+([92607-SPEECH] * [92607WSPEECH])+([92608-SPEECH] * [92608WSPEECH])+([92609-SPEECH] * [92609WSPEECH])+([V5336-SPEECH] * [V5336WSPEECH])+([92700-SPEECH] * [92700WSPEECH])+([98966-SPEECH] * [98966WSPEECH])+([98967-SPEECH] * [98967WSPEECH])+([98968-SPEECH] * [98968WSPEECH])+([98969-SPEECH] * [98969WSPEECH])+([99366-SPEECH] * [99366WSPEECH])+([99368-SPEECH] * [99368WSPEECH])");
            items.add(cachedWeightedProcedures);*/
        }

        return tableName;
    }

    private static Document executeRequest(String host, String path, String action, String requestBody) throws IOException, ParsingException {
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
