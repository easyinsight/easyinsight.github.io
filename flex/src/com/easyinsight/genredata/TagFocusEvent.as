package com.easyinsight.genredata
{
	import flash.events.Event;

	public class TagFocusEvent extends Event
	{
		public static var TAG_FOCUS:String = "tagFocus";
		
		public var tag:String;
		
		public function TagFocusEvent(tag:String)
		{
			super(TAG_FOCUS);
			this.tag = tag;
		}
		
		override public function clone():Event {
			return new TagFocusEvent(tag);
		}
	}
}