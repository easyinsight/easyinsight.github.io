package com.easyinsight.account.license
{
	import com.easyinsight.account.SubscriptionLicense;
	
	public class AdminSubscription
	{
		private var _licenseID:int;
		private var _userName:String;
		private var _feedID:int;
		private var _licenseValidator:LicenseValidator;
		
		public function AdminSubscription(subscriptionLicense:SubscriptionLicense)
		{
			this._licenseID = subscriptionLicense.subscriptionLicenseID;
			this._feedID = subscriptionLicense.feedID;
			if (subscriptionLicense.user != null) {
				this._userName = subscriptionLicense.user.userName;
			} 
		}

		public function set licenseValidator(licenseValidator:LicenseValidator):void {
			this._licenseValidator = licenseValidator;
		}
		
		public function get licenseID():int {
			return _licenseID;
		}
		
		public function get feedID():int {
			return _feedID;
		}
		
		public function validate():void {
			this._licenseValidator.validate();
		}
		
		[Bindable]
		public function get userName():String {
			return _userName;
		}
		
		public function set userName(userName:String):void {
			this._userName = userName;			
		}
		
		public function toSubscriptionLicense(userMap:Object):SubscriptionLicense {
			var license:SubscriptionLicense = new SubscriptionLicense();
			license.subscriptionLicenseID = _licenseID;
			if (_userName.length > 0) {
				license.user = userMap[_userName];
			}
			license.feedID = _feedID;
			return license;
		}
	}
}