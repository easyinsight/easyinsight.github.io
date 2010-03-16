package com.easyinsight.scorecard {
import com.easyinsight.outboundnotifications.OutboundEvent;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.DataSourceRefreshEvent")]
public class DataSourceAsyncEvent extends OutboundEvent {

    public static const NAME_UPDATE:int = 1;
    public static const DONE:int = 2;
    
    public var dataSourceID:int;

    public var dataSourceName:String;

    public var type:int;
    
    public function DataSourceAsyncEvent() {
        super();
    }
}
}