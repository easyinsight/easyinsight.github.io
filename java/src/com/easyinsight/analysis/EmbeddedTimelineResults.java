package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 1, 2009
 * Time: 9:41:12 AM
 */
public class EmbeddedTimelineResults extends EmbeddedResults {
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
}
