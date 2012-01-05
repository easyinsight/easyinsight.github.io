package com.easyinsight.solutions
{


import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;

	public class SolutionFeedIcons extends HBox
	{

        
        private var feedDescriptor:DataSourceDescriptor;
        
        private var deleteButton:Button;
        
		public function SolutionFeedIcons()
		{
			super();
			deleteButton = new Button();
			deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
			deleteButton.toolTip = "Delete";
			deleteButton.addEventListener(MouseEvent.CLICK, deleteCalled);
			addChild(deleteButton);
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
		}
		
		private function deleteCalled(event:MouseEvent):void {
			dispatchEvent(new DataSourceSolutionEvent(DataSourceSolutionEvent.DELETE_DATA_SOURCE, feedDescriptor));
		}
		
		override public function set data(value:Object):void {
			this.feedDescriptor = value as DataSourceDescriptor;
		}
		
		override public function get data():Object {
			return this.feedDescriptor;
		}
	}
}