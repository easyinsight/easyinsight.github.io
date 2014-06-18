/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/20/13
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.text {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.heatmap.TopoMapControlBar;
import com.easyinsight.analysis.heatmap.TopoMapDefinition;
import com.easyinsight.analysis.service.HTMLDataService;
import com.easyinsight.analysis.service.ListDataService;

public class TopoMapController implements IReportController {
    public function TopoMapController() {
    }


    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = HTMLDataService;
        factory.reportControlBar = TopoMapControlBar;
        factory.reportRenderer = "TopoModule.swf";
        factory.newDefinition = TopoMapDefinition;
        return factory;
    }
}
}
