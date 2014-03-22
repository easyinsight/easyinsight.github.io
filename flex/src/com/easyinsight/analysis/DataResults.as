package com.easyinsight.analysis {
import com.easyinsight.datasources.DataSourceInfo;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DataResults")]
public class DataResults {
    public var credentialRequirements:ArrayCollection;
    public var invalidAnalysisItemIDs:ArrayCollection;
    public var feedMetadata:FeedMetadata;
    public var reportFault:ReportFault;
    public var dataSourceInfo:DataSourceInfo;
    public var additionalProperties:Object;
    public var auditMessages:ArrayCollection;
    public var suggestions:ArrayCollection;
    public var uid:String;
    public var report:AnalysisDefinition;
    public var fieldEvents:Object;
    public var filterEvents:Object;

    public var databaseTime:int;
    public var processingTime:int;

    public function DataResults() {
    }
}
}