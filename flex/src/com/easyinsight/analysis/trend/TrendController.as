/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/11
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.TrendDataService;

public class TrendController implements IReportController {
    public function TrendController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = TrendDataService;
        factory.reportControlBar = TrendControlBar;
        factory.reportRenderer = "TrendModule.swf";
        factory.newDefinition = TrendDefinition;
        return factory;
    }
}
}
