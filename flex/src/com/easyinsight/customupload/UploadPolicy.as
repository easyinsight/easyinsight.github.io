package com.easyinsight.customupload
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.UploadPolicy")]
	public class UploadPolicy
	{
		public static const PUBLIC:int = 1;
		public static const PRIVATE:int = 2;
		public static const COMMERCIAL:int = 3;
		public static const GROUP:int = 5;
		
		public var publiclyVisible:Boolean;
		public var marketplaceVisible:Boolean;
		public var owners:ArrayCollection;
		public var viewers:ArrayCollection;
		
		public function UploadPolicy()
		{
		}

		public function getPolicyType():int {
			return 0;
		}
		
		public function getCost():Number {
			return 0;
		}
	}
}