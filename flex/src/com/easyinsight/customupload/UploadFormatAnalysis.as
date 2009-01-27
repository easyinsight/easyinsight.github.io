package com.easyinsight.customupload
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.UploadFormatAnalysis")]
	public class UploadFormatAnalysis
	{
		public var userUploadAnalysis:UserUploadAnalysis;
		public var uploadFormat:UploadFormat;
		
		public function UploadFormatAnalysis()
		{
		}

	}
}