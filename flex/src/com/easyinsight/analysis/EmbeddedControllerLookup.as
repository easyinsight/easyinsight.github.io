package com.easyinsight.analysis {
import com.easyinsight.analysis.charts.bubble.BubbleChartEmbeddedController;
import com.easyinsight.analysis.charts.plot.PlotChartEmbeddedController;
import com.easyinsight.analysis.charts.twoaxisbased.area.Area3DChartEmbeddedController;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartEmbeddedController;
import com.easyinsight.analysis.charts.twoaxisbased.line.Line3DChartEmbeddedController;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.column.Column3DChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.column.StackedColumnChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartEmbeddedController;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartEmbeddedController;
import com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartEmbeddedController;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartEmbeddedController;
import com.easyinsight.analysis.charts.yaxisbased.bar.StackedBarChartEmbeddedController;
import com.easyinsight.analysis.crosstab.CrosstabEmbeddedController;
import com.easyinsight.analysis.form.FormEmbeddedController;
import com.easyinsight.analysis.gantt.GanttEmbeddedController;
import com.easyinsight.analysis.gauge.GaugeEmbeddedController;
import com.easyinsight.analysis.heatmap.HeatMapEmbeddedController;
import com.easyinsight.analysis.list.ListEmbeddedController;
import com.easyinsight.analysis.maps.AfricaMapEmbeddedController;
import com.easyinsight.analysis.maps.AmericasMapEmbeddedController;
import com.easyinsight.analysis.maps.AsiaMapEmbeddedController;
import com.easyinsight.analysis.maps.EuropeMapEmbeddedController;
import com.easyinsight.analysis.maps.MiddleEastMapEmbeddedController;
import com.easyinsight.analysis.maps.USMapEmbeddedController;
import com.easyinsight.analysis.maps.WorldMapEmbeddedController;
import com.easyinsight.analysis.timeline.TimelineEmbeddedController;
import com.easyinsight.analysis.tree.TreeEmbeddedController;
import com.easyinsight.analysis.treemap.TreeMapEmbeddedController;

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
            case AnalysisDefinition.MAP_USA:
                controller = USMapEmbeddedController;
                break;
            case AnalysisDefinition.MAP_EUROPE:
                controller = EuropeMapEmbeddedController;
                break;
            case AnalysisDefinition.MAP_WORLD:
                controller = WorldMapEmbeddedController;
                break;
            case AnalysisDefinition.MAP_ASIA:
                controller = AsiaMapEmbeddedController;
                break;
            case AnalysisDefinition.MAP_AMERICAS:
                controller = AmericasMapEmbeddedController;
                break;
            case AnalysisDefinition.MAP_MIDDLE_EAST:
                controller = MiddleEastMapEmbeddedController;
                break;
            case AnalysisDefinition.MAP_AFRICA:
                controller = AfricaMapEmbeddedController;
                break;
            case AnalysisDefinition.COLUMN:
                controller = ColumnChartEmbeddedController;
                break;
            case AnalysisDefinition.STACKED_COLUMN:
                controller = StackedColumnChartEmbeddedController;
                break;
            case AnalysisDefinition.COLUMN3D:
                controller = Column3DChartEmbeddedController;
                break;
            case AnalysisDefinition.BAR:
                controller = BarChartEmbeddedController;
                break;
            case AnalysisDefinition.STACKED_BAR:
                controller = StackedBarChartEmbeddedController;
                break;
            case AnalysisDefinition.BAR3D:
                controller = Bar3DChartEmbeddedController;
                break;
            case AnalysisDefinition.PIE:
                controller = PieChartEmbeddedController;
                break;
            case AnalysisDefinition.PIE3D:
                controller = Pie3DChartEmbeddedController;
                break;
            case AnalysisDefinition.LINE:
                controller = LineChartEmbeddedController;
                break;
            case AnalysisDefinition.LINE3D:
                controller = Line3DChartEmbeddedController;
                break;
            case AnalysisDefinition.AREA:
                controller = AreaChartEmbeddedController;
                break;
            case AnalysisDefinition.AREA3D:
                controller = Area3DChartEmbeddedController;
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
            case AnalysisDefinition.TIMELINE:
                controller = TimelineEmbeddedController;
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
        }
        return controller;
    }
}
}