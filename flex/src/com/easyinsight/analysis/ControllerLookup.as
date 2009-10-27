package com.easyinsight.analysis {
import com.easyinsight.analysis.charts.bubble.BubbleChartController;
import com.easyinsight.analysis.charts.plot.PlotChartController;
import com.easyinsight.analysis.charts.twoaxisbased.area.Area3DChartController;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartController;
import com.easyinsight.analysis.charts.twoaxisbased.line.Line3DChartController;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartController;
import com.easyinsight.analysis.charts.twoaxisbased.line.MultiMeasureLineChartController;
import com.easyinsight.analysis.charts.xaxisbased.column.Column3DChartController;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartController;
import com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartController;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartController;
import com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartController;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartController;
import com.easyinsight.analysis.crosstab.CrosstabController;
import com.easyinsight.analysis.gauge.GaugeController;
import com.easyinsight.analysis.list.ListController;
import com.easyinsight.analysis.maps.AmericasMapController;
import com.easyinsight.analysis.maps.AsiaMapController;
import com.easyinsight.analysis.maps.EuropeMapController;
import com.easyinsight.analysis.maps.MiddleEastMapController;
import com.easyinsight.analysis.maps.USMapController;
import com.easyinsight.analysis.maps.WorldMapController;
import com.easyinsight.analysis.tree.TreeController;
import com.easyinsight.analysis.treemap.TreeMapController;

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
            case AnalysisDefinition.MAP_USA:
                controller = USMapController;
                break;
            case AnalysisDefinition.MAP_EUROPE:
                controller = EuropeMapController;
                break;
            case AnalysisDefinition.MAP_WORLD:
                controller = WorldMapController;
                break;
            case AnalysisDefinition.MAP_ASIA:
                controller = AsiaMapController;
                break;
            case AnalysisDefinition.MAP_AMERICAS:
                controller = AmericasMapController;
                break;
            case AnalysisDefinition.MAP_MIDDLE_EAST:
                controller = MiddleEastMapController;
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
            case AnalysisDefinition.MMLINE:
                controller = MultiMeasureLineChartController;
                break;
        }
        return controller;
    }
}
}