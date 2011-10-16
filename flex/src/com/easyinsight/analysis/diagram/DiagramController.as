/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.diagram {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.TrendDataService;

public class DiagramController implements IReportController {
    public function DiagramController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = TrendDataService;
        factory.reportControlBar = DiagramControlBar;
        factory.reportRenderer = "DiagramModule.swf";
        factory.newDefinition = DiagramDefinition;
        return factory;
    }
}
}
