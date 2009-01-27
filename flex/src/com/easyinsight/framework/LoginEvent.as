package com.easyinsight.framework
{
	import flash.events.Event;
	
	public class LoginEvent extends Event
	{
		public static const LOGIN:String = "loginEvent2";
		public static const LOGOUT:String = "logout";
		
		public function LoginEvent(type:String) {
			super(type, true);
		}
		
		override public function clone():Event {
            return new LoginEvent(type);
        }
	}
}