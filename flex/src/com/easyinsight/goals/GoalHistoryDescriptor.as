package com.easyinsight.goals {
import com.easyinsight.quicksearch.EIDescriptor;

public class GoalHistoryDescriptor extends EIDescriptor{

    public var dataSourceID:int;
    public var dataSourceName:String;

    public function GoalHistoryDescriptor(dataSourceID:int, dataSourceName:String) {
        super();
        name = "Goal History";
        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
    }

    override public function getType():int {
        return EIDescriptor.GOAL_HISTORY;
    }
}
}