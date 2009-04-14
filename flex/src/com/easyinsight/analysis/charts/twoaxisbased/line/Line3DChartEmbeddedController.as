package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class Line3DChartEmbeddedController implements IEmbeddedReportController {
    public function Line3DChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "Line3DChartModule.swf";
        factory.newDefinition = Line3DChartDefinition;
        return factory;
    }
}
}