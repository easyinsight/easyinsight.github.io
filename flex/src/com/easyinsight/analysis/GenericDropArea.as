package com.easyinsight.analysis
{
	import mx.controls.List;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	
	public class GenericDropArea extends DropArea
	{							
		public function GenericDropArea()
		{
			super();
		}
				
		
		override protected function dragEnterHandler(event:DragEvent):void {
			var initialList:List = List(event.dragInitiator);	
			var targetCanvas:DropArea = DropArea(event.currentTarget);
			if (initialList.selectedItem is DataFeedItem) {
				var selectedObject:DataFeedItem = DataFeedItem(initialList.selectedItem);				
				DragManager.acceptDragDrop(targetCanvas);					
			}			
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Item Here";
		}
		
		override protected function dragDropHandler(event:DragEvent):void {
			trace("starting the polymorph process...");
			var initialList:List = List(event.dragInitiator);
			var selectedObject:DataFeedItem = DataFeedItem(initialList.selectedItem);
			var dropArea:DropArea;
			if (selectedObject.type == "Measure") {	
				dropArea = new MeasureDropArea();			
			} else {
				dropArea = new DimensionDropArea();
			}
			dropArea.dataFeedItem = selectedObject;
			dispatchEvent(new DropAreaPolymorphEvent(this, dropArea));
		}
	}
}