package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.verticallist.EmbeddedTreeService;

public class TreeEmbeddedController implements IEmbeddedReportController {
    public function TreeEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = EmbeddedTreeService;
        factory.reportRenderer = "TreeModule.swf";
        //factory.newDefinition = TreeDefinition;
        return factory;
    }
}
}