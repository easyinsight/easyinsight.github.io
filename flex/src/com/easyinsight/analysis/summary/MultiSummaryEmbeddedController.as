/**
 * Created by jamesboe on 7/31/14.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.service.EmbeddedHTMLDataService;

public class MultiSummaryEmbeddedController implements IEmbeddedReportController {
    public function MultiSummaryEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = EmbeddedHTMLDataService;
        factory.reportRenderer = "TopoModule.swf";
        return factory;
    }
}
}
