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
public class BulkUpdateDataSourceTarget {

    private String dataSourceKey;
    private String apiKey;
    private String apiSecretKey;


    public BulkUpdateDataSourceTarget(String dataSourceKey, String apiKey, String apiSecretKey) {
        this.dataSourceKey = dataSourceKey;
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
    }

    private List<Update> updates = new ArrayList<Update>();

    public Update createUpdate() {
        Update update = new Update();
        updates.add(update);
        return update;
    }



    /**
     * Flushes all the rows you've created on this object and publishes them into Easy Insight.
     */
    public void flush() throws EasyInsightException {


        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<updates ");
            xmlBuilder.append("dataSourceName=\"");
            xmlBuilder.append(dataSourceKey);
            xmlBuilder.append("\">");

            for (Update update : updates) {
                xmlBuilder.append(update.toXML());
            }

            xmlBuilder.append("</updates>");

            Document document = InternalUtil.sendXML(xmlBuilder.toString(), apiKey, apiSecretKey, "bulkUpdateRows");


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
            updates = new ArrayList<Update>();
        } catch (EasyInsightException eie) {
            throw eie;
        } catch (Exception e) {
            throw new EasyInsightException(e);
        }
    }

}
