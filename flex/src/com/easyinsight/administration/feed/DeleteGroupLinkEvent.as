package com.easyinsight.administration.feed
{
	import flash.events.Event;

	public class DeleteGroupLinkEvent extends Event
	{
		public static const DELETE_GROUP_LINK:String = "deleteGroupLink";
		
		public function DeleteGroupLinkEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}