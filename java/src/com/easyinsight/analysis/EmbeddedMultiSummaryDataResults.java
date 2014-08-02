package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedMultiSummaryDataResults extends EmbeddedResults {
    private List<MultiSummaryRow> treeRows;

    public List<MultiSummaryRow> getTreeRows() {
        return treeRows;
    }

    public void setTreeRows(List<MultiSummaryRow> treeRows) {
        this.treeRows = treeRows;
    }
}
