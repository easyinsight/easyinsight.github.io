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
 * Date: 1/6/11
 * Time: 10:45 AM
 */
public class DataSourceFactory {
    private String key;
    private String secretKey;

    private String dataSourceKey;

    DataSourceFactory(String key, String secretKey, String dataSourceKey) {
        this.key = key;
        this.secretKey = secretKey;
        this.dataSourceKey = dataSourceKey;
    }

    private List<FieldDefinition> fieldDefinitions = new ArrayList<FieldDefinition>();

    public void addPostalCode(String postalCodeName) {
        addPostalCode(postalCodeName, postalCodeName);
    }

    public void addPostalCode(String postalCodeName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(postalCodeName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.POSTAL_CODE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addLongitude(String longitudeName) {
        addLongitude(longitudeName, longitudeName);
    }

    public void addLongitude(String longitudeName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(longitudeName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.LONGITUDE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addLatitude(String latitudeName) {
        addLatitude(latitudeName, latitudeName);
    }

    public void addLatitude(String latitudeName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(latitudeName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.LATITUDE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addTags(String tagsName) {
        addTags(tagsName, tagsName);
    }

    public void addTags(String tagsName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(tagsName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.TAGS);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addGrouping(String groupingName) {
        addGrouping(groupingName, groupingName);
    }

    public void addGrouping(String groupingName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(groupingName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.GROUPING);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addDate(String dateName) {
        addDate(dateName, dateName);
    }

    public void addDate(String dateName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(dateName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.DATE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addMeasure(String measureName) {
        addMeasure(measureName, measureName);
    }

    public void addMeasure(String measureName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(measureName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.MEASURE);
        fieldDefinitions.add(fieldDefinition);
    }
    /**
     * Defines the actual data source inside of Easy Insight and returns a <code>DataSourceOperationFactory</code>
     * object that you can use to publish data into the data source.
     */
    public DataSourceOperationFactory defineDataSource() {
        try {
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<defineDataSource>");
            xmlBuilder.append("<dataSourceName><![CDATA[");
            xmlBuilder.append(dataSourceKey);
            xmlBuilder.append("]]></dataSourceName>");
            xmlBuilder.append("<fields>");
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                xmlBuilder.append("<field dataType=\"");
                switch (fieldDefinition.getFieldType()) {
                    case DATE:
                        xmlBuilder.append("date");
                        break;
                    case GROUPING:
                        xmlBuilder.append("grouping");
                        break;
                    case LATITUDE:
                        xmlBuilder.append("latitude");
                        break;
                    case LONGITUDE:
                        xmlBuilder.append("longitude");
                        break;
                    case MEASURE:
                        xmlBuilder.append("measure");
                        break;
                    case POSTAL_CODE:
                        xmlBuilder.append("postal");
                        break;
                    case TAGS:
                        xmlBuilder.append("tags");
                        break;
                }
                xmlBuilder.append("\">");
                xmlBuilder.append("<key><![CDATA[");
                xmlBuilder.append(fieldDefinition.getInternalName());
                xmlBuilder.append("]]></key>");
                xmlBuilder.append("<name><![CDATA[");
                xmlBuilder.append(fieldDefinition.getDisplayName());
                xmlBuilder.append("]]></name>");
                xmlBuilder.append("</field>");
            }
            xmlBuilder.append("</fields>");
            xmlBuilder.append("</defineDataSource>");
            String xml = xmlBuilder.toString();
            System.out.println(xml);
            Document document = InternalUtil.sendXML(xml, key, secretKey, "defineDataSource");
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
            String dataSourceKey = results.get("dataSourceKey");
            return new DataSourceOperationFactory(key, secretKey, dataSourceKey);
        } catch (EasyInsightException eie) {
            throw eie;
        } catch (Exception e) {
            throw new EasyInsightException(e);
        }
    }
}
