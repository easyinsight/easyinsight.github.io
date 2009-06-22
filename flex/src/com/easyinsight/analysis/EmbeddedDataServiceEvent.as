package com.easyinsight.analysis {

import flash.events.Event;
import mx.collections.ArrayCollection;

public class EmbeddedDataServiceEvent extends Event {

    public static const DATA_RETURNED:String = "embeddedDataReturned";

    public var dataSet:ArrayCollection;
    public var clientProcessorMap:Object;
    public var analysisDefinition:AnalysisDefinition;
    public var dataSourceAccessible:Boolean;
    public var lastDataTime:Date;
    public var attribution:String;

    public function EmbeddedDataServiceEvent(type:String, dataSet:ArrayCollection, analysisDefinition:AnalysisDefinition, clientProcessorMap:Object, dataSourceAccessible:Boolean,
            lastDataTime:Date, attribution:String) {
        super(type);
        this.dataSet = dataSet;
        this.clientProcessorMap = clientProcessorMap;
        this.analysisDefinition = analysisDefinition;
        this.dataSourceAccessible = dataSourceAccessible;
        this.lastDataTime = lastDataTime;
        this.attribution = attribution;
    }

    override public function clone():Event {
        return new EmbeddedDataServiceEvent(type, dataSet, analysisDefinition, clientProcessorMap, dataSourceAccessible, lastDataTime, attribution);
    }
}
}