package com.easyinsight.datafeeds.database;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;

import java.sql.*;
import java.text.MessageFormat;

/**
 * User: jamesboe
 * Date: 11/27/12
 * Time: 10:24 PM
 */
public class OracleDatabaseConnection extends ServerDatabaseConnection {

    private String host;
    private int port;
    private String sid;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    @Override
    protected Connection createConnection() throws SQLException {
        String url = MessageFormat.format("jdbc:oracle:thin:@//{0}:{1}/{2}", host, String.valueOf(port), sid);
        return DriverManager.getConnection(url, dbUserName, dbPassword);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SERVER_ORACLE;
    }

    @Override
    public void beforeSave(EIConnection conn) throws Exception {
        super.beforeSave(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT oracle_database_connection.query_string FROM oracle_database_connection WHERE DATA_SOURCE_ID = ?");
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

        PreparedStatement findPasswordStmt = conn.prepareStatement("SELECT DATABASE_PASSWORD FROM oracle_database_connection WHERE DATA_SOURCE_ID = ?");
        findPasswordStmt.setLong(1, getDataFeedID());
        ResultSet passwordRS = findPasswordStmt.executeQuery();
        String existingPassword = null;
        if (passwordRS.next()) {
            existingPassword = passwordRS.getString(1);
        }
        findPasswordStmt.close();
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM oracle_database_connection WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO oracle_database_connection (data_source_id, host_name, server_port, sid," +
                "database_username, database_password, query_string, rebuild_fields) values (?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, host);
        insertStmt.setInt(3, port);
        insertStmt.setString(4, sid);
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
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT host_name, server_port, sid, database_username," +
                "database_password, query_string, rebuild_fields FROM oracle_database_connection WHERE data_source_id = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            host = rs.getString(1);
            port = rs.getInt(2);
            sid = rs.getString(3);
            dbUserName = rs.getString(4);
            dbPassword = rs.getString(5);
            if (dbPassword != null && !"".equals(dbPassword)) {
                dbPassword = PasswordStorage.decryptString(dbPassword);
            }
            setQuery(rs.getString(6));
            setRebuildFields(rs.getBoolean(7));
        }
        loadStmt.close();
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }
}
