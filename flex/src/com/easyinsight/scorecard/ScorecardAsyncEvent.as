package com.easyinsight.scorecard {
import com.easyinsight.outboundnotifications.OutboundEvent;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardRefreshEvent")]
public class ScorecardAsyncEvent extends OutboundEvent {

    public static const NAME_UPDATE:int = 1;
    public static const DONE:int = 2;

    public var kpis:ArrayCollection;
    public var scorecardID:int;

    public var dataSourceName:String;

    public var type:int;
    
    public function ScorecardAsyncEvent() {
        super();
    }
}
}