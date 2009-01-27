package com.easyinsight.account
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.IndividualAccount")]
	public class IndividualAccountType extends AccountType
	{
		public function IndividualAccountType()
		{
		}

		override public function isPrivateSharingAllowed():Boolean { 
			return false;
		};
		
	    override public function getSpaceAllowed():int {
	    	return 50000000;
		}
		
	    override public function getAccountType():int {
	    	return AccountType.INDIVIDUAL;
	    }
	}
}