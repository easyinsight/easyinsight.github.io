package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;
import com.easyinsight.core.Value;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 28, 2008
 * Time: 1:31:26 PM
 */
public class AnalysisDimensionResultMetadata extends AnalysisItemResultMetadata {

    private Set<Value> values = new HashSet<Value>();

    public void addValue(AnalysisItem analysisItem, Value value) {
        values.add(value);
    }

    public List<Value> getValues() {
        return new ArrayList<Value>(values);
    }

    public void setValues(List<Value> values) {
        this.values = new HashSet<Value>(values);
    }
}
