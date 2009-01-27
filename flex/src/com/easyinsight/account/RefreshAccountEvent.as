package com.easyinsight.account
{
	import flash.events.Event;

	public class RefreshAccountEvent extends Event
	{
		public static const REFRESH_ACCOUNT:String = "refreshAccount";
		
		public function RefreshAccountEvent()
		{
			super(REFRESH_ACCOUNT, true);
		}
		
		override public function clone():Event {
			return new RefreshAccountEvent();
		}
	}
}