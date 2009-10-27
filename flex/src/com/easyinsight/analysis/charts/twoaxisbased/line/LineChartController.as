package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;
public class LineChartController implements IReportController {
    public function LineChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = TwoAxisControlBar;
        factory.reportRenderer = "LineChartModule.swf";
        factory.newDefinition = LineChartDefinition;
        return factory;
    }
}
}