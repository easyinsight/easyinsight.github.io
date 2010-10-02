package com.easyinsight.analysis.gantt {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class GanttEmbeddedController implements IEmbeddedReportController {
    public function GanttEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "GanttModule.swf";        
        return factory;
    }
}
}