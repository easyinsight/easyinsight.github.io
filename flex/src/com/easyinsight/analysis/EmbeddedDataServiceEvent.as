package com.easyinsight.analysis {

import com.easyinsight.datasources.DataSourceInfo;

import flash.events.Event;
import mx.collections.ArrayCollection;

public class EmbeddedDataServiceEvent extends Event {

    public static const DATA_RETURNED:String = "embeddedDataReturned";

    public var dataSet:ArrayCollection;
    public var analysisDefinition:AnalysisDefinition;
    public var dataSourceAccessible:Boolean;
    public var reportFault:ReportFault;
    public var reportAuthor:String;
    public var dataSourceInfo:DataSourceInfo;
    public var additionalProperties:Object;

    public function EmbeddedDataServiceEvent(type:String, dataSet:ArrayCollection, analysisDefinition:AnalysisDefinition, dataSourceAccessible:Boolean,
            reportFault:ReportFault, dataSourceInfo:DataSourceInfo, additionalProperties:Object) {
        super(type);
        this.dataSet = dataSet;
        this.analysisDefinition = analysisDefinition;
        this.dataSourceAccessible = dataSourceAccessible;
        this.reportFault = reportFault;
        this.dataSourceInfo = dataSourceInfo;
        this.additionalProperties = additionalProperties;
    }

    override public function clone():Event {
        return new EmbeddedDataServiceEvent(type, dataSet, analysisDefinition, dataSourceAccessible,
                reportFault, dataSourceInfo, additionalProperties);
    }
}
}