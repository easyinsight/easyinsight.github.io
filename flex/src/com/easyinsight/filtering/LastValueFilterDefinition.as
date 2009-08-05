package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.LastValueFilter")]
public class LastValueFilterDefinition extends FilterDefinition{
    public function LastValueFilterDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.LAST_VALUE;
    }
}
}