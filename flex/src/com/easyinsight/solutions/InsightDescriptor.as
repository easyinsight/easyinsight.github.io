package com.easyinsight.solutions {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.InsightDescriptor")]
public class InsightDescriptor {
    public var insightID:int;
    public var name:String;
    public var dataFeedID:int;

    public function InsightDescriptor() {
        
    }
}
}