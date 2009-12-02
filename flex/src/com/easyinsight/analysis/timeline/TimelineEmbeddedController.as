package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.service.EmbeddedTimelineDataService;

public class TimelineEmbeddedController implements IEmbeddedReportController {
    public function TimelineEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "TimelineModule.swf";
        factory.reportDataService = EmbeddedTimelineDataService;
        //factory.newDefinition = MapDefinition;
        return factory;
    }
}
}