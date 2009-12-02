package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:15:44 AM
 */
public class SeriesDataResults extends DataResults {
    private List<ListDataResults> listDatas;
    private List<Value> seriesValues;

    public List<ListDataResults> getListDatas() {
        return listDatas;
    }

    public void setListDatas(List<ListDataResults> listDatas) {
        this.listDatas = listDatas;
    }

    public List<Value> getSeriesValues() {
        return seriesValues;
    }

    public void setSeriesValues(List<Value> seriesValues) {
        this.seriesValues = seriesValues;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedTimelineResults embeddedTimelineResults = new EmbeddedTimelineResults();
        embeddedTimelineResults.setListDatas(listDatas);
        embeddedTimelineResults.setSeriesValues(seriesValues);
        return embeddedTimelineResults;
    }
}
