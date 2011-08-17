/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {

import com.easyinsight.analysis.charts.bubble.BubbleChartModule;
import com.easyinsight.analysis.charts.plot.PlotChartModule;
import com.easyinsight.analysis.charts.twoaxisbased.area.Area3DChartModule;
import com.easyinsight.analysis.charts.twoaxisbased.area.AreaChartModule;
import com.easyinsight.analysis.charts.twoaxisbased.line.Line3DChartModule;
import com.easyinsight.analysis.charts.twoaxisbased.line.LineChartModule;
import com.easyinsight.analysis.charts.xaxisbased.column.Column3DChartModule;
import com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartModule;
import com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartModule;
import com.easyinsight.analysis.charts.xaxisbased.pie.PieChartModule;
import com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartModule;
import com.easyinsight.analysis.charts.yaxisbased.bar.BarChartModule;
import com.easyinsight.analysis.crosstab.CrosstabModule;
import com.easyinsight.analysis.gantt.GanttModule;
import com.easyinsight.analysis.gauge.GaugeModule;
import com.easyinsight.analysis.heatmap.HeatMapModule;
import com.easyinsight.analysis.list.ListModule;
import com.easyinsight.analysis.tree.TreeModule;
import com.easyinsight.analysis.verticallist.CombinedVerticalListModule;
import com.easyinsight.analysis.verticallist.VerticalListModule;

import flash.events.Event;

import flash.events.EventDispatcher;

import mx.core.Container;

import mx.modules.Module;

public class ReportModuleLoader extends EventDispatcher {

    private var moduleName:String;

    public function ReportModuleLoader() {
    }

    public function loadReportRenderer(_reportRendererModule:String, container:Container):void {
        moduleName = _reportRendererModule;
        dispatchEvent(new Event("moduleLoaded"));
    }

    public function create():Object {
        var module:Module;
        switch (moduleName) {
            case "ListModule.swf":
                module = new ListModule();
                break;
            case "TreeModule.swf":
                module = new TreeModule();
                break;
            case "VerticalListModule.swf":
                module = new VerticalListModule();
                break;
            case "CombinedVerticalListModule.swf":
                module = new CombinedVerticalListModule();
                break;
            case "HeatMapModule.swf":
                module = new HeatMapModule();
                break;
            case "GaugeModule.swf":
                module = new GaugeModule();
                break;
            case "GanttModule.swf":
                module = new GanttModule();
                break;
            case "CrosstabModule.swf":
                module = new CrosstabModule();
                break;
            case "ColumnChartModule.swf":
                module = new ColumnChartModule();
                break;
            case "Column3DChartModule.swf":
                module = new Column3DChartModule();
                break;
            case "BarChartModule.swf":
                module = new BarChartModule();
                break;
            case "Bar3DChartModule.swf":
                module = new Bar3DChartModule();
                break;
            case "AreaChartModule.swf":
                module = new AreaChartModule();
                break;
            case "Area3DChartModule.swf":
                module = new Area3DChartModule();
                break;
            case "LineChartModule.swf":
                module = new LineChartModule();
                break;
            case "Line3DChartModule.swf":
                module = new Line3DChartModule();
                break;
            case "PieModule.swf":
                module = new PieChartModule();
                break;
            case "Pie3DChartModule.swf":
                module = new Pie3DChartModule();
                break;
            case "PlotChartModule.swf":
                module = new PlotChartModule();
                break;
            case "BubbleChartModule.swf":
                module = new BubbleChartModule();
                break;
        }
        return module;
    }
}
}
