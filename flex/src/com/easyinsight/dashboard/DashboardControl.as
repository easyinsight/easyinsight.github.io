package com.easyinsight.dashboard {

public class DashboardControl {

    public var name:String;
    public var componentClass:Class;
    public var icon:Class;

    public function DashboardControl(name:String, componentClass:Class) {
        super();
        this.name = name;
        this.componentClass = componentClass;
    }


    public function createElement():DashboardElement {
        return new componentClass();
    }
}
}