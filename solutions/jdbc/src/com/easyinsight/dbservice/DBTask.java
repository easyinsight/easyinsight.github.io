package com.easyinsight.dbservice;

import com.easyinsight.dbservice.validated.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.net.URL;

/**
 * User: James Boe
 * Date: Jan 16, 2009
 * Time: 10:24:35 PM
 */
public class DBTask extends TimerTask {

    private String eiHost = System.getProperty("ei.target", "localhost:8080");

    public void run() {
        try {
            List<QueryConfiguration> queryConfigs = getQueryConfigurations();
            EIConfiguration eiConfiguration = getEIConfiguration();
            DBConfiguration dbConfiguration = getDBConfiguration();
            if (eiConfiguration != null && dbConfiguration != null) {
                URL url = new URL("http://" + this.eiHost + "/app/services/ValidatedPublishBasic");
                BasicAuthValidatedPublish service = new BasicAuthValidatingPublishServiceServiceLocator().getBasicAuthValidatingPublishServicePort(url);
                ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
                ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
                for (QueryConfiguration queryConfiguration : queryConfigs) {
                    QueryValidatedPublish publish = new QueryValidatedPublish(queryConfiguration, service);
                    publish.execute(dbConfiguration);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                }
                if (dbConfiguration != null) {
                    dbConfiguration.load(conn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return eiConfiguration;
    }

    private Connection getConnection() {
        try {
            String dbURL = "jdbc:derby:eijdbc";
            return DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return queryConfigurations;
    }
}
