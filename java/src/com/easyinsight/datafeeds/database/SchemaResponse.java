package com.easyinsight.datafeeds.database;

import java.util.List;

/**
 * User: jamesboe
 * Date: 5/29/13
 * Time: 2:14 PM
 */
public class SchemaResponse {
    private String error;
    private List<SchemaTable> schemaTables;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SchemaTable> getSchemaTables() {
        return schemaTables;
    }

    public void setSchemaTables(List<SchemaTable> schemaTables) {
        this.schemaTables = schemaTables;
    }
}
