package com.easyinsight.analysis.list {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class ListEmbeddedController implements IEmbeddedReportController {
    public function ListEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "ListModule.swf";
        //factory.newDefinition = ListDefinition;
        return factory;
    }
}
}