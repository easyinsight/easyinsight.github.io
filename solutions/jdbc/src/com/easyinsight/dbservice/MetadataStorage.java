package com.easyinsight.dbservice;

import flex.messaging.FlexContext;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Feb 4, 2009
 * Time: 12:30:08 AM
 */
public class MetadataStorage {
    public DBConfiguration getDBConfiguration() {
        DBConfiguration dbConfiguration = null;
        Connection conn = getConnection();
        try {
            PreparedStatement configStmt = conn.prepareStatement("SELECT DB_TYPE FROM DB_CONFIG");
            ResultSet rs = configStmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString(1);
                if (DBRemote.MYSQL.equals(type)) {
                    dbConfiguration = new MySQLConfiguration();
                } else if (DBRemote.GENERIC.equals(type)) {
                    dbConfiguration = new GenericDBConfiguration();
                }
                if (dbConfiguration != null) {
                    dbConfiguration.load(conn);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                LogClass.error(e);
            }
        }
        return dbConfiguration;
    }

    public EIConfiguration getEIConfiguration() {
        EIConfiguration eiConfiguration = null;
        Connection conn = getConnection();
        try {
            PreparedStatement configStmt = conn.prepareStatement("SELECT USERNAME, PASSWORD FROM EI_CONFIG");
            ResultSet rs = configStmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString(1);
                String password = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME).decrypt(rs.getString(2));
                eiConfiguration = new EIConfiguration();
                eiConfiguration.setUserName(userName);
                eiConfiguration.setPassword(password);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                LogClass.error(e);
            }
        }
        return eiConfiguration;
    }

    private Connection getConnection() {
        try {
            String dbURL = "jdbc:derby:eijdbc";
            return DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<QueryConfiguration> getQueryConfigurations() {
        List<QueryConfiguration> queryConfigurations = new ArrayList<QueryConfiguration>();
        Connection conn = getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT QUERY_CONFIG_ID, DATA_SOURCE, QUERY, AD_HOC, NAME, QUERY_MODE FROM QUERY_CONFIG");
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long queryConfigID = rs.getLong(1);
                String dataSource = rs.getString(2);
                String query = rs.getString(3);
                boolean adHoc = rs.getBoolean(4);
                String name = rs.getString(5);
                int mode = rs.getInt(6);
                QueryConfiguration queryConfiguration = new QueryConfiguration();
                queryConfiguration.setQueryConfigurationID(queryConfigID);
                queryConfiguration.setQuery(query);
                queryConfiguration.setAdHoc(adHoc);
                queryConfiguration.setDataSource(dataSource);
                queryConfiguration.setName(name);
                queryConfiguration.setPublishMode(mode);
                queryConfigurations.add(queryConfiguration);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                LogClass.error(e);
            }
        }
        return queryConfigurations;
    }
}
