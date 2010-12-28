package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.BasicAuthEIV3APIService;
import com.easyinsight.rowutil.v3web.DataSourceConnection;
import com.easyinsight.rowutil.v3web.EIDataV3;

import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:13 AM
 */
public class CompositeDataSourceFactory {
    private String dataSourceName;
    private String apiKey;
    private String apiSecretKey;

    public CompositeDataSourceFactory(String dataSourceName, String apiKey, String apiSecretKey) {
        this.dataSourceName = dataSourceName;
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
    }

    private List<DataSourceConnection> connections = new ArrayList<DataSourceConnection>();
    private List<String> dataSources = new ArrayList<String>();

    /**
     * Adds the specified data source to the composite data source definition.
     * @param dataSourceKey
     */
    public void addDataSource(String dataSourceKey) {
        dataSources.add(dataSourceKey);
    }

    /**
     * Defines a join between the specified source and target data sources, using the specified two fields
     * as the actual join columns.
     * @param sourceDataSource
     * @param targetDataSource
     * @param sourceField
     * @param targetField
     */
    public void joinDataSources(String sourceDataSource, String targetDataSource, String sourceField, String targetField) {
        DataSourceConnection dataSourceConnection = new DataSourceConnection();
        dataSourceConnection.setSourceDataSource(sourceDataSource);
        dataSourceConnection.setTargetDataSource(targetDataSource);
        dataSourceConnection.setSourceDataSourceField(sourceField);
        dataSourceConnection.setTargetDataSourceField(targetField);
        connections.add(dataSourceConnection);
    }

    /**
     * Defines the composite data source using the specified data sources and joins.
     */

    public void defineCompositeSource() {
        BasicAuthEIV3APIService service = new BasicAuthEIV3APIService();
        EIDataV3 port = service.getBasicAuthEIV3APIPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, apiKey);
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, apiSecretKey);
        port.defineCompositeDataSource(dataSources, connections, dataSourceName, null);
    }
}
