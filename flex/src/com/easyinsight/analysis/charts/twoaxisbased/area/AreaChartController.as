package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;
public class AreaChartController implements IReportController {
    public function AreaChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = TwoAxisControlBar;
        factory.reportRenderer = "AreaChartModule.swf";
        factory.newDefinition = AreaChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "AreaChartModule.swf";
        factory.newDefinition = AreaChartDefinition;
        return factory;
    }
}
}