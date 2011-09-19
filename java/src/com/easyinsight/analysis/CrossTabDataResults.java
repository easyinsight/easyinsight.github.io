package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.analysis.FeedMetadata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class CrossTabDataResults extends DataResults implements Serializable {
    private List<CrosstabMapWrapper> dataSet;
    private int columnCount;

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public List<CrosstabMapWrapper> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<CrosstabMapWrapper> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedCrosstabDataResults embeddedCrosstabDataResults = new EmbeddedCrosstabDataResults();
        embeddedCrosstabDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedCrosstabDataResults;
    }
}
