/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/14/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;

public class StackedColumnChartController implements IReportController {
    public function StackedColumnChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = StackedColumnChartControlBar;
        factory.reportRenderer = "StackedColumnChartModule.swf";
        factory.newDefinition = StackedColumnChartDefinition;
        return factory;
    }
}
}
