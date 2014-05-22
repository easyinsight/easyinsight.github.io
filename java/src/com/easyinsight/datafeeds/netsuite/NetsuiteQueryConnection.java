package com.easyinsight.datafeeds.netsuite;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/20/14
 * Time: 8:49 PM
 */
public class NetsuiteQueryConnection extends ServerDataSourceDefinition {
    private String query;
    private int timeout = 5;

    private boolean rebuildFields = true;

    private String accountID;
    private String netsuiteUserName;
    private String netsuitePassword;

    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public boolean isMigrateRequired() {
        return false;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.NETSUITE;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getNetsuiteUserName() {
        return netsuiteUserName;
    }

    public void setNetsuiteUserName(String netsuiteUserName) {
        this.netsuiteUserName = netsuiteUserName;
    }

    public String getNetsuitePassword() {
        return netsuitePassword;
    }

    public void setNetsuitePassword(String netsuitePassword) {
        this.netsuitePassword = netsuitePassword;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    private List<AnalysisItem> cacheFields = new ArrayList<AnalysisItem>();

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    protected void beforeRefresh(EIConnection conn) {
        if (rebuildFields) {
            if (getDataFeedID() != 0) {
                List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
                conn.setAutoCommit(true);
                Map<String, Key> keys = newDataSourceFields(null);
                Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
                for (AnalysisItem analysisItem : getFields()) {
                    fields.add(analysisItem);
                    map.put(analysisItem.getKey().toKeyString(), analysisItem);
                }
                boolean newField = false;
                if (getQuery() != null) {
                    try {
                        Class.forName("rssbus.jdbc.netsuite.NetSuiteDriver");
                        Connection connection = createConnection();
                        try {
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery(query);
                            rs.next();
                            int columnCount = rs.getMetaData().getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = rs.getMetaData().getColumnName(i);

                                AnalysisItem analysisItem = map.get(columnName);
                                if (analysisItem == null) {
                                    newField = true;
                                    if (columnName.endsWith("id") || columnName.endsWith("fk")) {
                                        analysisItem = new AnalysisDimension(new NamedKey(columnName), true);
                                    } else {
                                        switch (rs.getMetaData().getColumnType(i)) {
                                            case Types.BIGINT:
                                            case Types.TINYINT:
                                            case Types.SMALLINT:
                                            case Types.INTEGER:
                                            case Types.NUMERIC:
                                            case Types.FLOAT:
                                            case Types.DOUBLE:
                                            case Types.DECIMAL:
                                            case Types.REAL:
                                                analysisItem = new AnalysisMeasure(new NamedKey(columnName), AggregationTypes.SUM);
                                                break;
                                            case Types.BOOLEAN:
                                            case Types.BIT:
                                            case Types.CHAR:
                                            case Types.NCHAR:
                                            case Types.NVARCHAR:
                                            case Types.VARCHAR:
                                            case Types.LONGVARCHAR:
                                                analysisItem = new AnalysisDimension(new NamedKey(columnName), true);
                                                break;
                                            case Types.DATE:
                                            case Types.TIME:
                                            case Types.TIMESTAMP:
                                                analysisItem = new AnalysisDateDimension(new NamedKey(columnName), true, AnalysisDateDimension.DAY_LEVEL);
                                                break;
                                            default:
                                                throw new RuntimeException("This data type (" + rs.getMetaData().getColumnTypeName(i) + ") is not supported in Easy Insight. Type value: " + rs.getMetaData().getColumnType(i));
                                        }
                                    }
                                }
                                fields.add(analysisItem);
                            }
                        } finally {
                            connection.close();
                        }
                    } catch (Exception e) {
                        throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), this));
                    }
                }
                rebuildFields = newField;
                cacheFields = fields;
                conn.setAutoCommit(false);
            }
        }
    }

    public boolean isRebuildFields() {
        return rebuildFields;
    }

    public void setRebuildFields(boolean rebuildFields) {
        this.rebuildFields = rebuildFields;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();

        if (getDataFeedID() == 0) {
            return fields;
        }

        return cacheFields;
    }

    protected boolean otherwiseChanged() {
        boolean blah = rebuildFields;
        rebuildFields = false;
        return blah;
    }

    @Override
    public void beforeSave(EIConnection conn) throws Exception {
        super.beforeSave(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT netsuite.query FROM NETSUITE WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet existing = queryStmt.executeQuery();
        if (existing.next()) {
            String existingQueryString = existing.getString(1);
            if (existingQueryString == null || !existingQueryString.equals(getQuery())) {
                setRebuildFields(true);
            }
        } else {
            setRebuildFields(true);
        }
        queryStmt.close();
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement findPasswordStmt = conn.prepareStatement("SELECT NETSUITE_PASSWORD FROM NETSUITE WHERE DATA_SOURCE_ID = ?");
        findPasswordStmt.setLong(1, getDataFeedID());
        ResultSet passwordRS = findPasswordStmt.executeQuery();
        String existingPassword = null;
        if (passwordRS.next()) {
            existingPassword = passwordRS.getString(1);
        }
        findPasswordStmt.close();
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM NETSUITE WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO NETSUITE (account_id, netsuite_username, netsuite_password, query, " +
                "connection_timeout, rebuild_fields, data_source_id) values (?, ?, ?, ?, ?, ?, ?)");
        saveStmt.setString(1, accountID);
        saveStmt.setString(2, netsuiteUserName);
        if (netsuitePassword == null || "".equals(netsuitePassword)) {
            if (existingPassword == null) {
                saveStmt.setNull(3, Types.VARCHAR);
            } else {
                saveStmt.setString(3, existingPassword);
            }
        } else {
            saveStmt.setString(3, PasswordStorage.encryptString(netsuitePassword));
        }
        saveStmt.setString(4, query);
        saveStmt.setInt(5, timeout);
        saveStmt.setBoolean(6, rebuildFields);
        saveStmt.setLong(7, getDataFeedID());
        saveStmt.execute();
        saveStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT NETSUITE.account_id, NETSUITE.netsuite_username, NETSUITE.netsuite_password, netsuite.query, " +
                "netsuite.connection_timeout, netsuite.rebuild_fields FROM NETSUITE WHERE " +
                "data_source_id = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            accountID = rs.getString(1);
            netsuiteUserName = rs.getString(2);
            netsuitePassword = rs.getString(3);
            if (netsuitePassword != null) {
                netsuitePassword = PasswordStorage.decryptString(netsuitePassword);
            }
            query = rs.getString(4);
            timeout = rs.getInt(5);
            rebuildFields = rs.getBoolean(6);
        }
        loadStmt.close();
    }

    private Connection createConnection() throws SQLException {
        String url = "jdbc:netsuite:AccountID="+accountID+";Password="+netsuitePassword+";User=" + netsuiteUserName;
        return DriverManager.getConnection(url);
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, java.util.Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, java.util.Date lastRefreshDate) throws ReportException {
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        for (AnalysisItem field : getFields()) {
            map.put(field.getKey().toKeyString(), field);
        }
        try {
            DataSet dataSet = new DataSet();
            Class.forName("rssbus.jdbc.netsuite.NetSuiteDriver");
            Connection connection = createConnection();
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                int ct = 0;
                System.out.println("running the query...");
                Map<Integer, String> columnMap = new HashMap<Integer, String>();
                Map<Integer, Integer> columnTypeMap = new HashMap<Integer, Integer>();
                int cachedColumnCount = 0;
                while (rs.next()) {
                    System.out.println("okay, so we got a row...");
                    IRow row = dataSet.createRow();
                    if (cachedColumnCount == 0) {
                        cachedColumnCount = rs.getMetaData().getColumnCount();
                    }
                    for (int i = 1; i <= cachedColumnCount; i++) {
                        String columnName = columnMap.get(i);
                        if (columnName == null) {
                            columnName = rs.getMetaData().getColumnName(i);
                            columnMap.put(i, columnName);
                        }
                        System.out.println("getting query by column name " + columnName);
                        AnalysisItem analysisItem = map.get(columnName);
                        if (analysisItem == null) {
                            System.out.println("no item by name " + columnName);
                            continue;
                        }
                        Integer type = columnTypeMap.get(i);
                        if (type == null) {
                            type = rs.getMetaData().getColumnType(i);
                            columnTypeMap.put(i, type);
                        }
                        System.out.println("type = " + type);
                        switch (type) {
                            case Types.BIGINT:
                            case Types.TINYINT:
                            case Types.SMALLINT:
                            case Types.INTEGER:
                            case Types.NUMERIC:
                            case Types.FLOAT:
                            case Types.DOUBLE:
                            case Types.DECIMAL:
                            case Types.REAL:
                                double number = rs.getDouble(i);
                                if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                                    row.addValue(analysisItem.getKey(), String.valueOf((int) number));
                                } else {
                                    row.addValue(analysisItem.getKey(), number);
                                }
                                break;

                            case Types.BOOLEAN:
                            case Types.BIT:
                            case Types.CHAR:
                            case Types.NCHAR:
                            case Types.NVARCHAR:
                            case Types.VARCHAR:
                            case Types.LONGVARCHAR:
                                String string = rs.getString(i);
                                System.out.println("value for " + columnName + " = " + string);
                                row.addValue(analysisItem.getKey(), string);
                                break;

                            case Types.DATE:
                                try {
                                    java.util.Date d = rs.getDate(i);
                                    if (!rs.wasNull()) {
                                        row.addValue(analysisItem.getKey(), d);
                                    }
                                } catch (SQLException e) {
                                    if (e.getMessage() != null && e.getMessage().contains("can not be represented as java.sql.Date")) {
                                        row.addValue(analysisItem.getKey(), new EmptyValue());
                                    } else {
                                        LogClass.debug(e.getMessage());
                                    }
                                }
                                break;
                            case Types.TIME:
                                Time t = rs.getTime(i);
                                if (!rs.wasNull()) {
                                    row.addValue(analysisItem.getKey(), new java.util.Date(t.getTime()));
                                }
                                break;
                            case Types.TIMESTAMP:
                                try {
                                    Timestamp timestamp = rs.getTimestamp(i);
                                    if (!rs.wasNull()) {
                                        java.sql.Date date = new java.sql.Date(timestamp.getTime());
                                        row.addValue(analysisItem.getKey(), date);
                                    }
                                } catch (SQLException e) {
                                    // ignore
                                }
                                break;
                            default:
                                throw new RuntimeException("This data type (" + type + ") is not supported in Easy Insight. Type value: " + type);
                        }
                    }
                    ct++;
                    if (ct == 1000) {
                        IDataStorage.insertData(dataSet);
                        dataSet = new DataSet();
                        ct = 0;
                    }
                }
                IDataStorage.insertData(dataSet);
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }
}
