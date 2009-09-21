package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class MiddleEastMapEmbeddedController implements IEmbeddedReportController {
    public function MiddleEastMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "MiddleEastMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}