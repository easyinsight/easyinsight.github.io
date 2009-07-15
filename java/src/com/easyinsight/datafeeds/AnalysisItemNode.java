package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;

/**
 * Created by IntelliJ IDEA.
 * User: jboe
 * Date: Jul 14, 2009
 * Time: 1:57:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisItemNode extends FeedNode {
    private AnalysisItem analysisItem;

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    @Override
    public String toDisplay() {
        return analysisItem.toDisplay();
    }
}
