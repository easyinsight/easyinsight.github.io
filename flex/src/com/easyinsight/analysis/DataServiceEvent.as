package com.easyinsight.analysis {

import flash.events.Event;
import mx.collections.ArrayCollection;

public class DataServiceEvent extends Event {

    public static const DATA_RETURNED:String = "dataReturned";

    public var dataSet:ArrayCollection;
    public var clientProcessorMap:Object;
    public var limitedResults:Boolean;
    public var limitResults:int;
    public var maxResults:int;

    public function DataServiceEvent(type:String, dataSet:ArrayCollection, clientProcessorMap:Object, limitedResults:Boolean = false, limitResults:int = 0, maxResults:int = 0) {
        super(type);
        this.dataSet = dataSet;
        this.clientProcessorMap = clientProcessorMap;
        this.limitedResults = limitedResults;
        this.limitResults = limitResults;
        this.maxResults = maxResults;
    }

    override public function clone():Event {
        return new DataServiceEvent(type, dataSet, clientProcessorMap);
    }
}
}