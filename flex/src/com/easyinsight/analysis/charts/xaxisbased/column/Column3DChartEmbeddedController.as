package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class Column3DChartEmbeddedController implements IEmbeddedReportController {

    // private var blah:ColumnChart3D;
    
    public function Column3DChartEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "Column3DChartModule.swf";
        factory.newDefinition = Column3DChartDefinition;
        return factory;
    }
}
}