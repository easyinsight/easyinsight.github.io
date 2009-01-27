package com.easyinsight.filtering
{
	import flash.events.Event;

	public class FilterDeletionEvent extends Event
	{
		public static const DELETED_FILTER:String = "deletedFilter";
		private var filter:IFilter;
		
		public function FilterDeletionEvent(filter:IFilter)
		{
			super(DELETED_FILTER);
			this.filter = filter;
		}
		
		public function getFilter():IFilter {
			return this.filter;
		}
	}
}