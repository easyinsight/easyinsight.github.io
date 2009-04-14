package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class Pie3DChartEmbeddedController implements IEmbeddedReportController {
    public function Pie3DChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "Pie3DChartModule.swf";
        factory.newDefinition = Pie3DChartDefinition;
        return factory;
    }
}
}