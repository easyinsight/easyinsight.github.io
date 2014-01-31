package com.easyinsight.solutions {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.core.InsightDescriptor")]
public class InsightDescriptor extends EIDescriptor {
    public var dataFeedID:int;
    public var reportType:int;
    public var tags:ArrayCollection;
    public var configs:ArrayCollection;
    public var dataSourceReport:Boolean;

    public var size:int = 0;
    public var lastDataTime:Date = null;

    public function InsightDescriptor() {
        
    }

    override public function getType():int {
        return EIDescriptor.REPORT;
    }

    override public function get typeString():String {
        return "Report";
    }
}
}