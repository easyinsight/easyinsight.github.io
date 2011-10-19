package com.easyinsight.analysis {
import com.easyinsight.analysis.charts.bubble.BubbleChartController;
import com.easyinsight.analysis.charts.plot.PlotChartController;
import com.easyinsight.analysis.charts.twoaxisbased.area.Area3DChartController;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartController;
import com.easyinsight.analysis.charts.twoaxisbased.line.Line3DChartController;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartController;
import com.easyinsight.analysis.charts.xaxisbased.column.Column3DChartController;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartController;
import com.easyinsight.analysis.charts.xaxisbased.column.StackedColumnChartController;
import com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartController;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartController;
import com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartController;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartController;
import com.easyinsight.analysis.charts.yaxisbased.bar.StackedBarChartController;
import com.easyinsight.analysis.crosstab.CrosstabController;
import com.easyinsight.analysis.diagram.DiagramController;
import com.easyinsight.analysis.form.FormController;
import com.easyinsight.analysis.gantt.GanttController;
import com.easyinsight.analysis.gauge.GaugeController;
import com.easyinsight.analysis.heatmap.HeatMapController;
import com.easyinsight.analysis.list.ListController;
import com.easyinsight.analysis.tree.TreeController;
import com.easyinsight.analysis.treemap.TreeMapController;
import com.easyinsight.analysis.trend.TrendController;
import com.easyinsight.analysis.trend.TrendGridController;
import com.easyinsight.analysis.verticallist.CombinedVerticalListController;
import com.easyinsight.analysis.verticallist.VerticalListController;

public class ControllerLookup {

    public function ControllerLookup() {
    }

    public static function controllerForType(type:int):Class {
        var controller:Class;
        switch(type) {
            case AnalysisDefinition.LIST:
                controller = ListController;
                break;
            case AnalysisDefinition.CROSSTAB:
                controller = CrosstabController;
                break;
            case AnalysisDefinition.STACKED_COLUMN:
                controller = StackedColumnChartController;
                break;
            case AnalysisDefinition.STACKED_BAR:
                controller = StackedBarChartController;
                break;
            case AnalysisDefinition.COLUMN:
                controller = ColumnChartController;
                break;
            case AnalysisDefinition.COLUMN3D:
                controller = Column3DChartController;
                break;
            case AnalysisDefinition.BAR:
                controller = BarChartController;
                break;
            case AnalysisDefinition.BAR3D:
                controller = Bar3DChartController;
                break;
            case AnalysisDefinition.PIE:
                controller = PieChartController;
                break;
            case AnalysisDefinition.PIE3D:
                controller = Pie3DChartController;
                break;
            case AnalysisDefinition.LINE:
                controller = LineChartController;
                break;
            case AnalysisDefinition.LINE3D:
                controller = Line3DChartController;
                break;
            case AnalysisDefinition.AREA:
                controller = AreaChartController;
                break;
            case AnalysisDefinition.AREA3D:
                controller = Area3DChartController;
                break;
            case AnalysisDefinition.PLOT:
                controller = PlotChartController;
                break;
            case AnalysisDefinition.BUBBLE:
                controller = BubbleChartController;
                break;
            case AnalysisDefinition.GAUGE:
                controller = GaugeController;
                break;
            case AnalysisDefinition.TREEMAP:
                controller = TreeMapController;
                break;
            case AnalysisDefinition.TREE:
                controller = TreeController;
                break;
            case AnalysisDefinition.HEATMAP:
                controller = HeatMapController;
                break;
            case AnalysisDefinition.GANTT:
                controller = GanttController;
                break;
            case AnalysisDefinition.FORM:
                controller = FormController;
                break;
            case AnalysisDefinition.VERTICAL_LIST:
                controller = VerticalListController;
                break;
            case AnalysisDefinition.COMBINED_VERTICAL_LIST:
                controller = CombinedVerticalListController;
                break;
            case AnalysisDefinition.TREND:
                controller = TrendController;
                break;
            case AnalysisDefinition.TREND_GRID:
                controller = TrendGridController;
                break;
            case AnalysisDefinition.DIAGRAM:
                controller = DiagramController;
                break;
        }
        return controller;
    }
}
}