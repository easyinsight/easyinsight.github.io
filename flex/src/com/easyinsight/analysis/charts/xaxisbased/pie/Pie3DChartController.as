package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.xaxisbased.XAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;
public class Pie3DChartController implements IReportController {
    public function Pie3DChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = XAxisControlBar;
        factory.reportRenderer = "Pie3DChartModule.swf";
        factory.newDefinition = Pie3DChartDefinition;
        return factory;
    }
}
}