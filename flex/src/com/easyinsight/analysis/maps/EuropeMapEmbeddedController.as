package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class EuropeMapEmbeddedController implements IEmbeddedReportController {
    public function EuropeMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "EuropeMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}