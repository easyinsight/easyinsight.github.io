package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.charts.xaxisbased.XAxisControlBar;
import com.easyinsight.analysis.service.ListDataService;
import mx.charts.renderers.WedgeItemRenderer;


public class PieChartController implements IReportController {

    private var wedgeItemRenderer:WedgeItemRenderer;

    public function PieChartController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = XAxisControlBar;
        factory.reportRenderer = "PieChartModule.swf";
        factory.newDefinition = PieChartDefinition;
        return factory;
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "PieChartModule.swf";
        factory.newDefinition = PieChartDefinition;
        return factory;
    }
}
}