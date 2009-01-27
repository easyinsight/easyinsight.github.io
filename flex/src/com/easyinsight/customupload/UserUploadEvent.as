package com.easyinsight.customupload
{
	import flash.events.Event;

	public class UserUploadEvent extends Event
	{
		public var uploadID:Number;
		public static const USER_UPLOAD_COMPLETE:String = "userUploadComplete";
		public static const TYPE_ASSIGNMENT_COMPLETE:String = "typeAssignmentComplete";
		
		public function UserUploadEvent(type:String, uploadID:Number)
		{
			super(type);
			this.uploadID = uploadID;
		}
		
	}
}