package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.UploadResponse")]
	public class UploadResponse
	{
		public var successful:Boolean;
		public var failureMessage:String;
		public var feedID:int;
		public var analysisID:int;
		
		public function UploadResponse()
		{
		}

	}
}