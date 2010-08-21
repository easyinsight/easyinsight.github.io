package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
[Event(name="requestData", type="com.easyinsight.analysis.ReportDataEvent")]
public interface IReportControlBar {
    function set analysisDefinition(analysisDefinition:AnalysisDefinition):void;
    function createAnalysisDefinition():AnalysisDefinition;
    function set analysisItems(analysisItems:ArrayCollection):void;
    function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0,
                useWeakReference:Boolean = false):void;
    function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void;
    function isDataValid():Boolean;

    function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void;

    function set dataSourceID(dataSourceID:int):void;

    function onCustomChangeEvent(event:CustomChangeEvent):void;

    function onDataReceipt(event:DataServiceEvent):void;

    function highlight():void;
    function normal():void;
}
}