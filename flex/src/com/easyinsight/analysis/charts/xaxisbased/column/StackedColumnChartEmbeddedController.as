package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class StackedColumnChartEmbeddedController implements IEmbeddedReportController {

    // private var blah:ColumnChart3D;
    
    public function StackedColumnChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "StackedColumnChartModule.swf";
        //factory.newDefinition = ColumnChartDefinition;
        return factory;
    }
}
}