package com.easyinsight.helper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * User: jamesboe
 * Date: 12/28/10
 * Time: 12:57 PM
 */
public class TransactionTarget {
    private List<DataRow> rows = new LinkedList<DataRow>();
    private int bufferSize;

    private String dataSourceKey;
    private String apiKey;
    private String apiSecretKey;

    private String transactionID;

    private String callDataID;

    private boolean replaceData;

    TransactionTarget(String dataSourceKey, String apiKey, String apiSecretKey, boolean replaceData, int bufferSize) {
        this.dataSourceKey = dataSourceKey;
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.replaceData = replaceData;
        this.bufferSize = bufferSize;
    }

    public void setCallDataID(String callDataID) {
        this.callDataID = callDataID;
    }

    public void beginTransaction() {
        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<beginTransaction>");
            xmlBuilder.append("<dataSourceName><![CDATA[");
            xmlBuilder.append(dataSourceKey);
            xmlBuilder.append("]]></dataSourceName>");
            xmlBuilder.append("<operation>");
            xmlBuilder.append(replaceData ? "replace" : "add");
            xmlBuilder.append("</operation>");
            xmlBuilder.append("</beginTransaction>");
            Document document = InternalUtil.sendXML(xmlBuilder.toString(), apiKey, apiSecretKey, "beginTransaction");
            NodeList children = document.getChildNodes().item(0).getChildNodes();
            Map<String, String> results = new HashMap<String, String>();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                results.put(child.getNodeName(), child.getFirstChild().getNodeValue());
                System.out.println(child.getNodeName());
                System.out.println("\t" + child.getFirstChild().getNodeValue());
            }
            String code = results.get("code");
            if ("500".equals(code)) {
                throw new EasyInsightException(results.get("message"));
            } else if ("401".equals(code)) {
                throw new EasyInsightException("Your credentials were rejected by Easy Insight.");
            } else if ("400".equals(code)) {
                throw new EasyInsightException(results.get("message"));
            }
            transactionID = results.get("transactionID");
        } catch (EasyInsightException eie) {
            throw eie;
        } catch (Exception e) {
            throw new EasyInsightException(e);
        }
    }

    public void commit() {
        try {
            flush();
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<commit>");
            xmlBuilder.append("<transactionID>");
            xmlBuilder.append(transactionID);
            xmlBuilder.append("</transactionID>");
            xmlBuilder.append("<callDataID>");
            xmlBuilder.append(callDataID);
            xmlBuilder.append("</callDataID>");
            xmlBuilder.append("</commit>");
            Document document = InternalUtil.sendXML(xmlBuilder.toString(), apiKey, apiSecretKey, "commit");
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

    public void rollback() {
    }

    public void flush() {
        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<rows ");

            xmlBuilder.append("transactionID=\"");
            xmlBuilder.append(transactionID);
            xmlBuilder.append("\">");

            for (DataRow row : rows) {
                xmlBuilder.append(row.toXML());
            }
            xmlBuilder.append("</rows>");
            Document document = InternalUtil.sendXML(xmlBuilder.toString(), apiKey, apiSecretKey, "loadRows");
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
            rows = new LinkedList<DataRow>();
        } catch (EasyInsightException eie) {
            throw eie;
        } catch (Exception e) {
            throw new EasyInsightException(e);
        }
    }

    private void checkCapacity() {
        if (rows.size() == bufferSize) {
            flush();
        }
    }

    public DataRow newRow() {
        checkCapacity();
        DataRow dataRow = new DataRow();
        rows.add(dataRow);
        return dataRow;
    }
}
