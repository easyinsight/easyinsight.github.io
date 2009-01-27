package com.easyinsight.analysis
{
	import com.easyinsight.commands.ICommand;

	public class DropAreaAddedCommand implements ICommand
	{
		private var dropArea:DropArea;
		private var analysisItem:AnalysisItem;
        private var dropAreaGrouping:ListDropAreaGrouping;
        private var dropAreaGroupingState:Array;
		
		public function DropAreaAddedCommand(dropArea:DropArea, analysisItem:AnalysisItem) {
			this.dropArea = dropArea;
			this.analysisItem = analysisItem;
            this.dropAreaGrouping = dropArea.parent as ListDropAreaGrouping;
			this.dropAreaGroupingState = dropAreaGrouping.getChildrenState();
		}

		public function execute():void
		{
            this.dropAreaGrouping.applyChildState(dropAreaGroupingState);
			dropArea.analysisItem = analysisItem;
			dropArea.dispatchEvent(new DropAreaAddedEvent(analysisItem));
		}
		
		public function undo():Boolean
		{
            //dropArea.analysisItem = null;
			dropArea.deletion();
			return true;
		}
		
	}
}