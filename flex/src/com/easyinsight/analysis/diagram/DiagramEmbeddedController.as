/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.diagram {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.verticallist.EmbeddedTrendService;

public class DiagramEmbeddedController implements IEmbeddedReportController {

    public function DiagramEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = EmbeddedTrendService;
        factory.reportRenderer = "DiagramModule.swf";
        return factory;
    }
}
}
