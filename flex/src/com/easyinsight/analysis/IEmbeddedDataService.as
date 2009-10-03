package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Event(name="returnData", type="com.easyinsight.analysis.DataServiceEvent")]
public interface IEmbeddedDataService {
    function retrieveData(reportID:int, dataSourceID:int, filters:ArrayCollection, refreshAll:Boolean, drillthroughFilters:ArrayCollection):void;
    function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0,
                useWeakReference:Boolean = false):void;
}
}