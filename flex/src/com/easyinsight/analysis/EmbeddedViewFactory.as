package com.easyinsight.analysis {

import com.easyinsight.analysis.service.EmbeddedDataService;
import com.easyinsight.analysis.service.ReportRetrievalFault;
import com.easyinsight.customupload.ProblemDataEvent;
import com.easyinsight.framework.Constants;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.report.AbstractViewFactory;
import com.easyinsight.report.ReportCanvas;
import com.easyinsight.report.ReportEventProcessor;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.util.EIErrorEvent;

import flash.display.DisplayObject;
import flash.events.Event;
import flash.system.ApplicationDomain;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.Canvas;
import mx.controls.Alert;
import mx.controls.ProgressBar;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class EmbeddedViewFactory extends AbstractViewFactory implements IRetrievable {

    private var _reportRendererModule:String;
    //private var _newDefinition:Class;
    private var _reportDataService:Class = EmbeddedDataService;

    private var _loadingDisplay:LoadingModuleDisplay;

    private var _prefix:String = "";

    private var moduleInfo:IModuleInfo;

    private var _reportRenderer:IReportRenderer;
    private var _dataService:IEmbeddedDataService = new EmbeddedDataService();



    private var pendingRequest:Boolean = false;

    public function EmbeddedViewFactory() {
    }


    public function get reportRendererModule():String {
        return _reportRendererModule;
    }

    override public function set prefix(val:String):void {
        _prefix = val;
    }



    public function set reportDataService(val:Class):void {
        _reportDataService = val;
    }

    public function set reportRenderer(val:String):void {
        _reportRendererModule = val;
    }

    /*public function set newDefinition(val:Class):void {
        _newDefinition = val;
    }*/

    public function get dataService():IEmbeddedDataService {
        return _dataService;
    }

    private var canvas:Canvas;

    private var reportCanvas:ReportCanvas;

    private var _showLoading:Boolean = false;


    [Bindable(event="showLoadingChanged")]
    public function get showLoading():Boolean {
        return _showLoading;
    }

    public function set showLoading(value:Boolean):void {
        if (_showLoading == value) return;
        _showLoading = value;
        dispatchEvent(new Event("showLoadingChanged"));
    }

    private function onError(event:EIErrorEvent):void {
        stackTrace = event.error.getStackTrace();
        overlayIndex = 3;
    }

    override protected function createChildren():void {
        super.createChildren();

        _dataService = new _reportDataService();
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        _dataService.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, gotData);
        _dataService.addEventListener(ReportRetrievalFault.RETRIEVAL_FAULT, retrievalFault);
        _dataService.addEventListener(EIErrorEvent.ERROR, onError);
        canvas = new Canvas();
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
        BindingUtils.bindProperty(reportCanvas, "loading", this, "loading");
        BindingUtils.bindProperty(reportCanvas, "overlayIndex", this, "overlayIndex");
        BindingUtils.bindProperty(reportCanvas, "stackTrace", this, "overlayIndex");
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
        loadReportRenderer();
    }

    private function retrievalFault(event:ReportRetrievalFault):void {
        overlayIndex = 2;
        dispatchEvent(event);
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
        if (event.type == DataServiceLoadingEvent.LOADING_STARTED) overlayIndex = 1;
        loading = event.type == DataServiceLoadingEvent.LOADING_STARTED;
        dispatchEvent(event);
    }

    private function onDataRequest(event:ReportDataEvent):void {
        retrieveData(false);
    }

    override public function retrieveData(allSources:Boolean = false):void {
        if (_reportRenderer == null) {
            pendingRequest = true;
        } else {
            var overrides:ArrayCollection = new ArrayCollection();
            for each (var hierarchyOverride:AnalysisItemOverride in overrideObj) {
                overrides.addItem(hierarchyOverride);
            }
            _dataService.retrieveData(reportID, dataSourceID, filterDefinitions, allSources, drillthroughFilters, _noCache, overrides);
        }
    }

    private var overrideObj:Object = new Object();

    override public function addOverride(hierarchyOverride:AnalysisItemOverride):void {
        overrideObj[hierarchyOverride.analysisItemID] = hierarchyOverride;
    }

    private var _noCache:Boolean = false;

    override public function set noCache(value:Boolean):void {
        _noCache = value;
    }

    private var _report:AnalysisDefinition;

    override public function get report():AnalysisDefinition {
        return _report;
    }

    private function onProblem(event:ProblemDataEvent):void {
        var overrides:ArrayCollection = new ArrayCollection();
        for each (var hierarchyOverride:AnalysisItemOverride in overrideObj) {
            overrides.addItem(hierarchyOverride);
        }
        _dataService.retrieveData(reportID, dataSourceID, filterDefinitions, false, drillthroughFilters, _noCache, overrides);
    }

    override public function gotData(event:EmbeddedDataServiceEvent):void {
        if (event.reportFault != null) {
            event.reportFault.popup(this, onProblem);
        } else {
            event.additionalProperties.prefix = _prefix;
            try {
                _report = event.analysisDefinition;
                _reportRenderer.renderReport(event.dataSet, event.analysisDefinition, new Object(), event.additionalProperties);
                dispatchEvent(event);
            } catch(e:Error) {
                dispatchEvent(new EIErrorEvent(e));
            }
            overlayIndex = 0;
        }

    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule(_prefix + "/app/"+Constants.instance().buildPath+"/" + _reportRendererModule);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.moduleInfo = moduleInfo;
        reportCanvas.addChild(_loadingDisplay);
        moduleInfo.load(ApplicationDomain.currentDomain);
    }

    private var _stackTrace:String;

    private var _overlayIndex:int;

    private var _loading:Boolean;

    [Bindable(event="stackTraceChanged")]
    public function get stackTrace():String {
        return _stackTrace;
    }

    public function set stackTrace(value:String):void {
        if (_stackTrace == value) return;
        _stackTrace = value;
        dispatchEvent(new Event("stackTraceChanged"));
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

    [Bindable(event="loadingChanged")]
    public function get loading():Boolean {
        return _loading;
    }

    public function set loading(value:Boolean):void {
        if (_loading == value) return;
        _loading = value;
        dispatchEvent(new Event("loadingChanged"));
    }

// stackTrace="{stackTrace}" overlayIndex="{overlayIndex}" loading="{loading}"

    private function reportLoadHandler(event:ModuleEvent):void {
        _reportRenderer = moduleInfo.factory.create() as IReportRenderer;
        _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender, false, 0, true);
        _reportRenderer.addEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown, false, 0, true);
        _reportRenderer.addEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup, false, 0, true);
        _reportRenderer.addEventListener(ReportNavigationEvent.TO_REPORT, toReport, false, 0, true);
        _reportRenderer.addEventListener(ReportWindowEvent.REPORT_WINDOW, onReportWindow, false, 0, true);
        _reportRenderer.addEventListener(AnalysisItemChangeEvent.ANALYSIS_ITEM_CHANGE, onItemChange, false, 0, true);
        _dataService.preserveValues = _reportRenderer.preserveValues();
        if (_loadingDisplay != null) {
            reportCanvas.removeChild(_loadingDisplay);
            _loadingDisplay.moduleInfo = null;
            _loadingDisplay = null;
        }
        reportCanvas.reportBox.addChild(_reportRenderer as DisplayObject);
        if (pendingRequest) {
            pendingRequest = false;
            retrieveData(false);
        }
    }

    private function onItemChange(event:AnalysisItemChangeEvent):void {
        var o:DateLevelOverride = new DateLevelOverride();
        o.analysisItemID = event.item.analysisItemID;
        o.dateLevel = AnalysisDateDimension(event.item).dateLevel;
        dispatchEvent(new AnalysisItemOverrideEvent(o));
    }

    private function onReportWindow(event:ReportWindowEvent):void {
        var window:ReportEventProcessor = ReportEventProcessor.fromEvent(event, this);
        window.addEventListener(ReportNavigationEvent.TO_REPORT, toReport, false, 0, true);
    }

    private function toReport(event:ReportNavigationEvent):void {
        dispatchEvent(event);
    }

    private function onRollup(event:HierarchyRollupEvent):void {

    }

    private function drilldown(event:HierarchyDrilldownEvent):void {

    }



    private function customChangeFromControlBar(event:CustomChangeEvent):void {
        _reportRenderer.onCustomChangeEvent(event);
    }

    override public function updateExportMetadata():void {
        _reportRenderer.updateExportMetadata();
    }

    private function forceRender(event:ReportRendererEvent):void {
        retrieveData(false);
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        if (event.errorText != "SWF is not a loadable module") {
            Alert.show(event.errorText);
        }
    }
    }
}