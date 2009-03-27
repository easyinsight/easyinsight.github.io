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
    private var _availableFields:ArrayCollection;

    private var moduleInfo:IModuleInfo;

    private var _controlBar:IReportControlBar;
    private var _reportRenderer:IReportRenderer;
    private var _dataService:IReportDataService;

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
            if (_controlBar.isDataValid()) {
                _analysisDefinition = _controlBar.createAnalysisDefinition();
                _analysisDefinition.createDefaultLimits();
                _dataService.retrieveData(_analysisDefinition);
            }
        }
    }

    private function gotData(event:DataServiceEvent):void {
        _controlBar.onDataReceipt(event);
        _reportRenderer.renderReport(event.dataSet, _analysisDefinition, event.clientProcessorMap);
    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule("/app/easyui-debug/" + _reportRendererModule);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        moduleInfo.load();
    }
            
    private function reportLoadHandler(event:ModuleEvent):void {
        _reportRenderer = moduleInfo.factory.create() as IReportRenderer;
        _reportRenderer.addEventListener(ReportRendererEvent.ADD_ITEM, onItemAdded);
        _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender);
        _reportRenderer.addEventListener(CustomChangeEvent.CUSTOM_CHANGE, customChangeFromRenderer);
        addChild(_reportRenderer as DisplayObject);
        if (pendingRequest) {
            pendingRequest = false;
            retrieveData();
        }
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
        target.hierarchies = source.hierarchies;
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

    public function search(keyword:String):void {
    }

    public function getCoreView():DisplayObject {
        return _reportRenderer as DisplayObject;
    }}
}