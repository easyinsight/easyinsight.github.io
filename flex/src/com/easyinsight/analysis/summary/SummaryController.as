/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.TreeDataService;

public class SummaryController implements IReportController {
    public function SummaryController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = TreeDataService;
        factory.reportControlBar = SummaryControlBar;
        factory.reportRenderer = "SummaryModule.swf";
        factory.newDefinition = SummaryDefinition;
        return factory;
    }
}
}
