package com.easyinsight.listing
{
	import com.easyinsight.customupload.UploadPolicy;
	import com.easyinsight.genredata.AnalyzeEvent;
	
	import flash.events.MouseEvent;
	
	import mx.controls.Button;

	public class FeedAnalyzeButton extends Button
	{
		private var _data:DataFeedDescriptor;
		private var preview:Boolean;
		
		public function FeedAnalyzeButton()
		{
			super();
			label = "Analyze";
			addEventListener(MouseEvent.CLICK, analyze); 
		}
		
		override public function set data(value:Object):void {			
			var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;			
			_data = descriptor;
			preview = (descriptor.role != DataFeedDescriptor.OWNER && descriptor.role != DataFeedDescriptor.SUBSCRIBER); 
			if (preview) {
				label = "Preview";
			}
		}
		
		override public function get data():Object {
			return _data;
		}
		
		private function analyze(event:MouseEvent):void {
			dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(_data, preview)));
		}
	}
}