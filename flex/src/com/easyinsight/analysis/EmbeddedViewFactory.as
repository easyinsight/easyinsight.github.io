package com.easyinsight.analysis {

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
    private var _reportDataService:Class;
    
    private var _analysisDefinition:AnalysisDefinition;
    private var _availableFields:ArrayCollection;

    private var moduleInfo:IModuleInfo;

    private var _reportRenderer:IReportRenderer;
    private var _dataService:IReportDataService;

    public function EmbeddedViewFactory() {
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

    public function set reportRenderer(val:String):void {
        _reportRendererModule = val;
    }

    public function set newDefinition(val:Class):void {
        _newDefinition = val;
    }

    override protected function createChildren():void {
        super.createChildren();

        _dataService = new _reportDataService();
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceEvent.DATA_RETURNED, gotData);

        loadReportRenderer();
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
        dispatchEvent(event);
    }

    private function onDataRequest(event:ReportDataEvent):void {
        retrieveData();
    }

    public function retrieveData():void {
        _dataService.retrieveData(_analysisDefinition);
    }

    private function gotData(event:DataServiceEvent):void {
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
        addChild(_reportRenderer as DisplayObject);
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        Alert.show(event.errorText);
    }

    }
}