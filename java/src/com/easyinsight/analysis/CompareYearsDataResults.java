package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class CompareYearsDataResults extends DataResults implements Serializable {
    private List<CompareYearsRow> dataSet;

    public List<CompareYearsRow> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<CompareYearsRow> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedCompareYearsDataResults embeddedCrosstabDataResults = new EmbeddedCompareYearsDataResults();
        embeddedCrosstabDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedCrosstabDataResults;
    }
}
