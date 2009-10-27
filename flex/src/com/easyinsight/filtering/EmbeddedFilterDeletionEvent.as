package com.easyinsight.filtering
{
	import flash.events.Event;

	public class EmbeddedFilterDeletionEvent extends Event
	{
		public static const DELETED_FILTER:String = "deletedFilter";
		private var filter:IEmbeddedFilter;
		
		public function EmbeddedFilterDeletionEvent(filter:IEmbeddedFilter)
		{
			super(DELETED_FILTER);
			this.filter = filter;
		}
		
		public function getFilter():IEmbeddedFilter {
			return this.filter;
		}
	}
}