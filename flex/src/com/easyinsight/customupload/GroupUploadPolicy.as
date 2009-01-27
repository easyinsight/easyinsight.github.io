package com.easyinsight.customupload
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.GroupUploadPolicy")]
	public class GroupUploadPolicy extends UploadPolicy
	{
		public var groupDescriptors:ArrayCollection;
		
		public function GroupUploadPolicy()
		{
			super();
		}
		
		override public function getPolicyType():int {
			return UploadPolicy.GROUP;
		}
	}
}