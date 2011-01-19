package com.easyinsight.helper;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:32 AM
 */
public class DayWhereClause extends WhereClause {
    private int dayOfYear;
    private int year;

    public DayWhereClause(String key, int dayOfYear, int year) {
        super(key);
        this.dayOfYear = dayOfYear;
        this.year = year;
    }

    @Override
    String toXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<where whereType\"string\">");
        xml.append("<key>");
        xml.append(getKey());
        xml.append("</key>");

        xml.append("<year>");
        xml.append(year);
        xml.append("</year>");

        xml.append("<day>");
        xml.append(dayOfYear);
        xml.append("</day>");

        xml.append("</where>");
        return xml.toString();
    }
}
