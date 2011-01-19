package com.easyinsight.helper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 9:59 AM
 */
public class DataSourceTarget {

    public static final int ADD_ROWS = 1;
    public static final int REPLACE_ROWS = 2;
    public static final int UPDATE_ROWS = 3;

    private String dataSourceKey;
    private String apiKey;
    private String apiSecretKey;

    private int operation;

    private List<DataRow> rows = new ArrayList<DataRow>();

    DataSourceTarget(String dataSourceKey, String apiKey, String apiSecretKey, int operation) {
        this.dataSourceKey = dataSourceKey;
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.operation = operation;
    }

    void setWhereClauses(WhereClause[] whereClauses) {
        this.whereClauses = Arrays.asList(whereClauses);
    }

    private List<WhereClause> whereClauses;

    /**
     * Provides you with a <code>DataRow</code> object, representing one row of data in the data set you're adding
     * to the data source in Easy Insight.
     */
    public DataRow newRow() {
        DataRow dataRow = new DataRow();
        rows.add(dataRow);
        return dataRow;
    }

    /**
     * Flushes all the rows you've created on this object and publishes them into Easy Insight.
     */
    public void flush() throws EasyInsightException {


        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<rows ");
            if (operation == ADD_ROWS || operation == REPLACE_ROWS) {
                xmlBuilder.append("dataSourceName=\"");
                xmlBuilder.append(dataSourceKey);
                xmlBuilder.append("\">");
            } else {
                xmlBuilder.append(">");
            }

            for (DataRow row : rows) {
                xmlBuilder.append(row.toXML());
            }
            xmlBuilder.append("</rows>");

            Document document;
            if (operation == ADD_ROWS) {
                document = InternalUtil.sendXML(xmlBuilder.toString(), apiKey, apiSecretKey, "addRows");
            } else if (operation == REPLACE_ROWS) {
                document = InternalUtil.sendXML(xmlBuilder.toString(), apiKey, apiSecretKey, "replaceRows");
            } else if (operation == UPDATE_ROWS) {
                StringBuilder updateBuilder = new StringBuilder();
                updateBuilder.append("<update ");
                updateBuilder.append("dataSourceName=\"");
                updateBuilder.append(dataSourceKey);
                updateBuilder.append("\">");
                updateBuilder.append(xmlBuilder.toString());
                updateBuilder.append("<wheres>");
                for (WhereClause whereClause : whereClauses) {
                    updateBuilder.append(whereClause.toXML());
                }
                updateBuilder.append("</wheres>");
                updateBuilder.append("</update>");
                document = InternalUtil.sendXML(updateBuilder.toString(), apiKey, apiSecretKey, "updateRows");
            } else {
                throw new RuntimeException();
            }

            NodeList children = document.getChildNodes().item(0).getChildNodes();
            Map<String, String> results = new HashMap<String, String>();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                results.put(child.getNodeName(), child.getFirstChild().getNodeValue());
            }
            String code = results.get("code");
            if (!"200".equals(code)) {
                throw new EasyInsightException(results.get("message"));
            }
        } catch (EasyInsightException eie) {
            throw eie;
        } catch (Exception e) {
            throw new EasyInsightException(e);
        }
    }
}
