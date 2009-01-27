package com.easyinsight.analysis
{
	import com.easyinsight.commands.ICommand;

	public class DropAreaDragUpdateCommand implements ICommand
	{
		private var dropArea:DropArea;
		private var previousAnalysisItem:AnalysisItem;
		private var newAnalysisItem:AnalysisItem;
		
		public function DropAreaDragUpdateCommand(dropArea:DropArea, previousAnalysisItem:AnalysisItem, 
			newAnalysisItem:AnalysisItem) {
			this.dropArea = dropArea;
			this.previousAnalysisItem = previousAnalysisItem;
			this.newAnalysisItem = newAnalysisItem;
		}

		public function execute():void {
			this.dropArea.analysisItem = newAnalysisItem;
			this.dropArea.dispatchEvent(new DropAreaUpdateEvent(newAnalysisItem, previousAnalysisItem));			
		}
		
		public function undo():Boolean {
			this.dropArea.analysisItem = previousAnalysisItem;
			this.dropArea.dispatchEvent(new DropAreaUpdateEvent(previousAnalysisItem));
			return true;
		}

	}
}