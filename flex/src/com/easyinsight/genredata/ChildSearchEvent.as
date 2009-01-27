package com.easyinsight.genredata
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;

	public class ChildSearchEvent extends Event
	{
		public static const FEED_SEARCH:String = "feedSearch";
		public static const ANALYSIS_SEARCH:String = "analysisSearch";
		public var items:ArrayCollection;
		
		public function ChildSearchEvent(type:String, items:ArrayCollection)
		{
			super(type);
			this.items = items;
		}		
	}
}