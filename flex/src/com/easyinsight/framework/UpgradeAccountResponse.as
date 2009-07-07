package com.easyinsight.framework
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.UpgradeAccountResponse")]
	public class UpgradeAccountResponse
	{
		public var billingInformationNeeded:Boolean;
		public var user:UserTransferObject;
		
		public function UpgradeAccountResponse()
		{
		}

	}
}