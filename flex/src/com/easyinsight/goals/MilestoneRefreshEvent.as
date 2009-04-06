package com.easyinsight.goals {
import flash.events.Event;
public class MilestoneRefreshEvent extends Event{

    public static const MILESTONE_REFRESH:String = "milestoneRefresh";
    
    public function MilestoneRefreshEvent() {
        super(MILESTONE_REFRESH);
    }

    override public function clone():Event {
        return new MilestoneRefreshEvent();
    }
}
}