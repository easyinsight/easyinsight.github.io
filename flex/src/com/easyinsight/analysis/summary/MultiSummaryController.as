/**
 * Created by jamesboe on 7/30/14.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.HTMLDataService;

public class MultiSummaryController implements IReportController {
    public function MultiSummaryController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = HTMLDataService;
        factory.reportControlBar = MultiSummaryControlBar;
        factory.reportRenderer = "TopoModule.swf";
        factory.newDefinition = MultiSummaryDefinition;
        return factory;
    }
}
}
