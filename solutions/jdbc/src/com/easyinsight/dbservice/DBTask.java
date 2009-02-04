package com.easyinsight.dbservice;

import com.easyinsight.dbservice.validated.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;

/**
 * User: James Boe
 * Date: Jan 16, 2009
 * Time: 10:24:35 PM
 */
public class DBTask extends TimerTask {
    public void run() {

        try {
            List<QueryConfiguration> queryConfigs = getQueryConfigurations();
            EIConfiguration eiConfiguration = getEIConfiguration();
            DBConfiguration dbConfiguration = getDBConfiguration();
            if (eiConfiguration != null && dbConfiguration != null) {
                BasicAuthValidatedPublish service = new BasicAuthValidatingPublishServiceServiceLocator().getBasicAuthValidatingPublishServicePort();
                ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
                ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
                for (QueryConfiguration queryConfiguration : queryConfigs) {
                    String dataSourceKey = queryConfiguration.getDataSource();
                    Connection conn = dbConfiguration.getConnection();
                    try {
                        Row[] rows = createRows(queryConfiguration.getQuery(), conn);
                        if (queryConfiguration.getPublishMode() == QueryConfiguration.ADD) {
                            service.addRows(dataSourceKey, rows);
                        } else if (queryConfiguration.getPublishMode() == QueryConfiguration.REPLACE) {
                            service.replaceRows(dataSourceKey, rows);
                        }
                    } finally {
                        conn.close();
                    }
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

    private Row[] createRows(String query, Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsMetadata = rs.getMetaData();
        int columns = rsMetadata.getColumnCount();
        List<Row> rows = new ArrayList<Row>();
        while (rs.next()) {
            Row row = new Row();
            List<StringPair> stringPairs = new ArrayList<StringPair>();
            List<NumberPair> numberPairs = new ArrayList<NumberPair>();
            List<DatePair> datePairs = new ArrayList<DatePair>();
            for (int i = 0; i < columns; i++) {
                String columnName = rsMetadata.getColumnName(i + 1);
                Object object = rs.getObject(i + 1);
                if (object instanceof Number) {
                    Number number = (Number) object;
                    NumberPair numberPair = new NumberPair();
                    numberPair.setKey(columnName);
                    numberPair.setValue(number.doubleValue());
                    numberPairs.add(numberPair);
                } else if (object instanceof String) {
                    String string = (String) object;
                    StringPair stringPair = new StringPair();
                    stringPair.setKey(columnName);
                    stringPair.setValue(string);
                    stringPairs.add(stringPair);
                } else if (object instanceof Date) {
                    Date date = (Date) object;
                    DatePair datePair = new DatePair();
                    datePair.setKey(columnName);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    datePair.setValue(calendar);
                    datePairs.add(datePair);
                } else if (object == null) {
                    StringPair stringPair = new StringPair();
                    stringPair.setKey(columnName);
                    stringPair.setValue("");
                    stringPairs.add(stringPair);
                }
                System.out.println(columnName + " - " + object);
            }
            StringPair[] stringPairArray = new StringPair[stringPairs.size()];
            stringPairs.toArray(stringPairArray);
            NumberPair[] numberPairArray = new NumberPair[numberPairs.size()];
            numberPairs.toArray(numberPairArray);
            DatePair[] datePairArray = new DatePair[datePairs.size()];
            datePairs.toArray(datePairArray);
            row.setDatePairs(datePairArray);
            row.setStringPairs(stringPairArray);
            row.setNumberPairs(numberPairArray);
            rows.add(row);
        }
        Row[] rowArray = new Row[rows.size()];
        rows.toArray(rowArray);
        return rowArray;
    }
}
