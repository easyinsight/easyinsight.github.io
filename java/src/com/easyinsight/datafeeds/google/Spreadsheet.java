package com.easyinsight.datafeeds.google;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 11, 2009
 * Time: 8:11:19 PM
 */
public class Spreadsheet {
    private String title;
    private List<Worksheet> children = new ArrayList<Worksheet>();

    public List<Worksheet> getChildren() {
        return children;
    }

    public void setChildren(List<Worksheet> children) {
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
