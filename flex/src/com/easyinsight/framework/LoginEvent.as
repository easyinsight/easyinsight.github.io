package com.easyinsight.framework
{
	import flash.events.Event;
	
	public class LoginEvent extends Event
	{
		public static const LOGIN:String = "loginEvent2";
		public static const LOGOUT:String = "logout";
		public static const QUIET_LOGIN:String = "quietLogin";

        public var targetURL:String;
        public var newAccount:Boolean;

        public var authResponse:UserServiceResponse;
		
		public function LoginEvent(type:String, authResponse:UserServiceResponse = null) {
			super(type);
            this.authResponse = authResponse;
		}
		
		override public function clone():Event {
            return new LoginEvent(type, authResponse);
        }
	}
}