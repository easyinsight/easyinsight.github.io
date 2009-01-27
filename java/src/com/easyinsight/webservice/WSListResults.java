package com.easyinsight.webservice;

import com.easyinsight.analysis.WSAnalysisDefinition;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 6:28:08 PM
 */
public class WSListResults {
    private WSListRow[] listRows;
    private WSAnalysisItem[] analysisItems;
    private ShortAnalysisDefinition analysisDefinition;

    public ShortAnalysisDefinition getAnalysisDefinition() {
        return analysisDefinition;
    }

    public void setAnalysisDefinition(ShortAnalysisDefinition analysisDefinition) {
        this.analysisDefinition = analysisDefinition;
    }

    public WSListRow[] getListRows() {
        return listRows;
    }

    public void setListRows(WSListRow[] listRows) {
        this.listRows = listRows;
    }

    public WSAnalysisItem[] getAnalysisItems() {
        return analysisItems;
    }

    public void setAnalysisItems(WSAnalysisItem[] analysisItems) {
        this.analysisItems = analysisItems;
    }
}
