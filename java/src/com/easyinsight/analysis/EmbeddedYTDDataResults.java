package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedYTDDataResults extends EmbeddedResults {
    private List<YTDValue> dataSet;

    public List<YTDValue> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<YTDValue> dataSet) {
        this.dataSet = dataSet;
    }
}
