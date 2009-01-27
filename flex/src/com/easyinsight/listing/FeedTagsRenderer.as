package com.easyinsight.listing
{
	import mx.controls.Label;

	public class FeedTagsRenderer extends Label
	{
		private var descriptor:DataFeedDescriptor;
		
		public function FeedTagsRenderer()
		{
			super();
		}
		
		override public function set data(value:Object):void {
			this.descriptor = value as DataFeedDescriptor;
			descriptor.	
		}
		
		override public function get data():Object {
			return this.descriptor;
		}
	}
}