package com.easyinsight.etl {
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.etl.LookupTable")]
public class LookupTable {

    public var name:String;
    public var dataSourceID:int;
    public var urlKey:String;
    public var sourceField:AnalysisItem;
    public var targetField:AnalysisItem;
    public var lookupTableID:int;
    public var lookupPairs:ArrayCollection = new ArrayCollection();

    public function LookupTable() {
    }
}
}