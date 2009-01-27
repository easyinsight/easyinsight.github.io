package com.easyinsight.account
{
	import com.easyinsight.framework.UserTransferObject;
	
	import flash.events.Event;

	public class UserDefinitionEvent extends Event
	{
		public var USER_DEFINED:String = "userDefined";
		public var user:UserTransferObject;
        public var password:String;
		
		public function UserDefinitionEvent(user:UserTransferObject, password:String)
		{
			super(USER_DEFINED);
			this.user = user;
            this.password = password;
		}
		
		override public function clone():Event {
			return new UserDefinitionEvent(user, password);
		}
	}
}