package com.easyinsight.analysis.crosstab {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.CrosstabDataService;
public class CrosstabController implements IReportController {
    public function CrosstabController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = CrosstabDataService;
        factory.reportControlBar = CrosstabControlBar;
        factory.reportRenderer = "CrosstabModule.swf";
        factory.newDefinition = CrosstabDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = CrosstabDataService;
        factory.reportRenderer = "CrosstabModule.swf";
        factory.newDefinition = CrosstabDefinition;
        return factory;
    }
}
}