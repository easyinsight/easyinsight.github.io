package com.easyinsight.helper;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:30 AM
 */
public abstract class WhereClause {

    private String key;

    protected WhereClause(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    abstract String toXML();
}
