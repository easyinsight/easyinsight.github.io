package com.easyinsight.analysis
{
	import flash.events.Event;

	public class DefinitionMetadataRefreshEvent extends Event
	{
		public static const METADATA_REFRESH:String = "metadataRefresh";
		
		public function DefinitionMetadataRefreshEvent()
		{
			super(METADATA_REFRESH);
		}
		
		override public function clone():Event {
			return new DefinitionMetadataRefreshEvent();
		}
	}
}