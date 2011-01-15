/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/14/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;

public class StackedBarChartController implements IReportController {
    public function StackedBarChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = StackedBarChartControlBar;
        factory.reportRenderer = "StackedBarChartModule.swf";
        factory.newDefinition = StackedBarChartDefinition;
        return factory;
    }
}
}
