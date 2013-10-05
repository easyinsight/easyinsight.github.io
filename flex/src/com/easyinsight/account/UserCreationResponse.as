package com.easyinsight.account
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.UserCreationResponse")]
	public class UserCreationResponse
	{
		public var successful:Boolean;
		public var userID:int;
		public var failureMessage:String;
        public var token:String;
        public var url:String;
		
		public function UserCreationResponse()
		{
		}

	}
}