package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.NullFilter")]
public class NullValueFilterDefinition extends FilterDefinition {
    public function NullValueFilterDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.NULL;
    }
}
}