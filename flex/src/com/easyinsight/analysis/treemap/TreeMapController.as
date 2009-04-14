package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class TreeMapController implements IReportController {

    public function TreeMapController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = TreeMapControlBar;
        factory.reportRenderer = "TreeMapModule.swf";
        factory.newDefinition = TreeMapDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "TreeMapModule.swf";
        factory.newDefinition = TreeMapDefinition;
        return factory;
    }
}
}