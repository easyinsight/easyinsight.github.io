package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Event(name="returnData", type="com.easyinsight.analysis.DataServiceEvent")]
public interface IReportDataService {
    function retrieveData(definition:AnalysisDefinition, refreshAllSources:Boolean):void;
    function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0,
                useWeakReference:Boolean = false):void;
    function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void;
}
}