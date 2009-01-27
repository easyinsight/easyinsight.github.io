package com.easyinsight.goals
{
	public class GoalDirectionCondition extends GoalCondition
	{
		public var highGood:Boolean;
		
		public function GoalDirectionCondition()
		{
			super();
		}
		
		override public function get summary():String {
			return "";
		}
	}
}