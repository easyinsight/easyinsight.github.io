package com.easyinsight.listing
{
	import flash.events.Event;

	public class HeaderBarEvent extends Event
	{
		public static const HEADER_BAR_UPDATE:String = "headerBarUpdate";
		
		public var allowAnalysis:Boolean = allowAnalysis;
		public var instantSearch:Boolean = instantSearch;
		
		public function HeaderBarEvent(allowAnalysis:Boolean, instantSearch:Boolean)
		{
			super(HEADER_BAR_UPDATE);
			this.allowAnalysis = allowAnalysis;
			this.instantSearch = instantSearch;
		}
		
		override public function clone():Event {
            return new HeaderBarEvent(allowAnalysis, instantSearch);
        }
	}
}