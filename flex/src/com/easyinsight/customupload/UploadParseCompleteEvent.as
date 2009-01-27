package com.easyinsight.customupload
{
	import flash.events.Event;

	public class UploadParseCompleteEvent extends Event
	{
		public static const UPLOAD_PARSE_COMPLETE:String = "uploadParseComplete";
		
		public var uploadID:Number;
		public var uploadFormat:UploadFormat;
		
		public function UploadParseCompleteEvent(uploadID:Number, uploadFormat:UploadFormat) {
			super(UPLOAD_PARSE_COMPLETE);
			this.uploadID = uploadID;
			this.uploadFormat = uploadFormat;
		}
		
		override public function clone():Event {
			return new UploadParseCompleteEvent(uploadID, uploadFormat);
		}
	}
}