package com.easyinsight.analysis {

import com.easyinsight.filtering.FilterRawData;
import com.easyinsight.framework.DataServiceLoadingEvent;
import flash.display.DisplayObject;
import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class DataViewFactory extends VBox {

    private var _reportControlBar:Class;
    private var _reportRendererModule:String;
    private var _newDefinition:Class;
    private var _reportDataService:Class;
    
    private var _analysisDefinition:AnalysisDefinition;

    private var _lastData:ArrayCollection;
    private var _lastClientProcessorMap:Object;
    private var _availableFields:ArrayCollection;

    private var moduleInfo:IModuleInfo;

    private var _controlBar:IReportControlBar;
    private var _reportRenderer:IReportRenderer;
    private var _dataService:IReportDataService;

    private var _loadingDisplay:LoadingModuleDisplay;

    private var pendingRequest:Boolean = false;

    public function DataViewFactory() {
        this.percentHeight = 100;
        this.percentWidth = 100;
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

    override protected function createChildren():void {
        super.createChildren();

        _dataService = new _reportDataService();
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceEvent.DATA_RETURNED, gotData);

        _controlBar = createReportControlBar();
        _controlBar.analysisItems = _availableFields;
        _controlBar.addEventListener(ReportDataEvent.REQUEST_DATA, onDataRequest);
        _controlBar.addEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromControlBar);
        _controlBar.analysisDefinition = _analysisDefinition;
        addChild(_controlBar as DisplayObject);

        loadReportRenderer();
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
            removeChild(_reportRenderer as DisplayObject);
        }
        moduleInfo = null;
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
        dispatchEvent(event);
    }

    private function onDataRequest(event:ReportDataEvent):void {
        if (event.reload || _lastData == null) {
            retrieveData();
        } else {
            _analysisDefinition = _controlBar.createAnalysisDefinition();
            _reportRenderer.renderReport(_lastData, _analysisDefinition, _lastClientProcessorMap);
        }
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        if (_reportRenderer == null) {
            pendingRequest = true;
        } else {
            _analysisDefinition = _controlBar.createAnalysisDefinition();
            if (_controlBar.isDataValid()) {
                _analysisDefinition.createDefaultLimits();
                _dataService.retrieveData(_analysisDefinition, refreshAllSources);
            } else {
                _reportRenderer.renderReport(new ArrayCollection(), _analysisDefinition, new Object());
            }
        }
    }

    

    private function gotData(event:DataServiceEvent):void {
        dispatchEvent(event);
        _controlBar.onDataReceipt(event);
        _lastData = event.dataSet;
        _lastClientProcessorMap = event.clientProcessorMap;
        _reportRenderer.renderReport(event.dataSet, _analysisDefinition, event.clientProcessorMap);
    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule("/app/easyui-debug/" + _reportRendererModule);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.moduleInfo = moduleInfo;
        addChild(_loadingDisplay);
        moduleInfo.load();
    }
            
    private function reportLoadHandler(event:ModuleEvent):void {
        moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.removeEventListener(ModuleEvent.ERROR, reportFailureHandler);
        _reportRenderer = moduleInfo.factory.create() as IReportRenderer;
        //moduleInfo = null;
        if (_reportRenderer != null) {
            _reportRenderer.addEventListener(ReportRendererEvent.ADD_ITEM, onItemAdded);
            _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender);
            _reportRenderer.addEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromRenderer);
            _reportRenderer.addEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown);
            _reportRenderer.addEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup);
            if (_loadingDisplay != null) {
                removeChild(_loadingDisplay);
                _loadingDisplay.moduleInfo = null;
                _loadingDisplay = null;
            }
            addChild(_reportRenderer as DisplayObject);
            if (pendingRequest) {
                pendingRequest = false;
                retrieveData();
            }
        }
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
    }

    public function createNewDefinition():AnalysisDefinition {
        _analysisDefinition = new _newDefinition();
        return _analysisDefinition;
    }

    public function createFilterRawData():FilterRawData {
        return _reportRenderer.createFilterRawData();
    }

    public function invalidateItems(invalidAnalysisItemIDs:ArrayCollection):void {
    }

    public function updateExportMetadata():void {
    }

    public function getCoreView():DisplayObject {
        return _reportRenderer as DisplayObject;
    }

    public function addItem(analysisItem:AnalysisItem):void {
        _controlBar.addItem(analysisItem);
    }}
}