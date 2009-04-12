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
        public static const ADMINISTRATOR:int = 5;

        public static const INACTIVE:int = 1;
        public static const ACTIVE:int = 2;
        public static const DELINQUENT:int = 3;
        public static const SUSPENDED:int = 4;
        public static const CLOSED:int = 5;
        public static const PENDING_BILLING:int = 6;
        public static const PREPARING:int = 7;
        public static const BETA:int = 8;

		public var accountType:int;
		public var accountID:int;
		public var licenses:ArrayCollection;
        public var maxUsers:int;
        public var maxSize:int;
        public var name:String;
        public var accountState:int;
        public var uncheckedAPIEnabled:Boolean;
        public var validatedAPIEnabled:Boolean;
        public var uncheckedAPIAllowed:Boolean;
        public var validatedAPIAllowed:Boolean;
        public var dynamicAPIAllowed:Boolean;
        public var basicAuthAllowed:Boolean;
		
		public function Account()
		{
		}

	}
}