package com.easyinsight.account
{
	import com.easyinsight.framework.UserTransferObject;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.SubscriptionLicense")]
	public class SubscriptionLicense
	{
		public var subscriptionLicenseID:int;
		public var feedID:int;
		public var user:UserTransferObject;
		public var feedName:String; 
		
		public function SubscriptionLicense()
		{
		}

	}
}