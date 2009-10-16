package com.easyinsight.report {
import com.easyinsight.FullScreenPage;

import com.easyinsight.listing.AnalyzeSource;

public class MultiReportAnalyzeSource implements AnalyzeSource {

    private var dataSourceID:int;
    private var dataSourceName:String;

    public function MultiReportAnalyzeSource(dataSourceID:int, dataSourceName:String = null) {
        super();
        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
    }

    public function createAnalysisPopup():FullScreenPage {
        var view:MultiReportView = new MultiReportView();
        view.dataSourceID = dataSourceID;
        view.dataSourceName = dataSourceName;
        return view;
    }
}
}