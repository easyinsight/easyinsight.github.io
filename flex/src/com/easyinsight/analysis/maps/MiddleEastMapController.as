package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class MiddleEastMapController implements IReportController {
    public function MiddleEastMapController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = MapControlBar;
        factory.explicitType = AnalysisDefinition.MAP_MIDDLE_EAST;
        factory.reportRenderer = "MiddleEastMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "MiddleEastMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}