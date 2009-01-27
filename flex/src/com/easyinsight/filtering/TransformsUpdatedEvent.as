package com.easyinsight.filtering
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	public class TransformsUpdatedEvent extends Event
	{
		public static const UPDATED_TRANSFORMS:String = "updatedTransforms";
		[Bindable]
		public var filterDefinitions:ArrayCollection;
		
		public function TransformsUpdatedEvent(filterDefinitions:ArrayCollection)
		{
			super(UPDATED_TRANSFORMS);
			this.filterDefinitions = filterDefinitions;
		}
		
	}
}