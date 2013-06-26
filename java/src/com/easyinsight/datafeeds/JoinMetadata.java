package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;

/**
* User: jamesboe
* Date: 6/10/13
* Time: 12:01 PM
*/
public class JoinMetadata {
    public AnalysisItem analysisItem;
    public int dateLevel;

    JoinMetadata(AnalysisItem analysisItem, int dateLevel) {
        this.analysisItem = analysisItem;
        this.dateLevel = dateLevel;
    }
}
