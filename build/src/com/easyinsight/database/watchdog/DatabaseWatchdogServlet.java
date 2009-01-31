package com.easyinsight.database.watchdog;

import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.apache.tools.ant.BuildException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.text.MessageFormat;
import java.sql.*;

/**
 * User: James Boe
 * Date: Jan 30, 2009
 * Time: 6:12:56 PM
 */
public class DatabaseWatchdogServlet extends HttpServlet {

    private static final String urlTemplate = "jdbc:mysql://{0}:{1}/{2}?user={3}&password={4}";

    private String databaseName;
    private String databaseUser;
    private String databasePassword;

    public DatabaseWatchdogServlet() throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("database.properties");
        properties.load(fis);
        databaseName = (String) properties.get("database.name");
        databaseUser = (String) properties.get("database.user");
        databasePassword = (String) properties.get("database.password");
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException {
        try {
            updateFromS3();
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void updateFromS3() throws Exception {
            AWSCredentials credentials = new AWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            RestS3Service s3Service = new RestS3Service(credentials);
            S3Bucket bucket = s3Service.getBucket("eisql");
            S3Object[] objects = s3Service.listObjects(bucket);
            for (S3Object object : objects) {
                S3Object retrievedObject = s3Service.getObject(bucket, object.getKey());
                byte retrieveBuf[];
                retrieveBuf = new byte[1];
                InputStream bfis = retrievedObject.getDataInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (bfis.read(retrieveBuf) != -1) {
                    baos.write(retrieveBuf);
                }
                byte[] resultBytes = baos.toByteArray();
                String fileContents = new String(resultBytes);
                File file = new File(object.getKey());
                FileWriter fw = new FileWriter(file);
                fw.write(fileContents.toCharArray());
                fw.close();
            }
            migrate();
    }

    private void migrate() {
        File sqlDir = new File(".");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = MessageFormat.format(urlTemplate, "localhost", "3306", databaseName, databaseUser, databasePassword);
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
                            File sqlFile = new File(migration + ".sql");
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
}
