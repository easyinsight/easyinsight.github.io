package com.easyinsight.analysis {

import flash.events.Event;
import mx.collections.ArrayCollection;

public class DataServiceEvent extends Event {

    public static const DATA_RETURNED:String = "dataReturned";

    public var dataSet:ArrayCollection;
    public var clientProcessorMap:Object;

    public function DataServiceEvent(type:String, dataSet:ArrayCollection, clientProcessorMap:Object) {
        super(type);
        this.dataSet = dataSet;
        this.clientProcessorMap = clientProcessorMap;
    }

    override public function clone():Event {
        return new DataServiceEvent(type, dataSet, clientProcessorMap);
    }
}
}