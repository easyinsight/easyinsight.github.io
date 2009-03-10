package com.easyinsight.dbservice;

import com.easyinsight.solutions.dbservice.webservice.*;
import com.easyinsight.solutions.dbservice.webservice.DatePair;
import com.easyinsight.solutions.dbservice.webservice.NumberPair;
import com.easyinsight.solutions.dbservice.webservice.Row;
import com.easyinsight.solutions.dbservice.webservice.StringPair;
import com.easyinsight.solutions.dbservice.webservice.Where;
import com.easyinsight.solutions.dbservice.webservice.DayWhere;
import com.easyinsight.dbservice.validated.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.net.URL;

import flex.messaging.FlexContext;

import javax.xml.rpc.ServiceException;

/**
 * User: James Boe
 * Date: Jan 31, 2009
 * Time: 6:36:38 PM
 */
public class DBRemote {

    public static final String MYSQL = "MySQL";
    public static final String GENERIC = "Generic";

    private static Map<String, DBConfiguration> dbConfigMap = new HashMap<String, DBConfiguration>();
    private static Map<String, EIConfiguration> eiConfigMap = new HashMap<String, EIConfiguration>();

    private String eiHost = System.getProperty("ei.target", "www.easy-insight.com");

    public void forceRun(long queryConfigurationID) {
        try {
            EIConfiguration eiConfiguration = getEIConfiguration();
            DBConfiguration dbConfiguration = getDBConfiguration();
            QueryConfiguration queryConfiguration = getQueryConfiguration(queryConfigurationID);
            BasicAuthValidatedPublish service = new BasicAuthValidatingPublishServiceServiceLocator().getBasicAuthValidatingPublishServicePort();
            ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
            ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
            QueryValidatedPublish publish = new QueryValidatedPublish(queryConfiguration, service);
            publish.execute(dbConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
                if (MYSQL.equals(type)) {
                    dbConfiguration = new MySQLConfiguration();
                } else if (GENERIC.equals(type)) {
                    dbConfiguration = new GenericDBConfiguration();
                }
                if (dbConfiguration != null) {
                    dbConfiguration.load(conn);
                    dbConfigMap.put(FlexContext.getFlexSession().getId(), dbConfiguration);
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
                eiConfigMap.put(FlexContext.getFlexSession().getId(), eiConfiguration);
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

    public String validateDB(DBConfiguration dbConfiguration) {
        return dbConfiguration.validate();
    }

    public void assignDB(DBConfiguration dbConfiguration) {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
            dbConfigMap.put(FlexContext.getFlexSession().getId(), dbConfiguration);
            conn.prepareStatement("DELETE FROM MYSQL_CONFIG").executeUpdate();
            conn.prepareStatement("DELETE FROM DB_CONFIG").executeUpdate();
            PreparedStatement configStmt = conn.prepareStatement("INSERT INTO DB_CONFIG (DB_TYPE) VALUES (?)");
            configStmt.setString(1, dbConfiguration.getType());
            configStmt.execute();
            dbConfiguration.save(conn);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String validateEI(EIConfiguration eiConfiguration) {
        try {
            URL url = new URL("http://" + this.eiHost + "/app/services/UncheckedPublishBasic");
            BasicAuthUncheckedPublish service = new BasicAuthUncheckedPublishServiceServiceLocator().getBasicAuthUncheckedPublishServicePort(url);
            ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
            ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
            if (service.validateCredentials()) {
                return null;
            } else {
                return "Invalid credentials.";
            }
        } catch (Exception e) {
            return "Invalid credentials.";
        }
    }

    public void assignEI(EIConfiguration eiConfiguration) {
        Connection conn = getConnection();
        try {
            eiConfigMap.put(FlexContext.getFlexSession().getId(), eiConfiguration);
            conn.prepareStatement("DELETE FROM EI_CONFIG").executeUpdate();
            PreparedStatement insertUserStmt = conn.prepareStatement("INSERT INTO EI_CONFIG (USERNAME, PASSWORD) VALUES (?, ?)");
            insertUserStmt.setString(1, eiConfiguration.getUserName());
            insertUserStmt.setString(2, new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME).encrypt(eiConfiguration.getPassword()));
            insertUserStmt.execute();
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

    public List<String> getTables() {
        List<String> tables = new ArrayList<String>();
        try {
            DBConfiguration savedConfiguration = dbConfigMap.get(FlexContext.getFlexSession().getId());
            Connection conn = savedConfiguration.getConnection();
            try {
                ResultSet tableRS = conn.getMetaData().getTables(null, null, "", new String[] {"TABLE", "VIEW"});
                while (tableRS.next()){
                    tables.add(tableRS.getString(3));
                }
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return tables;
    }

    public List<Column> getColumns(String table) {
        List<Column> columns = new ArrayList<Column>();
        try {
            DBConfiguration savedConfiguration = dbConfigMap.get(FlexContext.getFlexSession().getId());
            Connection conn = savedConfiguration.getConnection();
            try {
                ResultSet tableRS = conn.getMetaData().getTables(null, null, table, new String[] {"TABLE", "VIEW"});
                if (tableRS.next()){
                    ResultSet columnRS = conn.getMetaData().getColumns(null, null, table, "");
                    while (columnRS.next()) {
                        String name = columnRS.getString(4);
                        String type = columnRS.getString(6);
                        columns.add(new Column(type, name));
                    }
                }
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return columns;
    }

    public void deleteQuery(long queryConfigurationID) {
        Connection conn = getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM QUERY_CONFIGURATION WHERE QUERY_CONFIGURATION_ID = ?");
            deleteStmt.setLong(1, queryConfigurationID);
            deleteStmt.executeUpdate();
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

    public TestQueryResults testQuery(QueryConfiguration queryConfiguration) {
        try {
            DBConfiguration savedConfiguration = dbConfigMap.get(FlexContext.getFlexSession().getId());
            if (savedConfiguration == null) {
                savedConfiguration = getDBConfiguration();
            }
            Connection conn = savedConfiguration.getConnection();
            TestQueryResults testQueryResults;
            try {
                Statement statement = conn.createStatement();
                statement.setMaxRows(5);
                ResultSet rs = statement.executeQuery(queryConfiguration.getQuery());
                ResultSetMetaData rsMetadata = rs.getMetaData();
                int columns = rsMetadata.getColumnCount();
                List<Map<String, String>> results = new ArrayList<Map<String, String>>();
                while (rs.next()) {
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < columns; i++) {
                        String columnName = rsMetadata.getColumnName(i + 1);
                        Object object = rs.getObject(i + 1);
                        map.put(columnName, object == null ? "" : object.toString());
                    }
                    results.add(map);
                }
                testQueryResults = new TestQueryResults();
                testQueryResults.setResults(results);
            } finally {
                conn.close();
            }
            return testQueryResults;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void publishQuery(QueryConfiguration queryConfiguration) {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUERY_CONFIG (DATA_SOURCE, QUERY, AD_HOC, NAME, QUERY_MODE) VALUES (?, ?, ?, ?, ?)");
            insertStmt.setString(1, queryConfiguration.getDataSource());
            insertStmt.setString(2, queryConfiguration.getQuery());
            insertStmt.setBoolean(3, queryConfiguration.isAdHoc());
            insertStmt.setString(4, queryConfiguration.getName());
            insertStmt.setInt(5, queryConfiguration.getPublishMode());
            insertStmt.execute();

            URL url = new URL("http://" + this.eiHost + "/app/services/UncheckedPublishBasic");
            BasicAuthUncheckedPublish service = new BasicAuthUncheckedPublishServiceServiceLocator().getBasicAuthUncheckedPublishServicePort(url);
            EIConfiguration eiConfiguration = eiConfigMap.get(FlexContext.getFlexSession().getId());
            ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
            ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
            service.disableUnchecked(queryConfiguration.getDataSource());
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String queryToEI(QueryConfiguration queryConfiguration) {
        String apiKey = null;
        try {
            URL url = new URL("http://" + this.eiHost + "/app/services/UncheckedPublishBasic");
            BasicAuthUncheckedPublish service = new BasicAuthUncheckedPublishServiceServiceLocator().getBasicAuthUncheckedPublishServicePort(url);
            EIConfiguration eiConfiguration = eiConfigMap.get(FlexContext.getFlexSession().getId());
            if (eiConfiguration == null) {
                eiConfiguration = getEIConfiguration();
            }
            DBConfiguration savedConfiguration = dbConfigMap.get(FlexContext.getFlexSession().getId());
            if (savedConfiguration == null) {
                savedConfiguration = getDBConfiguration();
            }
            ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
            ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
            String dataSourceKey = queryConfiguration.getDataSource();
            Connection conn = savedConfiguration.getConnection();
            try {
                Row[] rows = createRows(queryConfiguration.getQuery(), conn);
                if (queryConfiguration.getPublishMode() == QueryConfiguration.ADD) {
                    apiKey = service.addRows(dataSourceKey, rows);
                } else if (queryConfiguration.getPublishMode() == QueryConfiguration.REPLACE) {
                    apiKey = service.replaceRows(dataSourceKey, rows);
                } else if (queryConfiguration.getPublishMode() == QueryConfiguration.ADD_EXCLUSIVE_TODAY) {
                    Calendar cal = Calendar.getInstance();
                    DatePair datePair = new DatePair();
                    datePair.setKey("Date");
                    datePair.setValue(cal);
                    DayWhere dayWhere = new DayWhere();
                    dayWhere.setKey("Date");
                    dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
                    dayWhere.setYear(cal.get(Calendar.YEAR));
                    for (Row row : rows) {
                        DatePair[] datePairs = row.getDatePairs();
                        if (datePairs == null) {
                            datePairs = new DatePair[] { datePair };
                        } else {
                            List<DatePair> datePairList = new ArrayList<DatePair>(Arrays.asList(datePairs));
                            datePairList.add(datePair);
                            datePairs = new DatePair[datePairList.size()];
                            datePairList.toArray(datePairs);
                        }
                        row.setDatePairs(datePairs);
                    }
                    Where where = new Where();
                    where.setDayWheres(new DayWhere[] { dayWhere });
                    apiKey = service.updateRows(dataSourceKey, rows, where);
                }
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return apiKey;
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

    public QueryConfiguration getQueryConfiguration(long queryConfigurationID) {
        Connection conn = getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE, QUERY, AD_HOC, NAME, QUERY_MODE FROM QUERY_CONFIG WHERE " +
                    "QUERY_CONFIG_ID = ?");
            queryStmt.setLong(1, queryConfigurationID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                QueryConfiguration queryConfiguration = new QueryConfiguration();
                queryConfiguration.setDataSource(rs.getString(1));
                queryConfiguration.setQuery(rs.getString(2));
                queryConfiguration.setAdHoc(rs.getBoolean(3));
                queryConfiguration.setName(rs.getString(4));
                queryConfiguration.setPublishMode(rs.getInt(5));
                return queryConfiguration;
            } else {
                throw new RuntimeException();
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
