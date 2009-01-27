package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.PublicUploadPolicy")]
	public class PublicUploadPolicy extends UploadPolicy
	{
		public function PublicUploadPolicy()
		{
			super();
		}
		
		override public function getPolicyType():int {
			return UploadPolicy.PUBLIC;
		}
	}
}