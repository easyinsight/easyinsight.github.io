package com.easyinsight.analysis {

import com.easyinsight.filtering.FilterRawData;
import com.easyinsight.framework.Constants;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.pseudocontext.CustomCodeEvent;
import com.easyinsight.report.ReportEventProcessor;
import com.easyinsight.util.UserAudit;

import flash.display.DisplayObject;
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.Canvas;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.controls.ProgressBar;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class DataViewFactory extends VBox implements IRetrievable {

    private var _reportControlBar:Class;
    private var _reportRendererModule:String;
    private var _newDefinition:Class;
    private var _reportDataService:Class;

    private var _adHocMode:Boolean;
    
    private var _analysisDefinition:AnalysisDefinition;

    private var _lastData:ArrayCollection;
    private var _availableFields:ArrayCollection;

    private var moduleInfo:IModuleInfo;

    private var _dataSourceID:int;

    private var _controlBar:IReportControlBar;
    private var _reportRenderer:IReportRenderer;
    private var _dataService:IReportDataService;

    private var _loadingDisplay:LoadingModuleDisplay;

    private var pendingRequest:Boolean = false;

    public function DataViewFactory() {
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    public function highlightDropAreas():void {
        _controlBar.highlight();
    }

    public function revertDropAreas():void {
        _controlBar.normal();
    }

    public function set adHocMode(value:Boolean):void {
        _adHocMode = value;
    }

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    public function set availableFields(val:ArrayCollection):void {
        _availableFields = val;
    }

    public function set analysisDefinition(val:AnalysisDefinition):void {
        _analysisDefinition = val;
    }


    public function get analysisDefinition():AnalysisDefinition {
        return _analysisDefinition;
    }

    public function set reportDataService(val:Class):void {
        _reportDataService = val;
    }

    public function set reportControlBar(val:Class):void {
        _reportControlBar = val;
    }

    public function set reportRenderer(val:String):void {
        _reportRendererModule = val;
    }

    public function set newDefinition(val:Class):void {
        _newDefinition = val;
    }

    public function createReportControlBar():IReportControlBar {
        return new _reportControlBar();
    }

    public function get dropAreaControlBar():IReportControlBar {
        return _controlBar;
    }

    private var _obfuscate:Boolean;


    [Bindable(event="obfuscateChanged")]
    public function get obfuscate():Boolean {
        return _obfuscate;
    }

    public function set obfuscate(value:Boolean):void {
        if (_obfuscate == value) return;
        _obfuscate = value;
        dispatchEvent(new Event("obfuscateChanged"));
    }

    private var firstSize:Boolean = true;

    private var _controlBarWidth:int;


    [Bindable(event="controlBarWidthChanged")]
    public function get controlBarWidth():int {
        return _controlBarWidth;
    }

    public function set controlBarWidth(value:int):void {
        if (_controlBarWidth == value) return;
        _controlBarWidth = value;
        dispatchEvent(new Event("controlBarWidthChanged"));
    }

    protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        /*if (firstSize) {
            DisplayObject(_controlBar).width = unscaledWidth;
            firstSize = false;
        }*/
    }

    override protected function createChildren():void {
        super.createChildren();

        _dataService = new _reportDataService();
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent, false, 0, true);
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent, false, 0, true);
        _dataService.addEventListener(DataServiceEvent.DATA_RETURNED, gotData);
        BindingUtils.bindProperty(_dataService, "obfuscate", this, "obfuscate");

        _controlBar = createReportControlBar();
        _controlBar["id"] = "_controlBar";
        _controlBar.analysisItems = _availableFields;
        _controlBar.dataSourceID = _dataSourceID;
        _controlBar.addEventListener(ReportDataEvent.REQUEST_DATA, onDataRequest, false, 0, true);
        _controlBar.addEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromControlBar, false, 0, true);
        _controlBar.analysisDefinition = _analysisDefinition;
        //BindingUtils.bindProperty(_controlBar, "width", this, "controlBarWidth");
        addChild(_controlBar as DisplayObject);

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
        reportCanvas = new Canvas();
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

    private var canvas:Canvas;

    private var reportCanvas:Canvas;

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

    public function cleanup():void {
        _dataService.removeEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        _dataService.removeEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        _dataService.removeEventListener(DataServiceEvent.DATA_RETURNED, gotData);
        _controlBar.removeEventListener(ReportDataEvent.REQUEST_DATA, onDataRequest);
        _controlBar.removeEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromControlBar);
        if (_reportRenderer != null) {
            _reportRenderer.removeEventListener(ReportRendererEvent.ADD_ITEM, onItemAdded);
            _reportRenderer.removeEventListener(ReportRendererEvent.FORCE_RENDER, forceRender);
            _reportRenderer.removeEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromRenderer);
            _reportRenderer.removeEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown);
            _reportRenderer.removeEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup);
            reportCanvas.removeChild(_reportRenderer as DisplayObject);
        }
        moduleInfo = null;
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
        showLoading = (event.type == DataServiceLoadingEvent.LOADING_STARTED);
        dispatchEvent(event);
    }

    private function onDataRequest(event:ReportDataEvent):void {
        if (event.reload || _lastData == null) {
            retrieveData();
        } else {
            _analysisDefinition = _controlBar.createAnalysisDefinition();
            _reportRenderer.renderReport(_lastData, _analysisDefinition, new Object(), null);
        }
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        if (_adHocMode) {
            if (_reportRenderer == null) {
                pendingRequest = true;
            } else {
                _analysisDefinition = _controlBar.createAnalysisDefinition();
                if (_controlBar.isDataValid()) {
                    _analysisDefinition.createDefaultLimits();
                    _dataService.retrieveData(_analysisDefinition, refreshAllSources);
                    UserAudit.instance().audit(UserAudit.VALID_REPORT);
                } else {
                    _reportRenderer.renderReport(new ArrayCollection(), _analysisDefinition, new Object(), null);
                }
            }
        }
    }

    public function forceRetrieve():void {
        if (_reportRenderer == null) {
            pendingRequest = true;
        } else {
            _analysisDefinition = _controlBar.createAnalysisDefinition();
            if (_controlBar.isDataValid()) {
                _analysisDefinition.createDefaultLimits();
                _dataService.retrieveData(_analysisDefinition, false);
                UserAudit.instance().audit(UserAudit.VALID_REPORT);
            } else {
                _reportRenderer.renderReport(new ArrayCollection(), _analysisDefinition, new Object(), null);
            }
        }
    }

    public function isDataValid():Boolean {
        return _controlBar.isDataValid();
    }

    private function gotData(event:DataServiceEvent):void {
        dispatchEvent(event);
        _controlBar.onDataReceipt(event);
        _lastData = event.dataSet;
        _reportRenderer.renderReport(event.dataSet, _analysisDefinition, new Object(), event.additionalProperties);
    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule("/app/"+Constants.instance().buildPath+"/" + _reportRendererModule);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler, false, 0, true);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler, false, 0, true);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.moduleInfo = moduleInfo;
        reportCanvas.addChild(_loadingDisplay);
        moduleInfo.load();
    }
            
    private function reportLoadHandler(event:ModuleEvent):void {
        if (moduleInfo != null) {
            moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.removeEventListener(ModuleEvent.ERROR, reportFailureHandler);
            _reportRenderer = moduleInfo.factory.create() as IReportRenderer;
        }
        //moduleInfo = null;
        if (_reportRenderer != null) {
            _reportRenderer.addEventListener(ReportRendererEvent.ADD_ITEM, onItemAdded, false, 0, true);
            _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender, false, 0, true);
            _reportRenderer.addEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromRenderer, false, 0, true);
            _reportRenderer.addEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown, false, 0, true);
            _reportRenderer.addEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup, false, 0, true);
            _reportRenderer.addEventListener(ReportWindowEvent.REPORT_WINDOW, onReportWindow, false, 0, true);
            _reportRenderer.addEventListener(CustomCodeEvent.CUSTOM_CODE_LINK, onCustomCode, false, 0, true);
            _dataService.preserveValues = _reportRenderer.preserveValues();
            if (_loadingDisplay != null) {
                reportCanvas.removeChild(_loadingDisplay);
                _loadingDisplay.moduleInfo = null;
                _loadingDisplay = null;
            }
            reportCanvas.addChild(_reportRenderer as DisplayObject);
            if (pendingRequest) {
                pendingRequest = false;
                retrieveData();
            }
        }
    }

    private function onCustomCode(event:CustomCodeEvent):void {
        CustomCodeWindow.newWindow(this, event, _dataSourceID);
    }

    private function onReportWindow(event:ReportWindowEvent):void {
        if (event.dataSourceID == 0) {
            event.dataSourceID = _dataSourceID;
        }
        ReportEventProcessor.fromEvent(event, this);
    }

    private function onRollup(event:HierarchyRollupEvent):void {
        
    }

    private function drilldown(event:HierarchyDrilldownEvent):void {
        
    }

    private function customChangeFromControlBar(event:CustomChangeEvent):void {
        _reportRenderer.onCustomChangeEvent(event);
    }    

    private function customChangeFromRenderer(event:CustomChangeEvent):void {
        _controlBar.onCustomChangeEvent(event);
    }

    private function forceRender(event:ReportRendererEvent):void {
        retrieveData();
    }

    private function onItemAdded(event:ReportRendererEvent):void {
        _controlBar.addItem(event.analysisItem);
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.removeEventListener(ModuleEvent.ERROR, reportFailureHandler);
        Alert.show(event.errorText);
    }

    public function fromExistingDefinition(existingDefinition:AnalysisDefinition):AnalysisDefinition {
        if (existingDefinition is _newDefinition) {
            return existingDefinition;
        } else {
            var newDef:AnalysisDefinition = new _newDefinition();
            if (_explicitType > 0) {
                newDef.reportType = _explicitType;
            }
            var fields:ArrayCollection = existingDefinition.getFields();
            newDef.populate(fields);
            copyStandardData(existingDefinition, newDef);
            return newDef;
        }
    }

    private function copyStandardData(source:AnalysisDefinition, target:AnalysisDefinition):void {
        target.name = source.name;
        target.dataFeedID = source.dataFeedID;
        target.analysisID = source.analysisID;
        target.tagCloud = source.tagCloud;
        target.dataScrubs = source.dataScrubs;
        target.dateCreated = source.dateCreated;
        target.filterDefinitions = source.filterDefinitions;
        target.policy = source.policy;
        target.addedItems = source.addedItems;
        target.viewCount = source.viewCount;
        target.ratingCount = source.ratingCount;
        target.ratingAverage = source.ratingAverage;
        target.canSaveDirectly = source.canSaveDirectly;
        target.description = source.description;
        target.marketplaceVisible = source.marketplaceVisible;
        target.solutionVisible = source.solutionVisible;
        target.publiclyVisible = source.publiclyVisible;
        target.temporaryReport = source.temporaryReport;        
    }

    public function createNewDefinition():AnalysisDefinition {
        _analysisDefinition = new _newDefinition();
        if (_explicitType > 0) {
            _analysisDefinition.reportType = _explicitType;
        }
        return _analysisDefinition;
    }

    private var _explicitType:int;


    public function set explicitType(value:int):void {
        _explicitType = value;
    }

    public function createFilterRawData():FilterRawData {
        return _reportRenderer.createFilterRawData();
    }

    public function invalidateItems(invalidAnalysisItemIDs:ArrayCollection):void {
    }

    public function updateExportMetadata():void {
        _reportRenderer.updateExportMetadata();
    }

    public function getCoreView():DisplayObject {
        return _reportRenderer as DisplayObject;
    }

    public function addItem(analysisItem:AnalysisItem):void {
        _controlBar.addItem(analysisItem);
    }}
}