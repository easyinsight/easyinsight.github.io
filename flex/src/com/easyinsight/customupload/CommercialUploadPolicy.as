package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.CommercialUploadPolicy")]
	public class CommercialUploadPolicy extends UploadPolicy
	{
		public var price:Price;
		public var merchantID:int;
		
		public function CommercialUploadPolicy()
		{
			super();
		}
		
		override public function getPolicyType():int {
			return UploadPolicy.COMMERCIAL;
		}
		
		override public function getCost():Number {
			if (price == null) {
				return 0;
			} else {
				return price.cost;
			}
		}
	}
}