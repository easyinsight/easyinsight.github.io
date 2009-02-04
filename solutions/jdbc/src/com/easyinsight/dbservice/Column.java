package com.easyinsight.dbservice;

/**
 * User: James Boe
 * Date: Feb 1, 2009
 * Time: 11:25:28 AM
 */
public class Column {
    private String type;
    private String name;

    public Column() {
    }

    public Column(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
