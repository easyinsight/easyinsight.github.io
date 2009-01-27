package com.easyinsight.account
{
	import flash.events.Event;

	public class PreviousPageEvent extends Event
	{
		public var PREVIOUS_PAGE:String = "previousPage";
		
		public function PreviousPageEvent()
		{
			super(PREVIOUS_PAGE);
		}
		
	}
}