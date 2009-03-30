package com.easyinsight.solutions {
import com.easyinsight.quicksearch.EIDescriptor;
[Bindable]
[RemoteClass(alias="com.easyinsight.core.DataSourceDescriptor")]
public class DataSourceDescriptor extends EIDescriptor{
    public function DataSourceDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.DATA_SOURCE;
    }
}
}