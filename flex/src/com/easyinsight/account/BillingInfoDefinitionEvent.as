package com.easyinsight.account
{
	import flash.events.Event;

	public class BillingInfoDefinitionEvent extends Event
	{
		public static var BILLING_INFO_DEFINED:String = "billingInfoDefined";
		public var billingInfo:BillingInfo;
		
		public function BillingInfoDefinitionEvent(billingInfo:BillingInfo)
		{
			super(BILLING_INFO_DEFINED);
		}
		
		override public function clone():Event {
			return new BillingInfoDefinitionEvent(billingInfo);
		}
	}
}