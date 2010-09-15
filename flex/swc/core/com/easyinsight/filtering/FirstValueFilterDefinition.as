package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FirstValueFilter")]
public class FirstValueFilterDefinition extends FilterDefinition {
    public function FirstValueFilterDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.FIRST_VALUE;
    }
}
}