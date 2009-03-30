package com.easyinsight.genredata
{
    import com.easyinsight.solutions.InsightDescriptor;
    import flash.events.MouseEvent;
	
	import com.easyinsight.listing.AnalysisDefinitionAnalyzeSource;
	
	import mx.controls.LinkButton;

	public class PopularAnalysisItem extends LinkButton
	{
		private var _data:InsightDescriptor;
		
		public function PopularAnalysisItem()
		{
			super();
			addEventListener(MouseEvent.CLICK, analyze);
		}
		
		override public function set data(value:Object):void {						
			_data = value as InsightDescriptor;
			if (_data != null) {
				this.label = _data.name;
			}			
		}
		
		override public function get data():Object {
			return _data;
		}
		
		private function analyze(event:MouseEvent):void {
			parent.dispatchEvent(new ModuleAnalyzeEvent(new AnalysisDefinitionAnalyzeSource(_data)));
		}				
	}
}