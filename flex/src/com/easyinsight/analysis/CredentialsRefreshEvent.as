package com.easyinsight.analysis
{
	import flash.events.Event;

	public class CredentialsRefreshEvent extends Event
	{
		public static const CREDENTIALS_REFRESH:String = "credentialsRefresh";
		
		public function CredentialsRefreshEvent()
		{
			super(CREDENTIALS_REFRESH);
		}
		
		override public function clone():Event {
			return new CredentialsRefreshEvent();
		} 
	}
}