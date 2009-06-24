package com.easyinsight.report {
import com.easyinsight.framework.ModuleAnalyzeSource;

import flash.display.DisplayObject;

public class MultiReportAnalyzeSource extends ModuleAnalyzeSource{

    private var dataSourceID:int;

    public function MultiReportAnalyzeSource(dataSourceID:int) {
        super();
        this.dataSourceID = dataSourceID;
    }

    override public function createDirect():DisplayObject {
        var view:MultiReportView = new MultiReportView();
        view.dataSourceID = dataSourceID;
        return view;
    }
}
}