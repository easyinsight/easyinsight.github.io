package com.easyinsight.datafeeds.database;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;

import java.sql.*;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 11/27/12
 * Time: 10:24 PM
 */
public class SQLServerDatabaseConnection extends ServerDatabaseConnection {

    private String host;
    private int port = 1433;
    private String databaseName;
    private String dbUserName;
    private String dbPassword;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, java.util.Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, java.util.Date lastRefreshDate) throws ReportException {
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        for (AnalysisItem field : getFields()) {
            map.put(field.getKey().toKeyString(), field);
        }
        try {
            Calendar cal = Calendar.getInstance();
            DataSet dataSet = new DataSet();
            Connection connection = createConnection();
            Statement statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(1000);
            ResultSet rs = statement.executeQuery(getQuery());
            int ct = 0;
            while (rs.next()) {
                IRow row = dataSet.createRow();
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    AnalysisItem analysisItem = map.get(columnName);
                    if (analysisItem == null) {
                        continue;
                    }
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
                            if (string != null) {
                                string = string.trim();
                            }
                            row.addValue(analysisItem.getKey(), string);
                            break;

                        case Types.DATE:
                            java.util.Date d = rs.getDate(i);
                            if(!rs.wasNull()) {
                                cal.setTime(d);
                                int year = cal.get(Calendar.YEAR);
                                if (year == 1752 || year == 1753) {

                                } else {
                                    row.addValue(analysisItem.getKey(), d);
                                }
                            }
                            break;
                        case Types.TIME:
                            Time t = rs.getTime(i);
                            if(!rs.wasNull()) {
                                java.util.Date date = new java.util.Date(t.getTime());
                                cal.setTime(date);
                                int year = cal.get(Calendar.YEAR);
                                if (year == 1752 || year == 1753) {

                                } else {
                                    row.addValue(analysisItem.getKey(), date);
                                }
                            }
                            break;
                        case Types.TIMESTAMP:
                            try {
                                Timestamp timestamp = rs.getTimestamp(i);
                                if (!rs.wasNull()) {
                                    Date date = new Date(timestamp.getTime());
                                    cal.setTime(date);
                                    int year = cal.get(Calendar.YEAR);
                                    if (year == 1752 || year == 1753) {

                                    } else {
                                        row.addValue(analysisItem.getKey(), date);
                                    }
                                }
                            } catch (SQLException e) {
                                // ignore
                            }
                            break;
                        default:
                            throw new RuntimeException("This data type (" + rs.getMetaData().getColumnTypeName(i) + ") is not supported in Easy Insight. Type value: " + rs.getMetaData().getColumnType(i));
                    }
                }
                ct++;
                if (ct == 1000) {
                    IDataStorage.insertData(dataSet);
                    dataSet = new DataSet();
                    ct = 0;
                }
            }
            rs.close();
            statement.close();
            IDataStorage.insertData(dataSet);
        } catch (Exception e) {
            LogClass.userError(e.getMessage(), e);
        }
        return null;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    @Override
    protected Connection createConnection() throws SQLException {
        String url = MessageFormat.format("jdbc:sqlserver://{0}:{1};databaseName={2};loginTimeout=15;selectMode=cursor", host, String.valueOf(port), databaseName);
        return DriverManager.getConnection(url, dbUserName, dbPassword);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SERVER_SQL_SERVER;
    }

    @Override
    public void beforeSave(EIConnection conn) throws Exception {
        super.beforeSave(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT sql_server_database_connection.query_string FROM sql_server_database_connection WHERE DATA_SOURCE_ID = ?");
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
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        if (getCopyingFromSource() > 0) {
            MySQLDatabaseConnection dataSource = (MySQLDatabaseConnection) new FeedStorage().getFeedDefinitionData(getCopyingFromSource(), conn);
            setDbPassword(dataSource.getDbPassword());
        }
        PreparedStatement findPasswordStmt = conn.prepareStatement("SELECT DATABASE_PASSWORD FROM SQL_SERVER_DATABASE_CONNECTION WHERE DATA_SOURCE_ID = ?");
        findPasswordStmt.setLong(1, getDataFeedID());
        ResultSet passwordRS = findPasswordStmt.executeQuery();
        String existingPassword = null;
        if (passwordRS.next()) {
            existingPassword = passwordRS.getString(1);
        }
        findPasswordStmt.close();
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM SQL_SERVER_DATABASE_CONNECTION WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SQL_SERVER_DATABASE_CONNECTION (data_source_id, host_name, server_port, database_name," +
                "database_username, database_password, query_string, rebuild_fields, timeout) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, host);
        insertStmt.setInt(3, port);
        insertStmt.setString(4, databaseName);
        insertStmt.setString(5, dbUserName);
        if (dbPassword == null || "".equals(dbPassword)) {
            if (existingPassword == null) {
                insertStmt.setNull(6, Types.VARCHAR);
            } else {
                insertStmt.setString(6, existingPassword);
            }
        } else {
            insertStmt.setString(6, PasswordStorage.encryptString(dbPassword));
        }
        insertStmt.setString(7, getQuery());
        insertStmt.setBoolean(8, isRebuildFields());
        insertStmt.setLong(9, getTimeout());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT host_name, server_port, database_name, database_username," +
                "database_password, query_string, rebuild_fields, timeout FROM SQL_SERVER_DATABASE_CONNECTION WHERE data_source_id = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            host = rs.getString(1);
            port = rs.getInt(2);
            databaseName = rs.getString(3);
            dbUserName = rs.getString(4);
            dbPassword = rs.getString(5);
            if (dbPassword != null && !"".equals(dbPassword)) {
                dbPassword = PasswordStorage.decryptString(dbPassword);
            }
            setQuery(rs.getString(6));
            setRebuildFields(rs.getBoolean(7));
            setTimeout(rs.getInt(8));
        }
        loadStmt.close();
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }
}
