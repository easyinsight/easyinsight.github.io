package com.easyinsight.listing
{
	import flash.events.Event;

	public class ListingChangeEvent extends Event
	{
		public static const LISTING_CHANGE:String = "listingChange";
		public var perspective:IPerspective;
        public var properties:Object;
		
		public function ListingChangeEvent(perspective:IPerspective, properties:Object = null)
		{
			super(LISTING_CHANGE);
			this.perspective = perspective;
            this.properties = properties;
		}
		
		override public function clone():Event {
            return new ListingChangeEvent(perspective, properties);
        }
	}
}