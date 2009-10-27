package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class Bar3DChartEmbeddedController implements IEmbeddedReportController {
    public function Bar3DChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "Bar3DChartModule.swf";
        //factory.newDefinition = Bar3DChartDefinition;
        return factory;
    }
}
}