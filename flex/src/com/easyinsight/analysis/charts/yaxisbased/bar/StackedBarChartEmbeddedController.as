package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class StackedBarChartEmbeddedController implements IEmbeddedReportController {
    public function StackedBarChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "StackedBarChartModule.swf";
        //factory.newDefinition = BarChartDefinition;
        return factory;
    }
}
}