package com.easyinsight.analysis {
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;
import flash.events.EventDispatcher;


import mx.controls.Alert;
import mx.core.Application;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DrillThroughExecutor extends EventDispatcher {

    private var analysisService:RemoteObject;
    private var drillThrough:DrillThrough;

    public function DrillThroughExecutor(drillThrough:DrillThrough) {
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.openAnalysisIfPossibleByID.addEventListener(ResultEvent.RESULT, onResult);
        this.drillThrough = drillThrough;
    }

    public function send():void {
        ProgressAlert.alert(Application.application as DisplayObject, "Retrieving report information...", null, analysisService.openAnalysisIfPossibleByID);
        analysisService.openAnalysisIfPossibleByID.send(drillThrough.reportID);
    }

    private function onResult(event:ResultEvent):void {
        var result:InsightResponse = analysisService.openAnalysisIfPossibleByID.lastResult as InsightResponse;
        analysisService.openAnalysisIfPossibleByID.removeEventListener(ResultEvent.RESULT, onResult);
        if (result.insightDescriptor != null) {
            dispatchEvent(new DrillThroughEvent(result.insightDescriptor, drillThrough));
        } else {
            Alert.show("We were unable to load the requested report.");
        }
    }
}
}