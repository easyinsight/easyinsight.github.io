package com.easyinsight.customupload
{
	import flash.events.Event;

	public class UploadConfigEvent extends Event
	{
		public static const UPLOAD_CONFIG_COMPLETE:String = "userConfigComplete";
		public static const UPLOAD_CONFIG_CANCEL:String = "userConfigCancel";
		
		public var feedID:int;
		
		public function UploadConfigEvent(type:String, feedID:int = 0)
		{
			super(type, true);
			this.feedID = feedID;
		}
		
		override public function clone():Event {
			return new UploadConfigEvent(type, feedID);
		}
	}
}