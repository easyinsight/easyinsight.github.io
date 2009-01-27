package com.easyinsight.listing
{
	import flash.events.Event;

	public class CatalogNavigationEvent extends Event
	{
		public static const RETURN_TO_STORE:String = "returnToStore";
		
		public function CatalogNavigationEvent(type:String)
		{
			super(type);
		}
		
		override public function clone():Event {
			return new CatalogNavigationEvent(RETURN_TO_STORE);
		}
	}
}