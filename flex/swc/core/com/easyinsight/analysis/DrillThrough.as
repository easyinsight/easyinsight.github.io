package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DrillThrough")]
public class DrillThrough extends Link{

    public var reportID:int;
    public var dashboardID:int;
    public var miniWindow:Boolean;
    public var marmotScript:String;
    public var addAllFilters:Boolean;
    public var showDrillThroughFilters:Boolean;
    public var filterRowGroupings:Boolean;

    public function DrillThrough() {
        super();
    }

    override public function get type():String {
        return "Drillthrough";
    }

    override public function createString(name:String = null):String {
        if (name != null) {
            return "drillthrough to " + name;
        }
        return "drillthrough";
    }
}
}