package com.easyinsight.store
{
	import com.easyinsight.framework.UserTransferObject;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.store.UserMerchantBinding")]
	public class UserMerchantBinding
	{
		public var userMerchantBindingID:Number;
		public var accountID:UserTransferObject;
		public var merchant:Merchant;
		public var bindingType:int;
		
		public function UserMerchantBinding()
		{
		}

	}
}