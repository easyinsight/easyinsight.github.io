package com.easyinsight.customupload
{
	import com.easyinsight.administration.feed.FeedDefinitionData;

	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.file.FileBasedFeedDefinition")]
	public class FileBasedFeedDefinition extends FeedDefinitionData
	{
		public var uploadFormat:UploadFormat;
		
		public function FileBasedFeedDefinition()
		{
			super();
		}


        override public function allowFieldEdit():Boolean {
            return true;
        }
    }
}