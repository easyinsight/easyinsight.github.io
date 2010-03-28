package com.easyinsight.customupload
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.UploadResponse")]
	public class UploadResponse
	{
		public var successful:Boolean;
		public var failureMessage:String;
		public var feedID:int;
        public var infos:ArrayCollection;
		
		public function UploadResponse()
		{
		}

	}
}