package com.easyinsight.datafeeds.infusionsoft;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/16/13
 * Time: 1:35 PM
 */
public class InfusionsoftCustomCache {

    private Map<Integer, List<CustomField>> customFieldMap = new HashMap<Integer, List<CustomField>>();

    public Map<Integer, List<CustomField>> getCustomFieldMap() {
        return customFieldMap;
    }

    protected void query(InfusionsoftCompositeSource infusionsoftCompositeSource) {
        try {
            List<String> fields = new ArrayList<String>();
            fields.add("DataType");
            fields.add("Id");
            fields.add("FormId");
            fields.add("Name");
            fields.add("Label");
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            String url = infusionsoftCompositeSource.getUrl()+":443/api/xmlrpc";
            config.setServerURL(new URL(url));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);


            //The secure encryption key
            boolean hasMoreResults;
            int page = 0;
            do {
                int count = 0;
                List parameters = new ArrayList();
                parameters.add(infusionsoftCompositeSource.getInfusionApiKey());
                parameters.add("DataFormField");
                parameters.add(1000);
                parameters.add(page);
                Map emptyMap = new HashMap();
                parameters.add(emptyMap);
                parameters.add(fields);
                Object[] results = (Object[]) client.execute("DataService.query", parameters);
                for (Object result : results) {
                    Map resultMap = (Map) result;
                    int dataType = (Integer) resultMap.get("DataType");
                    int id = (Integer) resultMap.get("Id");
                    int formID = (Integer) resultMap.get("FormId");
                    String name = (String) resultMap.get("Name");
                    String label = (String) resultMap.get("Label");
                    CustomField customField = new CustomField(id, dataType, name, label);
                    List<CustomField> customFields = customFieldMap.get(formID);
                    if (customFields == null) {
                        customFields = new ArrayList<CustomField>();
                        customFieldMap.put(formID, customFields);
                    }
                    customFields.add(customField);
                    count++;
                }
                hasMoreResults = count == 1000;
                page++;
            } while (hasMoreResults);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
