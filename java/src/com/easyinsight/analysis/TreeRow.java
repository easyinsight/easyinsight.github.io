package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:18 PM
 */
public class TreeRow {
    private Map<String, Object> values = new HashMap<String, Object>();
    private Value groupingColumn;
    private List<TreeRow> children = new ArrayList<TreeRow>();
    private boolean summaryColumn;
    private AnalysisItem groupingField;
    private int backgroundColor;
    private int textColor;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isSummaryColumn() {
        return summaryColumn;
    }

    public void setSummaryColumn(boolean summaryColumn) {
        this.summaryColumn = summaryColumn;
    }

    public AnalysisItem getGroupingField() {
        return groupingField;
    }

    public void setGroupingField(AnalysisItem groupingField) {
        this.groupingField = groupingField;
    }

    public Value getGroupingColumn() {
        return groupingColumn;
    }

    public void setGroupingColumn(Value groupingColumn) {
        this.groupingColumn = groupingColumn;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public List<TreeRow> getChildren() {
        return children;
    }

    public void setChildren(List<TreeRow> children) {
        this.children = children;
    }
}
