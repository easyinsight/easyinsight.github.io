package com.easyinsight.analysis.gauge {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class GaugeEmbeddedController implements IEmbeddedReportController {

    public function GaugeEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "GaugeModule.swf";
        //factory.newDefinition = GaugeDefinition;
        return factory;
    }
}
}