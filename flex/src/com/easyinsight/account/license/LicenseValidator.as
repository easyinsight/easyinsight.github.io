package com.easyinsight.account.license
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	public class LicenseValidator extends Validator
	{
		private var userMap:Object = new Object();
		
		public function LicenseValidator(userMap:Object) {
			this.userMap = userMap;
		}


		override protected function doValidation(value:Object):Array {
			var results:Array = new Array();
			results = super.doValidation(value);
			if (results.length > 0) {
				return results;
			}
			
			var userName:String = value as String;
			if (userName.length > 0) {
				if (userMap[userName] == null) {
					results.push(new ValidationResult(true, null, "noSuchUser", "You must specify a valid user name."));
				}
			}
			return results;
		}		
	}
}