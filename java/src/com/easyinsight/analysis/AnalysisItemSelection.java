package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 11/11/13
 * Time: 11:52 AM
 */
public class AnalysisItemSelection {
    private AnalysisItem analysisItem;
    private boolean selected;
    private int customDateLevel;

    public AnalysisItemSelection() {
    }

    public AnalysisItemSelection(AnalysisItem analysisItem, boolean selected) {
        this.analysisItem = analysisItem;
        this.selected = selected;
    }

    public int getCustomDateLevel() {
        return customDateLevel;
    }

    public void setCustomDateLevel(int customDateLevel) {
        this.customDateLevel = customDateLevel;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
