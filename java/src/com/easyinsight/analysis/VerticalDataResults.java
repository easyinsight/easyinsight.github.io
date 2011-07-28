package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class VerticalDataResults extends DataResults implements Serializable {
    private List<DataResults> map;


    public List<DataResults> getMap() {
        return map;
    }

    public void setMap(List<DataResults> map) {
        this.map = map;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedDataResults embeddedDataResults = new EmbeddedDataResults();
        /*embeddedDataResults.setHeaders(headers);
        if (headers.length == 0) {
            embeddedDataResults.setRows(new ListRow[0]);
        } else {
            embeddedDataResults.setRows(rows);
        }*/
        
        embeddedDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedDataResults;
    }
}
