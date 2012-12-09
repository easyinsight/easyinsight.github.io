package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
import com.easyinsight.analysis.service.TreeDataService;

public class TreeController implements IReportController {
    public function TreeController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = TreeDataService;
        factory.reportControlBar = TreeControlBar;
        factory.reportRenderer = "TreeModule.swf";
        factory.newDefinition = TreeDefinition;
        return factory;
    }
}
}