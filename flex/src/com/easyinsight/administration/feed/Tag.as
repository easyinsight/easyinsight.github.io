package com.easyinsight.administration.feed
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.Tag")]
	public class Tag
	{
		public var tagID:Number;
		public var tagName:String;
		public var useCount:int;
		
		public function Tag()
		{
		}

	}
}