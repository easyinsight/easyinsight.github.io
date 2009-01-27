package com.easyinsight.webservice;

/**
 * User: James Boe
 * Date: Aug 13, 2008
 * Time: 11:31:25 AM
 */
public class ShortCrosstabDefinition {
    private WSAnalysisItem[] rowItems;
    private WSAnalysisItem[] columnItems;
    private WSAnalysisItem[] measureItems;

    public WSAnalysisItem[] getRowItems() {
        return rowItems;
    }

    public void setRowItems(WSAnalysisItem[] rowItems) {
        this.rowItems = rowItems;
    }

    public WSAnalysisItem[] getColumnItems() {
        return columnItems;
    }

    public void setColumnItems(WSAnalysisItem[] columnItems) {
        this.columnItems = columnItems;
    }

    public WSAnalysisItem[] getMeasureItems() {
        return measureItems;
    }

    public void setMeasureItems(WSAnalysisItem[] measureItems) {
        this.measureItems = measureItems;
    }
}
