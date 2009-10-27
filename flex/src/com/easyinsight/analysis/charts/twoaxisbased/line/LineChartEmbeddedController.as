package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class LineChartEmbeddedController implements IEmbeddedReportController {
    public function LineChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "LineChartModule.swf";
        //factory.newDefinition = LineChartDefinition;
        return factory;
    }
}
}