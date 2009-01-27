package com.easyinsight.account.license
{
	import mx.controls.TextInput;

	public class UserAccountEditor extends TextInput
	{
		private var license:AdminSubscription;
		private var validator:LicenseValidator;		
		
		public function UserAccountEditor(userMap:Object)
		{
			super();
			validator = new LicenseValidator(userMap);
			validator.source = this;
			validator.property = "text";
			validator.required = false;						
		}
		
		override public function set data(value:Object):void {
			this.license = value as AdminSubscription;
			this.text = this.license.userName;
			this.license.licenseValidator = validator;				
		}
		
		override public function get data():Object {
			return this.license;
		}
	}
}