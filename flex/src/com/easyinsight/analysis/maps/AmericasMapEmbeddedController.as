package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class AmericasMapEmbeddedController implements IEmbeddedReportController {
    public function AmericasMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "AmericasMapModule.swf";
        //factory.newDefinition = MapDefinition;
        return factory;
    }
}
}