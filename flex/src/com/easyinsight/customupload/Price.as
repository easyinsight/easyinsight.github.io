package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.stores.Price")]
	public class Price
	{
		public var cost:Number;
		public var priceID:int;
		public function Price()
		{
		}

	}
}