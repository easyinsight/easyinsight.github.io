package com.easyinsight.analysis {

import com.easyinsight.report.ReportNavigationEvent;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DrillThroughExecutor extends EventDispatcher {

    private var analysisService:RemoteObject;
    private var reportID:int;
    private var editor:Boolean;
    private var filters:ArrayCollection;

    public function DrillThroughExecutor(reportID:int, filters:ArrayCollection) {
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.openAnalysisIfPossible.addEventListener(ResultEvent.RESULT, onResult);
        this.reportID = reportID;
        this.filters = filters;
    }

    public function send():void {
        analysisService.openAnalysisIfPossible.send(reportID);
    }

    private function onResult(event:ResultEvent):void {
        var result:InsightResponse = analysisService.openAnalysisIfPossible.lastResult as InsightResponse;
        if (result.insightDescriptor != null) {
            if (editor) {
                //dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(result.insightDescriptor)));
            } else {
                dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, result.insightDescriptor, filters));
            }
        }
    }
}
}