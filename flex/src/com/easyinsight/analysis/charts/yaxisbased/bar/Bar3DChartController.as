package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.yaxisbased.YAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;

public class Bar3DChartController implements IReportController {
    public function Bar3DChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = YAxisControlBar;
        factory.reportRenderer = "Bar3DChartModule.swf";
        factory.newDefinition = Bar3DChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "Bar3DChartModule.swf";
        factory.newDefinition = Bar3DChartDefinition;
        return factory;
    }
}
}