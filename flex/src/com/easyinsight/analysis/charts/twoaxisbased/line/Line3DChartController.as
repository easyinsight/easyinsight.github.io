package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;
public class Line3DChartController implements IReportController {
    public function Line3DChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = TwoAxisControlBar;
        factory.reportRenderer = "Line3DChartModule.swf";
        factory.newDefinition = Line3DChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "Line3DChartModule.swf";
        factory.newDefinition = Line3DChartDefinition;
        return factory;
    }
}
}