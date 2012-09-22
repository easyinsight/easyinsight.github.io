package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedTreeDataResults extends EmbeddedResults {
    private List<TreeRow> treeRows;

    public List<TreeRow> getTreeRows() {
        return treeRows;
    }

    public void setTreeRows(List<TreeRow> treeRows) {
        this.treeRows = treeRows;
    }
}
