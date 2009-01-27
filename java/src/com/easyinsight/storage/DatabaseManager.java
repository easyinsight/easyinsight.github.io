package com.easyinsight.storage;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.net.URL;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

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

    private DatabaseManager() {
        loadAvailableDatabases();
    }

    public Database getDatabase(String databaseID) {
        return dbMap.get(databaseID);
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
                    dbMap.put(dbID, database);
                }
            }
        } catch (IOException e) {
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
