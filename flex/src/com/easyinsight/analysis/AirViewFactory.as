package com.easyinsight.analysis {

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
import com.easyinsight.analysis.gauge.GaugeModule;
import com.easyinsight.analysis.list.ListModule;
import com.easyinsight.analysis.maps.MapModule;
import com.easyinsight.analysis.service.EmbeddedDataService;
import com.easyinsight.analysis.tree.TreeModule;
import com.easyinsight.analysis.treemap.TreeMapModule;
import com.easyinsight.framework.DataServiceLoadingEvent;
import flash.display.DisplayObject;
import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;

public class AirViewFactory extends VBox {

    private var _reportRendererModule:String;
    private var _newDefinition:Class;
    private var _reportDataService:Class = EmbeddedDataService;

    private var _reportType:int;

    private var _reportID:int;
    private var _availableFields:ArrayCollection;

    private var _prefix:String = "";

    private var moduleInfo:IModuleInfo;

    private var _reportRenderer:IReportRenderer;
    private var _dataService:IEmbeddedDataService = new EmbeddedDataService();

    private var pendingRequest:Boolean = false;

    public function AirViewFactory() {
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    public function set prefix(val:String):void {
        _prefix = val;
    }

    public function set availableFields(val:ArrayCollection):void {
        _availableFields = val;
    }

    public function set reportID(val:int):void {
        _reportID = val;
    }

    public function set reportDataService(val:Class):void {
        _reportDataService = val;
    }

    public function set reportRenderer(val:String):void {
        _reportRendererModule = val;
    }

    public function set newDefinition(val:Class):void {
        _newDefinition = val;
    }

    public function set reportType(value:int):void {
        _reportType = value;
    }

    override protected function createChildren():void {
        super.createChildren();

        _dataService = new _reportDataService();
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        _dataService.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, gotData);

        loadReportRenderer();
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
        dispatchEvent(event);
    }

    private function onDataRequest(event:ReportDataEvent):void {
        retrieveData();
    }

    public function retrieveData():void {
        if (_reportRenderer == null) {
            pendingRequest = true;
        } else {
            _dataService.retrieveData(_reportID);
        }
    }

    private function gotData(event:EmbeddedDataServiceEvent):void {
        _reportRenderer.renderReport(event.dataSet, event.analysisDefinition, event.clientProcessorMap);
    }

    private function loadReportRenderer():void {
        _reportRenderer = lookup(_reportType);
        _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender);
        _reportRenderer.addEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown);
        _reportRenderer.addEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup);
        addChild(_reportRenderer as DisplayObject);
        if (pendingRequest) {
            pendingRequest = false;
            retrieveData();
        }
    }

    private function lookup(reportType:int):IReportRenderer {
        var controller:Class;
        switch(reportType) {
            case AnalysisDefinition.LIST:
                controller = ListModule;
                break;
            case AnalysisDefinition.CROSSTAB:
                controller = CrosstabModule;
                break;
            case AnalysisDefinition.MAP:
                controller = MapModule;
                break;
            case AnalysisDefinition.COLUMN:
                controller = ColumnChartModule;
                break;
            case AnalysisDefinition.COLUMN3D:
                controller = Column3DChartModule;
                break;
            case AnalysisDefinition.BAR:
                controller = BarChartModule;
                break;
            case AnalysisDefinition.BAR3D:
                controller = Bar3DChartModule;
                break;
            case AnalysisDefinition.PIE:
                controller = PieChartModule;
                break;
            case AnalysisDefinition.PIE3D:
                controller = Pie3DChartModule;
                break;
            case AnalysisDefinition.LINE:
                controller = LineChartModule;
                break;
            case AnalysisDefinition.LINE3D:
                controller = Line3DChartModule;
                break;
            case AnalysisDefinition.AREA:
                controller = AreaChartModule;
                break;
            case AnalysisDefinition.AREA3D:
                controller = Area3DChartModule;
                break;
            case AnalysisDefinition.PLOT:
                controller = PlotChartModule;
                break;
            case AnalysisDefinition.BUBBLE:
                controller = BubbleChartModule;
                break;
            case AnalysisDefinition.GAUGE:
                controller = GaugeModule;
                break;
            case AnalysisDefinition.TREEMAP:
                controller = TreeMapModule;
                break;
            case AnalysisDefinition.TREE:
                controller = TreeModule;
                break;
        }
        return new controller();
    }

    private function onRollup(event:HierarchyRollupEvent):void {

    }

    private function drilldown(event:HierarchyDrilldownEvent):void {

    }

    private function customChangeFromControlBar(event:CustomChangeEvent):void {
        _reportRenderer.onCustomChangeEvent(event);
    }

    private function forceRender(event:ReportRendererEvent):void {
        retrieveData();
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        Alert.show(event.errorText);
    }
    }
}