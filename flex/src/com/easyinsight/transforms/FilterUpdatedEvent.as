package com.easyinsight.transforms
{
	import analysis.FilterDefinition;
	
	import flash.events.Event;

	public class FilterUpdatedEvent extends Event
	{
		private var filterDefinition:FilterDefinition;
		
		public function FilterUpdatedEvent(filterDefinition:FilterDefinition)
		{
			this.filterDefinition = filterDefinition;
		}
		
		public function getFilterDefinition():void {
			return this.filterDefinition;
		}
	}
}