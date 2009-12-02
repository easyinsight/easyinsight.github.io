package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.EmbeddedTimelineResults")]
public class EmbeddedTimelineResults extends EmbeddedResults {
    public var listDatas:ArrayCollection;
    public var seriesValues:ArrayCollection;
    public function EmbeddedTimelineResults() {
        super();
    }
}
}