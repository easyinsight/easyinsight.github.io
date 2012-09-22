package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class TreeDataResults extends DataResults implements Serializable {
    private List<TreeRow> treeRows;

    public List<TreeRow> getTreeRows() {
        return treeRows;
    }

    public void setTreeRows(List<TreeRow> treeRows) {
        this.treeRows = treeRows;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedTreeDataResults embeddedCrosstabDataResults = new EmbeddedTreeDataResults();
        embeddedCrosstabDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedCrosstabDataResults;
    }
}
