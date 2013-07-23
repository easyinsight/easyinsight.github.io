package com.easyinsight.filtering
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	public class TransformsUpdatedEvent extends Event
	{
		public static const UPDATED_TRANSFORMS:String = "updatedTransforms";

		public var filterDefinitions:ArrayCollection;
        public var rerender:Boolean = true;
		
		public function TransformsUpdatedEvent(filterDefinitions:ArrayCollection, rerender:Boolean = true)
		{
			super(UPDATED_TRANSFORMS);
			this.filterDefinitions = filterDefinitions;
            this.rerender = rerender;
		}


        override public function clone():Event {
            return new TransformsUpdatedEvent(filterDefinitions, rerender);
        }
    }
}