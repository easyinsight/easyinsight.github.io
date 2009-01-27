package com.easyinsight.util
{
	import flash.events.Event;

	public class EdgeDeletionEvent extends Event
	{
		public static const EDGE_DELETED:String = "edgeDeleted";
		
		public function EdgeDeletionEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}