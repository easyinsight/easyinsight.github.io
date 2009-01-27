package com.easyinsight.customupload
{
	import flash.events.Event;

	public class UploadCancelEvent extends Event
	{
		public static const UPLOAD_CANCEL:String = "uploadCancel";
		
		public function UploadCancelEvent()
		{
			super(UPLOAD_CANCEL);
		}
		
		override public function clone():Event {
			return new UploadCancelEvent();
		}
	}
}