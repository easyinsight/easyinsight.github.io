package com.easyinsight.account
{
	import flash.events.Event;

	public class AccountCreationConfirmationEvent extends Event
	{
		public static var ACCOUNT_CREATION_CONFIRMED:String = "accountCreationConfirmed";
		
		public function AccountCreationConfirmationEvent()
		{
			super(ACCOUNT_CREATION_CONFIRMED);
		}
		
		override public function clone():Event {
			return new AccountCreationConfirmationEvent();
		}
	}
}