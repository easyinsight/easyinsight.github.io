package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/17/13
 * Time: 8:57 AM
 */
public abstract class InfusionsoftTableSource extends ServerDataSourceDefinition {

    protected DataSet query(String table, List<AnalysisItem> fieldList, InfusionsoftCompositeSource infusionsoftCompositeSource) throws MalformedURLException, XmlRpcException {
        return query(table, fieldList, infusionsoftCompositeSource, new HashSet<String>());
    }

    @Override
    public boolean isMigrateRequired() {
        return false;
    }

    protected DataSet query(String table, List<AnalysisItem> fieldList, InfusionsoftCompositeSource infusionsoftCompositeSource, Collection<String> skip) throws MalformedURLException, XmlRpcException {
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        List<String> fields = new ArrayList<String>();
        for (AnalysisItem field : fieldList) {
            if (skip.contains(field.getKey().toKeyString())) {
                continue;
            }
            if (Character.isDigit(field.getKey().toKeyString().charAt(0))) {
                fields.add("_" + field.toDisplay());
                map.put("_" + field.toDisplay(), field);
            } else {
                fields.add(field.getKey().toKeyString());
                map.put(field.getKey().toKeyString(), field);
            }
        }
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEncoding("UTF-8");
        String url = infusionsoftCompositeSource.getUrl()+":443/api/xmlrpc";
        config.setServerURL(new URL(url));
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
            Map emptyMap = new HashMap();
            parameters.add(emptyMap);
            parameters.add(fields);
            Object[] results = new Object[0];
            try {
                results = (Object[]) client.execute("DataService.query", parameters);
            } catch (XmlRpcException e) {
                LogClass.error(e);
                count = 1000;
            }
            for (Object result : results) {
                Map resultMap = (Map) result;
                count++;
                IRow row = dataSet.createRow();
                for (String field : fields) {
                    if (skip.contains(field)) {
                        continue;
                    }
                    Object value = resultMap.get(field);
                    AnalysisItem analysisItem = map.get(field);
                    if (value instanceof Date) {
                        Date dateValue = (Date) value;
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_YEAR, -1);
                        if (dateValue.after(c.getTime())) {
                            System.out.println("started with " + dateValue);
                        }
                        ZonedDateTime lzdt = dateValue.toInstant().atZone(ZoneId.systemDefault());



                        if (dateValue.after(c.getTime())) {
                            System.out.println("then got " + lzdt);
                        }

                        ZonedDateTime offset = ZonedDateTime.of(lzdt.getYear(), lzdt.getMonthValue(), lzdt.getDayOfMonth(),
                                lzdt.getHour(), lzdt.getMinute(), lzdt.getNano(), lzdt.getSecond(), infusionsoftCompositeSource.getTimezone());

                        /*LocalDateTime offset = LocalDateTime.of(lzdt.getYear(), lzdt.getMonthValue(), lzdt.getDayOfMonth(),
                                lzdt.getHour(), lzdt.getMinute(), lzdt.getNano(), lzdt.getSecond());*/
                        //ZonedDateTime lzdt = dateValue.toInstant().atZone(infusionsoftCompositeSource.getTimezone());

                        //ZonedDateTime zdt = lzdt.withZoneSameInstant(ZoneId.systemDefault());
                        if (dateValue.after(c.getTime())) {
                            System.out.println("ended with " + offset);
                        }

                        //lzdt.withZoneSameLocal(ZoneId.systemDefault());

                        ZonedDateTime zdt = lzdt.withZoneSameLocal(ZoneId.systemDefault());

                        if (dateValue.after(c.getTime())) {
                            System.out.println("and back to " + zdt);
                        }

                        row.addValue(analysisItem.getKey(), new DateValue(Date.from(zdt.toInstant())));
                    } else if (value instanceof Number) {
                        if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                            Number number = (Number) value;
                            if (number instanceof Integer) {
                                row.addValue(analysisItem.getKey(), String.valueOf(number));
                            } else {
                                row.addValue(analysisItem.getKey(), (Number) value);
                            }
                        } else {
                            row.addValue(analysisItem.getKey(), (Number) value);
                        }
                    } else {
                        if (value != null) {
                            row.addValue(analysisItem.getKey(), value.toString());
                        }
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
