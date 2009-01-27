package com.easyinsight.account
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.AccountTransferObject")]
	public class Account
	{
		public var accountType:AccountType;
		public var accountID:int;
		public var licenses:ArrayCollection;
        public var maxUsers:int;
        public var maxSize:int;
		
		public function Account()
		{
		}

	}
}