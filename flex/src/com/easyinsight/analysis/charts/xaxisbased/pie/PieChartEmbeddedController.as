package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class PieChartEmbeddedController implements IEmbeddedReportController {
    public function PieChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "PieChartModule.swf";
        //factory.newDefinition = PieChartDefinition;
        return factory;
    }
}
}