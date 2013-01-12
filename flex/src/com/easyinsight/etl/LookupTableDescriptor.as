package com.easyinsight.etl {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.etl.LookupTableDescriptor")]
public class LookupTableDescriptor extends EIDescriptor {
    public function LookupTableDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.LOOKUP_TABLE;
    }

    override public function get typeString():String {
        return "Lookup Table";
    }
}
}