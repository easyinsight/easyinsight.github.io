package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedCompareYearsDataResults extends EmbeddedResults {
    private List<CompareYearsRow> dataSet;

    public List<CompareYearsRow> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<CompareYearsRow> dataSet) {
        this.dataSet = dataSet;
    }
}
