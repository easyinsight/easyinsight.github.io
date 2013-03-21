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
import com.easyinsight.analysis.service.ListDataService;

public class TextController implements IReportController {
    public function TextController() {
    }


    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = TextControlBar;
        factory.reportRenderer = "TextModule.swf";
        factory.newDefinition = TextReport;
        return factory;
    }
}
}
