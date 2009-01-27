package com.easyinsight.util
{
	import com.easyinsight.feedassembly.EdgeEditEvent;
	
	import flash.events.MouseEvent;
	
	import mx.controls.VRule;
	import mx.core.UIComponent;

	public class GraphLine extends UIComponent
	{
		private var selected:Boolean = false;
		public var fromID:int;
		public var toID:int;
		private var color:uint = 0xFFFFFF;
		private var fromX:int;
		private var fromY:int;
		private var toX:int;
		private var toY:int;
		
		public function GraphLine(fromX:int, fromY:int, toX:int, toY:int, fromID:int, toID:int)
		{
			super();
			this.fromX = fromX;
			this.fromY = fromY;
			this.toX = toX;
			this.toY = toY;
			this.fromID = fromID;
			this.toID = toID;
			this.doubleClickEnabled = true;
			addEventListener(MouseEvent.DOUBLE_CLICK, editLine);
			addEventListener(MouseEvent.CLICK, selectLine);
		}


        public function get fromNodeID():int {
            return fromID;
        }
        public function get toNodeID():int {
            return toID;
        }
        public function updateCoordinates(fromX:int, fromY:int, toX:int, toY:int):void {
			this.fromX = fromX;
			this.fromY = fromY;
			this.toX = toX;
			this.toY = toY;
			invalidateDisplayList();	
		}
		
		private function editLine(event:MouseEvent):void {
			dispatchEvent(new EdgeEditEvent(String(this.fromID), String(this.toID)));
		}
		
		public function clearSelection():void {
			color = 0xFFFFFF;
			selected = false;
			invalidateDisplayList();
		}
		
		private function selectLine(event:MouseEvent):void {
			if (!selected) {
				color = 0xFF0000; 		 		
				selected = true;
				if (!event.ctrlKey) {
					dispatchEvent(new EdgeSelectionEvent(EdgeSelectionEvent.EDGE_SELECTED, this));
				} else {
					dispatchEvent(new EdgeSelectionEvent(EdgeSelectionEvent.EDGE_CTRL_SELECT, this));
				}
			} else {
				color = 0xFFFFFF;
				selected = false;				
				dispatchEvent(new EdgeSelectionEvent(EdgeSelectionEvent.EDGE_DESELECTED, this));				
			}
			
			invalidateDisplayList();
		}
		
		public function isSelected():Boolean {
			return selected;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(10, color, 0);
			graphics.moveTo(fromX, fromY);
			graphics.lineTo(toX, toY);
			graphics.lineStyle(3, color, .75);
			graphics.moveTo(fromX, fromY);
			graphics.lineTo(toX, toY);			
			var vrule:VRule;				
		}
	}
}