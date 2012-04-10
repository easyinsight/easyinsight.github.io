package com.easyinsight.analysis {

import com.easyinsight.util.ProgressAlert;

import flash.events.EventDispatcher;

import mx.core.Application;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class CalculationConversionExecutor extends EventDispatcher {

    private var analysisService:RemoteObject;
    private var analysisItem:AnalysisItem;
    private var dataSourceID:int;

    public function CalculationConversionExecutor(analysisItem:AnalysisItem, dataSourceID:int) {
        this.dataSourceID = dataSourceID;
        this.analysisItem = analysisItem;
        analysisService = new RemoteObject();
        analysisService.destination = "feeds";
        analysisService.convertToCalculation.addEventListener(ResultEvent.RESULT, onResult);
    }

    public function send():void {
        ProgressAlert.alert(Application.application as UIComponent, "Converting to calculation...", null, analysisService.convertToCalculation);
        analysisService.convertToCalculation.send(analysisItem, dataSourceID);
    }

    private function onResult(event:ResultEvent):void {

    }
}
}