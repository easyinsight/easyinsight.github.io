package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;
import com.easyinsight.html.FilterUtils;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 28, 2008
 * Time: 1:31:26 PM
 */
public class AnalysisDimensionResultMetadata extends AnalysisItemResultMetadata {

    private Collection<Value> values = new HashSet<Value>();
    private List<String> strings;

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

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public void calculateCaches() {
        strings = new ArrayList<String>(values.size());


        for (Value value : values) {
            strings.add(FilterUtils.toFilterString(value));
        }
        strings.remove("");
        Collections.sort(strings, new Comparator<String>() {

            public int compare(String s, String s1) {
                return s.compareToIgnoreCase(s1);
            }
        });
        strings.add(0, "[ No Value ]");
        values = null;
    }
}
