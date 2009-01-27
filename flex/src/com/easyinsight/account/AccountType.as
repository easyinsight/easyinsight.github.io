package com.easyinsight.account
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.AccountType")]
	public class AccountType
	{
		public static const FREE:int = 1;
		public static const INDIVIDUAL:int = 2;
		public static const COMMERCIAL:int = 3;
		
		public function AccountType()
		{
		}

		public function isPrivateSharingAllowed():Boolean { 
			return false;
		};
		
	    public function getSpaceAllowed():int {
	    	return 0;
		}
		
	    public function getAccountType():int {
	    	return 0;
	    }
	}
}