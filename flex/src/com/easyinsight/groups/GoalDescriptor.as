package com.easyinsight.groups {
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.GoalDescriptor")]
public class GoalDescriptor {

    public var name:String;
    public var id:int;

    public function GoalDescriptor() {
    }
}
}