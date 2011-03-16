package com.easyinsight.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:07 AM
 */
public class DataRow {
    private Map<String, String> stringValues = new HashMap<String, String>();
    private Map<String, Number> numberValues = new HashMap<String, Number>();
    private Map<String, Date> dateValues = new HashMap<String, Date>();

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public void addValue(String key, String value) {
        stringValues.put(key, value);
    }

    public void addValue(String key, Number value) {
        numberValues.put(key, value);
    }

    public void addValue(String key, Date value) {
        dateValues.put(key, value);
    }

    String toXML() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<row>");
        for (Map.Entry<String, String> entry : stringValues.entrySet()) {
            xmlBuilder.append("<");
            xmlBuilder.append(entry.getKey());
            xmlBuilder.append("><![CDATA[");

            xmlBuilder.append(entry.getValue());

            xmlBuilder.append("]]></");
            xmlBuilder.append(entry.getKey());
            xmlBuilder.append(">");
        }

        for (Map.Entry<String, Number> entry : numberValues.entrySet()) {
            xmlBuilder.append("<");
            xmlBuilder.append(entry.getKey());
            xmlBuilder.append(">");

            xmlBuilder.append(entry.getValue());

            xmlBuilder.append("</");
            xmlBuilder.append(entry.getKey());
            xmlBuilder.append(">");
        }

        for (Map.Entry<String, Date> entry : dateValues.entrySet()) {
            xmlBuilder.append("<");
            xmlBuilder.append(entry.getKey());
            xmlBuilder.append(">");
            if(entry.getValue() != null)
                xmlBuilder.append(dateFormat.format(entry.getValue()));

            xmlBuilder.append("</");
            xmlBuilder.append(entry.getKey());
            xmlBuilder.append(">");
        }
        xmlBuilder.append("</row>");
        return xmlBuilder.toString();
    }
}
