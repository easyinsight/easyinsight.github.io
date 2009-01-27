package com.easyinsight.webservice;

/**
 * User: James Boe
 * Date: Aug 13, 2008
 * Time: 11:31:04 AM
 */
public class ShortListDefinition {
    private WSAnalysisItem[] analysisItems;
    private boolean showLineNumbers;

    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    public void setShowLineNumbers(boolean showLineNumbers) {
        this.showLineNumbers = showLineNumbers;
    }

    public WSAnalysisItem[] getAnalysisItems() {
        return analysisItems;
    }

    public void setAnalysisItems(WSAnalysisItem[] analysisItems) {
        this.analysisItems = analysisItems;
    }
}
