package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class Area3DChartEmbeddedController implements IEmbeddedReportController {
    public function Area3DChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "Area3DChartModule.swf";
        //factory.newDefinition = Area3DChartDefinition;
        return factory;
    }
}
}