package com.easyinsight.goals {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.goals.GoalTreeDescriptor")]
public class GoalTreeDescriptor extends EIDescriptor {
    
    public var role:int;
    public var iconName:String;

    public function GoalTreeDescriptor() {
        
    }

    override public function getType():int {
        return EIDescriptor.GOAL_TREE;
    }

    public function get size():int {
        return 0;
    }
}
}