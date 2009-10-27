package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class AreaChartEmbeddedController implements IEmbeddedReportController {
    public function AreaChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "AreaChartModule.swf";
        //factory.newDefinition = AreaChartDefinition;
        return factory;
    }
}
}