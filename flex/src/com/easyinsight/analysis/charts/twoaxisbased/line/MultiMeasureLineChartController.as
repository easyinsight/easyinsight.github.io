package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.twoaxisbased.MultiMeasureControlBar;
import com.easyinsight.analysis.service.ListDataService;

public class MultiMeasureLineChartController implements IReportController {
    public function MultiMeasureLineChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = MultiMeasureControlBar;
        factory.reportRenderer = "MultiMeasureLineChartModule.swf";
        factory.newDefinition = MultiMeasureLineChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        return null;
    }
}
}