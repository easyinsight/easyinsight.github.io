package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedCrosstabDataResults extends EmbeddedResults {
    private List<CrosstabMapWrapper> dataSet = new ArrayList<CrosstabMapWrapper>();
    private int columnCount;

    public List<CrosstabMapWrapper> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<CrosstabMapWrapper> dataSet) {
        this.dataSet = dataSet;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
