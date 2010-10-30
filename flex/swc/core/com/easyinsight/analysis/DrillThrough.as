package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DrillThrough")]
public class DrillThrough extends Link{
    public var reportID:int;
    public var miniWindow:Boolean;
    public function DrillThrough() {
        super();
    }

    override public function get type():String {
        return "Drillthrough";
    }
}
}