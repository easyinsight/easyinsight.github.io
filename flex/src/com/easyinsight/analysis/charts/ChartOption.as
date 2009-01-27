package com.easyinsight.analysis.charts
{
	public class ChartOption
	{
		private var _configClass:Class;
		private var _icon:Class;
		private var _label:String;
		
		public function ChartOption()
		{
		}

		public function set configurationClass(configClass:Class):void {
			this._configClass = configClass;	
		}
		
		public function get configuration():ChartConfiguration {
			return new _configClass();
		}
		
		public function set icon(iconClass:Class):void {
			this._icon = iconClass;
		}
		
		public function get icon():Class {
			return this._icon;
		}
		
		public function get label():String {
			return this._label;
		}
		
		public function set label(label:String):void {
			this._label = label;
		}		
	}
}