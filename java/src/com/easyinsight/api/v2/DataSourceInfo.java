package com.easyinsight.api.v2;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 12:16:29 PM
 */
public class DataSourceInfo {
    private String dataSourceName;
    private String apiKey;
    private String[] stringFields;
    private String[] numberFields;
    private String[] dateFields;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String[] getStringFields() {
        return stringFields;
    }

    public void setStringFields(String[] stringFields) {
        this.stringFields = stringFields;
    }

    public String[] getNumberFields() {
        return numberFields;
    }

    public void setNumberFields(String[] numberFields) {
        this.numberFields = numberFields;
    }

    public String[] getDateFields() {
        return dateFields;
    }

    public void setDateFields(String[] dateFields) {
        this.dateFields = dateFields;
    }
}
