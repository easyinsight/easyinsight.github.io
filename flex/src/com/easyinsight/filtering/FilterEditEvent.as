package com.easyinsight.filtering
{
	import flash.events.Event;

	public class FilterEditEvent extends Event
	{
		public static const FILTER_EDIT:String = "filterEdit";
		
		public var filterDefinition:FilterDefinition;
		public var previousFilterDefinition:FilterDefinition;
        public var editorClass:Class;
        public var rebuild:Boolean;
		
		public function FilterEditEvent(filterDefinition:FilterDefinition, previousFilterDefinition:FilterDefinition, editorClass:Class = null,
                rebuild:Boolean = false)
		{
			super(FILTER_EDIT);
			this.filterDefinition = filterDefinition;
			this.previousFilterDefinition = previousFilterDefinition;
            this.editorClass = editorClass;
            this.rebuild = rebuild;
		}
		
		override public function clone():Event {
			return new FilterEditEvent(filterDefinition, previousFilterDefinition, editorClass, rebuild);
		}
	}
}