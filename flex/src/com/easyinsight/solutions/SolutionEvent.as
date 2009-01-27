package com.easyinsight.solutions
{
	import flash.events.Event;

	public class SolutionEvent extends Event
	{
		public static const SOLUTION_INSTALLED:String = "solutionInstalled";
		public static const SOLUTION_REMOVED:String = "solutionRemoved";
		
		public var solutionID:int;
		
		public function SolutionEvent(type:String, solutionID:int)
		{
			super(type, true);
			this.solutionID = solutionID;
		}
		
		override public function clone():Event {
			return new SolutionEvent(type, solutionID);
		}
	}
}