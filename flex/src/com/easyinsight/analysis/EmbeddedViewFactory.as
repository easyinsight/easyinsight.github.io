package com.easyinsight.analysis {

import com.easyinsight.analysis.service.EmbeddedDataService;
import com.easyinsight.framework.DataServiceLoadingEvent;
import flash.display.DisplayObject;
import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class EmbeddedViewFactory extends VBox {

    private var _reportRendererModule:String;
    private var _newDefinition:Class;
    private var _reportDataService:Class = EmbeddedDataService;

    private var _reportID:int;
    private var _dataSourceID:int;
    private var _availableFields:ArrayCollection;

    private var _prefix:String = "";

    private var moduleInfo:IModuleInfo;

    private var _reportRenderer:IReportRenderer;
    private var _dataService:IEmbeddedDataService = new EmbeddedDataService();

    private var _filterDefinitions:ArrayCollection;

    private var pendingRequest:Boolean = false;

    public function EmbeddedViewFactory() {
        this.percentHeight = 100;
        this.percentWidth = 100;
    }


    public function set filterDefinitions(value:ArrayCollection):void {
        _filterDefinitions = value;
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


    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
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

    public function get dataService():IEmbeddedDataService {
        return _dataService;
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
            _dataService.retrieveData(_reportID, _dataSourceID, _filterDefinitions);
        }
    }

    private function gotData(event:EmbeddedDataServiceEvent):void {
        _reportRenderer.renderReport(event.dataSet, event.analysisDefinition, event.clientProcessorMap);
        dispatchEvent(event);
    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule(_prefix + "/app/easyui-debug/" + _reportRendererModule);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        moduleInfo.load();
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        _reportRenderer = moduleInfo.factory.create() as IReportRenderer;
        _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender);
        _reportRenderer.addEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown);
        _reportRenderer.addEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup);
        addChild(_reportRenderer as DisplayObject);
        if (pendingRequest) {
            pendingRequest = false;
            retrieveData();
        }
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