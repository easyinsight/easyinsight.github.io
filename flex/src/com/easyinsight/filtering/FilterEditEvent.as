package com.easyinsight.filtering
{
	import flash.events.Event;

	public class FilterEditEvent extends Event
	{
		public static const FILTER_EDIT:String = "filterEdit";
		
		public var filterDefinition:FilterDefinition;
		public var previousFilterDefinition:FilterDefinition;
		
		public function FilterEditEvent(filterDefinition:FilterDefinition, previousFilterDefinition:FilterDefinition)
		{
			super(FILTER_EDIT);
			this.filterDefinition = filterDefinition;
			this.previousFilterDefinition = previousFilterDefinition;
		}
		
		override public function clone():Event {
			return new FilterEditEvent(filterDefinition, previousFilterDefinition);
		}
	}
}