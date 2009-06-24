package com.easyinsight.framework
{
	import flash.events.Event;
	
	public class LoginEvent extends Event
	{
		public static const LOGIN:String = "loginEvent2";
		public static const LOGOUT:String = "logout";

        public var targetURL:String;
		
		public function LoginEvent(type:String, targetURL:String = null) {
			super(type, true);
            this.targetURL = targetURL;
		}
		
		override public function clone():Event {
            return new LoginEvent(type, targetURL);
        }
	}
}