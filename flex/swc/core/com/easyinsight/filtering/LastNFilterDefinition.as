package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.LastNFilterDefinition")]
public class LastNFilterDefinition extends FilterDefinition{

    public var limit:int;

    public function LastNFilterDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.LAST_N;
    }
}
}