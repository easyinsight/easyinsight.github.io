package com.easyinsight.dbservice;

import flex.messaging.FlexContext;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 14, 2009
 * Time: 10:52:47 PM
 */
public class Configuration {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        System.out.print("Easy Insight User Name: ");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String userName = bufferedReader.readLine();
            System.out.print("Easy Insight Password: ");
            String password = bufferedReader.readLine();
            System.out.print("Database Host:");
            String dbHost = bufferedReader.readLine();
            System.out.print("Database Port:");
            String dbPort = bufferedReader.readLine();
            System.out.print("Database Name:");
            String dbName = bufferedReader.readLine();
            System.out.print("Database User Name:");
            String dbUserName = bufferedReader.readLine();
            System.out.print("Database Password:");
            String dbPassword = bufferedReader.readLine();
            MySQLConfiguration dbConfiguration = new MySQLConfiguration();
            dbConfiguration.setDatabaseName(dbName);
            dbConfiguration.setHost(dbHost);
            dbConfiguration.setPort(dbPort);
            dbConfiguration.setUserName(dbUserName);
            dbConfiguration.setPassword(dbPassword);
            System.out.println("Saving...");
            EIConfiguration eiConfiguration = new EIConfiguration();
            eiConfiguration.setUserName(userName);
            eiConfiguration.setPassword(password);
            exportConfiguration(dbConfiguration, eiConfiguration);
            System.out.println("Saved. Copy the new eicredentials.xml file into the bin directory of your application server.");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void exportConfiguration(DBConfiguration dbConfiguration, EIConfiguration eiConfiguration) throws StringEncrypter.EncryptionException, IOException {
        StringEncrypter stringEncrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);

        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<config>\r\n");
        xmlBuilder.append("\t<database type=\"");
        xmlBuilder.append(dbConfiguration.getType());
        xmlBuilder.append("\">\r\n");
        xmlBuilder.append(dbConfiguration.toXML(stringEncrypter));
        xmlBuilder.append("\t</database>\r\n");
        xmlBuilder.append("\t<ei>\r\n");
        xmlBuilder.append("\t\t<username>");
        xmlBuilder.append(eiConfiguration.getUserName());
        xmlBuilder.append("</username>\r\n");
        xmlBuilder.append("\t\t<password><![CDATA[");
        xmlBuilder.append(stringEncrypter.encrypt(eiConfiguration.getPassword()));
        xmlBuilder.append("]]></password>\r\n");
        xmlBuilder.append("\t</ei>\r\n");
        xmlBuilder.append("</config>");
        File outputFile = new File("eicredentials.xml");
        FileWriter fw = new FileWriter(outputFile);
        fw.write(xmlBuilder.toString());
        fw.close();
    }

    private static Connection getConnection() {
        try {
            String dbURL = "jdbc:derby:eijdbc";
            return DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void assignEI(EIConfiguration eiConfiguration) {
        Connection conn = getConnection();
        try {
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

    public static void assignDB(DBConfiguration dbConfiguration) {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
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
}
