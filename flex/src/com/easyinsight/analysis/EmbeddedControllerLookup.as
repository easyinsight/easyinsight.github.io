package com.easyinsight.analysis {
import com.easyinsight.analysis.charts.bubble.BubbleChartEmbeddedController;
import com.easyinsight.analysis.charts.plot.PlotChartEmbeddedController;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartEmbeddedController;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.column.StackedColumnChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartEmbeddedController;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartEmbeddedController;
import com.easyinsight.analysis.charts.yaxisbased.bar.StackedBarChartEmbeddedController;
import com.easyinsight.analysis.crosstab.CrosstabEmbeddedController;
import com.easyinsight.analysis.diagram.DiagramEmbeddedController;
import com.easyinsight.analysis.form.FormEmbeddedController;
import com.easyinsight.analysis.gantt.GanttEmbeddedController;
import com.easyinsight.analysis.gauge.GaugeEmbeddedController;
import com.easyinsight.analysis.heatmap.HeatMapEmbeddedController;
import com.easyinsight.analysis.list.ListEmbeddedController;
import com.easyinsight.analysis.summary.SummaryEmbeddedController;
import com.easyinsight.analysis.tree.TreeEmbeddedController;
import com.easyinsight.analysis.treemap.TreeMapEmbeddedController;
import com.easyinsight.analysis.trend.TrendEmbeddedController;
import com.easyinsight.analysis.trend.TrendGridEmbeddedController;
import com.easyinsight.analysis.verticallist.CombinedVerticalListEmbeddedController;
import com.easyinsight.analysis.verticallist.VerticalListEmbeddedController;
import com.easyinsight.analysis.ytd.CompareYearsEmbeddedController;
import com.easyinsight.analysis.ytd.YTDEmbeddedController;

public class EmbeddedControllerLookup {

    public function EmbeddedControllerLookup() {
    }

    public static function controllerForType(type:int):Class {
        var controller:Class;
        switch(type) {
            case AnalysisDefinition.LIST:
                controller = ListEmbeddedController;
                break;
            case AnalysisDefinition.CROSSTAB:
                controller = CrosstabEmbeddedController;
                break;
            case AnalysisDefinition.COLUMN:
                controller = ColumnChartEmbeddedController;
                break;
            case AnalysisDefinition.STACKED_COLUMN:
                controller = StackedColumnChartEmbeddedController;
                break;
            case AnalysisDefinition.BAR:
                controller = BarChartEmbeddedController;
                break;
            case AnalysisDefinition.STACKED_BAR:
                controller = StackedBarChartEmbeddedController;
                break;
            case AnalysisDefinition.PIE:
                controller = PieChartEmbeddedController;
                break;
            case AnalysisDefinition.LINE:
                controller = LineChartEmbeddedController;
                break;
            case AnalysisDefinition.AREA:
                controller = AreaChartEmbeddedController;
                break;
            case AnalysisDefinition.PLOT:
                controller = PlotChartEmbeddedController;
                break;
            case AnalysisDefinition.BUBBLE:
                controller = BubbleChartEmbeddedController;
                break;
            case AnalysisDefinition.GAUGE:
                controller = GaugeEmbeddedController;
                break;
            case AnalysisDefinition.TREEMAP:
                controller = TreeMapEmbeddedController;
                break;
            case AnalysisDefinition.TREE:
                controller = TreeEmbeddedController;
                break;
            case AnalysisDefinition.HEATMAP:
                controller = HeatMapEmbeddedController;
                break;
            case AnalysisDefinition.GANTT:
                controller = GanttEmbeddedController;
                break;
            case AnalysisDefinition.FORM:
                controller = FormEmbeddedController;
                break;
            case AnalysisDefinition.VERTICAL_LIST:
                controller = VerticalListEmbeddedController;
                break;
            case AnalysisDefinition.COMBINED_VERTICAL_LIST:
                controller = CombinedVerticalListEmbeddedController;
                break;
            case AnalysisDefinition.TREND:
                controller = TrendEmbeddedController;
                break;
            case AnalysisDefinition.TREND_GRID:
                controller = TrendGridEmbeddedController;
                break;
            case AnalysisDefinition.DIAGRAM:
                controller = DiagramEmbeddedController;
                break;
            case AnalysisDefinition.YTD:
                controller = YTDEmbeddedController;
                break;
            case AnalysisDefinition.COMPARE_YEARS:
                controller = CompareYearsEmbeddedController;
                break;
            case AnalysisDefinition.SUMMARY:
                controller = SummaryEmbeddedController;
                break;
        }
        return controller;
    }
}
}