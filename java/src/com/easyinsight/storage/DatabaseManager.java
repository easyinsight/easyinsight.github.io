package com.easyinsight.storage;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.net.URL;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * User: James Boe
 * Date: Nov 9, 2008
 * Time: 6:53:49 PM
 */
public class DatabaseManager {

    private Map<String, Database> dbMap = new HashMap<String, Database>();
    private static DatabaseManager instance;

    public static DatabaseManager instance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void shutdown() {
        for (Database database : dbMap.values()) {
            database.shutdown();
        }
    }

    private DatabaseManager() {
        loadAvailableDatabases();
    }

    public Database getDatabase(String databaseID) {
        return dbMap.get(databaseID);
    }

    public void defineDateTable(Database database) throws SQLException {
        Connection conn = database.getConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet tableRS = conn.getMetaData().getTables(null, null, "DATE_DIMENSION", null);
            if (!tableRS.next()) {
                PreparedStatement dbStmt = conn.prepareStatement("CREATE TABLE DATE_DIMENSION (DATE_DIMENSION_ID BIGINT(11) AUTO_INCREMENT NOT NULL," +
                        "DIM_DATE DATE NOT NULL, DIM_DAY_OF_MONTH INTEGER NOT NULL, DIM_MONTH INTEGER NOT NULL, " +
                        "DIM_QUARTER_OF_YEAR INTEGER NOT NULL, DIM_YEAR INTEGER NOT NULL, DIM_WEEK_OF_YEAR INTEGER NOT NULL," +
                        "DIM_DAY_OF_WEEK INTEGER NOT NULL, DIM_DAY_OF_YEAR INTEGER NOT NULL, PRIMARY KEY (DATE_DIMENSION_ID), INDEX (DIM_DATE), INDEX(DIM_DAY_OF_MONTH)," +
                        "INDEX(DIM_QUARTER_OF_YEAR), INDEX(DIM_YEAR, DIM_DAY_OF_YEAR), INDEX(DIM_WEEK_OF_YEAR), INDEX(DIM_DAY_OF_WEEK), INDEX(DIM_DAY_OF_YEAR))");
                dbStmt.execute();
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DATE_DIMENSION (DIM_DATE, DIM_DAY_OF_MONTH," +
                        "DIM_MONTH, DIM_QUARTER_OF_YEAR, DIM_YEAR, DIM_WEEK_OF_YEAR, DIM_DAY_OF_WEEK, DIM_DAY_OF_YEAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -3);
                for (int i = 0; i < 6; i++) {
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    int year = cal.get(Calendar.YEAR);
                    int newYear = year + 1;
                    cal.set(Calendar.YEAR, newYear);
                    for (int j = 0; j < ((cal.get(Calendar.YEAR) % 4 == 0) ? 366 : 365); j++) {
                        cal.set(Calendar.DAY_OF_YEAR, j + 1);
                        insertStmt.setDate(1, new java.sql.Date(cal.getTime().getTime()));
                        insertStmt.setInt(2, cal.get(Calendar.DAY_OF_MONTH));
                        insertStmt.setInt(3, cal.get(Calendar.MONTH));
                        int quarterOfYear;
                        switch (cal.get(Calendar.MONTH)) {
                            case Calendar.JANUARY:
                            case Calendar.FEBRUARY:
                            case Calendar.MARCH:
                                quarterOfYear = 0;
                                break;
                            case Calendar.APRIL:
                            case Calendar.MAY:
                            case Calendar.JUNE:
                                quarterOfYear = 1;
                                break;
                            case Calendar.JULY:
                            case Calendar.AUGUST:
                            case Calendar.SEPTEMBER:
                                quarterOfYear = 2;
                                break;
                            case Calendar.OCTOBER:
                            case Calendar.NOVEMBER:
                            case Calendar.DECEMBER:
                            default:
                                quarterOfYear = 3;
                                break;
                        }
                        insertStmt.setInt(4, quarterOfYear);
                        insertStmt.setInt(5, cal.get(Calendar.YEAR));
                        insertStmt.setInt(6, cal.get(Calendar.WEEK_OF_YEAR));
                        insertStmt.setInt(7, cal.get(Calendar.DAY_OF_WEEK));
                        insertStmt.setInt(8, cal.get(Calendar.DAY_OF_YEAR));
                        insertStmt.execute();
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();            
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            database.closeConnection(conn);
        }
    }

    private void loadAvailableDatabases() {
        try {
            URL url = getClass().getClassLoader().getResource("eiconfig.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(url.getFile())));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String propertyKey = (String) entry.getKey();
                if (propertyKey.indexOf("storage.host") != -1) {
                    String dbID = propertyKey.split("\\.")[0];
                    String host = (String) entry.getValue();
                    String port = properties.getProperty(dbID + ".storage.port");
                    String databaseName = properties.getProperty(dbID + ".storage.name");
                    String user = properties.getProperty(dbID + ".storage.username");
                    String password = properties.getProperty(dbID + ".storage.password");
                    Database database = Database.create(host, port, databaseName, user, password);
                    defineDateTable(database);
                    dbMap.put(dbID, database);
                }
            }
        } catch (IOException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String chooseDatabase(Connection conn) throws SQLException {
        PreparedStatement dbStmt = conn.prepareStatement("SELECT SUM(SIZE), DATABASE_NAME FROM FEED_PERSISTENCE_METADATA GROUP BY DATABASE_NAME");
        ResultSet dbSizes = dbStmt.executeQuery();
        String dbToUse = null;
        long smallestSize = Long.MAX_VALUE;
        Set<String> foundDBs = new HashSet<String>(dbMap.keySet());
        while (dbSizes.next()) {
            long size = dbSizes.getLong(1);
            String name = dbSizes.getString(2);
            foundDBs.remove(name);
            if (size < smallestSize) {
                dbToUse = name;
                smallestSize = size;
            }
        }
        if (!foundDBs.isEmpty()) {
            dbToUse = foundDBs.iterator().next();
        }        
        return dbToUse;
    }
}
