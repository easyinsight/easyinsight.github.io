package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class AsiaMapEmbeddedController implements IEmbeddedReportController {
    public function AsiaMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "AsiaMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}