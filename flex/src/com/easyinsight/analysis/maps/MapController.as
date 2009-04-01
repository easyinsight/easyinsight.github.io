package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class MapController implements IReportController {
    public function MapController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = MapControlBar;
        factory.reportRenderer = "MapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "MapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}