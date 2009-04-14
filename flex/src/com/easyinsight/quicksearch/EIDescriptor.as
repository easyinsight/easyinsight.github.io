package com.easyinsight.quicksearch {
[Bindable]
[RemoteClass(alias="com.easyinsight.core.EIDescriptor")]
public class EIDescriptor {

    public static const DATA_SOURCE:int = 1;
    public static const REPORT:int = 2;

    public var id:int;
    public var name:String;
    public var icon:Class;
    
    public function EIDescriptor() {
    }

    public function getType():int {
        return 0;
    }
}
}