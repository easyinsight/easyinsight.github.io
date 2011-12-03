/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.YTDDataService;

public class YTDController implements IReportController {
    public function YTDController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = YTDDataService;
        factory.reportControlBar = YTDControlBar;
        factory.reportRenderer = "YTD.swf";
        factory.newDefinition = YTDDefinition;
        return factory;
    }
}
}
