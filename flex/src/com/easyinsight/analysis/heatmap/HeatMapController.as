package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.service.ListDataService;

public class HeatMapController {
    public function HeatMapController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = HeatMapControlBar;
        factory.reportRenderer = "HeatMapModule.swf";
        factory.newDefinition = HeatMapDefinition;
        return factory;
    }
}
}