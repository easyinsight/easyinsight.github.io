package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.LastValueFilter")]
public class LastValueFilterDefinition extends FilterDefinition{

    public var absolute:Boolean = true;
    public var threshold:int = 1;

    public function LastValueFilterDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.LAST_VALUE;
    }
}
}