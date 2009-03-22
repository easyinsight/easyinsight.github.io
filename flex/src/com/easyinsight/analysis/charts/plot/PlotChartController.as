package com.easyinsight.analysis.charts.plot {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class PlotChartController implements IReportController {
    public function PlotChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = PlotChartControlBar;
        factory.reportRenderer = "PlotChartModule.swf";
        factory.newDefinition = PlotChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "PlotChartModule.swf";
        factory.newDefinition = PlotChartDefinition;
        return factory;
    }
}
}