package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;

import java.util.Set;

/**
 * User: jamesboe
 * Date: 4/28/11
 * Time: 12:21 PM
 */
public class InvalidFieldsException extends RuntimeException {
    private Set<AnalysisItem> analysisItems;

    public InvalidFieldsException(Set<AnalysisItem> analysisItems) {
        super("Unable to find fields " + analysisItems.toString());
        this.analysisItems = analysisItems;
    }

    public Set<AnalysisItem> getAnalysisItems() {
        return analysisItems;
    }
}
