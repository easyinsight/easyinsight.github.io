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

public class TextEmbeddedController implements IEmbeddedReportController {
    public function TextEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "TextModule.swf";
        return factory;
    }
}
}
