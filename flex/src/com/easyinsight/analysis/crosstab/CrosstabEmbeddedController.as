package com.easyinsight.analysis.crosstab {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.verticallist.EmbeddedCrosstabService;

public class CrosstabEmbeddedController implements IEmbeddedReportController {
    public function CrosstabEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = EmbeddedCrosstabService;
        factory.reportRenderer = "CrosstabModule.swf";
        //factory.newDefinition = CrosstabDefinition;
        return factory;
    }
}
}