package com.easyinsight.customupload
{
	import flash.events.Event;

	public class UploadConfigEvent extends Event
	{
		public static const UPLOAD_CONFIG_COMPLETE:String = "userConfigComplete";
		public static const UPLOAD_CONFIG_CANCEL:String = "userConfigCancel";
		
		public var feedID:int;
        public var name:String;
		
		public function UploadConfigEvent(type:String, feedID:int = 0, name:String = null)
		{
			super(type, true);
			this.feedID = feedID;
            this.name = name;
		}
		
		override public function clone():Event {
			return new UploadConfigEvent(type, feedID, name);
		}
	}
}