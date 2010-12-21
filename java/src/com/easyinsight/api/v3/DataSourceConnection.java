package com.easyinsight.api.v3;

/**
 * User: jamesboe
 * Date: 12/21/10
 * Time: 11:08 AM
 */
public class DataSourceConnection {
    private String sourceDataSource;
    private String sourceDataSourceField;
    private String targetDataSource;
    private String targetDataSourceField;

    public String getSourceDataSource() {
        return sourceDataSource;
    }

    public void setSourceDataSource(String sourceDataSource) {
        this.sourceDataSource = sourceDataSource;
    }

    public String getSourceDataSourceField() {
        return sourceDataSourceField;
    }

    public void setSourceDataSourceField(String sourceDataSourceField) {
        this.sourceDataSourceField = sourceDataSourceField;
    }

    public String getTargetDataSource() {
        return targetDataSource;
    }

    public void setTargetDataSource(String targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    public String getTargetDataSourceField() {
        return targetDataSourceField;
    }

    public void setTargetDataSourceField(String targetDataSourceField) {
        this.targetDataSourceField = targetDataSourceField;
    }
}
