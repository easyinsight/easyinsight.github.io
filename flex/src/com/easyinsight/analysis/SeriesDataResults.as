package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.SeriesDataResults")]
public class SeriesDataResults extends DataResults {
    
    public var listDatas:ArrayCollection;
    public var seriesValues:ArrayCollection;

    public function SeriesDataResults() {
        super();
    }
}
}