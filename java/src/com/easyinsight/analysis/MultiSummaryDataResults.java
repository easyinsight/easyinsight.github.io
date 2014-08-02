package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class MultiSummaryDataResults extends DataResults implements Serializable {
    private List<MultiSummaryRow> treeRows;

    public List<MultiSummaryRow> getTreeRows() {
        return treeRows;
    }

    public void setTreeRows(List<MultiSummaryRow> treeRows) {
        this.treeRows = treeRows;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedMultiSummaryDataResults embeddedCrosstabDataResults = new EmbeddedMultiSummaryDataResults();
        embeddedCrosstabDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedCrosstabDataResults;
    }
}
