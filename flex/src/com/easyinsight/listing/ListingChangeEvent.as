package com.easyinsight.listing
{
	import flash.events.Event;

	public class ListingChangeEvent extends Event
	{
		public static const LISTING_CHANGE:String = "listingChange";
		public var perspective:IPerspective;
		
		public function ListingChangeEvent(perspective:IPerspective)
		{
			super(LISTING_CHANGE);
			this.perspective = perspective;
		}
		
		override public function clone():Event {
            return new ListingChangeEvent(perspective);
        }
	}
}