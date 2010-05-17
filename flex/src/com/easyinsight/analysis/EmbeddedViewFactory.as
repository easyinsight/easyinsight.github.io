package com.easyinsight.analysis {

import com.easyinsight.analysis.service.EmbeddedDataService;
import com.easyinsight.analysis.service.ReportRetrievalFault;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.framework.HierarchyOverride;
import com.easyinsight.report.AbstractViewFactory;

import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.util.EIErrorEvent;

import flash.display.DisplayObject;
import flash.system.ApplicationDomain;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class EmbeddedViewFactory extends AbstractViewFactory implements IRetrievable {

    private var _reportRendererModule:String;
    //private var _newDefinition:Class;
    private var _reportDataService:Class = EmbeddedDataService;



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

    

    override protected function createChildren():void {
        super.createChildren();

        _dataService = new _reportDataService();
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        _dataService.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        _dataService.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, gotData);
        _dataService.addEventListener(ReportRetrievalFault.RETRIEVAL_FAULT, retrievalFault);
        loadReportRenderer();
    }

    private function retrievalFault(event:ReportRetrievalFault):void {
        dispatchEvent(event);
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
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
            for each (var hierarchyOverride:HierarchyOverride in overrideObj) {
                overrides.addItem(hierarchyOverride);
            }
            _dataService.retrieveData(reportID, dataSourceID, filterDefinitions, allSources, drillthroughFilters, _noCache, overrides);
        }
    }

    private var overrideObj:Object = new Object();

    override public function addOverride(hierarchyOverride:HierarchyOverride):void {
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

    override public function gotData(event:EmbeddedDataServiceEvent):void {
        if (event.credentialRequirements != null && event.credentialRequirements.length > 0) {
        } else {
            event.additionalProperties.prefix = _prefix;
            try {
                _report = event.analysisDefinition;
                _reportRenderer.renderReport(event.dataSet, event.analysisDefinition, event.clientProcessorMap, event.additionalProperties);
                dispatchEvent(event);
            } catch(e:Error) {
                dispatchEvent(new EIErrorEvent(e));
            }
        }

    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule(_prefix + "/app/easyui-debug/" + _reportRendererModule);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        moduleInfo.load(ApplicationDomain.currentDomain);
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        _reportRenderer = moduleInfo.factory.create() as IReportRenderer;
        _reportRenderer.addEventListener(ReportRendererEvent.FORCE_RENDER, forceRender);
        _reportRenderer.addEventListener(HierarchyDrilldownEvent.DRILLDOWN, drilldown);
        _reportRenderer.addEventListener(HierarchyRollupEvent.HIERARCHY_ROLLUP, onRollup);
        _reportRenderer.addEventListener(ReportNavigationEvent.TO_REPORT, toReport);
        _dataService.preserveValues = _reportRenderer.preserveValues();
        addChild(_reportRenderer as DisplayObject);
        if (pendingRequest) {
            pendingRequest = false;
            retrieveData(false);
        }
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
        Alert.show(event.errorText);
    }
    }
}