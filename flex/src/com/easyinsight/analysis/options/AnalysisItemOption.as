package com.easyinsight.analysis.options
{
	import analysis.AnalysisItem;
	
	public class AnalysisItemOption
	{
		private var _label:String;			
		
		public function AnalysisItemOption()
			{
			super();
			label = getLabelValue();
		}
		
		[Bindable]
		public function get label():String {
			return _label;
		}
		
		public function set label(label:String):void {
			this._label = label;
		}
		
		public function get data():Object {
			return this;
		}
		
		public function get value():Object {
			return label;
		}
		
		public function hasCustomContent():Boolean {
			return false;
		}
		
		public function createCustomContent(object:Object, caller:Function):Array {
			return null;
		}
		
		public function createAnalysisItem(key:String):AnalysisItem {
			return null;
		}
		
		protected function getLabelValue():String {
			return "Average";
		}			
	}
}