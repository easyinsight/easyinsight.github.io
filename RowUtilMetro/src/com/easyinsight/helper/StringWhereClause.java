package com.easyinsight.helper;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:32 AM
 */
public class StringWhereClause extends WhereClause {

    private String value;

    public StringWhereClause(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    String toXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<where whereType\"string\">");
        xml.append("<key>");
        xml.append(getKey());
        xml.append("</key>");

        xml.append("<value>");
        xml.append(value);
        xml.append("</value>");

        xml.append("</where>");
        return xml.toString();
    }
}
