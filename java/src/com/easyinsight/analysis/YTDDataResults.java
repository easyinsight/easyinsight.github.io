package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class YTDDataResults extends DataResults implements Serializable {
    private List<YTDValue> dataSet;

    public List<YTDValue> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<YTDValue> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedYTDDataResults embeddedCrosstabDataResults = new EmbeddedYTDDataResults();
        embeddedCrosstabDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedCrosstabDataResults;
    }
}
