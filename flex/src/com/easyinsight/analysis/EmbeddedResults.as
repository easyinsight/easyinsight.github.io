package com.easyinsight.analysis {
import com.easyinsight.datasources.DataSourceInfo;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.EmbeddedResults")]
public class EmbeddedResults {
    
    public var definition:AnalysisDefinition;
    public var dataSourceAccessible:Boolean;
    public var dataSourceInfo:DataSourceInfo;
    public var reportFault:ReportFault;
    public var additionalProperties:Object;

    public function EmbeddedResults() {
    }
}
}