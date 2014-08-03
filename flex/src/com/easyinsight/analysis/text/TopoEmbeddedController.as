/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/20/13
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.text {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.service.EmbeddedHTMLDataService;

public class TopoEmbeddedController implements IEmbeddedReportController {
    public function TopoEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = EmbeddedHTMLDataService;
        factory.reportRenderer = "TopoModule.swf";
        return factory;
    }
}
}
