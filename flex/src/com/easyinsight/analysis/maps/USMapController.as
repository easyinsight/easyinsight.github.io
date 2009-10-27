package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
public class USMapController implements IReportController {
    public function USMapController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = MapControlBar;
        factory.explicitType = AnalysisDefinition.MAP_USA;
        factory.reportRenderer = "UnitedStatesMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}