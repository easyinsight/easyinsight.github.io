package com.easyinsight.solutions {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.core.DataSourceDescriptor")]
public class DataSourceDescriptor extends EIDescriptor{

    public var description:String;
    public var dataSourceType:int;
    public var size:int;
    public var lastDataTime:Date;
    public var groupSourceID:int;
    public var children:ArrayCollection;

    public function DataSourceDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.DATA_SOURCE;
    }
}
}