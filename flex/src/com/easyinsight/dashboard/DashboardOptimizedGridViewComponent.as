package com.easyinsight.dashboard {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedDataResults;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.IReportRenderer;
import com.easyinsight.analysis.LoadingModuleDisplay;
import com.easyinsight.analysis.gauge.GaugeEmbeddedController;
import com.easyinsight.analysis.service.EmbeddedOptimizedDataService;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.framework.Constants;
import com.easyinsight.report.ReportCanvas;
import com.easyinsight.util.EIErrorEvent;

import flash.events.Event;

import flash.system.ApplicationDomain;

import mx.binding.utils.BindingUtils;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.Canvas;
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.controls.Alert;
import mx.controls.ProgressBar;
import mx.core.UIComponent;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class DashboardOptimizedGridViewComponent extends Canvas implements IDashboardViewComponent {

    public var dashboardGrid:DashboardGrid;

    private var grid:Grid;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardOptimizedGridViewComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private var viewChildren:ArrayCollection;

    private var moduleInfo:IModuleInfo;

    private var _loadingDisplay:LoadingModuleDisplay;

    private function reportFailureHandler(event:ModuleEvent):void {
        if (event.errorText != "SWF is not a loadable module") {
            Alert.show(event.errorText);
        }
    }

    private function loadReportRenderer(moduleName:String):void {
        moduleInfo = ModuleManager.getModule(Constants.instance().prefix + "/app/"+Constants.instance().buildPath+"/" + moduleName);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.moduleInfo = moduleInfo;
        reportCanvas.addChild(_loadingDisplay);
        moduleInfo.load(ApplicationDomain.currentDomain);
    }

    private var reportMap:Object;

    private function onProblem():void {

    }

    public function gotData(event:OptimizedDataServiceEvent):void {
        if (event.reportFault != null) {
            event.reportFault.popup(this, onProblem);
        } else {
            //event.additionalProperties.prefix = _prefix;
            try {
                for (var reportID:String in event.resultsMap) {
                    var reportRenderer:IReportRenderer = reportMap[reportID];
                    var results:EmbeddedDataServiceEvent = event.resultsMap[reportID];
                    reportRenderer.renderReport(results.dataSet, results.analysisDefinition, new Object(), results.additionalProperties);
                }
            } catch(e:Error) {
                dispatchEvent(new EIErrorEvent(e));
            }
            overlayIndex = 0;
        }
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        if (_loadingDisplay != null) {
            reportCanvas.removeChild(_loadingDisplay);
            _loadingDisplay.moduleInfo = null;
            _loadingDisplay = null;
        }
        grid = new Grid();
        grid.percentWidth = 100;
        grid.percentHeight = 100;
        reportMap = new Object();
         for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = new GridRow();
            gridRow.percentWidth = 100;
            gridRow.percentHeight = 100;
            grid.addChild(gridRow);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = new GridItem();
                gridItem.setStyle("paddingLeft", dashboardGrid.paddingLeft);
                gridItem.setStyle("paddingRight", dashboardGrid.paddingRight);
                gridItem.setStyle("paddingTop", dashboardGrid.paddingTop);
                gridItem.setStyle("paddingBottom", dashboardGrid.paddingBottom);
                gridItem.setStyle("borderStyle", "solid");
                gridItem.setStyle("borderThickness", 1);
                gridItem.setStyle("borderColor", 0);
                gridItem.percentWidth = 100;
                gridItem.percentHeight = 100;
                var child:UIComponent = moduleInfo.factory.create() as UIComponent;
                var _reportRenderer:IReportRenderer = child as IReportRenderer;
                var dashboardReport:DashboardReport = DashboardReport(e.dashboardElement);
                dataSourceID = dashboardReport.report.dataFeedID;
                reportIDs.addItem(dashboardReport.report.id);
                reportMap[String(dashboardReport.report.id)] = _reportRenderer;
                /*_reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender, false, 0, true);
                _reportRenderer.addEventListener(ReportWindowEvent.REPORT_WINDOW, onReportWindow, false, 0, true);
                _reportRenderer.addEventListener(AnalysisItemChangeEvent.ANALYSIS_ITEM_CHANGE, onItemChange, false, 0, true);
                _dataService.preserveValues = _reportRenderer.preserveValues();*/
                viewChildren.addItem(child);
                gridItem.addChild(child);
                gridRow.addChild(gridItem);
            }
        }
        reportCanvas.reportBox.addChild(grid);
        setup = true;
        if (queued) {
            queued = false;
            retrievedDataOnce = true;
            retrieveReportData();
        }
    }

    private var service:EmbeddedOptimizedDataService;

    private var reportCanvas:ReportCanvas;

    private var _showLoading:Boolean;

    private var _overlayIndex:int;

    [Bindable(event="showLoadingChanged")]
    public function get showLoading():Boolean {
        return _showLoading;
    }

    public function set showLoading(value:Boolean):void {
        if (_showLoading == value) return;
        _showLoading = value;
        dispatchEvent(new Event("showLoadingChanged"));
    }

    [Bindable(event="overlayIndexChanged")]
    public function get overlayIndex():int {
        return _overlayIndex;
    }

    public function set overlayIndex(value:int):void {
        if (_overlayIndex == value) return;
        _overlayIndex = value;
        dispatchEvent(new Event("overlayIndexChanged"));
    }

    protected override function createChildren():void {
        super.createChildren();
        service = new EmbeddedOptimizedDataService();
        service.addEventListener(OptimizedDataServiceEvent.OPTIMIZE_RESULTS, gotData);
        viewChildren = new ArrayCollection();

        // before anything else, determine if we can optimize out the call

        // we can optimize IF...
        // all reports are the same type
        // all reports are gauges (for now)

        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(AnalysisDefinition.GAUGE);
        var controller:IEmbeddedReportController = new controllerClass();
        var moduleName:String = controller.createEmbeddedView().reportRendererModule;

        var canvas:Canvas = new Canvas();
        canvas.percentHeight = 100;
        canvas.percentWidth = 100;
        var box:Box = new Box();
        BindingUtils.bindProperty(box, "visible", this, "showLoading");
        box.percentWidth = 100;
        box.percentHeight = 100;
        box.setStyle("horizontalAlign", "center");
        box.setStyle("verticalAlign", "middle");
        var screen:Canvas = new Canvas();
        screen.percentHeight = 100;
        screen.percentWidth = 100;
        screen.setStyle("backgroundColor", 0x000000);
        screen.setStyle("backgroundAlpha", .1);
        BindingUtils.bindProperty(screen, "visible", this, "showLoading");
        reportCanvas = new ReportCanvas();
        BindingUtils.bindProperty(reportCanvas, "overlayIndex", this, "overlayIndex");
        //BindingUtils.bindProperty(reportCanvas, "stackTrace", this, "overlayIndex");
        reportCanvas.percentHeight = 100;
        reportCanvas.percentWidth = 100;
        var progressBar:ProgressBar = new ProgressBar();
        BindingUtils.bindProperty(progressBar, "indeterminate", this, "showLoading");
        progressBar.label = "Loading the report...";
        progressBar.setStyle("fontSize", 18);
        box.addChild(progressBar);
        canvas.addChild(reportCanvas);
        canvas.addChild(screen);
        canvas.addChild(box);
        addChild(canvas);
        loadReportRenderer(moduleName);
    }

    private function findItem(x:int, y:int):DashboardGridItem {
        for each (var e:DashboardGridItem in dashboardGrid.gridItems) {
            if (e.rowIndex == x && e.columnIndex == y) {
                return e;
            }
        }
        return null;
    }

    private var setup:Boolean = false;

    private var queued:Boolean;

    private var filterMap:Object = new Object();

    public function updateAdditionalFilters(filterMap:Object):void {
        for (var id:String in filterMap) {
            var filters:ArrayCollection = filterMap[id];
            if (filters != null) {
                this.filterMap[id] = filters;
            }
        }
    }

    private function createAdditionalFilters(filterMap:Object):ArrayCollection {
        var filterColl:ArrayCollection = new ArrayCollection();
        for (var id:String in filterMap) {
            var filters:ArrayCollection = filterMap[id];
            if (filters != null) {
                for each (var filter:FilterDefinition in filters) {
                    filterColl.addItem(filter);
                }
            }
        }
        return filterColl;
    }

    private function retrieveReportData():void {
        var filters:ArrayCollection = createAdditionalFilters(filterMap);
        service.retrieveReportData(reportIDs, dataSourceID, filters);
    }

    private var reportIDs:ArrayCollection = new ArrayCollection();

    private var dataSourceID:int;

    public function refresh():void {
        if (setup) {
            retrievedDataOnce = true;
            retrieveReportData();
        } else {
            queued = true;
        }
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        if (setup) {
            retrievedDataOnce = true;
            retrieveReportData();
        } else {
            queued = true;
        }
    }

    private var retrievedDataOnce:Boolean;

    public function initialRetrieve():void {
        if (!retrievedDataOnce) {
            if (setup) {
                retrievedDataOnce = true;
                retrieveReportData();
            } else {
                queued = true;
            }
        }
    }
}
}