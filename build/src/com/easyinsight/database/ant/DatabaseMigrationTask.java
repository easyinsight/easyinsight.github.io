package com.easyinsight.database.ant;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import java.text.MessageFormat;
import java.sql.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * User: James Boe
 * Date: Jan 30, 2009
 * Time: 1:23:38 PM
 */
public class DatabaseMigrationTask extends Task {

    private static final String urlTemplate = "jdbc:mysql://{0}:{1}/{2}?user={3}&password={4}";
    private String databaseHost = "localhost";
    private String databasePort = "3306";
    private String databaseName = "dms";
    private String databaseUserName = "dms";
    private String databasePassword = "dms";
    private String sqlDirectory;

    public void setSqlDirectory(String sqlDirectory) {
        this.sqlDirectory = sqlDirectory;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDatabaseUserName(String databaseUserName) {
        this.databaseUserName = databaseUserName;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public void execute() throws BuildException {
        if (sqlDirectory == null) {
            throw new BuildException("You must specify a SQL directory.");
        }
        File sqlDir = new File(sqlDirectory);
        if (!sqlDir.exists()) {
            throw new BuildException("The specified SQL directory does not exist.");
        } else if (!sqlDir.isDirectory()) {
            throw new BuildException("The specified SQL directory is not a directory.");
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = MessageFormat.format(urlTemplate, databaseHost, databasePort, databaseName, databaseUserName, databasePassword);
            Connection conn = DriverManager.getConnection(url);
            List<Integer> migrations = null;
            int migrationOn = 0;
            String migrationCommand = null;
            try {
                conn.setAutoCommit(false);
                Statement statement = conn.createStatement();
                int fromVersion = 0;
                ResultSet tableRS = conn.getMetaData().getTables(null, null, "database_version", null);
                if (tableRS.next()) {
                    PreparedStatement versionGetStmt = conn.prepareStatement("SELECT version FROM database_version");
                    ResultSet versionRS = versionGetStmt.executeQuery();
                    if (versionRS.next()) {
                        fromVersion = versionRS.getInt(1);
                    }
                }
                if (fromVersion > 0) {
                    File[] sqlFiles = sqlDir.listFiles();
                    migrations = new ArrayList<Integer>();
                    for (File sqlFile : sqlFiles) {
                        String firstPart = sqlFile.getName().substring(0, sqlFile.getName().indexOf('.'));
                        try {
                            int fileVersion = Integer.parseInt(firstPart);
                            if (fileVersion > fromVersion) {
                                migrations.add(fileVersion);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    if (!migrations.isEmpty()) {
                        Collections.sort(migrations);
                        for (Integer migration : migrations) {
                            migrationOn = migration;
                            File sqlFile = new File(sqlDirectory + (sqlDirectory.endsWith(File.separator) ? "" : File.separator) + migration + ".sql");
                            FileReader fr = new FileReader(sqlFile);
                            char[] buf = new char[(int) sqlFile.length()];
                            fr.read(buf);
                            String fileContents = new String(buf);
                            String[] commands = fileContents.split("\\;");
                            for (String command : commands) {
                                migrationCommand = command;
                                statement.execute(command);
                            }
                        }
                        PreparedStatement updateVersionStmt = conn.prepareStatement("UPDATE DATABASE_VERSION SET VERSION = ?");
                        updateVersionStmt.setInt(1, migrationOn);
                        updateVersionStmt.executeUpdate();
                    }
                } else {
                    migrationCommand = runCreateSQL(migrationCommand, statement);
                    migrationCommand = runInitialData(migrationCommand, statement);
                }
                conn.commit();
            } catch (Exception se) {
                conn.rollback();
                if (migrations != null) {
                    throw new BuildException("Encountered exception " + se.getMessage() + " while executing " + migrationCommand +
                            " from migration " + migrationOn + ".sql out of " +
                            "migrations " + migrations + ".");
                } else {
                    throw new BuildException("Encountered exception " + se.getMessage() + " while executing " + migrationCommand + " in " +
                            "building database from create.sql.");
                }
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BuildException) {
                throw (BuildException) e;
            } else {
                throw new BuildException(e);
            }
        }
    }

    private String runCreateSQL(String migrationCommand, Statement statement) throws IOException, SQLException {
        File createFile = new File(sqlDirectory + (sqlDirectory.endsWith(File.separator) ? "" : File.separator) +
            "create.sql");
        if (!createFile.exists()) {
            throw new BuildException("No create.sql found in " + sqlDirectory + ".");
        }
        FileReader fr = new FileReader(createFile);
        char[] buf = new char[(int) createFile.length()];
        fr.read(buf);
        String fileContents = new String(buf);
        String[] commands = fileContents.split("\\;");
        for (String command : commands) {
            migrationCommand = command;
            statement.execute(command);
        }
        return migrationCommand;
    }

    private String runInitialData(String migrationCommand, Statement statement) throws IOException, SQLException {
        File createFile = new File(sqlDirectory + (sqlDirectory.endsWith(File.separator) ? "" : File.separator) +
            "initial_data.sql");
        if (!createFile.exists()) {
            throw new BuildException("No inital_data.sql found in " + sqlDirectory + ".");
        }
        FileReader fr = new FileReader(createFile);
        char[] buf = new char[(int) createFile.length()];
        fr.read(buf);
        String fileContents = new String(buf);
        String[] commands = fileContents.split("\\;");
        for (String command : commands) {
            migrationCommand = command;
            statement.execute(command);
        }
        return migrationCommand;
    }
}
