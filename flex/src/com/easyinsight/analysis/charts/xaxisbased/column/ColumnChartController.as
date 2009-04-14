package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;
import com.easyinsight.analysis.charts.xaxisbased.XAxisControlBar;
import mx.charts.ColumnChart;

public class ColumnChartController implements IReportController {

    private var chart:ColumnChart;
    
    public function ColumnChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = XAxisControlBar;
        factory.reportRenderer = "ColumnChartModule.swf";
        factory.newDefinition = ColumnChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "ColumnChartModule.swf";
        factory.newDefinition = ColumnChartDefinition;
        return factory;
    }
}
}