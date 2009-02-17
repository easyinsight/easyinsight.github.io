package com.easyinsight.account
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.AccountTransferObject")]
	public class Account
	{
        public static const FREE:int = 1;
        public static const INDIVIDUAL:int = 2;
        public static const PROFESSIONAL:int = 3;
        public static const ENTERPRISE:int = 4;

		public var accountType:int;
		public var accountID:int;
		public var licenses:ArrayCollection;
        public var maxUsers:int;
        public var maxSize:int;
        public var name:String;
        public var accountState:int;
		
		public function Account()
		{
		}

	}
}