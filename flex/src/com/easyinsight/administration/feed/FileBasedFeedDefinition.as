package com.easyinsight.administration.feed
{
	import com.easyinsight.customupload.UploadFormat;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.file.FileBasedFeedDefinition")]
	public class FileBasedFeedDefinition extends FeedDefinitionData
	{
		public var uploadFormat:UploadFormat;
		
		public function FileBasedFeedDefinition()
		{
			super();
		}
		
	}
}