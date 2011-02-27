package com.easyinsight.datafeeds.custom;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.custom.client.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 8, 2009
 * Time: 2:59:42 PM
 */
public class CustomDataSource extends ServerDataSourceDefinition {

    private static final QName SERVICE_NAME = new QName("http://db.easyinsight.com/", "DatabaseServerService");

    private String wsdl;
    private Map<String, String> properties;

    private DataProvider port;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CUSTOM;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        return new DataSet();
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new CustomDataFeed(wsdl);
    }

    @Override
    public String validateCredentials() {
        try {
            URL url = new URL(wsdl);
            DataProviderService ss = new DataProviderService(url, SERVICE_NAME);
            DataProvider dataProvider = ss.getDataProviderPort();
            BindingProvider provider = (BindingProvider) dataProvider;
            Map<String, Object> requestContext = provider.getRequestContext();
            /*requestContext.put(BindingProvider.USERNAME_PROPERTY, credentials.getUserName());
            requestContext.put(BindingProvider.PASSWORD_PROPERTY, credentials.getPassword());*/
            dataProvider.getFields();
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public DataProvider getPort(Credentials credentials) {
        try {
            if (port == null) {
                URL url = new URL(wsdl);
                DataProviderService ss = new DataProviderService(url, SERVICE_NAME);
                port = ss.getDataProviderPort();
                BindingProvider provider = (BindingProvider) port;
                Map<String, Object> requestContext = provider.getRequestContext();
                requestContext.put(BindingProvider.USERNAME_PROPERTY, credentials.getUserName());
                requestContext.put(BindingProvider.PASSWORD_PROPERTY, credentials.getPassword());
            }
            return port;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return new ArrayList<String>();
    }

    @Override
    public Map<String, Key> newDataSourceFields() {
        /*Map<String, Key> fieldMap = new HashMap<String, Key>();
        Folder folder = getPort(credentials).getFields();
        List<String> keys = recurseFields(folder);
        for (String field : keys) {
            fieldMap.put(field, new NamedKey(field));
        }
        return fieldMap;*/
        throw new UnsupportedOperationException();
    }

    private List<String> recurseFields(Folder folder) {
        List<String> fields = new ArrayList<String>();
        if (folder.getFolders() != null) {
            for (Field field : folder.getFields()) {
                fields.add(field.getKey());
            }
        }
        if (folder.getFolders() != null) {
            for (Folder childFolder : folder.getFolders()) {
                fields.addAll(recurseFields(childFolder));
            }
        }
        return fields;
    }

    private List<AnalysisItem> recurseItems(Map<String, Key> keys, Folder base) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        for (Folder folder : base.getFolders()) {
            FeedFolder feedFolder = defineFolder(folder.getName());
            List<AnalysisItem> childItems = recurseItems(keys, folder);
            analysisItems.addAll(childItems);
            for (AnalysisItem childItem : childItems) {
                feedFolder.addAnalysisItem(childItem);
            }
        }
        for (Field field : base.getFields()) {
            Key key = keys.get(field.getKey());
            analysisItems.add(createAnalysisItem(key, field));
        }
        return analysisItems;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        /*Folder base = getPort(credentials).getFields();
        return recurseItems(keys, base);*/
        throw new UnsupportedOperationException();
    }

    private AnalysisItem createAnalysisItem(Key key, Field field) {
        FieldType fieldType = field.getFieldType();
        
        AnalysisItem analysisItem;

        if (fieldType == FieldType.DATE) {
            analysisItem = new AnalysisDateDimension(key, true, AnalysisDateDimension.DAY_LEVEL);
        } else if (fieldType == FieldType.GROUPING) {
            analysisItem = new AnalysisDimension(key, true);
        } else if (fieldType == FieldType.MEASURE) {
            analysisItem = new AnalysisMeasure(key, AggregationTypes.SUM);
        } else if (fieldType == FieldType.TAGS) {
            analysisItem = new AnalysisList(key, true, ",");
        } else {
            throw new RuntimeException();
        }
        analysisItem.setDisplayName(field.getDisplayName());
        return analysisItem;
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM CUSTOM_DATA_SOURCE WHERE DATA_FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement basecampStmt = conn.prepareStatement("INSERT INTO CUSTOM_DATA_SOURCE (DATA_FEED_ID, WSDL_URL) VALUES (?, ?)");
        basecampStmt.setLong(1, getDataFeedID());
        basecampStmt.setString(2, getWsdl());
        basecampStmt.execute();
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT WSDL_URL FROM CUSTOM_DATA_SOURCE WHERE DATA_FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            this.setWsdl(rs.getString(1));
        }
    }
}
