package com.easyinsight.analysis.crosstab {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.service.CrosstabDataService;
public class CrosstabEmbeddedController implements IEmbeddedReportController {
    public function CrosstabEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = CrosstabDataService;
        factory.reportRenderer = "CrosstabModule.swf";
        factory.newDefinition = CrosstabDefinition;
        return factory;
    }
}
}