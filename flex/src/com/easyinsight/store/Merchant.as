package com.easyinsight.store
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.stores.Merchant")]
	public class Merchant
	{
		public var merchantID:Number;
		public var name:String;
		public var users:ArrayCollection;
		
		public function Merchant()
		{
		}

	}
}