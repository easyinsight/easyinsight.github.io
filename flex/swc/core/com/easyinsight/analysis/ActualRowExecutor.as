package com.easyinsight.analysis {

import com.easyinsight.rowedit.ActualRowEvent;
import com.easyinsight.rowedit.ActualRowSet;
import com.easyinsight.util.ProgressAlert;

import flash.events.EventDispatcher;

import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ActualRowExecutor extends EventDispatcher {

    private var analysisService:RemoteObject;
    private var data:Object;
    private var analysisItem:AnalysisItem;
    private var report:AnalysisDefinition;

    public function ActualRowExecutor(data:Object, analysisItem:AnalysisItem, report:AnalysisDefinition) {
        this.data = data;
        this.analysisItem = analysisItem;
        this.report = report;
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.getActualRows.addEventListener(ResultEvent.RESULT, onResult);
    }

    public function send():void {
        ProgressAlert.alert(Application.application as UIComponent, "Retrieving actual rows...", null, analysisService.getActualRows);
        analysisService.getActualRows.send(data, analysisItem, report, new Date().getTimezoneOffset());
    }

    private function onResult(event:ResultEvent):void {
        var actualRowSet:ActualRowSet = analysisService.getActualRows.lastResult as ActualRowSet;
        analysisService.getActualRows.removeEventListener(ResultEvent.RESULT, onResult);
        dispatchEvent(new ActualRowEvent(actualRowSet));
    }
}
}