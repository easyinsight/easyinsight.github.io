package com.easyinsight.analysis.gantt {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;

public class GanttController implements IReportController {
    public function GanttController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = GanttChartControlBar;
        factory.reportRenderer = "GanttModule.swf";
        factory.newDefinition = GanttReport;
        return factory;
    }
}
}