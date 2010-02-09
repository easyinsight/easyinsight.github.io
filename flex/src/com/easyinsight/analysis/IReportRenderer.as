package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterRawData;
import mx.collections.ArrayCollection;
public interface IReportRenderer {
    function renderReport(dataSet:ArrayCollection, analysisDefinition:AnalysisDefinition, clientProcessorMap:Object, additionalProperties:Object):void;
    function createFilterRawData():FilterRawData;
    function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0,
                useWeakReference:Boolean = false):void;
    function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void;
    function updateExportMetadata():void;
    function onCustomChangeEvent(event:CustomChangeEvent):void;
    function preserveValues():Boolean;
}
}