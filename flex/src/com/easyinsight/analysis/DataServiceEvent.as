package com.easyinsight.analysis {

import com.easyinsight.datasources.DataSourceInfo;

import flash.events.Event;
import mx.collections.ArrayCollection;

public class DataServiceEvent extends Event {

    public static const DATA_RETURNED:String = "dataReturned";

    public var dataSet:ArrayCollection;
    public var limitedResults:Boolean;
    public var limitResults:int;
    public var maxResults:int;
    public var dataSource:DataSourceInfo;
    public var additionalProperties:Object;
    public var reportAudit:ArrayCollection;
    public var reportFault:ReportFault;
    public var suggestions:ArrayCollection;
    public var hasData:Boolean = false;

    public function DataServiceEvent(type:String, dataSet:ArrayCollection, dataSource:DataSourceInfo,
                                     additionalProperties:Object, reportAudit:ArrayCollection, reportFault:ReportFault,
                                     limitedResults:Boolean = false, limitResults:int = 0, maxResults:int = 0, suggestions:ArrayCollection = null,
            hasData:Boolean = false) {
        super(type);
        this.dataSet = dataSet;
        this.dataSource = dataSource;
        this.limitedResults = limitedResults;
        this.limitResults = limitResults;
        this.maxResults = maxResults;
        this.additionalProperties = additionalProperties;
        this.reportAudit = reportAudit;
        this.reportFault = reportFault;
        this.suggestions = suggestions;
        this.hasData = hasData;
    }

    override public function clone():Event {
        return new DataServiceEvent(type, dataSet, dataSource, additionalProperties, reportAudit, reportFault,
                limitedResults, limitResults, maxResults, suggestions, hasData);
    }
}
}