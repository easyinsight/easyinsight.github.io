package com.easyinsight.account
{
	import com.easyinsight.framework.LoginEvent;
	import com.easyinsight.framework.User;
	import com.easyinsight.listing.IPerspective;
	import com.easyinsight.listing.ListingOption;

	public class AccountsListingOption extends ListingOption
	{
		public function AccountsListingOption(displayName:String, enableAnalysis:Boolean, iconClass:Class, perspective:IPerspective)
		{
			super(displayName, enableAnalysis, iconClass, perspective);
			User.getEventNotifier().addEventListener(LoginEvent.LOGIN, onLogin);
		}
		
		private function onLogin(event:LoginEvent):void {
			//clearChildren();
            //var myProfileOption:ListingOption = new ListingOption("My Profile", false, null, new UserProfileEditor());
            //addChild(myProfileOption);
		}
	}
}