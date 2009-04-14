package com.easyinsight.analysis.charts.plot {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class PlotChartEmbeddedController implements IEmbeddedReportController {
    public function PlotChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "PlotChartModule.swf";
        factory.newDefinition = PlotChartDefinition;
        return factory;
    }
}
}