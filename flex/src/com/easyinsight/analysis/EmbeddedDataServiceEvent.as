package com.easyinsight.analysis {

import com.easyinsight.datasources.DataSourceInfo;

import flash.events.Event;
import mx.collections.ArrayCollection;

public class EmbeddedDataServiceEvent extends Event {

    public static const DATA_RETURNED:String = "embeddedDataReturned";

    public var dataSet:ArrayCollection;
    public var clientProcessorMap:Object;
    public var analysisDefinition:AnalysisDefinition;
    public var dataSourceAccessible:Boolean;
    public var attribution:String;
    public var credentialRequirements:ArrayCollection;
    public var ratingsAverage:Number;
    public var ratingsCount:int;
    public var dataSourceInfo:DataSourceInfo;

    public function EmbeddedDataServiceEvent(type:String, dataSet:ArrayCollection, analysisDefinition:AnalysisDefinition, clientProcessorMap:Object, dataSourceAccessible:Boolean,
            attribution:String, credentialRequirements:ArrayCollection, dataSourceInfo:DataSourceInfo, ratingsAverage:Number,
                ratingsCount:int) {
        super(type);
        this.dataSet = dataSet;
        this.clientProcessorMap = clientProcessorMap;
        this.analysisDefinition = analysisDefinition;
        this.dataSourceAccessible = dataSourceAccessible;
        this.attribution = attribution;
        this.credentialRequirements = credentialRequirements;
        this.dataSourceInfo = dataSourceInfo;
        this.ratingsAverage = ratingsAverage;
        this.ratingsCount = ratingsCount;
    }

    override public function clone():Event {
        return new EmbeddedDataServiceEvent(type, dataSet, analysisDefinition, clientProcessorMap, dataSourceAccessible, attribution,
                credentialRequirements, dataSourceInfo, ratingsAverage, ratingsCount);
    }
}
}