package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;

public class TimelineController implements IReportController {
    public function TimelineController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = TimelineDataService;
        factory.reportControlBar = TimelineControlBar;
        factory.reportRenderer = "TimelineModule.swf";
        factory.newDefinition = Timeline;
        return factory;
    }
}
}