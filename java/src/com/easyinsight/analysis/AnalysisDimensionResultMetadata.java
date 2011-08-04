package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Value;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 28, 2008
 * Time: 1:31:26 PM
 */
public class AnalysisDimensionResultMetadata extends AnalysisItemResultMetadata {

    private Collection<Value> values = new HashSet<Value>();

    public void addValue(AnalysisItem analysisItem, Value value, InsightRequestMetadata insightRequestMetadata) {
        values.add(value);
    }

    public List<Value> getValues() {
        values = new ArrayList<Value>(values);
        return (List<Value>) values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}
