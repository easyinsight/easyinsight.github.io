package com.easyinsight.google
{
	import com.easyinsight.listing.DataFeedDescriptor;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.google.Worksheet")]
	public class Worksheet
	{
		public var title:String;
		public var url:String;
		public var spreadsheet:String;
		public var feedDescriptor:DataFeedDescriptor;
		
		public function Worksheet()
		{
		}

	}
}