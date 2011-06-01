package com.easyinsight.framework
{		
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.UserTransferObject")]
	public class UserTransferObject
	{
		public var accountID:int;			
		public var userID:int;
		public var userName:String;
		public var email:String;
		public var name:String;
        public var accountAdmin:Boolean;
        public var firstName:String;
        public var title:String;
        public var personaID:int;
        public var optInEmail:Boolean;
        public var fixedDashboardID:int;
		
		public function UserTransferObject()
		{
		}

        public function get fullName():String {
            return firstName + " " + name;
        }
	}
}