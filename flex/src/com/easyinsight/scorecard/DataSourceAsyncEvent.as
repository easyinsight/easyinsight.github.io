package com.easyinsight.scorecard {
import com.easyinsight.outboundnotifications.OutboundEvent;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.DataSourceRefreshEvent")]
public class DataSourceAsyncEvent extends OutboundEvent {

    public static const NAME_UPDATE:int = 1;
    public static const DONE:int = 2;
    public static const PROGRESS:int = 3;
    public static const BLOCKED:int = 4;

    public var dataSourceID:int;

    public var dataSourceName:String;

    public var current:int;

    public var max:int;

    public var type:int;

    public var timestamp:Date;

    public var async:Boolean;
    
    public function DataSourceAsyncEvent() {
        super();
    }
}
}