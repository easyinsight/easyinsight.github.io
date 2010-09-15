package com.easyinsight.filtering {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.OrderedFilterDefinition")]
public class OrderedFilterDefinition extends FilterDefinition {

    public var filters:ArrayCollection;

    public function OrderedFilterDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.ORDERED;
    }
}
}