package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class TreeEmbeddedController implements IEmbeddedReportController {
    public function TreeEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "TreeModule.swf";
        factory.newDefinition = TreeDefinition;
        return factory;
    }
}
}