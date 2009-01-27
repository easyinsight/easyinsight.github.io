package com.easyinsight.framework
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.UserTransferObject")]
	public class UserTransferObject
	{
		public var accountID:int;			
		public var userID:int;
		public var userName:String;
		public var email:String;
		public var name:String;		
		public var licenses:ArrayCollection;
        public var accountAdmin:Boolean;
        public var dataSourceCreator:Boolean;
        public var insightCreator:Boolean;
		
		public function UserTransferObject()
		{
		}

	}
}