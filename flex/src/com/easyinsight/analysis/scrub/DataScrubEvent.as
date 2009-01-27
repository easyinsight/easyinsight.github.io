package com.easyinsight.analysis.scrub
{
	import flash.events.Event;

	public class DataScrubEvent extends Event
	{
		public static var ADDED_SCRUB:String = "addedScrub";
		public static var UPDATED_SCRUB:String = "updatedScrub";
		public static var DELETED_SCRUB:String = "deletedScrub";
		
		public var dataScrub:DataScrub;
		
		public function DataScrubEvent(type:String, dataScrub:DataScrub)
		{
			super(type, true);
			this.dataScrub = dataScrub;
		}
		
		override public function clone():Event {
			return new DataScrubEvent(type, dataScrub);
		}
	}
}