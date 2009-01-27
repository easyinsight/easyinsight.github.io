package com.easyinsight.account
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.FreeAccount")]
	public class FreeAccountType extends AccountType
	{
		public function FreeAccountType()
		{
		}

		override public function isPrivateSharingAllowed():Boolean { 
			return false;
		};
		
	    override public function getSpaceAllowed():int {
	    	return 1000000;
		}
		
	    override public function getAccountType():int {
	    	return AccountType.FREE;
	    }
	}
}