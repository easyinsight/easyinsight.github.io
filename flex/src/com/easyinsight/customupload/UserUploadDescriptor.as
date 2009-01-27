package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.dataproviders.userupload.UserUploadDescriptor")]
	public class UserUploadDescriptor
	{
		public var name:String;
		public var fileName:String;
		public var dataFeedID:int;
		
		public function UserUploadDescriptor()
			{
			super();
		}

	}
}