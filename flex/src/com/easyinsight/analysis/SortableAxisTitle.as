package com.easyinsight.analysis
{
	import mx.containers.Box;
	import mx.controls.Label;
	import mx.core.IDataRenderer;

	public class SortableAxisTitle extends Box implements IDataRenderer
	{
		private var myText:Label;
		private var value:Object;
		private var sortButton:ChartSortButton;
		private var _analysisItem:AnalysisItem;
		private var _axisDirection:Boolean;


        public function get analysisItem():AnalysisItem {
            return _analysisItem;
        }

        public function set analysisItem(val:AnalysisItem):void {
            _analysisItem = val;
            if (sortButton != null) {
                sortButton.analysisItem = val;
            }
        }

        public function get axisDirection():Boolean {
            return _axisDirection;
        }

        public function set axisDirection(val:Boolean):void {
            _axisDirection = val;
        }

        public function SortableAxisTitle()
		{
			super();
			//if (horizontal) {
				direction = "horizontal";
			//} else {
			//	direction = "vertical";
			//}
			setStyle("color", "#000000");
			this.horizontalScrollPolicy = "off";
			this.verticalScrollPolicy = "off";
		}
		
		override public function get data():Object {
			return value;
		}
		
		override public function set data(value:Object):void {
			this.value = value;
			myText.text = String(value);
			invalidateSize();
			invalidateDisplayList();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			myText = new Label();
			myText.setStyle("color", "#000000");
			myText.setStyle("fontSize", 14);			
			addChild(myText);
			sortButton = new ChartSortButton();
            if (analysisItem != null) {
                sortButton.analysisItem = analysisItem;
            }
			addChild(sortButton);
		}
		
		override protected function measure():void {
			myText.validateNow();
			sortButton.validateNow();
			//if (axisDirection) {			
				measuredWidth = myText.measuredMinWidth + sortButton.measuredMinWidth + 20;
				measuredHeight = myText.measuredMinHeight;
			//} else {
			//	measuredWidth = myText.measuredMinWidth + 40;
			//	measuredHeight = myText.measuredMinHeight + sortButton.measuredMinHeight;
			//}
			height = measuredHeight;
			width = measuredWidth;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			/*if(data.hasOwnProperty('text')) {
                myText.text = data.text;
            } else {
                myText.text = data.toString();        
            }*/
            
            if (!axisDirection) {
            	myText.rotation = 180;
            	myText.y = myText.y + myText.height;
            	myText.x = myText.x + myText.width;
            }
            
            myText.validateNow();
            myText.setActualSize(unscaledWidth, unscaledHeight);
            
            
            
            //this.setStyle("textAlign","center");
		}
	}
}