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
        public var startType:int;

        public var authResponse:UserServiceResponse;
		
		public function LoginEvent(type:String, authResponse:UserServiceResponse = null, startType:int = 0) {
			super(type);
            this.authResponse = authResponse;
            this.startType = startType;
		}
		
		override public function clone():Event {
            return new LoginEvent(type, authResponse, startType);
        }
	}
}