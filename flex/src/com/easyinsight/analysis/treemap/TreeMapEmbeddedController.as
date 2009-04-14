package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class TreeMapEmbeddedController implements IEmbeddedReportController {

    public function TreeMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "TreeMapModule.swf";
        factory.newDefinition = TreeMapDefinition;
        return factory;
    }
}
}