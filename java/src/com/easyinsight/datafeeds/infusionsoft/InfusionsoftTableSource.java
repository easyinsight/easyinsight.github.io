package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/17/13
 * Time: 8:57 AM
 */
public abstract class InfusionsoftTableSource extends ServerDataSourceDefinition {

    protected DataSet query(String table, List<AnalysisItem> fieldList, InfusionsoftCompositeSource infusionsoftCompositeSource) throws MalformedURLException, XmlRpcException {
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        List<String> fields = new ArrayList<String>();
        for (AnalysisItem field : fieldList) {
            fields.add(field.getKey().toKeyString());
            map.put(field.getKey().toKeyString(), field);
        }
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("https://"+infusionsoftCompositeSource.getUrl()+".infusionsoft.com:443/api/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        DataSet dataSet = new DataSet();

        //The secure encryption key
        boolean hasMoreResults;
        int page = 0;
        do {
            int count = 0;
            List parameters = new ArrayList();
            parameters.add(infusionsoftCompositeSource.getInfusionApiKey());
            parameters.add(table);
            parameters.add(1000);
            parameters.add(page);
            Map blah = new HashMap();
            parameters.add(blah);
            parameters.add(fields);
            Object[] results = (Object[]) client.execute("DataService.query", parameters);
            for (Object result : results) {
                Map resultMap = (Map) result;
                count++;
                IRow row = dataSet.createRow();
                for (String field : fields) {
                    Object value = resultMap.get(field);
                    AnalysisItem analysisItem = map.get(field);
                    if (value instanceof Date) {
                        row.addValue(field, new DateValue((Date) value));
                    } else if (value instanceof Number) {
                        if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                            Number number = (Number) value;
                            if (number instanceof Integer) {
                                row.addValue(field, String.valueOf(number));
                            } else {
                                row.addValue(field, (Number) value);
                            }
                        } else {
                            row.addValue(field, (Number) value);
                        }
                    } else {
                        row.addValue(field, (String) value);
                    }
                }
            }
            hasMoreResults = count == 1000;
            page++;
        } while (hasMoreResults);
        return dataSet;
    }

    public static void main(String[] args) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("https://bn136.infusionsoft.com:443/api/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        //The secure encryption key
        String key = "c9d1f3a9f6b2c7613776edf05d4304a6";
        List parameters = new ArrayList();
        parameters.add(key);
        parameters.add("Lead");
        parameters.add(1000);
        parameters.add(0);
        Map blah = new HashMap();
        parameters.add(blah);
        List fields = new ArrayList();
        fields.add("Id");
        fields.add("OpportunityTitle");
        fields.add("DateCreated");
        parameters.add(fields);
        Object[] results = (Object[]) client.execute("DataService.query", parameters);
        System.out.println(results);
    }
}
