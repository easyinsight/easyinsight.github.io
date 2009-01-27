package com.easyinsight.customupload
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	public class UploadMetadataCompleteEvent extends Event
	{
		public static const UPLOAD_METADATA_COMPLETE:String = "uploadMetadataComplete";
		
		public var feedName:String;
		public var feedGenre:String;
		public var fields:ArrayCollection;
		public var feedID:int;		
		
		public function UploadMetadataCompleteEvent(feedName:String, feedGenre:String, fields:ArrayCollection, feedID:int)
		{
			super(UPLOAD_METADATA_COMPLETE);
			this.feedName = feedName;
			this.feedGenre = feedGenre;
			this.fields = fields;
			this.feedID = feedID;			
		}
		
		override public function clone():Event {
			return new UploadMetadataCompleteEvent(feedName, feedGenre, fields, feedID);
		}
	}
}