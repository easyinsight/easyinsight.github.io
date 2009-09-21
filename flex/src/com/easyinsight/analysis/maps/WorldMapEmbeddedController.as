package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class WorldMapEmbeddedController implements IEmbeddedReportController {
    public function WorldMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "WorldMapModule.swf";
        factory.newDefinition = MapDefinition;
        return factory;
    }
}
}