/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/2/11
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.ytd {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.service.EmbeddedYTDDataService;

public class YTDEmbeddedController implements IEmbeddedReportController {
    public function YTDEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = EmbeddedYTDDataService;
        factory.reportRenderer = "YTD.swf";
        return factory;
    }
}
}
