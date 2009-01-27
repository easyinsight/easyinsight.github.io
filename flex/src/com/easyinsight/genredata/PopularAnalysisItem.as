package com.easyinsight.genredata
{
	import com.easyinsight.analysis.AnalysisDefinition;
	
	import flash.events.MouseEvent;
	
	import com.easyinsight.listing.AnalysisDefinitionAnalyzeSource;
	
	import mx.controls.LinkButton;

	public class PopularAnalysisItem extends LinkButton
	{
		private var _data:AnalysisDefinition;
		
		public function PopularAnalysisItem()
		{
			super();
			addEventListener(MouseEvent.CLICK, analyze);
		}
		
		override public function set data(value:Object):void {						
			_data = value as AnalysisDefinition;
			if (_data != null) {
				this.label = _data.name;
			}			
		}
		
		override public function get data():Object {
			return _data;
		}
		
		private function analyze(event:MouseEvent):void {
			parent.dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(_data)));
		}				
	}
}