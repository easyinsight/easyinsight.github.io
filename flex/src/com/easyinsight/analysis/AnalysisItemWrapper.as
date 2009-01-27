package com.easyinsight.analysis
{
	public class AnalysisItemWrapper
	{
		private var _analysisItem:AnalysisItem;
		private var _displayName:String;
		
		public function AnalysisItemWrapper(analysisItem:AnalysisItem)	{
			this._analysisItem = analysisItem;
			this._displayName = analysisItem.display;
		}
		
		[Bindable]
		public function set displayName(displayName:String):void {
			this._displayName = displayName;
		}

		public function get displayName():String {
			return this._displayName;
		}
		
		public function get analysisItem():AnalysisItem {
			return _analysisItem;
		}
		
		public function set analysisItem(analysisItem:AnalysisItem):void {
			this._analysisItem = analysisItem;
		}
	}
}