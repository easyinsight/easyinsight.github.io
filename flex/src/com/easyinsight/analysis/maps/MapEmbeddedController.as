package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class MapEmbeddedController implements IEmbeddedReportController {
    public function MapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "MapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}