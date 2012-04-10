package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/4/12
 * Time: 10:27 AM
 */
public class ActualRowLayoutItem {
    private int columns;
    private List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
    private int columnWidth;
    private int formLabelWidth;

    public int getFormLabelWidth() {
        return formLabelWidth;
    }

    public void setFormLabelWidth(int formLabelWidth) {
        this.formLabelWidth = formLabelWidth;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<AnalysisItem> getAnalysisItems() {
        return analysisItems;
    }

    public void setAnalysisItems(List<AnalysisItem> analysisItems) {
        this.analysisItems = analysisItems;
    }
}
