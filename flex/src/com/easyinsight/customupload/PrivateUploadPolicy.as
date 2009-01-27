package com.easyinsight.customupload
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.PrivateUploadPolicy")]
	public class PrivateUploadPolicy extends UploadPolicy
	{
		public var userFeedLinks:ArrayCollection;
		
		public function PrivateUploadPolicy()
		{
		}

		override public function getPolicyType():int {
			return UploadPolicy.PRIVATE;
		}
	}
}