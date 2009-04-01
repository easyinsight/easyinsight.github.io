package com.easyinsight.analysis.list {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class ListController implements IReportController {
    public function ListController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = ListControlBar;
        factory.reportRenderer = "ListModule.swf";
        factory.newDefinition = ListDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "ListModule.swf";
        factory.newDefinition = ListDefinition;
        return factory;
    }
}
}