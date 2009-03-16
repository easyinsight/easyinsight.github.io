package com.easyinsight.dbservice;

import com.easyinsight.dbservice.validated.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: James Boe
 * Date: Jan 16, 2009
 * Time: 10:24:35 PM
 */
public class DBTask extends TimerTask {

    private String eiHost = System.getProperty("ei.target", "www.easy-insight.com");

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
                    System.out.println("Running " + queryConfiguration.getName());
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
                } else if (DBRemote.GENERIC.equals(type)) {
                    dbConfiguration = new GenericDBConfiguration();
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

    private Properties getProperties() {
        try {
            File file = new File("override.properties");
            if (file.exists()) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File("config.properties")));
                return properties;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public EIConfiguration getEIConfiguration() {
        EIConfiguration eiConfiguration = null;
        Properties properties = getProperties();
        if (properties.getProperty("ei.user") != null) {
            try {
                eiConfiguration = new EIConfiguration();
                eiConfiguration.setUserName(properties.getProperty("ei.user"));
                eiConfiguration.setPassword(new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME).decrypt(properties.getProperty("ei.password")));
            } catch (StringEncrypter.EncryptionException e) {
                throw new RuntimeException(e);
            }
        } else {
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
