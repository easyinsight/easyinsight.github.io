package com.easyinsight.customupload
{
	import flash.events.Event;

	public class UploadConfigEvent extends Event
	{
		public static const UPLOAD_CONFIG_COMPLETE:String = "userConfigComplete";
		public static const UPLOAD_CONFIG_CANCEL:String = "userConfigCancel";
		
		public var feedID:int;
        public var name:String;
        public var startInAdmin:Boolean;
		
		public function UploadConfigEvent(type:String, feedID:int = 0, name:String = null, startInAdmin:Boolean = false)
		{
			super(type, true);
			this.feedID = feedID;
            this.name = name;
            this.startInAdmin = startInAdmin;
		}
		
		override public function clone():Event {
			return new UploadConfigEvent(type, feedID, name, startInAdmin);
		}
	}
}