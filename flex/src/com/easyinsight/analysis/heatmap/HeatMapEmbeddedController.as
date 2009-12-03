package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class HeatMapEmbeddedController implements IEmbeddedReportController {
    public function HeatMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "HeatMapModule.swf";        
        return factory;
    }
}
}