package com.easyinsight.goals
{
	public class GoalToNumberCondition extends GoalCondition
	{
		public var target:Number;
		
		public function GoalToNumberCondition()
		{
			super();
		}
		
		override public function get summary():String {
			return "";
		}
	}
}