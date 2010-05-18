package com.easyinsight.analysis
{
	import flash.events.MouseEvent;
	
	import mx.controls.Button;

	public class ChartSortButton extends Button
	{
		public static var NONE:int = 0;
		public static var ASCENDING:int = 1;
		public static var DESCENDING:int = 2;
		
		private var sortState:int = NONE;
		private var _analysisItem:AnalysisItem;
		
		[Embed(source="../../../../assets/sort_up_down.png")]
        public var noneIcon:Class;
        
        [Embed(source="../../../../assets/sort_ascending.png")]
        public var sortUpIcon:Class;
        
        [Embed(source="../../../../assets/sort_descending.png")]
        public var sortDownIcon:Class;
		
		public function ChartSortButton() {
			super();
			addEventListener(MouseEvent.CLICK, sortClick);
		}

        private function updateIcon():void {
            if (sortState == NONE) {
				setStyle("icon", noneIcon);
			} else if (sortState == ASCENDING) {
				this.setStyle("icon", sortUpIcon);
			} else if (sortState == DESCENDING) {
				this.setStyle("icon", sortDownIcon);
			}
        }

        public function set analysisItem(val:AnalysisItem):void {
            _analysisItem = val;
            this.sortState = val.sort;
            updateIcon();
        }

        private function sortClick(event:MouseEvent):void {
			if (sortState == NONE) {
				sortState = ASCENDING;
			} else if (sortState == ASCENDING) {
				sortState = DESCENDING;
			} else if (sortState == DESCENDING) {
				sortState = NONE;
			}
            updateIcon();
			_analysisItem.sort = sortState;
			dispatchEvent(new ChartSortEvent(_analysisItem, sortState));
		}
	}
}