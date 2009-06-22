package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class MultiMeasureLineChartEmbeddedController implements IEmbeddedReportController {
    public function MultiMeasureLineChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "MultiMeasureLineChartModule.swf";
        factory.newDefinition = MultiMeasureLineChartDefinition;
        return factory;
    }
}
}