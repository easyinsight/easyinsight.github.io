package com.easyinsight.account
{
	import com.easyinsight.framework.UserTransferObject;
	
	import flash.events.Event;

	public class AccountWizardResultEvent extends Event
	{
		public static var ACCOUNT_CREATED:String = "accountCreated";
		public static var ACCOUNT_CANCEL:String = "accountCreationCanceled";
		
		public var account:Account;
		public var user:UserTransferObject;
		
		public function AccountWizardResultEvent(type:String, account:Account = null, user:UserTransferObject = null)
		{
			super(type);
			this.account = account;
			this.user = user;
		}
		
		override public function clone():Event {
			return new AccountWizardResultEvent(type, account);
		}
	}
}