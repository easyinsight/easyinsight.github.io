/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.CompareYearsDataService;

public class CompareYearsController implements IReportController {
    public function CompareYearsController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = CompareYearsDataService;
        factory.reportControlBar = CompareYearsControlBar;
        factory.reportRenderer = "CompareYears.swf";
        factory.newDefinition = CompareYearsDefinition;
        return factory;
    }
}
}
