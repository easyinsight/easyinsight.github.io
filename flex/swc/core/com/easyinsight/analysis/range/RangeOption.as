package com.easyinsight.analysis.range {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.RangeOption")]
public class RangeOption {

    public var rangeOptionID:int;
    public var rangeMinimum:Number = 0;
    public var rangeMaximum:Number = 0;
    public var displayName:String;

    public function RangeOption() {
    }
}
}