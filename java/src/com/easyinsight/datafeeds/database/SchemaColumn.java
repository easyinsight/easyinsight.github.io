package com.easyinsight.datafeeds.database;

import java.util.List;

/**
 * User: jamesboe
 * Date: 5/29/13
 * Time: 1:27 PM
 */
public class SchemaColumn {
    private String name;
    private List<SchemaColumn> schemaColumns;

    public List<SchemaColumn> getSchemaColumns() {
        return schemaColumns;
    }

    public void setSchemaColumns(List<SchemaColumn> schemaColumns) {
        this.schemaColumns = schemaColumns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
