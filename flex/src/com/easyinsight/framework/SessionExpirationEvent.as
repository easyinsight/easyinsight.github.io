package com.easyinsight.framework
{
	import flash.events.Event;

	public class SessionExpirationEvent extends Event
	{
		public static const SESSION_EXPIRATION:String = "sessionExpiration";
		
		public function SessionExpirationEvent()
		{
			super(SESSION_EXPIRATION, true);
		}
		
		override public function clone():Event {
			return new SessionExpirationEvent();
		}
	}
}