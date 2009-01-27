package com.easyinsight.store
{
	import flash.events.Event;

	public class CreatedMerchantEvent extends Event
	{
		public static const CREATED_MERCHANT:String = "createdMerchant";
		
		public var merchant:Merchant;
		
		public function CreatedMerchantEvent(merchant:Merchant)
		{
			super(CREATED_MERCHANT);
			this.merchant = merchant;
		}
		
		override public function clone():Event {
			return new CreatedMerchantEvent(merchant);
		}
	}
}