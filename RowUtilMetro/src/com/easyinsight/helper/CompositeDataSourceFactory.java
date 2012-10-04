package com.easyinsight.helper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void defineCompositeSource() throws EasyInsightException {
        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<defineCompositeDataSource>");
            xmlBuilder.append("<dataSourceName>");
            xmlBuilder.append(dataSourceName);
            xmlBuilder.append("</dataSourceName>");
            xmlBuilder.append("<dataSources>");
            for (String dataSource : dataSources) {
                xmlBuilder.append("<dataSource>");
                xmlBuilder.append(dataSource);
                xmlBuilder.append("</dataSource>");
            }
            xmlBuilder.append("</dataSources>");
            xmlBuilder.append("<connections>");

            for (DataSourceConnection connection : connections) {
                xmlBuilder.append("<connection>");

                xmlBuilder.append("<sourceDataSource>");
                xmlBuilder.append(connection.getSourceDataSource());
                xmlBuilder.append("</sourceDataSource>");

                xmlBuilder.append("<targetDataSource>");
                xmlBuilder.append(connection.getTargetDataSource());
                xmlBuilder.append("</targetDataSource>");

                xmlBuilder.append("<sourceDataSourceField>");
                xmlBuilder.append(connection.getSourceDataSourceField());
                xmlBuilder.append("</sourceDataSourceField>");

                xmlBuilder.append("<targetDataSourceField>");
                xmlBuilder.append(connection.getTargetDataSourceField());
                xmlBuilder.append("</targetDataSourceField>");

                xmlBuilder.append("</connection>");
            }

            xmlBuilder.append("</connections>");
            xmlBuilder.append("</defineCompositeDataSource>");
            String xml = xmlBuilder.toString();
            Document document = InternalUtil.sendXML(xml, apiKey, apiSecretKey, "defineCompositeDataSource");
            NodeList children = document.getChildNodes().item(0).getChildNodes();
            Map<String, String> results = new HashMap<String, String>();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                results.put(child.getNodeName(), child.getFirstChild().getNodeValue());
            }
            String code = results.get("code");
            if ("500".equals(code)) {
                throw new EasyInsightException(results.get("message"));
            } else if ("401".equals(code)) {
                throw new EasyInsightException("Your credentials were rejected by Easy Insight.");
            } else if ("400".equals(code)) {
                throw new EasyInsightException(results.get("message"));
            }
        } catch (EasyInsightException eie) {
            throw eie;
        } catch (Exception e) {
            throw new EasyInsightException(e);
        }
    }
}
