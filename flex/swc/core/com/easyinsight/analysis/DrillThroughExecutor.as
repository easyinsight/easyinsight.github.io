package com.easyinsight.analysis {

import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;
import flash.events.EventDispatcher;


import mx.controls.Alert;
import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DrillThroughExecutor extends EventDispatcher {

    private var analysisService:RemoteObject;
    private var drillThrough:DrillThrough;
    private var data:Object;
    private var analysisItem:AnalysisItem;
    private var report:AnalysisDefinition;

    public function DrillThroughExecutor(drillThrough:DrillThrough, data:Object, analysisItem:AnalysisItem, report:AnalysisDefinition) {
        this.drillThrough = drillThrough;
        this.data = data;
        this.analysisItem = analysisItem;
        this.report = report;
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.drillThrough.addEventListener(ResultEvent.RESULT, onResult);
    }

    public function send():void {
        ProgressAlert.alert(Application.application as UIComponent, "Retrieving report information...", null, analysisService.drillThrough);
        analysisService.drillThrough.send(drillThrough, data, analysisItem, report);
    }

    private function onResult(event:ResultEvent):void {
        var result:DrillThroughResponse = analysisService.drillThrough.lastResult as DrillThroughResponse;
        analysisService.drillThrough.removeEventListener(ResultEvent.RESULT, onResult);
        dispatchEvent(new DrillThroughEvent(drillThrough, result));
    }
}
}