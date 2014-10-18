package com.easyinsight.datafeeds.json;

import com.easyinsight.analysis.AnalysisItem;

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
    private List<AnalysisItem> generatedFields;
    private int results;
    private String suggestedJSONPath;


    public String getSuggestedJSONPath() {
        return suggestedJSONPath;
    }

    public void setSuggestedJSONPath(String suggestedJSONPath) {
        this.suggestedJSONPath = suggestedJSONPath;
    }

    public List<AnalysisItem> getGeneratedFields() {
        return generatedFields;
    }

    public void setGeneratedFields(List<AnalysisItem> generatedFields) {
        this.generatedFields = generatedFields;
    }

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
