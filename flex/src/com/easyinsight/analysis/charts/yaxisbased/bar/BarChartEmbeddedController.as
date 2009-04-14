package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class BarChartEmbeddedController implements IEmbeddedReportController {
    public function BarChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "BarChartModule.swf";
        factory.newDefinition = BarChartDefinition;
        return factory;
    }
}
}