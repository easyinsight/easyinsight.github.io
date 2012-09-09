package com.easyinsight.datafeeds.file;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.userupload.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

/**
 * User: James Boe
 * Date: Jul 13, 2008
 * Time: 10:26:47 AM
 */
public class FileBasedFeedDefinition extends ServerDataSourceDefinition {

    public static final int HSSF_MODEL = 1;
    public static final int XSSF_MODEL = 2;

    public static final int GET = 1;
    public static final int POST = 2;

    private UploadFormat uploadFormat;

    private String url;

    private int httpMethod = GET;

    private String userName;

    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        return new ArrayList<AnalysisItem>();
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UploadFormat getUploadFormat() {
        return uploadFormat;
    }

    public void setUploadFormat(UploadFormat uploadFormat) {
        this.uploadFormat = uploadFormat;
    }

    @Override
    public int getDataSourceType() {
        if (url != null && !"".equals(url)) {
            return DataSourceInfo.STORED_PULL;
        }
        return DataSourceInfo.STORED_PUSH;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, java.util.Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, java.util.Date lastRefreshDate) throws ReportException {
        try {
            AnalysisMeasure rowCount = null;
            for (AnalysisItem field : getFields()) {
                if (field.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) field;
                    if (measure.isRowCountField()) {
                        rowCount = measure;
                    }
                }
            }
            HttpClient client = new HttpClient();
            if (userName != null && !"".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            HttpMethod restMethod;
            if (httpMethod == GET) {
                restMethod = new GetMethod(url);
            } else if (httpMethod == POST) {
                restMethod = new PostMethod(url);
            } else {
                throw new RuntimeException("Unknown http method " + httpMethod);
            }
            client.executeMethod(restMethod);
            if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("We were unable to authenticate against " + url + " with the specified credentials.", this));
            } else if (restMethod.getStatusCode() == 404) {
                throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a downloadable file at " + url + ".", this));
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bis = new BufferedInputStream(restMethod.getResponseBodyAsStream(), 8192);
            byte[] buffer = new byte[8192];
            BufferedOutputStream bufOS = new BufferedOutputStream(baos, 8192);
            int nBytes;
            while ((nBytes = bis.read(buffer)) != -1) {
                bufOS.write(buffer, 0, nBytes);
            }
            bufOS.flush();
            PersistableDataSetForm form = uploadFormat.createDataSet(baos.toByteArray(), getFields());
            return form.toDataSet(rowCount);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM FILE_BASED_DATA_SOURCE WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO FILE_BASED_DATA_SOURCE (AUTH_USERNAME, AUTH_PASSWORD," +
                "ENDPOINT, HTTP_METHOD, DATA_SOURCE_ID) VALUES (?, ?, ?, ?, ?)");
        insertStmt.setString(1, getUserName());
        insertStmt.setString(2, getPassword() != null ? PasswordStorage.encryptString(getPassword()) : null);
        insertStmt.setString(3, getUrl());
        insertStmt.setInt(4, getHttpMethod());
        insertStmt.setLong(5, getDataFeedID());
        insertStmt.execute();
        uploadFormat.persist(conn, getDataFeedID());
    }

    public FeedType getFeedType() {
        return FeedType.STATIC;
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement stmt = conn.prepareStatement("SELECT AUTH_USERNAME, AUTH_PASSWORD, ENDPOINT, HTTP_METHOD FROM FILE_BASED_DATA_SOURCE WHERE " +
                "DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        ResultSet dsRS = stmt.executeQuery();
        if (dsRS.next()) {
            userName = dsRS.getString(1);
            String password = dsRS.getString(2);
            if (!dsRS.wasNull()) {
                setPassword(PasswordStorage.decryptString(password));
            }
            url = dsRS.getString(3);
            httpMethod = dsRS.getInt(4);
        }
        UploadFormat uploadFormat;
        PreparedStatement excelFormatStmt = conn.prepareStatement("SELECT EXCEL_MODEL FROM EXCEL_UPLOAD_FORMAT WHERE " +
                "FEED_ID = ?");
        excelFormatStmt.setLong(1, getDataFeedID());
        ResultSet rs = excelFormatStmt.executeQuery();
        if (rs.next()) {
            int model = rs.getInt(1);
            if (model == HSSF_MODEL)
                uploadFormat = new ExcelUploadFormat();
            else
                uploadFormat = new XSSFExcelUploadFormat();
        } else {
            PreparedStatement delimitedFormatStmt = conn.prepareStatement("SELECT DELIMITER_PATTERN, DELIMITER_ESCAPE FROM " +
                    "FLAT_FILE_UPLOAD_FORMAT WHERE FEED_ID = ?");
            delimitedFormatStmt.setLong(1, getDataFeedID());
            rs = delimitedFormatStmt.executeQuery();
            if (rs.next()) {
                String pattern = rs.getString(1);
                String escape = rs.getString(2);
                if (rs.wasNull()) {
                    escape = null;
                }
                if(!pattern.equals(","))
                    uploadFormat = new FlatFileUploadFormat(pattern, escape);
                else
                    uploadFormat = new CsvFileUploadFormat();
            } else {
                throw new RuntimeException("Could not match feed to an upload format");
            }
            delimitedFormatStmt.close();
        }
        excelFormatStmt.close();
        this.uploadFormat = uploadFormat;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }
}
