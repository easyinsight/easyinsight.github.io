package com.easyinsight.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/3/11
 * Time: 9:45 AM
 */
public class CompareYearsRow {
    private AnalysisItem measure;
    private Map<String, CompareYearsResult> results = new HashMap<String, CompareYearsResult>();

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public Map<String, CompareYearsResult> getResults() {
        return results;
    }

    public void setResults(Map<String, CompareYearsResult> results) {
        this.results = results;
    }
}
