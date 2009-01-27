package com.easyinsight.listing
{
	import flash.events.Event;

	public class KeywordSearchEvent extends Event
	{
		public static const KEYWORD_SEARCH:String = "keywordSearch";
		public var keyword:String;
		
		public function KeywordSearchEvent(keyword:String)
		{
			super(KEYWORD_SEARCH);
			this.keyword = keyword;
		}
		
	}
}