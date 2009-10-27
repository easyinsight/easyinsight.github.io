package com.easyinsight.analysis.charts.bubble {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class BubbleChartEmbeddedController implements IEmbeddedReportController {
    public function BubbleChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "BubbleChartModule.swf";
        //factory.newDefinition = BubbleChartDefinition;
        return factory;
    }
}
}