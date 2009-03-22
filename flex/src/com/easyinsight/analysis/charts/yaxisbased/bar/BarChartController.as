package com.easyinsight.analysis.charts.yaxisbased.bar {

import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.yaxisbased.YAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;

public class BarChartController implements IReportController {
    public function BarChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = YAxisControlBar;
        factory.reportRenderer = "BarChartModule.swf";
        factory.newDefinition = BarChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "BarChartModule.swf";
        factory.newDefinition = BarChartDefinition;
        return factory;
    }
}
}