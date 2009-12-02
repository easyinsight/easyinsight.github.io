package com.easyinsight.analysis {
import com.easyinsight.datasources.DataSourceInfo;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DataResults")]
public class DataResults {
    public var credentialRequirements:ArrayCollection;
    public var invalidAnalysisItemIDs:ArrayCollection;
    public var feedMetadata:FeedMetadata;
    public var dataSourceInfo:DataSourceInfo;
    public var additionalProperties:Object;

    public function DataResults() {
    }
}
}