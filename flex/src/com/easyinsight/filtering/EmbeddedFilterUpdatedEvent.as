package com.easyinsight.filtering
{
	import flash.events.Event;

	public class EmbeddedFilterUpdatedEvent extends Event
	{
		public static const FILTER_ADDED:String = "filterAdded";
		public static const FILTER_UPDATED:String = "filterUpdated";
		public var filterDefinition:FilterDefinition;
		public var previousFilterDefinition:FilterDefinition;
		public var filter:IEmbeddedFilter;
        public var rebuild:Boolean;
		
		public function EmbeddedFilterUpdatedEvent(type:String, filterDefinition:FilterDefinition, previousFilterDefinition:FilterDefinition,
			filter:IEmbeddedFilter, bubbles:Boolean = false, rebuild:Boolean = false)
		{
			super(type, bubbles);
			this.filterDefinition = filterDefinition;
			this.previousFilterDefinition = previousFilterDefinition;
			this.filter = filter;
            this.rebuild = rebuild;
		}
		
		override public function clone():Event {
			return new EmbeddedFilterUpdatedEvent(type, filterDefinition, previousFilterDefinition, filter, rebuild);
		}
	}
}