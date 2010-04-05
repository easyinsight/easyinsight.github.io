package com.easyinsight.goals {
import com.easyinsight.outboundnotifications.OutboundEvent;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.goals.KPITreeRefreshEvent")]
public class KPITreeAsyncEvent extends OutboundEvent {

    public static const NAME_UPDATE:int = 1;
    public static const DONE:int = 2;

    public var kpis:ArrayCollection;
    public var kpiTreeID:int;

    public var dataSourceName:String;

    public var type:int;
    
    public function KPITreeAsyncEvent() {
        super();
    }
}
}