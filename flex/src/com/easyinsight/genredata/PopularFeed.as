package com.easyinsight.genredata
{
	import flash.events.MouseEvent;
	
	import com.easyinsight.listing.DataFeedDescriptor;
	import com.easyinsight.listing.DescriptorAnalyzeSource;
	
	import mx.controls.LinkButton;

	public class PopularFeed extends LinkButton
	{
		private var _data:DataFeedDescriptor;
		
		public function PopularFeed()
		{
			super();
			addEventListener(MouseEvent.CLICK, analyze);
		}
		
		override public function set data(value:Object):void {						
			_data = value as DataFeedDescriptor;
			if (_data != null) {
				this.label = _data.name;
			}			
		}
		
		override public function get data():Object {
			return _data;
		}
		
		private function analyze(event:MouseEvent):void {
			parent.dispatchEvent(new ModuleAnalyzeEvent(new DescriptorAnalyzeSource(_data)));
		}		
		
	}
}