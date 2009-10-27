package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;
public class Area3DChartController implements IReportController {
    public function Area3DChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = TwoAxisControlBar;
        factory.reportRenderer = "Area3DChartModule.swf";
        factory.newDefinition = Area3DChartDefinition;
        return factory;
    }
}
}