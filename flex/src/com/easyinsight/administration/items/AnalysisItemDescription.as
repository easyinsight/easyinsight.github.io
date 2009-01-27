package com.easyinsight.administration.items
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.options.OptionFactory;
	
	import mx.controls.Label;

	public class AnalysisItemDescription extends Label
	{
		private var analysisItem:AnalysisItem;
		
		public function AnalysisItemDescription()
		{
			super();
		}
		
		override public function set data(object:Object):void {
			this.analysisItem = object as AnalysisItem;
			if (this.analysisItem != null) {
				this.text = OptionFactory.getAnalysisItemOption(analysisItem);
			}
		}
		
		override public function get data():Object {
			return this.analysisItem;
		}
	}
}