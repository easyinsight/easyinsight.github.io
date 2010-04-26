package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class AfricaMapEmbeddedController implements IEmbeddedReportController {
    public function AfricaMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "AfricaMapModule.swf";
        //factory.newDefinition = MapDefinition;
        return factory;
    }
}
}