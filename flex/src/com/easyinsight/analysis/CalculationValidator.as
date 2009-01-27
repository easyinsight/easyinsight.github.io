package com.easyinsight.analysis
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;

	public class CalculationValidator extends Validator
	{
		private var _failureMessage:String;
		
		public function CalculationValidator()
		{
			super();
		}
		
		public function get failureMessage():String {
			return _failureMessage;
		}
		
		public function set failureMessage(failureMessage:String):void {
			_failureMessage = failureMessage;
		}
		
		override protected function doValidation(value:Object):Array {
			var results:Array = [];
			results = super.doValidation(value);
			if (results.length > 0) {
				return results;
			}
			if (failureMessage != null && failureMessage.length > 0) {
				results.push(new ValidationResult(true, null, "invalidCalc", failureMessage));
			}
			return results;
		}
	}
}