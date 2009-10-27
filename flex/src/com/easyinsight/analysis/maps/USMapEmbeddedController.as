package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
public class USMapEmbeddedController implements IEmbeddedReportController {
    public function USMapEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "UnitedStatesMapModule.swf";
        //factory.newDefinition = MapDefinition;
        return factory;
    }
}
}