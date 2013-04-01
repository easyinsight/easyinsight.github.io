package com.easyinsight.datafeeds.json;

import java.util.List;

/**
 * User: jamesboe
 * Date: 4/1/13
 * Time: 10:05 AM
 */
public class JSONSetup {
    private String result;
    private String fieldLine;
    private List<String> fields;
    private int results;

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public String getFieldLine() {
        return fieldLine;
    }

    public void setFieldLine(String fieldLine) {
        this.fieldLine = fieldLine;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
