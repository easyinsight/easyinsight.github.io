package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.Where;

import javax.xml.datatype.DatatypeConfigurationException;

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

    abstract void addToWhere(Where where) throws DatatypeConfigurationException;
}
