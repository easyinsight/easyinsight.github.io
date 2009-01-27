package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemTypes;
	
	public class SliderFilterContainer
	{
		public function SliderFilterContainer() {
			this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
			this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
		}

		protected function dragEnterHandler(event:DragEvent):void {
			var initialList:List = List(event.dragInitiator);						
			var selectedObject:AnalysisItem = AnalysisItem(initialList.selectedItem);
			if (selectedObject.hasType(AnalysisItemTypes.MEASURE || selectedObject.hasType(AnalysisItemTypes.DATE) {
				DragManager.acceptDragDrop(this);
			}	
		}
		
		protected function dragDropHandler(event:DragEvent):void {
			
		}

		public function addAnalysisItem(analysisItem:AnalysisItem):void {
			
		}
	}
}