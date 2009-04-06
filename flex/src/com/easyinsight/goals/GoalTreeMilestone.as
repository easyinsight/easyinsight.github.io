package com.easyinsight.goals
{
    [Bindable]
    [RemoteClass(alias="com.easyinsight.goals.GoalTreeMilestone")]
	public class GoalTreeMilestone
	{
        public var milestoneName:String;
        public var milestoneDate:Date;
        public var milestoneID:int;

		public function GoalTreeMilestone()
		{
		}

	}
}