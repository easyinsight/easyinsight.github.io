package com.easyinsight.analysis.charts.bubble {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class BubbleChartController implements IReportController {
    public function BubbleChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = BubbleChartControlBar;
        factory.reportRenderer = "BubbleChartModule.swf";
        factory.newDefinition = BubbleChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "BubbleChartModule.swf";
        factory.newDefinition = BubbleChartDefinition;
        return factory;
    }
}
}