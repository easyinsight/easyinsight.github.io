package com.easyinsight.account
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.CommercialAccount")]
	public class CommercialAccountType extends AccountType
	{
		public function CommercialAccountType()
		{
		}

		override public function isPrivateSharingAllowed():Boolean { 
			return true;
		};
		
	    override public function getSpaceAllowed():int {
	    	return 500000000;
		}
		
	    override public function getAccountType():int {
	    	return AccountType.COMMERCIAL;
	    }
	}
}