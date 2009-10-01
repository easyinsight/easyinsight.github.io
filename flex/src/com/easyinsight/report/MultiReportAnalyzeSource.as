package com.easyinsight.report {
import com.easyinsight.framework.ModuleAnalyzeSource;

import flash.display.DisplayObject;

public class MultiReportAnalyzeSource extends ModuleAnalyzeSource{

    private var dataSourceID:int;
    private var dataSourceName:String;

    public function MultiReportAnalyzeSource(dataSourceID:int, dataSourceName:String = null) {
        super();
        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
    }

    override public function createDirect():DisplayObject {
        var view:MultiReportView = new MultiReportView();
        view.dataSourceID = dataSourceID;
        view.dataSourceName = dataSourceName;
        return view;
    }
}
}