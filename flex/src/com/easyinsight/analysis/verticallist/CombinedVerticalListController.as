/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/14/11
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;

public class CombinedVerticalListController implements IReportController {
    public function CombinedVerticalListController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = VerticalListService;
        factory.reportControlBar = CombinedVerticalListControlBar;
        factory.reportRenderer = "CombinedVerticalList.swf";
        factory.newDefinition = CombinedVerticalListDefinition;
        return factory;
    }
}
}
