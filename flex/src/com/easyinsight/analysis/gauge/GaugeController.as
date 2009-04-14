package com.easyinsight.analysis.gauge {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class GaugeController implements IReportController {

    public function GaugeController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = GaugeControlBar;
        factory.reportRenderer = "GaugeModule.swf";
        factory.newDefinition = GaugeDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportRenderer = "GaugeModule.swf";
        factory.newDefinition = GaugeDefinition;
        return factory;
    }
}
}