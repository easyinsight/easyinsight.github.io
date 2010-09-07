package com.easyinsight.etl {
import com.easyinsight.analysis.Value;

[Bindable]
[RemoteClass(alias="com.easyinsight.etl.LookupPair")]
public class LookupPair {

    public var sourceValue:Value;
    public var targetValue:Value;
    public var lookupPairID:int;

    public function LookupPair() {
    }
}
}