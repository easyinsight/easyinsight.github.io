package com.easyinsight.analysis
{
import flash.events.EventDispatcher;

import mx.events.FlexEvent;

public class AnalysisItemWrapper extends EventDispatcher
	{
		private var _analysisItem:AnalysisItem;
		private var _displayName:String;
		
		public function AnalysisItemWrapper(analysisItem:AnalysisItem)	{
			this._analysisItem = analysisItem;
			this._displayName = analysisItem.display;
		}

        public function get keyName():String {
            return _analysisItem.key.createString();
        }
		
		[Bindable]
		public function set displayName(displayName:String):void {
			this._displayName = displayName;
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
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