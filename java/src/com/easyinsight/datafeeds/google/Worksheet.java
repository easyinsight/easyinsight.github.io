package com.easyinsight.datafeeds.google;


import com.easyinsight.core.DataSourceDescriptor;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 1:24:52 PM
 */
public class Worksheet {
    private String title;
    private String url;
    private String spreadsheet;
    private DataSourceDescriptor feedDescriptor;

    public DataSourceDescriptor getFeedDescriptor() {
        return feedDescriptor;
    }

    public void setFeedDescriptor(DataSourceDescriptor feedDescriptor) {
        this.feedDescriptor = feedDescriptor;
    }

    public String getSpreadsheet() {
        return spreadsheet;
    }

    public void setSpreadsheet(String spreadsheet) {
        this.spreadsheet = spreadsheet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return title + " - " + url;
    }
}
