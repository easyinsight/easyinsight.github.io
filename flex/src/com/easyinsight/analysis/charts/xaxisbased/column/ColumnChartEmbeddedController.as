package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class ColumnChartEmbeddedController implements IEmbeddedReportController {

    // private var blah:ColumnChart3D;
    
    public function ColumnChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "ColumnChartModule.swf";
        factory.newDefinition = ColumnChartDefinition;
        return factory;
    }
}
}